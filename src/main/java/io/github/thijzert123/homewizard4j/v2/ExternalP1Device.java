package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Some smart meters have more than one external device connected to it.
 * This can be, for example, a gas and a water meter.
 * This class adds support for these meters. They are listed in {@link P1Meter#getExternalP1Devices()}.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this class</a>
 *
 * @author Thijzert123
 * @see P1Meter#getExternalP1Devices()
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ExternalP1Device {
    @JsonProperty("unique_id")
    private final Optional<String> uniqueId = Optional.empty();
    @JsonProperty("type")
    private final Optional<String> type = Optional.empty();
    @JsonProperty("timestamp")
    private final OptionalDouble timestamp = OptionalDouble.empty();
    @JsonProperty("value")
    private final OptionalDouble value = OptionalDouble.empty();
    @JsonProperty("unit")
    private final Optional<String> unit = Optional.empty();

    private ExternalP1Device() {
    }

    /**
     * Returns the unique identifier from this device.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this method</a>
     *
     * @return the unique identifier from this device
     */
    public Optional<String> getUniqueId() {
        return uniqueId;
    }

    /**
     * Returns the type of the device. It can only be one of these values:
     * <ul>
     *     <li><code>gas_meter</code>
     *     <li><code>heat_meter</code>
     *     <li><code>warm_water_meter</code>
     *     <li><code>water_meter</code>
     *     <li><code>inlet_heat_meter</code>
     * </ul>
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this method</a>
     *
     * @return the type of the device
     */
    public Optional<String> getType() {
        return type;
    }

    /**
     * Returns the timestamp provided by external device, describing when measurement was sent.
     * Timestamps are formatted via a ISO 8601 string ({@code YYYY-MM-DDTHH:mm:ss}).
     * The timestamp is without timezone information as this is not provided by the device.
     * Example: {@code 2024-06-28T14:12:34}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this method</a>
     *
     * @return the most recent value update time stamp
     */
    public OptionalDouble getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the raw value.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this method</a>
     *
     * @return the raw value
     */
    public OptionalDouble getValue() {
        return value;
    }

    /**
     * Returns the unit of the value, for example: <code>m3</code> or <code>GJ</code>,
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/measurement#external-devices">Official API documentation related to this method</a>
     *
     * @return the unit of the value
     */
    public Optional<String> getUnit() {
        return unit;
    }
}