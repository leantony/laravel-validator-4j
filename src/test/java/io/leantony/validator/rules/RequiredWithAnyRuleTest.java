package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredWithAnyRuleTest {

    @Test
    public void testAnyFieldPresent() {
        RequiredWithAnyRule rule = new RequiredWithAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        // At least one present: current becomes required.
        assertFalse(rule.validate("current", null, data));
        assertFalse(rule.validate("current", "", data));
        assertTrue(rule.validate("current", "value", data));
    }

    @Test
    public void testNoFieldPresent() {
        RequiredWithAnyRule rule = new RequiredWithAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        // Neither field is present: current is optional.
        assertTrue(rule.validate("current", null, data));
        assertTrue(rule.validate("current", "", data));
    }

    @Test
    public void testErrorMessage() {
        RequiredWithAnyRule rule = new RequiredWithAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field2", "value2");
        String msg = rule.getErrorMessage("current", "", data);
        assertEquals("current is required when at least one of [field1, field2] is present.", msg);
    }

    @Test
    public void testEmptyParameterThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredWithAnyRule("   ");
        });
        assertEquals("RequiredWithAnyRule requires a comma-separated list of fields.", exception.getMessage());
    }
}
