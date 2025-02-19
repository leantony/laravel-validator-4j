package io.leantony.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * The type Validation helper.
 */
public class ValidationHelper {

    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Validation helper.
     *
     * @param mapper the mapper
     */
    public ValidationHelper(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    /**
     * Converts the given DTO to a Map using Jackson and builds a Validator with the provided rules.
     *
     * @param dto   the data transfer object to validate
     * @param rules a Map of field names to DSL rule strings (e.g., "email" -> "required|email")
     * @return a Validator instance that can then be used to call validate() and getErrors()
     */
    public Validator createValidator(Object dto, Map<String, String> rules) {
        Map<String, Object> data = objectMapper.convertValue(dto, new TypeReference<>() {
        });
        // build and return the Validator.
        return ValidatorBuilder.make(data, rules);
    }

    /**
     * Validate dto.
     *
     * @param dto   the dto
     * @param rules the rules
     * @return the list
     */
    public List<String> validate(Object dto, Map<String, String> rules) {
        var validator = createValidator(dto, rules);
        validator.validate();
        return validator.getErrors();
    }

    /**
     * Is valid boolean.
     *
     * @param dto   the dto
     * @param rules the rules
     * @return the boolean
     */
    public Boolean isValid(Object dto, Map<String, String> rules) {
        var validator = createValidator(dto, rules);
        return validator.validate();
    }

    /**
     * Validate.
     *
     * @param dto   the dto
     * @param rules the rules
     * @return the list
     */
    public List<String> validate(Map<String, Object> dto, Map<String, String> rules) {
        var validator = ValidatorBuilder.make(dto, rules);
        validator.validate();
        return validator.getErrors();
    }
}
