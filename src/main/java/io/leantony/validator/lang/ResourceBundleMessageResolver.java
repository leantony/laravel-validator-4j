package io.leantony.validator.lang;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The type Resource bundle message resolver.
 */
public class ResourceBundleMessageResolver implements ValidationMessageResolver {
    private final ResourceBundle bundle;

    /**
     * Instantiates a new Resource bundle message resolver.
     *
     * @param locale the locale
     */
    public ResourceBundleMessageResolver(Locale locale) {
        // Assumes your properties files are named "validationMessages_{locale}.properties"
        this.bundle = ResourceBundle.getBundle("validationMessages", locale);
    }

    @Override
    public String resolve(String key, Object... params) {
        String template = bundle.getString(key);
        return MessageFormat.format(template, params);
    }
}
