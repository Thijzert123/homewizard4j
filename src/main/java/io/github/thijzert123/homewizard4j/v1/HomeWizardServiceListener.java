package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
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
        if (WaterMeter.PRODUCT_TYPES.contains(productType)) {
            addWaterMeter(serviceInfo);
        } else if (P1Meter.PRODUCT_TYPES.contains(productType)) {
            addP1Meter(serviceInfo);
        } else if (EnergySocket.PRODUCT_TYPES.contains(productType)) {
            addEnergySocket(serviceInfo);
        } else if (KWhMeter.PRODUCT_TYPES.contains(productType)) {
            addKWhMeter(serviceInfo);
        }

        // Notify the discoverer that a new device has been added
        synchronized (discoverer.deviceAddedNotifier) {
            discoverer.deviceAddedNotifier.notifyAll();
        }
    }

    private Object createDevice(final Class<?> clazz, final ServiceInfo serviceInfo) {
        LOGGER.trace("Creating device, class name: '{}', service info: '{}'", clazz.getName(), serviceInfo);
        try {
            return clazz.getDeclaredConstructor(
                    Optional.class,
                    boolean.class,
                    String.class,
                    int.class,
                    String.class,
                    Optional.class,
                    Optional.class,
                    Optional.class
            ).newInstance(
                    Optional.of(serviceInfo.getQualifiedName()),
                    Objects.equals(serviceInfo.getPropertyString("api_enabled"), "1"),
                    serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                    serviceInfo.getPort(),
                    serviceInfo.getPropertyString("path"),
                    Optional.of(serviceInfo.getPropertyString("product_type")),
                    Optional.of(serviceInfo.getPropertyString("product_name")),
                    Optional.of(serviceInfo.getPropertyString("serial"))
            );
        } catch (final InstantiationException |
                       IllegalAccessException |
                       InvocationTargetException |
                       NoSuchMethodException exception) {
            LOGGER.error("Fatal error while creating device, please report this to the library developer(s)", exception);
            throw new RuntimeException(exception);
        }
    }

    private void addWaterMeter(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding WaterMeter...");
        discoverer.waterMeters.add((WaterMeter) createDevice(WaterMeter.class, serviceInfo));
    }

    private void addP1Meter(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding P1Meter...");
        discoverer.p1Meters.add((P1Meter) createDevice(P1Meter.class, serviceInfo));
    }

    private void addEnergySocket(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding EnergySocket...");
        discoverer.energySockets.add((EnergySocket) createDevice(EnergySocket.class, serviceInfo));
    }

    private void addKWhMeter(final ServiceInfo serviceInfo) {
        LOGGER.trace("Adding KWhMeter...");
        discoverer.kWhMeters.add((KWhMeter) createDevice(KWhMeter.class, serviceInfo));
    }
}
