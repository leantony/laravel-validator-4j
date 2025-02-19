package io.leantony.validator.rules;

import io.leantony.validator.Validator;
import io.leantony.validator.ValidatorBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnumRuleTest {

    @Test
    void testValidEnumValues() {
        EnumRule rule = new EnumRule(OrderStatus.class.getName());

        assertTrue(rule.validate("status", "PENDING", new HashMap<>()));
        assertTrue(rule.validate("status", "SHIPPED", new HashMap<>()));
        assertTrue(rule.validate("status", "DELIVERED", new HashMap<>()));
        assertTrue(rule.validate("status", "CANCELLED", new HashMap<>()));
    }

    @Test
    void testValidEnumValuesIgnoreCase() {
        EnumRule rule = new EnumRule(OrderStatus.class.getName());

        assertTrue(rule.validate("status", "pending", new HashMap<>()));
        assertTrue(rule.validate("status", "shipped", new HashMap<>()));
        assertTrue(rule.validate("status", "delivered", new HashMap<>()));
        assertTrue(rule.validate("status", "cancelled", new HashMap<>()));
    }

    @Test
    void testInvalidEnumValues() {
        EnumRule rule = new EnumRule(OrderStatus.class.getName());

        assertFalse(rule.validate("status", "IN_TRANSIT", new HashMap<>()));
        assertFalse(rule.validate("status", "123", new HashMap<>()));
        assertFalse(rule.validate("status", "", new HashMap<>()));
        assertFalse(rule.validate("status", null, new HashMap<>()));
    }

    @Test
    void testErrorMessage() {
        EnumRule rule = new EnumRule(OrderStatus.class.getName());
        String errorMessage = rule.getErrorMessage("status", "INVALID", new HashMap<>());

        assertEquals("status must be one of [PENDING, SHIPPED, DELIVERED, CANCELLED].", errorMessage);
    }

    @Test
    void testEnumValidationInValidator() {
        Map<String, Object> data = Map.of("status", "PENDING");
        Map<String, String> ruleDefinitions = Map.of("status", "enum:" + OrderStatus.class.getName());

        Validator validator = ValidatorBuilder.make(data, ruleDefinitions);
        assertTrue(validator.validate());
        assertEquals(0, validator.getErrors().size());
    }

    @Test
    void testValidatorFailsOnInvalidEnumValue() {
        Map<String, Object> data = Map.of("status", "INVALID");
        Map<String, String> ruleDefinitions = Map.of("status", "enum:" + OrderStatus.class.getName());

        Validator validator = ValidatorBuilder.make(data, ruleDefinitions);
        assertFalse(validator.validate());

        List<String> errors = validator.getErrors();
        assertEquals(1, errors.size());
        assertEquals("status must be one of [PENDING, SHIPPED, DELIVERED, CANCELLED].", errors.get(0));
    }

    @Test
    void testValidatorFailsOnNullEnumValue() {
        Map<String, Object> data = new HashMap<>(); // No status field provided
        Map<String, String> ruleDefinitions = Map.of("status", "enum:" + OrderStatus.class.getName());

        Validator validator = ValidatorBuilder.make(data, ruleDefinitions);
        assertFalse(validator.validate());

        List<String> errors = validator.getErrors();
        assertEquals(1, errors.size());
        assertEquals("status must be one of [PENDING, SHIPPED, DELIVERED, CANCELLED].", errors.get(0));
    }

    enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }
}
