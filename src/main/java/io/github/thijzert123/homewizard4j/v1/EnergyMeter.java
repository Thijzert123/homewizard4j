package io.github.thijzert123.homewizard4j.v1;

import java.util.Optional;

/**
 * Some kind of energy meter that is able to measure energy use.
 *
 * @author Thijzert123
 * @since 2.0.0
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class EnergyMeter extends Device {
    EnergyMeter(final Optional<String> serviceName,
                final boolean apiEnabled,
                final String hostAddress,
                final int port,
                final String apiPath,
                final Optional<String> productType,
                final Optional<String> productName,
                final Optional<String> serial) {
        super(
                serviceName,
                apiEnabled,
                hostAddress,
                port,
                apiPath,
                productType,
                productName,
                serial
        );
    }
}
