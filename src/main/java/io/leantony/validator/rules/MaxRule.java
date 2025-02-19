package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Map;

/**
 * The type Max rule.
 */
public class MaxRule extends BaseRule {
    private final BigDecimal max;
    private final boolean isInteger;

    /**
     * Instantiates a new Max rule.
     *
     * @param max the max
     */
    public MaxRule(double max) {
        this.max = BigDecimal.valueOf(max);
        this.isInteger = isWholeNumber(this.max);
    }

    // Helper methods
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
        String maxString = isInteger ?
                String.valueOf(max.intValueExact()) :
                max.stripTrailingZeros().toPlainString();

        if (value instanceof Number) {
            return MessageRegistry.getResolver().resolve("max.number", field, maxString);
        } else if (value instanceof CharSequence) {
            return MessageRegistry.getResolver().resolve("max.length", field, maxString);
        } else if (value instanceof Map) {
            return MessageRegistry.getResolver().resolve("max.map", field, maxString);
        } else if (value instanceof Iterable || (value != null && value.getClass().isArray())) {
            return MessageRegistry.getResolver().resolve("max.collection", field, maxString);
        }

        return MessageRegistry.getResolver().resolve("max", field, maxString);
    }

    // Validation implementations
    private boolean validateNumber(Number value) {
        return new BigDecimal(value.toString()).compareTo(max) <= 0;
    }

    private boolean validateLength(String value) {
        return isInteger && value.length() <= max.intValueExact();
    }

    private boolean validateSize(int size) {
        return isInteger && size <= max.intValueExact();
    }
}