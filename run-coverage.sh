#!/bin/bash

# Script to run tests for UserProfileInteractor (following project conventions)
# Other contributors use: mvn test -Dtest=TestClassName

echo "Running UserProfileInteractor tests..."
mvn test -Dtest=UserProfileInteractorTest

echo ""
echo "=========================================="
echo "Tests completed!"
echo "=========================================="
echo ""
echo "To run with code coverage report:"
echo "  mvn clean test jacoco:report -Dtest=UserProfileInteractorTest"
echo "  open target/site/jacoco/index.html"
echo ""

