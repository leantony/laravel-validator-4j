package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanRuleTest {

    @Test
    public void testValidBooleanValues() {
        BooleanRule rule = new BooleanRule();
        Map<String, Object> data = new HashMap<>();

        // Test with Boolean types.
        assertTrue(rule.validate("active", true, data));
        assertTrue(rule.validate("active", false, data));

        // Test with valid string representations.
        assertTrue(rule.validate("active", "true", data));
        assertTrue(rule.validate("active", "False", data));

        // Test with numeric values (0 and 1)
        assertTrue(rule.validate("active", 0, data));
        assertTrue(rule.validate("active", 1, data));

        // Test with string "0" and "1"
        assertTrue(rule.validate("active", "0", data));
        assertTrue(rule.validate("active", "1", data));
    }

    @Test
    public void testInvalidBooleanValues() {
        BooleanRule rule = new BooleanRule();
        Map<String, Object> data = new HashMap<>();

        // Should fail for non-boolean strings and other types.
        assertFalse(rule.validate("active", "yes", data));
        assertFalse(rule.validate("active", 2, data));
        assertFalse(rule.validate("active", "2", data));
        assertFalse(rule.validate("active", "abc", data));
        assertFalse(rule.validate("active", new Object(), data));
    }

    @Test
    public void testBooleanErrorMessage() {
        BooleanRule rule = new BooleanRule();
        Map<String, Object> data = new HashMap<>();

        String msg = rule.getErrorMessage("active", "yes", data);
        assertEquals("active must be a boolean value.", msg);
    }
}
