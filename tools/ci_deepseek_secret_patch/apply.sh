\
    #!/usr/bin/env bash
    set -Eeuo pipefail
    set +H

    # Repo root
    cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")" || { echo "❌ repo not found"; exit 1; }

    WF=".github/workflows/android-apk.yml"
    [ -f "$WF" ] || { echo "❌ $WF not found"; exit 1; }

    echo "== BEFORE (context around steps:) =="
    ln=$(awk '/^[[:space:]]*steps:/ {print NR; exit}' "$WF")
    if [ -z "${ln:-}" ]; then
      echo "❌ Could not find a steps: block in $WF"; exit 1
    fi
    nl -ba "$WF" | sed -n "$((ln-2)),$((ln+15))p"

    cp -f "$WF" "$WF.bak"

    # Determine indentation
    steps_line="$ln"
    steps_indent="$(sed -n "${steps_line}p" "$WF" | sed -E 's/^([[:space:]]*).*/\1/')"
    step_indent="${steps_indent}  "
    run_indent="${step_indent}  "
    cmd_indent="${run_indent}  "

    # Build a correct block in a temp file
    BLOCK="$(mktemp)"
    {
      printf "%s- name: Inject DeepSeek key\n" "$step_indent"
      printf "%srun: |\n" "$step_indent  "
      printf "%smkdir -p ~/.gradle\n" "$cmd_indent"
      printf "%secho \"DEEPSEEK_API_KEY=\${{ secrets.DEEPSEEK_API_KEY }}\" >> ~/.gradle/gradle.properties\n" "$cmd_indent"
    } > "$BLOCK"

    TMP="$(mktemp)"
    if grep -qE "^[[:space:]]*- name:[[:space:]]*Inject DeepSeek key" "$WF"; then
      # Replace existing block
      awk -v step_indent="$step_indent" -v block="$BLOCK" '
        BEGIN{replacing=0}
        {
          line=$0
          if (replacing==1) {
            if (index(line, step_indent "- ")==1) {
              # end of old block, emit new block then current line
              while ((getline b < block) > 0) print b
              close(block)
              print line
              replacing=0
            }
            next
          }
          if (index(line, step_indent "- name: Inject DeepSeek key")==1) {
             replacing=1
             next
          }
          print line
        }
        END{
          if (replacing==1) {
            # reached EOF while in block: append the block
            while ((getline b < block) > 0) print b
            close(block)
          }
        }
      ' "$WF" > "$TMP"
    else
      # Insert new block after steps:
      awk -v ins_after="$steps_line" -v block="$BLOCK" '
        NR==ins_after {
          print
          while ((getline b < block) > 0) print b
          close(block)
          next
        }
        { print }
      ' "$WF" > "$TMP"
    fi

    mv "$TMP" "$WF"
    rm -f "$BLOCK"

    echo
    echo "-- Diff --"
    git --no-pager diff -U3 -- "$WF" | sed -n "1,140p" || true

    git add "$WF"
    echo
    echo "== AFTER (context around Inject step) =="
    iln=$(grep -n "Inject DeepSeek key" "$WF" | head -n1 | cut -d: -f1 || echo 1)
    nl -ba "$WF" | sed -n "$((iln-2)),$((iln+12))p"
    echo "✅ Workflow fixed & staged (no commit)."
