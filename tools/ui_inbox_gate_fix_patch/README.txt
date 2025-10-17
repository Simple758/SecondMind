ui_inbox_gate_fix_patch
-----------------------
This patch:
1) Replaces SecondMindNotificationListener with a gated version (checks InboxGate.active).
2) Adds a FloatingActionButton overlay to the inbox route in MainActivity (idempotent), with a SmartToy icon.
   Tap = set InboxGate.active=true and request rebind so the listener snapshots current notifications.

Privacy: No continuous background reads; snapshot only while gate is active.
Build/Push: Not triggered. Files are staged; you decide when to commit/push.
