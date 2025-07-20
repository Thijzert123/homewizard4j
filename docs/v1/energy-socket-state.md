---
title: Energy socket state
parent: V1
nav_order: 5
layout: default
---

# Energy socket state
The energy socket has a _state_, which you can control with the `EnergySocketState`. It allows you to read and change
three values:
- Whether the relay in the switch is on
- Whether the switch lock is active; this means that the socket cannot be turned off
- Brightness of the LED when the socket is on

Just like with the `SystemConfiguration`, you have to save with `save()` in order to update the data on the
physical device. The example below shows how to change the power state.
```java
package io.github.thijzert123.homewizard4j.example.v1;
import io.github.thijzert123.homewizard4j.v1.*;
import java.io.IOException;

public class ChangeAndPrintEnergySocketState {
    public static void main(final String[] args) throws IOException {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer(1000);

        // Loop every energy socket
        for (final EnergySocket energySocket : discoverer.getEnergySockets()) {

            // Get the state and update the data
            final EnergySocketState state = energySocket.getEnergySocketState();
            state.update();

            // Print whether the power is on
            System.out.println("Power on: " + state.getPowerOn());

            state.setPowerOn(false);
            state.save();

            // Update and print whether the power is on
            state.update(); // This isn't technically necessary, but it's good practise
            System.out.println("Power on: " + state.getPowerOn());
        }
    }
}
```
If the socket was on before running this example, this would be the output:
```
Power on: Optional[true]
Power on: Optional[false]
```