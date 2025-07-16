package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device helps you to understand the energy consumption of the devices you plug into the energy socket.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#energy-socket-hwe-skt">Official API documentation related to this class</a>
 *
 * @author Thijzert123
 * @see Device
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class EnergySocket extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-SKT");

    private final DeviceState deviceState;

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

    @JsonProperty("total_power_import_kwh")
    private final OptionalDouble totalPowerImportKwh = OptionalDouble.empty();
    @JsonProperty("total_power_import_t1_kwh")
    private final OptionalDouble totalPowerImportT1Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_kwh")
    private final OptionalDouble totalPowerExportKwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_t1_kwh")
    private final OptionalDouble totalPowerExportT1Kwh = OptionalDouble.empty();
    @JsonProperty("active_power_w")
    private final OptionalDouble activePowerW = OptionalDouble.empty();
    @JsonProperty("active_power_l1_w")
    private final OptionalDouble activePowerL1W = OptionalDouble.empty();
    @JsonProperty("active_voltage_v")
    private final OptionalDouble activeVoltageV = OptionalDouble.empty();
    @JsonProperty("active_current_a")
    private final OptionalDouble activeCurrentA = OptionalDouble.empty();
    @JsonProperty("active_reactive_power_var")
    private final OptionalDouble activeReactivePowerVar = OptionalDouble.empty();
    @JsonProperty("active_apparent_power_va")
    private final OptionalDouble activeApparentPowerVa = OptionalDouble.empty();
    @JsonProperty("active_power_factor")
    private final OptionalDouble activePowerFactor = OptionalDouble.empty();
    @JsonProperty("active_frequency_hz")
    private final OptionalDouble activeFrequencyHz = OptionalDouble.empty();

    EnergySocket(final Optional<String> serviceName,
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

        deviceState = new DeviceState(this);

        this.productType = productType;
        this.productName = productName;
        this.serial = serial;
    }

    /**
     * Manually create a {@link EnergySocket}. When getting devices this way, some methods become useless,
     * for example {@link #getServiceName()}. For more information on what data is available,
     * check the Javadocs for the methods.
     *
     * @param apiEnabled  whether the API is enabled on the device: you have to check this yourself!
     * @param hostAddress host address, like {@code 192.168.1.123}
     * @param port        port, should be {@code 80}
     * @param apiPath     API path, should be {@code /api/v1}
     */
    public EnergySocket(final boolean apiEnabled,
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
     * Manually create a {@link EnergySocket}. This assumes that the API is enabled. It is important that you have checked
     * this before, because if the API is not actually enabled, some exceptions when updating could occur.
     * <p>
     * For port and API path, the values {@link Device#DEFAULT_PORT} and {@link Device#DEFAULT_API_PATH} are used.
     *
     * @param hostAddress the host
     */
    public EnergySocket(final String hostAddress) {
        this(
                true,
                hostAddress,
                Device.DEFAULT_PORT,
                Device.DEFAULT_API_PATH
        );
    }

    @Override
    public void updateAll() throws HomeWizardApiException {
        super.updateAll();
        getDeviceState().update();
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
     * Returns the energy usage meter reading in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportKwh() {
        return totalPowerImportKwh;
    }

    /**
     * Returns the energy feed-in meter reading in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportKwh() {
        return totalPowerExportKwh;
    }

    /**
     * Returns the total active usage in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return total active usage in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerW() {
        return activePowerW;
    }

    /**
     * Returns the active voltage in volts.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return active voltage in volts
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageV() {
        return activeVoltageV;
    }

    /**
     * Returns the active current in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return active current in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentA() {
        return activeCurrentA;
    }

    /**
     * Returns the reactive power in volt-amperes reactive.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * This information is only available for {@code HWE-SKT-21}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return reactive power in volt-amperes reactive
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactivePowerVar() {
        return activeReactivePowerVar;
    }

    /**
     * Returns the apparent power in volt-amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * This information is only available for {@code HWE-SKT-21}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return apparent power in volt-amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentPowerVa() {
        return activeApparentPowerVa;
    }

    /**
     * Returns the power factor.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * This information is only available for {@code HWE-SKT-21}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return power factor
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerFactor() {
        return activePowerFactor;
    }

    /**
     * Returns the frequency in hertz.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-1">Official API documentation related to this method</a>
     *
     * @return frequency in hertz
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveFrequencyHz() {
        return activeFrequencyHz;
    }

    /**
     * Returns the {@link DeviceState}. With this you are able to change settings specific to the energy socket.
     *
     * @return the {@link DeviceState}
     * @see DeviceState
     */
    public DeviceState getDeviceState() {
        return deviceState;
    }
}
