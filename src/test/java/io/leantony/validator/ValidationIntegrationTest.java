package io.leantony.validator;

import io.leantony.validator.lang.MessageRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class ValidationIntegrationTest {

    private static final Map<String, String> rules = new HashMap<>();

    @BeforeAll
    static void setupRules() {
        rules.put("name", "required|alpha");
        rules.put("email", "required|email");
        rules.put("age", "required|numeric|min:18|max:65");
        rules.put("password", "required|length:8");
        rules.put("confirmPassword", "required|sameOrEqual:password");
        rules.put("birthDate", "required|date:yyyy-MM-dd|beforeOrEqual:1998-05-15");
        rules.put("countryCode", "required|country");
        rules.put("currency", "required|currency");
        rules.put("website", "required|url");
        rules.put("tags", "required|max:5");
        rules.put("status", "required|in:active,inactive,suspended");
        rules.put("role", "required|notIn:guest,banned");
        rules.put("contactNumber", "required|phoneno:KE,US");
        rules.put("isActive", "boolean");
        rules.put("roleType", "enum:io.cellulant.vas.validator.ValidationIntegrationTest$" + RoleEnum.class.getSimpleName());

        // Nested validation
        rules.put("addresses[0].street", "required");
        rules.put("addresses[0].city", "required|alpha");
        rules.put("addresses[*].postalCode", "required|numeric");
        rules.put("settings.preferences.theme", "required|in:light,dark");
        rules.put("users[*].email", "required|email");
    }

    @Test
    void testValidData() {
        Map<String, Object> validData = new HashMap<>();
        validData.put("name", "JohnDoe");
        validData.put("email", "johndoe@example.com");
        validData.put("age", 30);
        validData.put("password", "StrongPass8");
        validData.put("confirmPassword", "StrongPass8");
        validData.put("birthDate", "1997-05-15");
        validData.put("countryCode", "US");
        validData.put("currency", "USD");
        validData.put("website", "https://example.com");
        validData.put("tags", List.of("tech", "news", "finance"));
        validData.put("status", "active");
        validData.put("role", "admin");
        validData.put("contactNumber", "+254712345678");
        validData.put("isActive", true);
        validData.put("roleType", "ADMIN");

        // Nested Objects
        validData.put("addresses", List.of(Map.of("street", "123 Main St", "city", "NewYork", "postalCode", "10001")));
        validData.put("settings", Map.of("preferences", Map.of("theme", "dark")));
        validData.put("users", List.of(Map.of("email", "user1@example.com")));

        List<String> errors = RequestValidator.validate(validData, rules);
        Assertions.assertTrue(errors.isEmpty(), "Expected no validation errors, but got: " + errors);
    }

    @Test
    void testInvalidData() {
        Map<String, Object> invalidData = new HashMap<>();
        invalidData.put("name", "John123");  // Fails alpha rule
        invalidData.put("email", "invalid-email");  // Fails email rule
        invalidData.put("age", 17);  // Fails min:18 rule
        invalidData.put("password", "short");  // Fails length:8 rule
        invalidData.put("confirmPassword", "mismatch");  // Fails sameOrEqual rule
        invalidData.put("birthDate", "2025-01-01");  // Fails beforeOrEqual:today
        invalidData.put("countryCode", "XYZ");  // Invalid country
        invalidData.put("currency", "XYZ");  // Invalid currency
        invalidData.put("website", "invalid-url");  // Fails URL validation
        invalidData.put("tags", List.of("one", "two", "three", "four", "five", "six"));  // Exceeds maxSize:5
        invalidData.put("status", "pending");  // Not in allowed in:active,inactive,suspended
        invalidData.put("role", "guest");  // In notIn:guest,banned
        invalidData.put("contactNumber", "invalidNumber");  // Fails phone validation
        invalidData.put("isActive", "maybe");  // Fails boolean check
        invalidData.put("roleType", "UNKNOWN");  // Invalid enum value

        // Nested Objects
        invalidData.put("addresses", List.of(Map.of("street", "", "city", "123City", "postalCode", "ABC")));  // Multiple failures
        invalidData.put("settings", Map.of("preferences", Map.of("theme", "blue")));  // Not in:light,dark
        invalidData.put("users", List.of(Map.of("email", "invalid-email")));  // Invalid email in nested object

        List<String> errors = RequestValidator.validate(invalidData, rules);
        Assertions.assertFalse(errors.isEmpty(), "Expected validation errors, but got none.");
        Assertions.assertEquals(20, errors.size(), "Expected 14 validation errors, but got " + errors.size());
    }

    @Test
    void testMaxNestingDepthEnforcement() {
        Validator.setMaxNestingDepth(4); // Enforce a lower nesting depth

        Map<String, String> deepRules = new HashMap<>();
        deepRules.put("a.b.c.d.e", "required");  // Exceeds depth limit

        Map<String, Object> data = new HashMap<>();
        data.put("a", Map.of("b", Map.of("c", Map.of("d", Map.of("e", "value"))))); // Too deep

        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> RequestValidator.validate(data, deepRules)
        );
        Assertions.assertTrue(exception.getMessage().contains("exceeds maximum allowed nesting depth"));
    }

    @Test
    void testOverrideValidationMessages() {
        Validator.setMaxNestingDepth(5);
        MessageRegistry.overrideMessage(Locale.ENGLISH, "required", "{0} is absolutely necessary!");
        MessageRegistry.overrideMessage(Locale.ENGLISH, "min", "{0} must be at least {1}!");

        Map<String, Object> testData = new HashMap<>();
        testData.put("name", "");  // Triggers required rule
        testData.put("age", 16);  // Triggers min:18 rule

        List<String> errors = RequestValidator.validate(testData, rules);
        Assertions.assertTrue(errors.contains("name is absolutely necessary!"));
        Assertions.assertTrue(errors.contains("age must be at least 18!"));
    }

    public enum RoleEnum {
        ADMIN,
        USER,
        MODERATOR,
        SUPER_ADMIN;
    }
}
