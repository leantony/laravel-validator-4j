package io.leantony.validator;

import io.leantony.validator.rules.OptionalRule;
import io.leantony.validator.rules.ValidationRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Validator.
 */
@SuppressWarnings({"rawtypes", "RegExpRedundantEscape"})
public class Validator {
    // Precompiled regex to match tokens like "users[0]", "users[*]", or "users[1-5]"
    private static final Pattern INDEX_PATTERN = Pattern.compile("(.+)\\[(\\*|\\d+(?:-\\d+)?)\\]");
    /**
     * The constant MAX_NESTING_DEPTH.
     */
    private static int MAX_NESTING_DEPTH = 5;
    private final Map<String, Object> data;
    private final Map<String, List<ValidationRule>> rules;
    private final List<String> errors = new ArrayList<>();

    /**
     * Instantiates a new Validator.
     *
     * @param data  the data
     * @param rules the rules
     */
    public Validator(Map<String, Object> data, Map<String, List<ValidationRule>> rules) {
        this.data = data;
        this.rules = rules;
    }

    /**
     * Gets max nesting depth.
     *
     * @return the max nesting depth
     */
    public static int getMaxNestingDepth() {
        return MAX_NESTING_DEPTH;
    }

    /**
     * Sets max nesting depth.
     *
     * @param depth the depth
     */
    public static void setMaxNestingDepth(int depth) {
        if (depth < 2 || depth > 10) {
            throw new IllegalArgumentException("Max nesting depth should be between 2 and 10");
        }
        MAX_NESTING_DEPTH = depth;
    }

    /**
     * Validates the data using the provided rules.
     * Flattens the rule map (resolving wildcards, ranges, etc.) so that validation iterates over a flat map.
     *
     * @return true if all validations pass; false otherwise.
     */
    public boolean validate() {
        errors.clear();
        boolean valid = true;
        Map<String, List<ValidationRule>> flatRules = flattenRules();
        for (Map.Entry<String, List<ValidationRule>> entry : flatRules.entrySet()) {
            String field = entry.getKey();
            Object value = getValue(data, field);

            // If the field is marked optional and the value is empty, skip further validation.
            boolean skipValidation = entry.getValue().stream()
                    .anyMatch(rule -> rule instanceof OptionalRule) && isEmpty(value);
            if (skipValidation) {
                continue;
            }

            for (ValidationRule rule : entry.getValue()) {
                if (!rule.validate(field, value, data)) {
                    valid = false;
                    errors.add(rule.getErrorMessage(field, value, data));
                }
            }
        }
        return valid;
    }

    private boolean isEmpty(Object value) {
        return value == null || value.toString().trim().isEmpty();
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Flattens the rules map by resolving keys that contain wildcards/range notation into concrete field paths.
     */
    private Map<String, List<ValidationRule>> flattenRules() {
        Map<String, List<ValidationRule>> flatRules = new HashMap<>();
        for (Map.Entry<String, List<ValidationRule>> entry : rules.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split("\\.");
            if (parts.length > MAX_NESTING_DEPTH) {
                throw new IllegalArgumentException("Rule '" + key + "' exceeds maximum allowed nesting depth (" + MAX_NESTING_DEPTH + ")");
            }
            if (key.contains("*") || (key.contains("[") && key.contains("-"))) {
                List<ResolvedField> resolvedFields = resolveWildcardField(data, key);
                for (ResolvedField rf : resolvedFields) {
                    flatRules.computeIfAbsent(rf.fieldPath, k -> new ArrayList<>()).addAll(entry.getValue());
                }
            } else {
                flatRules.put(key, entry.getValue());
            }
        }
        return flatRules;
    }

    /**
     * Retrieves a nested value using dot notation and index notation.
     * (Assumes that wildcards and ranges have been resolved so that this method receives a concrete path.)
     */
    @SuppressWarnings({"unchecked", "RegExpRedundantEscape"})
    private Object getValue(Map<String, Object> data, String field) {
        String[] parts = field.split("\\.");
        Object current = data;
        for (String part : parts) {
            if (current == null) return null;
            Matcher matcher = INDEX_PATTERN.matcher(part);
            if (matcher.matches()) {
                String key = matcher.group(1);
                String indexPart = matcher.group(2);
                if (current instanceof Map) {
                    current = ((Map<String, Object>) current).get(key);
                }
                if (current instanceof List list) {
                    int index = Integer.parseInt(indexPart); // concrete index expected here
                    if (index < list.size()) {
                        current = list.get(index);
                    } else {
                        return null;
                    }
                }
            } else {
                if (current instanceof Map) {
                    current = ((Map<String, Object>) current).get(part);
                } else {
                    return null;
                }
            }
        }
        return current;
    }

    /**
     * Recursively resolves wildcard and range field keys.
     * Returns a list of concrete field paths that match the pattern.
     */
    @SuppressWarnings("unchecked")
    private List<ResolvedField> resolveWildcardField(Object current, String[] parts, int index, String pathSoFar) {
        List<ResolvedField> result = new ArrayList<>();
        if (index >= parts.length) {
            result.add(new ResolvedField(pathSoFar, current));
            return result;
        }
        String part = parts[index];
        Matcher matcher = INDEX_PATTERN.matcher(part);
        if (matcher.matches()) {
            // Token like: field[index], where index may be a concrete number, a wildcard (*), or a range (x-y)
            String key = matcher.group(1);
            String indexPart = matcher.group(2);
            Object next = null;
            if (current instanceof Map) {
                next = ((Map<String, Object>) current).get(key);
            }
            if (next == null) return result;
            if (next instanceof List list) {
                if (indexPart.equals("*")) {
                    for (int i = 0; i < list.size(); i++) {
                        String newPath = pathSoFar.isEmpty() ? key + "[" + i + "]" : pathSoFar + "." + key + "[" + i + "]";
                        result.addAll(resolveWildcardField(list.get(i), parts, index + 1, newPath));
                    }
                } else if (indexPart.contains("-")) {
                    String[] rangeParts = indexPart.split("-");
                    int start = Integer.parseInt(rangeParts[0]);
                    int end = Integer.parseInt(rangeParts[1]);
                    for (int i = start; i <= end && i < list.size(); i++) {
                        String newPath = pathSoFar.isEmpty() ? key + "[" + i + "]" : pathSoFar + "." + key + "[" + i + "]";
                        result.addAll(resolveWildcardField(list.get(i), parts, index + 1, newPath));
                    }
                } else {
                    int i = Integer.parseInt(indexPart);
                    if (i < list.size()) {
                        String newPath = pathSoFar.isEmpty() ? key + "[" + i + "]" : pathSoFar + "." + key + "[" + i + "]";
                        result.addAll(resolveWildcardField(list.get(i), parts, index + 1, newPath));
                    }
                }
            } else {
                // If next is not a list, just continue with the key resolved.
                String newPath = pathSoFar.isEmpty() ? key : pathSoFar + "." + key;
                result.addAll(resolveWildcardField(next, parts, index + 1, newPath));
            }
        } else if (part.equals("*")) {
            // Wildcard for maps or lists at this level.
            if (current instanceof Map map) {
                for (Object keyObj : map.keySet()) {
                    String key = keyObj.toString();
                    Object next = map.get(key);
                    String newPath = pathSoFar.isEmpty() ? key : pathSoFar + "." + key;
                    result.addAll(resolveWildcardField(next, parts, index + 1, newPath));
                }
            } else if (current instanceof List list) {
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    String newPath = pathSoFar.isEmpty() ? "[" + i + "]" : pathSoFar + "[" + i + "]";
                    result.addAll(resolveWildcardField(item, parts, index + 1, newPath));
                }
            }
        } else {
            // Plain field name
            String newPath = pathSoFar.isEmpty() ? part : pathSoFar + "." + part;
            Object next = null;
            if (current instanceof Map) {
                next = ((Map<String, Object>) current).get(part);
            }
            if (next != null) {
                result.addAll(resolveWildcardField(next, parts, index + 1, newPath));
            }
        }
        return result;
    }

    private List<ResolvedField> resolveWildcardField(Map<String, Object> data, String field) {
        String[] parts = field.split("\\.");
        return resolveWildcardField(data, parts, 0, "");
    }

    /**
     * A helper record to hold a resolved field path and its value.
     */
    private record ResolvedField(String fieldPath, Object value) {
    }
}
