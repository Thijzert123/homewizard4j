package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;

/**
 * This exception is thrown when the HTTP response status code was not OK.
 * HTTP status codes are universal status codes that apply everywhere, e.g. {@code 200}=OK, {@code 400}=Bad request, etc.
 * The code {@code 200} means that the response and request were OK and ready for further use.
 * Code {@code 400} makes it so that it doesn't contain a body, so a {@link HomeWizardApiException} is thrown instead.
 *
 * @author Thijzert123
 * @see HomeWizardApiException
 */
public class HomeWizardErrorResponseException extends HomeWizardApiException {
    private final ErrorResponse errorResponse;

    HomeWizardErrorResponseException(final ErrorResponse errorResponse, final Logger logger) {
        super(errorResponse.getHttpError().toString(), logger);
        this.errorResponse = errorResponse;
    }

    /**
     * Returns the deserialized HTTP response.
     *
     * @return the error response
     */
    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
