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
        LOGGER.debug("Saving fields...");

        try {
            final String requestBody = objectMapper.writeValueAsString(this);
            LOGGER.trace("Requesting with body: '{}'", requestBody);
            final String responseBody = HttpUtils.getBody("PUT", fullAddress,
                    HttpRequest.BodyPublishers.ofString(requestBody));
            LOGGER.trace("Response with body: '{}'", responseBody);
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
