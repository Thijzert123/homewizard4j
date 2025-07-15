package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Discovers HomeWizard devices using mDNS. This class should be your starting point when using the API.
 * When you initialize the class, a mDNS scanner is made with {@link JmDNS}. It scans specifically for HomeWizard devices.
 * Before calling one of the getters, you should wait for about a second. If you have slow internet speeds,
 * you might need to wait for an even longer time.
 * <p>
 * This code example shows how to retrieve all devices and print their name:
 *
 * <pre>
 * // Initialize discoverer
 * final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
 *
 * //
 * // Wait for some time...
 * //
 *
 * // Get all devices, including water meters and P1 meters
 * {@code final List<Device> devices = discoverer.getAllDevices();}
 * for (final Device device : devices) {
 *     // Print the name of each device
 *     System.out.println(device.getProductName());
 * }
 * </pre>
 *
 * For more information, see <a href="https://github.com/Thijzert123/homewizard4j?tab=readme-ov-file#discovery">discovery</a>.
 *
 * @author Thijzert123
 * @see Device
 */
public class HomeWizardDiscoverer implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SERVICE_TYPE = "_hwenergy._tcp.local.";
    private final JmDNS jmDNS;

    final List<WaterMeter> waterMeters;
    final List<P1Meter> p1Meters;
    final List<EnergySocket> energySockets;
    final List<KWhMeter> kWhMeters;

    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     * This discoverer starts with 0 devices.
     *
     * @throws IOException when something has gone wrong while creating the mDNS discoverer
     */
    public HomeWizardDiscoverer() throws IOException {
        waterMeters = new ArrayList<>();
        p1Meters = new ArrayList<>();
        energySockets = new ArrayList<>();
        kWhMeters = new ArrayList<>();

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
        waterMeters.addAll(discovererToMerge.getWaterMeters());
        p1Meters.addAll(discovererToMerge.getP1Meters());
        energySockets.addAll(discovererToMerge.getEnergySockets());
    }

    /**
     * Closes the {@link JmDNS} discoverer.
     */
    public void close() throws IOException {
        jmDNS.close();
    }

    /**
     * Returns all waterMeter devices.
     *
     * @return all waterMeter devices
     */
    public List<WaterMeter> getWaterMeters() {
        return waterMeters;
    }

    /**
     * Returns all P1 meter devices.
     *
     * @return all P1 meter devices
     */
    public List<P1Meter> getP1Meters() {
        return p1Meters;
    }

    /**
     * Returns all energy socket devices.
     *
     * @return all energy socket devices
     */
    public List<EnergySocket> getEnergySockets() {
        return energySockets;
    }

    /**
     * Returns all kWh meter devices.
     *
     * @return all kWh meter devices
     */
    public List<KWhMeter> getKWhMeters() {
        return kWhMeters;
    }

    /**
     * Returns all devices.
     *
     * @return all devices
     */
    public List<Device> getAllDevices() {
        final List<Device> devices = new ArrayList<>();
        devices.addAll(getWaterMeters());
        devices.addAll(getP1Meters());
        devices.addAll(getEnergySockets());
        devices.addAll(getKWhMeters());
        return devices;
    }
}
