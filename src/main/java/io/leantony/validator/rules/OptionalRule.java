package io.leantony.validator.rules;

import java.util.Map;

/**
 * The type Optional rule.
 */
public class OptionalRule extends BaseRule {
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        return true;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return "";
    }
}
