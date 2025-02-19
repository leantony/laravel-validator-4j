package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InRuleTest {

    @Test
    public void testValueInAllowedSet() {
        InRule rule = new InRule("apple,banana,orange");
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("fruit", "banana", data));
    }

    @Test
    public void testValueNotInAllowedSet() {
        InRule rule = new InRule("apple,banana,orange");
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("fruit", "pear", data));
    }

    @Test
    public void testErrorMessage() {
        InRule rule = new InRule("apple,banana,orange");
        Map<String, Object> data = new HashMap<>();
        String expected = "fruit must be one of [apple, banana, orange].";
        assertEquals(expected, rule.getErrorMessage("fruit", "pear", data));
    }
}
