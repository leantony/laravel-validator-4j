package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AlphaRuleTest {

    @Test
    public void testAlphaValid() {
        AlphaRule rule = new AlphaRule();
        Map<String, Object> data = new HashMap<>();
        // "JohnDoe" contains only alphabetic characters.
        assertTrue(rule.validate("name", "JohnDoe", data));
    }

    @Test
    public void testAlphaInvalid() {
        AlphaRule rule = new AlphaRule();
        Map<String, Object> data = new HashMap<>();
        // "John123" contains digits, so it should fail.
        assertFalse(rule.validate("name", "John123", data));
        String msg = rule.getErrorMessage("name", "John123", data);
        assertEquals("name must contain only alphabetic characters.", msg);
    }
}
