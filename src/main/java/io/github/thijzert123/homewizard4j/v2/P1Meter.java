package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device to measure electricity and gas use.
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
    public static final List<String> PRODUCT_TYPES = List.of("HWE-P1"); // TODO check here: https://api-documentation.homewizard.com/docs/v2/authorization#hostname-validation
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonProperty("unique_id")
    private final Optional<String> uniqueId = Optional.empty();
    @JsonProperty("protocol_version")
    private final OptionalDouble protocolVersion = OptionalDouble.empty();
    @JsonProperty("meter_model")
    private final Optional<String> meterModel = Optional.empty();
    @JsonProperty("energy_import_kwh")
    private final OptionalDouble energyImportKwh = OptionalDouble.empty();
    @JsonProperty("energy_import_t1_kwh")
    private final OptionalDouble energyImportT1Kwh = OptionalDouble.empty();
    @JsonProperty("energy_import_t2_kwh")
    private final OptionalDouble energyImportT2Kwh = OptionalDouble.empty();
    @JsonProperty("energy_import_t3_kwh")
    private final OptionalDouble energyImportT3Kwh = OptionalDouble.empty();
    @JsonProperty("energy_import_t4_kwh")
    private final OptionalDouble energyImportT4Kwh = OptionalDouble.empty();
    @JsonProperty("energy_export_kwh")
    private final OptionalDouble energyExportKwh = OptionalDouble.empty();
    @JsonProperty("energy_export_t1_kwh")
    private final OptionalDouble energyExportT1Kwh = OptionalDouble.empty();
    @JsonProperty("energy_export_t2_kwh")
    private final OptionalDouble energyExportT2Kwh = OptionalDouble.empty();
    @JsonProperty("energy_export_t3_kwh")
    private final OptionalDouble energyExportT3Kwh = OptionalDouble.empty();
    @JsonProperty("energy_export_t4_kwh")
    private final OptionalDouble energyExportT4Kwh = OptionalDouble.empty();
    @JsonProperty("power_w")
    private final OptionalDouble powerW = OptionalDouble.empty();
    @JsonProperty("power_l1_w")
    private final OptionalDouble powerL1W = OptionalDouble.empty();
    @JsonProperty("power_l2_w")
    private final OptionalDouble powerL2W = OptionalDouble.empty();
    @JsonProperty("power_l3_w")
    private final OptionalDouble powerL3W = OptionalDouble.empty();
    @JsonProperty("voltage_v")
    private final OptionalDouble voltageV = OptionalDouble.empty();
    @JsonProperty("voltage_l1_v")
    private final OptionalDouble voltageL1V = OptionalDouble.empty();
    @JsonProperty("voltage_l2_v")
    private final OptionalDouble voltageL2V = OptionalDouble.empty();
    @JsonProperty("voltage_l3_v")
    private final OptionalDouble voltageL3V = OptionalDouble.empty();
    @JsonProperty("current_a")
    private final OptionalDouble currentA = OptionalDouble.empty();
    @JsonProperty("current_l1_a")
    private final OptionalDouble currentL1A = OptionalDouble.empty();
    @JsonProperty("current_l2_a")
    private final OptionalDouble currentL2A = OptionalDouble.empty();
    @JsonProperty("current_l3_a")
    private final OptionalDouble currentL3A = OptionalDouble.empty();
    @JsonProperty("frequency_hz")
    private final OptionalDouble frequencyHz = OptionalDouble.empty();
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
    @JsonProperty("average_power_15m_w")
    private final OptionalDouble averagePower15mW = OptionalDouble.empty();
    @JsonProperty("monthly_power_peak_w")
    private final OptionalDouble monthlyPowerPeakW = OptionalDouble.empty();
    @JsonProperty("monthly_power_peak_timestamp")
    private final OptionalDouble monthlyPowerPeakTimestamp = OptionalDouble.empty();
    @JsonProperty("external")
    private final Optional<List<ExternalP1Device>> external = Optional.empty();

    P1Meter(final Optional<String> serviceName,
            final String hostAddress,
            final int port,
            final Optional<String> productType,
            final Optional<String> productName,
            final Optional<String> serial,
            final Optional<String> id,
            final Optional<String> apiVersion) {
        super(
                serviceName,
                hostAddress,
                port,
                productType,
                productName,
                serial,
                id,
                apiVersion
        );
    }

    public P1Meter(final String hostAddress) {
        super(
                Optional.empty(),
                hostAddress,
                Device.DEFAULT_PORT,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
    }

    /**
     * Returns the most recent, valid telegram that was given by the P1 meter. It requires a token to be
     * present in the {@link DeviceAuthorizer}.
     * <p>
     * This method always does an HTTPS request, so you can always use it.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/telegram">Official API documentation related to this method</a>
     *
     * @return telegram by the P1 meter
     * @throws NoTokenPresentException when no token is present in an associated {@link DeviceAuthorizer}
     * @throws HomeWizardApiException when something has gone wrong while retrieving the telegram
     */
    public String retrieveLastTelegram() throws HomeWizardApiException {
        LOGGER.debug("Retrieving last telegram...");
        final Optional<String> token = getAuthorizer().getToken();
        if (token.isPresent()) {
            final HttpResponse<String> httpResponse = HttpUtils.sendRequest("PUT",
                    token.get(),
                    createFullApiAddress("/api/telegram"));
            return httpResponse.body();
        } else {
            LOGGER.trace("No token present while retrieving last telegram, throwing NoTokenPresentException");
            throw new NoTokenPresentException(LOGGER);
        }
    }

    /**
     * Returns the unique identifier from the smart meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return DSMR version of the smart meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Returns the brand identification the smart meter.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for all tariffs in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImport() {
        return energyImportKwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 1 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 1 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImportT1() {
        return energyImportT1Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 2 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 2 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImportT2() {
        return energyImportT2Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 3 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 3 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImportT3() {
        return energyImportT3Kwh;
    }

    /**
     * Returns the energy usage meter reading for tariff 4 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading for tariff 4 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImportT4() {
        return energyImportT4Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for all tariffs in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for all tariffs in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExport() {
        return energyExportKwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 1 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 1 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExportT1() {
        return energyExportT1Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 2 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 2 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExportT2() {
        return energyExportT2Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 3 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 3 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExportT3() {
        return energyExportT3Kwh;
    }

    /**
     * Returns the energy feed-in meter reading for tariff 4 in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading for tariff 4 in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExportT4() {
        return energyExportT4Kwh;
    }

    /**
     * Returns the total active usage in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return total active usage in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getPower() {
        return powerW;
    }

    /**
     * Returns the active usage for phase 1 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active usage for phase 1 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getPowerL1() {
        return powerL1W;
    }

    /**
     * Returns the active usage for phase 2 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active usage for phase 2 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getPowerL2() {
        return powerL2W;
    }

    /**
     * Returns the active usage for phase 3 in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active usage for phase 3 in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getPowerL3() {
        return powerL3W;
    }

    /**
     * Returns the active voltage in ampere. This value is the sum of absolute values,
     * so if l1=2, l2=3, l3=-1 then this value is 6.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltage() {
        return voltageV;
    }

    /**
     * Returns the active voltage for phase 1 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 1 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageL1() {
        return voltageL1V;
    }

    /**
     * Returns the active voltage for phase 2 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 2 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageL2() {
        return voltageL2V;
    }

    /**
     * Returns the active voltage for phase 3 in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active voltage for phase 3 in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltageL3() {
        return voltageL3V;
    }

    /**
     * Returns the active current in ampere. This value is the sum of absolute values,
     * so if l1=2, l2=3, l3=-1 then this value is 6.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getCurrent() {
        return currentA;
    }

    /**
     * Returns the active current for phase 1 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 1 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getCurrentL1() {
        return currentL1A;
    }

    /**
     * Returns the active current for phase 2 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 2 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getCurrentL2() {
        return currentL2A;
    }

    /**
     * Returns the active current for phase 3 in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active current for phase 3 in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getCurrentL3() {
        return currentL3A;
    }

    /**
     * Returns the line frequency in hertz.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return line frequency in hertz
     * @see #updateMeasurements()
     */
    public OptionalDouble getFrequency() {
        return frequencyHz;
    }

    /**
     * Returns the number of voltage sags detected by meter for phase 1.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return number of 'long' power fails detected by meter
     * @see #updateMeasurements()
     */
    public OptionalDouble getLongPowerFailCount() {
        return longPowerFailCount;
    }

    /**
     * Returns the active average demand in watts, available for smart meters with capacity rate.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return active average demand in watts
     * @see #updateMeasurements()
     */
    public OptionalDouble getAveragePower() {
        return averagePower15mW;
    }

    /**
     * Returns the peak average demand of this month.
     * There is a spelling mistake with this data point in the official API, but in this Java API, it is fixed.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
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
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return timestamp when peak demand was registered
     * @see #updateMeasurements()
     */
    public OptionalDouble getMonthlyPowerPeakTimestamp() {
        return monthlyPowerPeakTimestamp;
    }

    /**
     * Returns a list of externally connected utility meters.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters">Official API documentation related to this method</a>
     *
     * @return a list of externally connected utility meters
     * @see #updateMeasurements()
     */
    public Optional<List<ExternalP1Device>> getExternalP1Devices() {
        return external;
    }
}
