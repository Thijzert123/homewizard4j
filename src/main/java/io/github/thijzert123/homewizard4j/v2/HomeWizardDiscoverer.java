package io.github.thijzert123.homewizard4j.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Discovers HomeWizard devices using mDNS.
 * When you initialize the class, a mDNS scanner is made with {@link JmDNS}. It scans specifically for HomeWizard devices.
 * Before calling one of the getters, you should wait for about a second. If you have slow internet speeds,
 * you might need to wait for an even longer time.
 * @author Thijzert123
 */
public class HomeWizardDiscoverer implements AutoCloseable {
    public enum DeviceType {
        ALL,
        P1_METER,
        PLUG_IN_BATTERY
    }

    /**
     * Full service type. If a device on your local network has this service type, this discoverer will detect and register it.
     */
    public static final String SERVICE_TYPE = "_homewizard._tcp.local.";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final JmDNS jmDNS;

    final Object deviceAddedNotifier = new Object();

    final List<P1Meter> p1Meters;
    final List<PlugInBattery> plugInBatteries;

    public HomeWizardDiscoverer() throws IOException {
        LOGGER.trace("Initializing HomeWizardDiscoverer...");

        p1Meters = new ArrayList<>();
        plugInBatteries = new ArrayList<>();

        jmDNS = JmDNS.create(InetAddress.getLocalHost());
        jmDNS.addServiceListener(SERVICE_TYPE, new HomeWizardServiceListener(this));
    }

    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     * It adds the devices from the provided discoverer.
     *
     * @param discovererToMerge discoverer to merge with this one
     * @throws IOException when something has gone wrong while creating the mDNS discoverer
     */
    public HomeWizardDiscoverer(final HomeWizardDiscoverer discovererToMerge) throws IOException {
        this();
        p1Meters.addAll(discovererToMerge.p1Meters);
        plugInBatteries.addAll(discovererToMerge.plugInBatteries);
    }


    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     * It blocks for the specified amount of time and closes the listener.
     * This is done by calling {@link #waitForMillis(long)} and {@link #close()}.
     *
     * @param millis time in millis
     * @throws IOException when something has gone wrong while creating or closing the mDNS discoverer
     */
    public HomeWizardDiscoverer(final long millis) throws IOException {
        this();
        waitForMillis(millis);
        close();
    }

    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     * It blocks until a specific count of the specified device type is reached and closes after.
     * This is done by calling {@link #waitForDevices(DeviceType, int)} and {@link #close()}.
     *
     * @param deviceType type of device to wait for
     * @param deviceCount device count to wait for
     * @throws IOException when something has gone wrong while creating or closing the mDNS discoverer
     */
    public HomeWizardDiscoverer(final DeviceType deviceType, final int deviceCount) throws IOException {
        this();
        waitForDevices(deviceType, deviceCount);
        close();
    }

    /**
     * Blocks for a specified time.
     *
     * @param millis time in millis
     * @return the current discoverer
     */
    public HomeWizardDiscoverer waitForMillis(final long millis) {
        LOGGER.debug("Blocking for {} millis", millis);
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException interruptedException) {
            LOGGER.error(interruptedException.getMessage(), interruptedException);
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * Blocks until a specific cound of a specified device type is reached.
     *
     * @param deviceType  type of device to wait for
     * @param deviceCount device count to wait for
     * @return the current discoverer
     */
    public HomeWizardDiscoverer waitForDevices(final DeviceType deviceType, final int deviceCount) {
        LOGGER.debug("Blocking until device count is {}, including all devices", deviceCount);
        synchronized (deviceAddedNotifier) {
            while (getDevices(deviceType).size() < deviceCount) {
                LOGGER.trace("Still blocking, waiting for device count to go up");
                try {
                    deviceAddedNotifier.wait();
                } catch (final InterruptedException interruptedException) {
                    LOGGER.error(interruptedException.getMessage(), interruptedException);
                    Thread.currentThread().interrupt();
                }
            }
        }
        LOGGER.trace("Device count {} reached, stopped blocking", deviceCount);

        return this;
    }

    /**
     * Returns a {@link List} of discovered  devices with the specified type.
     *
     * @param deviceType type to include in the returned list
     * @return {@link List} of discovered devices with the specified type
     */
    public List<? extends Device> getDevices(final DeviceType deviceType) {
        if (deviceType == DeviceType.P1_METER) {
            return getP1Meters();
        } else if (deviceType == DeviceType.PLUG_IN_BATTERY) {
            return getPlugInBatteries();
        } else {
            return getAllDevices();
        }
    }

    /**
     * Closes the {@link JmDNS} discoverer.
     *
     * @throws IOException when something has gone wrong
     */
    public void close() throws IOException {
        LOGGER.debug("Closing...");
        jmDNS.close();
    }

    /**
     * Closes the {@link JmDNS} discoverer and returns this discoverer instance.
     *
     * @return this discoverer instance
     * @throws IOException when something has gone wrong
     */
    public HomeWizardDiscoverer thenClose() throws IOException {
        close();
        return this;
    }

    /**
     * Returns all discovered P1 meter devices.
     *
     * @return all discovered P1 meter devices
     */
    public List<P1Meter> getP1Meters() {
        return p1Meters;
    }

    /**
     * Returns all discovered plug-in battery devices.
     *
     * @return all discovered plug-in battery devices
     */
    public List<PlugInBattery> getPlugInBatteries() {
        return plugInBatteries;
    }

    /**
     * Returns all discovered devices.
     *
     * @return all discovered devices
     */
    public List<Device> getAllDevices() {
        final List<Device> devices = new ArrayList<>();
        devices.addAll(getPlugInBatteries());
        return devices;
    }
}
