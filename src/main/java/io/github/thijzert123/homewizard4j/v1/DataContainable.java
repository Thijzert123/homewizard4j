package io.github.thijzert123.homewizard4j.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thijzert123
 */
abstract class DataContainable {
    final ObjectMapper objectMapper;

    DataContainable() {
        objectMapper = new ObjectMapper();
        // Makes sure Optional is supported
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * Provides a String JSON representation for this class.
     * It does this by calling every {@code get*} and {@code is*} method and putting their name and return value in a {@link Map}.
     * This then gets serialized as a JSON.
     *
     * @return String JSON representation for this class
     */
    @Override
    public String toString() {
        final Map<String, Object> values = new HashMap<>();
        for (final Method method : getClass().getMethods()) {
            final String name = method.getName();
            if ((name.startsWith("get") || name.startsWith("is")) &&
                    method.getParameterCount() == 0 && !name.equals("getClass")) {
                try {
                    final Object returnValue = method.invoke(this);
                    values.put(name, returnValue);
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }

        try {
            return objectMapper.writeValueAsString(values);
        } catch (final JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }
    }
}
