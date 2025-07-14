package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A HomeWizard device, such as a Watermeter or P1-meter.
 * <p>
 * For some methods, you first need to call an update method ({@link #updateDeviceInfo()} and {@link #updateMeasurements()}).
 * If you don't do that, they return an empty {@link Optional}.
 *
 * @author Thijzert123
 * @see Optional
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Device {
    /**
     * The default port of the API.
     */
    public static final int DEFAULT_PORT = 80;
    /**
     * The default API path.
     */
    public static final String DEFAULT_API_PATH = "/api/v1";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ObjectMapper objectMapper;

    private final Optional<String> serviceName;
    private final boolean apiEnabled;
    private final String hostAddress;
    private final int port;
    private final String apiPath;
    private final SystemConfiguration systemConfiguration;

    Device(final Optional<String> serviceName,
           final boolean apiEnabled,
           final String hostAddress,
           final int port,
           final String apiPath) {
        objectMapper = new ObjectMapper();
        // Makes sure Optional is supported
        objectMapper.registerModule(new Jdk8Module());

        this.serviceName = serviceName;
        this.apiEnabled = apiEnabled;
        this.hostAddress = hostAddress;
        this.port = port;
        this.apiPath = apiPath;
        systemConfiguration = new SystemConfiguration(this);
    }

    boolean update(final String fullAddress, final Object objectToUpdate) throws HomeWizardApiException {
        LOGGER.debug("Updating device fields");

        if (!isApiEnabled()) {
            LOGGER.debug("API is not enabled, not updating");
            return false;
        }

        final String responseBody = HttpUtils.getBody("GET", fullAddress);
        try {
            LOGGER.debug("Mapping body with ObjectMapper, updating this instance... ");
            objectMapper.readerForUpdating(objectToUpdate).readValue(responseBody);
            LOGGER.debug("Mapping body with ObjectMapper, updating this instance, done");
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
        return true;
    }

    boolean updateDeviceInfo(final Device objectToUpdate) throws HomeWizardApiException {
        return update(getFullAddress() + "/api", objectToUpdate);
    }

    boolean updateMeasurements(final Device objectToUpdate) throws HomeWizardApiException {
        return update(getFullApiPath() + "/data", objectToUpdate);
    }

    /**
     * Calls {@link #updateDeviceInfo()} and {@link #updateMeasurements()}.
     * It retrieves the system configuration vis {@link #getSystemConfiguration()}
     * and calls {@link SystemConfiguration#update()}.
     *
     * @return whether all actions where successful
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public boolean updateAll() throws HomeWizardApiException {
        return updateDeviceInfo() && updateMeasurements() && getSystemConfiguration().update();
    }

    /**
     * Updates the fields related to the device info. Requires {@link #isApiEnabled()} to be <code>true</code>.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/api#parameters">Official API documentation related to this method</a>
     *
     * @return whether the action was successful
     * @see #isApiEnabled()
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public abstract boolean updateDeviceInfo() throws HomeWizardApiException;

    /**
     * Updates the fields related to measurements. Requires {@link #isApiEnabled()} to be <code>true</code>.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement">Official API documentation related to this method</a>
     *
     * @return whether the action was successful
     * @see #isApiEnabled()
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public abstract boolean updateMeasurements() throws HomeWizardApiException;

    /**
     * The status light of the device will blink for a few seconds after calling this method,
     * allowing someone to identify the physical device.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/identify">Official API documentation related to this method</a>
     *
     * @throws HomeWizardApiException when something has gone wrong when requesting blinking the status light
     */
    public void identify() throws HomeWizardApiException {
        LOGGER.debug("Identify device");
        HttpUtils.getBody("PUT", getFullApiPath() + "/identify");
    }

    /**
     * Returns the full qualified service name, for example <code>watermeter-ABC123._hwenergy._tcp.local.</code>.
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
     * Returns the full API path (example: <code>http://192.168.1.123:80/api/v1</code>) constructed in this way:
     * <p>
     * {@link #getFullAddress()} + {@link #getApiPath()}
     *
     * @return full API path
     * @see #getFullAddress()
     * @see #getApiPath()
     */
    public String getFullApiPath() {
        return getFullAddress() + getApiPath();
    }

    /**
     * Returns the full address (example: <code>http://192.168.1.123:80</code>) constructed in this way:
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
     * Returns the host address, for example <code>192.168.1.123</code>.
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
    public abstract Optional<String> getSerial();

    /**
     * A unique identifier for a device that won't change in the official API, for example <code>HWE-WTR</code>.
     * <p>
     * This information is always available.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return the unique product type
     */
    public abstract String getProductType();

    /**
     * Returns a user-friendly name (example: <code>Watermeter</code>) that may change at any time, so you should not use this for device identification.
     * Instead, use {@link #getProductType()}.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #updateDeviceInfo()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records">Official API documentation related to this method</a>
     *
     * @return product name
     */
    public abstract Optional<String> getProductName();

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
    public abstract Optional<String> getFirmwareVersion();

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
    public abstract Optional<String> getApiVersion();

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
    public abstract Optional<String> getWifiSsid();

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
    public abstract OptionalDouble getWifiStrength();

    /**
     * Provides a human-readable {@link String} useful for debugging by calling all get* methods.
     *
     * @return a human-readable representation of this class
     */
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Method method : getClass().getMethods()) {
            final String name = method.getName();
            if (name.startsWith("get") && method.getParameterCount() == 0 && !name.equals("getClass")) {
                stringBuilder.append(name).append(" = ");
                String returnValue;
                try {
                    returnValue = method.invoke(this).toString();
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    returnValue = exception.getMessage();
                }
                stringBuilder.append(returnValue).append(", ");
            }
        }
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }
}
