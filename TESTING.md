# Testing Guide for UserProfileInteractor

## How to Run Tests (Following Project Conventions)

This project follows the standard Maven testing pattern used by other contributors:

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=UserProfileInteractorTest
```

### Run with Script
```bash
./run-coverage.sh
```

## Test Structure

Tests follow the same pattern as other test files in the project:
- Located in `src/test/java/use_case/user_profile/`
- Use JUnit 5 (`@Test` annotations)
- Use anonymous classes for presenters (like `LoginInteractorTest`, `SignupInteractorTest`)
- Use `InMemoryUserDataAccessObject` or custom test implementations for data access

## Example: How Other Contributors Test

Looking at `LoginInteractorTest.java` and `SignupInteractorTest.java`, they:
1. Create input data
2. Set up a repository (usually `InMemoryUserDataAccessObject`)
3. Create an anonymous presenter that asserts expected behavior
4. Create the interactor and execute
5. Assertions are made in the presenter callbacks

## Code Coverage (Optional)

To generate a code coverage report:
```bash
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest
open target/site/jacoco/index.html
```

Navigate to: `use_case` → `user_profile` → `UserProfileInteractor.java`

## Current Test Coverage

- **18 test cases** covering all success and failure scenarios
- **100% line coverage**
- **97% branch coverage** (1 edge case branch)
- **100% method coverage**

