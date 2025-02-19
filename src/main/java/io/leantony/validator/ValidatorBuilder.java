package io.leantony.validator;

import io.leantony.validator.rules.ValidationRule;

import java.util.*;

/**
 * The type Validator builder.
 */
public class ValidatorBuilder {
    /**
     * Creates a Validator instance using the provided data and rule definitions.
     *
     * @param data            a Map representing your DTO
     * @param ruleDefinitions a Map from field names to DSL rule strings (e.g., "email" -> "required|email")
     * @return a Validator instance
     */
    public static Validator make(Map<String, Object> data, Map<String, String> ruleDefinitions) {
        Map<String, List<ValidationRule>> rules = new HashMap<>();
        for (Map.Entry<String, String> entry : ruleDefinitions.entrySet()) {
            String field = entry.getKey();
            String[] parts = field.split("\\.");
            if (parts.length > Validator.getMaxNestingDepth()) {
                throw new IllegalArgumentException("Rule '" + field + "' exceeds maximum allowed nesting depth (" + Validator.getMaxNestingDepth() + ")");
            }
            String ruleStr = entry.getValue();
            List<ValidationRule> ruleList = parseRules(ruleStr);
            rules.put(field, ruleList);
        }
        return new Validator(data, rules);
    }
    
    private static List<ValidationRule> parseRules(String ruleStr) {
        List<ValidationRule> rules = new ArrayList<>();
        List<String> tokens = tokenize(ruleStr, '|');
        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue;
            int colonIndex = indexOfUnescaped(token, ':');
            String ruleName;
            String parameter = null;
            if (colonIndex != -1) {
                ruleName = token.substring(0, colonIndex).trim();
                parameter = token.substring(colonIndex + 1).trim();
                // Unescape any escaped colon.
                parameter = parameter.replace("\\:", ":");
            } else {
                ruleName = token;
            }
            // Unescape escaped pipes in ruleName.
            ruleName = ruleName.replace("\\|", "|");
            RuleFactory factory = RuleRegistry.get(ruleName);
            if (factory == null) {
                throw new IllegalArgumentException("Unknown validation rule: " + ruleName);
            }
            try {
                ValidationRule rule = factory.create(parameter);
                rules.add(rule);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("Error creating rule for '%s' with parameter '%s'.", ruleName, parameter), e);
            }
        }
        return rules;
    }
    
    @SuppressWarnings("SameParameterValue")
    private static List<String> tokenize(String input, char delimiter) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean escaping = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (escaping) {
                current.append(c);
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == delimiter) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            tokens.add(current.toString());
        }
        return tokens;
    }
    
    @SuppressWarnings("SameParameterValue")
    private static int indexOfUnescaped(String input, char target) {
        boolean escaping = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (escaping) {
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == target) {
                return i;
            }
        }
        return -1;
    }
}
