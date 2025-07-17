package io.github.thijzert123.homewizard4j.test.v1;

import com.sun.net.httpserver.HttpServer;
import io.github.thijzert123.homewizard4j.test.TestHttpHandler;
import io.github.thijzert123.homewizard4j.test.Utils;
import io.github.thijzert123.homewizard4j.v1.EnergySocket;
import io.github.thijzert123.homewizard4j.v1.HomeWizardApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Thijzert123
 */
public class EnergySocketTest {
    private static EnergySocket energySocket;

    @BeforeAll
    public static void beforeAll() throws IOException {
        final HttpServer httpServer = Utils.initializeServer(8323, "energySocket");
        httpServer.createContext("/test/state", new TestHttpHandler(Utils.getResourceAsString("energySocket/state.json")));
        httpServer.start();

        energySocket = new EnergySocket(true, "localhost", 8323, "/test");
        energySocket.updateAll();
    }

    @Test
    public void test() throws HomeWizardApiException {
        Assertions.assertEquals("{\"service_name\":null,\"api_enabled\":true,\"host_address\":\"localhost\",\"port\":8323,\"api_path\":\"/test\",\"system_configuration\":{\"cloud_enabled\":true},\"energy_socket_state\":{\"power_on\":true,\"switch_lock\":false,\"brightness\":255},\"product_type\":\"HWE-SKT\",\"product_name\":\"Energy Socket\",\"serial\":\"3c35e7aabbcc\",\"firmware_version\":\"5.18\",\"api_version\":\"v1\",\"wifi_ssid\":\"My Wi-Fi\",\"wifi_strength\":100.0,\"total_power_import_kwh\":30.511,\"total_power_export_kwh\":85.951,\"active_power_w\":543.312,\"active_voltage_v\":231.539,\"active_current_a\":2.346,\"active_reactive_power_var\":123.456,\"active_apparent_power_va\":666.768,\"active_power_factor\":0.81688,\"active_frequency_hz\":50.005}",
                energySocket.toJson());
    }
}
