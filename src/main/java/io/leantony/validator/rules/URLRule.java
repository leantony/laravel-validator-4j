package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * The type Url rule.
 */
public class URLRule extends BaseRule {
    // A basic URL regex pattern.
    @SuppressWarnings("RegExpSimplifiable")
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$");

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) {
            return false;
        }
        return URL_PATTERN.matcher((String) value).matches();
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("url", field);
    }
}
