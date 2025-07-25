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
import java.util.Optional;

/**
 * @author Thijzert123
 */
abstract class Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Device device;
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
     * Used for token. You should always call this method when initialising an Updatable.
     *
     * @param device new Device
     */
    void setDevice(final Device device) {
        this.device = device;
    }

    void update(final String apiEndpoint) throws HomeWizardApiException {
        final String fullAddress = device.createFullApiAddress(apiEndpoint);
        LOGGER.debug("Updating instance of {} from address: {}", getClass().getName(), fullAddress);

        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isEmpty()) {
            throw new NoTokenPresentException(LOGGER);
        }

        final HttpResponse<String> httpResponse = HttpUtils.sendRequest(token.get(), fullAddress);

        try {
            objectMapper.readerForUpdating(this).readValue(httpResponse.body());
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Updates all possible data of this instance, including (if present) sub-data classes like {@link DeviceSystem}.
     *
     * @throws NoTokenPresentException when no token was present in the associated {@link DeviceAuthorizer}
     * @throws HomeWizardErrorResponseException when an unexpected response gets returned
     * @throws HomeWizardApiException when something else has gone wrong while updating
     */
    public abstract void update() throws HomeWizardApiException;
}
