package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.KWhMeter;
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

//    @Test
//    public void testToString() {
//        Assertions.assertEquals("{\"getFirmwareVersion\":\"5.18\",\"getActiveCurrentL3A\":15.477,\"getActiveApparentCurrentL2A\":15.539,\"getActiveApparentPowerL2Va\":3548.879,\"getActivePowerW\":7100.278,\"getProductName\":\"kWh Meter\",\"getActiveReactiveCurrentL3A\":1.143,\"getActiveVoltageV\":null,\"getActiveVoltageL3V\":229.612,\"getFullAddress\":\"http://localhost:8324\",\"getActiveCurrentA\":30.999,\"getWifiStrength\":84.0,\"getActiveCurrentL2A\":15.521,\"getActiveApparentCurrentL3A\":15.519,\"getActiveReactivePowerL1Var\":0.0,\"getSystemConfiguration\":{\"cloud_enabled\":true},\"getActiveFrequencyHz\":49.926,\"getApiPath\":\"/test\",\"getActivePowerL3W\":3553.263,\"getActiveReactiveCurrentL1A\":0.0,\"isApiEnabled\":true,\"getActiveCurrentL1A\":0.0,\"getServiceName\":null,\"getActiveApparentPowerL1Va\":0.0,\"getActiveApparentPowerL3Va\":3563.414,\"getActiveApparentPowerVa\":7112.293,\"getActiveReactivePowerVar\":-429.025,\"getActiveReactivePowerL2Var\":-166.675,\"getActivePowerL2W\":3547.015,\"getActivePowerFactorL1\":1.0,\"getActiveVoltageL1V\":230.751,\"getActivePowerFactorL3\":0.997,\"getWifiSsid\":\"HW WiFi\",\"getActivePowerFactorL2\":0.999,\"getActivePowerFactor\":null,\"getFullApiAddress\":\"http://localhost:8324/test\",\"getTotalPowerImportKwh\":2940.101,\"getApiVersion\":\"v1\",\"getHostAddress\":\"localhost\",\"getActiveApparentCurrentA\":31.058,\"getTotalPowerExportKwh\":0.0,\"getActiveApparentCurrentL1A\":0.0,\"getActiveReactiveCurrentL2A\":0.73,\"getActivePowerL1W\":0.0,\"getActiveReactivePowerL3Var\":-262.35,\"getSerial\":\"3c39e7aa2bcc\",\"getPort\":8324,\"getActiveVoltageL2V\":228.391,\"getActiveReactiveCurrentA\":1.872,\"getProductType\":\"HWE-KWH3\"}", kWhMeter.toString());
//    }

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
