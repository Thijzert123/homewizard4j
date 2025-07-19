package io.github.thijzert123.homewizard4j.test.v1;

import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.HomeWizardApiException;
import io.github.thijzert123.homewizard4j.v1.P1Meter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Thijzert123
 */
public class P1MeterTest {
    private static String expectedJson;

    @BeforeAll
    public static void beforeAll() throws IOException {
        Utils.initializeServer(8322, "p1Meter").start();

        expectedJson = "{\"service_name\":null,\"api_enabled\":true,\"host_address\":\"localhost\",\"port\":8322,\"api_path\":\"/test\",\"system_configuration\":{\"cloud_enabled\":true},\"product_type\":\"HWE-P1\",\"product_name\":\"P1 Meter\",\"serial\":\"3c39e7aabbcc\",\"firmware_version\":\"5.18\",\"api_version\":\"v1\",\"wifi_ssid\":\"My Wi-Fi\",\"wifi_strength\":100.0,\"unique_id\":\"00112233445566778899AABBCCDDEEFF\",\"smr_version\":50.0,\"meter_model\":\"ISKRA  2M550T-101\",\"total_power_import_kwh\":13779.338,\"total_power_import_t1_kwh\":10830.511,\"total_power_import_t2_kwh\":2948.827,\"total_power_import_t3_kwh\":null,\"total_power_import_t4_kwh\":null,\"total_power_export_kwh\":0.0,\"total_power_export_t1_kwh\":0.0,\"total_power_export_t2_kwh\":0.0,\"total_power_export_t3_kwh\":null,\"total_power_export_t4_kwh\":null,\"active_power_w\":-543.0,\"active_power_l1_w\":-676.0,\"active_power_l2_w\":133.0,\"active_power_l3_w\":0.0,\"active_voltage_l1_v\":null,\"active_voltage_l2_v\":null,\"active_voltage_l3_v\":null,\"active_current_l1_a\":-4.0,\"active_current_l2_a\":2.0,\"active_current_l3_a\":0.0,\"active_frequency_hz\":null,\"voltage_sag_l1_count\":1.0,\"voltage_sag_l2_count\":1.0,\"voltage_sag_l3_count\":0.0,\"voltage_swell_l1_count\":0.0,\"voltage_swell_l2_count\":0.0,\"voltage_swell_l3_count\":0.0,\"any_power_fail_count\":4.0,\"long_power_fail_count\":5.0,\"active_power_average_w\":123.0,\"montly_power_peak_w\":1111.0,\"montly_power_peak_timestamp\":2.3010108001E11,\"total_gas_m3\":2569.646,\"gas_timestamp\":2.1060614001E11,\"unique_gas_id\":null,\"external\":[{\"unique_id\":\"FFEEDDCCBBAA99887766554433221100\",\"type\":\"gas_meter\",\"timestamp\":2.1060614001E11,\"value\":2569.646,\"unit\":\"m3\"},{\"unique_id\":\"ABCDEF0123456789ABCDEF0123456789\",\"type\":\"water_meter\",\"timestamp\":2.10606140015E11,\"value\":123.456,\"unit\":\"m3\"}]}";
    }

    @Test
    public void testToJson() throws HomeWizardApiException {
        final P1Meter p1Meter = new P1Meter(true, "localhost", 8322, "/test");
        p1Meter.updateAll();

        Assertions.assertEquals(expectedJson, p1Meter.toJson());
    }

    @Test
    public void testFromJson() throws HomeWizardApiException {
        final P1Meter p1Meter = new P1Meter(true, "localhost", 8322, "/test");
        p1Meter.updateFromJson(expectedJson);

        Assertions.assertEquals(expectedJson, p1Meter.toJson());
    }
}
