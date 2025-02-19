package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmailRuleTest {

    @Test
    public void testValidEmail() {
        EmailRule rule = new EmailRule();
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("email", "test@example.com", data));
    }

    @Test
    public void testInvalidEmail() {
        EmailRule rule = new EmailRule();
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("email", "invalid-email", data));
        String msg = rule.getErrorMessage("email", "invalid-email", data);
        assertEquals("email must be a valid email address.", msg);
    }
}
