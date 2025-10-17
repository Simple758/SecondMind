UI + AI patch pack
====================

This pack adds:
1) DeveloperActivity (opened via drawer "Developer") with a **Ping DeepSeek** button.
2) AiChatActivity with a simple chat UI and TTS.
3) MainActivity routes: "developer" and "ai" that **launch the activities**.
4) DrawerContent gets a new "AI" item **above Developer** (if DrawerContent.kt is found).
5) Manifest entries for both activities.

How to apply
------------
1) Copy this zip to your phone's Downloads.
2) In Termux, run:

   bash -lc '
     cd ~/SecondMind
     unzip -o ~/storage/downloads/ui-ai-dev-patch-*.zip -d tools 2>/dev/null || unzip -o /storage/emulated/0/Download/ui-ai-dev-patch-*.zip -d tools
     bash tools/ui_ai_dev_patch/apply.sh
   '

The script is idempotent and will not duplicate code if run again.
