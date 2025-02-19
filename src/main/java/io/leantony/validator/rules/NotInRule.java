package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Not in rule.
 */
public class NotInRule extends BaseRule {
    private final List<String> disallowedValues;

    /**
     * Instantiates a new Not in rule.
     *
     * @param parameter the parameter
     */
    public NotInRule(String parameter) {
        disallowedValues = Arrays.asList(parameter.split("\\s*,\\s*"));
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) {
            // Treat null as valid since there's nothing to check.
            return true;
        }
        return !disallowedValues.contains(value.toString());
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        String disallowed = String.join(", ", disallowedValues);
        return MessageRegistry.getResolver().resolve("notIn", field, disallowed);
    }
}
