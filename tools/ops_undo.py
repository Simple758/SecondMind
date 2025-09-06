
#!/usr/bin/env python3

Restore files touched by the last agent apply, using .ops_receipts/last.json and per-file snapshots.
The snapshots are expected at .ops_receipts/snapshots/<path_with__>.orig

import json, pathlib, sys

ROOT = pathlib.Path(".").resolve()
rcp = ROOT/".ops_receipts/last.json"
snaps = ROOT/".ops_receipts/snapshots"

if not rcp.exists():
    print("No last receipt found at", rcp)
    sys.exit(2)

try:
    plan = json.loads(rcp.read_text(encoding="utf-8"))
except Exception as e:
    print("Failed to read last receipt:", e)
    sys.exit(3)

touched = []
for step in plan.get("steps", []):
    path = step.get("file")
    if not path:
        continue
    snap = snaps/(path.replace("/", "__") + ".orig")
    if snap.exists():
        dst = ROOT/pathlib.Path(path)
        dst.parent.mkdir(parents=True, exist_ok=True)
        dst.write_text(snap.read_text(encoding="utf-8"), encoding="utf-8")
        touched.append(path)

if not touched:
    print("No snapshots found to restore.")
else:
    print("Restored files:")
    for t in sorted(set(touched)):
        print(" -", t)
