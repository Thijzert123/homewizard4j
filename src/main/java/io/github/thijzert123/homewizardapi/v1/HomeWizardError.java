package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The error that is inside the {@link ErrorResponse}. This contains the actual information about the error.
 * The error codes are defined by HomeWizard, they are NOT the same as the universal HTTP status codes.
 * <p>
 * <a href="https://api-documentation.homewizard.com/docs/v1/error-handling#error-codes">Official API documentation related to this class</a>
 *
 * @author Thijzert123
 * @see ErrorResponse
 */
public class HomeWizardError {
    @JsonProperty("id")
    private int errorCode;
    @JsonProperty("description")
    private String description;

    private HomeWizardError() {
    }

    /**
     * Returns the error code, such as {@code 202} or {@code 901}.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the provided error description.
     *
     * @return the error description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Generates a {@link String} representation of this error,
     * for example: {@code HomeWizardError code 202: API not enabled}.
     *
     * @return {@link String} representation of this error
     */
    @Override
    public String toString() {
        return "HomeWizardError code " + errorCode + ": " + description;
    }
}
