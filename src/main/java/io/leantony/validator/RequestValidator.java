package io.leantony.validator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * The type Request validator.
 */
public class RequestValidator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.ALWAYS);

    private static final ValidationHelper VALIDATION_HELPER = new ValidationHelper(OBJECT_MAPPER);

    private RequestValidator() {
    }

    /**
     * Gets validation helper.
     *
     * @return the validation helper
     */
    public static ValidationHelper getValidationHelper() {
        return VALIDATION_HELPER;
    }

    /**
     * Validate object.
     *
     * @param object the object
     * @param rules  the rules
     * @return the list
     */
    public static List<String> validate(Object object, Map<String, String> rules) {
        return getValidationHelper().validate(object, rules);
    }
}
