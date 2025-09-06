This bundle adds safe, additive utilities for your DeepSeek integration in SecondMind:

- .gitignore additions to keep real secrets out of Git
- config/deepseek.env.example for local, non-committed secrets
- tools/ops_undo.py to restore files touched by the last agent apply
- scripts/apply_deepseek_hardening.sh to install these in your repo (idempotent)

How to apply (from your repo root):
  unzip /path/to/deepseek-hardening-bundle.zip -d .
  bash scripts/apply_deepseek_hardening.sh

After applying:
  cp config/deepseek.env.example config/deepseek.env
  # fill values in config/deepseek.env (do NOT commit this file)
