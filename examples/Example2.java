import io.github.thijzert123.homewizard4j.v1.*;

public class Example2 {
    public void changeAndPrintCloudCommunication() throws InterruptedException {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
        Thread.sleep(1000);

        discoverer.getAllDevices().forEach(device -> {
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
        });
    }
}