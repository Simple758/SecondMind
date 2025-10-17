This patch fixes two things:
1) Repairs the Inbox overlay block in MainActivity.kt with a valid composable("inbox") {} overlay and balanced braces.
2) Ensures a wiki destination exists and provides a safe WikiBrainFoodCard that navigates in-app.

Usage:
1) Save the zip to Downloads.
2) Run the robust apply command I provided in chat.
3) Review staged diff, then `git commit -m "Fix: inbox overlay + wiki card"` and push (if desired).

Idempotent: safe to re-run; backs up touched files as .bak.<timestamp>.
