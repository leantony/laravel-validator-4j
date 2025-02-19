package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneNoRuleTest {

    @Test
    public void testValidPhoneWithDefaultRegion() {
        PhoneNoRule rule = new PhoneNoRule(null);
        Map<String, Object> data = new HashMap<>();
        // Example US number: try "6502530000" (assuming default region "US").
        assertTrue(rule.validate("phone", "6502530000", data) ||
                rule.validate("phone", "+1 650-253-0000", data));
    }

    @Test
    public void testInvalidPhoneWithDefaultRegion() {
        PhoneNoRule rule = new PhoneNoRule(null);
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("phone", "12345", data));
    }

    @Test
    public void testValidPhoneWithAllowedRegions() {
        PhoneNoRule rule = new PhoneNoRule("KE,UG,ZM");
        Map<String, Object> data = new HashMap<>();
        // Example Kenyan number: using libphonenumber examples, "+254712345678" should be valid.
        assertTrue(rule.validate("phone", "+254712345678", data));
    }

    @Test
    public void testInvalidPhoneWithAllowedRegions() {
        PhoneNoRule rule = new PhoneNoRule("KE,UG,ZM");
        Map<String, Object> data = new HashMap<>();
        // A US number likely won't be valid for these regions.
        assertFalse(rule.validate("phone", "6502530000", data));
    }

    @Test
    public void testValidPhoneUsingCountryField() {
        PhoneNoRule rule = new PhoneNoRule("countryCode");
        Map<String, Object> data = new HashMap<>();
        data.put("countryCode", "KE"); // Use Kenya
        // Assume "+254712345678" is valid for Kenya.
        assertTrue(rule.validate("phone", "+254712345678", data));
    }

    @Test
    public void testInvalidPhoneUsingCountryField() {
        PhoneNoRule rule = new PhoneNoRule("countryCode");
        Map<String, Object> data = new HashMap<>();
        data.put("countryCode", "KE"); // Kenya
        // A US number will not be valid for Kenya.
        assertFalse(rule.validate("phone", "6502530000", data));
    }

    @Test
    public void testValidPhone_WithLiteralCountryCode() {
        // 'KEN' is provided as a literal country code.
        // The modified PhoneNoRule should detect that "KEN" is a known country code and use it as the allowed region.
        PhoneNoRule rule = new PhoneNoRule("KEN");
        Map<String, Object> data = new HashMap<>();

        // Use a valid Kenyan phone number (example: +254712345678)
        boolean result = rule.validate("phone", "+254712345678", data);
        // Expected: Validation passes.
        assertTrue(result, "Expected valid phone number with literal country code 'KEN' to pass.");
    }

    @Test
    public void testInvalidPhone_WithLiteralCountryCode() {
        // 'KEN' is provided as a literal country code.
        PhoneNoRule rule = new PhoneNoRule("KEN");
        Map<String, Object> data = new HashMap<>();

        // Use an invalid phone number (e.g., a too-short number)
        boolean result = rule.validate("phone", "12345", data);
        // Expected: Validation fails.
        assertFalse(result, "Expected invalid phone number to fail with literal country code 'KEN'.");
    }
}
