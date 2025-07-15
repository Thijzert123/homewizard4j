package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device to measure mainly electricity and gas use.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#p1-meter-hwe-p1">Official API documentation related to this class</a>
 *
 * @author Thijzert123
 * @see Device
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class P1Meter extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-P1");

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

    @JsonProperty("unique_id")
    private final Optional<String> uniqueId = Optional.empty();
    @JsonProperty("smr_version")
    private final OptionalDouble smrVersion = OptionalDouble.empty();
    @JsonProperty("meter_model")
    private final Optional<String> meterModel = Optional.empty();
    @JsonProperty("total_power_import_kwh")
    private final OptionalDouble totalPowerImportKwh = OptionalDouble.empty();
    @JsonProperty("total_power_import_t1_kwh")
    private final OptionalDouble totalPowerImportT1Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_import_t2_kwh")
    private final OptionalDouble totalPowerImportT2Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_import_t3_kwh")
    private final OptionalDouble totalPowerImportT3Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_import_t4_kwh")
    private final OptionalDouble totalPowerImportT4Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_kwh")
    private final OptionalDouble totalPowerExportKwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_t1_kwh")
    private final OptionalDouble totalPowerExportT1Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_t2_kwh")
    private final OptionalDouble totalPowerExportT2Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_t3_kwh")
    private final OptionalDouble totalPowerExportT3Kwh = OptionalDouble.empty();
    @JsonProperty("total_power_export_t4_kwh")
    private final OptionalDouble totalPowerExportT4Kwh = OptionalDouble.empty();
    @JsonProperty("active_power_w")
    private final OptionalDouble activePowerW = OptionalDouble.empty();
    @JsonProperty("active_power_l1_w")
    private final OptionalDouble activePowerL1W = OptionalDouble.empty();
    @JsonProperty("active_power_l2_w")
    private final OptionalDouble activePowerL2W = OptionalDouble.empty();
    @JsonProperty("active_power_l3_w")
    private final OptionalDouble activePowerL3W = OptionalDouble.empty();
    @JsonProperty("active_voltage_l1_v")
    private final OptionalDouble activeVoltageL1V = OptionalDouble.empty();
    @JsonProperty("active_voltage_l2_v")
    private final OptionalDouble activeVoltageL2V = OptionalDouble.empty();
    @JsonProperty("active_voltage_l3_v")
    private final OptionalDouble activeVoltageL3V = OptionalDouble.empty();
    @JsonProperty("active_current_l1_a")
    private final OptionalDouble activeCurrentL1A = OptionalDouble.empty();
    @JsonProperty("active_current_l2_a")
    private final OptionalDouble activeCurrentL2A = OptionalDouble.empty();
    @JsonProperty("active_current_l3_a")
    private final OptionalDouble activeCurrentL3A = OptionalDouble.empty();
    @JsonProperty("active_frequency_hz")
    private final OptionalDouble activeFrequencyHz = OptionalDouble.empty();
    @JsonProperty("voltage_sag_l1_count")
    private final OptionalDouble voltageSagL1Count = OptionalDouble.empty();
    @JsonProperty("voltage_sag_l2_count")
    private final OptionalDouble voltageSagL2Count = OptionalDouble.empty();
    @JsonProperty("voltage_sag_l3_count")
    private final OptionalDouble voltageSagL3Count = OptionalDouble.empty();
    @JsonProperty("voltage_swell_l1_count")
    private final OptionalDouble voltageSwellL1Count = OptionalDouble.empty();
    @JsonProperty("voltage_swell_l2_count")
    private final OptionalDouble voltageSwellL2Count = OptionalDouble.empty();
    @JsonProperty("voltage_swell_l3_count")
    private final OptionalDouble voltageSwellL3Count = OptionalDouble.empty();
    @JsonProperty("any_power_fail_count")
    private final OptionalDouble anyPowerFailCount = OptionalDouble.empty();
    @JsonProperty("long_power_fail_count")
    private final OptionalDouble longPowerFailCount = OptionalDouble.empty();
    @JsonProperty("active_power_average_w")
    private final OptionalDouble activePowerAverageW = OptionalDouble.empty();
    @JsonProperty("montly_power_peak_w")
    private final OptionalDouble monthlyPowerPeakW = OptionalDouble.empty();
    @JsonProperty("montly_power_peak_timestamp")
    private final OptionalDouble monthlyPowerPeakTimestamp = OptionalDouble.empty();
    @JsonProperty("total_gas_m3")
    private final OptionalDouble totalGasM3 = OptionalDouble.empty();
    @JsonProperty("gas_timestamp")
    private final OptionalDouble gasTimestamp = OptionalDouble.empty();
    @JsonProperty("unique_gas_id")
    private final OptionalDouble uniqueGasId = OptionalDouble.empty();
    @JsonProperty("external")
    private final Optional<List<ExternalP1Device>> externalP1Devices = Optional.empty();

    P1Meter(final Optional<String> serviceName,
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
     * Manually create a {@link P1Meter}. When getting devices this way, some methods become useless,
     * for example {@link #getServiceName()}. For more information on what data is available,
     * check the Javadocs for the methods.
     *
     * @param apiEnabled whether the API is enabled on the device: you have to check this yourself!
     * @param hostAddress host address, like {@code 192.168.1.123}
     * @param port port, should be {@code 80}
     * @param apiPath API path, should be {@code /api/v1}
     */
    public P1Meter(final boolean apiEnabled,
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
     * Manually create a {@link P1Meter}. This assumes that the API is enabled. It is important that you have checked
     * this before, because if the API is not actually enabled, some exceptions when updating could occur.
     * <p>
     * For port and API path, the values {@link Device#DEFAULT_PORT} and {@link Device#DEFAULT_API_PATH} are used.
     *
     * @param hostAddress the host
     */
    public P1Meter(final String hostAddress) {
        this(
                true,
                hostAddress,
                Device.DEFAULT_PORT,
                Device.DEFAULT_API_PATH
        );
    }

    /**
     * Returns the most recent, valid telegram that was given by the P1 meter.
     * <p>
     * This method always does an HTTP request, so you can always use it.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/telegram">Official API documentation related to this method</a>
     *
     * @return telegram by the P1 meter
     * @throws HomeWizardApiException when something has gone wrong while retrieving the telegram
     */
    public String retrieveLastTelegram() throws HomeWizardApiException {
        return HttpUtils.getBody("GET", getFullApiAddress() + "/telegram");
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
     * Returns the unique identifier from the smart meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return unique identifier from the smart meter
     * @see #updateMeasurements()
     */
    public Optional<String> getUniqueId() {
        return uniqueId;
    }

    /**
     * Returns the DSMR version of the smart meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return DSMR version of the smart meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getSmrVersion() {
        return smrVersion;
    }

    /**
     * Returns the brand identification the smart meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return brand identification the smart meter
     * @see #updateMeasurements()
     */
    public Optional<String> getMeterModel() {
        return meterModel;
    }

    /**
     * Returns the energy usage meter reading for all tariffs in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for all tariffs in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportKwh() {
        return totalPowerImportKwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 1 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 1 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportT1Kwh() {
        return totalPowerImportT1Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 2 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 2 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportT2Kwh() {
        return totalPowerImportT2Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 3 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 3 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportT3Kwh() {
        return totalPowerImportT3Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 4 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 4 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerImportT4Kwh() {
        return totalPowerImportT4Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for all tariffs in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for all tariffs in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportKwh() {
        return totalPowerExportKwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 1 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 1 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportT1Kwh() {
        return totalPowerExportT1Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 2 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 2 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportT2Kwh() {
        return totalPowerExportT2Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 3 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 3 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportT3Kwh() {
        return totalPowerExportT3Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 4 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 4 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalPowerExportT4Kwh() {
        return totalPowerExportT4Kwh;
    }

    /**
     * Returns the total active usage in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return total active usage in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerW() {
        return activePowerW;
    }

    /**
     * Returns the active usage for phase 1 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
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
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
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
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active usage for phase 3 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerL3W() {
        return activePowerL3W;
    }

    /**
     * Returns the active voltage for phase 1 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 1 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL1V() {
        return activeVoltageL1V;
    }

    /**
     * Returns the active voltage for phase 2 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 2 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL2V() {
        return activeVoltageL2V;
    }

    /**
     * Returns the active voltage for phase 3 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 3 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveVoltageL3V() {
        return activeVoltageL3V;
    }

    /**
     * Returns the active current for phase 1 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 1 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL1A() {
        return activeCurrentL1A;
    }

    /**
     * Returns the active current for phase 2 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 2 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL2A() {
        return activeCurrentL2A;
    }

    /**
     * Returns the active current for phase 3 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 3 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveCurrentL3A() {
        return activeCurrentL3A;
    }

    /**
     * Returns the line frequency in hertz.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return line frequency in hertz
     * @see #updateMeasurements()
     */
    public OptionalDouble getActiveFrequencyHz() {
        return activeFrequencyHz;
    }

    /**
     * Returns the number of voltage sags detected by meter for phase 1.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage sags detected by meter for phase 1
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSagL1Count() {
        return voltageSagL1Count;
    }

    /**
     * Returns the number of voltage sags detected by meter for phase 2.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage sags detected by meter for phase 2
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSagL2Count() {
        return voltageSagL2Count;
    }

    /**
     * Returns the number of voltage sags detected by meter for phase 3.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage sags detected by meter for phase 3
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSagL3Count() {
        return voltageSagL3Count;
    }

    /**
     * Returns the number of voltage swells detected by meter for phase 1.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage swells detected by meter for phase 1
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSwellL1Count() {
        return voltageSwellL1Count;
    }

    /**
     * Returns the number of voltage swells detected by meter for phase 2.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage swells detected by meter for phase 2
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSwellL2Count() {
        return voltageSwellL2Count;
    }

    /**
     * Returns the number of voltage swells detected by meter for phase 3.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of voltage swells detected by meter for phase 3
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageSwellL3Count() {
        return voltageSwellL3Count;
    }

    /**
     * Returns the number of power failures detected by meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of power failures detected by meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getAnyPowerFailCount() {
        return anyPowerFailCount;
    }

    /**
     * Returns the number of 'long' power fails detected by meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of 'long' power fails detected by meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getLongPowerFailCount() {
        return longPowerFailCount;
    }

    /**
     * Returns the active average demand.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active average demand
     * @see #updateMeasurements()
     */
    public OptionalDouble getActivePowerAverageW() {
        return activePowerAverageW;
    }

    /**
     * Returns the peak average demand of this month.
     * There is a spelling mistake with this data point in the official API, but in this Java API, it is fixed.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return peak average demand of this month
     * @see #updateMeasurements()
     */
    public OptionalDouble getMonthlyPowerPeakW() {
        return monthlyPowerPeakW;
    }

    /**
     * Returns the timestamp when peak demand was registered, formatted as <code>YYMMDDhhmmss</code>.
     * There is a spelling mistake with this data point in the official API, but in this Java API, it is fixed.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return timestamp when peak demand was registered
     * @see #updateMeasurements()
     */
    public OptionalDouble getMonthlyPowerPeakTimestamp() {
        return monthlyPowerPeakTimestamp;
    }

    /**
     * Returns the gas meter reading in m3 for the first detected gas meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return gas meter reading in m3 for the first detected gas meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getTotalGasM3() {
        return totalGasM3;
    }

    /**
     * Returns the most recent gas update time stamp structured as <code>YYMMDDhhmmss</code>.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return most recent gas update time stamp
     * @see #updateMeasurements()
     */
    public OptionalDouble getGasTimestamp() {
        return gasTimestamp;
    }

    /**
     * Returns the unique identifier for the gas meter, can be used to migrate to the 'external' data point.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return unique identifier for the gas meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getUniqueGasId() {
        return uniqueGasId;
    }

    /**
     * Returns a list of externally connected utility meters.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return a list of externally connected utility meters
     * @see #updateMeasurements()
     */
    public Optional<List<ExternalP1Device>> getExternalP1Devices() {
        return externalP1Devices;
    }
}
