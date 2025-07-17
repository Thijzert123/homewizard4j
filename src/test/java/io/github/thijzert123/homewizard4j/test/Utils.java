package io.github.thijzert123.homewizard4j.test;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * @author Thijzert123
 */
public class Utils {
    public static HttpServer initializeServer(final int port, final String resourceDir) throws IOException {
        final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(null);

        server.createContext("/api", new TestHttpHandler(getResourceAsString(resourceDir + "/deviceInfo.json")));
        server.createContext("/test/data", new TestHttpHandler(getResourceAsString(resourceDir + "/measurements.json")));
        server.createContext("/test/system", new TestHttpHandler(getResourceAsString(resourceDir + "/systemConfiguration.json")));

        return server;
    }

    public static String getResourceAsString(final String file) throws IOException {
        final InputStream responseStream = Utils.class.getClassLoader().getResourceAsStream(file);
        return new String(responseStream.readAllBytes());
    }
}
