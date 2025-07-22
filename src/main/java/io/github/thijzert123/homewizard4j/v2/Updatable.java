package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpResponse;

/**
 * @author Thijzert123
 */
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

    void update(final String token, final String fullAddress) throws HomeWizardApiException {
        LOGGER.debug("Updating instance from {}", fullAddress);
        final HttpResponse<String> httpResponse = HttpUtils.sendRequest(token, fullAddress);

        try {
            objectMapper.readerForUpdating(this).readValue(httpResponse.body());
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Updates all the data of this instance.
     *
     * @return {@code true} when successful, otherwise {@code false}
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    public abstract boolean update() throws HomeWizardApiException;
}
