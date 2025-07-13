<div align="center">
  <img src="https://github.com/Thijzert123/homewizard4j/blob/main/logo.png?raw=true" width=100 height=100/>
  <h1>homewizard4j</h1>
  Unofficial Java API for HomeWizard devices

  _retrieving data_ • _changing configuration_ • _error handling_
</div>

## Usage
### Discovery
You should always start with a `HomeWizardDiscoverer`. This is able to scan for all HomeWizard devices in your local network.
When initializing the discoverer, it immediately starts scanning for HomeWizard devices. This is done on a different thread than the main thread, so you should wait on your main thread for a few seconds before getting the devices from the `HomeWizardDiscoverer`. This can be done by using `Thread.sleep()`, waiting for a user input or using an algorithm like [exponential backoff](https://en.wikipedia.org/wiki/Exponential_backoff) if you know how many devices to be scanned you are waiting for.

When you eventually have a `List` with multiple instances of `Device`, you can use `getProductName()` for an user-friendly name representation of the device. This can be useful for debugging purposes, but also for quick examples like this.

Below is how all the code would look together. Notice that you can call `close()` to stop the discoverer from scanning devices. This can be useful if you know you already have all the devices discovered, because when closing the discoverer you can conserve resources. To scan again, you have to create a new `HomeWizardDiscoverer`. You can, however, hand over all devices from one `HomeWizardDiscoverer` instance to another one, with the `HomeWizardDiscoverer(HomeWizardDiscoverer)` constructor.
For more information, please refer to the Javadocs.
```java
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
```
### System configuration
Every `Device` has a `SystemConfiguration` inside. When using API v1, you can only change whether cloud communication on the device is enabled. To do this, use `getSystemConfiguration()` on your `Device`. The returned `SystemConfiguration` is still empty, so to fill it with the configuration that is currently active on the device, call `update()` on the `SystemConfiguration` instance. Then you can use `isCloudEnabled()` to return whether cloud communication is enabled on the device.

To change a value... (coming soon)
```java
import io.github.thijzert123.homewizard4j.v1.*;

public class Example2 {
    public void changeAndPrintCloudCommunication() throws InterruptedException {
        // Used for retrieving all HomeWizard devices
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
```
