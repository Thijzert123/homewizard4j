<div align="center">
  <img src="https://github.com/Thijzert123/homewizard4j/blob/main/logo.png?raw=true" width=100 height=100 />
  <h1>homewizard4j</h1>
  Unofficial Java API for HomeWizard devices

  _automatic discovery_ • _retrieving data_ • _changing configuration_
</div>

## Usage
For more in-depth explanation per class/method, please refer to the Javadocs.

### Discovery
You should always start with a `HomeWizardDiscoverer`. This is able to scan for all HomeWizard devices in your local network.
When initializing the discoverer, it immediately starts scanning for HomeWizard devices. This is done on a different thread than the main thread, so you should wait on your main thread for a few seconds before getting the devices from the `HomeWizardDiscoverer`. This can be done by using `Thread.sleep()`, waiting for a user input or using an algorithm like [exponential backoff](https://en.wikipedia.org/wiki/Exponential_backoff) if you know how many devices you are waiting for.

When you eventually have a `List` with multiple instances of `Device`, you can use `getProductName()` for an user-friendly name representation of the device. This can be useful for debugging purposes, but also for quick examples like this.

Below is how all the code would look together. Notice that you can call `close()` to stop the discoverer from scanning devices. This can be useful if you know you already have all the devices discovered, because when closing the discoverer you can conserve resources. To scan again, you have to create a new `HomeWizardDiscoverer`. You can, however, hand over all devices from one `HomeWizardDiscoverer` instance to another one, with the `HomeWizardDiscoverer(HomeWizardDiscoverer)` constructor.
For more information, please refer to the Javadocs.
```java
package io.github.thijzert123.homewizard4j.example;
import io.github.thijzert123.homewizard4j.v1.*;
import java.util.*;
import java.io.IOException;

public class AutomaticDeviceDiscovery {
    public static void main(final String[] args) throws InterruptedException, IOException {
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
            final Optional<String> productName = device.getProductName();

            System.out.println(productName);
        }
    }
}
```
Example output when a watermeter and P1 meter are discovered:
```
Optional[Watermeter]
Optional[P1 Meter]
```

### System configuration
Every `Device` has a `SystemConfiguration` inside. When using API v1, you can only change whether cloud communication on the device is enabled. To do this, use `getSystemConfiguration()` on your `Device`. The returned `SystemConfiguration` is still empty, so to fill it with the configuration that is currently active on the device, call `update()` on the `SystemConfiguration` instance. Then you can use `isCloudEnabled()` to return whether cloud communication is enabled on the device.

To change a value, simply use one of the setters, which, in this case, is `setCloudEnabled()`. Change it to the value you want, but remember to call `save()` to make sure the changed data is also updated on the device.
In the example below, you can see that after changing the value, `update()` is still called. This isn't _necessary_, but it is recommended, because other programs can change the same value after you have saved your value.
In conclusion, you should call `update()` regularly.
```java
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
```
Example output when one device is discovered and cloud communication wasn't enabled beforehand:
```
Cloud enabled: Optional[false]
Cloud enabled: Optional[true]
```

### Updating data
You might have already noticed that most getters of a `Device` return an `Optional` of some kind. This has two reasons:
- For some fields, you first have to update the device by calling `update*()` methods. In the Javadocs you can see what update method you have to call for a specific field to update.
- Not all data points are returned by the official API when updating. When you don't use gas, the P1 meter won't return datapoints that are about gas.

The default value for all fields is `Optional.empty()` (or with another form of `Optional`, like `OptionalInt` or `OptionalDouble`), except for some. These fields are required when initializing the class, so you can always access them. Some of these values are never able to change (product type), others _should_ never change, like the api version: this can _technically_ change, but it doesn't because the api version is dependent on how you do HTTP request; these are always the same.
