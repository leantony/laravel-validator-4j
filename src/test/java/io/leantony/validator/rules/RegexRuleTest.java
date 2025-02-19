package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RegexRuleTest {

    @Test
    void validPattern_ShouldPassValidation() {
        RegexRule rule = new RegexRule("^[a-zA-Z]+$"); // Only letters allowed
        assertTrue(rule.validate("name", "HelloWorld", Collections.emptyMap()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "hello123", "!", " "})
    void invalidPattern_ShouldFailValidation(String input) {
        RegexRule rule = new RegexRule("^[a-zA-Z]+$"); // Only letters allowed
        assertFalse(rule.validate("name", input, Collections.emptyMap()));
    }

    @Test
    void nullValue_ShouldFailValidation() {
        RegexRule rule = new RegexRule("^[a-zA-Z]+$");
        assertFalse(rule.validate("name", null, Collections.emptyMap()));
    }

    @Test
    void emptyString_ShouldFailValidation() {
        RegexRule rule = new RegexRule("^[a-zA-Z]+$");
        assertFalse(rule.validate("name", "", Collections.emptyMap()));
    }

    @Test
    void largeInput_ShouldFailValidation() {
        RegexRule rule = new RegexRule(".*");
        String largeInput = "a".repeat(1025); // Exceeds MAX_INPUT_LENGTH
        assertFalse(rule.validate("testField", largeInput, Collections.emptyMap()));
    }

    @Test
    void regexTimeout_ShouldFailValidation() {
        // Evil regex that can cause catastrophic backtracking
        RegexRule rule = new RegexRule("(a+)+$");

        // Input designed to trigger excessive backtracking
        String slowInput = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa!";

        assertFalse(rule.validate("testField", slowInput, Collections.emptyMap()));
    }

    @Test
    void getErrorMessage_ShouldReturnExpectedMessage() {
        RegexRule rule = new RegexRule(".*");
        String message = rule.getErrorMessage("username", "invalid", Collections.emptyMap());
        assertEquals("username does not match the required format.", message);
    }
}
