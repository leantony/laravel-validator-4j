package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Same or equal rule.
 */
public class SameOrEqualRule extends BaseRule {
    private final String otherField;

    /**
     * Instantiates a new Same or equal rule.
     *
     * @param otherField the other field
     */
    public SameOrEqualRule(String otherField) {
        this.otherField = otherField;
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        Object other = data.get(otherField);
        if (value == null) {
            return other == null;
        }
        return value.equals(other);
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("sameOrEqual", field, otherField);
    }
}