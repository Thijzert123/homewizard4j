package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thijzert123.homewizardapi.HomeWizardApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class for internal HTTP traffic.
 *
 * @author Thijzert123
 */
public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    /**
     * Set your own {@link HttpClient} to customize things like timeout duration.
     * Make sure to use {@link HttpClient.Version#HTTP_1_1} for HTTP version, otherwise requests won't work.
     * When calling this method, it checks if you have set the correct {@link HttpClient.Version}.
     * If you have, it returns <code>true</code>, otherwise it returns <code>false.</code>.
     * You are allowed to set the client in both cases.
     *
     * @param httpClient the new HTTP client
     * @return whether the HTTP version is correct for this API
     */
    public static boolean setHttpClient(final HttpClient httpClient) {
        LOGGER.trace("Set HTTP client to '{}'", httpClient.toString());
        HttpUtils.httpClient = httpClient;
        return httpClient.version() == HttpClient.Version.HTTP_1_1;
    }

    /**
     * Makes an HTTP request and returns the body. If you input the fullAddress without <code>http://</code>,
     * it appends that at the front of the address.
     *
     * @param method request method
     * @param fullAddress full address, can be without <code>http://</code>
     * @param bodyPublisher body publisher if necessary for the request method
     * @return body of the request
     */
    static String getBody(final String method, String fullAddress, final HttpRequest.BodyPublisher bodyPublisher) {
        if (!fullAddress.startsWith("http://")) {
            fullAddress = "http://" + fullAddress;
        }

        LOGGER.debug("Request body, method: '{}', fullAddress: '{}'", method, fullAddress);
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .method(method, bodyPublisher)
                .uri(URI.create(fullAddress))
                .build();

        try {
            LOGGER.trace("Sending request");
            final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            checkErrors(response);

            final String body = response.body();
            LOGGER.debug("GET body: '{}'", body);
            return body;
        } catch (final IOException | InterruptedException exception) {
            throw new HomeWizardApiException(exception, LOGGER);
        }
    }

    /**
     * <a href="https://api-documentation.homewizard.com/docs/v1/error-handling#error-codes">Official API documentation</a>
     *
     * @param response the response to check
     * @throws HomeWizardErrorResponseException when status code wasn't 200 or 400 and the error has been handled correctly
     * @throws HomeWizardApiException when something else has gone wrong, or if status code was 400
     */
    private static void checkErrors(final HttpResponse<String> response) {
        final int statusCode = response.statusCode();
        LOGGER.trace("Check errors, status code '{}'", statusCode);

        if (statusCode == 200) return; // success code
        if (statusCode == 400) { // doesn't contain body, so should be handled separately
            throw new HomeWizardApiException("Got status code " + statusCode + ", is HttpClient.Version set to HTTP_1_1?", LOGGER);
        }

        final String body = response.body();
        try {
            LOGGER.trace("Mapping body to ErrorResponse");
            final ErrorResponse errorResponse = OBJECT_MAPPER.readValue(body, ErrorResponse.class);
            throw new HomeWizardErrorResponseException(errorResponse, LOGGER);
        } catch (final JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException("While checkErrors, status code was: " + statusCode, jsonProcessingException, LOGGER);
        }
    }
}
