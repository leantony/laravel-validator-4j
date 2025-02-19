package io.leantony.validator.rules;

import java.util.Map;

/**
 * The type Required rule.
 */
public class RequiredRule extends BaseRule {
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        return value != null && !value.toString().trim().isEmpty();
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return field + " is required.";
    }
}
