package io.github.thijzert123.homewizardapi.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

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
        discoverer.allServiceInfo.remove(serviceEvent.getInfo());
    }

    @Override
    public void serviceResolved(final ServiceEvent serviceEvent) {
        LOGGER.debug("Service resolved: {}", serviceEvent.getInfo());

        final ServiceInfo serviceInfo = serviceEvent.getInfo();
        discoverer.allServiceInfo.add(serviceInfo);

        final String productType = serviceInfo.getPropertyString("product_type");
        LOGGER.trace("Product type: {}", productType);
        if (Objects.equals(productType, Watermeter.PRODUCT_TYPE)) {
            LOGGER.trace("Product type Watermeter");
            discoverer.allWatermeterServiceInfo.add(serviceInfo);
        } else if (Objects.equals(productType, P1Meter.PRODUCT_TYPE)) {
            LOGGER.trace("Product type P1Meter");
            discoverer.allP1MeterServiceInfo.add(serviceInfo);
        }
    }
}
