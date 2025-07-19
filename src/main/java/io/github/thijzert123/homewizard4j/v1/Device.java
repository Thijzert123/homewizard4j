package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A HomeWizard device, such as a Water meter or P1-meter. To get an instance, you can use {@link HomeWizardDiscoverer}
 * or manually initialize it. For more information about that, see
 * <a href="https://github.com/Thijzert123/homewizard4j?tab=readme-ov-file#manual-devices">manual devices</a>.
 * <p>
 * For some methods, you first need to call an update method ({@link #updateDeviceInfo()} or {@link #updateMeasurements()})
 * before they become useful.
 * If you don't do that, they return an empty {@link Optional}.
 * See <a href="https://github.com/Thijzert123/homewizard4j#updating-data">updating data</a> for more information.
 *
 * @author Thijzert123
 * @see Optional
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class Device extends Updatable {
    /**
     * The default port of the API.
     */
    public static final int DEFAULT_PORT = 80;
    /**
     * The default API path. This is what {@link String} you append to the IP.
     * In {@code 192.168.1.123/api/v1} is {@code /api/v1} the API path.
     */
    public static final String DEFAULT_API_PATH = "/api/v1";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonProperty("service_name")
    private final Optional<String> serviceName;
    @JsonProperty("api_enabled")
    private final boolean apiEnabled;
    @JsonProperty("host_address")
    private final String hostAddress;
    @JsonProperty("port")
    private final int port;
    @JsonProperty("api_path")
    private final String apiPath;

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

    @JsonProperty("system_configuration")
    private final SystemConfiguration systemConfiguration;

    Device(final Optional<String> serviceName,
           final boolean apiEnabled,
           final String hostAddress,
           final int port,
           final String apiPath,
           final Optional<String> productType,
           final Optional<String> productName,
           final Optional<String> serial) {
        LOGGER.trace("Initializing Device...");

        this.serviceName = serviceName;
        this.apiEnabled = apiEnabled;
        this.hostAddress = hostAddress;
        this.port = port;
        this.apiPath = apiPath;

        this.productType = productType;
        this.productName = productName;
        this.serial = serial;

        systemConfiguration = new SystemConfiguration(this);
    }

    /**
     * Generates a JSON based on all the currently loaded values.
     * The generated JSON won't be compatible with the official HomeWizard API,
     * but you can use it to save {@link Device} instances to files.
     * <p>
     * Empty {@link Optional} values will be {@code null}!
     *
     * @return JSON based on all the currently loaded values
     * @throws HomeWizardApiException when something has gone wrong
     * @since 2.0.0
     */
    public String toJson() throws HomeWizardApiException {
        LOGGER.trace("Mapping Device to JSON...");
        try {
            final String json = objectMapper.writeValueAsString(this);
            LOGGER.debug("Mapped Device to JSON from JSON: '{}'", json);
            return json;
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Updates data based on the provided JSON.
     *
     * @param json JSON to use for updating
     * @throws HomeWizardApiException when something has gone wrong
     * @since 2.0.0
     */
    public void updateFromJson(final String json) throws HomeWizardApiException {
        LOGGER.trace("Updating Device from JSON: '{}'", json);
        try {
            final Device device = objectMapper.readerForUpdating(this).readValue(json);
            LOGGER.debug("Updated Device from JSON");

            // done to make sure the configuration has access to fields of this instance
            systemConfiguration.updatePrivateFields(this);
            if (this instanceof EnergySocket energySocket) {
                energySocket.getEnergySocketState().updatePrivateFields(device);
            }
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Updates the fields related to the device info. Requires {@link #isApiEnabled()} to be <code>true</code>.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @throws HomeWizardApiException when something has gone wrong while updating
     * @see #isApiEnabled()
     */
    public void updateDeviceInfo() throws HomeWizardApiException {
        LOGGER.trace("Updating device info...");
        update(getFullAddress() + "/api");
    }

    /**
     * Updates the fields related to measurements. Requires {@link #isApiEnabled()} to be <code>true</code>.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement">Official API documentation related to this method</a>
     *
     * @throws HomeWizardApiException when something has gone wrong while updating
     * @see #isApiEnabled()
     */
    public void updateMeasurements() throws HomeWizardApiException {
        LOGGER.trace("Updating measurements...");
        update(getFullApiAddress() + "/data");
    }

    /**
     * Calls {@link #updateDeviceInfo()} and {@link #updateMeasurements()}.
     * It retrieves the system configuration vis {@link #getSystemConfiguration()}
     * and calls {@link SystemConfiguration#update()}.
     *
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public void updateAll() throws HomeWizardApiException {
        LOGGER.trace("Updating all...");
        updateDeviceInfo();
        updateMeasurements();
        getSystemConfiguration().update();
    }

    /**
     * The status light of the device will blink for a few seconds after calling this method,
     * allowing someone to identify the physical device.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/identify">Official API documentation related to this method</a>
     *
     * @throws HomeWizardApiException when something has gone wrong when requesting blinking the status light
     */
    public void identify() throws HomeWizardApiException {
        LOGGER.debug("Identify Device");
        HttpUtils.getBody("PUT", getFullApiAddress() + "/identify");
    }

    /**
     * Returns the full qualified mDNS service name.
     * For example: <code>watermeter-ABC123._hwenergy._tcp.local.</code>.
     * <p>
     * This information is always available if the instance was returned by {@link HomeWizardDiscoverer}.
     * Otherwise, you can never get this information, even if you use one of the update methods.
     *
     * @return full qualified service name
     */
    public Optional<String> getServiceName() {
        return serviceName;
    }

    /**
     * Returns the full API address. For example: <code>http://192.168.1.123:80/api/v1</code>.
     * It is constructed like this:
     * <p>
     * {@link #getFullAddress()} + {@link #getApiPath()}
     *
     * @return full API path
     * @see #getFullAddress()
     * @see #getApiPath()
     */
    public String getFullApiAddress() {
        return getFullAddress() + getApiPath();
    }

    /**
     * Returns the full address. For example: <code>http://192.168.1.123:80</code>.
     * It is constructed like this:
     * <p>
     * <code>http://</code> + {@link #getHostAddress()} + <code>:</code> + {@link #getPort()}
     *
     * @return full address
     * @see #getHostAddress()
     * @see #getPort()
     */
    public String getFullAddress() {
        return "http://" + getHostAddress() + ":" + getPort();
    }

    /**
     * Returns weather the API is enabled. If <code>false</code>,
     * {@link #updateDeviceInfo()} and {@link #updateMeasurements()} cannot be used.
     * To enable the local API, go to
     * <a href="https://api-documentation.homewizard.com/docs/getting-started/#2-enable-the-api">the official documentation on how to enable the local API</a>.
     * <p>
     * This information is always available.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return whether the API is enabled
     */
    public boolean isApiEnabled() {
        return apiEnabled;
    }

    /**
     * Returns the host address. This is the IP address of the device.
     * For example: <code>192.168.1.123</code>.
     * <p>
     * This information is always available.
     *
     * @return returns the host address
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Returns the port. Because the <code>v1</code> API uses HTTP, the port should always be <code>80</code>.
     * <p>
     * This information is always available.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the path to the API. With <code>v1</code>, it should be <code>/api/v1</code>.
     * <p>
     * This information is always available.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return the path to the API
     */
    public String getApiPath() {
        return apiPath;
    }

    /**
     * Returns the system configuration. You can change the values with the returned class.
     * <p>
     * This information is always available.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/system">Official API documentation related to this method</a>
     *
     * @return the system configuration
     */
    public SystemConfiguration getSystemConfiguration() {
        return systemConfiguration;
    }

    /**
     * Returns the serial number, also used as the MAC address.
     * This unique value per device consists of 12 hexadecimal characters,
     * for example <code>1a2b3c4d5e6f</code>.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #updateDeviceInfo()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return a unique serial number for this device
     */
    public Optional<String> getSerial() {
        return serial;
    }

    /**
     * A unique identifier for a device that won't change in the official API, for example <code>HWE-WTR</code>.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #updateDeviceInfo()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return the unique product type
     */
    public Optional<String> getProductType() {
        return productType;
    }

    /**
     * Returns a user-friendly name representation of the device. For example: <code>WaterMeter</code>.
     * It may change at any time, so you should not use this for device identification.
     * Instead, use {@link #getProductType()} if you want to know exactly what type of device something is.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #updateDeviceInfo()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return product name
     */
    public Optional<String> getProductName() {
        return productName;
    }

    /**
     * Returns the version of the currently installed firmware.
     * <p>
     * In order to get this information, you must first call {@link #updateDeviceInfo()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @return the version of the currently installed firmware
     * @see #updateDeviceInfo()
     */
    public Optional<String> getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * Returns the current API version. It should always be <code>v1</code>.
     * <p>
     * In order to get this information, you must first call {@link #updateDeviceInfo()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @return the current API version
     * @see #updateDeviceInfo()
     */
    public Optional<String> getApiVersion() {
        return apiVersion;
    }

    /**
     * Returns the SSID of the Wi-Fi network the device is connected to.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @return the SSID of the Wi-Fi network the device is connected to
     * @see #updateMeasurements()
     */
    public Optional<String> getWifiSsid() {
        return wifiSsid;
    }

    /**
     * Returns the strength of the Wi-Fi the device is currently connected to.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @return the Wi-Fi strength
     * @see #updateMeasurements()
     */
    public OptionalDouble getWifiStrength() {
        return wifiStrength;
    }
}
