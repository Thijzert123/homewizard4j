package io.github.thijzert123.homewizardapi.test;

import io.github.thijzert123.homewizardapi.v1.HomeWizardDiscoverer;
import io.github.thijzert123.homewizardapi.v1.Watermeter;
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
            discoverer.getAllServiceInfo().forEach((serviceInfo) -> {
                System.out.println(serviceInfo.getPropertyString("product_type"));
            });
            discoverer.getWatermeters().forEach((watermeter -> {
                LOGGER.info("{}", watermeter.toString());
                watermeter.identify();
                LOGGER.info(String.valueOf(watermeter.updateDeviceInfo()));
                LOGGER.info(String.valueOf(watermeter.updateMeasurements()));
                LOGGER.info(String.valueOf(watermeter.getFirmwareVersion()));
                LOGGER.info(String.valueOf(watermeter.getTotalLiterM3()));
            }));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
