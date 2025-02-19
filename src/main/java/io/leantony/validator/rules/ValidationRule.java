package io.leantony.validator.rules;

import java.util.Map;

/**
 * The interface Validation rule.
 */
public interface ValidationRule {
    /**
     * Validates the value for the given field.
     *
     * @param field the field name (used in error messages)
     * @param value the value to validate
     * @param data  the complete data map (for cross‑field validations)
     * @return true if valid; false otherwise.
     */
    boolean validate(String field, Object value, Map<String, Object> data);

    /**
     * Returns the error message for the field when validation fails.
     *
     * @param field the field
     * @param value the value
     * @param data  the data
     * @return the error message
     */
    String getErrorMessage(String field, Object value, Map<String, Object> data);
}
