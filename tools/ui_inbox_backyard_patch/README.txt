SecondMind – Inbox On-Demand AI + Backyard Scaffolding (Reception vs Store Room)

What this patch does
--------------------
1) Adds an AI FAB on the Inbox screen. Tap → gated snapshot → digest dialog → gate off.
   - No background scraping. AI sees only a sanitized, batched snapshot (optional DeepSeek refine).

2) Adds Backyard scaffolding:
   - reception/ (ephemeral ring buffer stubs; NOT AI-visible)
   - storeroom/ (curated facts/entities; stub repo)
   - policy/ (allowlist + sanitizer)
   - gateway/ (read-only allowlisted interface for AI)
   - ui/ (PromoteSheet stub)

3) Patches MainActivity.kt to overlay the FAB at the Inbox route (idempotent).
4) Patches SecondMindNotificationListener.kt to respect the gate and to snapshot on rebind (idempotent).

How to apply
------------
1) Copy this zip to your Android Downloads folder.

2) Run (Termux):
   bash -lc '
     set -Eeuo pipefail; set +H
     cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")" || { echo "❌ repo not found"; exit 1; }
     ZIP=""
     for d in "$HOME/storage/downloads" "/storage/emulated/0/Download" "."; do
       cand=$(ls -1t "$d"/ui-inbox-backyard-patch-*.zip 2>/dev/null | head -n1 || true)
       [ -n "$cand" ] && ZIP="$cand" && break
     done
     [ -n "$ZIP" ] || { echo "❌ ui-inbox-backyard-patch-*.zip not found"; exit 1; }
     echo "→ Using ZIP: $ZIP"
     unzip -oq "$ZIP" -d .
     # fix nested tools/tools if any
     if [ -d tools/tools/ui_inbox_backyard_patch ]; then
       rm -rf tools/ui_inbox_backyard_patch 2>/dev/null || true
       mv tools/tools/ui_inbox_backyard_patch tools/
       rmdir tools/tools 2>/dev/null || true
     fi
     bash tools/ui_inbox_backyard_patch/apply.sh
   '

Notes
-----
- Idempotent: re-running keeps the same result.
- Stages changes only. No local build, no push.
- If the listener path differs in your repo, adjust apply.sh LISTENER variable.
