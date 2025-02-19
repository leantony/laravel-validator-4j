package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredWithoutRuleTest {

    @Test
    public void testAllOtherFieldsAbsent() {
        RequiredWithoutRule rule = new RequiredWithoutRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        // Both field1 and field2 absent: current becomes required.
        assertFalse(rule.validate("current", null, data));
        assertFalse(rule.validate("current", "", data));
        assertTrue(rule.validate("current", "value", data));
    }

    @Test
    public void testOneFieldPresent() {
        RequiredWithoutRule rule = new RequiredWithoutRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        // One field present: current is optional.
        assertTrue(rule.validate("current", null, data));
        assertTrue(rule.validate("current", "", data));
    }

    @Test
    public void testErrorMessage() {
        RequiredWithoutRule rule = new RequiredWithoutRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        String msg = rule.getErrorMessage("current", "", data);
        assertEquals("current is required when [field1, field2] are missing.", msg);
    }

    @Test
    public void testEmptyParameterThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredWithoutRule(null);
        });
        assertEquals("RequiredWithoutRule requires a comma-separated list of fields.", exception.getMessage());
    }
}
