package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String firmwareVersion;
    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("wifi_ssid")
    private String wifiSsid;
    @JsonProperty("wifi_strength")
    private double wifiStrength;

    @JsonProperty("total_liter_m3")
    private double totalLiterM3;
    @JsonProperty("active_liter_lpm")
    private double activeLiterLpm;
    @JsonProperty("total_liter_offset_m3")
    private double totalLiterOffsetM3;

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
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String getWifiSsid() {
        return wifiSsid;
    }

    @Override
    public double getWifiStrength() {
        return wifiStrength;
    }

    /**
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return total water usage in cubic meters since installation
     */
    public double getTotalLiterM3() {
        return totalLiterM3;
    }

    /**
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return active water usage in liters per minute
     */
    public double getActiveLiterLpm() {
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
    public double getTotalLiterOffsetM3() {
        return totalLiterOffsetM3;
    }
}
