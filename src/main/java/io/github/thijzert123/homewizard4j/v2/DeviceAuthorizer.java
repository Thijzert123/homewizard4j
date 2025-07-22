package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thijzert123.homewizard4j.v2.json.AuthorizeRequest;
import io.github.thijzert123.homewizard4j.v2.json.AuthorizeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * With this authorizer, you are able to request a token.
 *
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DeviceAuthorizer {
    public enum AuthorizeStatus {
        AUTHORISATION_SUCCESS,
        NEEDS_BUTTON_PRESS
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Device device;

    private Optional<String> name = Optional.empty();
    private Optional<String> token = Optional.empty();

    DeviceAuthorizer(final Device device) {
        this.device = device;
    }

    public AuthorizeStatus authorize(final String name) throws HomeWizardApiException {
        this.name = Optional.of(name);
        final AuthorizeRequest authorizeRequestBody = new AuthorizeRequest(name);
        try {
            final String authorizeRequestBodyJson = objectMapper.writeValueAsString(authorizeRequestBody);
            final HttpResponse<String> httpResponse = HttpUtils.sendRequest(
                    "POST",
                    device.createFullApiAddress("/api/user"),
                    HttpRequest.BodyPublishers.ofString(authorizeRequestBodyJson));

            final AuthorizeResponse authorizeResponse = objectMapper.readValue(httpResponse.body(), AuthorizeResponse.class);
            final String error = authorizeResponse.getError();
            if (error != null) {
                // The button has not been pressed
                if (Objects.equals(error, "user:creation-not-enabled")) {
                    return AuthorizeStatus.NEEDS_BUTTON_PRESS;
                } else {
                    throw new HomeWizardApiException("Unknown error response while authorizing: " + error, LOGGER);
                }
            } else {
                this.token = Optional.of(authorizeResponse.getToken());
                return AuthorizeStatus.AUTHORISATION_SUCCESS;
            }
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    public AuthorizeStatus waitForAuthorizeSuccess(final String name, final long interval) throws HomeWizardApiException {
        while (true) {
            final AuthorizeStatus authorizeStatus = authorize(name);

            if (authorizeStatus == AuthorizeStatus.AUTHORISATION_SUCCESS) {
                return AuthorizeStatus.AUTHORISATION_SUCCESS;
            }

            try {
                Thread.sleep(interval);
            } catch (final InterruptedException interruptedException) {
                LOGGER.error(interruptedException.getMessage(), interruptedException);
                Thread.currentThread().interrupt();
            }
        }
    }

    public AuthorizeStatus waitForAuthorizeSuccess(final String name) throws HomeWizardApiException {
        return waitForAuthorizeSuccess(name, 5000);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getToken() {
        return token;
    }

    public void setName(final String name) {
        this.name = Optional.of(name);
    }

    public void setToken(final String token) {
        this.token = Optional.of(token);
    }

    public Device getDevice() {
        return device;
    }
}
