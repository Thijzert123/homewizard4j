package io.github.thijzert123.homewizard4j.v2;

import org.slf4j.Logger;

/**
 * When you try to make a request to the API and need a token, but the token is not present in an associated
 * {@link DeviceAuthorizer}, this exception gets thrown.
 *
 * @author Thijzert123
 */
public class NoTokenPresentException extends HomeWizardApiException {
    NoTokenPresentException(final Logger logger) {
        super("No token present in an associated DeviceAuthorizer", logger);
    }
}
