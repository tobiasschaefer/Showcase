#!/usr/bin/env bash
set -e # Exit with nonzero exit code if anything fails

START_TIME=$SECONDS

echo -n "Java Version: " && java -version
echo -n "Javac Version: " && javac -version
./mvnw install --batch-mode

echo "Directory content after build:"
ls -al

ELAPSED_TIME=$(($SECONDS - $START_TIME))
echo "API Gateway Build & test duration: $ELAPSED_TIME seconds"
