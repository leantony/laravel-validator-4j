package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * The type Date rule.
 */
public class DateRule extends BaseRule {
    private final String format;
    private final SimpleDateFormat sdf;

    /**
     * Constructs a DateRule with a given date format.
     *
     * @param format the date format to use; if null or empty, defaults to "yyyy-MM-dd"
     */
    public DateRule(String format) {
        if (format == null || format.trim().isEmpty()) {
            this.format = "yyyy-MM-dd";
        } else {
            this.format = format;
        }
        this.sdf = new SimpleDateFormat(this.format);
        this.sdf.setLenient(false);
    }

    /**
     * Constructs a DateRule that uses the default format "yyyy-MM-dd".
     */
    public DateRule() {
        this("yyyy-MM-dd");
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) {
            return false;
        }
        try {
            sdf.parse((String) value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("date", field, format);
    }
}
