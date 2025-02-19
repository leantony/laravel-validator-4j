package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MaxRuleTest {
    private static final Map<String, Object> DATA = Map.of();

    private static Stream<Arguments> validCases() {
        return Stream.of(
                // Numeric values
                Arguments.of(10.0, 5),
                Arguments.of(5.0, 4.99),
                Arguments.of(0.0, -1),

                // String lengths
                Arguments.of(10.0, "short"),
                Arguments.of(5.0, ""),

                // Collection sizes
                Arguments.of(4.0, List.of(1, 2, 3)),
                Arguments.of(2.0, Set.of()),
                Arguments.of(5.0, new Object[3])
        );
    }

    private static Stream<Arguments> invalidCases() {
        return Stream.of(
                // Numeric values
                Arguments.of(5.0, 10),
                Arguments.of(3.14, 3.15),
                Arguments.of(0.0, 1),
                Arguments.of(-5.0, -4),

                // String lengths
                Arguments.of(3.0, "abcd"),
                Arguments.of(5.0, "Hello!"),

                // Collection sizes
                Arguments.of(2.0, List.of(1, 2, 3)),
                Arguments.of(1.0, Set.of("A", "B")),
                Arguments.of(3.0, new int[]{1, 2, 3, 4})
        );
    }

    private static Stream<Arguments> errorMessageCases() {
        return Stream.of(
                Arguments.of(5.0, 6, "field must be at most 5."),
                Arguments.of(3.0, "abcd", "field must be at most 3 characters long."),
                Arguments.of(2.0, List.of(1, 2, 3), "field must contain at most 2 items."),
                Arguments.of(10.0, new Object(), "field exceeds maximum requirement of 10 characters.")
        );
    }

    @ParameterizedTest
    @MethodSource("validCases")
    void validate_ValidCases_ReturnsTrue(double max, Object value) {
        MaxRule rule = new MaxRule(max);
        assertTrue(rule.validate("field", value, DATA));
    }

    @ParameterizedTest
    @MethodSource("invalidCases")
    void validate_InvalidCases_ReturnsFalse(double max, Object value) {
        MaxRule rule = new MaxRule(max);
        assertFalse(rule.validate("field", value, DATA));
    }

    @Test
    void validate_NullValue_ReturnsFalse() {
        MaxRule rule = new MaxRule(5.0);
        assertFalse(rule.validate("field", null, DATA));
    }

    @Test
    void validate_InvalidType_ReturnsFalse() {
        MaxRule rule = new MaxRule(5.0);
        assertFalse(rule.validate("field", new Object(), DATA));
    }

    @ParameterizedTest
    @MethodSource("errorMessageCases")
    void getErrorMessage_ReturnsCorrectMessage(double max, Object value, String expected) {
        MaxRule rule = new MaxRule(max);
        String message = rule.getErrorMessage("field", value, DATA);
        assertEquals(expected, message);
    }
}