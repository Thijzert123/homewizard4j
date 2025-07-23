package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.OptionalDouble;

/**
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DeviceSystem extends Updatable {
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

    public boolean update() throws HomeWizardApiException {
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            update(token.get(), device.createFullApiAddress("/api"));
            return true;
        } else {
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
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            HttpUtils.sendRequest("PUT", token.get(), device.createFullApiAddress("/api/system/reboot"));
            return true;
        } else {
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
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            HttpUtils.sendRequest("PUT", token.get(), device.createFullApiAddress("/api/system/identify"));
            return true;
        } else {
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
