# Validator library
A java validator lib that provides an easy-to-use API much like we have on the popular PHP laravel framework

# Installation
Add the dependency as below. latest version is `1.0.7`
```xml
<dependency>
	<groupId>io.leantony</groupId>
	<artifactId>validator-lib</artifactId>
	<version>1.0.7</version>
</dependency>
```

# Validation Rules Documentation

This document provides a complete reference for all supported validation rules in the library. Each section includes a description of the rule, instructions for adding it to your validation map, and sample Java code demonstrating the actual validation using a basic DTO.

---

## Table of Contents
1. [Basic Validation Rules](#1-basic-validation-rules)  
   - [Required](#11-required)  
   - [Email](#12-email)  
   - [Min](#13-min)  
   - [Max](#14-max)  
   - [Length](#15-length)  
   - [Date](#16-date)  
   - [Alpha](#17-alpha)  
   - [Numeric](#18-numeric)  
   - [Regex](#19-regex)  
   - [Optional / Nullable](#110-optional--nullable)  
   - [AfterOrEqual](#111-afterorequal)  
   - [BeforeOrEqual](#112-beforeorequal)  
   - [MaxSize](#113-maxsize)  
   - [Boolean](#114-boolean)  
   - [SameOrEqual](#115-sameorequal)  
   - [Different](#116-different)  
   - [URL](#117-url)  
   - [NotIn](#118-notin)  
   - [In](#119-in)  
   - [Country](#120-country)  
   - [Currency](#121-currency)  
   - [RequiredWith](#122-requiredwith)  
   - [RequiredWithAny](#123-requiredwithany)  
   - [RequiredWithout](#124-requiredwithout)  
   - [RequiredWithoutAny](#125-requiredwithoutany)  
   - [RequiredIf](#126-requiredif)  
   - [Phoneno](#127-phoneno)  
   - [Enum](#128-enum)  
2. [Complex Use Cases: Nested Validations](#2-complex-use-cases-nested-validations)  
3. [Spring Boot Integration](#3-spring-boot-integration)  
4. [Adding Your Own Custom Rules](#4-adding-your-own-custom-rules)  
5. [Using a Custom ObjectMapper](#5-using-a-custom-objectmapper)  

---

## 1. Basic Validation Rules

For the basic examples below, assume we have the following DTO:

```java
public class UserDto {
    private String name;
    private String email;
    private Integer age;
    private String birthDate;
    private String countryCode;
    private String password;
    private String confirmPassword;
    private String phone;
    private Boolean active;
    // getters and setters omitted for brevity
}
```

Validation is performed by defining a map of rules and then calling the validator (e.g., using `RequestValidator.validate(userDto, rules)`).

### 1.1 Required
**Description:** Ensures the field is present and not `null` or empty.

**Validation Definition:**
```java
rules.put("name", "required");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message if 'name' is missing: ["name is required."]
```

[Back to top](#table-of-contents)

---

### 1.2 Email
**Description:** Validates that the field contains a valid email address.

**Validation Definition:**
```java
rules.put("email", "required|email");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message if email is invalid: ["email must be a valid email address."]
```

[Back to top](#table-of-contents)

---

### 1.3 Min
**Description:**  
Ensures that a field's value is at least the specified minimum. This rule supports:
- **Numeric fields:** The field’s numeric value must be at least the specified number.
- **String fields:** The field’s length must be at least the specified number of characters.
- **Collections/Maps:** The number of elements must be at least the specified count.

**Validation Definitions:**
```java
// Numeric example:
rules.put("age", "required|min:18");

// String example (minimum length 3):
rules.put("username", "required|min:3");

// Collection example (at least one element):
rules.put("tags", "required|min:1");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example errors:
// "age must be at least 18."
// "username must be at least 3 characters long."
// "tags must have at least 1 element."
```

[Back to top](#table-of-contents)

---

### 1.4 Max
**Description:**  
Ensures that a field's value does not exceed the specified maximum. This rule supports:
- **Numeric fields:** The field’s numeric value must be no more than the specified number.
- **String fields:** The field’s length must be no more than the specified number of characters.
- **Collections/Maps:** The number of elements must be no more than the specified count.

**Validation Definitions:**
```java
// Numeric example:
rules.put("age", "required|max:65");

// String example (maximum length 15):
rules.put("username", "required|max:15");

// Collection example (no more than 5 elements):
rules.put("tags", "required|max:5");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example errors:
// "age must be at most 65."
// "username must be at most 15 characters long."
// "tags must have no more than 5 elements."
```

[Back to top](#table-of-contents)

---

### 1.5 Length
**Description:**  
Ensures that a string field's length is exactly the specified number of characters or falls within a specified range.

**Validation Definitions:**
- **Exact Length:**
  ```java
  rules.put("password", "length:8");
  ```
- **Range:**
  ```java
  rules.put("username", "length:3,12");
  ```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example errors:
// "password must be exactly 8 characters long."
// "username must be between 3 and 12 characters long."
```

[Back to top](#table-of-contents)

---

### 1.6 Date
**Description:** Validates that a field is a valid date. The default format is `yyyy-MM-dd`, but a custom format can be specified.

**Validation Definition:**
```java
rules.put("birthDate", "required|date");
rules.put("eventDate", "required|date:dd/MM/yyyy");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message for invalid date: ["birthDate must be a valid date in the format yyyy-MM-dd."]
```

[Back to top](#table-of-contents)

---

### 1.7 Alpha
**Description:** Ensures that the field contains only alphabetic characters (A–Z, a–z).

**Validation Definition:**
```java
rules.put("name", "required|alpha");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["name must contain only alphabetic characters."]
```

[Back to top](#table-of-contents)

---

### 1.8 Numeric
**Description:**  
Validates that a field is a valid number or can be converted to one. Additionally, you can specify a required number of digits by providing a parameter. When a digit count is provided (e.g., `"numeric:10"`), the rule will strip non-digit characters (such as decimal points or signs) and verify that the remaining digits exactly match the specified count.

**Validation Definitions:**

- **Basic Numeric Validation (no digit count):**
  ```java
  rules.put("age", "required|numeric");
  ```

- **Numeric Validation with Digit Count (e.g., exactly 10 digits):**
  ```java
  rules.put("id", "required|numeric:10");
  ```

**Performing Validation Examples:**

- For basic numeric validation:
  ```java
  List<String> errors = RequestValidator.validate(userDto, rules);
  // If 'age' is not numeric, an error like: "age must be numeric." is returned.
  ```

- For numeric validation with digit count:
  ```java
  List<String> errors = RequestValidator.validate(userDto, rules);
  // If 'id' doesn't have exactly 10 digits (ignoring non-digits), an error like:
  // "id must be numeric and contain exactly 10 digits." is returned.
  ```

[Back to top](#table-of-contents)

---

### 1.9 Regex
**Description:** Validates that a field's value matches a specified regular expression pattern.

**Validation Definition:**
```java
rules.put("password", "required|regex:^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
```
_This regex requires at least one letter, one digit, and a minimum of 8 characters._

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["password does not match the required format."]
```

[Back to top](#table-of-contents)

---

### 1.10 Optional / Nullable
**Description:**  
This rule marks a field as optional. If the field is missing, `null`, or empty (after trimming), subsequent validations for that field are skipped. However, if a value is provided, then it must satisfy all the additional rules specified. For example, using `username:optional|length:5` means that if a username is provided, it must be exactly 5 characters long; if not provided, it is considered valid.

**Validation Definition Example:**
```java
rules.put("username", "optional|length:5");
```

**Performing Validation Example:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// - If userDto.username is null or empty, validation passes without further checks.
// - If userDto.username is provided, for instance "Alice", it must have exactly 5 characters.
//   Otherwise, an error message like "username must be exactly 5 characters long." will be returned.
```

[Back to top](#table-of-contents)

---

### 1.11 AfterOrEqual
**Description:** A cross-field rule ensuring that a field's value is after or equal to another field's value (useful for dates or numbers).
> This rule is aliased by `greaterOrEqualTo`

**Validation Definition:**
```java
rules.put("startDate", "required|date");
rules.put("endDate", "required|date|afterOrEqual:startDate");
rules.put("endDate", "required|date|greaterOrEqualTo:startDate"); // same results
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["endDate must be after or equal to startDate."]
```

[Back to top](#table-of-contents)

---

### 1.12 BeforeOrEqual
**Description:** A cross-field rule ensuring that a field's value is before or equal to another field's value.
> This rule is aliased by `lessOrEqualTo`

**Validation Definition:**
```java
rules.put("startDate", "required|date|beforeOrEqual:endDate");
rules.put("startDate", "required|date|lessOrEqualTo:endDate"); // same results
rules.put("endDate", "required|date");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["startDate must be before or equal to endDate."]
```

[Back to top](#table-of-contents)

---

### 1.13 MaxSize
**Description:** For collections or maps, ensures that the number of elements does not exceed the specified maximum.

**Validation Definition:**
```java
rules.put("tags", "required|maxSize:3");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["tags must have no more than 3 elements."]
```

[Back to top](#table-of-contents)

---

### 1.14 Boolean
**Description:** Validates that the field is a boolean value (accepting true/false, \"true\"/\"false\", 1/0, or \"1\"/\"0\").

**Validation Definition:**
```java
rules.put("active", "required|boolean");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["active must be a boolean value."]
```

[Back to top](#table-of-contents)

---

### 1.15 SameOrEqual
**Description:** A cross-field rule ensuring that a field's value is exactly equal to another specified field's value.

**Validation Definition:**
```java
rules.put("password", "required");
rules.put("confirmPassword", "required|sameOrEqual:password");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["confirmPassword must be equal to password."]
```

[Back to top](#table-of-contents)

---

### 1.16 Different
**Description:** A cross-field rule ensuring that a field's value is different from another specified field's value.

**Validation Definition:**
```java
rules.put("oldEmail", "required|email");
rules.put("newEmail", "required|different:oldEmail");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["newEmail must be different from oldEmail."]
```

[Back to top](#table-of-contents)

---

### 1.17 URL
**Description:** Validates that the field contains a properly formatted URL (supports HTTP, HTTPS, and FTP).

**Validation Definition:**
```java
rules.put("website", "required|url");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["website must be a valid URL."]
```

[Back to top](#table-of-contents)

---

### 1.18 NotIn
**Description:** Validates that the field's value is not among a set of disallowed values (specified as a comma-separated list).

**Validation Definition:**
```java
rules.put("status", "required|notIn:pending,cancelled");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["status must not be one of [pending, cancelled]."]
```

[Back to top](#table-of-contents)

---

### 1.19 In
**Description:** Validates that the field's value is one of the allowed values (specified as a comma-separated list).

**Validation Definition:**
```java
rules.put("role", "required|in:admin,user,guest");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["role must be one of [admin, user, guest]."]
```

[Back to top](#table-of-contents)

---

### 1.20 Country
**Description:** Validates that the field is a valid ISO country code (either 2-letter or 3-letter, case-insensitive).

**Validation Definition:**
```java
rules.put("countryCode", "required|country");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["countryCode must be a valid ISO country code."]
```

[Back to top](#table-of-contents)

---

### 1.21 Currency
**Description:** Validates that the field is a valid ISO 4217 currency code (typically a 3-letter code, e.g., USD, EUR).

**Validation Definition:**
```java
rules.put("currency", "required|currency");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["currency must be a valid ISO currency code."]
```

[Back to top](#table-of-contents)

---

### 1.22 RequiredWith
**Description:** The field is required if **all** of the specified other fields (provided as a comma-separated list) are present and non-empty.

**Validation Definition:**
```java
rules.put("emergencyContactName", "requiredWith:emergencyContactPhone,emergencyContactEmail");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["emergencyContactName is required when emergencyContactPhone and emergencyContactEmail are present."]
```

[Back to top](#table-of-contents)

---

### 1.23 RequiredWithAny
**Description:** The field is required if **any** of the specified other fields (provided as a comma-separated list) is present and non-empty.

**Validation Definition:**
```java
rules.put("secondaryEmail", "requiredWithAny:primaryEmail,workEmail");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["secondaryEmail is required when at least one of primaryEmail or workEmail is present."]
```

[Back to top](#table-of-contents)

---

### 1.24 RequiredWithout
**Description:** The field is required if **all** of the specified other fields (provided as a comma-separated list) are absent or empty.

**Validation Definition:**
```java
rules.put("alternatePhone", "requiredWithout:homePhone,mobilePhone");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["alternatePhone is required when both homePhone and mobilePhone are missing."]
```

[Back to top](#table-of-contents)

---

### 1.25 RequiredWithoutAny
**Description:** The field is required if **any** of the specified other fields (provided as a comma-separated list) is absent or empty.

**Validation Definition:**
```java
rules.put("faxNumber", "requiredWithoutAny:officePhone,cellPhone");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["faxNumber is required when either officePhone or cellPhone is missing."]
```

[Back to top](#table-of-contents)

---

### 1.26 RequiredIf
**Description:** The field is required if a specified key's value equals a given value. Parameters must be in the format `key,value`.

**Validation Definition:**
```java
rules.put("discountCode", "requiredIf:membershipLevel,gold");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["discountCode is required because membershipLevel is gold."]
```

[Back to top](#table-of-contents)

---

### 1.27 Phoneno
**Description:** Validates that the field contains a valid phone number using libphonenumber.
> Ensure that you have `libphonenumber` installed as a maven dependency, otherwise this validation rule won't work

- **Default Region Mode:** If no parameter is provided, the rule uses the default region (set to "KE").
- **Allowed Regions Mode:** If a comma-separated list is provided (e.g., `KEN,GH,ZMB`), each token is trimmed and uppercased, then verified against `CountryRule.VALID_COUNTRY_CODES`. The phone number is valid if it is valid in at least one of those regions.
- **Country Field Mode:** If a single parameter is provided that isn’t a comma-separated list, it is treated as a field name from which to obtain the region dynamically.

**Validation Definitions & Usage Examples:**

- **Default Region (KE):**
```java
rules.put("phone", "required|phoneno");
```
**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["phone must be a valid phone number."]
```

- **Allowed Regions (using 3-letter codes):**
> Note that you can mix both 2 and 3-letter codes here. The validator will convert all 3-letter codes to their 2 letter equivalents and pass those to `libphonenumber`

```java
rules.put("phone", "required|phoneno:KEN,GH,ZMB");
```
**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["phone must be a valid phone number for one of the regions: [KEN, GH, ZMB]."]
```

- **Country Field Mode:**
```java
rules.put("phone", "required|phoneno:countryCode");
```
**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["phone must be a valid phone number based on the country code given in the countryCode field."]
```

**Performing Validation from spring boot (with country code in header)**
```java
@RestController
@RequestMapping("/api")
public class PhoneValidationController {

    @PostMapping("/validatePhone")
    public ResponseEntity<?> validatePhone(
            @RequestHeader("X-Country-Code") String countryCode, // assume its KE or KEN given
            @RequestBody Map<String, Object> requestBody) {
        
        // Build the validation rules map dynamically using the header value.
        Map<String, String> rules = new HashMap<>();
        rules.put("phone", "required|phoneno:" + countryCode);
        
        // Validate the request body.
        List<String> errors = RequestValidator.validate(requestBody, rules);
        if (!errors.isEmpty()) {
            // Example error message: ["phone must be a valid phone number for one of the regions: [KE]."]
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok("Validation passed");
    }
}
```
[Back to top](#table-of-contents)

---

### 1.28 Enum
**Description:**  
Validates that a field's value is contained within the values of an existing enum class. You must specify the fully qualified class name of the enum as a parameter. The rule uses reflection to check if the field's value matches one of the enum constants.

**Validation Definition:**
```java
rules.put("role", "required|enum:com.example.RoleEnum");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error message: ["role must be one of [ADMIN, USER, GUEST]."]
```

[Back to top](#table-of-contents)

---

## 2. Complex Use Cases: Nested Validations

For nested objects and collections, you can use dot notation, index notation for lists, and wildcard notation for maps.

### 2.1 Nested Objects
Assume we have the following DTO:

```java
public class Address {
    private String street;
    private String city;
    private String country;
    // getters and setters omitted for brevity
}

public class UserDto {
    private String name;
    private Address address;
    // getters and setters omitted for brevity
}
```

**Validation Definition:**
```java
Map<String, String> rules = new HashMap<>();
rules.put("address.street", "required");
rules.put("address.city", "required|alpha");
```

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error: ["address.street is required."]
```

### 2.2 List Index Notation
Assume the DTO has a list of addresses:

```java
public class UserDto {
    private String name;
    private List<Address> addresses;
    // getters and setters omitted for brevity
}
```

**Validation Definitions:**
- **Single Index:**
  ```java
  rules.put("addresses[0].street", "required");
  ```
  _Ensures that the first address has a street._

- **Wildcard Index:**
  ```java
  rules.put("addresses[*].street", "required");
  ```
  _Ensures that every address in the list has a street._

- **Range Notation:**
  ```java
  rules.put("addresses[1-5].street", "required");
  ```
  _Ensures that addresses 1 through 5 (if present) have a street._

**Performing Validation:**
```java
List<String> errors = RequestValidator.validate(userDto, rules);
// Example error: ["addresses[0].street is required."] if the first address's street is missing.
```

### 2.3 Wildcard Notation for Maps
**Validation Definition (Wildcard Notation):**
Assume the DTO contains a map of roles:

```java
public class UserDto {
    private String name;
    private Map<String, Object> roles;
    // getters and setters omitted for brevity
}
```

**Validation Definitions:**
- **Top-level Map Wildcard:**
  ```java
  rules.put("roles.*", "required");
  ```
  _Ensures that every value in the roles map is present._

- **Nested Wildcard:**
  ```java
  rules.put("roles.*.*", "required");
  ```
  _Ensures that for each key in the roles map, every nested value is present._

- **Combined Dot and Wildcard:**
  ```java
  rules.put("roles.type.name", "required");
  ```
  _Ensures that within the roles map, the `type` object has a `name` field._

- **List inside Map (with Wildcard and Range):**  
  If a role contains a list of types:
  ```java
  rules.put("roles.types[0].name", "required");   // First item in types must have a name.
  rules.put("roles.types[*].name", "required");     // Every item in types must have a name.
  rules.put("roles.types[1-5].name", "required");   // Types 1 to 5 in roles must have a name.
  ```

[Back to top](#table-of-contents)

---

## 3. Spring Boot Integration

Create a simple REST controller and call the validator:

```java
@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestBody UserDto userDto) {
        Map<String, String> rules = new HashMap<>();
        rules.put("email", "required|email");
        rules.put("password", "required|min:8");
        List<String> errors = RequestValidator.validate(userDto, rules);
        // Example error if email is invalid: ["email must be a valid email address."]
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok("Validation passed");
    }
}
```

If you want to use a custom `ObjectMapper`, create a bean of type `ValidationHelper` and call its `validate` method:

```java
@Bean
public ValidationHelper validationHelper(ObjectMapper objectMapper) {
    return new ValidationHelper(objectMapper);
}
```

Then inject and use it:

```java
@Autowired
private ValidationHelper validationHelper;

public List<String> validateUser(UserDto userDto) {
    Map<String, String> rules = new HashMap<>();
    rules.put("email", "required|email");
    rules.put("password", "required|min:8");
    List<String> errors = validationHelper.validate(userDto, rules);
    // Example error if email is invalid: ["email must be a valid email address."]
    return errors;
}
```

[Back to top](#table-of-contents)

---

## 4. Adding Your Own Custom Rules
Adding custom rules is pretty simple. See an example below that creates a rule that validates a DB entry as being unique

### 4.1 Add a JPA Repository or Use Your Own

```java
public interface UserRepository extends JpaRepository<User, Long> {
    long countByEmail(String email);
}
```

### 4.2 Create the Custom Validation Rule

```java
public class UniqueEmailRule extends BaseRule {
    private final UserRepository userRepository;
    
    public UniqueEmailRule(UserRepository userRepository, String parameter) {
        this.userRepository = userRepository;
    }
    
    @Override
    public boolean validate(String field, Object value, Map<String, Object> data) {
        if (!(value instanceof String)) return false;
        String email = ((String) value).trim();
        if (email.isEmpty()) return false;
        long count = userRepository.countByEmail(email);
        return count == 0;
    }
    
    @Override
    public String getErrorMessage(String field, Object value, Map<String, Object> data) {
        return field + " must be unique. The email already exists.";
    }
}
```

### 4.3 Register the Custom Validation Rule

```java
@Configuration
public class CustomValidationConfiguration {
    public CustomValidationConfiguration(UserRepository userRepository) {
        RuleRegistry.register("uniqueEmail", param -> new UniqueEmailRule(userRepository, param));
    }
}
```

### 4.4 Using Your Custom Validation Rule

```java
Map<String, String> ruleDefs = new HashMap<>();
ruleDefs.put("email", "required|email|uniqueEmail");
List<String> errors = RequestValidator.validate(userDto, ruleDefs);
// Example error if email is already in use: ["email must be unique. The email already exists."]
```

[Back to top](#table-of-contents)

---

## 5. Using a Custom ObjectMapper

By default, the validation system uses a standard Jackson `ObjectMapper`. If you require a custom instance, create a bean of type `ValidationHelper` and call its `validate` method manually.

**Configuration Example:**

```java
@Configuration
public class ValidationConfig {
    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    
    @Bean
    public ValidationHelper validationHelper(ObjectMapper customObjectMapper) {
        return new ValidationHelper(customObjectMapper);
    }
}
```

**Usage in a Service or Controller:**

```java
@Autowired
private ValidationHelper validationHelper;

public List<String> validateUser(UserDto userDto) {
    Map<String, String> rules = new HashMap<>();
    rules.put("email", "required|email");
    rules.put("password", "required|min:8");
    List<String> errors = validationHelper.validate(userDto, rules);
    // Example error if email is invalid: ["email must be a valid email address."]
    return errors;
}
```

[Back to top](#table-of-contents)