package io.github.thijzert123.homewizard4j.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

/**
 * @author Thijzert123
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class Device extends Updatable { // TODO check all Javadoc, as well as links to official documentation
    /**
     * The default port of the API.
     */
    public static final int DEFAULT_PORT = 443; // TODO what is the default port (probably 443)
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @JsonProperty("service_name")
    private final Optional<String> serviceName;
    @JsonProperty("host_address")
    private final String hostAddress;
    @JsonProperty("port")
    private final int port;

    @JsonProperty("product_type")
    private final Optional<String> productType;
    @JsonProperty("product_name")
    private final Optional<String> productName;
    @JsonProperty("serial")
    private final Optional<String> serial;
    @JsonProperty("id")
    private final Optional<String> id;
    @JsonProperty("api_version")
    private final Optional<String> apiVersion;
    @JsonProperty("firmware_version")
    private final Optional<String> firmwareVersion = Optional.empty();

    private final DeviceAuthorizer authorizer;
    private final DeviceSystem system;

    // TODO wifi info

    Device(final Optional<String> serviceName,
           final String hostAddress,
           final int port,
           final Optional<String> productType,
           final Optional<String> productName,
           final Optional<String> serial,
           final Optional<String> id,
           final Optional<String> apiVersion) {
        LOGGER.trace("Initializing Device...");

        this.serviceName = serviceName;
        this.hostAddress = hostAddress;
        this.port = port;

        this.productType = productType;
        this.productName = productName;
        this.serial = serial;
        this.id = id;
        this.apiVersion = apiVersion;

        authorizer = new DeviceAuthorizer(this);
        system = new DeviceSystem(this);

        // TODO system configuration
    }

    // TODO getFullApiPath or something

    String createFullApiAddress(final String apiEndpoint) {
        return getFullAddress() + apiEndpoint;
    }

    /**
     * Update all the basic information from the device. This method returns {@code true} when updating was
     * successful. If the token is not present ({@link DeviceAuthorizer#getToken()}), this method returns {@code false}.
     *
     * @return {@code true} when successful
     * @throws HomeWizardApiException when something has gone wrong while updating
     */
    @Override
    public boolean update() throws HomeWizardApiException {
        final Optional<String> token = authorizer.getToken();
        if (token.isPresent()) {
            update(token.get(), createFullApiAddress("/api"));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the full qualified mDNS service name.
     * For example: <code>watermeter-ABC123._homewizard._tcp.local.</code>.
     * <p>
     * This information is always available if the instance was returned by {@link HomeWizardDiscoverer}.
     * Otherwise, you can never get this information, even if you use {@link #update()}.
     *
     * @return full qualified service name
     */
    public Optional<String> getServiceName() {
        return serviceName;
    }

    /**
     * Returns the full address. For example: <code>https://192.168.1.123:443</code>. // TODO check port here
     * It is constructed like this:
     * <p>
     * <code>https://</code> + {@link #getHostAddress()} + <code>:</code> + {@link #getPort()}
     *
     * @return full address
     * @see #getHostAddress()
     * @see #getPort()
     */
    public String getFullAddress() {
        return "https://" + getHostAddress() + ":" + getPort();
    }

    /**
     * Returns the host address. This is the IP address of the device.
     * For example: <code>192.168.1.123</code>.
     * <p>
     * This information is always available.
     *
     * @return returns the host address
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Returns the port. Because the <code>v2</code> API uses HTTPS, the port should always be <code>443</code>. // TODO check port here
     * <p>
     * This information is always available.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the serial number, also used as the MAC address.
     * This unique value per device consists of 12 hexadecimal characters,
     * for example, <code>1a2b3c4d5e6f</code>. The serial is used for unique identification:
     * if serial is the same, the whole device must be the same.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #update()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/device_information#parameters">Official API documentation related to this method</a>
     *
     * @return a unique serial number for this device
     */
    public Optional<String> getSerial() {
        return serial;
    }

    /**
     * A unique identifier for a device that won't change in the official API, for example <code>HWE-WTR</code>.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #update()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/device_information#parameters">Official API documentation related to this method</a>
     *
     * @return the unique product type
     */
    public Optional<String> getProductType() {
        return productType;
    }

    /**
     * Returns a user-friendly name representation of the device. For example: <code>WaterMeter</code>.
     * It may change at any time, so you should not use this for device identification.
     * Instead, use {@link #getProductType()} if you want to know exactly what type of device something is.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #update()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/device_information#parameters">Official API documentation related to this method</a>
     *
     * @return product name
     */
    public Optional<String> getProductName() {
        return productName;
    }

    /**
     * Returns the device identifier used during validation of the SSL certificate, formatted as
     * {@code appliance/<product_type>/<serial>}. Example: {@code appliance/p1dongle/5c2fafaabbcc}.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #update()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/discovery#txt-records-1">Official API documentation related to this method</a>
     *
     * @return the device identifier used during validation of the SSL certificate
     */
    public Optional<String> getId() {
        if (id.isPresent()) {
            return id;
        } else if (productType.isPresent() && serial.isPresent()) {
            return Optional.of("appliance/" + productType.get() + "/" + serial.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the current API version. It should always be <code>v1</code>.
     * <p>
     * If the instance was returned by {@link HomeWizardDiscoverer}, this information is always available.
     * Otherwise, you have to call {@link #update()} first.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/device_information#parameters">Official API documentation related to this method</a>
     *
     * @return the current API version
     * @see #update()
     */
    public Optional<String> getApiVersion() {
        return apiVersion;
    }

    /**
     * Returns the version of the currently installed firmware.
     * <p>
     * In order to get this information, you must first call {@link #update()}.
     * <p>
     * <a href="https://api-documentation.homewizard.com/docs/v2/device_information#parameters">Official API documentation related to this method</a>
     *
     * @return the version of the currently installed firmware
     * @see #update()
     */
    public Optional<String> getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * Returns the authorizer associated with this device. With this, you are able to create a token, as well as set one
     * if you already have one.
     *
     * @return the authorizer associated with this device
     */
    public DeviceAuthorizer getAuthorizer() {
        return authorizer;
    }

    /**
     * Returns the system associated with this device. It can be used to get and set some system information,
     * like the state of the cloud communication and the uptime of the device.
     *
     * @return the system associated with this device
     */
    public DeviceSystem getSystem() {
        return system;
    }
}
