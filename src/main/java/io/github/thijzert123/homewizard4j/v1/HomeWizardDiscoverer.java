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

    final List<ServiceInfo> allServiceInfo;
    final List<ServiceInfo> allWatermeterServiceInfo;
    final List<ServiceInfo> allP1MeterServiceInfo;

    /**
     * Initializes the discoverer and starts scanning for HomeWizard devices.
     */
    public HomeWizardDiscoverer() {
        allServiceInfo = new ArrayList<>();
        allWatermeterServiceInfo = new ArrayList<>();
        allP1MeterServiceInfo = new ArrayList<>();

        try {
            jmDNS = JmDNS.create(InetAddress.getLocalHost());
            jmDNS.addServiceListener(SERVICE_TYPE, new HomeWizardServiceListener(this));
        } catch (final IOException ioException) {
            throw new HomeWizardApiException(ioException, LOGGER);
        }
    }

    /**
     * Closes the {@link JmDNS} discoverer.
     */
    public void close() throws IOException {
        jmDNS.close();
    }

    /**
     * Returns information about all HomeWizard mDNS services discovered.
     *
     * @return information about all HomeWizard mDNS services discovered
     */
    public List<ServiceInfo> getAllServiceInfo() {
        return allServiceInfo;
    }

    /**
     * Returns information about all HomeWizard watermeter mDNS services discovered.
     *
     * @return information about all HomeWizard watermeter mDNS services discovered
     */
    public List<ServiceInfo> getAllWatermeterServiceInfo() {
        return allWatermeterServiceInfo;
    }

    /**
     * Returns information about all HomeWizard P1 meter mDNS services discovered.
     *
     * @return information about all HomeWizard P1 meter mDNS services discovered
     */
    public List<ServiceInfo> getAllP1MeterServiceInfo() {
        return allP1MeterServiceInfo;
    }

    /**
     * Returns all watermeter devices.
     *
     * @return all watermeter devices
     */
    public List<Watermeter> getWatermeters() {
        final List<Watermeter> watermeters = new ArrayList<>();
        allWatermeterServiceInfo.forEach((serviceInfo -> {
            watermeters.add(new Watermeter(
                    serviceInfo.getQualifiedName(),
                    serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                    serviceInfo.getPort(),
                    Objects.equals(serviceInfo.getPropertyString("api_enabled"), "1"),
                    serviceInfo.getPropertyString("path"),
                    serviceInfo.getPropertyString("serial"),
                    serviceInfo.getPropertyString("product_name")));
        }));
        return watermeters;
    }

    /**
     * Returns all P1 meter devices.
     *
     * @return all P1 meter devices
     */
    public List<P1Meter> getP1Meters() {
        final List<P1Meter> p1Meters = new ArrayList<>();
        allP1MeterServiceInfo.forEach(serviceInfo -> {
            p1Meters.add(new P1Meter(
                    serviceInfo.getQualifiedName(),
                    serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                    serviceInfo.getPort(),
                    Objects.equals(serviceInfo.getPropertyString("api_enabled"), "1"),
                    serviceInfo.getPropertyString("path"),
                    serviceInfo.getPropertyString("serial"),
                    serviceInfo.getPropertyString("product_name")));
        });
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
