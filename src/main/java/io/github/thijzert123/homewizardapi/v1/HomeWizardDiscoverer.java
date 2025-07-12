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

    public HomeWizardDiscoverer() {
        allServiceInfo = new ArrayList<>();
        allWatermeterServiceInfo = new ArrayList<>();

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
}
