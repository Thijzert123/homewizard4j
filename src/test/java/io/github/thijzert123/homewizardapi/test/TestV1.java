package io.github.thijzert123.homewizardapi.test;

import io.github.thijzert123.homewizardapi.v1.HomeWizardDiscoverer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * @author Thijzert123
 */
public class TestV1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void test() {
        try (final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer()) {
            Thread.sleep(1000);
            discoverer.getAllDevices().forEach(device -> {
                device.updateAll();
                LOGGER.info(device.toString());
            });
        } catch (InterruptedException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
