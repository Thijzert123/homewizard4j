package io.github.thijzert123.homewizard4j.v2;

import java.util.List;
import java.util.Optional;

/**
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PlugInBattery extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-BAT"); // TODO product type is maybe https://api-documentation.homewizard.com/docs/v2/authorization#hostname-validation

    PlugInBattery(final Optional<String> serviceName,
                  final String hostAddress,
                  final int port,
                  final Optional<String> productType,
                  final Optional<String> productName,
                  final Optional<String> serial,
                  final Optional<String> id,
                  final Optional<String> apiVersion) {
        super(
                serviceName,
                hostAddress,
                port,
                productType,
                productName,
                serial,
                id,
                apiVersion
        );
    }
}
