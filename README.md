<div align="center">
  <img src="https://github.com/Thijzert123/homewizard4j/blob/main/logo.png?raw=true" alt="homewizard4j logo" width=100 height=100 />
  <h1>homewizard4j</h1>
  Unofficial Java API for HomeWizard devices

  _automatic discovery_ • _retrieving data_ • _changing configuration_

  [![Maven-central](https://img.shields.io/maven-central/v/io.github.thijzert123/homewizard4j?style=for-the-badge&logo=maven&color=blue)](https://central.sonatype.com/artifact/io.github.thijzert123/homewizard4j)
  [![Javadoc](https://javadoc.io/badge2/io.github.thijzert123/homewizard4j/javadoc.svg?color=yellow&style=for-the-badge)](https://javadoc.io/doc/io.github.thijzert123/homewizard4j) 
  [![License](https://img.shields.io/github/license/Thijzert123/homewizard4j?style=for-the-badge&color=A9CF04)](https://opensource.org/license/apache-2-0)
  [![Code quality](https://img.shields.io/codefactor/grade/github/Thijzert123/homewizard4j?style=for-the-badge)](https://www.codefactor.io/repository/github/thijzert123/homewizard4j)
</div>

## Compatibility
Currently only the `v1` API is supported. Support for `v2` is in development. All devices (that are natively compatible with `v1`) are implemented in this Java API, but `EnergySocket` and `KWhMeter` are not tested on real devices. They _should_ work, but if you have encountered a bug or error, please open an issue.

## Usage
For more in-depth explanation per class/method than is mentioned on this page, please refer to the [Javadocs](https://javadoc.io/doc/io.github.thijzert123/homewizard4j). It is also recommended to take a look at the [Official HomeWizard API documentation](https://api-documentation.homewizard.com/docs/introduction).

All the code examples below are also available for you to see/run at [`src/test/java/io/github/thijzert123/homewizard4j/example`](https://github.com/Thijzert123/homewizard4j/tree/main/src/test/java/io/github/thijzert123/homewizard4j/example).

### Adding the dependency
Use one of the snippets below or get one at [Maven Central](https://central.sonatype.com/artifact/io.github.thijzert123/homewizard4j).

#### Maven
```xml
<dependency>
    <groupId>io.github.thijzert123</groupId>
    <artifactId>homewizard4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Gradle
```gradle
implementation group: 'io.github.thijzert123', name: 'homewizard4j', version: '1.0.0'
```

### Discovery
You should always start with a `HomeWizardDiscoverer`. This can scan for all HomeWizard devices in your local network.
When initializing the discoverer, it immediately starts scanning for HomeWizard devices. This is done on a different thread than the main thread, so you should wait on your main thread for a few seconds before getting the devices from the `HomeWizardDiscoverer`. This can be achieved by using `Thread.sleep()`, waiting for user input, or employing an algorithm like [exponential backoff](https://en.wikipedia.org/wiki/Exponential_backoff) if you know the number of devices to wait for.

When you eventually have a `List` with multiple instances of `Device`, you can use `getProductName()` for a user-friendly name representation of the device. This can be useful for debugging purposes, but also for quick examples like this.

Below is how all the code would look together. Notice that you can call `close()` to stop the discoverer from scanning devices. This can be useful if you know you already have all the devices discovered because when closing the discoverer you can conserve resources. To scan again, you have to create a new `HomeWizardDiscoverer`. You can, however, hand over all devices from one `HomeWizardDiscoverer` instance to another one, with the `HomeWizardDiscoverer(HomeWizardDiscoverer)` constructor.
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
Example output when a water meter and P1 meter are discovered:
```
Optional[Watermeter]
Optional[P1 Meter]
```

### Manual devices
You can also choose not to use the discoverer but instead initialize a `Device` yourself. For this, you need the IP address (or domain name) of the device. In the example below you see that this is not the only data we pass to the constructor.
That is because we also pass whether the API is enabled on the device (hence the `true` value). Then we pass the IP/domain name, port, and the API path. You can also choose to use the constructor where you only pass the IP. The other values will be the default:
- API enabled: `true`
- Port: `80`
- API path: `/api/v1`

When using a manually added `Device`, you lose access to some of its fields, for example, `getServiceName()`. That is because this data is only passed to the `Device` initializer when using the discoverer. For more information about if or when data is available, check the methods in the Javadocs.
```java
package io.github.thijzert123.homewizard4j.example;
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

### System configuration
Every `Device` has a `SystemConfiguration` inside. When using API v1, you can only change whether cloud communication on the device is enabled. To do this, use `getSystemConfiguration()` on your `Device`. The returned `SystemConfiguration` is still empty, so to fill it with the configuration that is currently active on the device, call `update()` on the `SystemConfiguration` instance. Then you can use `isCloudEnabled()` to return whether cloud communication is enabled on the device.

To change a value, simply use one of the setters, which, in this case, is `setCloudEnabled()`. Change it to the value you want, but remember to call `save()` to make sure the changed data is also updated on the device.
In the example below, you can see that after changing the value, `update()` is still called. This isn't _necessary_, but it is recommended because other programs can change the same value after you have saved your value.
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
        }
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
- For some fields, you first have to update the device by calling one of the `update*()` methods. In the Javadocs you can see what update method you have to call for a specific field to update.
- Not all data points are returned by the official API when updating. When you don't use gas, the P1 meter won't return data points that are about gas.

The default value for all fields is `Optional.empty()` (or with another form of `Optional`, like `OptionalInt` or `OptionalDouble`), except for some. These fields are required when initializing the class, so you can always access them. Some of these values are never able to change, for example, host address and port.

---
_This project is not affiliated with HomeWizard._
