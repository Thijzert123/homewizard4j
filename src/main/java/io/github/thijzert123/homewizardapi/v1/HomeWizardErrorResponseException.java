package io.github.thijzert123.homewizardapi.v1;

import org.slf4j.Logger;

/**
 * This exception is thrown when the HTTP response status code
 * (universal status codes that apply everywhere, e.g. {@code 200}=OK, {@code 400}=Bad request, etc.)
 * was not {@code 200} or {@code 400}. The code {@code 200} explains itself,
 * as that means the response and request are good and ready for further use.
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
