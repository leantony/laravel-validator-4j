package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Boolean rule.
 */
public class BooleanRule extends BaseRule {
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return true;
        }
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            // Accept 0 and 1 as valid boolean values.
            return d == 0 || d == 1;
        }
        if (value instanceof String) {
            String s = ((String) value).trim().toLowerCase();
            // Accept "true", "false", "0", and "1"
            return s.equals("true") || s.equals("false") || s.equals("0") || s.equals("1");
        }
        return false;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("boolean", field);
    }
}
