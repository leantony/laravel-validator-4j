package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DifferentRuleTest {

    @Test
    public void testValuesAreDifferent() {
        DifferentRule rule = new DifferentRule("oldEmail");
        Map<String, Object> data = new HashMap<>();
        data.put("oldEmail", "old@example.com");

        assertTrue(rule.validate("newEmail", "new@example.com", data));
    }

    @Test
    public void testValuesAreNotDifferent() {
        DifferentRule rule = new DifferentRule("oldEmail");
        Map<String, Object> data = new HashMap<>();
        data.put("oldEmail", "old@example.com");

        assertFalse(rule.validate("newEmail", "old@example.com", data));
    }

    @Test
    public void testDifferentErrorMessage() {
        DifferentRule rule = new DifferentRule("oldEmail");
        Map<String, Object> data = new HashMap<>();
        data.put("oldEmail", "old@example.com");

        String msg = rule.getErrorMessage("newEmail", "old@example.com", data);
        assertEquals("newEmail must be different from oldEmail.", msg);
    }
}
