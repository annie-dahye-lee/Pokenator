# How to Show Evidence of 100% Code Coverage

## Step 1: Generate Coverage Report

Run this command:
```bash
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest
```

## Step 2: Open the Coverage Report

```bash
open target/site/jacoco/index.html
```

## Step 3: Navigate to Your Class

1. In the coverage report, click on **`use_case`**
2. Then click on **`user_profile`**
3. Then click on **`UserProfileInteractor.java`**

## Step 4: Take Screenshots

### Screenshot Option 1: Summary Table (Best for Evidence)

1. You'll see a table with coverage metrics
2. Scroll to the bottom row labeled **"Total"**
3. Take a screenshot showing:
   - **Instructions**: 100% (0 of 191 missed)
   - **Branches**: 97% (1 of 36 missed) 
   - **Lines**: 100% (0 of 54 missed)
   - **Methods**: 100% (0 of 2 missed)

### Screenshot Option 2: Source Code View

1. Click on **`UserProfileInteractor.java.html`** (the source code link)
2. You'll see the source code with:
   - **Green background** = Covered lines
   - **Red background** = Not covered (if any)
   - **Yellow diamonds** = Partially covered branches
3. Take a screenshot showing the code with green highlighting

### Screenshot Option 3: Package Index View

1. Go back to `target/site/jacoco/index.html`
2. Find the row for `use_case.user_profile.UserProfileInteractor`
3. Take a screenshot of that table row showing the coverage percentages

## Step 5: Direct Link to Your Class Report

You can also directly open:
```bash
open target/site/jacoco/use_case.user_profile/UserProfileInteractor.html
```

This shows the summary table immediately.

## Current Coverage Metrics

- ✅ **Instructions**: 100% (0 of 191 missed)
- ⚠️ **Branches**: 97% (1 of 36 missed) - This is very close to 100%
- ✅ **Lines**: 100% (0 of 54 missed)
- ✅ **Methods**: 100% (0 of 2 missed)

## For Documentation/Report

You can say:
- "Achieved 100% line coverage, 100% method coverage, and 97% branch coverage for UserProfileInteractor"
- Include screenshots of:
  1. The summary table showing 100% line coverage
  2. The source code view showing all lines highlighted in green
  3. The test results showing all 18 tests passing

## Quick Command to Open Everything

```bash
# Generate report and open
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest && open target/site/jacoco/use_case.user_profile/UserProfileInteractor.html
```

