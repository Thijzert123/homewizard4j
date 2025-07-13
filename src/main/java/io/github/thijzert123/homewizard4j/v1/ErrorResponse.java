package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The error response that you get when an {@link HomeWizardErrorResponseException} is thrown.
 *
 * @author Thijzert123
 * @see HomeWizardErrorResponseException
 */
public class ErrorResponse {
    @JsonProperty("homeWizardError")
    private HomeWizardError homeWizardError;

    private ErrorResponse() {
    }

    /**
     * Returns the HomeWizard-specific error
     *
     * @return the HomeWizard error
     */
    public HomeWizardError getHttpError() {
        return homeWizardError;
    }
}
