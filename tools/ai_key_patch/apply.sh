#!/usr/bin/env bash
set -Eeo pipefail
set +H

ROOT="$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")"
cd "$ROOT" || { echo "❌ Cannot cd to $ROOT"; exit 1; }

APP_GRADLE="app/build.gradle"
if [ ! -f "$APP_GRADLE" ]; then
  echo "❌ $APP_GRADLE not found under $ROOT"
  exit 1
fi

echo "== Repo: $ROOT"
echo "== BEFORE (first 140 lines) =="
nl -ba "$APP_GRADLE" | sed -n "1,140p"
echo

TMP="$APP_GRADLE.__patched__"
awk -f - "$APP_GRADLE" > "$TMP" <<'AWK'
BEGIN{
  hasKey=0; keyAdded=0;
  hasField=0; fieldAdded=0;
  inPlugins=0; pdepth=0;
}
/^[[:space:]]*def[[:space:]]+deepseekKey[[:space:]]*=/ { hasKey=1 }
/buildConfigField[[:space:]]+"String",[[:space:]]+"DEEPSEEK_API_KEY"/ { hasField=1 }
 
/^[[:space:]]*plugins[[:space:]]*\{/ { inPlugins=1; pdepth=0; }

{
  if (inPlugins==1) {
    print $0
    for (i=1; i<=length($0); i++) {
      c=substr($0,i,1)
      if (c=="{") pdepth++
      else if (c=="}") pdepth--
    }
    if (pdepth==0) {
      inPlugins=0
      if (!hasKey && !keyAdded) {
        print ""
        print "def deepseekKey = System.getenv(\"DEEPSEEK_API_KEY\") ?: (project.hasProperty(\"DEEPSEEK_API_KEY\") ? project.property(\"DEEPSEEK_API_KEY\") : \"\")"
        print ""
        keyAdded=1
      }
    }
    next
  }

  if ($0 ~ /buildConfigField[[:space:]]+\"String\",[[:space:]]+\"NEWS_API_KEY\"/) {
    print $0
    if (!hasField && !fieldAdded) {
      print "      buildConfigField \"String\", \"DEEPSEEK_API_KEY\", \"\"\${deepseekKey}\"\""
      fieldAdded=1
    }
    next
  }

  if ($0 ~ /^[ \t]*defaultConfig[ \t]*\{/) {
    print $0
    if (!hasField && !fieldAdded) {
      print "      buildConfigField \"String\", \"DEEPSEEK_API_KEY\", \"\"\${deepseekKey}\"\""
      fieldAdded=1
    }
    next
  }

  print $0
}
AWK

if cmp -s "$APP_GRADLE" "$TMP"; then
  echo "No changes required (already configured)."
  rm -f "$TMP"
  exit 0
fi

echo "-- Diff --"
git --no-pager diff --no-index -U3 "$APP_GRADLE" "$TMP" || true
mv "$TMP" "$APP_GRADLE"
tail -c1 "$APP_GRADLE" | od -An -t u1 | grep -q 10 || echo >> "$APP_GRADLE"
git add "$APP_GRADLE"
echo
echo "== AFTER (first 160 lines) =="
nl -ba "$APP_GRADLE" | sed -n "1,160p"
echo
echo "✅ Staged changes for $APP_GRADLE (not committed)."
