package io.leantony.validator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneNoRuleIntegrationTest {

    // Scenario 1: No parameter provided; default region is "KE"
    @Test
    public void testPhoneWithDefaultRegion_KE() {
        Map<String, Object> data = new HashMap<>();
        // A valid Kenyan number. (e.g., +254712345678)
        data.put("phone", "+254712345678");

        Map<String, String> rules = new HashMap<>();
        // No parameter for phoneno; uses default region "KE".
        rules.put("phone", "required|phoneno");

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertTrue(valid, "Expected a valid phone number to pass validation with default region KE.");
        assertTrue(errors.isEmpty(), "Expected no errors for a valid phone number in KE.");
    }

    // Scenario 2: No parameter provided; default region is "KE" and invalid phone number.
    @Test
    public void testPhoneWithDefaultRegionInvalid_KE() {
        Map<String, Object> data = new HashMap<>();
        // A US phone number, which is invalid in Kenya.
        data.put("phone", "6502530000");

        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno"); // uses default region "KE"

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertFalse(valid, "Expected phone number to fail validation using default region KE.");
        // The error message is defined in PhoneNoRule.
        assertTrue(errors.contains("phone must be a valid phone number."),
                "Expected error message for an invalid phone number with default region KE.");
    }

    // Scenario 3: Allowed Regions Mode ("KE,GH,ZM")
    @Test
    public void testPhoneWithAllowedRegionsValid() {
        Map<String, Object> data = new HashMap<>();
        // A valid Kenyan number.
        data.put("phone", "+254712345678");

        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno:KEN,GH,ZMB"); // mix the ISO codes

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertTrue(valid, "Expected phone number to be valid for allowed regions KE, GH, ZM.");
        assertTrue(errors.isEmpty(), "Expected no errors for a valid phone with allowed regions.");
    }

    @Test
    public void testPhoneWithAllowedRegionsInvalid() {
        Map<String, Object> data = new HashMap<>();
        // A US phone number is likely invalid for KE, GH, ZM.
        data.put("phone", "6502530000");

        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno:KE,GH,ZM");

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertFalse(valid, "Expected phone number to be invalid for allowed regions KE, GH, ZM.");
        assertTrue(errors.contains("phone must be a valid phone number for one of the regions: [KE, GH, ZM]."),
                "Expected error message for an invalid phone with allowed regions.");
    }

    // Scenario 4: Country Field Mode (region provided via another field)
    @Test
    public void testPhoneWithCountryFieldValid() {
        Map<String, Object> data = new HashMap<>();
        // A valid Kenyan number.
        data.put("phone", "+254712345678");
        // The country field is provided.
        data.put("countryCode", "KEN"); // ISO 3

        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno:countryCode");

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertTrue(valid, "Expected phone number to be valid when using country field 'countryCode'.");
        assertTrue(errors.isEmpty(), "Expected no errors for valid phone with country field.");
    }

    @Test
    public void testPhoneWithCountryFieldInvalid() {
        Map<String, Object> data = new HashMap<>();
        // A US phone number, invalid when countryCode indicates KE.
        data.put("phone", "6502530000");
        data.put("countryCode", "KE");

        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno:countryCode");

        Validator validator = ValidatorBuilder.make(data, rules);
        boolean valid = validator.validate();
        List<String> errors = validator.getErrors();

        assertFalse(valid, "Expected phone number to be invalid when using country field 'countryCode'.");
        assertTrue(errors.contains("phone must be a valid phone number based on the country code provided in the countryCode field."),
                "Expected error message for invalid phone using country field.");
    }

    @Test
    public void testValidPhone_DefaultRegion() {
        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "phoneno:KE"); // Default country code

        Map<String, Object> data = new HashMap<>();
        data.put("phone", "+254712345678"); // Valid Kenyan number

        List<String> errors = RequestValidator.validate(data, rules);
        assertTrue(errors.isEmpty(), "Expected validation to pass using default region.");
    }

    @Test
    public void testValidPhone_Iso2Header() {
        String countryCode = "KE";
        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "phoneno:" + countryCode); // Using ISO2 country code

        Map<String, Object> data = new HashMap<>();
        data.put("phone", "+254712345678"); // Valid Kenyan number

        List<String> errors = RequestValidator.validate(data, rules);
        assertTrue(errors.isEmpty(), "Expected validation to pass using ISO2 country code.");
    }

    @Test
    public void testValidPhone_Iso3Header() {
        String countryCode = "KEN"; // ISO3 country code
        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "phoneno:" + countryCode);

        Map<String, Object> data = new HashMap<>();
        data.put("phone", "+254712345678"); // Valid Kenyan number

        List<String> errors = RequestValidator.validate(data, rules);
        assertTrue(errors.isEmpty(), "Expected validation to pass using ISO3 country code.");
    }

    @Test
    public void testInvalidPhone() {
        String countryCode = "KE";
        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "phoneno:" + countryCode);

        Map<String, Object> data = new HashMap<>();
        data.put("phone", "12345"); // Clearly invalid phone number

        List<String> errors = RequestValidator.validate(data, rules);
        assertFalse(errors.isEmpty(), "Expected validation to fail for an invalid phone number.");
        assertEquals("phone must be a valid phone number for one of the regions: [KE].", errors.get(0), "Expected specific validation error message.");
    }
}
