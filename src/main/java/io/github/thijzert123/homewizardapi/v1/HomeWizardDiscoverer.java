package io.github.thijzert123.homewizardapi.v1;

import io.github.thijzert123.homewizardapi.HomeWizardApiException;
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
 * @author Thijzert123
 */
public class HomeWizardDiscoverer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SERVICE_TYPE = "_hwenergy._tcp.local.";

    final List<ServiceInfo> allServiceInfo;
    final List<ServiceInfo> allWatermeterServiceInfo;
    final List<ServiceInfo> allP1MeterServiceInfo;

    public HomeWizardDiscoverer() {
        allServiceInfo = new ArrayList<>();
        allWatermeterServiceInfo = new ArrayList<>();
        allP1MeterServiceInfo = new ArrayList<>();

        try {
            final JmDNS jmDNS = JmDNS.create(InetAddress.getLocalHost());
            jmDNS.addServiceListener(SERVICE_TYPE, new HomeWizardServiceListener(this));
        } catch (final IOException ioException) {
            throw new HomeWizardApiException(ioException, LOGGER);
        }
    }

    public List<ServiceInfo> getAllServiceInfo() {
        return allServiceInfo;
    }

    public List<ServiceInfo> getAllWatermeterServiceInfo() {
        return allWatermeterServiceInfo;
    }

    public List<ServiceInfo> getAllP1MeterServiceInfo() {
        return allP1MeterServiceInfo;
    }

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

    public List<Device> getAllDevices() {
        final List<Device> devices = new ArrayList<>();
        devices.addAll(getWatermeters());
        devices.addAll(getP1Meters());
        return devices;
    }
}
