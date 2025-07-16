package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.EnergySocket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class EnergySocketTest {
    private static EnergySocket energySocket;

    @BeforeAll
    public static void beforeAll() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8323), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(Utils.getResourceAsString("energySocket/deviceInfo.json")));
        server.createContext("/test/data", new TestHttpHandler(Utils.getResourceAsString("energySocket/measurements.json")));
        server.createContext("/test/system", new TestHttpHandler(Utils.getResourceAsString("energySocket/systemConfiguration.json")));
        server.createContext("/test/state", new TestHttpHandler(Utils.getResourceAsString("energySocket/deviceState.json")));

        server.start();

        energySocket = new EnergySocket(true, "localhost", 8323, "/test");
        energySocket.updateAll();
    }

//    @Test
//    public void testToString() {
//        Assertions.assertEquals("{\"isApiEnabled\":true,\"getFirmwareVersion\":\"5.18\",\"getServiceName\":null,\"getActivePowerW\":543.312,\"getProductName\":\"Energy Socket\",\"getActiveApparentPowerVa\":666.768,\"getActiveReactivePowerVar\":123.456,\"getActiveVoltageV\":231.539,\"getDeviceState\":{\"power_on\":true,\"switch_lock\":false,\"brightness\":255},\"getWifiSsid\":\"My Wi-Fi\",\"getActivePowerFactor\":0.81688,\"getFullApiAddress\":\"http://localhost:8323/test\",\"getTotalPowerImportKwh\":30.511,\"getApiVersion\":\"v1\",\"getHostAddress\":\"localhost\",\"getFullAddress\":\"http://localhost:8323\",\"getActiveCurrentA\":2.346,\"getWifiStrength\":100.0,\"getTotalPowerExportKwh\":85.951,\"getSerial\":\"3c35e7aabbcc\",\"getPort\":8323,\"getSystemConfiguration\":{\"cloud_enabled\":true},\"getActiveFrequencyHz\":50.005,\"getApiPath\":\"/test\",\"getProductType\":\"HWE-SKT\"}", energySocket.toString());
//    }

    @Test
    public void testDeviceInfo() {
        Assertions.assertEquals("HWE-SKT", energySocket.getProductType().get());
        Assertions.assertEquals("Energy Socket", energySocket.getProductName().get());
        Assertions.assertEquals("3c35e7aabbcc", energySocket.getSerial().get());
        Assertions.assertEquals("5.18", energySocket.getFirmwareVersion().get());
        Assertions.assertEquals("v1", energySocket.getApiVersion().get());
    }

    @Test
    public void testMeasurements() {
        Assertions.assertEquals("My Wi-Fi", energySocket.getWifiSsid().get());
        Assertions.assertEquals(100.0, energySocket.getWifiStrength().getAsDouble());
        Assertions.assertEquals(30.511, energySocket.getTotalPowerImportKwh().getAsDouble());
        Assertions.assertEquals(85.951, energySocket.getTotalPowerExportKwh().getAsDouble());
        Assertions.assertEquals(543.312, energySocket.getActivePowerW().getAsDouble());
        Assertions.assertEquals(231.539, energySocket.getActiveVoltageV().getAsDouble());
        Assertions.assertEquals(2.346, energySocket.getActiveCurrentA().getAsDouble());
        Assertions.assertEquals(123.456, energySocket.getActiveReactivePowerVar().getAsDouble());
        Assertions.assertEquals(666.768, energySocket.getActiveApparentPowerVa().getAsDouble());
        Assertions.assertEquals(0.81688, energySocket.getActivePowerFactor().getAsDouble());
        Assertions.assertEquals(50.005, energySocket.getActiveFrequencyHz().getAsDouble());
    }

    @Test
    public void testSystemConfiguration() {
        Assertions.assertEquals(true, energySocket.getSystemConfiguration().isCloudEnabled().get());
    }

    @Test
    public void testDeviceState() {
        Assertions.assertEquals(true, energySocket.getDeviceState().getPowerOn().get());
        Assertions.assertEquals(false, energySocket.getDeviceState().getSwitchLock().get());
        Assertions.assertEquals(255, energySocket.getDeviceState().getBrightness().getAsInt());
    }
}
