package io.github.thijzert123.homewizard4j.v2;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Thijzert123
 */
public class HomeWizardErrorResponseException extends HomeWizardApiException {
    private static final Map<String, String> descriptions = new HashMap<>();
    static {
        descriptions.put("request:invalid-json", "Server was unable to parse the JSON data.");
        descriptions.put("json:no-parameters-recognized", "JSON was correct, but no required parameters were found.");
        descriptions.put("json:parameter-invalid-type", "Invalid type for a parameter, e.g. a string where a number is expected."); // json:parameter-invalid-type:<parameter>
        descriptions.put("json:parameter-missing", "Required parameter is missing."); // json:parameter-missing:<parameter>
        descriptions.put("json:parameter-not-exclusive", "Two or more parameters are mutually exclusive.");
        descriptions.put("request:api-version-not-supported", "Requested API version not supported.");
        descriptions.put("request:internal-server-error", "Something went wrong on the server side.");
        descriptions.put("request:too-large", "Request too large.");
        descriptions.put("request:unknown-subscription", "WebSocket subscription topic not recognized.");
        descriptions.put("request:unknown-type", "WebSocket type parameter not recognized.");
        descriptions.put("telegram:no-telegram-received", "No telegram received.");
        descriptions.put("user:creation-not-enabled", "User creation is not enabled, button press required.");
        descriptions.put("user:invalid-prefix", "Invalid name prefix provided.");
        descriptions.put("user:name-empty", "Upon creating a user, the name was empty.");
        descriptions.put("user:name-invalid", "Upon creating a user, the name contained invalid tokens.");
        descriptions.put("user:name-too-long", "Upon creating a user, the name was too long.");
        descriptions.put("user:no-storage", "No storage available to store a new user, delete existing users.");
        descriptions.put("user:not-found", "Tried to delete a user which does not exist.");
        descriptions.put("user:unauthorized", "User token is invalid or not authorized.");
    }

    private final String errorCode;

    HomeWizardErrorResponseException(final String errorCode, final Logger logger) {
        super("Got error code " + errorCode + ", description: ", logger);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        String errorCodeToLookUp = errorCode;
        final String[] errorCodeToLookUpSplit = errorCodeToLookUp.split(":");

        // when error code has a parameter, for example, json:parameter-missing:<parameter>,
        // only look up the first part in the description map
        if (errorCodeToLookUpSplit.length >= 3) {
            errorCodeToLookUp = errorCodeToLookUpSplit[0] + errorCodeToLookUpSplit[1];
        }

        return descriptions.get(errorCodeToLookUp);
    }
}
