package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Thijzert123
 */
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    final ObjectMapper objectMapper;

    Updatable() {
        // Only (de)serialize annotated fields (@JsonProperty)
        objectMapper = JsonMapper.builder()
                .disable(MapperFeature.AUTO_DETECT_CREATORS,
                        MapperFeature.AUTO_DETECT_FIELDS,
                        MapperFeature.AUTO_DETECT_GETTERS,
                        MapperFeature.AUTO_DETECT_IS_GETTERS)
                .build();

        // Makes sure Optional is supported
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * Updates the data from the device.
     * If you have made changes without saving them using {@link Savable#save(String)}, they will be discarded!
     *
     * @throws HomeWizardApiException when something has gone wrong while updating data
     */
    void update(final String fullAddress) throws HomeWizardApiException {
        LOGGER.debug("Updating fields...");

        final String responseBody = HttpUtils.getBody("GET", fullAddress);
        try {
            LOGGER.trace("Mapping body '{}' with ObjectMapper, updating this instance...", responseBody);
            objectMapper.readerForUpdating(this).readValue(responseBody);
            LOGGER.trace("Mapping body with ObjectMapper, updating this instance, done");
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }
}
