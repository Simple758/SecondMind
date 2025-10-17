UI Drawer AI item patch
-----------------------
What it does:
- Ensures the AI item is listed in the drawer (above Developer).
- Uses route string "ai" so it matches MainActivity's composable(\"ai\").
- Adds SmartToy import if missing.
- Idempotent: safe to run multiple times.

Usage (Termux):
  cd ~/SecondMind
  unzip -o ~/storage/downloads/ui-ai-drawer-item-patch-*.zip -d tools 2>/dev/null || unzip -o /storage/emulated/0/Download/ui-ai-drawer-item-patch-*.zip -d tools
  bash tools/ui_ai_drawer_item_patch/apply.sh

Then push:
  git commit -m "UI: add AI item to drawer" && git push origin HEAD:main
