package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thijzert123
 */
class Error {
    @JsonProperty("id")
    private int errorCode;
    @JsonProperty("description")
    private String description;

    int getErrorCode() {
        return errorCode;
    }

    String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Error code " + errorCode + ": " + description;
    }
}
