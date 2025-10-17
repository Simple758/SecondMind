# CI DeepSeek Secret Patch

What this does
--------------
* Ensures your GitHub Actions workflow (`.github/workflows/android-apk.yml`)
  has a step named **"Inject DeepSeek key"** and that the `run: |` block is
  correctly indented so YAML parses it.
* If the step is missing, it inserts it right after the `steps:` line.
* If the step exists, it replaces it with a correctly indented block.

How to apply
------------
1) Copy this folder (or the zip) to your repo, then run:

    bash tools/ci_deepseek_secret_patch/apply.sh

2) Commit & push to trigger CI:

    git commit -m "CI: fix DeepSeek secret injection"
    git push

Notes
-----
* The script is idempotent—safe to run multiple times.
* It does **not** build locally; it only patches the workflow and stages it.
