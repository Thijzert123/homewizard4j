---
title: Manual devices
parent: V1
nav_order: 2
layout: default
---

# Manual devices
You can also choose not to use the discoverer but instead initialize a `Device` yourself. For this, you need the IP address (or domain name) of the device. In the example below you see that this is not the only data we pass to the constructor.
That is because we also pass whether the API is enabled on the device (hence the `true` value). Then we pass the IP/domain name, port, and the API path. You can also choose to use the constructor where you only pass the IP. The other values will be the default:
- API enabled: `true`
- Port: `80`
- API path: `/api/v1`

When using a manually added `Device`, you lose access to some of its fields, for example, `getServiceName()`. That is because this data is only passed to the `Device` initializer when using the discoverer. For more information about if or when data is available, check the methods in the Javadocs.
```java
package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.util.*;
import java.io.IOException;

public class ManualDevice {
    public static void main(final String[] args) throws IOException {
        // Initialize water meter with specified IP
        final WaterMeter waterMeter = new WaterMeter(true, "192.168.1.123", 80, "/api/v1");

        // Update all possible data
        waterMeter.updateAll();

        // Get product name and print
        final Optional<String> productName = waterMeter.getProductName();
        System.out.println(productName);
    }
}
```
Output:
```
Optional[Watermeter]
```