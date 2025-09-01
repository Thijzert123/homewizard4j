package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Thijzert123
 */
class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient;

    static {
        final TrustManager MOCK_TRUST_MANAGER = new X509ExtendedTrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
        };
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{MOCK_TRUST_MANAGER}, new SecureRandom());
            httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();
        } catch (final NoSuchAlgorithmException | KeyManagementException exception) {
            LOGGER.error("Something went wrong while creating HttpClient", exception);
            throw new RuntimeException(exception);
        }
    }

    private static HttpResponse<String> sendRequest(final String method,
                                            final String contentType,
                                            final String apiVersion,
                                            final Optional<String> token,
                                            final String fullAddress,
                                            final HttpRequest.BodyPublisher bodyPublisher) throws HomeWizardApiException {
        LOGGER.debug("Making web request {} to {}", method, fullAddress);

        final HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .method(method, bodyPublisher)
                .header("Content-Type", contentType)
                .header("X-Api-Version", apiVersion)
                .uri(URI.create(fullAddress));

        // Add token to headers when it is present
        token.ifPresent(s -> httpRequestBuilder.header("Authorization", "Bearer " + s));

        final HttpRequest httpRequest = httpRequestBuilder.build();

        try {
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            LOGGER.trace("Response body: {}", httpResponse.body());
            errorHandling(httpResponse);
            return httpResponse;
        } catch (final IOException | InterruptedException exception) {
            throw new HomeWizardApiException(exception, LOGGER);
        }
    }

    private static void errorHandling(final HttpResponse<String> httpResponse) throws HomeWizardApiException {
        final int statusCode = httpResponse.statusCode();
        LOGGER.trace("Handling errors, status code: {}", statusCode);
        if (statusCode >= 200 && statusCode <= 299) { // success
            return;
        }

        try {
            final Map<String, String> errorResponse = objectMapper.readValue(httpResponse.body(),
                    new TypeReference<HashMap<String, String>>() {});
            final String errorCode = errorResponse.get("error");
            throw new HomeWizardErrorResponseException(errorCode, LOGGER);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new HomeWizardApiException(jsonProcessingException, LOGGER);
        }
    }

    static HttpResponse<String> sendRequest(final String method,
                                            final String fullAddress,
                                            final HttpRequest.BodyPublisher bodyPublisher) throws HomeWizardApiException {
        return sendRequest(
                method,
                "application/json",
                "2.0.1",
                Optional.empty(),
                fullAddress,
                bodyPublisher
        );
    }

    static HttpResponse<String> sendRequest(final String method,
                                            final String token,
                                            final String fullAddress,
                                            final HttpRequest.BodyPublisher bodyPublisher) throws HomeWizardApiException {
        return sendRequest(
                method,
                "application/json",
                "2.0.1",
                Optional.of(token),
                fullAddress,
                bodyPublisher
        );
    }

    static HttpResponse<String> sendRequest(final String method,
                                            final String token,
                                            final String fullAddress) throws HomeWizardApiException {
        return sendRequest(
                method,
                "application/json",
                "2.0.1",
                Optional.of(token),
                fullAddress,
                HttpRequest.BodyPublishers.noBody()
        );
    }

    static HttpResponse<String> sendRequest(final String token, final String fullAddress) throws HomeWizardApiException {
        return sendRequest(
                "GET",
                "application/json",
                "2.0.1",
                Optional.of(token),
                fullAddress,
                HttpRequest.BodyPublishers.noBody()
        );
    }
}
