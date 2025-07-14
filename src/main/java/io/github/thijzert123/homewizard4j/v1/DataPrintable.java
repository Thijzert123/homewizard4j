package io.github.thijzert123.homewizard4j.v1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Thijzert123
 */
abstract class DataPrintable {
    /**
     * Provides a human-readable {@link String} useful for debugging by calling all get* and is* methods.
     *
     * @return a human-readable representation of this class
     */
    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Method method : getClass().getMethods()) {
            final String name = method.getName();
            if ((name.startsWith("get") || name.startsWith("is")) &&
                    method.getParameterCount() == 0 && !name.equals("getClass")) {
                stringBuilder.append(name).append(" = ");
                String returnValue;
                try {
                    returnValue = method.invoke(this).toString();
                } catch (final IllegalAccessException | InvocationTargetException exception) {
                    returnValue = exception.getMessage();
                }
                stringBuilder.append(returnValue).append(", ");
            }
        }
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }
}
