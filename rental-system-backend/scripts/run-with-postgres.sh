#!/usr/bin/env bash
set -euo pipefail

# Load .env.local (dev) first, then fall back to .env
if [ -f .env.local ]; then
  echo "Loading .env.local"
  export $(grep -v '^#' .env.local | xargs)
elif [ -f .env ]; then
  echo "Loading .env"
  export $(grep -v '^#' .env | xargs)
fi

# Ensure profile is set
export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-postgres}

echo "Starting application with profile: $SPRING_PROFILES_ACTIVE"

./gradlew bootRun --no-daemon --args="--spring.profiles.active=$SPRING_PROFILES_ACTIVE"

