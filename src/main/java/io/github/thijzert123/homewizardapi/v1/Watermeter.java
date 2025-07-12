package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device to measure water use.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#watermeter-hwe-wtr">Official API documentation for this device</a>
 *
 * @author Thijzert123
 */
public class Watermeter extends Device {
    /**
     * @see Device#getProductType()
     */
    public static final String PRODUCT_TYPE = "HWE-WTR";
    private final String productName;

    private final String serviceName;
    private final String hostAddress;
    private final int port;

    private final boolean apiEnabled;
    private final String apiPath;
    private final String serial;

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

    Watermeter(final String serviceName,
               final String hostAddress,
               final int port,
               final boolean apiEnabled,
               final String apiPath,
               final String serial,
               final String productName) {
        this.serviceName = serviceName;
        this.hostAddress = hostAddress;
        this.port = port;
        this.apiEnabled = apiEnabled;
        this.apiPath = apiPath;
        this.serial = serial;
        this.productName = productName;
    }

    public boolean updateDeviceInfo() {
        return super.updateDeviceInfo(this);
    }

    public boolean updateMeasurements() {
        return super.updateMeasurements(this);
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getHostAddress() {
        return hostAddress;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public boolean isApiEnabled() {
        return apiEnabled;
    }

    @Override
    public String getApiPath() {
        return apiPath;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public String getProductType() {
        return PRODUCT_TYPE;
    }

    @Override
    public String getProductName() {
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
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return total water usage in cubic meters since installation
     */
    public OptionalDouble getTotalLiterM3() {
        return totalLiterM3;
    }

    /**
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return active water usage in liters per minute
     */
    public OptionalDouble getActiveLiterLpm() {
        return activeLiterLpm;
    }

    /**
     * According to the official API documentation, this value is in development and should not be used.
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
