package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BeforeOrEqualRuleTest {
    private static final Map<String, Object> DATA = Map.of(
            "comparisonField", 10,
            "dateField", "2023-12-31"
    );

    private static Stream<Arguments> validCases() {
        return Stream.of(
                Arguments.of("numericField", 5, "comparisonField"),
                Arguments.of("equalNumeric", 10, "comparisonField"),
                Arguments.of("dateField", "2023-01-01", "dateField"),
                Arguments.of("equalDate", "2023-12-31", "dateField")
        );
    }

    private static Stream<Arguments> invalidCases() {
        return Stream.of(
                Arguments.of("numericField", 15, "comparisonField"),
                Arguments.of("dateField", "2024-01-01", "dateField")
        );
    }

    // Test valid scenarios
    @ParameterizedTest
    @MethodSource("validCases")
    void validate_ValidCases_ReturnsTrue(String ruleField, Object value, String otherField) {
        BeforeOrEqualRule rule = new BeforeOrEqualRule(otherField);
        assertTrue(rule.validate(ruleField, value, DATA));
    }

    // Test invalid scenarios
    @ParameterizedTest
    @MethodSource("invalidCases")
    void validate_InvalidCases_ReturnsFalse(String ruleField, Object value, String otherField) {
        BeforeOrEqualRule rule = new BeforeOrEqualRule(otherField);
        assertFalse(rule.validate(ruleField, value, DATA));
    }

    @Test
    void validate_TypeMismatch_ReturnsFalse() {
        BeforeOrEqualRule rule = new BeforeOrEqualRule("comparisonField");
        assertFalse(rule.validate("field", "not-a-number", DATA));
    }

    @Test
    void validate_NullValue_ReturnsFalse() {
        BeforeOrEqualRule rule = new BeforeOrEqualRule("comparisonField");
        assertFalse(rule.validate("field", null, DATA));
    }

    @Test
    void validate_MissingComparisonField_ReturnsFalse() {
        BeforeOrEqualRule rule = new BeforeOrEqualRule("nonExistingField");
        assertFalse(rule.validate("field", 5, DATA));
    }

    @Test
    void getErrorMessage_NumericComparison_ReturnsCorrectMessage() {
        BeforeOrEqualRule rule = new BeforeOrEqualRule("comparisonField");
        String message = rule.getErrorMessage("age", 25, DATA);
        assertEquals("age must be less than or equal to comparisonField.", message);
    }

    @Test
    void getErrorMessage_DateComparison_ReturnsCorrectMessage() {
        BeforeOrEqualRule rule = new BeforeOrEqualRule("dateField");
        String message = rule.getErrorMessage("startDate", "2023-01-01", DATA);
        assertEquals("startDate must be before or equal to dateField (using format yyyy-MM-dd).", message);
    }

    @Test
    void constructor_InvalidDateFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new BeforeOrEqualRule("dateField", "invalid-format")
        );
    }
}