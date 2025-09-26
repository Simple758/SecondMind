#!/usr/bin/env bash
set -Eeuo pipefail
set +H

# --- Locate files ---
cd "/data/data/com.termux/files/home/SecondMind"
MAIN="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
if [ -z "" ]; then
  MAIN="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
fi
[ -n "" ] || { echo "❌ MainActivity.kt not found under app/src/main/java"; exit 1; }

TARGET=402
WSTART=-10; [  -lt 1 ] && WSTART=1
WEND=10
tot=; [ "" -gt "" ] && WEND=""

show_window () { sed -n ",p" "" | nl -ba; }

echo "== BEFORE (-) =="
show_window

# --- Step 1: normalize quotes & invisible chars (safe/idempotent) ---
if command -v perl >/dev/null 2>&1; then
  tmp="/data/data/com.termux/files/usr/tmp/tmp.v6l6q89XPv"
  perl -CSDA -pe tr/x{2018}x{2019}/x27x27/
