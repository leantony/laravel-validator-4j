package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Map;

/**
 * The type Min rule.
 */
public class MinRule extends BaseRule {
    private final BigDecimal min;
    private final boolean isInteger;

    /**
     * Instantiates a new Min rule.
     *
     * @param min the min
     */
    public MinRule(double min) {
        this.min = BigDecimal.valueOf(min);
        this.isInteger = isWholeNumber(this.min);
    }

    private static boolean isWholeNumber(BigDecimal value) {
        return value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0;
    }

    private static int sizeOf(Iterable<?> iterable) {
        int count = 0;
        for (Object obj : iterable) count++;
        return count;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) return false;

        if (value instanceof Number) {
            return validateNumber((Number) value);
        } else if (value instanceof CharSequence) {
            return validateLength(value.toString());
        } else if (value instanceof Map) {
            return validateSize(((Map<?, ?>) value).size());
        } else if (value instanceof Iterable) {
            return validateSize(sizeOf((Iterable<?>) value));
        } else if (value.getClass().isArray()) {
            return validateSize(Array.getLength(value));
        }

        return false;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        String minString = isInteger ?
                String.valueOf(min.intValueExact()) :
                min.stripTrailingZeros().toPlainString();

        if (value instanceof Number) {
            return MessageRegistry.getResolver().resolve("min.number", field, minString);
        } else if (value instanceof CharSequence) {
            return MessageRegistry.getResolver().resolve("min.length", field, minString);
        } else if (value instanceof Map) {
            return MessageRegistry.getResolver().resolve("min.map", field, minString);
        } else if (value instanceof Iterable || (value != null && value.getClass().isArray())) {
            return MessageRegistry.getResolver().resolve("min.collection", field, minString);
        }

        return MessageRegistry.getResolver().resolve("min", field, minString);
    }

    // Rest of the implementation remains the same
    private boolean validateNumber(Number value) {
        return new BigDecimal(value.toString()).compareTo(min) >= 0;
    }

    private boolean validateLength(String value) {
        return isInteger && value.length() >= min.intValueExact();
    }

    private boolean validateSize(int size) {
        return isInteger && size >= min.intValueExact();
    }
}