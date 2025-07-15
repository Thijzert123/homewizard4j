package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device that give insight in your electricity usage.
 * It has four types, available in two versions:
 * <ul>
 *     <li>1-phase: {@code HWE-KWH1} and {@code SDM230-wifi}
 *     <li>3-phase: {@code HWE-KWH3} and {@code SDM630-wifi}
 * </ul>
 * All of these types can be used with this class. Some methods are reserved for specific versions (1-phase or 2-phase).
 * To see what version you need for a method, check their Javadocs.
 *
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class KWhMeter extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-KWH1", "HWE-KWH3", "SDM230-wifi", "SDM630-wifi");

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
    @JsonProperty("total_power_export_kwh")
    private final OptionalDouble totalPowerExportKwh = OptionalDouble.empty();
    @JsonProperty("active_power_w")
    private final OptionalDouble activePowerW = OptionalDouble.empty();
    @JsonProperty("active_power_l1_w")
    private final OptionalDouble activePowerL1W = OptionalDouble.empty();
    @JsonProperty("active_power_l2_w")
    private final OptionalDouble activePowerL2W = OptionalDouble.empty();
    @JsonProperty("active_power_l3_w")
    private final OptionalDouble activePowerL3W = OptionalDouble.empty();
    @JsonProperty("active_voltage_v")
    private final OptionalDouble activeVoltageV = OptionalDouble.empty();
    @JsonProperty("active_voltage_l1_v")
    private final OptionalDouble activeVoltageL1V = OptionalDouble.empty();
    @JsonProperty("active_voltage_l2_v")
    private final OptionalDouble activeVoltageL2V = OptionalDouble.empty();
    @JsonProperty("active_voltage_l3_v")
    private final OptionalDouble activeVoltageL3V = OptionalDouble.empty();
    @JsonProperty("active_current_a")
    private final OptionalDouble activeCurrentA = OptionalDouble.empty();
    @JsonProperty("active_current_l1_a")
    private final OptionalDouble activeCurrentL1A = OptionalDouble.empty();
    @JsonProperty("active_current_l2_a")
    private final OptionalDouble activeCurrentL2A = OptionalDouble.empty();
    @JsonProperty("active_current_l3_a")
    private final OptionalDouble activeCurrentL3A = OptionalDouble.empty();
    @JsonProperty("active_apparent_current_a")
    private final OptionalDouble activeApparentCurrentA = OptionalDouble.empty();
    @JsonProperty("active_apparent_current_l1_a")
    private final OptionalDouble activeApparentCurrentL1A = OptionalDouble.empty();
    @JsonProperty("active_apparent_current_l2_a")
    private final OptionalDouble activeApparentCurrentL2A = OptionalDouble.empty();
    @JsonProperty("active_apparent_current_l3_a")
    private final OptionalDouble activeApparentCurrentL3A = OptionalDouble.empty();
    @JsonProperty("active_reactive_current_a")
    private final OptionalDouble activeReactiveCurrentA = OptionalDouble.empty();
    @JsonProperty("active_reactive_current_l1_a")
    private final OptionalDouble activeReactiveCurrentL1A = OptionalDouble.empty();
    @JsonProperty("active_reactive_current_l2_a")
    private final OptionalDouble activeReactiveCurrentL2A = OptionalDouble.empty();
    @JsonProperty("active_reactive_current_l3_a")
    private final OptionalDouble activeReactiveCurrentL3A = OptionalDouble.empty();
    @JsonProperty("active_apparent_power_va")
    private final OptionalDouble activeApparentPowerVa = OptionalDouble.empty();
    @JsonProperty("active_apparent_power_l1_va")
    private final OptionalDouble activeApparentPowerL1Va = OptionalDouble.empty();
    @JsonProperty("active_apparent_power_l2_va")
    private final OptionalDouble activeApparentPowerL2Va = OptionalDouble.empty();
    @JsonProperty("active_apparent_power_l3_va")
    private final OptionalDouble activeApparentPowerL3Va = OptionalDouble.empty();
    @JsonProperty("active_reactive_power_var")
    private final OptionalDouble activeReactivePowerVar = OptionalDouble.empty();
    @JsonProperty("active_reactive_power_l1_var")
    private final OptionalDouble activeReactivePowerL1Var = OptionalDouble.empty();
    @JsonProperty("active_reactive_power_l2_var")
    private final OptionalDouble activeReactivePowerL2Var = OptionalDouble.empty();
    @JsonProperty("active_reactive_power_l3_var")
    private final OptionalDouble activeReactivePowerL3Var = OptionalDouble.empty();
    @JsonProperty("active_power_factor")
    private final OptionalDouble activePowerFactor = OptionalDouble.empty();
    @JsonProperty("active_power_factor_l1")
    private final OptionalDouble activePowerFactorL1 = OptionalDouble.empty();
    @JsonProperty("active_power_factor_l2")
    private final OptionalDouble activePowerFactorL2 = OptionalDouble.empty();
    @JsonProperty("active_power_factor_l3")
    private final OptionalDouble activePowerFactorL3 = OptionalDouble.empty();
    @JsonProperty("active_frequency_hz")
    private final OptionalDouble activeFrequencyHz = OptionalDouble.empty();

    KWhMeter(final Optional<String> serviceName,
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
     * Manually create a {@link KWhMeter}. When getting devices this way, some methods become useless,
     * for example {@link #getServiceName()}. For more information on what data is available,
     * check the Javadocs for the methods.
     *
     * @param apiEnabled whether the API is enabled on the device: you have to check this yourself!
     * @param hostAddress host address, like {@code 192.168.1.123}
     * @param port port, should be {@code 80}
     * @param apiPath API path, should be {@code /api/v1}
     */
    public KWhMeter(final boolean apiEnabled,
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
     * Manually create a {@link KWhMeter}. This assumes that the API is enabled. It is important that you have checked
     * this before, because if the API is not actually enabled, some exceptions when updating could occur.
     * <p>
     * For port and API path, the values {@link Device#DEFAULT_PORT} and {@link Device#DEFAULT_API_PATH} are used.
     *
     * @param hostAddress the host
     */
    public KWhMeter(final String hostAddress) {
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
     * Returns the energy usage meter reading in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
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
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
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
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return total active usage in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerW() {
        return activePowerW;
    }

    /**
     * Returns the active usage for phase 1 in watt, same as {@code active_power_w} for the 1-phase variant.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active usage for phase 1 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerL1W() {
        return activePowerL1W;
    }

    /**
     * Returns the active usage for phase 2 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active usage for phase 2 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerL2W() {
        return activePowerL2W;
    }

    /**
     * Returns the active usage for phase 3 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active usage for phase 3 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerL3W() {
        return activePowerL3W;
    }

    /**
     * Returns the active voltage in volts.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 1-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active voltage in volts
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageV() {
        return activeVoltageV;
    }

    /**
     * Returns the active voltage for phase 1 in volts.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 1 in volts
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL1V() {
        return activeVoltageL1V;
    }

    /**
     * Returns the active voltage for phase 2 in volts.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 2 in volts
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL2V() {
        return activeVoltageL2V;
    }

    /**
     * Returns the active voltage for phase 3 in volts.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 3 in volts
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL3V() {
        return activeVoltageL3V;
    }

    /**
     * Returns the active current in amperes, the sum of all phases for the 3-phase variant.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active current in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentA() {
        return activeCurrentA;
    }

    /**
     * Returns the active current for phase 1 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active current for phase 1 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL1A() {
        return activeCurrentL1A;
    }

    /**
     * Returns the active current for phase 2 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active current for phase 2 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL2A() {
        return activeCurrentL2A;
    }

    /**
     * Returns the active current for phase 3 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return active current for phase 3 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL3A() {
        return activeCurrentL3A;
    }

    /**
     * Returns the apparent current in amperes, the sum of all phases for the 3-phase variant.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent current in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentCurrentA() {
        return activeApparentCurrentA;
    }

    /**
     * Returns the apparent current for phase 1 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent current for phase 1 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentCurrentL1A() {
        return activeApparentCurrentL1A;
    }

    /**
     * Returns the apparent current for phase 2 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent current for phase 2 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentCurrentL2A() {
        return activeApparentCurrentL2A;
    }

    /**
     * Returns the apparent current for phase 3 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent current for phase 3 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentCurrentL3A() {
        return activeApparentCurrentL3A;
    }

    /**
     * Returns the reactive current in amperes, the sum of all phases for the 3-phase variant.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive current in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactiveCurrentA() {
        return activeReactiveCurrentA;
    }

    /**
     * Returns the reactive current for phase 1 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive current for phase 1 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactiveCurrentL1A() {
        return activeReactiveCurrentL1A;
    }

    /**
     * Returns the reactive current for phase 2 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive current for phase 2 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactiveCurrentL2A() {
        return activeReactiveCurrentL2A;
    }

    /**
     * Returns the reactive current for phase 3 in amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive current for phase 3 in amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactiveCurrentL3A() {
        return activeReactiveCurrentL3A;
    }

    /**
     * Returns the apparent power in volt-amperes, the sum of all phases for the 3-phase variants.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent power in volt-amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentPowerVa() {
        return activeApparentPowerVa;
    }

    /**
     * Returns the apparent power for phase 1 in volt-amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent power for phase 1 in volt-amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentPowerL1Va() {
        return activeApparentPowerL1Va;
    }

    /**
     * Returns the apparent power for phase 2 in volt-amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent power for phase 2 in volt-amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentPowerL2Va() {
        return activeApparentPowerL2Va;
    }

    /**
     * Returns the apparent power for phase 3 in volt-amperes.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return apparent power for phase 3 in volt-amperes
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveApparentPowerL3Va() {
        return activeApparentPowerL3Va;
    }

    /**
     * Returns the reactive power in volt-amperes reactive, the sum of all phases for the 3-phase variant.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive power in volt-amperes reactive
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactivePowerVar() {
        return activeReactivePowerVar;
    }

    /**
     * Returns the reactive power for phase 1 in volt-amperes reactive.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive power for phase 1 in volt-amperes reactive
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactivePowerL1Var() {
        return activeReactivePowerL1Var;
    }

    /**
     * Returns the reactive power for phase 2 in volt-amperes reactive.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive power for phase 2 in volt-amperes reactive
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactivePowerL2Var() {
        return activeReactivePowerL2Var;
    }

    /**
     * Returns the reactive power for phase 3 in volt-amperes reactive.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return reactive power for phase 3 in volt-amperes reactive
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveReactivePowerL3Var() {
        return activeReactivePowerL3Var;
    }

    /**
     * Returns the power factor.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 1-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return power factor
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerFactor() {
        return activePowerFactor;
    }

    /**
     * Returns the power factor for phase 1.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return power factor for phase 1
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerFactorL1() {
        return activePowerFactorL1;
    }

    /**
     * Returns the power factor for phase 2.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return power factor for phase 2
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerFactorL2() {
        return activePowerFactorL2;
    }

    /**
     * Returns the power factor for phase 3.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It is only available for the 3-phase variant.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return power factor for phase 3
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerFactorL3() {
        return activePowerFactorL3;
    }

    /**
     * Returns the frequency in hertz.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * It available for both the 1-phase and 3-phase variants.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters-2">Official API documentation related to this method</a>
     *
     * @return frequency in hertz
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveFrequencyHz() {
        return activeFrequencyHz;
    }
}
