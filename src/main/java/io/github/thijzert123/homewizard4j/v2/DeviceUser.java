package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a registered user on a HomeWizard device.
 * When registering a token, the HomeWizard device saves the username you specified when authorising.
 *
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DeviceUser {
    /**
     * The status of authorizing.
     */
    public enum AuthorizeStatus {
        /**
         * You have successfully authorised with the device, and you can get the token with {@link #getToken()}.
         */
        AUTHORISATION_SUCCESS,
        /**
         * So far, authorisation is going well, but the user still needs to press the button before calling
         * {@link #authorize} again.
         */
        NEEDS_BUTTON_PRESS
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Device device;

    @JsonProperty("name")
    private final String name;
    private Optional<String> token = Optional.empty();

    DeviceUser(final Device device, final String name) {
        this.device = device;
        this.name = name;
    }

    DeviceUser(final Device device, final String name, final String token) {
        this(device, name);
        this.token = Optional.of(token);
    }

    /**
     * Makes an authorisation request to the device. You have to provide a name. With that username,
     * you can remove the token. The name has to satisfy this regex: {@code ^local\/[a-zA-Z0-9\-_/\\# ]{1,40}$}.
     * See <a href="https://api-documentation.homewizard.com/docs/v2/authorization#parameters">the official documentation</a> for more information.
     *
     * @return the status of the authorisation process
     * @throws HomeWizardErrorResponseException when an unexpected error response gets returned
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
    public AuthorizeStatus authorize() throws HomeWizardApiException {
        try {
            // create body to send with the request
            final Map<String, String> requestBodyMap = Map.of("name", name);
            final String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            try {
                final HttpResponse<String> httpResponse = HttpUtils.sendRequest(
                        "POST",
                        device.createFullApiAddress("/api/user"),
                        HttpRequest.BodyPublishers.ofString(requestBody));

                final Map<String, String> response = objectMapper.readValue(httpResponse.body(),
                        new TypeReference<HashMap<String, String>>() {});

                token = Optional.of(response.get("token"));
                return AuthorizeStatus.AUTHORISATION_SUCCESS;
            } catch (final HomeWizardErrorResponseException homeWizardErrorResponseException) {
                final String errorCode = homeWizardErrorResponseException.getErrorCode();
                if (Objects.equals(errorCode, "user:creation-not-enabled")) {
                    return AuthorizeStatus.NEEDS_BUTTON_PRESS;
                } else {
                    throw new HomeWizardApiException("Unknown error response while authorizing: " + errorCode, LOGGER);
                }
            }
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    /**
     * Continuously makes authorisation requests with {@link #authorize} with the given interval. It blocks
     * until {@link #authorize} returns {@link DeviceAuthorizer.AuthorizeStatus#AUTHORISATION_SUCCESS}.
     *
     * @param interval the interval between making requests in ms
     * @return the status of the authorisation process - should always be {@link DeviceAuthorizer.AuthorizeStatus#AUTHORISATION_SUCCESS}
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
    public AuthorizeStatus waitForAuthorizeSuccess(final long interval) throws HomeWizardApiException {
        while (true) {
            final AuthorizeStatus authorizeStatus = authorize();

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
     * Does the same as {@link #waitForAuthorizeSuccess(long)}, but this method takes {@code 5000} ms as interval.
     *
     * @return the status of the authorisation process - should always be {@link DeviceAuthorizer.AuthorizeStatus#AUTHORISATION_SUCCESS}
     * @throws HomeWizardApiException when something has gone wrong while making the API request
     */
    public AuthorizeStatus waitForAuthorizeSuccess() throws HomeWizardApiException {
        return waitForAuthorizeSuccess(5000);
    }

    public boolean isAuthorized() {
        return token.isPresent();
    }

    /**
     * Returns the name of the user, used for identification
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    // TODO add javadoc to methods below

    public Optional<String> getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = Optional.of(token);
    }

    public void setAsDefault() { // TODO add javadoc
        device.getUserManagement().setCurrentUser(this);
    }
}
