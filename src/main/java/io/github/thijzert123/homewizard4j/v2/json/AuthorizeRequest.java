package io.github.thijzert123.homewizard4j.v2.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thijzert123
 */
public class AuthorizeRequest {
    @JsonProperty("name")
    private String name;

    public AuthorizeRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
