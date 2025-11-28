#!/bin/bash

# Quick script to generate and show code coverage evidence

echo "Generating code coverage report..."
mvn clean test jacoco:report -Dtest=UserProfileInteractorTest

echo ""
echo "=========================================="
echo "Coverage Report Generated!"
echo "=========================================="
echo ""
echo "Opening coverage report in browser..."
echo ""

# Open the specific class report (shows summary table immediately)
open target/site/jacoco/use_case.user_profile/UserProfileInteractor.html

echo "Screenshot Instructions:"
echo "1. Take a screenshot of the 'Total' row at the bottom of the table"
echo "2. It should show:"
echo "   - Instructions: 100% (0 of 191 missed)"
echo "   - Branches: 97% (1 of 36 missed)"
echo "   - Lines: 100% (0 of 54 missed)"
echo "   - Methods: 100% (0 of 2 missed)"
echo ""
echo "For source code view, click on 'UserProfileInteractor.java.html'"
echo ""

