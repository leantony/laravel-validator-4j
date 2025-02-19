package io.leantony.validator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ExtractMethodRecommender")
public class NestedRulesIntegrationTest {

    // -- Integration test: Valid DTO --
    @Test
    void testValidDto() {
        // Build a valid TestDto object.
        TestDto dto = new TestDto();
        dto.email = "test@example.com";
        dto.password = "securePassword";

        // Create a list of users (all with valid email and names)
        User user1 = new User();
        user1.email = "user1@example.com";
        user1.names = Map.of("first", "John", "last", "Doe");

        User user2 = new User();
        user2.email = "user2@example.com";
        user2.names = Map.of("first", "Jane", "last", "Smith");

        dto.users = List.of(user1, user2);

        // Build a roles map. For example, roles may contain top-level keys and nested maps.
        Map<String, Object> roles = new HashMap<>();
        roles.put("name", "Admin");
        roles.put("type", Map.of("name", "SuperAdmin"));

        // For roles.types, assume it's a list of maps (each with a 'name' key).
        List<Map<String, Object>> types = new ArrayList<>();
        types.add(Map.of("name", "Type1"));
        types.add(Map.of("name", "Type2"));
        roles.put("types", types);

        dto.roles = roles;

        // Define rules covering simple and nested cases:
        Map<String, String> rules = new HashMap<>();
        // Basic rules:
        rules.put("email", "required|email");
        rules.put("password", "required|min:8");

        // Users list validations:
        rules.put("users[0].email", "required|email");   // First user's email is required.
        rules.put("users[*].email", "required|email");     // Every user's email is required.
        rules.put("users[1-5].email", "required|email");   // Users 1 to 5 must have an email.

        // Roles map validations:
        rules.put("roles.*", "required");                  // Every top-level value in roles is required.
        rules.put("roles.*.*", "required");                // Every nested value in roles is required.
        rules.put("roles.name", "required");               // The roles map must have a name.
        rules.put("roles.type.name", "required");          // The roles.type map must have a name.

        // Roles.types validations:
        rules.put("roles.types[0].name", "required");      // First item in types must have a name.
        rules.put("roles.types[*].name", "required");        // Every item in types must have a name.
        rules.put("roles.types[1-5].name", "required");      // Items 1 to 5 in types must have a name.

        // Nested maps in users:
        rules.put("users[*].names.*", "required");           // Every value in the names map is required.
        // For demonstration, if values in names were themselves maps, this would validate deeper levels:
        rules.put("users[*].names.*.*", "required");

        List<String> errors = RequestValidator.validate(dto, rules);
        // Expect no errors.
        assertTrue(errors.isEmpty(), "Expected no errors for a valid DTO");
    }

    // -- Integration test: Invalid DTO --
    @Test
    void testInvalidDto() {
        // Build an invalid TestDto object.
        TestDto dto = new TestDto();
        dto.email = "invalid-email";  // invalid email format
        dto.password = "short";       // too short

        // Users list with issues:
        User user1 = new User();
        user1.email = "";             // missing email
        user1.names = Map.of("first", "", "last", "Doe");  // empty 'first' name

        User user2 = new User();
        user2.email = "not-an-email"; // invalid email
        user2.names = Map.of("first", "Jane"); // missing 'last' name (if required)

        dto.users = List.of(user1, user2);

        // Roles map with issues:
        Map<String, Object> roles = new HashMap<>();
        roles.put("name", "");  // empty name
        roles.put("type", Map.of("name", ""));  // empty type name
        roles.put("types", List.of(Map.of("name", "")));  // missing type name in list
        dto.roles = roles;

        // Define the same set of rules as before.
        Map<String, String> rules = new HashMap<>();
        rules.put("email", "required|email");
        rules.put("password", "required|min:8");

        rules.put("users[0].email", "required|email");
        rules.put("users[*].email", "required|email");
        rules.put("users[1-5].email", "required|email");

        rules.put("roles.*", "required");
        rules.put("roles.*.*", "required");
        rules.put("roles.name", "required");
        rules.put("roles.type.name", "required");

        rules.put("roles.types[0].name", "required");
        rules.put("roles.types[*].name", "required");
        rules.put("roles.types[1-5].name", "required");

        rules.put("users[*].names.*", "required");
        rules.put("users[*].names.*.*", "required");

        List<String> errors = RequestValidator.validate(dto, rules);
        // Expect errors to be present.
        assertFalse(errors.isEmpty(), "Expected errors for an invalid DTO");

        // Optionally, print errors for debugging.
        errors.forEach(System.out::println);
    }

    // Sample DTO classes for the integration test:
    public static class TestDto {
        public String email;
        public String password;
        public List<User> users;
        public Map<String, Object> roles;
    }

    public static class User {
        public String email;
        public Map<String, Object> names; // e.g., { "first": "John", "last": "Doe" }
    }
}
