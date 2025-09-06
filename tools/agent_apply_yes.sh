#!/usr/bin/env bash
set -euo pipefail
if command -v yes >/dev/null 2>&1; then
  yes y | tools/agentctl apply
else
  printf "y\n" | tools/agentctl apply
fi
