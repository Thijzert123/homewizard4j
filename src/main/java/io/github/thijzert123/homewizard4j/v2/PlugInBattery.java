package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * A device that saves your solar energy for later.
 *
 * @author Thijzert123
 * @see Device
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class PlugInBattery extends Device {
    /**
     * Possible unique product identifiers for this device.
     *
     * @see #getProductType()
     */
    public static final List<String> PRODUCT_TYPES = List.of("HWE-BAT"); // TODO product type is maybe https://api-documentation.homewizard.com/docs/v2/authorization#hostname-validation

    @JsonProperty("energy_import_kwh")
    private final OptionalDouble energyImportKwh = OptionalDouble.empty();
    @JsonProperty("energy_export_kwh")
    private final OptionalDouble energyExportKwh = OptionalDouble.empty();
    @JsonProperty("power_w")
    private final OptionalDouble powerW = OptionalDouble.empty();
    @JsonProperty("voltage_l1_v")
    private final OptionalDouble voltageL1V = OptionalDouble.empty();
    @JsonProperty("current_a")
    private final OptionalDouble currentA = OptionalDouble.empty();
    @JsonProperty("frequency_hz")
    private final OptionalDouble frequencyHz = OptionalDouble.empty();
    @JsonProperty("state_of_charge_pct")
    private final OptionalDouble stateOfChargePct = OptionalDouble.empty();
    @JsonProperty("cycles")
    private final OptionalDouble cycles = OptionalDouble.empty();

    PlugInBattery(final Optional<String> serviceName,
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

    public PlugInBattery(final String hostAddress) {
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
     * Returns the energy usage meter reading in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return energy usage meter reading in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyImport() {
        return energyImportKwh;
    }

    /**
     * Returns the energy feed-in meter reading in kWh.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return energy feed-in meter reading in kWh
     * @see #updateMeasurements()
     */
    public OptionalDouble getEnergyExport() {
        return energyExportKwh;
    }

    /**
     * Returns the total active usage in watt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return total active usage in watt
     * @see #updateMeasurements()
     */
    public OptionalDouble getPower() {
        return powerW;
    }

    /**
     * Returns the active voltage in volt.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return active voltage in volt
     * @see #updateMeasurements()
     */
    public OptionalDouble getVoltage() {
        return voltageL1V;
    }

    /**
     * Returns the active current in ampere.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return active current in ampere
     * @see #updateMeasurements()
     */
    public OptionalDouble getCurrent() {
        return currentA;
    }

    /**
     * Returns the line frequency in hertz.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return line frequency in hertz
     * @see #updateMeasurements()
     */
    public OptionalDouble getFrequency() {
        return frequencyHz;
    }

    /**
     * Returns the current state of charge in percent.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return current state of charge in percent
     * @see #updateMeasurements()
     */
    public OptionalDouble getStateOfCharge() {
        return stateOfChargePct;
    }

    /**
     * Returns the number of battery cycles.
     * <p>
     * In order to get this information, you must first call {@link #updateMeasurements()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#parameters-3">Official API documentation related to this method</a>
     *
     * @return number of battery cycles
     * @see #updateMeasurements()
     */
    public OptionalDouble getCycles() {
        return cycles;
    }
}
