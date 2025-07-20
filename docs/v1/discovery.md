---
title: Discovery
parent: V1
nav_order: 1
layout: default
---

# Discovery
If you don't know the IP addresses of your devices, you can use the `HomeWizardDiscoverer`.
This scans your local network for all HomeWizard devices. When initializing the discoverer,
it immediately starts scanning. This is done on a different thread than the main thread,
so you should wait on your main thread for a few seconds before getting the devices from the `HomeWizardDiscoverer`.
This can be achieved by using the `waitForMillis(long)` method,
waiting for user input, or using `waitForDeviceCount(DeviceType, int)`.
In this example, a wait time of 1000 milliseconds (1 second) is used, but it could be possible that you need
to increase it before it discovers your devices.

When you eventually have a `List` with multiple instances of `Device`,
you can use `getProductName()` for a user-friendly name representation of the device.
This can be useful for debugging purposes, but also for quick examples like this.

Below is how all the code would look together.
Notice that you can call `close()` to stop the discoverer from scanning devices.
This can be useful if you know you already have all the devices discovered because when closing the discoverer you conserve resources.
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

## Merging a discoverer
As mentioned before, when you close a discoverer, you cannot make it start discovering again. For this, you need to
create a new instance. That instance, however, does not have any devices discovered, so you would have to give up
valuable time in order to discover what you have earlier discovered with another instance. But, there is a solution:
the `HomeWizardDiscoverer(HomeWizardDiscoverer)` constructor. It takes the provided discoverer, gets all its
discovered devices and adds them to his devices. You can see it in action in this code example:
```java
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
```
If `discoverer1` discovers 2 devices and `discoverer2` discovers 1 additional device, the output would look like this:
```
Discoverer 1 device count: 2
Discoverer 2 device count: 3
```