package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CountryRuleTest {

    @Test
    public void testValidTwoLetterCountryCode() {
        CountryRule rule = new CountryRule();
        Map<String, Object> data = new HashMap<>();
        // "US" should be valid.
        assertTrue(rule.validate("country", "US", data));
        assertTrue(rule.validate("country", "KE", data));
        assertTrue(rule.validate("country", "UG", data));
    }

    @Test
    public void testValidThreeLetterCountryCode() {
        CountryRule rule = new CountryRule();
        Map<String, Object> data = new HashMap<>();
        // "USA" should be valid.
        assertTrue(rule.validate("country", "USA", data));
        assertTrue(rule.validate("country", "GHA", data));
        assertTrue(rule.validate("country", "ZMB", data));
        assertTrue(rule.validate("country", "KEN", data));
    }

    @Test
    public void testInvalidCountryCode() {
        CountryRule rule = new CountryRule();
        Map<String, Object> data = new HashMap<>();
        // "XX" is typically not a valid country code.
        assertFalse(rule.validate("country", "XX", data));
    }

    @Test
    public void testErrorMessage() {
        CountryRule rule = new CountryRule();
        Map<String, Object> data = new HashMap<>();
        String msg = rule.getErrorMessage("country", "XX", data);
        assertEquals("country must be a valid ISO country code.", msg);
    }
}
