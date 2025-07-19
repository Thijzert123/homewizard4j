---
title: Discovery
parent: V1
nav_order: 1
layout: default
---

# Discovery
If you don't know the IP addresses of your devices, you can use the `HomeWizardDiscoverer`. This scans your local network for all HomeWizard devices.
When initializing the discoverer, it immediately starts scanning. This is done on a different thread than the main thread, so you should wait on your main thread for a few seconds before getting the devices from the `HomeWizardDiscoverer`. This can be achieved by using the `waitForMillis(long)` method, waiting for user input, or using `waitForDeviceCount(DeviceType, int)`.

When you eventually have a `List` with multiple instances of `Device`, you can use `getProductName()` for a user-friendly name representation of the device. This can be useful for debugging purposes, but also for quick examples like this.

Below is how all the code would look together. Notice that you can call `close()` to stop the discoverer from scanning devices. This can be useful if you know you already have all the devices discovered because when closing the discoverer you can conserve resources. To scan again, you have to create a new `HomeWizardDiscoverer`. You can, however, hand over all devices from one `HomeWizardDiscoverer` instance to another one, with the `HomeWizardDiscoverer(HomeWizardDiscoverer)` constructor.
For more information, please refer to the Javadocs.
```java
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
```
Example output when a water meter and P1 meter are discovered:
```
Optional[Watermeter]
Optional[P1 Meter]
```

If you want a quicker way of initializing the discoverer, waiting for it to find devices and closing the discoverer,
you can also use the `HomeWizardDiscoverer(long)` constructor. This initializes the discoverer like you would
normally, but then it waits for the specified amount of time in milliseconds. After it is done with waiting,
it closes the discoverer to preserve resources, but still allowing you to access all the discovered devices.

In conclusion, this code:
```java
final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer(1000);
```
does the same as this code:
```java
final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
discoverer.waitForMillis(1000);
discoverer.close();
```