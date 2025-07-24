package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@JsonInclude(JsonInclude.Include.NON_EMPTY) // don't include Optional.empty() values in JSON TODO check if this works
public class DeviceSystem extends Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonProperty("wifi_ssid")
    private final Optional<String> wifiSsid = Optional.empty();
    @JsonProperty("wifi_rssi_db")
    private final Optional<String> wifiRssiDb = Optional.empty();
    @JsonProperty("uptime_s")
    private final OptionalDouble uptimeS = OptionalDouble.empty();
    @JsonProperty("cloud_enabled")
    private Optional<Boolean> cloudEnabled = Optional.empty();
    @JsonProperty("status_led_brightness_pct")
    private OptionalDouble statusLedBrightnessPct = OptionalDouble.empty();
    @JsonProperty("api_v1_enabled")
    private Optional<Boolean> apiV1Enabled = Optional.empty();

    private final Device device;

    DeviceSystem(final Device device) {
        this.device = device;
    }

    /**
     * Updates all the data from the device. It requires the token to be present in the
     * associated {@link DeviceAuthorizer}. If you have changed any values, they will be discarded.
     *
     * @return whether updating the data was successful
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public boolean update() throws HomeWizardApiException {
        LOGGER.debug("Updating Device...");
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            update(token.get(), device.createFullApiAddress("/api/system"));
            return true;
        } else {
            LOGGER.trace("No token present while updating DeviceSystem, returning false");
            return false;
        }
    }

    /**
     * Saves all the changed data to the device. It requires the token to be present in the
     * associated {@link DeviceAuthorizer}. If you have changed an unsupported value for your device,
     * this method will throw an exception.
     *
     * @return whether saving the data was successful
     * @throws HomeWizardApiException when something has gone wrong while saving
     */
    public boolean save() throws HomeWizardApiException {
        LOGGER.debug("Saving DeviceSystem...");
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            try {
                final String systemJson = objectMapper.writeValueAsString(this);
                LOGGER.trace("Saving json: {}", systemJson);
                HttpUtils.sendRequest("PUT",
                        token.get(),
                        device.createFullApiAddress("/api/system"),
                        HttpRequest.BodyPublishers.ofString(systemJson));
                return true;
            } catch (final JsonProcessingException jsonProcessingException) {
                throw new HomeWizardApiException(jsonProcessingException, LOGGER);
            }
        } else {
            LOGGER.trace("No token present while saving DeviceSystem, returning false");
            return false;
        }
    }

    /**
     * Reboots the device. This is not available for the plug-in battery. It requires the token to be present in the
     * associated {@link DeviceAuthorizer}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#actions">Official API related to this method</a>
     *
     * @return whether the action was successful
     * @throws HomeWizardApiException when something has gone wrong while sending the request
     */
    public boolean reboot() throws HomeWizardApiException {
        LOGGER.debug("Rebooting device...");
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            HttpUtils.sendRequest("PUT", token.get(), device.createFullApiAddress("/api/system/reboot"));
            return true;
        } else {
            LOGGER.trace("No token present while rebooting device, returning false");
            return false;
        }
    }

    /**
     * Lets the device identify itself by blinking the status LED. This is not available for the kWh meter.
     * It requires the token to be present in the associated {@link DeviceAuthorizer}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#actions">Official API related to this method</a>
     *
     * @return whether the action was successful
     * @throws HomeWizardApiException when something has gone wrong while sending the request
     */
    public boolean identify() throws HomeWizardApiException {
        LOGGER.debug("Identifying device...");
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            HttpUtils.sendRequest("PUT", token.get(), device.createFullApiAddress("/api/system/identify"));
            return true;
        } else {
            LOGGER.trace("No token present while identifying device, returning false");
            return false;
        }
    }

    /**
     * Returns the SSID of the Wi-Fi network.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return the SSID of the Wi-Fi network
     */
    public Optional<String> getWifiSsid() {
        return wifiSsid;
    }

    /**
     * Returns the signal strength of the Wi-Fi network.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return the signal strength of the Wi-Fi network
     */
    public Optional<String> getWifiRssi() {
        return wifiRssiDb;
    }

    /**
     * Returns the uptime of the device in seconds.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return the uptime of the device in seconds
     */
    public OptionalDouble getUptime() {
        return uptimeS;
    }

    /**
     * Returns the state of the cloud communication.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return the state of the cloud communication
     */
    public Optional<Boolean> getCloudEnabled() {
        return cloudEnabled;
    }

    /**
     * Sets the state of the cloud communication. You can not change this data on the plug-in battery.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @param cloudEnabled the state of the cloud communication
     */
    public void setCloudEnabled(final boolean cloudEnabled) {
        this.cloudEnabled = Optional.of(cloudEnabled);
    }

    /**
     * Returns the brightness of the status LED in percent. This information is not available for the kWh meter.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return the brightness of the status LED in percent
     */
    public OptionalDouble getStatusLedBrightness() {
        return statusLedBrightnessPct;
    }

    /**
     * Sets the brightness of the status LED in percent. This is not available for the kWh meter.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @param statusLedBrightness the brightness of the status LED in percent
     */
    public void setStatusLedBrightness(final double statusLedBrightness) {
        statusLedBrightnessPct = OptionalDouble.of(statusLedBrightness);
    }

    /**
     * Returns whether the v1 API is enabled. This information is not available for the plug-in battery.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @return whether the v1 API is enabled
     */
    public Optional<Boolean> getApiV1Enabled() {
        return apiV1Enabled;
    }

    /**
     * Sets whether the v1 API is enabled. This is not available for the plug-in battery.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/system#parameters">Official API documentation related to this method</a>
     *
     * @param apiV1Enabled whether the v1 API is enabled
     */
    public void setApiV1Enabled(final boolean apiV1Enabled) {
        this.apiV1Enabled = Optional.of(apiV1Enabled);
    }

    /**
     * Returns the device associated with this system.
     *
     * @return the device associated with this system
     */
    public Device getDevice() {
        return device;
    }
}
