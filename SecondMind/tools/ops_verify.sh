#!/usr/bin/env bash
set -euo pipefail
if [[ -x ./tools/sanity.sh ]]; then
  ./tools/sanity.sh
else
  ./gradlew :app:assembleDebug -x test
fi
echo "VERIFY OK"
