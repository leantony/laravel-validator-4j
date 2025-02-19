package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Currency;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The type Currency rule.
 */
public class CurrencyRule extends BaseRule {
    private static final Set<String> validCurrencies = new HashSet<>();

    static {
        // Build a set of valid currency codes (ISO 4217). Currency codes are 3 letters.
        for (Currency currency : Currency.getAvailableCurrencies()) {
            validCurrencies.add(currency.getCurrencyCode().toUpperCase());
        }
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) {
            return false;
        }
        String code = ((String) value).trim().toUpperCase();
        return validCurrencies.contains(code);
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("currency", field);
    }
}
