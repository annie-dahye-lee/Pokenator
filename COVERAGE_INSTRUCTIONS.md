# How to Run Tests and View Code Coverage for UserProfileInteractor

## Running Tests (Following Project Conventions)

Other contributors in this repo run tests using:
```bash
mvn test -Dtest=TestClassName
```

For UserProfileInteractor:
```bash
mvn test -Dtest=UserProfileInteractorTest
```

Or use the script:
```bash
./run-coverage.sh
```

## Code Coverage (Optional)

To generate a code coverage report:
```bash
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest
```

2. **Open the coverage report:**
   ```bash
   open target/site/jacoco/index.html
   ```

## Finding Your Class Coverage

1. Open `target/site/jacoco/index.html` in your browser
2. Navigate to: **use_case** → **user_profile** → **UserProfileInteractor.java**
3. You'll see:
   - **Line coverage**: Shows which lines are covered (green) and which are not (red)
   - **Branch coverage**: Shows which branches (if/else) are covered
   - **Method coverage**: Shows which methods are covered

## Taking a Screenshot

### Option 1: Screenshot the HTML Report
1. Open `target/site/jacoco/use_case.user_profile/UserProfileInteractor.java.html`
2. Take a screenshot showing:
   - The coverage summary table at the top
   - The source code with green (covered) and red (not covered) highlighting

### Option 2: Screenshot the Index Page
1. Open `target/site/jacoco/index.html`
2. Navigate to the UserProfileInteractor row
3. Take a screenshot showing the coverage percentages

## Coverage Details

The report shows:
- **Instructions**: Number of bytecode instructions
- **Branches**: Number of branches (if/else statements)
- **Lines**: Number of lines of code
- **Methods**: Number of methods
- **Classes**: Number of classes

For 100% coverage, all metrics should show 100% (or 0 missed).

## Command Line Summary

You can also see a summary in the terminal:
```bash
mvn test jacoco:report -Dtest=UserProfileInteractorTest | grep -A 10 "UserProfileInteractor"
```

