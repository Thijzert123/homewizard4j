package io.github.thijzert123.homewizardapi.test;

import io.github.thijzert123.homewizardapi.v1.HomeWizardDiscoverer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Thijzert123
 */
public class TestHW {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void test() {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
        try {
            Thread.sleep(1000);
            discoverer.getAllDevices().forEach(device -> {
                device.updateAll();
                LOGGER.info(device.toString());
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    @Test
    public void generateAll() {
        generate("smr_version");
        generate("meter_model");
        generate("wifi_ssid");
        generate("wifi_strength");
        generate("total_power_import_kwh");
        generate("total_power_import_t1_kwh");
        generate("total_power_import_t2_kwh");
        generate("total_power_import_t3_kwh");
        generate("total_power_import_t4_kwh");
        generate("total_power_export_kwh");
        generate("total_power_export_t1_kwh");
        generate("total_power_export_t2_kwh");
        generate("total_power_export_t3_kwh");
        generate("total_power_export_t4_kwh");
        generate("active_power_w");
        generate("active_power_l1_w");
        generate("active_power_l2_w");
        generate("active_power_l3_w");
        generate("active_voltage_l1_v");
        generate("active_voltage_l2_v");
        generate("active_voltage_l3_v");
        generate("active_current_l1_a");
        generate("active_current_l2_a");
        generate("active_current_l3_a");
        generate("active_frequency_hz");
        generate("voltage_sag_l1_count");
        generate("voltage_sag_l2_count");
        generate("voltage_sag_l3_count");
        generate("voltage_swell_l1_count");
        generate("voltage_swell_l2_count");
        generate("voltage_swell_l3_count");
        generate("any_power_fail_count");
        generate("long_power_fail_count");
        generate("active_power_average_w");
        generate("montly_power_peak_w");
        generate("montly_power_peak_timestamp");
        generate("total_gas_m3");
        generate("gas_timestamp");
        generate("unique_gas_id");
    }

    public void generate(final String startstring) {
        final StringBuilder string = new StringBuilder(startstring);
        final StringBuilder builder = new StringBuilder("    ");

        builder.append("@JsonProperty(\"").append(string).append("\")");
        builder.append(System.lineSeparator());

        builder.append("    private final OptionalDouble ");
        while (true) {
            final int index = string.indexOf("_");
            if (index == -1) {
                break;
            }
            string.replace(index, index + 1, "");
            string.replace(index, index + 1, string.substring(index, index + 1).toUpperCase());

        }
        builder.append(string).append(" = OptionalDouble.empty();");
        System.out.println(builder.toString());
    }
}
