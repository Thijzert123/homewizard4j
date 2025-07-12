package io.github.thijzert123.homewizardapi.v1;

import io.github.thijzert123.homewizardapi.HomeWizardApiException;
import org.slf4j.Logger;

/**
 * @author Thijzert123
 */
public class HomeWizardErrorResponseException extends HomeWizardApiException {
  HomeWizardErrorResponseException(final ErrorResponse errorResponse, final Logger logger) {
    super(errorResponse.getError().toString(), logger);
  }
}
