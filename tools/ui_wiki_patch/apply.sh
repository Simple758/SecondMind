#!/usr/bin/env bash
set -Eeuo pipefail
set +H

cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")" || { echo "❌ repo not found"; exit 1; }
echo "== Repo: $(pwd)"

NAV="app/src/main/java/com/secondmind/minimal/ui/NavigationRoutes.kt"
MAIN="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
CARD="app/src/main/java/com/secondmind/minimal/feature/wiki/WikiCard.kt"
REPO="app/src/main/java/com/secondmind/minimal/feature/wiki/WikiRepo.kt"
NEW_DIR="app/src/main/java/com/secondmind/minimal/feature/wiki"
PATCH_ROOT="tools/ui_wiki_patch/src/app/src/main/java/com/secondmind/minimal/feature/wiki"

mkdir -p "$NEW_DIR"

# 1) Copy new files (safe overwrite to temp, only move if changed)
for f in WikiScreen.kt WikiViewModel.kt; do
  if [ -f "$PATCH_ROOT/$f" ]; then
    TMP="$NEW_DIR/.$f.__tmp__"
    cp -f "$PATCH_ROOT/$f" "$TMP"
    if [ ! -f "$NEW_DIR/$f" ] || ! cmp -s "$NEW_DIR/$f" "$TMP" ; then
      mv -f "$TMP" "$NEW_DIR/$f"
      git add "$NEW_DIR/$f"
      echo "✓ Installed/updated: $NEW_DIR/$f"
    else
      rm -f "$TMP"
      echo "— Unchanged: $NEW_DIR/$f"
    fi
  fi
done

# 2) Extend WikiRepo.kt with search/getSummary/cache if file exists
if [ -f "$REPO" ]; then
  if ! grep -q "fun searchTitles(" "$REPO"; then
    echo "-- Patching $REPO (append helpers) --"
    cat >> "$REPO" <<'KOT'
// ====== Added by ui_wiki_patch (safe append) ======
/**
 * Minimal search and summary helpers using Wikipedia REST.
 * No external deps; uses java.net + org.json (Android built-in).
 */
private fun httpGetJson(url: String): org.json.JSONObject {
    val u = java.net.URL(url)
    val c = (u.openConnection() as java.net.HttpURLConnection).apply {
        requestMethod = "GET"
        setRequestProperty("Accept", "application/json")
        connectTimeout = 8000
        readTimeout = 8000
    }
    return try {
        val text = c.inputStream.bufferedReader().use { it.readText() }
        org.json.JSONObject(text)
    } finally {
        c.disconnect()
    }
}

fun searchTitles(query: String, limit: Int = 5): List<String> {
    return try {
        val enc = java.net.URLEncoder.encode(query, "UTF-8")
        val j = httpGetJson("https://en.wikipedia.org/w/rest.php/v1/search/title?q=$enc&limit=$limit")
        val arr = j.optJSONArray("pages") ?: return emptyList()
        buildList {
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val t = obj.optString("title")
                if (t.isNotBlank()) add(t)
            }
        }
    } catch (_: Throwable) { emptyList() }
}

data class WikiSummary(val title: String, val extract: String, val thumbnail: String? = null)

fun getSummary(title: String): WikiSummary? {
    return try {
        val enc = java.net.URLEncoder.encode(title, "UTF-8")
        val j = httpGetJson("https://en.wikipedia.org/api/rest_v1/page/summary/$enc")
        val extract = j.optString("extract")
        val thumb = j.optJSONObject("thumbnail")?.optString("source")
        if (extract.isNullOrBlank()) null else WikiSummary(title = j.optString("title", title), extract = extract, thumbnail = thumb)
    } catch (_: Throwable) { null }
}

// Simple 7-day TTL cache using SharedPreferences (JSON map)
fun cacheGet(context: android.content.Context, key: String): String? {
    val sp = context.getSharedPreferences("wiki_cache", 0)
    val now = System.currentTimeMillis()
    return try {
        val raw = sp.getString(key, null) ?: return null
        val j = org.json.JSONObject(raw)
        val ttl = j.optLong("ttl", 0L)
        val until = j.optLong("until", 0L)
        if (ttl <= 0L || until < now) { null } else j.optString("value", null)
    } catch (_: Throwable) { null }
}
fun cachePut(context: android.content.Context, key: String, value: String, days: Int = 7) {
    val sp = context.getSharedPreferences("wiki_cache", 0)
    val now = System.currentTimeMillis()
    val until = now + days * 24L * 3600_000L
    val j = org.json.JSONObject().put("value", value).put("ttl", days).put("until", until)
    sp.edit().putString(key, j.toString()).apply()
}
// ====== end ui_wiki_patch append ======
KOT
    git add "$REPO"
  else
    echo "— $REPO already has search helpers; skipping."
  fi
else
  echo "⚠️  $REPO not found; skipping repo helpers (screen will fetch via VM stubs)."
fi

# 3) Add NavigationRoutes.WIKI constant
if [ -f "$NAV" ]; then
  if ! grep -qE 'WIKI[[:space:]]*=' "$NAV"; then
    echo "-- Adding WIKI route to $NAV --"
    awk '
      BEGIN{ins=0}
      /object[[:space:]]+NavigationRoutes/ {print; obj=1; next}
      obj==1 && /^\}/ && ins==0 { print "  const val WIKI = \"wiki\""; print; obj=0; ins=1; next }
      { print }
    ' "$NAV" > "$NAV.__new__" && mv "$NAV.__new__" "$NAV"
    git add "$NAV"
  else
    echo "— WIKI route already present."
  fi
else
  echo "⚠️  $NAV missing; cannot add WIKI route."
fi

# 4) Add composable(\"wiki\") in MainActivity NavHost
if [ -f "$MAIN" ]; then
  if ! grep -q 'composable("wiki")' "$MAIN"; then
    echo "-- Inserting composable(\"wiki\") in $MAIN --"
    awk '
      BEGIN{added=0}
      /NavHost\(.*\)\s*\{/ { nav=1 }
      {
        print $0
        if (nav==1 && added==0 && $0 ~ /composable\("news"\)/) {
          print "  composable(\"wiki\") { com.secondmind.minimal.feature.wiki.WikiScreen() }"
          added=1
        }
      }
    ' "$MAIN" > "$MAIN.__new__" && mv "$MAIN.__new__" "$MAIN"
    if ! grep -q 'composable("wiki")' "$MAIN"; then
      # Fallback: append near end of NavHost block if not found after news
      awk '
        BEGIN{added=0}
        /NavHost\(.*\)\s*\{/ { nav=1 }
        {
          if (nav==1 && added==0 && /^\s*\}\s*$/) {
            print "  composable(\"wiki\") { com.secondmind.minimal.feature.wiki.WikiScreen() }"
            added=1
          }
          print $0
        }
      ' "$MAIN" > "$MAIN.__new__" && mv "$MAIN.__new__" "$MAIN"
    fi
    git add "$MAIN"
  else
    echo "— composable(\"wiki\") already present."
  fi
else
  echo "⚠️  $MAIN missing; cannot add destination."
fi

# 5) Attempt to wire WikiBrainFoodCard to navigate (best effort)
if [ -f "$CARD" ]; then
  if ! grep -q "NavigationRoutes.WIKI" "$CARD"; then
    echo "-- Trying to wire WikiBrainFoodCard to navigate -> WIKI --"
    # Common pattern: replace any open URL action to wikipedia with nav
    sed -E -i.bak \
      's@onClick[[:space:]]*=\s*\{[^}]*\}@onClick = { navController?.navigate(NavigationRoutes.WIKI) }@' \
      "$CARD" || true
    # If NavigationRoutes not imported, add it
    if ! grep -q "NavigationRoutes" "$CARD"; then
      sed -i '1i import com.secondmind.minimal.ui.NavigationRoutes' "$CARD" || true
    fi
    git add "$CARD" || true
  else
    echo "— Wiki card already navigates in-app."
  fi
else
  echo "ℹ️  $CARD not found; skipping card wiring (you can add a Home link to route=\"wiki\")."
fi

echo
echo "== Staged files =="
git diff --name-only --cached || true

echo
echo "== Previews =="
for f in "$NAV" "$MAIN" "$NEW_DIR/WikiScreen.kt" "$NEW_DIR/WikiViewModel.kt"; do
  [ -f "$f" ] && { echo "-- $f (first 120 lines) --"; nl -ba "$f" | sed -n "1,120p"; echo; }
done

echo "✅ Wiki patch applied (no commit, no build)."
