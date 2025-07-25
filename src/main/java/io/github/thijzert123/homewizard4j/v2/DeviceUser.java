package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a registered user on a HomeWizard device.
 * When registering a token, the HomeWizard device saves the username you specified when authorising.
 *
 * @author Thijzert123
 */
public class DeviceUser {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("current")
    private final boolean current;

    /**
     * Creates an instance based on the given name.
     * @param name identifier of user
     */
    public DeviceUser(final String name) {
        this.name = name;
        current = false;
    }

    /**
     * Returns the name
     * @return
     */
    public String getName() {
        return name;
    }

    public boolean isCurrentUser() {
        return current;
    }

    public static DeviceUser fromLocal(final String name) {
        return new DeviceUser("local/" + name);
    }

    public static DeviceUser fromCloud(final String name) {
        return new DeviceUser("cloud/" + name);
    }
}
