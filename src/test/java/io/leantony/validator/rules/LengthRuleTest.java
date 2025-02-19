package io.leantony.validator.rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LengthRuleTest {

    // Provide test data for exact length tests.
    static Stream<Arguments> exactLengthProvider() {
        return Stream.of(
                Arguments.of("5", "hello", true),
                Arguments.of("5", "hell", false),
                Arguments.of("5", "helloo", false)
        );
    }

    // Provide test data for range length tests.
    static Stream<Arguments> rangeLengthProvider() {
        return Stream.of(
                Arguments.of("2,10", "hi", true),            // 2 characters, minimum valid.
                Arguments.of("2,10", "hello", true),           // 5 characters, valid.
                Arguments.of("2,10", "h", false),              // Too short.
                Arguments.of("2,10", "thisislongerthan10", false) // Too long.
        );
    }

    @ParameterizedTest
    @MethodSource("exactLengthProvider")
    public void testExactLengthRule(String param, String value, boolean expected) {
        LengthRule rule = new LengthRule(param);
        boolean result = rule.validate("field", value, new HashMap<>());
        assertEquals(expected, result, "For value: '" + value + "', expected result: " + expected);
    }

    @ParameterizedTest
    @MethodSource("rangeLengthProvider")
    public void testRangeLengthRule(String param, String value, boolean expected) {
        LengthRule rule = new LengthRule(param);
        boolean result = rule.validate("field", value, new HashMap<>());
        assertEquals(expected, result, "For value: '" + value + "', expected result: " + expected);
    }

    @Test
    public void testErrorMessageExact() {
        LengthRule rule = new LengthRule("5");
        String errorMessage = rule.getErrorMessage("username", "hell", new HashMap<>());
        assertEquals("username must be exactly 5 characters long.", errorMessage);
    }

    @Test
    public void testErrorMessageRange() {
        LengthRule rule = new LengthRule("2,10");
        String errorMessage = rule.getErrorMessage("username", "h", new HashMap<>());
        assertEquals("username must be between 2 and 10 characters long.", errorMessage);
    }

    @Test
    public void testNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule(null));
    }

    @Test
    public void testEmptyParameter() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("   "));
    }

    @Test
    public void testTooManyParameters() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("1,2,3"));
    }

    @Test
    public void testNonNumericParameter() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("a,b"));
    }

    @Test
    public void testNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("-1"));
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("2, -5"));
    }

    @Test
    public void testMinExceedsMax() {
        assertThrows(IllegalArgumentException.class, () -> new LengthRule("10,5"));
    }
}
