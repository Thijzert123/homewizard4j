package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Thijzert123
 */
abstract class Updatable extends DataPrintable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    final ObjectMapper objectMapper;

    Updatable() {
        objectMapper = new ObjectMapper();
        // Makes sure Optional is supported
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * Updates the data from the device.
     * If you have made changes without saving them using {@link Savable#save(String, Object)}, they will be discarded!
     *
     * @throws HomeWizardApiException when something has gone wrong while updating data
     */
    void update(final String fullAddress, final Object objectToUpdate) throws HomeWizardApiException {
        LOGGER.debug("Updating device fields");

        final String responseBody = HttpUtils.getBody("GET", fullAddress);
        try {
            LOGGER.debug("Mapping body with ObjectMapper, updating this instance... ");
            objectMapper.readerForUpdating(objectToUpdate).readValue(responseBody);
            LOGGER.debug("Mapping body with ObjectMapper, updating this instance, done");
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }
}
