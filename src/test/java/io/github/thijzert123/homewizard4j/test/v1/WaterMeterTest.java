package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.WaterMeter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class WaterMeterTest {
    private static WaterMeter waterMeter;

    @BeforeAll
    public static void beforeAll() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8321), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(Utils.getResourceAsString("waterMeter/deviceInfo.json")));
        server.createContext("/test/data", new TestHttpHandler(Utils.getResourceAsString("waterMeter/measurements.json")));
        server.createContext("/test/system", new TestHttpHandler(Utils.getResourceAsString("waterMeter/systemConfiguration.json")));

        server.start();

        waterMeter = new WaterMeter(true, "localhost", 8321, "/test");
        waterMeter.updateAll();
    }

    @Test
    public void testDeviceInfo() {
        Assertions.assertEquals("HWE-WTR", waterMeter.getProductType().get());
        Assertions.assertEquals("Watermeter", waterMeter.getProductName().get());
        Assertions.assertEquals("3c39e7363bCC", waterMeter.getSerial().get());
        Assertions.assertEquals("5.18", waterMeter.getFirmwareVersion().get());
        Assertions.assertEquals("v1", waterMeter.getApiVersion().get());
    }

    @Test
    public void testMeasurements() {
        Assertions.assertEquals("My Wi-Fi", waterMeter.getWifiSsid().get());
        Assertions.assertEquals(100.0, waterMeter.getWifiStrength().getAsDouble());
        Assertions.assertEquals(123.456, waterMeter.getTotalLiterM3().getAsDouble());
        Assertions.assertEquals(7.2, waterMeter.getActiveLiterLpm().getAsDouble());
        Assertions.assertEquals(0.0, waterMeter.getTotalLiterOffsetM3().getAsDouble());
    }

    @Test
    public void testSystemConfiguration() {
        Assertions.assertEquals(true, waterMeter.getSystemConfiguration().isCloudEnabled().get());
    }
}
