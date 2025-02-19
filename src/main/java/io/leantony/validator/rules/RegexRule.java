package io.leantony.validator.rules;

import io.leantony.validator.lang.MessageRegistry;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * The type Regex rule.
 */
public class RegexRule extends BaseRule {
    // Hardcoded security parameters
    private static final long TIMEOUT_MS = 100;
    private static final int MAX_INPUT_LENGTH = 1024;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private final Pattern pattern;

    /**
     * Instantiates a new Regex rule.
     *
     * @param regex the regex
     */
    public RegexRule(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (value == null || value.toString().length() > MAX_INPUT_LENGTH) {
            return false;
        }

        String input = value.toString();
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> pattern.matcher(input).matches())
                .orTimeout(TIMEOUT_MS, TIME_UNIT);

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            future.cancel(true);
            return false;
        }
    }

    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return MessageRegistry.getResolver().resolve("regex", field);
    }
}
