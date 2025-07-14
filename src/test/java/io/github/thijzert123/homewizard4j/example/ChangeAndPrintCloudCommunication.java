package io.github.thijzert123.homewizard4j.example;
import io.github.thijzert123.homewizard4j.v1.*;
import java.io.IOException;

public class ChangeAndPrintCloudCommunication {
    public static void main(final String[] args) throws InterruptedException, IOException {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
        Thread.sleep(1000);

        for (final Device device : discoverer.getAllDevices()) {
            final SystemConfiguration configuration = device.getSystemConfiguration();

            // Update fields and print data
            configuration.update();
            System.out.println("Cloud enabled: " + configuration.isCloudEnabled());

            // Set data and save to the device
            configuration.setCloudEnabled(true);
            configuration.save();

            // Update fields and print data
            configuration.update(); // This isn't technically necessary, but it's good practise
            System.out.println("Cloud enabled: " + configuration.isCloudEnabled());
        };
    }
}