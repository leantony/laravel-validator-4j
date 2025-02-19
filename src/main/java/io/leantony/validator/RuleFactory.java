package io.leantony.validator;

import io.leantony.validator.rules.ValidationRule;

/**
 * The interface Rule factory.
 */
public interface RuleFactory {
    /**
     * Create validation rule.
     *
     * @param parameter the parameter
     * @return the validation rule
     */
    ValidationRule create(String parameter);
}
