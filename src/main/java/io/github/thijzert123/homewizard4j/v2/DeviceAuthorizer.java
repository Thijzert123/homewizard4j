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
 * This class serves as an authorizer for one device. For most API requests, you need a token. You can use this
 * class to get one. It works as follows:
 * <ul>
 *     <li>A request with a name is made to the device
 *     <li>The device denies, doesn't give a token and says to press the physical button on the device
 *     <li>The user presses the button
 *     <li>Another request is made within 30 seconds of pressing the button
 *     <li>The device responds with a token that you can use to make all other requests
 * </ul>
 *
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DeviceAuthorizer {
    /**
     * The status after authorizing.
     */
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

    /**
     * Makes an authorisation request to the device. You have to provide a name. With that username,
     * you can remove the token. The name has to satisfy this regex: {@code ^local\/[a-zA-Z0-9\-_/\\# ]{1,40}$}.
     * See <a href="https://api-documentation.homewizard.com/docs/v2/authorization#parameters">the official documentation</a> for more information.
     *
     * @param name name to use for authorisation
     * @return the status of the authorisation process
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
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

    /**
     * Continuously makes authorisation requests with {@link #authorize(String)} with the given interval. It blocks
     * until {@link #authorize(String)} returns {@link AuthorizeStatus#AUTHORISATION_SUCCESS}.
     *
     * @param name     name to use for authorisation
     * @param interval the interval between making requests in ms
     * @return the status of the authorisation process - should always be {@link AuthorizeStatus#AUTHORISATION_SUCCESS}
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
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

    /**
     * Does the same as {@link #waitForAuthorizeSuccess(String, long)}, but this method takes {@code 5000} ms as interval.
     *
     * @param name name to use for authorisation
     * @return the status of the authorisation process - should always be {@link AuthorizeStatus#AUTHORISATION_SUCCESS}
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
    public AuthorizeStatus waitForAuthorizeSuccess(final String name) throws HomeWizardApiException {
        return waitForAuthorizeSuccess(name, 5000);
    }

    /**
     * Returns the name associated with the token. This is available after the first request, the person does not
     * have to press the button for this information to be available.
     *
     * @return optional name associated with the token
     */
    public Optional<String> getName() {
        return name;
    }

    /**
     * Returns the token when the device is already authorized.
     *
     * @return the token
     */
    public Optional<String> getToken() {
        return token;
    }

    /**
     * Sets the name associated with the token. It is important to not change the name when one request has already
     * been made.
     *
     * @param name new name associated with the token
     */
    public void setName(final String name) {
        this.name = Optional.of(name);
    }

    /**
     * Sets the token if you already had one.
     *
     * @param token new token
     */
    public void setToken(final String token) {
        this.token = Optional.of(token);
    }

    /**
     * Returns the device associated with this authorizer.
     *
     * @return the device associated with this authorizer
     */
    public Device getDevice() {
        return device;
    }
}
