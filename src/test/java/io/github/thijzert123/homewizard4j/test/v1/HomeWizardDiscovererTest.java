package io.github.thijzert123.homewizard4j.test.v1;

import io.github.thijzert123.homewizard4j.v1.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thijzert123
 */
public class HomeWizardDiscovererTest {
    static JmDNS jmDNS;
    static HomeWizardDiscoverer discoverer;

    @BeforeAll
    public static void beforeAll() throws IOException, InterruptedException {
        jmDNS = JmDNS.create(Inet4Address.getLocalHost());
        jmDNS.registerService(generateServiceInfo("HWE-WTR", "Watermeter", 7700));
        jmDNS.registerService(generateServiceInfo("HWE-P1", "P1Meter", 7701));
        jmDNS.registerService(generateServiceInfo("HWE-SKT", "EnergySocket", 7702));
        jmDNS.registerService(generateServiceInfo("SDM230-wifi", "kWhMeter", 7703));

        discoverer = new HomeWizardDiscoverer();

        // Make sure all services are registered and discovered
        Thread.sleep(5000);
    }

    private static ServiceInfo generateServiceInfo(final String productType, final String name, final int port) {
        final Map<String, String> properties = new HashMap<>();
        properties.put("api_enabled", "1");
        properties.put("path", "/api/v1");
        properties.put("serial", "5c2Faf1e8b42");
        properties.put("product_type", productType);
        properties.put("product_name", name);

        return ServiceInfo.create(
                HomeWizardDiscoverer.SERVICE_TYPE,
                name,
                port,
                0,
                0,
                properties);
    }

    @Test
    public void testWaterMeter() {
        boolean testPassed = false;
        // check if one of the water meters is the one we made in this test class
        for (final WaterMeter waterMeter : discoverer.getWaterMeters()) {
            if (waterMeter.getServiceName().get().equals("Watermeter._hwenergy._tcp.local.")) {
                testPassed = true;
            }
        }
        Assertions.assertTrue(testPassed);
    }

    @Test
    public void testWaterP1Meter() {
        boolean testPassed = false;
        for (final P1Meter p1Meter : discoverer.getP1Meters()) {
            if (p1Meter.getServiceName().get().equals("P1Meter._hwenergy._tcp.local.")) {
                testPassed = true;
            }
        }
        Assertions.assertTrue(testPassed);
    }

    @Test
    public void testEnergySocket() {
        boolean testPassed = false;
        for (final EnergySocket energySocket : discoverer.getEnergySockets()) {
            if (energySocket.getServiceName().get().equals("EnergySocket._hwenergy._tcp.local.")) {
                testPassed = true;
            }
        }
        Assertions.assertTrue(testPassed);
    }

    @Test
    public void testKWhMeter() {
        boolean testPassed = false;
        for (final KWhMeter kWhMeter : discoverer.getKWhMeters()) {
            if (kWhMeter.getServiceName().get().equals("kWhMeter._hwenergy._tcp.local.")) {
                testPassed = true;
            }
        }
        Assertions.assertTrue(testPassed);
    }

    @Test
    public void testAllDevices() {
        // Should be 4, but if tested on a network with more devices, the test also passes
        Assertions.assertTrue(discoverer.getAllDevices().size() >= 4);
    }

    @AfterAll
    public static void afterAll() throws IOException {
        jmDNS.close();
        discoverer.close();
    }
}
