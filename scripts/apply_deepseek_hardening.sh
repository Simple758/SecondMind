
    #!/usr/bin/env bash
    set -euo pipefail

    ROOT="$(pwd)"
    TS="$(date +%Y%m%d-%H%M%S)"
    BACKUP_DIR=".hardening_backup/$TS"
    mkdir -p "$BACKUP_DIR"

    echo "== SecondMind DeepSeek hardening apply =="
    echo "Repo root: $ROOT"

    # 1) Append .gitignore rules idempotently
    if [[ -f ".gitignore" ]]; then
      if ! grep -q "deepseek-hardening" .gitignore; then
        echo "Updating .gitignore ..."
        cat >> .gitignore <<'GITIGNORE_ADD'
    # == agent & secrets (added by deepseek-hardening) ==
config/*.env
!config/*.env.example
.ops_receipts/
.hardening_backup/

    GITIGNORE_ADD
      else
        echo ".gitignore already contains deepseek-hardening rules"
      fi
    else
      echo "Creating .gitignore ..."
      cat > .gitignore <<'GITIGNORE_ADD'
    # == agent & secrets (added by deepseek-hardening) ==
config/*.env
!config/*.env.example
.ops_receipts/
.hardening_backup/

    GITIGNORE_ADD
    fi

    # 2) Ensure config/deepseek.env.example exists (backup if overwritten)
    mkdir -p config
    if [[ -f "config/deepseek.env.example" ]]; then
      cp -a "config/deepseek.env.example" "$BACKUP_DIR/deepseek.env.example.bak"
      echo "Backed up existing config/deepseek.env.example -> $BACKUP_DIR"
    fi
    cp -a "./config/deepseek.env.example" "config/deepseek.env.example" || true

    # 3) Install tools/ops_undo.py (backup existing if present)
    mkdir -p tools
    if [[ -f "tools/ops_undo.py" ]]; then
      cp -a "tools/ops_undo.py" "$BACKUP_DIR/ops_undo.py.bak"
    fi
    cp -a "./tools/ops_undo.py" "tools/ops_undo.py"
    chmod +x "tools/ops_undo.py"

    # 4) Create snapshots dir if not present
    mkdir -p ".ops_receipts/snapshots"

    # 5) Git stage + commit (safe if repo present)
    if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
      git add .gitignore config/deepseek.env.example tools/ops_undo.py || true
      git commit -m "[agent-hardening] secrets hygiene (.env.example) + undo tool + .gitignore" || true
      echo "Committed hardening changes (or nothing to commit)."
    else
      echo "Not a git repository; skipped commit."
    fi

    echo
    echo "Done."
    echo "Next steps:"
    echo "  1) Copy config/deepseek.env.example -> config/deepseek.env and fill your real key."
    echo "  2) Use 'tools/ops_undo.py' to revert last agent apply if needed."
    echo
    echo "Apply complete."
