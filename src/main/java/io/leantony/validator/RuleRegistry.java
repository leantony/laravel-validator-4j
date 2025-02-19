package io.leantony.validator;

import io.cellulant.vas.validator.rules.*;
import io.leantony.validator.rules.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Rule registry.
 */
public class RuleRegistry {
    private static final Map<String, RuleFactory> registry = new HashMap<>();

    // default rules
    static {
        // "required": Ensures the field is present and non-empty.
        registry.put("required", param -> new RequiredRule());

        // "email": Validates that the field contains a properly formatted email address.
        registry.put("email", param -> new EmailRule());

        // "min": Validates that a numeric field is at least the specified value.
        // Parameter should be a number (as a String) that is parsed to a Double.
        registry.put("min", param -> new MinRule(Double.parseDouble(param)));

        // "max": Validates that a numeric field is no more than the specified value.
        // Parameter should be a number (as a String) that is parsed to a Double.
        registry.put("max", param -> new MaxRule(Double.parseDouble(param)));

        // "date": Validates that a field contains a valid date.
        // If no format is provided, it defaults to "yyyy-MM-dd"; otherwise, uses the given format.
        registry.put("date", param -> {
            if (param == null || param.trim().isEmpty()) {
                return new DateRule();
            } else {
                return new DateRule(param);
            }
        });

        // "alpha": Validates that a field contains only alphabetic characters.
        registry.put("alpha", param -> new AlphaRule());

        // "alphanumeric": Validates that a field contains only letters and digits.
        registry.put("alphanumeric", param -> new AlphaNumericRule());

        // "numeric": Validates that a field is numeric.
        // Optionally, a parameter can specify an exact digit count (ignoring non-digit characters).
        registry.put("numeric", param -> {
            if (param == null || param.trim().isEmpty()) {
                return new NumericRule();
            } else {
                return new NumericRule(Integer.parseInt(param.trim()));
            }
        });

        // "regex": Validates that a field matches the specified regular expression.
        registry.put("regex", RegexRule::new);

        // "optional": Marks a field as optional. If missing or empty, subsequent validations are skipped.
        registry.put("optional", param -> new OptionalRule());

        // "nullable": Alias for optional.
        registry.put("nullable", param -> new OptionalRule());

        // "afterOrEqual": Cross-field rule; ensures a field's value is after or equal to another field's value.
        registry.put("afterOrEqual", AfterOrEqualRule::new);

        // "beforeOrEqual": Cross-field rule; ensures a field's value is before or equal to another field's value.
        registry.put("beforeOrEqual", BeforeOrEqualRule::new);

        // "lessOrEqualTo": Alias for beforeOrEqual.
        registry.put("lessOrEqualTo", BeforeOrEqualRule::new);

        // "greaterOrEqualTo": Alias for afterOrEqual.
        registry.put("greaterOrEqualTo", AfterOrEqualRule::new);

        // "maxSize": Validates that a collection or string does not exceed a specified size.
        // Parameter should be a number (as a String) indicating the maximum allowed size.
        registry.put("maxSize", param -> new MaxRule(Integer.parseInt(param)));

        // "boolean": Validates that a field is a boolean value (true/false, or acceptable variants).
        registry.put("boolean", param -> new BooleanRule());

        // "sameOrEqual": Validates that a field's value is equal to the value of another specified field.
        registry.put("sameOrEqual", SameOrEqualRule::new);

        // "different": Validates that a field's value is different from the value of another specified field.
        registry.put("different", DifferentRule::new);

        // "url": Validates that a field contains a properly formatted URL.
        registry.put("url", param -> new URLRule());

        // "notIn": Validates that a field's value is not contained within a provided list of disallowed values.
        registry.put("notIn", NotInRule::new);

        // "in": Validates that a field's value is contained within a provided list of allowed values.
        registry.put("in", InRule::new);

        // "enum": Validates that a field's value is contained within the values of an existing enum class (fully qualified name).\n
        // Format: enum:com.example.myEnumClass\n" +
        registry.put("enum", EnumRule::new);

        // "country": Validates that a field is a valid ISO country code (2-letter or 3-letter, case-insensitive).
        registry.put("country", param -> new CountryRule());

        // "currency": Validates that a field is a valid ISO 4217 currency code.
        registry.put("currency", param -> new CurrencyRule());

        // "requiredWith": The field is required if all of the specified other fields are present and non-empty.
        registry.put("requiredWith", RequiredWithRule::new);

        // "requiredWithAny": The field is required if any of the specified other fields are present and non-empty.
        registry.put("requiredWithAny", RequiredWithAnyRule::new);

        // "requiredWithout": The field is required if all of the specified other fields are absent or empty.
        registry.put("requiredWithout", RequiredWithoutRule::new);

        // "requiredWithoutAny": The field is required if any of the specified other fields are absent or empty.
        registry.put("requiredWithoutAny", RequiredWithoutAnyRule::new);

        // "requiredIf": The field is required if a specified condition is met (other field equals a given value).\n" +
        // Format: requiredIf:key,value\n" +
        // Example: \"requiredIf:membershipLevel,gold\"\n" +
        registry.put("requiredIf", RequiredIfRule::new);

        // "length": Validates that a string's length is either exactly a specified value or falls within a given range.\n" +
        // Format: \"length:5\" for exact length, or \"length:2,10\" for a range.\n" +
        registry.put("length", LengthRule::new);

        // "phoneno": Validates that a field contains a valid phone number, with support for allowed regions\n" +
        // or dynamic region retrieval from another field.\n" +
        // Format: \"phoneno:KE\" or \"phoneno:KEN,UGA\" or \"phoneno:countryField\"\n" +
        registry.put("phoneno", PhoneNoRule::new);
    }

    static {
        Validator.setMaxNestingDepth(5);
    }

    /**
     * Register.
     *
     * @param ruleName the rule name
     * @param factory  the factory
     */
    public static void register(String ruleName, RuleFactory factory) {
        registry.put(ruleName, factory);
    }

    /**
     * Get rule factory.
     *
     * @param ruleName the rule name
     * @return the rule factory
     */
    public static RuleFactory get(String ruleName) {
        return registry.get(ruleName);
    }
}
