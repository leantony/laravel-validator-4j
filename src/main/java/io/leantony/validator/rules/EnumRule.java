package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Enum rule.
 */
public class EnumRule implements ValidationRule {
    private final Class<? extends Enum<?>> enumClass;
    private final String allowedValues;

    /**
     * Constructs an EnumRule with the specified enum class.
     *
     * @param enumClassName The fully qualified name of the enum class.
     */
    @SuppressWarnings("unchecked")
    public EnumRule(String enumClassName) {
        try {
            Class<?> clazz = Class.forName(enumClassName);
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("Provided class is not an enum: " + enumClassName);
            }
            this.enumClass = (Class<? extends Enum<?>>) clazz;
            this.allowedValues = Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Enum class not found: " + enumClassName, e);
        }
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null) {
            return false; // Null values are not allowed in this validation
        }
        String stringValue = value.toString();
        return Arrays.stream(enumClass.getEnumConstants())
                     .map(Enum::name)
                     .anyMatch(enumValue -> enumValue.equalsIgnoreCase(stringValue));
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        // Use the "enum" key and pass the allowed values (as a comma‐separated string)
        return MessageRegistry.getResolver().resolve("enum", field, allowedValues);
    }

}
