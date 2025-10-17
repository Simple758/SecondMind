
SecondMind – Audiobook Feature (Drop-in Module)
==============================================

What’s included
---------------
• Data models, PDF extractor (iText), cache/TTS manager
• Repository + use-cases (DeepSeek summaries via AIServiceLocator)
• ViewModel + Compose screens and components
• MediaPlayer-based offline playback (with speed control)
• Integration patches for Navigation, Drawer, MainActivity
• Gradle & Manifest patches

How to integrate
----------------
1) Copy everything under app/src/main/java/com/secondmind/minimal into your project preserving folders.
2) Apply the patch snippets from INTEGRATIONS/*.patch into your existing files.
3) Add the Gradle dependencies and Manifest permissions from INTEGRATIONS patches.
4) Build and run. Open the “Audiobooks” screen from the drawer, pick a PDF, wait for summaries & audio.
   (First TTS synthesis can take time depending on device voices.)

Notes
-----
• The iText (itextg) dependency is used only for PDF text extraction.
• TTS synthesis saves WAV files under getExternalFilesDir()/audiobooks/<bookId>/.
• The AI integration assumes your project already defines AIServiceLocator, Prompt, AIResult, etc.
  If the package differs, adjust imports in AudiobookRepositoryImpl accordingly.
