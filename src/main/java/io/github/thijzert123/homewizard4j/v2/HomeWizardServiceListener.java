package io.github.thijzert123.homewizard4j.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * @author Thijzert123
 */
class HomeWizardServiceListener implements ServiceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HomeWizardDiscoverer discoverer;

    HomeWizardServiceListener(final HomeWizardDiscoverer discoverer) {
        this.discoverer = discoverer;
    }

    @Override
    public void serviceAdded(final ServiceEvent serviceEvent) {
        LOGGER.trace("Service added: {}", serviceEvent.getInfo());
    }

    @Override
    public void serviceRemoved(final ServiceEvent serviceEvent) {
        LOGGER.trace("Service removed: {}", serviceEvent.getInfo());
    }

    @Override
    public void serviceResolved(final ServiceEvent serviceEvent) {
        LOGGER.trace("Service resolved: {}", serviceEvent.getInfo());

        final ServiceInfo serviceInfo = serviceEvent.getInfo();
        final String productType = serviceInfo.getPropertyString("product_type");
        LOGGER.debug("Discovered device, product type: {}", productType);

        if (isSerialRegistered(serviceInfo.getPropertyString("serial"))) {
            return;
        }

        if (P1Meter.PRODUCT_TYPES.contains(productType)) {
            addP1Meter(serviceInfo);
        } else if (PlugInBattery.PRODUCT_TYPES.contains(productType)) {
            addPlugInBattery(serviceInfo);
        }

        // Notify the discoverer that a new device has been added
        synchronized (discoverer.deviceAddedNotifier) {
            discoverer.deviceAddedNotifier.notifyAll();
        }
    }

    /**
     * Check if device is already registered in the discoverer. This is necessary because otherwise a discoverer
     * can scan the same device twice if it was merged.
     *
     * @param serial serial to check
     * @return whether the serial is registered in the discoverer
     */
    private boolean isSerialRegistered(final String serial) {
        LOGGER.trace("Checking if serial is already registered...");

        final List<Device> devices = discoverer.getAllDevices();
        for (final Device device : devices) {
            if (device.getSerial().isPresent()) {
                // is serial the same as serial to check?
                if (device.getSerial().get().equals(serial)) {
                    LOGGER.trace("Serial was already registered");
                    return true;
                }
            }
        }
        LOGGER.trace("Serial is not registered");
        return false;
    }

    private Object createDevice(final Class<?> clazz, final ServiceInfo serviceInfo) {
        LOGGER.trace("Creating device, class name: '{}', service info: '{}'", clazz.getName(), serviceInfo);
        try {
            return clazz.getDeclaredConstructor(
                    Optional.class,
                    String.class,
                    int.class,
                    Optional.class,
                    Optional.class,
                    Optional.class,
                    Optional.class,
                    Optional.class
            ).newInstance(
                    Optional.of(serviceInfo.getQualifiedName()),
                    serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                    serviceInfo.getPort(),
                    Optional.of(serviceInfo.getPropertyString("product_type")),
                    Optional.of(serviceInfo.getPropertyString("product_name")),
                    Optional.of(serviceInfo.getPropertyString("serial")),
                    Optional.of(serviceInfo.getPropertyString("id")),
                    Optional.of(serviceInfo.getPropertyString("api_version"))
            );
        } catch (final InstantiationException |
                       IllegalAccessException |
                       InvocationTargetException |
                       NoSuchMethodException exception) {
            LOGGER.error("Fatal error while creating device, please report this to the library developer(s)", exception);
            throw new RuntimeException(exception);
        }
    }

    private void addP1Meter(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding P1 meter...");
        discoverer.p1Meters.add((P1Meter) createDevice(P1Meter.class, serviceInfo));
    }

    private void addPlugInBattery(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding plug-in battery...");
        discoverer.plugInBatteries.add((PlugInBattery) createDevice(PlugInBattery.class, serviceInfo));
    }
}
