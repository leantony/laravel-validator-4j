package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyRuleTest {

    @Test
    public void testValidCurrencyCode() {
        CurrencyRule rule = new CurrencyRule();
        Map<String, Object> data = new HashMap<>();
        // Test with uppercase and lowercase valid codes.
        assertTrue(rule.validate("currency", "USD", data));
        assertTrue(rule.validate("currency", "eur", data));
    }

    @Test
    public void testInvalidCurrencyCode() {
        CurrencyRule rule = new CurrencyRule();
        Map<String, Object> data = new HashMap<>();
        // "XYZ" is not a valid ISO currency code.
        assertFalse(rule.validate("currency", "XYZ", data));
        // Non-string values should fail.
        assertFalse(rule.validate("currency", 123, data));
    }

    @Test
    public void testErrorMessage() {
        CurrencyRule rule = new CurrencyRule();
        Map<String, Object> data = new HashMap<>();
        String msg = rule.getErrorMessage("currency", "XYZ", data);
        assertEquals("currency must be a valid ISO currency code.", msg);
    }
}