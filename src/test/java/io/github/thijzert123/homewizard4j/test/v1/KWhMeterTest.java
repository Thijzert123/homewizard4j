package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.KWhMeter;
import io.github.thijzert123.homewizard4j.v1.P1Meter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class KWhMeterTest {
    private static KWhMeter kWhMeter;

    @BeforeAll
    public static void beforeAll() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8324), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(Utils.getResourceAsString("kWhMeter/deviceInfo.json")));
        server.createContext("/test/data", new TestHttpHandler(Utils.getResourceAsString("kWhMeter/measurements.json")));
        server.createContext("/test/system", new TestHttpHandler(Utils.getResourceAsString("kWhMeter/systemConfiguration.json")));

        server.start();

        kWhMeter = new KWhMeter(true, "localhost", 8324, "/test");
        kWhMeter.updateAll();
    }

    @Test
    public void testDeviceInfo() {
        Assertions.assertEquals("HWE-KWH3", kWhMeter.getProductType().get());
        Assertions.assertEquals("kWh Meter", kWhMeter.getProductName().get());
        Assertions.assertEquals("3c39e7aa2bcc", kWhMeter.getSerial().get());
        Assertions.assertEquals("5.18", kWhMeter.getFirmwareVersion().get());
        Assertions.assertEquals("v1", kWhMeter.getApiVersion().get());
    }

    @Test
    public void testMeasurements() {
        Assertions.assertEquals("HW WiFi", kWhMeter.getWifiSsid().get());
        Assertions.assertEquals(84, kWhMeter.getWifiStrength().getAsDouble());
        Assertions.assertEquals(2940.101, kWhMeter.getTotalPowerImportKwh().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getTotalPowerExportKwh().getAsDouble());
        Assertions.assertEquals(7100.278, kWhMeter.getActivePowerW().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActivePowerL1W().getAsDouble());
        Assertions.assertEquals(3547.015, kWhMeter.getActivePowerL2W().getAsDouble());
        Assertions.assertEquals(3553.263, kWhMeter.getActivePowerL3W().getAsDouble());
        Assertions.assertEquals(230.751, kWhMeter.getActiveVoltageL1V().getAsDouble());
        Assertions.assertEquals(228.391, kWhMeter.getActiveVoltageL2V().getAsDouble());
        Assertions.assertEquals(229.612, kWhMeter.getActiveVoltageL3V().getAsDouble());
        Assertions.assertEquals(30.999, kWhMeter.getActiveCurrentA().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActiveCurrentL1A().getAsDouble());
        Assertions.assertEquals(15.521, kWhMeter.getActiveCurrentL2A().getAsDouble());
        Assertions.assertEquals(15.477, kWhMeter.getActiveCurrentL3A().getAsDouble());
        Assertions.assertEquals(31.058, kWhMeter.getActiveApparentCurrentA().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActiveApparentCurrentL1A().getAsDouble());
        Assertions.assertEquals(15.539, kWhMeter.getActiveApparentCurrentL2A().getAsDouble());
        Assertions.assertEquals(15.519, kWhMeter.getActiveApparentCurrentL3A().getAsDouble());
        Assertions.assertEquals(1.872, kWhMeter.getActiveReactiveCurrentA().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActiveReactiveCurrentL1A().getAsDouble());
        Assertions.assertEquals(0.73, kWhMeter.getActiveReactiveCurrentL2A().getAsDouble());
        Assertions.assertEquals(1.143, kWhMeter.getActiveReactiveCurrentL3A().getAsDouble());
        Assertions.assertEquals(7112.293, kWhMeter.getActiveApparentPowerVa().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActiveApparentPowerL1Va().getAsDouble());
        Assertions.assertEquals(3548.879, kWhMeter.getActiveApparentPowerL2Va().getAsDouble());
        Assertions.assertEquals(3563.414, kWhMeter.getActiveApparentPowerL3Va().getAsDouble());
        Assertions.assertEquals(-429.025, kWhMeter.getActiveReactivePowerVar().getAsDouble());
        Assertions.assertEquals(0, kWhMeter.getActiveReactivePowerL1Var().getAsDouble());
        Assertions.assertEquals(-166.675, kWhMeter.getActiveReactivePowerL2Var().getAsDouble());
        Assertions.assertEquals(-262.35, kWhMeter.getActiveReactivePowerL3Var().getAsDouble());
        Assertions.assertEquals(1, kWhMeter.getActivePowerFactorL1().getAsDouble());
        Assertions.assertEquals(0.999, kWhMeter.getActivePowerFactorL2().getAsDouble());
        Assertions.assertEquals(0.997, kWhMeter.getActivePowerFactorL3().getAsDouble());
        Assertions.assertEquals(49.926,  kWhMeter.getActiveFrequencyHz().getAsDouble());
    }

    @Test
    public void testSystemConfiguration() {
        Assertions.assertEquals(true, kWhMeter.getSystemConfiguration().isCloudEnabled().get());
    }
}
