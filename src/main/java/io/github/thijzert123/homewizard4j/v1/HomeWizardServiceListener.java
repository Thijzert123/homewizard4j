package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Thijzert123
 */
class HomeWizardServiceListener implements ServiceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HomeWizardDiscoverer discoverer;

    HomeWizardServiceListener(final HomeWizardDiscoverer discoverer) {
        this.discoverer = discoverer;
    }

    @Override
    public void serviceAdded(final ServiceEvent serviceEvent) {
        LOGGER.debug("Service added: {}", serviceEvent.getInfo());
    }

    @Override
    public void serviceRemoved(final ServiceEvent serviceEvent) {
        LOGGER.debug("Service removed: {}", serviceEvent.getInfo());
    }

    @Override
    public void serviceResolved(final ServiceEvent serviceEvent) {
        LOGGER.debug("Service resolved: {}", serviceEvent.getInfo());

        final ServiceInfo serviceInfo = serviceEvent.getInfo();
        final String productType = serviceInfo.getPropertyString("product_type");

        LOGGER.trace("Product type: {}", productType);
        if (Objects.equals(productType, Watermeter.PRODUCT_TYPE)) {
            LOGGER.trace("Product type Watermeter");
            addWatermeter(serviceInfo);
        } else if (Objects.equals(productType, P1Meter.PRODUCT_TYPE)) {
            LOGGER.trace("Product type P1Meter");
            addP1Meter(serviceInfo);
        }
    }

    private void addWatermeter(final ServiceInfo serviceInfo) {
        discoverer.watermeters.add(new Watermeter(
                Optional.of(serviceInfo.getQualifiedName()),
                Objects.equals(serviceInfo.getPropertyString("api_enabled"), "1"),
                serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                serviceInfo.getPort(),
                serviceInfo.getPropertyString("path"),
                Optional.of(serviceInfo.getPropertyString("product_name")),
                Optional.of(serviceInfo.getPropertyString("product_name"))
        ));
    }

    private void addP1Meter(final ServiceInfo serviceInfo) {
        discoverer.p1Meters.add(new P1Meter(
                Optional.of(serviceInfo.getQualifiedName()),
                Objects.equals(serviceInfo.getPropertyString("api_enabled"), "1"),
                serviceInfo.getHostAddresses()[0], // HomeWizard stuff should only have 1 host address
                serviceInfo.getPort(),
                serviceInfo.getPropertyString("path"),
                Optional.of(serviceInfo.getPropertyString("product_name")),
                Optional.of(serviceInfo.getPropertyString("product_name"))
        ));
    }
}
