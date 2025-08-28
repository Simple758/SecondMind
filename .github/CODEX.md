# SecondMind – Codex Patch Rules
Guardrails above all: compile-only, tiny diffs, no version bumps, no experimental APIs.
If a change needs navigation, pass lambdas from `AppNav()`; do not reference `nav` outside it.
If adding screens, gate entry with `FeatureToggles` booleans so main stays green even when code lands.
When unsure, create a minimal stub and TODO. Your diff must compile with `:app:assembleDebug`.
