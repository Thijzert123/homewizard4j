package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

/**
 * The configuration for a HomeWizard device.
 * This class allows you to retrieve the configuration as well as change and save it.
 * <p>
 * When first acquiring an instance of this class, all fields will have an empty {@link Optional} in it.
 * To change this, use {@link #update()} to update these fields so you can use them.
 *
 * @author Thijzert123
 * @see #update()
 * @see Optional
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SystemConfiguration extends Savable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String apiAddress;

    @JsonProperty("cloud_enabled")
    private Optional<Boolean> cloudEnabled = Optional.empty();

    SystemConfiguration(final Device device) {
        apiAddress = device.getFullApiAddress() + "/system";
    }

    /**
     * Updates all the data in the device.
     *
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public void update() throws HomeWizardApiException {
        update(apiAddress);
    }

    @Override
    public void save() throws HomeWizardApiException {
        save(apiAddress);
    }

    /**
     * Gets if the cloud is enabled.
     * <p>
     * In order to get this information, you must first call {@link #update()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/system#enable-cloud">Official API documentation related to this method</a>
     *
     * @return whether the cloud is enabled
     */
    public Optional<Boolean> isCloudEnabled() {
        LOGGER.debug("getCloudEnabled: '{}'", cloudEnabled);
        return cloudEnabled;
    }

    /**
     * Change if the cloud is enabled. You have to call {@link #save()} to save the data to the device.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v1/system#enable-cloud">Official API documentation related to this method</a>
     *
     * @param cloudEnabled whether the cloud is enabled
     * @see #save()
     */
    public void setCloudEnabled(final boolean cloudEnabled) {
        LOGGER.debug("setCloudEnabled: '{}'", cloudEnabled);
        this.cloudEnabled = Optional.of(cloudEnabled);
    }
}
