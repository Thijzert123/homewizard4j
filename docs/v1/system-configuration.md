---
title: System configuration
parent: V1
nav_order: 3
layout: default
---

# System configuration
Every `Device` has a `SystemConfiguration` inside. When using API v1, you can only change whether cloud communication on the device is enabled. To do this, use `getSystemConfiguration()` on your `Device`. The returned `SystemConfiguration` is still empty, so to fill it with the configuration that is currently active on the device, call `update()` on the `SystemConfiguration` instance. Then you can use `isCloudEnabled()` to return whether cloud communication is enabled on the device.

To change a value, simply use one of the setters, which, in this case, is `setCloudEnabled()`. Change it to the value you want, but remember to call `save()` to make sure the changed data is also updated on the device.
In the example below, you can see that after changing the value, `update()` is still called. This isn't _necessary_, but it is recommended because other programs can change the same value after you have saved your value.
In conclusion, you should call `update()` regularly.
```java
package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.io.IOException;

public class ChangeAndPrintCloudCommunication {
    public static void main(final String[] args) throws IOException {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer(1000);

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
        }
    }
}
```
Example output when one device is discovered and cloud communication wasn't enabled beforehand:
```
Cloud enabled: Optional[false]
Cloud enabled: Optional[true]
```