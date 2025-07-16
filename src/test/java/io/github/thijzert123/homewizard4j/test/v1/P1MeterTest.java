package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.P1Meter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class P1MeterTest {
    private static P1Meter p1Meter;

    @BeforeAll
    public static void beforeAll() throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8322), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(Utils.getResourceAsString("p1Meter/deviceInfo.json")));
        server.createContext("/test/data", new TestHttpHandler(Utils.getResourceAsString("p1Meter/measurements.json")));
        server.createContext("/test/system", new TestHttpHandler(Utils.getResourceAsString("p1Meter/systemConfiguration.json")));

        server.start();

        p1Meter = new P1Meter(true, "localhost", 8322, "/test");
        p1Meter.updateAll();
    }

//    @Test
//    public void testToString() {
//        Assertions.assertEquals("{\"getSmrVersion\":50.0,\"getFirmwareVersion\":\"5.18\",\"getActiveCurrentL3A\":null,\"getVoltageSagL2Count\":null,\"getTotalPowerImportT2Kwh\":2948.827,\"getActivePowerW\":-678.0,\"getProductName\":\"P1 Meter\",\"getTotalPowerExportT4Kwh\":null,\"getActivePowerAverageW\":null,\"getUniqueId\":null,\"getActiveVoltageL3V\":null,\"getMonthlyPowerPeakW\":null,\"getFullAddress\":\"http://localhost:8322\",\"getVoltageSwellL3Count\":null,\"getExternalP1Devices\":null,\"getVoltageSagL1Count\":null,\"getWifiStrength\":100.0,\"getActiveCurrentL2A\":null,\"getTotalPowerImportT3Kwh\":null,\"getTotalPowerExportT1Kwh\":1285.951,\"getVoltageSwellL2Count\":null,\"getMonthlyPowerPeakTimestamp\":null,\"getSystemConfiguration\":{\"cloud_enabled\":true},\"getActiveFrequencyHz\":null,\"getApiPath\":\"/test\",\"getActivePowerL3W\":null,\"isApiEnabled\":true,\"getUniqueGasId\":null,\"getActiveCurrentL1A\":null,\"getServiceName\":null,\"getMeterModel\":\"ISKRA 2M550T-101\",\"getTotalPowerImportT4Kwh\":null,\"getTotalPowerExportT2Kwh\":2876.51,\"getActivePowerL2W\":null,\"getLongPowerFailCount\":null,\"getActiveVoltageL1V\":null,\"getWifiSsid\":\"My Wi-Fi\",\"getFullApiAddress\":\"http://localhost:8322/test\",\"getTotalPowerImportKwh\":null,\"getApiVersion\":\"v1\",\"getHostAddress\":\"localhost\",\"getAnyPowerFailCount\":null,\"getTotalPowerExportKwh\":null,\"getTotalPowerImportT1Kwh\":10830.511,\"getTotalGasM3\":null,\"getTotalPowerExportT3Kwh\":null,\"getActivePowerL1W\":-676.0,\"getVoltageSagL3Count\":null,\"getSerial\":\"3c39e7aabbcc\",\"getPort\":8322,\"getActiveVoltageL2V\":null,\"getVoltageSwellL1Count\":null,\"getProductType\":\"HWE-P1\",\"getGasTimestamp\":null}", p1Meter.toString());
//    }

    @Test
    public void testDeviceInfo() {
        Assertions.assertEquals("HWE-P1", p1Meter.getProductType().get());
        Assertions.assertEquals("P1 Meter", p1Meter.getProductName().get());
        Assertions.assertEquals("3c39e7aabbcc", p1Meter.getSerial().get());
        Assertions.assertEquals("5.18", p1Meter.getFirmwareVersion().get());
        Assertions.assertEquals("v1", p1Meter.getApiVersion().get());
    }

    @Test
    public void testMeasurements() {
        Assertions.assertEquals(50.0, p1Meter.getSmrVersion().getAsDouble());
        Assertions.assertEquals("ISKRA 2M550T-101", p1Meter.getMeterModel().get());
        Assertions.assertEquals("My Wi-Fi", p1Meter.getWifiSsid().get());
        Assertions.assertEquals(100.0, p1Meter.getWifiStrength().getAsDouble());
        Assertions.assertEquals(10830.511, p1Meter.getTotalPowerImportT1Kwh().getAsDouble());
        Assertions.assertEquals(2948.827, p1Meter.getTotalPowerImportT2Kwh().getAsDouble());
        Assertions.assertEquals(1285.951, p1Meter.getTotalPowerExportT1Kwh().getAsDouble());
        Assertions.assertEquals(2876.51, p1Meter.getTotalPowerExportT2Kwh().getAsDouble());
        Assertions.assertEquals(-678, p1Meter.getActivePowerW().getAsDouble());
        Assertions.assertEquals(-676, p1Meter.getActivePowerL1W().getAsDouble());
    }

    @Test
    public void testSystemConfiguration() {
        Assertions.assertEquals(true, p1Meter.getSystemConfiguration().isCloudEnabled().get());
    }
}
