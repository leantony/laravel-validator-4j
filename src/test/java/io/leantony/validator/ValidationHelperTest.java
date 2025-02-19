package io.leantony.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationHelperTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final ValidationHelper helper = new ValidationHelper(mapper);

    @Test
    public void testRequiredRuleForNameField() {
        DummyDto dto = new DummyDto();
        dto.setName(null);

        Map<String, String> rules = new HashMap<>();
        rules.put("name", "required");

        List<String> errors = helper.validate(dto, rules);

        assertFalse(errors.isEmpty(), "Errors should not be empty when 'name' is null.");
        assertTrue(errors.contains("name is required."), "Expected error message: 'name is required.'");

        boolean isValid = helper.isValid(dto, rules);
        assertFalse(isValid, "Validation should fail when 'name' is null.");
    }

    @Test
    public void testValidNamePassesRequiredRule() {
        DummyDto dto = new DummyDto("John Doe");

        Map<String, String> rules = new HashMap<>();
        rules.put("name", "required");

        List<String> errors = helper.validate(dto, rules);

        assertTrue(errors.isEmpty(), "Errors should be empty when 'name' is provided.");

        boolean isValid = helper.isValid(dto, rules);
        assertTrue(isValid, "Validation should pass when 'name' is provided.");
    }

    @Test
    public void testValidateWithMapInput() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("name", null);

        Map<String, String> rules = new HashMap<>();
        rules.put("name", "required");

        List<String> errors = helper.validate(dto, rules);

        assertFalse(errors.isEmpty(), "Errors should not be empty when 'name' is null.");
        assertTrue(errors.contains("name is required."), "Expected error message: 'name is required.'");

        boolean isValid = helper.isValid(dto, rules);
        assertFalse(isValid, "Validation should fail when 'name' is null.");
    }

    static class DummyDto {
        private String name;

        public DummyDto() {
        }

        public DummyDto(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
