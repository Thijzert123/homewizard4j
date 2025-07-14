package io.github.thijzert123.homewizard4j.v1;

import org.slf4j.Logger;

/**
 * When something has gone wrong while doing actions related to the HomeWizard API, this exception can be thrown.
 * The only thing special about this exception is that it automatically logs to the provided logger.
 * <p>
 * You should not have to throw this exception.
 *
 * @author Thijzert123
 */
public class HomeWizardApiException extends Exception {
    HomeWizardApiException(final String message, final Throwable cause, final Logger logger) {
        super(message, cause);
        logger.error(message, cause);
    }

    HomeWizardApiException(final Throwable cause, final Logger logger) {
        super(cause);
        logger.error(cause.getMessage(), cause);
    }

    HomeWizardApiException(final String message, final Logger logger) {
        super(message);
        logger.error(message);
    }
}
