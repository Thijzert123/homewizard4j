package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device to measure water use.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#watermeter-hwe-wtr">Official API documentation related to this class</a>
 *
 * @author Thijzert123
 * @see Device
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Watermeter extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-WTR");

    @JsonProperty("product_type")
    private final Optional<String> productType;
    @JsonProperty("product_name")
    private final Optional<String> productName;
    @JsonProperty("serial")
    private final Optional<String> serial;

    @JsonProperty("firmware_version")
    private final Optional<String> firmwareVersion = Optional.empty();
    @JsonProperty("api_version")
    private final Optional<String> apiVersion = Optional.empty();

    @JsonProperty("wifi_ssid")
    private final Optional<String> wifiSsid = Optional.empty();
    @JsonProperty("wifi_strength")
    private final OptionalDouble wifiStrength = OptionalDouble.empty();

    @JsonProperty("total_liter_m3")
    private final OptionalDouble totalLiterM3 = OptionalDouble.empty();
    @JsonProperty("active_liter_lpm")
    private final OptionalDouble activeLiterLpm = OptionalDouble.empty();
    @JsonProperty("total_liter_offset_m3")
    private final OptionalDouble totalLiterOffsetM3 = OptionalDouble.empty();

    Watermeter(final Optional<String> serviceName,
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
                apiPath
        );

        this.productType = productType;
        this.productName = productName;
        this.serial = serial;
    }

    /**
     * Manually create a {@link Watermeter}. When getting devices this way, some methods become useless,
     * for example {@link #getServiceName()}. For more information on what data is available,
     * check the Javadocs for the methods.
     *
     * @param apiEnabled whether the API is enabled on the device: you have to check this yourself!
     * @param hostAddress host address, like {@code 192.168.1.123}
     * @param port port, should be {@code 80}
     * @param apiPath API path, should be {@code /api/v1}
     */
    public Watermeter(final boolean apiEnabled,
                      final String hostAddress,
                      final int port,
                      final String apiPath) {
        this(
                Optional.empty(),
                apiEnabled,
                hostAddress,
                port,
                apiPath,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }

    /**
     * Manually create a {@link Watermeter}. This assumes that the API is enabled. It is important that you have checked
     * this before, because if the API is not actually enabled, some exceptions when updating could occur.
     * <p>
     * For port and API path, the values {@link Device#DEFAULT_PORT} and {@link Device#DEFAULT_API_PATH} are used.
     *
     * @param hostAddress the host
     */
    public Watermeter(final String hostAddress) {
        this(
                true,
                hostAddress,
                Device.DEFAULT_PORT,
                Device.DEFAULT_API_PATH
        );
    }

    @Override
    public void updateDeviceInfo() throws HomeWizardApiException {
        updateDeviceInfo(this);
    }

    @Override
    public void updateMeasurements() throws HomeWizardApiException {
        updateMeasurements(this);
    }

    @Override
    public Optional<String> getSerial() {
        return serial;
    }

    @Override
    public Optional<String> getProductType() {
        return productType;
    }

    @Override
    public Optional<String> getProductName() {
        return productName;
    }

    @Override
    public Optional<String> getFirmwareVersion() {
        return firmwareVersion;
    }

    @Override
    public Optional<String> getApiVersion() {
        return apiVersion;
    }

    @Override
    public Optional<String> getWifiSsid() {
        return wifiSsid;
    }

    @Override
    public OptionalDouble getWifiStrength() {
        return wifiStrength;
    }


    /**
     * Returns the total water usage in cubic meters (m^3) since the installation of the device.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return total water usage in cubic meters since installation
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalLiterM3() {
        return totalLiterM3;
    }

    /**
     * Returns the active water usage in liters per minute.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return active water usage in liters per minute
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveLiterLpm() {
        return activeLiterLpm;
    }

    /**
     * Returns total liter offset.
     * According to the official API documentation, this value is in development and should not be used.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return total liter offset
     * @deprecated value is in development and should not be used, annotation is used as a warning for IDEs
     */
    @Deprecated
    public OptionalDouble getTotalLiterOffsetM3() {
        return totalLiterOffsetM3;
    }
}
