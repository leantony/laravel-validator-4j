package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SameOrEqualRuleTest {

    @Test
    void valuesAreEqual_ShouldPassValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("confirmPassword", "secret123");

        SameOrEqualRule rule = new SameOrEqualRule("confirmPassword");
        assertTrue(rule.validate("password", "secret123", data));
    }

    @Test
    void valuesAreNotEqual_ShouldFailValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("confirmPassword", "password123");

        SameOrEqualRule rule = new SameOrEqualRule("confirmPassword");
        assertFalse(rule.validate("password", "different123", data));
    }

    @Test
    void bothValuesAreNull_ShouldPassValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("otherField", null);

        SameOrEqualRule rule = new SameOrEqualRule("otherField");
        assertTrue(rule.validate("field", null, data));
    }

    @Test
    void oneValueIsNull_ShouldFailValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("confirmEmail", "user@example.com");

        SameOrEqualRule rule = new SameOrEqualRule("confirmEmail");
        assertFalse(rule.validate("email", null, data));
    }

    @Test
    void missingOtherField_ShouldFailValidation() {
        Map<String, Object> data = new HashMap<>();
        SameOrEqualRule rule = new SameOrEqualRule("nonExistentField");
        assertFalse(rule.validate("username", "admin", data));
    }

    @Test
    void differentTypes_ShouldFailValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("age", 30);

        SameOrEqualRule rule = new SameOrEqualRule("age");
        assertFalse(rule.validate("userAge", "30", data)); // String vs Integer
    }

    @Test
    void sameIntegerValues_ShouldPassValidation() {
        Map<String, Object> data = new HashMap<>();
        data.put("quantity", 5);

        SameOrEqualRule rule = new SameOrEqualRule("quantity");
        assertTrue(rule.validate("orderQuantity", 5, data));
    }

    @Test
    void errorMessage_ShouldBeCorrect() {
        Map<String, Object> data = new HashMap<>();
        SameOrEqualRule rule = new SameOrEqualRule("confirmPassword");

        String expectedMessage = "password must be equal to confirmPassword.";
        assertEquals(expectedMessage, rule.getErrorMessage("password", "abc123", data));
    }
}
