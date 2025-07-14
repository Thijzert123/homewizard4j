package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * // Get all devices, including watermeters and P1 meters
 * {@code final List<Device> devices = discoverer.getAllDevices();}
 * for (final Device device : devices) {
 *     // Print the name of each device
 *     System.out.println(device.getProductName());
 * }
 * </pre>
 *
 * @author Thijzert123
 * @see Device
 */
public class HomeWizardDiscoverer implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SERVICE_TYPE = "_hwenergy._tcp.local.";
    private final JmDNS jmDNS;

    final List<Watermeter> watermeters;
    final List<P1Meter> p1Meters;

    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     * This discoverer starts with 0 devices.
     *
     * @throws IOException when something has gone wrong while creating the mDNS discoverer
     */
    public HomeWizardDiscoverer() throws IOException {
        watermeters = new ArrayList<>();
        p1Meters = new ArrayList<>();

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
        watermeters.addAll(discovererToMerge.getWatermeters());
        p1Meters.addAll(discovererToMerge.getP1Meters());
    }

    /**
     * Closes the {@link JmDNS} discoverer.
     */
    public void close() throws IOException {
        jmDNS.close();
    }

    /**
     * Returns all watermeter devices.
     *
     * @return all watermeter devices
     */
    public List<Watermeter> getWatermeters() {
        return watermeters;
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
     * Returns all devices.
     *
     * @return all devices
     */
    public List<Device> getAllDevices() {
        final List<Device> devices = new ArrayList<>();
        devices.addAll(getWatermeters());
        devices.addAll(getP1Meters());
        return devices;
    }
}
