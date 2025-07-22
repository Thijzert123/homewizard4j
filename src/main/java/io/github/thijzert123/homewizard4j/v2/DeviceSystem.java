package io.github.thijzert123.homewizard4j.v2;

import java.util.Optional;

/**
 * @author Thijzert123
 */
public class DeviceSystem extends Updatable {
    private final Device device;

    DeviceSystem(final Device device) {
        this.device = device;
    }

    public boolean update() throws HomeWizardApiException {
        final Optional<String> token = device.getAuthorizer().getToken();
        if (token.isPresent()) {
            update(token.get(), device.createFullApiAddress("/api"));
            return true;
        } else {
            return false;
        }
    }
}
