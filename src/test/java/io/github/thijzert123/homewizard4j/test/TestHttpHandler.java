package io.github.thijzert123.homewizard4j.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Thijzert123
 */
public class TestHttpHandler implements HttpHandler {
    private final String response;

    public TestHttpHandler(final String response) {
        this.response = response;
    }

    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, response.length());
        final OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
