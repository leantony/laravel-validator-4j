package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Required without rule.
 */
public class RequiredWithoutRule extends BaseRule {
    private final List<String> otherFields;

    /**
     * Instantiates a new Required without rule.
     *
     * @param parameter the parameter
     */
    public RequiredWithoutRule(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            throw new IllegalArgumentException("RequiredWithoutRule requires a comma-separated list of fields.");
        }
        otherFields = Arrays.stream(parameter.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        // Check if ALL specified fields are absent or empty.
        boolean allAbsent = otherFields.stream().allMatch(f -> {
            Object v = data.get(f);
            return v == null || v.toString().trim().isEmpty();
        });
        if (allAbsent) {
            return value != null && !value.toString().trim().isEmpty();
        }
        return true;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("requiredWithout", field, otherFields);
    }
}
