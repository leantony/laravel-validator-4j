package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * The type Before or equal rule.
 */
public class BeforeOrEqualRule extends BaseRule {
    private final String otherField;
    private final String dateFormat;

    /**
     * Instantiates a new Before or equal rule.
     *
     * @param otherField the other field
     */
    public BeforeOrEqualRule(String otherField) {
        this(otherField, "yyyy-MM-dd");
    }

    /**
     * Instantiates a new Before or equal rule.
     *
     * @param otherField the other field
     * @param dateFormat the date format
     */
    public BeforeOrEqualRule(String otherField, String dateFormat) {
        validateDateFormat(dateFormat);
        this.otherField = Objects.requireNonNull(otherField, "Comparison field cannot be null");
        this.dateFormat = dateFormat;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null || data == null) return false;

        Object otherValue = data.get(otherField);
        if (otherValue == null) return false;

        // First try numeric comparison
        if (areBothNumbers(value, otherValue)) {
            return compareNumbers(value, otherValue);
        }

        // Then try date comparison
        if (areBothDates(value, otherValue)) {
            return compareDates(value, otherValue);
        }

        // Type mismatch
        return false;
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        Object otherValue = data != null ? data.get(otherField) : null;
        if (areBothNumbers(value, otherValue)) {
            return MessageRegistry.getResolver().resolve("beforeOrEqual.numeric", field, otherField);
        } else {
            return MessageRegistry.getResolver().resolve("beforeOrEqual.date", field, otherField, dateFormat);
        }
    }

    private boolean areBothNumbers(Object a, Object b) {
        try {
            new BigDecimal(a.toString());
            new BigDecimal(b.toString());
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private boolean areBothDates(Object a, Object b) {
        try {
            return parseDate(a) != null && parseDate(b) != null;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean compareNumbers(Object a, Object b) {
        BigDecimal num1 = new BigDecimal(a.toString());
        BigDecimal num2 = new BigDecimal(b.toString());
        return num1.compareTo(num2) <= 0;
    }

    private boolean compareDates(Object a, Object b) {
        try {
            Date date1 = parseDate(a);
            Date date2 = parseDate(b);
            return !date1.after(date2);
        } catch (ParseException e) {
            return false;
        }
    }

    private Date parseDate(Object value) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        return sdf.parse(value.toString());
    }

    private void validateDateFormat(String format) {
        try {
            new SimpleDateFormat(format);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format: " + format);
        }
    }
}