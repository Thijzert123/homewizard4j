package io.github.thijzert123.homewizard4j.v2;

import org.slf4j.Logger;

/**
 * When you try to make a request to the API and need a token, but the token is not present in an associated
 * {@link DeviceAuthorizer}, this exception gets thrown.
 *
 * @author Thijzert123
 */
public class NoTokenPresentException extends HomeWizardApiException {
    NoTokenPresentException(final String message, final Throwable cause, final Logger logger) {
        super(message, cause, logger);
    }

    NoTokenPresentException(final Throwable cause, final Logger logger) {
        super(cause, logger);
    }

    NoTokenPresentException(final String message, final Logger logger) {
        super(message, logger);
    }
}
