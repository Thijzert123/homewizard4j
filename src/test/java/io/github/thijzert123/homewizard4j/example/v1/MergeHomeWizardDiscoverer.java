package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.io.IOException;

public class MergeHomeWizardDiscoverer {
    public static void main(final String[] args) throws IOException {
        // Initialize the first discoverer, scan for 1 second and close it
        final HomeWizardDiscoverer discoverer1 = new HomeWizardDiscoverer(1000);

        // Print the number of devices for discoverer1
        final int discoverer1DeviceCount = discoverer1.getAllDevices().size();
        System.out.println("Discoverer 1 device count: " + discoverer1DeviceCount);

        // Create new discoverer and merge discoverer1 with discoverer2
        final HomeWizardDiscoverer discoverer2 = new HomeWizardDiscoverer(discoverer1);
        // Scan for 1 second, then close discoverer2
        discoverer2.waitForMillis(1000).close();

        // Print the number of devices for discoverer2
        final int discoverer2DeviceCount = discoverer2.getAllDevices().size();
        System.out.println("Discoverer 2 device count: " + discoverer2DeviceCount);
    }
}
