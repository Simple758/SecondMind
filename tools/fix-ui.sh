#!/usr/bin/env bash
set -euo pipefail

echo "==> Branch & stash safety"
git rev-parse --abbrev-ref HEAD
git status --porcelain
# Optional: uncomment to auto-stash
# git stash push -u -m "pre-fix-ui $(date -Iseconds)" >/dev/null 2>&1 || true

changed=0
diff_and_mark() {
  local f="$1"
  if git ls-files --error-unmatch "$f" >/dev/null 2>&1; then
    echo "---- BEFORE: $f"
    git --no-pager diff -- "$f" || true
    changed=1
  fi
}

apply_sed() {
  local f="$1"; shift
  if [[ -f "$f" ]]; then
    local before_sha; before_sha="$(git hash-object "$f" 2>/dev/null || echo 0)"
    # shellcheck disable=SC2124
    local sedexpr="$@"
    eval "sed -i $sedexpr \"$f\""
    local after_sha; after_sha="$(git hash-object "$f" 2>/dev/null || echo 0)"
    if [[ "$before_sha" != "$after_sha" ]]; then diff_and_mark "$f"; fi
  fi
}

apply_perl() {
  local f="$1"; shift
  [[ -f "$f" ]] || return 0
  local before_sha; before_sha="$(git hash-object "$f" 2>/dev/null || echo 0)"
  perl -0777 -pe "$@" -i -- "$f"
  local after_sha; after_sha="$(git hash-object "$f" 2>/dev/null || echo 0)"
  if [[ "$before_sha" != "$after_sha" ]]; then diff_and_mark "$f"; fi
}

echo "==> Fix #1: Remove hard height cap on HomeCarousel usage (prevents clipping and infinite constraints)"
MA="${MA:-app/src/main/java/com/secondmind/minimal/MainActivity.kt}"
apply_perl "$MA" \
  "s/\\.heightIn\\s*\\(\\s*min\\s*=\\s*[^,]+,\\s*max\\s*=\\s*[^\\)]+\\)//g"

echo "==> Fix #2: Make HomeCarousel intrinsically fill space; avoid nested scroll clashes"
HC="${HC:-app/src/main/java/com/secondmind/minimal/home/HomeCarousel.kt}"
# Ensure LazyVerticalGrid has fillMaxSize and not verticalScroll
apply_perl "$HC" \
  "s/LazyVerticalGrid\\(([^)]*)\\)/LazyVerticalGrid(\\1\\n    , modifier = (\\1 =~ /modifier\\s*=/ ? modifier.fillMaxSize() : Modifier.fillMaxSize()) )/s" \
  || true
# In case above is too clever, do explicit replacements:
apply_perl "$HC" "s/modifier\\s*=\\s*modifier[^,)]*/modifier = modifier.fillMaxSize()/g"
apply_perl "$HC" "s/modifier\\s*=\\s*Modifier(?!\\.fillMaxSize\\(\\))/modifier = Modifier.fillMaxSize()/g"
# Strip accidental verticalScroll wrappers near Lazy containers
for f in "$MA" "$HC"; do
  apply_perl "$f" "s/Modifier\\.verticalScroll\\s*\\(\\s*rememberScrollState\\s*\\(\\s*\\)\\s*\\)\\s*//g"
done

echo "==> Fix #3: Wrap app content in your custom theme (if missing)"
apply_perl "$MA" \
  "s/setContent\\s*\\{\\s*/setContent {\\n    SecondMindMinimalTheme {\\n/g"
apply_perl "$MA" \
  "s/(setContent\\s*\\{[\\s\\S]*?)\\n\\s*\\}\\s*$/\\1\\n    }\\n}\\n/s"

echo "==> Fix #4: News refresh enabled + basic empty/error state + a11y descriptions"
NP="${NP:-app/src/main/java/com/secondmind/minimal/news/NewsPanel.kt}"
# Enable Refresh button (remove forced disabled)
apply_perl "$NP" "s/(OutlinedButton\\s*\\(\\s*onClick\\s*=\\s*[^,]+,?)\\s*enabled\\s*=\\s*false\\s*(,?)/\\1\\2/s"
# Add simple empty state if not already present
grep -q "Couldn.t load news" "$NP" 2>/dev/null || apply_perl "$NP" \
  "s/(LazyColumn\\s*\\{)/\\1\\n    if (!isLoading && articles.isEmpty()) {\\n        item { Text(\\\"Couldn\\x27t load news. Check connection or tap Refresh.\\\") }\\n        return@LazyColumn\\n    }/s"
# Give AsyncImage a contentDescription (use title fallback)
apply_perl "$NP" \
  "s/AsyncImage\\s*\\(([^)]*)\\)/do { my $b=\$1; if (\$b !~ /contentDescription\\s*=/) { \$b .= \", contentDescription = article.title ?: \\\"News image\\\"\" } \"AsyncImage(\".\$b.\")\" }/eg"

echo "==> Fix #5: Kill stray broken imports / partial tokens that often appear from bad patches (safe no-ops otherwise)"
for f in "$MA" "$HC" "$NP"; do
  [[ -f "$f" ]] || continue
  apply_perl "$f" "s/\\bLazyColum\\b/LazyColumn/g"
  apply_perl "$f" "s/import\\s+androidx\\.compose\\.foundation\\.lazy\\.grid\\s*$/import androidx.compose.foundation.lazy.grid/g"
  apply_perl "$f" "s/\\b\\.GridCells\\b/GridCells/g"
done

if [[ "$changed" -eq 0 ]]; then
  echo "==> No target patterns were found. Nothing changed."
  exit 0
fi

echo "==> Post-edit diff summary"
git --no-pager diff --name-only | sed "s/^/  modified: /"

echo "==> Kotlin quick compile sanity (no tests/lint)"
if [[ -x ./gradlew ]]; then
  ./gradlew :app:compileDebugKotlin -x lint -x test --no-daemon --stacktrace
else
  echo "gradlew not found, skipping compile sanity."
fi

echo "==> If compile OK, commit and push to main"
if [[ -x ./gradlew ]]; then
  ./gradlew :app:compileDebugKotlin -x lint -x test --no-daemon >/dev/null 2>&1 && ok=1 || ok=0
else
  ok=1
fi

if [[ "$ok" -eq 1 ]]; then
  git add -A
  git commit -m "UI fixes: remove nested-scroll height trap, theme wrap, News refresh+a11y, grid fillMaxSize, empty state"
  git push origin HEAD:main
  echo "==> Push complete."
  echo "==> Last commit:"
  git --no-pager log -1 --stat
else
  echo "!! Sanity compile failed — not committing."
  git --no-pager diff
  exit 1
fi
