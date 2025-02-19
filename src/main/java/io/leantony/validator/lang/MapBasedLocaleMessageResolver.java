package io.leantony.validator.lang;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

/**
 * The type Map based locale message resolver.
 */
public class MapBasedLocaleMessageResolver implements ValidationMessageResolver {
    private final Map<Locale, Map<String, String>> messagesByLocale;
    private final Locale locale;

    /**
     * Instantiates a new Map based locale message resolver.
     *
     * @param messagesByLocale the messages by locale
     * @param locale           the locale
     */
    public MapBasedLocaleMessageResolver(Map<Locale, Map<String, String>> messagesByLocale, Locale locale) {
        this.messagesByLocale = messagesByLocale;
        this.locale = locale;
    }

    @Override
    public String resolve(String key, Object... params) {
        Map<String, String> messages = messagesByLocale.get(locale);
        if (messages == null) {
            // Fallback to English if the specified locale is not available.
            messages = messagesByLocale.get(Locale.ENGLISH);
        }
        String template = messages.getOrDefault(key, "{0} is invalid.");
        return MessageFormat.format(template, params);
    }
}
