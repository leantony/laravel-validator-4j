package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Length rule.
 */
public final class LengthRule extends BaseRule {
    private final int minLength;
    private final int maxLength;

    /**
     * Instantiates a new Length rule.
     *
     * @param parameter the parameter
     */
    public LengthRule(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException("LengthRule parameter cannot be null or empty");
        }

        int[] bounds = parseParameter(parameter.trim());
        this.minLength = bounds[0];
        this.maxLength = bounds[1];

        validateBounds();
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) return false;
        String str = value.toString();
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        // Choose key based on whether it's an exact length or a range.
        String key = (minLength == maxLength) ? "length" : "lengthRange";
        return MessageRegistry.getResolver().resolve(key, field, minLength, maxLength);
    }

    private int[] parseParameter(String param) {
        String[] parts = param.split(",");
        if (parts.length > 2) {
            throw new IllegalArgumentException("Invalid LengthRule parameter format: " + param);
        }

        try {
            int[] bounds = new int[2];
            if (parts.length == 1) {
                bounds[0] = bounds[1] = Integer.parseInt(parts[0]);
            } else {
                bounds[0] = Integer.parseInt(parts[0].trim());
                bounds[1] = Integer.parseInt(parts[1].trim());
            }
            return bounds;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in LengthRule parameter: " + param, e);
        }
    }

    private void validateBounds() {
        if (minLength < 0 || maxLength < 0) {
            throw new IllegalArgumentException("Length values cannot be negative");
        }
        if (minLength > maxLength) {
            throw new IllegalArgumentException(
                    "Minimum length (" + minLength + ") cannot exceed maximum length (" + maxLength + ")"
            );
        }
    }
}