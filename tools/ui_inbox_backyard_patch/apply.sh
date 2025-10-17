#!/usr/bin/env bash
set -Eeuo pipefail; set +H
cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")" || { echo "❌ repo not found"; exit 1; }

echo "== Repo: $(pwd)"
FAIL=0

stage(){ git add "$@" >/dev/null 2>&1 || true; }
section(){ printf '\n== %s ==\n' "$1"; }

# ---------- Paths ----------
MA="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
LISTENER="app/src/main/java/com/secondmind/minimal/notify/SecondMindNotificationListener.kt"

# ---------- 1) New files ----------
section "Installing new files"

mkdir -p app/src/main/java/com/secondmind/minimal/inbox
cat > app/src/main/java/com/secondmind/minimal/inbox/InboxGate.kt <<'KOT'
package com.secondmind.minimal.inbox
/** Session gate: when true, listener may snapshot; otherwise dormant. */
object InboxGate { @Volatile var active: Boolean = false }
KOT
stage app/src/main/java/com/secondmind/minimal/inbox/InboxGate.kt
echo "✓ InboxGate.kt"

mkdir -p app/src/main/java/com/secondmind/minimal/feature/inbox
cat > app/src/main/java/com/secondmind/minimal/feature/inbox/NotificationSummarizer.kt <<'KOT'
package com.secondmind.minimal.feature.inbox

import android.content.Context
import com.secondmind.minimal.core.ai.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NotificationSummarizer {
  /** Very small, local fallback digest by app. */
  fun summarizeLocal(lines: List<String>): String {
    if (lines.isEmpty()) return "No recent notifications."
    val byApp = lines.groupBy { it.substringBefore(":").ifBlank { "app" } }
    val sb = StringBuilder()
    for ((app, lns) in byApp) {
      val sample = lns.take(3).joinToString("; ") { it.substringAfter(":").take(80) }
      sb.append("• ").append(app).append(": ").append(sample)
      if (lns.size > 3) sb.append(" …(+").append(lns.size - 3).append(")")
      sb.append('\n')
    }
    return sb.toString().trim()
  }

  /** Optional AI refine (120 tokens max). Falls back to local on error. */
  suspend fun summarizeWithAI(ctx: Context, lines: List<String>): String = withContext(Dispatchers.IO) {
    val local = summarizeLocal(lines)
    if (lines.isEmpty()) return@withContext local
    return@withContext try {
      val promptText = buildString {
        append("You receive a redacted snapshot of Android notifications grouped by app.\n")
        append("Return a tight, privacy-safe digest in 3–5 bullets. No PII. Max 120 tokens.\n")
        append("Lines:\n")
        for (ln in lines) append("- ").append(ln.take(200)).append('\n')
      }
      when (val res = AIServiceLocator.get().complete(
        context = ctx,
        prompt = Prompt(system = "Summarize briefly, privacy-first. Keep generic.", user = promptText),
        options = AIOptions(model = "deepseek-chat", maxTokens = 120, temperature = 0.2),
        packet = ContextPacket(source = ContextPacket.Source.UI, appPackage = ctx.packageName, text = "inbox-digest")
      )) {
        is AIResult.Text -> res.content
        is AIResult.Error -> "AI error: ${res.message}\n\n$local"
      }
    } catch (_: Throwable) {
      local
    }
  }
}
KOT
stage app/src/main/java/com/secondmind/minimal/feature/inbox/NotificationSummarizer.kt
echo "✓ NotificationSummarizer.kt"

# Backyard scaffolding
ROOT_BK=app/src/main/java/com/secondmind/minimal/core/backyard
mkdir -p "$ROOT_BK"/{reception,storeroom,gateway,policy,ui}

cat > "$ROOT_BK/reception/ReceptionBus.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.reception
/** Receives raw/volatile events from producers. NOT AI-visible. */
object ReceptionBus {
  // TODO: add channels/flows; keep ephemeral only.
}
KOT

cat > "$ROOT_BK/reception/Buffers.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.reception
/** Tiny in-memory ring buffer, auto-truncating; ephemeral. */
class RingBuffer<T>(private val cap: Int = 128) {
  private val q = ArrayList<T>()
  @Synchronized fun add(x: T) { if (q.size >= cap) q.removeAt(0); q.add(x) }
  @Synchronized fun snapshot(): List<T> = q.toList()
  @Synchronized fun clear() { q.clear() }
}
KOT

cat > "$ROOT_BK/storeroom/BackyardEntities.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.storeroom
/** Curated, sanitized records AI may read via gateway. */
data class Fact(val key: String, val value: String, val ts: Long, val ttlDays: Int = 30)
data class Event(val id: String, val type: String, val payload: String, val ts: Long)
data class Insight(val id: String, val tag: String, val summary: String, val ts: Long)
KOT

cat > "$ROOT_BK/storeroom/BackyardRepo.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.storeroom
import java.util.concurrent.ConcurrentHashMap
/** Simple in-memory repo stub (Room later). */
class BackyardRepo {
  private val facts = ConcurrentHashMap<String, Fact>()
  fun upsertFact(f: Fact) { facts[f.key] = f }
  fun getFact(key: String): Fact? = facts[key]
  fun list(prefix: String): List<Fact> = facts.values.filter { it.key.startsWith(prefix) }.sortedByDescending { it.ts }
  fun cleanup(now: Long = System.currentTimeMillis()) {
    facts.values.removeIf { f -> f.ttlDays > 0 && now - f.ts > f.ttlDays * 86_400_000L }
  }
}
KOT

cat > "$ROOT_BK/policy/BackyardAllowlist.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.policy
/** Keys the AI gateway is allowed to expose. Edit explicitly. */
object BackyardAllowlist {
  val keys: Set<String> = setOf(
    "notif.count.1h",
    "usage.screen.on.ms",
    "network.tx.rx.kb"
  )
  fun isAllowed(key: String) = keys.contains(key)
}
KOT

cat > "$ROOT_BK/policy/Sanitizer.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.policy
/** Minimal sanitizers: clamp precision, strip obvious PII. */
object Sanitizer {
  private val email = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
  private val phone = Regex("\\b\\+?\\d{7,}\\b")
  fun clamp(s: String, max: Int = 240) = s.take(max)
  fun redact(s: String) = s.replace(email, "###").replace(phone, "###")
}
KOT

cat > "$ROOT_BK/gateway/BackyardQueryService.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.gateway
import com.secondmind.minimal.core.backyard.policy.BackyardAllowlist
import com.secondmind.minimal.core.backyard.storeroom.BackyardRepo
/** Narrow, read-only API the AI may query. */
class BackyardQueryService(private val repo: BackyardRepo = BackyardRepo()) {
  fun get(key: String): String? {
    if (!BackyardAllowlist.isAllowed(key)) return null
    return repo.getFact(key)?.value
  }
  fun list(prefix: String): Map<String,String> =
    repo.list(prefix).filter { BackyardAllowlist.isAllowed(it.key) }.associate { it.key to it.value }
}
KOT

cat > "$ROOT_BK/ui/PromoteSheet.kt" <<'KOT'
package com.secondmind.minimal.core.backyard.ui
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
/** Stub: user can promote Reception → Store Room after consent. */
@Composable
fun PromoteSheet(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } },
    title = { Text("Promote to AI Store Room") },
    text = { Text("This is a stub. When implemented, you can curate and promote sanitized facts here.") },
    properties = DialogProperties(dismissOnClickOutside = true)
  )
}
KOT

stage "$ROOT_BK"/{reception/ReceptionBus.kt,reception/Buffers.kt,storeroom/BackyardEntities.kt,storeroom/BackyardRepo.kt,policy/BackyardAllowlist.kt,policy/Sanitizer.kt,gateway/BackyardQueryService.kt,ui/PromoteSheet.kt}
echo "✓ Backyard scaffolding"

# ---------- 2) Patch MainActivity: Inbox overlay FAB ----------
section "Patching MainActivity (Inbox AI overlay)"
if [ -f "$MA" ]; then
  cp -f "$MA" "$MA.__bak__"
  if grep -q "Inbox AI overlay" "$MA"; then
    echo "Already patched."
  else
    awk '
      BEGIN{in=0; depth=0; done=0}
      {
        if(done){ print; next }
        if(!in && $0 ~ /composable\\("inbox"\\)[[:space:]]*\\{/){ in=1; depth=0; next }
        if(in){
          # track braces to find end of composable
          for(i=1;i<=length($0);i++){ c=substr($0,i,1); if(c=="{") depth++; else if(c=="}"){ if(depth==0){in=0; break} depth-- } }
          if(depth==0){
            print "composable(\"inbox\") {"
            print "  val ctx = androidx.compose.ui.platform.LocalContext.current"
            print "  val scope = androidx.compose.runtime.rememberCoroutineScope()"
            print "  var digest by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }"
            print "  var busy by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }"
            print "  androidx.compose.foundation.layout.Box(androidx.compose.ui.Modifier.fillMaxSize()) {"
            print "    com.secondmind.minimal.ui.InboxScreen()"
            print "    androidx.compose.material3.FloatingActionButton("
            print "      onClick = {"
            print "        if (!com.secondmind.minimal.util.NotificationAccess.isEnabled(ctx)) {"
            print "          com.secondmind.minimal.util.NotificationAccess.openSettings(ctx)"
            print "        } else if (!busy) {"
            print "          busy = true"
            print "          com.secondmind.minimal.inbox.InboxGate.active = true"
            print "          try {"
            print "            android.service.notification.NotificationListenerService.requestRebind("
            print "              android.content.ComponentName(ctx, com.secondmind.minimal.notify.SecondMindNotificationListener::class.java)"
            print "            )"
            print "          } catch (_: Throwable) {}"
            print "          val lines = com.secondmind.minimal.notify.SecondMindNotificationListener.consumeSnapshot()"
            print "          if (lines.isEmpty()) { digest = \"No recent notifications.\"; busy = false }"
            print "          else {"
            print "            digest = \"Summarizing…\""
            print "            scope.launch {"
            print "              val d = com.secondmind.minimal.feature.inbox.NotificationSummarizer.summarizeWithAI(ctx, lines)"
            print "              digest = d; busy = false"
            print "            }"
            print "          }"
            print "        }"
            print "      },"
            print "      modifier = androidx.compose.ui.Modifier.align(androidx.compose.ui.Alignment.BottomEnd).padding(16.dp)"
            print "    ) { androidx.compose.material3.Icon(androidx.compose.material.icons.Icons.Filled.SmartToy, contentDescription = \"AI\") }"
            print "  }"
            print "  if (digest != null) {"
            print "    androidx.compose.material3.AlertDialog("
            print "      onDismissRequest = { digest = null },"
            print "      confirmButton = { androidx.compose.material3.TextButton(onClick = { digest = null }) { androidx.compose.material3.Text(\"Close\") } },"
            print "      title = { androidx.compose.material3.Text(\"Inbox Digest\") },"
            print "      text = { androidx.compose.material3.Text(digest!!) }"
            print "    )"
            print "  }"
            print "}"
            print "// Inbox AI overlay"
            done=1; next
          } else { next }
        }
        print
      }' "$MA" > "$MA.__new__" && mv "$MA.__new__" "$MA"
  fi
  echo "-- BEFORE/AFTER (excerpt) --"
  echo "BEFORE:"; sed -n "1,220p" "$MA.__bak__" | grep -n 'composable("inbox")' -n | head -n1 || true
  echo "AFTER :"; nl -ba "$MA" | sed -n '120,200p' | sed -n '1,80p'
  stage "$MA"
else
  echo "⚠️  $MA not found"; FAIL=1
fi

# ---------- 3) Patch NotificationListener: gate + snapshot ----------
section "Patching SecondMindNotificationListener"
if [ -f "$LISTENER" ]; then
  cp -f "$LISTENER" "$LISTENER.__bak__"
  awk '
    BEGIN{ s="" }
    { s = s $0 "\n" }
    END{
      hasComp = match(s, /companion object[^{]*\\{[\\s\\S]*consumeSnapshot\\(/)
      if(!hasComp){
        gsub(/class[^{]*SecondMindNotificationListener[^{]*\\{/,
             "&\n  companion object {\n    @Volatile private var lastSnapshot: List<String> = emptyList()\n"\
             "    fun consumeSnapshot(): List<String> { val out = lastSnapshot; lastSnapshot = emptyList(); return out }\n"\
             "  }\n", s)
      }
      # guard on posted
      gsub(/override fun onNotificationPosted\\([\\s\\S]*?\\)\\s*\\{\\s*/,
           "&if (!com.secondmind.minimal.inbox.InboxGate.active) return\n", s)
      # add onListenerConnected if missing
      if (index(s, "override fun onListenerConnected()") == 0){
        sub(/\\}\\s*$/,
"  override fun onListenerConnected() {\n"\
"    super.onListenerConnected()\n"\
"    if (com.secondmind.minimal.inbox.InboxGate.active) {\n"\
"      val lines = (activeNotifications ?: emptyArray()).map { n ->\n"\
"        val app = n.packageName.substringAfterLast('.')\n"\
"        val t = n.notification.extras?.getCharSequence(\"android.title\")?.toString() ?: \"\"\n"\
"        val x = n.notification.extras?.getCharSequence(\"android.text\")?.toString() ?: \"\"\n"\
"        sanitize(\"\$app: \$t - \$x\")\n"\
"      }\n"\
"      lastSnapshot = lines\n"\
"      com.secondmind.minimal.inbox.InboxGate.active = false\n"\
"    }\n"\
"  }\n"\
"  private fun sanitize(s: String): String = s\n"\
"    .replace(Regex(\"[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\"), \"###\")\n"\
"    .replace(Regex(\"\\\\b\\\\+?\\\\d{7,}\\\\b\"), \"###\")\n"\
"    .take(240)\n"\
"}\n", s)
      }
      printf("%s", s)
    }' "$LISTENER" > "$LISTENER.__new__" && mv "$LISTENER.__new__" "$LISTENER"
  echo "-- Listener tail --"
  tail -n 60 "$LISTENER"
  stage "$LISTENER"
else
  echo "⚠️  $LISTENER not found"; FAIL=1
fi

section "Staged files"
git diff --cached --name-only || true

if [ "$FAIL" -eq 0 ]; then
  echo
  echo "✅ Patch applied & staged. (No build, no push)"
else
  echo "❌ Patch had warnings; see above."
fi
