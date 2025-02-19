package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AlphaNumericRuleTest {

    private final AlphaNumericRule rule = new AlphaNumericRule();

    @ParameterizedTest
    @ValueSource(strings = {"abc123", "ABC", "123", "a1B2C3"})
    void validAlphanumericStrings_ShouldPassValidation(String input) {
        assertTrue(rule.validate("testField", input, Collections.emptyMap()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc 123", "abc!", "123#", "@test", "hello world", " "})
    void invalidAlphanumericStrings_ShouldFailValidation(String input) {
        assertFalse(rule.validate("testField", input, Collections.emptyMap()));
    }

    @Test
    void nullValue_ShouldFailValidation() {
        assertFalse(rule.validate("testField", null, Collections.emptyMap()));
    }

    @Test
    void emptyString_ShouldFailValidation() {
        assertFalse(rule.validate("testField", "", Collections.emptyMap()));
    }

    @Test
    void getErrorMessage_ShouldReturnExpectedMessage() {
        String message = rule.getErrorMessage("username", "abc@123", Collections.emptyMap());
        assertEquals("username must contain only letters and digits.", message);
    }
}
