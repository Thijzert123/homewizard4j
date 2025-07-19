package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.util.*;
import java.io.IOException;

public class ManualDevice {
    public static void main(final String[] args) throws IOException {
        // Initialize water meter with specified IP
        final WaterMeter waterMeter = new WaterMeter(true, "192.168.1.123", 80, "/api/v1");

        // Update all possible data
        waterMeter.updateAll();

        // Get product name and print
        final Optional<String> productName = waterMeter.getProductName();
        System.out.println(productName);
    }
}
