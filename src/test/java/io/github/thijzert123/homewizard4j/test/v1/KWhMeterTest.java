package io.github.thijzert123.homewizard4j.test.v1;

import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.HomeWizardApiException;
import io.github.thijzert123.homewizard4j.v1.KWhMeter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Thijzert123
 */
public class KWhMeterTest {
    private static String expectedJson;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Utils.initializeServer(8324, "kWhMeter").start();

        expectedJson = "{\"service_name\":null,\"api_enabled\":true,\"host_address\":\"localhost\",\"port\":8324,\"api_path\":\"/test\",\"system_configuration\":{\"cloud_enabled\":true},\"product_type\":\"HWE-KWH3\",\"product_name\":\"kWh Meter\",\"serial\":\"3c39e7aa2bcc\",\"firmware_version\":\"5.18\",\"api_version\":\"v1\",\"wifi_ssid\":\"HW WiFi\",\"wifi_strength\":84.0,\"total_power_import_kwh\":2940.101,\"total_power_export_kwh\":0.0,\"active_power_w\":7100.278,\"active_power_l1_w\":0.0,\"active_power_l2_w\":3547.015,\"active_power_l3_w\":3553.263,\"active_voltage_v\":null,\"active_voltage_l1_v\":230.751,\"active_voltage_l2_v\":228.391,\"active_voltage_l3_v\":229.612,\"active_current_a\":30.999,\"active_current_l1_a\":0.0,\"active_current_l2_a\":15.521,\"active_current_l3_a\":15.477,\"active_apparent_current_a\":31.058,\"active_apparent_current_l1_a\":0.0,\"active_apparent_current_l2_a\":15.539,\"active_apparent_current_l3_a\":15.519,\"active_reactive_current_a\":1.872,\"active_reactive_current_l1_a\":0.0,\"active_reactive_current_l2_a\":0.73,\"active_reactive_current_l3_a\":1.143,\"active_apparent_power_va\":7112.293,\"active_apparent_power_l1_va\":0.0,\"active_apparent_power_l2_va\":3548.879,\"active_apparent_power_l3_va\":3563.414,\"active_reactive_power_var\":-429.025,\"active_reactive_power_l1_var\":0.0,\"active_reactive_power_l2_var\":-166.675,\"active_reactive_power_l3_var\":-262.35,\"active_power_factor\":null,\"active_power_factor_l1\":1.0,\"active_power_factor_l2\":0.999,\"active_power_factor_l3\":0.997,\"active_frequency_hz\":49.926}";
    }

    @Test
    public void testToJson() throws HomeWizardApiException {
        final KWhMeter kWhMeter = new KWhMeter(true, "localhost", 8324, "/test");
        kWhMeter.updateAll();

        Assertions.assertEquals(expectedJson, kWhMeter.toJson());
    }

    @Test
    public void testFromJson() throws HomeWizardApiException {
        final KWhMeter kWhMeter = new KWhMeter(true, "localhost", 8324, "/test");
        kWhMeter.updateFromJson(expectedJson);

        Assertions.assertEquals(expectedJson, kWhMeter.toJson());
    }
}
