package io.github.thijzert123.homewizard4j.test.v1;

import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.HomeWizardApiException;
import io.github.thijzert123.homewizard4j.v1.WaterMeter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Thijzert123
 */
public class WaterMeterTest {
    private static String expectedJson;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Utils.initializeServer(8321, "waterMeter").start();

        expectedJson = "{\"service_name\":null,\"api_enabled\":true,\"host_address\":\"localhost\",\"port\":8321,\"api_path\":\"/test\",\"product_type\":\"HWE-WTR\",\"product_name\":\"Watermeter\",\"serial\":\"3c39e7363bCC\",\"firmware_version\":\"5.18\",\"api_version\":\"v1\",\"wifi_ssid\":\"My Wi-Fi\",\"wifi_strength\":100.0,\"system_configuration\":{\"cloud_enabled\":true},\"total_liter_m3\":123.456,\"active_liter_lpm\":7.2,\"total_liter_offset_m3\":0.0}";
    }

    @Test
    public void testToJson() throws HomeWizardApiException {
        final WaterMeter waterMeter = new WaterMeter(true, "localhost", 8321, "/test");
        waterMeter.updateAll();

        Assertions.assertEquals(expectedJson, waterMeter.toJson());
    }

    @Test
    public void testFromJson() throws HomeWizardApiException {
        final WaterMeter waterMeter = new WaterMeter(true, "localhost", 8321, "/test");
        waterMeter.updateFromJson(expectedJson);

        Assertions.assertEquals(expectedJson, waterMeter.toJson());
    }
}
