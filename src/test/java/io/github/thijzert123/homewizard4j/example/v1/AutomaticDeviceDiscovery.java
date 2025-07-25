package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.util.*;
import java.io.IOException;

public class AutomaticDeviceDiscovery {
    public static void main(final String[] args) throws IOException {
        // Create the discoverer and start scanning for HomeWizard devices.
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();

        // Wait for 1 second to give the discoverer time
        // to discover HomeWizard devices.
        discoverer.waitForMillis(1000);

        // Get all discovered devices
        final List<Device> deviceList = discoverer.getAllDevices();

        // Makes the discoverer stop scanning for devices
        discoverer.close();

        for (final Device device : deviceList) {
            // User-friendly name of the device
            final Optional<String> productName = device.getProductName();

            System.out.println(productName);
        }
    }
}