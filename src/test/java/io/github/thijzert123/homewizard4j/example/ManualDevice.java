package io.github.thijzert123.homewizard4j.example;
import io.github.thijzert123.homewizard4j.v1.*;
import java.util.*;
import java.io.IOException;

public class ManualDevice {
    public static void main(final String[] args) throws IOException {
        // Initialize watermeter with specified IP
        final Watermeter watermeter = new Watermeter(true, "192.168.1.123", 80, "/api/v1");

        // Update all possible data
        watermeter.updateAll();

        // Get product name and print
        final Optional<String> productName = watermeter.getProductName();
        System.out.println(productName);
    }
}
