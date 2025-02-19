package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type In rule.
 */
public class InRule extends BaseRule {
    private final List<String> allowedValues;

    /**
     * Instantiates a new In rule.
     *
     * @param parameter the parameter
     */
    public InRule(String parameter) {
        // Split the parameter on commas (ignoring spaces around commas)
        allowedValues = Arrays.asList(parameter.split("\\s*,\\s*"));
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) {
            return false;
        }
        return allowedValues.contains(value.toString());
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        // Join allowed values as a comma-separated string.
        String allowed = String.join(", ", allowedValues);
        return MessageRegistry.getResolver().resolve("in", field, allowed);
    }

}
