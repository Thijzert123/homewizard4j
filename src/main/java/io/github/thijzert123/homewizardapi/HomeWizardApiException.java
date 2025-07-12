package io.github.thijzert123.homewizardapi;

import org.slf4j.Logger;

/**
 * @author Thijzert123
 */
public class HomeWizardApiException extends RuntimeException {
    public HomeWizardApiException(final String message, final Throwable cause, final Logger logger) {
        super(message, cause);
        logger.error(message, cause);
    }

    public HomeWizardApiException(final Throwable cause, final Logger logger) {
        super(cause);
        logger.error(cause.getMessage(), cause);
    }

    public HomeWizardApiException(final String message, final Logger logger) {
        super(message);
        logger.error(message);
    }
}
