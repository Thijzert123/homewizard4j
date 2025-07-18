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

## Documentation
You can use one of these three documentation sources when using this project:
- [Main `homewizard4j` documentation](https://thijzert123.github.io/homewizard4j)
- [Javadoc](https://javadoc.io/doc/io.github.thijzert123/homewizard4j)
- [Official HomeWizard API documentation](https://api-documentation.homewizard.com)

## Quick start
If you just want a quick example, follow this guide.

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

### Discover all devices
If you don't know the IP addresses of your devices, you can use the `HomeWizardDiscoverer`. It scans your local network
for HomeWizard devices. With the returned `Device` instances, you can get lots of information and measurements.
In this example, the product name (for example: `P1 Meter`) of all discovered devices get printed:
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