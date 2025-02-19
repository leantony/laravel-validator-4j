package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Alpha rule.
 */
public class AlphaRule extends BaseRule {
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) {
            return false;
        }
        return ((String) value).matches("^[a-zA-Z]+$");
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("alpha", field);
    }
}
