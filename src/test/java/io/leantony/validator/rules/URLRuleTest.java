package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class URLRuleTest {

    @Test
    public void testValidURLs() {
        URLRule rule = new URLRule();
        Map<String, Object> data = new HashMap<>();
        assertTrue(rule.validate("website", "https://example.com", data));
        assertTrue(rule.validate("website", "http://example.com", data));
        assertTrue(rule.validate("website", "ftp://example.com", data));
    }

    @Test
    public void testInvalidURLs() {
        URLRule rule = new URLRule();
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("website", "example.com", data));
        assertFalse(rule.validate("website", "htp://example.com", data));
        assertFalse(rule.validate("website", "not a url", data));
    }

    @Test
    public void testNullValue() {
        URLRule rule = new URLRule();
        Map<String, Object> data = new HashMap<>();
        assertFalse(rule.validate("website", null, data));
    }

    @Test
    public void testErrorMessage() {
        URLRule rule = new URLRule();
        Map<String, Object> data = new HashMap<>();
        String errorMessage = rule.getErrorMessage("website", "invalid", data);
        assertEquals("website must be a valid URL.", errorMessage);
    }
}
