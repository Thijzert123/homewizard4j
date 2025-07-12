package io.github.thijzert123.homewizardapi.test;

import io.github.thijzert123.homewizardapi.v1.HomeWizardDiscoverer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Thijzert123
 */
public class TestHW {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    public void test() {
        final HomeWizardDiscoverer discoverer = new HomeWizardDiscoverer();
        try {
            Thread.sleep(1000);
            discoverer.getWatermeters().forEach((watermeter -> {
                watermeter.updateAll();
                LOGGER.info(watermeter.toString());
            }));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
