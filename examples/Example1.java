import io.github.thijzert123.homewizard4j.v1.*;
import java.io.IOException;
import java.util.List;

public class Example1 {
    public void printAllDeviceNames() throws InterruptedException, IOException {
        // Create the discoverer and start scanning for HomeWizard devices.
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();

        // Wait for a second to give the discoverer time
        // to discover HomeWizard devices.
        Thread.sleep(1000);

        // Get all discovered devices
        final List<Device> deviceList = discoverer.getAllDevices();

        // Makes the discoverer stop scanning for devices
        discoverer.close();

        for (final Device device : deviceList) {
            // User-friendly name of the device
            final String productName = device.getProductName();

            System.out.println(productName);
        }
    }
}