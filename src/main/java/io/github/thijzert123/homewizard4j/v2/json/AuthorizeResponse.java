package io.github.thijzert123.homewizard4j.v2.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thijzert123
 */
public class AuthorizeResponse {
    @JsonProperty("error")
    private String error;
    @JsonProperty("token")
    private String token;
    @JsonProperty("name")
    private String name;

    public String getError() {
        return error;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
