package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Thijzert123
 */
public class DeviceUserManagement extends Updatable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonValue
    private final List<HashMap<String, ?>> usersDeserialisation = new ArrayList<>(); // TODO test if this serialization works

    private final Device device;
    private final List<DeviceUser> users = new ArrayList<>();
    private DeviceUser currentUser;

    DeviceUserManagement(final Device device) {
        this.device = device;
        setDevice(device);
    }

    @Override
    public void update() throws HomeWizardApiException { // TODO test if this testing works
        update("/api/user");
        for (final HashMap<String, ?> user : usersDeserialisation) {
            users.add(new DeviceUser(device, (String) user.get("name")));
        }
    }

    public DeviceUser createUser(final String name) {
        return new DeviceUser(device, name);
    }

    /**
     * Create an already authorized user with the provided token. If you do not have access to the name,
     * you can technically input an empty string, and everything will work fine. This, however,
     * is not recommended, as the returned {@link DeviceUser} will be incomplete and it will be harder to remove
     * the token. This method won't add the user to the list returned by {@link #getUsers()}.
     *
     * @param name name of user to create
     * @param token token for user
     * @return the created user
     */
    public DeviceUser createUser(final String name, final String token) {
        return new DeviceUser(device, name, token);
    }

    /**
     * Removes token associated with the given user. This method won't remove the user from the list
     * returned by {@link #getUsers()}.
     *
     * @param user user to remove
     * @throws NoTokenPresentException          when no token is present in an associated {@link DeviceAuthorizer}
     * @throws HomeWizardErrorResponseException when an unexpected error response gets returned
     * @throws HomeWizardApiException           when something else has gone wrong while removing the user
     */
    public void removeUser(final DeviceUser user) throws HomeWizardApiException {
        LOGGER.debug("Removing user with name: {}", user.getName());
        final Optional<String> token = device.getToken();
        if (token.isPresent()) {
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
     * @throws NoTokenPresentException          when no token is present in an associated {@link DeviceAuthorizer}
     * @throws HomeWizardErrorResponseException when an unexpected error response gets returned
     * @throws HomeWizardApiException           when something else has gone wrong while removing the user
     */
    public void removeUser(final String username) throws HomeWizardApiException {
        removeUser(new DeviceUser(device, username));
    }

    /**
     * Returns all users registered on this device.
     *
     * @return all users registered on this device
     */
    public List<DeviceUser> getUsers() {
        return users;
    }

    public DeviceUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final DeviceUser currentUser) {
        this.currentUser = currentUser;
    }
}
