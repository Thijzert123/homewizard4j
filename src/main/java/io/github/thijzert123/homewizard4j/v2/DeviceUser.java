package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thijzert123
 */
public class DeviceUser {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("current")
    private final boolean current;

    public DeviceUser(final String name) {
        this.name = name;
        current = false;
    }

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
