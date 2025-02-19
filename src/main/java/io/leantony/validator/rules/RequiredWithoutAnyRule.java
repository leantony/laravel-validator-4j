package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Required without any rule.
 */
public class RequiredWithoutAnyRule extends BaseRule {
    private final List<String> otherFields;

    /**
     * Instantiates a new Required without any rule.
     *
     * @param parameter the parameter
     */
    public RequiredWithoutAnyRule(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException("RequiredWithoutAnyRule requires a comma-separated list of fields.");
        }
        otherFields = Arrays.stream(parameter.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        // Check if ANY of the specified fields are absent or empty.
        boolean anyAbsent = otherFields.stream().anyMatch(f -> {
            Object v = data.get(f);
            return v == null || v.toString().trim().isEmpty();
        });
        if (anyAbsent) {
            return value != null && !value.toString().trim().isEmpty();
        }
        return true;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("requiredWithoutAny", field, otherFields);
    }
}
