SecondMind – UI Wiki Patch
==========================

What this does
--------------
- Adds a new in-app Wiki screen (NavigationRoutes.WIKI = "wiki").
- Inserts a NavHost destination composable for "wiki".
- Adds WikiScreen.kt (Compose + TTS on tap), WikiViewModel.kt (simple VM).
- Extends existing feature/wiki/WikiRepo.kt with search + summary + simple cache.
- Tries to wire Home’s Wiki card to navigate to the new screen.
- Idempotent patch; shows before/after excerpts; stages files but does not commit.

How to apply
------------
From the repo root:
  bash tools/ui_wiki_patch/apply.sh

Safety
------
- The script is idempotent.
- If it cannot safely change WikiCard.kt, it prints a warning and leaves it as-is.
- No manifest or dependency changes.
