package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Required if rule.
 */
public class RequiredIfRule extends BaseRule {
    private final String key;
    private final String expectedValue;

    /**
     * Instantiates a new Required if rule.
     *
     * @param parameter the parameter
     */
    public RequiredIfRule(String parameter) {
        if (parameter == null || !parameter.contains(",")) {
            throw new IllegalArgumentException("RequiredIfRule requires parameters in the format 'key,value'.");
        }
        String[] parts = parameter.split(",", 2);
        key = parts[0].trim();
        expectedValue = parts[1].trim();
        if (key.isEmpty() || expectedValue.isEmpty()) {
            throw new IllegalArgumentException("Both key and expected value must be non-empty in RequiredIfRule.");
        }
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        Object condition = data.get(key);
        if (condition != null && condition.toString().trim().equalsIgnoreCase(expectedValue)) {
            return value != null && !value.toString().trim().isEmpty();
        }
        return true;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("requiredIf", field, key, expectedValue);
    }
}
