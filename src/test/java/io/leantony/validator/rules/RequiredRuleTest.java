package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RequiredRuleTest {

    @Test
    public void testNonEmpty() {
        RequiredRule rule = new RequiredRule();
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("field", "some value", data));
    }

    @Test
    public void testEmpty() {
        RequiredRule rule = new RequiredRule();
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("field", "", data));
        String msg = rule.getErrorMessage("field", "", data);
        assertEquals("field is required.", msg);
    }

    @Test
    public void testNull() {
        RequiredRule rule = new RequiredRule();
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("field", null, data));
        String msg = rule.getErrorMessage("field", null, data);
        assertEquals("field is required.", msg);
    }
}
