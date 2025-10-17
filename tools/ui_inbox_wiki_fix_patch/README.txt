SecondMind – Inbox overlay & Wiki fixes
--------------------------------------

What it does
- Replaces the broken // InboxAIOverlay block with a valid composable("inbox") overlay FAB that triggers a gated snapshot.
- Inserts composable("wiki") after the first composable("news") if missing.
- Drops in a safe WikiBrainFoodCard (no syntax traps).
- Ensures NavigationRoutes.WIKI constant exists.
- Backs up touched files as *.bak.YYYYMMDD-HHMMSS
- Idempotent: re-running is safe.

Apply:
  unzip -oq ui-inbox-wiki-fix-patch-*.zip -d .
  bash tools/ui_inbox_wiki_fix_patch/apply.sh
  # then: git commit -m "Fix: inbox overlay route + safe Wiki card" && git push
