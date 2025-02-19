package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumericRuleTest {

    @Test
    void shouldPassForNumbers() {
        NumericRule rule = new NumericRule();
        assertThat(rule.validate("amount", 123, null)).isTrue();
        assertThat(rule.validate("amount", 12.5, null)).isTrue();
        assertThat(rule.validate("amount", "456", null)).isTrue();
    }

    @Test
    void shouldFailForNonNumericValues() {
        NumericRule rule = new NumericRule();
        assertThat(rule.validate("amount", "abc", null)).isFalse();
        assertThat(rule.validate("amount", "12abc", null)).isFalse();
        assertThat(rule.validate("amount", true, null)).isFalse();
    }

    @Test
    void shouldReturnCorrectErrorMessage() {
        NumericRule rule = new NumericRule();
        assertThat(rule.getErrorMessage("amount", "abc", null))
                .isEqualTo("amount must be numeric.");
    }

    @Test
    public void testNumericRuleWithoutDigitCount_validNumber() {
        NumericRule rule = new NumericRule();
        // "12345" is numeric.
        boolean valid = rule.validate("testField", "12345", new HashMap<>());
        assertTrue(valid, "Expected numeric value '12345' to pass without digit count check.");
    }

    @Test
    public void testNumericRuleWithoutDigitCount_invalidNumber() {
        NumericRule rule = new NumericRule();
        // "abc" is not numeric.
        boolean valid = rule.validate("testField", "abc", new HashMap<>());
        assertFalse(valid, "Expected non-numeric value 'abc' to fail without digit count check.");
    }

    @Test
    public void testNumericRuleWithDigitCount_validNumber() {
        // Rule expecting exactly 5 digits.
        NumericRule rule = new NumericRule(5);
        // "12345" has exactly 5 digits.
        boolean valid = rule.validate("testField", "12345", new HashMap<>());
        assertTrue(valid, "Expected numeric value '12345' to pass with digit count of 5.");
    }

    @Test
    public void testNumericRuleWithDigitCount_invalidNumber() {
        // Rule expecting exactly 5 digits.
        NumericRule rule = new NumericRule(5);
        // "1234" has 4 digits, and "123456" has 6 digits.
        boolean valid1 = rule.validate("testField", "1234", new HashMap<>());
        boolean valid2 = rule.validate("testField", "123456", new HashMap<>());
        assertFalse(valid1, "Expected numeric value '1234' to fail with digit count of 5.");
        assertFalse(valid2, "Expected numeric value '123456' to fail with digit count of 5.");
    }

    @Test
    public void testNumericRuleWithDecimalAndSign() {
        // Rule expecting exactly 5 digits.
        NumericRule rule = new NumericRule(5);
        // "-123.45" becomes "12345" after removing non-digits.
        boolean valid = rule.validate("testField", "-123.45", new HashMap<>());
        assertTrue(valid, "Expected numeric value '-123.45' to pass with digit count of 5.");
    }
}
