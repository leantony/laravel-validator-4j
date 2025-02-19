package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Alpha numeric rule.
 */
public class AlphaNumericRule extends BaseRule {

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) return false;
        String str = value.toString().trim();
        if (str.isEmpty()) return false;
        // Validate that the string contains only letters and digits.
        return str.matches("^[A-Za-z0-9]+$");
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("alphanumeric", field);
    }
}
