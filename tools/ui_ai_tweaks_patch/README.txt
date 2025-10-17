UI AI Tweaks Patch
==================

What this does:
- AI Chat screen uses AMOLED-black background (like Home).
- Developer screen uses AMOLED-black background.
- AI Chat no longer auto-speaks responses.
- Each assistant message has a speaker icon; tap to play TTS for that message.
- Model selector dropdown at the top (defaults to deepseek-chat; includes deepseek-reasoner).

Apply:
------
From repo root:

  unzip -o ~/storage/downloads/ui-ai-tweaks-*.zip -d . 2>/dev/null ||   unzip -o /storage/emulated/0/Download/ui-ai-tweaks-*.zip -d .

  bash tools/ui_ai_tweaks_patch/apply.sh

This script overwrites the two files, shows a diff, and stages them.
