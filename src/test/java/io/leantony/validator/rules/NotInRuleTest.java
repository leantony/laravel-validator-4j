package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NotInRuleTest {

    @Test
    public void testValueNotInDisallowedSet() {
        NotInRule rule = new NotInRule("red, blue, green");
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("color", "yellow", data));
    }

    @Test
    public void testValueInDisallowedSet() {
        NotInRule rule = new NotInRule("red, blue, green");
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("color", "red", data));
    }

    @Test
    public void testNullValuePasses() {
        NotInRule rule = new NotInRule("red, blue, green");
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("color", null, data));
    }

    @Test
    public void testErrorMessage() {
        NotInRule rule = new NotInRule("red, blue, green");
        Map<String, Object> data = new HashMap<>();
        String errorMessage = rule.getErrorMessage("color", "red", data);
        assertEquals("color must not be one of [red, blue, green].", errorMessage);
    }
}
