package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredIfRuleTest {

    @Test
    public void testConditionMet() {
        RequiredIfRule rule = new RequiredIfRule("status, active");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "active");
        // Condition is met: current field must be provided.
        assertFalse(rule.validate("email", null, data));
        assertFalse(rule.validate("email", "", data));
        assertTrue(rule.validate("email", "test@example.com", data));
    }

    @Test
    public void testConditionNotMet() {
        RequiredIfRule rule = new RequiredIfRule("status, active");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "inactive");
        // Condition not met: current field is optional.
        assertTrue(rule.validate("email", null, data));
        assertTrue(rule.validate("email", "", data));
    }

    @Test
    public void testErrorMessage() {
        RequiredIfRule rule = new RequiredIfRule("status, active");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "active");
        String msg = rule.getErrorMessage("email", "", data);
        assertEquals("email is required because status is active.", msg);
    }

    @Test
    public void testMissingCommaThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredIfRule("status");
        });
        assertEquals("RequiredIfRule requires parameters in the format 'key,value'.", exception.getMessage());
    }

    @Test
    public void testEmptyParametersThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RequiredIfRule(" , ");
        });
        assertEquals("Both key and expected value must be non-empty in RequiredIfRule.", exception.getMessage());
    }
}
