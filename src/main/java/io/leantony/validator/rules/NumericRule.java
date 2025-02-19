package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;

/**
 * The type Numeric rule.
 */
public class NumericRule extends BaseRule {
    private final Integer digitCount; // If null, no digit count check is performed

    /**
     * Instantiates a new Numeric rule.
     */
    public NumericRule() {
        this.digitCount = null;
    }

    /**
     * Instantiates a new Numeric rule.
     *
     * @param digitCount the digit count
     */
    public NumericRule(int digitCount) {
        this.digitCount = digitCount;
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) return false;
        String strValue = value.toString().trim();
        try {
            // Check that the value can be parsed as a number
            Double.parseDouble(strValue);
        } catch (NumberFormatException e) {
            return false;
        }
        if (digitCount != null) {
            // Remove any non-digit characters (e.g., decimal points, minus signs) to count only the digits.
            @SuppressWarnings("RegExpSimplifiable")
            String digits = strValue.replaceAll("[^\\d]", "");
            return digits.length() == digitCount;
        }
        return true;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        if (digitCount != null) {
            return MessageRegistry.getResolver().resolve("numeric.exact", field, digitCount);
        }
        return MessageRegistry.getResolver().resolve("numeric", field);
    }
}
