package io.leantony.validator.rules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.leantony.validator.ValidationHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptionalRuleTest {

    @Test
    public void testOptionalEmailWhenMissing() {
        ObjectMapper mapper = new ObjectMapper();
        // Include null values in the conversion.
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        ValidationHelper helper = new ValidationHelper(mapper);

        DummyDto dto = new DummyDto(null);

        Map<String, String> rules = new HashMap<>();
        rules.put("email", "optional|email");

        // When the email is null, because it's optional, validation should pass.
        List<String> errors = helper.validate(dto, rules);
        assertTrue(errors.isEmpty(), "Expected no errors when 'email' is null for an optional field.");

        Boolean valid = helper.isValid(dto, rules);
        assertTrue(valid, "Expected isValid to return true for a missing optional email.");
    }

    @Test
    public void testOptionalEmailWhenPresentButInvalid() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        ValidationHelper helper = new ValidationHelper(mapper);

        DummyDto dto = new DummyDto("invalid-email");

        Map<String, String> rules = new HashMap<>();
        rules.put("email", "optional|email");

        // When the email is present but invalid, an error should be produced.
        List<String> errors = helper.validate(dto, rules);
        assertFalse(errors.isEmpty(), "Expected errors when an invalid email is provided.");
        assertTrue(errors.contains("email must be a valid email address."), "Expected error message: 'email must be a valid email address.'");

        Boolean valid = helper.isValid(dto, rules);
        assertFalse(valid, "Expected isValid to return false for an invalid email.");
    }

    @Test
    public void testOptionalEmailWhenPresentAndValid() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        ValidationHelper helper = new ValidationHelper(mapper);

        DummyDto dto = new DummyDto("test@example.com");

        Map<String, String> rules = new HashMap<>();
        rules.put("email", "optional|email");

        // When the email is valid, there should be no errors.
        List<String> errors = helper.validate(dto, rules);
        assertTrue(errors.isEmpty(), "Expected no errors when a valid email is provided.");

        Boolean valid = helper.isValid(dto, rules);
        assertTrue(valid, "Expected isValid to return true for a valid email.");
    }

    public static class DummyDto {
        private String email;

        public DummyDto() {
        }

        public DummyDto(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
