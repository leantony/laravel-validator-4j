package io.leantony.validator.rules;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import io.leantony.validator.lang.MessageRegistry;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Phone no rule.
 */
public class PhoneNoRule extends BaseRule {
    private final List<String> allowedRegions;
    private final String countryField;
    @SuppressWarnings("FieldCanBeLocal")
    private final String defaultRegion = "KE"; // Default region is Kenya.
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    /**
     * Constructs a PhoneNoRule.
     *
     * @param parameter If null/empty, uses the default region ("KE").
     *                  If it contains commas and every token (after trimming)
     *                  is present in CountryRule.VALID_COUNTRY_CODES, it's treated as a list of allowed regions.
     *                  Otherwise, the parameter is treated as a field name that provides the region code.
     */
    public PhoneNoRule(String parameter) {
        if (parameter == null || parameter.trim().isEmpty()) {
            this.allowedRegions = null;
            this.countryField = null;
        } else {
            parameter = parameter.trim();
            if (parameter.contains(",")) {
                List<String> tokens = Arrays.stream(parameter.split(","))
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());
                boolean allCodes = CountryRule.VALID_COUNTRY_CODES.containsAll(tokens);
                if (allCodes) {
                    this.allowedRegions = tokens;
                    this.countryField = null;
                } else {
                    this.allowedRegions = null;
                    this.countryField = parameter;
                }
            } else {
                String paramUp = parameter.toUpperCase();
                if (CountryRule.VALID_COUNTRY_CODES.contains(paramUp)) {
                    this.allowedRegions = Collections.singletonList(paramUp);
                    this.countryField = null;
                } else {
                    this.allowedRegions = null;
                    this.countryField = parameter;
                }
            }
        }
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) return false;
        String phoneStr = ((String) value).trim();
        if (phoneStr.isEmpty()) return false;

        if (allowedRegions != null && !allowedRegions.isEmpty()) {
            return allowedRegions.stream().anyMatch(reg -> isValidPhoneForRegion(phoneStr, reg));
        } else {
            String region = defaultRegion;
            if (countryField != null) {
                Object regionObj = data.get(countryField);
                if (regionObj instanceof String && !((String) regionObj).trim().isEmpty()) {
                    region = convertToAlpha2(((String) regionObj).trim().toUpperCase());
                }
            }
            return isValidPhoneForRegion(phoneStr, region);
        }
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        if (allowedRegions != null && !allowedRegions.isEmpty()) {
            String regions = String.join(", ", allowedRegions);
            return MessageRegistry.getResolver().resolve("phoneno.allowed", field, regions);
        } else if (countryField != null) {
            return MessageRegistry.getResolver().resolve("phoneno.countryField", field, countryField);
        } else {
            return MessageRegistry.getResolver().resolve("phoneno.default", field);
        }
    }

    private boolean isValidPhoneForRegion(String phone, String region) {
        try {
            var number = phoneUtil.parse(phone, region);
            return phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
    }

    /**
     * Converts a given ISO country code token to its 2-letter version.
     * If the token is already 2 letters, it is returned as-is.
     * If it's 3 letters, this method iterates over ISO2 codes using Locale.getISOCountries()
     * and compares the corresponding ISO3 code with the token.
     *
     * @param token The country code token (2 or 3 letters).
     * @return The corresponding 2-letter code, or the token in uppercase if conversion is not possible.
     */
    private String convertToAlpha2(String token) {
        token = token.toUpperCase();
        if (token.length() == 2) {
            return token;
        } else if (token.length() == 3) {
            for (String iso2 : Locale.getISOCountries()) {
                try {
                    String iso3 = new Locale("", iso2).getISO3Country().toUpperCase();
                    if (iso3.equals(token)) {
                        return iso2.toUpperCase();
                    }
                } catch (Exception e) {
                    // Ignore and continue.
                }
            }
        }
        return token;
    }
}
