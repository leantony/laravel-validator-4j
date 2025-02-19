package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The type country rule.
 */
public class CountryRule extends BaseRule {
    /**
     * The constant VALID_COUNTRY_CODES.
     */
    public static final Set<String> VALID_COUNTRY_CODES = new HashSet<>();

    static {
        for (String iso2 : Locale.getISOCountries()) {
            VALID_COUNTRY_CODES.add(iso2.toUpperCase());
            try {
                String iso3 = new Locale("", iso2).getISO3Country().toUpperCase();
                VALID_COUNTRY_CODES.add(iso3);
            } catch (Exception e) {
                // Ignore locales without an ISO3 code.
            }
        }
    }

    @Override
    public boolean validate(String field, Object value, java.util.Map<String, Object> data) {
        if (!(value instanceof String)) return false;
        String code = ((String) value).trim().toUpperCase();
        return VALID_COUNTRY_CODES.contains(code);
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("country", field);
    }
}
