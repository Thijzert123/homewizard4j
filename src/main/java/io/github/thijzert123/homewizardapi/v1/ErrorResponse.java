package io.github.thijzert123.homewizardapi.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thijzert123
 */
class ErrorResponse {
    @JsonProperty("error")
    private Error error;

    Error getError() {
        return error;
    }
}
