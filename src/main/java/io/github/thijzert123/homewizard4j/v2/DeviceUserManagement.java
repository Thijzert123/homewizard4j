package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Removes token associated with the given user.
     *
     * @param user user to remove
     * @throws NoTokenPresentException when no token is present in an associated {@link DeviceAuthorizer}
     * @throws HomeWizardErrorResponseException when an unexpected error response gets returned
     * @throws HomeWizardApiException when something else has gone wrong while removing the user
     */
    public void removeUser(final DeviceUser user) throws HomeWizardApiException {
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
            } catch (final JsonProcessingException jsonProcessingException) {
                throw new HomeWizardApiException(jsonProcessingException, LOGGER);
            }
        } else {
            LOGGER.trace("No token present while removing user, throwing NoTokenPresentException");
            throw new NoTokenPresentException(LOGGER);
        }
    }

    /**
     * Removes token associated with a user. It creates an {@link DeviceUser} based on the given username.
     *
     * @param username identifier of user to remove
     * @throws NoTokenPresentException when no token is present in an associated {@link DeviceAuthorizer}
     * @throws HomeWizardErrorResponseException when an unexpected error response gets returned
     * @throws HomeWizardApiException when something else has gone wrong while removing the user
     */
    public void removeUser(final String username) throws HomeWizardApiException {
        removeUser(new DeviceUser(username));
    }

    /**
     * Returns the user of the token that is currently being used.
     *
     * @return the user of the token that is currently being used
     */
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
