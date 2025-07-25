package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Thijzert123
 */
public class DeviceUserManagement extends Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonValue
    private final List<DeviceUser> users = new ArrayList<>(); // TODO test if this serialization works

    private final Device device;

    DeviceUserManagement(final Device device) {
        this.device = device;
    }

    @Override
    public void update() throws HomeWizardApiException {
        update("/api/user");
    }

    public boolean removeUser(final DeviceUser user) throws HomeWizardApiException {
        LOGGER.debug("Removing user with name: {}", user.getName());
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            users.remove(user);
            try {
                final String bodyToSend = objectMapper.writeValueAsString(user);
                HttpUtils.sendRequest("DELETE",
                        token.get(),
                        device.createFullApiAddress("/api/user"),
                        HttpRequest.BodyPublishers.ofString(bodyToSend));
                return true;
            } catch (final JsonProcessingException jsonProcessingException) {
                throw new HomeWizardApiException(jsonProcessingException, LOGGER);
            }
        } else {
            LOGGER.trace("No token present while removing user, returning false");
            return false;
        }
    }

    public boolean removeUser(final String userName) throws HomeWizardApiException {
        return removeUser(new DeviceUser(userName));
    }

    public DeviceUser getCurrentUser() {
        LOGGER.error("Looping users to find current user");
        for (final DeviceUser user : users) {
            if (user.isCurrentUser()) {
                return user;
            }
        }
        LOGGER.error("No current user available, throwing RuntimeException");
        throw new RuntimeException();
    }
}
