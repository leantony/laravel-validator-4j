package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DateRuleTest {

    @Test
    void validDate_DefaultFormat_ShouldPassValidation() {
        DateRule rule = new DateRule();
        assertTrue(rule.validate("birthDate", "2025-02-15", Collections.emptyMap()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"15-02-2025", "02-15-2025", "2025/02/15"})
    void invalidDate_DefaultFormat_ShouldFailValidation(String input) {
        DateRule rule = new DateRule();
        assertFalse(rule.validate("birthDate", input, Collections.emptyMap()));
    }

    @Test
    void validDate_CustomFormat_ShouldPassValidation() {
        DateRule rule = new DateRule("dd/MM/yyyy");
        assertTrue(rule.validate("appointment", "15/02/2025", Collections.emptyMap()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2025-02-15", "15-02-2025", "2025/02/15"})
    void invalidDate_CustomFormat_ShouldFailValidation(String input) {
        DateRule rule = new DateRule("dd/MM/yyyy");
        assertFalse(rule.validate("appointment", input, Collections.emptyMap()));
    }

    @Test
    void nullValue_ShouldFailValidation() {
        DateRule rule = new DateRule();
        assertFalse(rule.validate("eventDate", null, Collections.emptyMap()));
    }

    @Test
    void nonStringValue_ShouldFailValidation() {
        DateRule rule = new DateRule();
        assertFalse(rule.validate("eventDate", 20250215, Collections.emptyMap()));
    }

    @Test
    void emptyString_ShouldFailValidation() {
        DateRule rule = new DateRule();
        assertFalse(rule.validate("eventDate", "", Collections.emptyMap()));
    }

    @Test
    void invalidDateValue_ShouldFailValidation() {
        DateRule rule = new DateRule();
        assertFalse(rule.validate("eventDate", "2025-02-30", Collections.emptyMap())); // Feb 30 is invalid
    }

    @Test
    void getErrorMessage_ShouldReturnExpectedMessage() {
        DateRule rule = new DateRule("MM/dd/yyyy");
        String message = rule.getErrorMessage("startDate", "invalid", Collections.emptyMap());
        assertEquals("startDate must be a valid date in the format MM/dd/yyyy.", message);
    }
}
