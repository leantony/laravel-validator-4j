package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredWithRuleTest {

    @Test
    public void testAllOtherFieldsPresent() {
        RequiredWithRule rule = new RequiredWithRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");
        // When both field1 and field2 are present, the current field is required.
        assertFalse(rule.validate("current", null, data));
        assertFalse(rule.validate("current", "", data));
        assertTrue(rule.validate("current", "value", data));
    }

    @Test
    public void testNotAllOtherFieldsPresent() {
        RequiredWithRule rule = new RequiredWithRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        // field2 is missing, so current is not enforced.
        assertTrue(rule.validate("current", null, data));
        assertTrue(rule.validate("current", "", data));
        assertTrue(rule.validate("current", "value", data));
    }

    @Test
    public void testErrorMessage() {
        RequiredWithRule rule = new RequiredWithRule("field1, field2");
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "value1");
        data.put("field2", "value2");
        String msg = rule.getErrorMessage("current", "", data);
        assertEquals("current is required when [field1, field2] are present.", msg);
    }

    @Test
    public void testEmptyParameterThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredWithRule("");
        });
        assertEquals("RequiredWithRule requires a comma-separated list of fields.", exception.getMessage());
    }
}
