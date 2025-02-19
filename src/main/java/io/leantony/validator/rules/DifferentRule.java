package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Different rule.
 */
public class DifferentRule extends BaseRule {
    private final String otherField;

    /**
     * Instantiates a new Different rule.
     *
     * @param otherField the other field
     */
    public DifferentRule(String otherField) {
        this.otherField = otherField;
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        Object other = data.get(otherField);
        if (value == null) {
            // If the value is null, we consider it different if the other field is not null.
            return other != null;
        }
        return !value.equals(other);
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("different", field, otherField);
    }
}
