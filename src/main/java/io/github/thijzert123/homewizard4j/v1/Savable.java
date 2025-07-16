package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;

/**
 * @author Thijzert123
 */
abstract class Savable extends Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Saves all the changed data.
     *
     * @throws HomeWizardApiException when something goes wrong while saving
     */
    public void save(final String fullAddress) throws HomeWizardApiException {
        LOGGER.debug("Saving system configuration");

        try {
            final String body = objectMapper.writeValueAsString(this);

            HttpUtils.getBody("PUT", fullAddress,
                    HttpRequest.BodyPublishers.ofString(body));
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Saves all the changed data to the device.
     *
     * @throws HomeWizardApiException when something goes wrong while saving
     */
    public abstract void save() throws HomeWizardApiException;
}
