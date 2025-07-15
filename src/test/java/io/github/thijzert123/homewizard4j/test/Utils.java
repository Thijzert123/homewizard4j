package io.github.thijzert123.homewizard4j.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Thijzert123
 */
public class Utils {
    public static String getResourceAsString(final String file) throws IOException {
        final InputStream responseStream = Utils.class.getClassLoader().getResourceAsStream(file);
        return new String(responseStream.readAllBytes());
    }
}
