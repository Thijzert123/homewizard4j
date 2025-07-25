/**
 * This module provides everything you need for {@code homewizard4j}. Please be sure to take a look at
 * <a href="https://thijzert123.github.io/homewizard4j">the full documentation</a> and the
 * <a href="https://github.com/Thijzert123/homewizard4j">GitHub repository</a>.
 *
 * @author Thijzert123
 * @since 2.1.0
 */
module io.github.thijzert123.homewizard4j {
    requires static java.net.http;
    requires static javax.jmdns;
    requires static jdk.httpserver;
    requires static com.fasterxml.jackson.annotation;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    opens io.github.thijzert123.homewizard4j.v1 to com.fasterxml.jackson.databind;

    exports io.github.thijzert123.homewizard4j.v1;
    exports io.github.thijzert123.homewizard4j.v2;
}