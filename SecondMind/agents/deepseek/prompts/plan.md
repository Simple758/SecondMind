Task: Convert the user's instruction + context into a minimal Ops Plan.

Constraints:
- Only use allowed ops.
- Include guards to avoid duplicate imports/blocks.
- Target exact anchors: after/before strings that already exist in snippets provided.
- Prefer single-responsibility steps.

Output: Valid JSON, no prose.
