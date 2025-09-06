#!/usr/bin/env bash
set -euo pipefail
WF_NAME="${CI_WORKFLOW_NAME:-Android APK}"
cmd="${1:-help}"
case "$cmd" in
  recent) gh run list --workflow "$WF_NAME" -L "${2:-5}" ;;
  view)
    RUN_ID="${2:-latest}"
    if [[ "$RUN_ID" == "latest" ]]; then
      RUN_ID=$(gh run list --workflow "$WF_NAME" -L 1 --json databaseId --jq '.[0].databaseId')
    fi
    echo "Run: $RUN_ID"
    gh run view "$RUN_ID"
    echo "---- tail logs (last 150 lines) ----"
    gh run view "$RUN_ID" --log | tail -n 150
    ;;
  artifact)
    RUN_ID="${2:-latest}"; DEST="${3:-./artifacts}"
    mkdir -p "$DEST"
    if [[ "$RUN_ID" == "latest" ]]; then
      RUN_ID=$(gh run list --workflow "$WF_NAME" -L 1 --json databaseId --jq '.[0].databaseId')
    fi
    gh run download "$RUN_ID" -D "$DEST"
    echo "Artifacts downloaded to $DEST"
    ;;
  *) echo "Usage: tools/ci.sh {recent|view [RUN_ID]|artifact [RUN_ID] [DEST]}"; exit 2;;
esac
