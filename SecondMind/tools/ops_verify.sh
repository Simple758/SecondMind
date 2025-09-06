#!/usr/bin/env bash
set -euo pipefail

# If no SDK configured, skip local verify and rely on CI
if [[ ! -f ./local.properties && -z "${ANDROID_HOME:-}" && -z "${ANDROID_SDK_ROOT:-}" ]]; then
  echo "LOCAL VERIFY SKIPPED: No Android SDK detected. Using CI verification."
  exit 0
fi

if [[ -x ./tools/sanity.sh ]]; then
  ./tools/sanity.sh
else
  ./gradlew :app:assembleDebug -x test
fi
echo "VERIFY OK"
