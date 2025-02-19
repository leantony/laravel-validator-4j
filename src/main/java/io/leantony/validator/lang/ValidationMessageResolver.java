package io.leantony.validator.lang;

/**
 * The interface Validation message resolver.
 */
public interface ValidationMessageResolver {
    /**
     * Resolves a message for a given key with parameters.
     *
     * @param key    the message key (e.g., "length")
     * @param params parameters used in the message template
     * @return the resolved error message
     */
    String resolve(String key, Object... params);
}
