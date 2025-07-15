package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Represents the actual state of an energy socket.
 *
 * @author Thijzert123
 * @see EnergySocket
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DeviceState extends Savable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String apiAddress;

    @JsonProperty("power_on")
    private Optional<Boolean> powerOn = Optional.empty();
    @JsonProperty("switch_lock")
    private Optional<Boolean> switchLock = Optional.empty();
    @JsonProperty("brightness")
    private OptionalInt brightness = OptionalInt.empty();

    DeviceState(final Device device) {
        apiAddress = device.getFullApiAddress() + "/state";
    }

    /**
     * Updates all the data. If you previously made changes, they will be discarded!
     *
     * @throws HomeWizardApiException when something goes wrong while updating
     */
    public void update() throws HomeWizardApiException {
        update(apiAddress, this);
    }

    @Override
    public void save() throws HomeWizardApiException {
        save(apiAddress, this);
    }

    /**
     * Returns whether the switch is on.
     *
     * @return whether the switch is on
     */
    public Optional<Boolean> getPowerOn() {
        return powerOn;
    }

    /**
     * Changes whether the switch is on.
     *
     * @param powerOn whether the switch is on
     */
    public void setPowerOn(final boolean powerOn) {
        this.powerOn = Optional.of(powerOn);
    }

    /**
     * Returns whether the switch lock is active. When active, the switch cannot be turned off.
     *
     * @return whether the switch lock is active
     */
    public Optional<Boolean> getSwitchLock() {
        return switchLock;
    }

    /**
     * Changes whether the switch lock is active. When active, the socket cannot be turned off.
     *
     * @param switchLock whether the switch lock is active
     */
    public void setSwitchLock(final boolean switchLock) {
        this.switchLock = Optional.of(switchLock);
    }

    /**
     * Returns the brightness of LED ring when socket is 'on'. Value from 0 (0%) to 255 (100%).
     *
     * @return the brightness of LED ring
     */
    public OptionalInt getBrightness() {
        return brightness;
    }

    /**
     * Returns the brightness of LED ring when socket is 'on'. Value from 0 (0%) to 255 (100%).
     *
     * @param brightness the brightness of LED ring
     */
    public void setBrightness(final int brightness) {
        this.brightness = OptionalInt.of(brightness);
    }
}
