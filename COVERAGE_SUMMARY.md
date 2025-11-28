# Code Coverage Summary for UserProfileInteractor

## Current Coverage Status

✅ **Instructions**: 100% (0 of 191 missed)  
⚠️ **Branches**: 97% (1 of 36 missed)  
✅ **Lines**: 100% (0 of 54 missed)  
✅ **Methods**: 100% (0 of 2 missed)

## How to View Coverage Report

### Method 1: Using the Script
```bash
./run-coverage.sh
```
This will:
- Run all tests
- Generate the coverage report
- Open it in your browser automatically

### Method 2: Manual Commands
```bash
# Run tests and generate report
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest

# Open the report
open target/site/jacoco/index.html
```

## Finding Your Coverage

1. **Open**: `target/site/jacoco/index.html`
2. **Navigate**: Click through: `use_case` → `user_profile` → `UserProfileInteractor.java`
3. **View Details**: Click on `UserProfileInteractor.java.html` to see line-by-line coverage

## Taking a Screenshot

### Screenshot 1: Summary Table
1. Open `target/site/jacoco/use_case.user_profile/UserProfileInteractor.html`
2. Scroll to the "Total" row at the bottom
3. Take a screenshot showing:
   - Element: Total
   - Coverage percentages for Instructions, Branches, Lines, Methods

### Screenshot 2: Source Code with Coverage
1. Open `target/site/jacoco/use_case.user_profile/UserProfileInteractor.java.html`
2. You'll see:
   - **Green background** = Covered lines
   - **Red background** = Not covered lines
   - **Yellow diamond** = Partially covered branches
3. Take a screenshot showing the source code with coverage highlighting

### Screenshot 3: Package View
1. Open `target/site/jacoco/index.html`
2. Find the row for `use_case.user_profile.UserProfileInteractor`
3. Take a screenshot showing the coverage percentages in the table

## Coverage Details

The report shows:
- **Instructions**: Bytecode instructions executed
- **Branches**: If/else branches covered (97% - 1 branch not covered)
- **Lines**: Lines of code executed
- **Methods**: Methods executed

## Note on Branch Coverage

You have 97% branch coverage (1 branch missed). This is likely a rare edge case branch. To achieve 100%:
1. Open the source code view (`UserProfileInteractor.java.html`)
2. Look for yellow diamonds (partially covered branches)
3. Add a test case to cover that specific branch condition

## Files Generated

- `target/site/jacoco/index.html` - Main coverage report
- `target/site/jacoco/use_case.user_profile/UserProfileInteractor.html` - Class summary
- `target/site/jacoco/use_case.user_profile/UserProfileInteractor.java.html` - Source code with coverage

