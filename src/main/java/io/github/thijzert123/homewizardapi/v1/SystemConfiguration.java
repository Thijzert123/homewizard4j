package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpRequest;
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
public class SystemConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Device device;
    private final String systemApiPath;

    @JsonProperty("cloud_enabled")
    private Optional<Boolean> cloudEnabled = Optional.empty();

    SystemConfiguration(final Device device) {
        this.device = device;
        systemApiPath = device.getFullApiPath() + "/system";
    }

    /**
     * Updates the data from the device. If you have made changes without saving them using {@link #save()},
     * they will be discarded!
     *
     * @return whether the action was successful
     */
    public boolean update() {
        LOGGER.debug("Updating system configuration");
        return device.update(systemApiPath, this);
    }

    /**
     * Saves the changed data to the device. If all data fields have an empty {@link Optional},
     * you this method fails and returns <code>false</code>.
     *
     * @return whether the action was successful
     */
    public boolean save() {
        LOGGER.debug("Saving system configuration");
        if (cloudEnabled.isEmpty()) {
            LOGGER.debug("All fields have empty optional, failed saving system configuration");
            return false;
        }
        HttpUtils.getBody("PUT", systemApiPath,
                HttpRequest.BodyPublishers.ofString("{\"cloud_enabled\": " + cloudEnabled.get() + "}"));
        return true;
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
    public Optional<Boolean> getCloudEnabled() {
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

    /**
     * Provides a human-readable {@link String} useful for debugging by calling all get* methods.
     *
     * @return a human-readable representation of this class
     */
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Method method : getClass().getMethods()) {
            final String name = method.getName();
            if (name.startsWith("get") && method.getParameterCount() == 0 && !name.equals("getClass")) {
                stringBuilder.append(name).append(" = ");
                String returnValue;
                try {
                    returnValue = method.invoke(this).toString();
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    returnValue = exception.getMessage();
                }
                stringBuilder.append(returnValue).append(", ");
            }
        }
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }
}
