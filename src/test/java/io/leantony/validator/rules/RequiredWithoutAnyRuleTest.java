package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredWithoutAnyRuleTest {

    @Test
    public void testAnyFieldAbsent() {
        RequiredWithoutAnyRule rule = new RequiredWithoutAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "");  // Considered absent.
        // At least one field absent: current becomes required.
        assertFalse(rule.validate("current", null, data));
        assertFalse(rule.validate("current", "", data));
        assertTrue(rule.validate("current", "value", data));
    }

    @Test
    public void testAllFieldsPresent() {
        RequiredWithoutAnyRule rule = new RequiredWithoutAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");
        // All fields present: current is optional.
        assertTrue(rule.validate("current", null, data));
        assertTrue(rule.validate("current", "", data));
    }

    @Test
    public void testErrorMessage() {
        RequiredWithoutAnyRule rule = new RequiredWithoutAnyRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        String msg = rule.getErrorMessage("current", "", data);
        assertEquals("current is required when either [field1, field2] is missing.", msg);
    }

    @Test
    public void testEmptyParameterThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredWithoutAnyRule(" ");
        });
        assertEquals("RequiredWithoutAnyRule requires a comma-separated list of fields.", exception.getMessage());
    }
}
