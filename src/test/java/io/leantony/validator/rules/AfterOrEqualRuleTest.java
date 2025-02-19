package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AfterOrEqualRuleTest {
    private static final Map<String, Object> DATA = Map.of(
            "comparisonField", 5,
            "dateField", "2023-01-01"
    );

    private static Stream<Arguments> validCases() {
        return Stream.of(
                Arguments.of("numericField", 10, "comparisonField"),
                Arguments.of("equalNumeric", 5, "comparisonField"),
                Arguments.of("dateField", "2023-12-31", "dateField"),
                Arguments.of("equalDate", "2023-01-01", "dateField")
        );
    }

    private static Stream<Arguments> invalidCases() {
        return Stream.of(
                Arguments.of("numericField", 3, "comparisonField"),
                Arguments.of("dateField", "2022-12-31", "dateField")
        );
    }

    // Test valid scenarios
    @ParameterizedTest
    @MethodSource("validCases")
    void validate_ValidCases_ReturnsTrue(String ruleField, Object value, String otherField) {
        AfterOrEqualRule rule = new AfterOrEqualRule(otherField);
        assertTrue(rule.validate(ruleField, value, DATA));
    }

    // Test invalid scenarios
    @ParameterizedTest
    @MethodSource("invalidCases")
    void validate_InvalidCases_ReturnsFalse(String ruleField, Object value, String otherField) {
        AfterOrEqualRule rule = new AfterOrEqualRule(otherField);
        assertFalse(rule.validate(ruleField, value, DATA));
    }

    @Test
    void validate_TypeMismatch_ReturnsFalse() {
        AfterOrEqualRule rule = new AfterOrEqualRule("comparisonField");
        assertFalse(rule.validate("field", "not-a-number", DATA));
    }

    @Test
    void validate_NullValue_ReturnsFalse() {
        AfterOrEqualRule rule = new AfterOrEqualRule("comparisonField");
        assertFalse(rule.validate("field", null, DATA));
    }

    @Test
    void validate_MissingComparisonField_ReturnsFalse() {
        AfterOrEqualRule rule = new AfterOrEqualRule("nonExistingField");
        assertFalse(rule.validate("field", 10, DATA));
    }

    @Test
    void getErrorMessage_NumericComparison_ReturnsCorrectMessage() {
        AfterOrEqualRule rule = new AfterOrEqualRule("comparisonField");
        String message = rule.getErrorMessage("score", 85, DATA);
        assertEquals("score must be greater than or equal to comparisonField.", message);
    }

    @Test
    void getErrorMessage_DateComparison_ReturnsCorrectMessage() {
        AfterOrEqualRule rule = new AfterOrEqualRule("dateField");
        String message = rule.getErrorMessage("endDate", "2023-12-31", DATA);
        assertEquals("endDate must be after or equal to dateField (using format yyyy-MM-dd).", message);
    }

    @Test
    void constructor_InvalidDateFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new AfterOrEqualRule("dateField", "invalid-format")
        );
    }

    @Test
    void validate_CustomDateFormat_WorksCorrectly() {
        Map<String, Object> customData = Map.of("customDate", "31/12/2023");
        AfterOrEqualRule rule = new AfterOrEqualRule("customDate", "dd/MM/yyyy");
        assertTrue(rule.validate("testDate", "01/01/2024", customData));
    }
}