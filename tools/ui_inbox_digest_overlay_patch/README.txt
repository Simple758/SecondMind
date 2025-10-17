UI Inbox Digest Overlay Patch
-----------------------------

Contents:
- app/src/main/java/com/secondmind/minimal/ui/InboxOverlay.kt
- tools/ui_inbox_digest_overlay_patch/apply.sh  (idempotent)

What it does:
- Adds a FloatingActionButton overlay to the Inbox screen that, when tapped:
  * If notification access is not enabled, opens system settings.
  * If enabled, gates notification intake (InboxGate.active = true), triggers a one-shot
    rebind of the NotificationListener, calls NotificationSummarizer.summarizeOnce(ctx),
    shows the digest in an in-app dialog, then turns the gate OFF again.

- Wires the overlay into MainActivity’s `composable("inbox")` route by injecting:
    // InboxAIOverlay
    com.secondmind.minimal.ui.InboxAIOverlay()

Safe to apply repeatedly (idempotent). No commit or push is performed.
