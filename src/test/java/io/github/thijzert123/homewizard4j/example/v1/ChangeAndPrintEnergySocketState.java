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
