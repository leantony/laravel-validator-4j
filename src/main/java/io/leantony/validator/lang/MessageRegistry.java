package io.leantony.validator.lang;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The type Message registry.
 */
public final class MessageRegistry {

    // A map that holds locale-specific message maps.
    private static final Map<Locale, Map<String, String>> messagesByLocale = new HashMap<>();

    // The current global ValidationMessageResolver.
    private static ValidationMessageResolver resolver;

    static {
        // Default messages for English.
        Map<String, String> englishMessages = new HashMap<>();
        englishMessages.put("required", "{0} is required.");
        englishMessages.put("email", "{0} must be a valid email address.");
        englishMessages.put("min", "{0} does not meet the minimum requirement of {1} characters.");
        englishMessages.put("min.number", "{0} must be at least {1}.");
        englishMessages.put("min.length", "{0} must be at least {1} characters long.");
        englishMessages.put("min.map", "{0} must contain at least {1} entries.");
        englishMessages.put("min.collection", "{0} must contain at least {1} items.");
        englishMessages.put("max", "{0} exceeds maximum requirement of {1} characters.");
        englishMessages.put("max.number", "{0} must be at most {1}.");
        englishMessages.put("max.length", "{0} must be at most {1} characters long.");
        englishMessages.put("max.map", "{0} must contain at most {1} entries.");
        englishMessages.put("max.collection", "{0} must contain at most {1} items.");
        englishMessages.put("length", "{0} must be exactly {1} characters long.");
        englishMessages.put("lengthRange", "{0} must be between {1} and {2} characters long.");
        englishMessages.put("date", "{0} must be a valid date in the format {1}.");
        englishMessages.put("alpha", "{0} must contain only alphabetic characters.");
        englishMessages.put("numeric", "{0} must be numeric.");
        englishMessages.put("numeric.exact", "{0} must be numeric and contain exactly {1} digits.");
        englishMessages.put("alphanumeric", "{0} must contain only letters and digits.");
        englishMessages.put("regex", "{0} does not match the required format.");
        englishMessages.put("afterOrEqual.numeric", "{0} must be greater than or equal to {1}.");
        englishMessages.put("afterOrEqual.date", "{0} must be after or equal to {1} (using format {2}).");
        englishMessages.put("beforeOrEqual.numeric", "{0} must be less than or equal to {1}.");
        englishMessages.put("beforeOrEqual.date", "{0} must be before or equal to {1} (using format {2}).");
        englishMessages.put("maxSize", "{0} must have no more than {1} elements.");
        englishMessages.put("boolean", "{0} must be a boolean value.");
        englishMessages.put("sameOrEqual", "{0} must be equal to {1}.");
        englishMessages.put("different", "{0} must be different from {1}.");
        englishMessages.put("url", "{0} must be a valid URL.");
        englishMessages.put("notIn", "{0} must not be one of [{1}].");
        englishMessages.put("in", "{0} must be one of [{1}].");
        englishMessages.put("enum", "{0} must be one of [{1}].");
        englishMessages.put("country", "{0} must be a valid ISO country code.");
        englishMessages.put("currency", "{0} must be a valid ISO currency code.");
        englishMessages.put("requiredWith", "{0} is required when {1} are present.");
        englishMessages.put("requiredWithAny", "{0} is required when at least one of {1} is present.");
        englishMessages.put("requiredWithout", "{0} is required when {1} are missing.");
        englishMessages.put("requiredWithoutAny", "{0} is required when either {1} is missing.");
        englishMessages.put("requiredIf", "{0} is required because {1} is {2}.");
        englishMessages.put("phoneno.allowed", "{0} must be a valid phone number for one of the regions: [{1}].");
        englishMessages.put("phoneno.countryField", "{0} must be a valid phone number based on the country code provided in the {1} field.");
        englishMessages.put("phoneno.default", "{0} must be a valid phone number.");

        // Put the default maps into our messagesByLocale.
        messagesByLocale.put(Locale.ENGLISH, englishMessages);

        // Set the default resolver using English as the active locale.
        resolver = new MapBasedLocaleMessageResolver(messagesByLocale, Locale.ENGLISH);
    }

    private MessageRegistry() {
    }

    /**
     * Returns the current global ValidationMessageResolver.
     *
     * @return the current resolver
     */
    public static ValidationMessageResolver getResolver() {
        return resolver;
    }

    /**
     * Allows setting a new global ValidationMessageResolver.
     *
     * @param newResolver the new resolver to use
     */
    public static void setResolver(ValidationMessageResolver newResolver) {
        resolver = newResolver;
    }

    /**
     * Overrides the message template for a specific key in the given locale.
     *
     * @param locale     the locale for which to override the message
     * @param key        the message key (e.g., "required", "length", etc.)
     * @param newMessage the new message template
     */
    public static void overrideMessage(Locale locale, String key, String newMessage) {
        Map<String, String> localeMessages = messagesByLocale.computeIfAbsent(locale, k -> new HashMap<>());
        localeMessages.put(key, newMessage);
    }

    /**
     * Switches the active locale for the map-based resolver.
     *
     * @param newLocale the new locale to use
     */
    public static void switchLocale(Locale newLocale) {
        resolver = new MapBasedLocaleMessageResolver(messagesByLocale, newLocale);
    }
}