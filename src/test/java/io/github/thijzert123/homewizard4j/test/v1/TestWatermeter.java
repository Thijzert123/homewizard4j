package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.Watermeter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class TestWatermeter {
    private static Watermeter watermeter;

    @BeforeAll
    public static void beforeAll() throws IOException  {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8321), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(Utils.getResourceAsString("watermeter/deviceInfo")));
        server.createContext("/test/data", new TestHttpHandler(Utils.getResourceAsString("watermeter/measurements")));
        server.createContext("/test/system", new TestHttpHandler(Utils.getResourceAsString("watermeter/systemConfiguration")));

        server.start();

        watermeter = new Watermeter(true, "localhost", 8321, "/test");
        watermeter.updateAll();
    }

    @Test
    public void testDeviceInfo() {
        Assertions.assertEquals("HWE-WTR", watermeter.getProductType().get());
        Assertions.assertEquals("Watermeter", watermeter.getProductName().get());
        Assertions.assertEquals("3c39e7363bCC", watermeter.getSerial().get());
        Assertions.assertEquals("5.18", watermeter.getFirmwareVersion().get());
        Assertions.assertEquals("v1", watermeter.getApiVersion().get());
    }

    @Test
    public void testMeasurements() {
        Assertions.assertEquals("My Wi-Fi", watermeter.getWifiSsid().get());
        Assertions.assertEquals(100.0, watermeter.getWifiStrength().getAsDouble());
        Assertions.assertEquals(123.456, watermeter.getTotalLiterM3().getAsDouble());
        Assertions.assertEquals(7.2, watermeter.getActiveLiterLpm().getAsDouble());
        Assertions.assertEquals(0.0, watermeter.getTotalLiterOffsetM3().getAsDouble());
    }

    @Test
    public void testSystemConfiguration() {
        Assertions.assertEquals(true, watermeter.getSystemConfiguration().isCloudEnabled().get());
    }
}
