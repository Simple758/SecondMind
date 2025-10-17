#!/usr/bin/env bash
set -Eeuo pipefail; set +H
WF=".github/workflows/android-apk.yml"
[ -f ".github/workflows/android-apk.yml" ] || { echo "❌ .github/workflows/android-apk.yml not found"; exit 1; }

cp -f ".github/workflows/android-apk.yml" ".github/workflows/android-apk.yml.bak"

awk 
