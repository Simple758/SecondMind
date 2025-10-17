SecondMind — Inbox overlay + Wiki route/card fix (idempotent)

What this patch does
- Rewrites the Inbox overlay block cleanly between `// InboxAIOverlay` and the next `composable("news")`.
- Ensures `composable("wiki") { WikiScreen() }` exists (inserted right after the News route).
- Replaces WikiBrainFoodCard with a minimal, compile-clean version that navigates in-app.
- Prints brace-balance for MainActivity so you can verify {=N }=N.

How to apply
1) Copy zip to your repo root or Downloads.
2) Run the robust apply command from the chat message.
