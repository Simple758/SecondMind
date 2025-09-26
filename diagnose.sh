#!/usr/bin/env bash
set -u
cd ~/SecondMind || exit 1

MAIN="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
MANI="app/src/main/AndroidManifest.xml"

[ -f "$MAIN" ] || { echo "❌ Missing: $MAIN"; exit 1; }

echo "== File info =="
echo "  $(wc -l < "$MAIN") lines — $MAIN"

echo
echo "== Context around likely lines (240 260 264 268 270 300) =="
for L in 240 260 264 268 270 300; do
  total=$(wc -l < "$MAIN")
  [ "$L" -le "$total" ] || continue
  s=$((L-12)); [ $s -lt 1 ] && s=1
  e=$((L+12)); [ $e -gt $total ] && e=$total
  echo "-- near line $L --"
  nl -ba "$MAIN" | sed -n "${s},${e}p"
done

echo
echo "== Tail 40 lines =="
nl -ba "$MAIN" | tail -n 40

echo
echo "== Whole-file balance (braces & parens) =="
awk 'BEGIN{b=0;p=0;bad=0}
{
  lb=gsub(/\{/,"{"); rb=gsub(/\}/,"}");
  lp=gsub(/\(/,"("); rp=gsub(/\)/,")");
  b+=lb-rb; p+=lp-rp;
  if (b<0 || p<0) {
    printf("  NEGATIVE depth at line %d (braces=%d, parens=%d): %s\n", NR,b,p,$0);
    bad=1
  }
}
END{
  printf("  Totals: braces=%d, parens=%d\n", b,p);
  if(bad==0) print "  OK: never went negative";
}' "$MAIN"

echo
echo "== Suspicious sequences =="
if ! grep -nE '}\s*@Composable' "$MAIN"; then echo "  (none for '} @Composable')"; fi
if ! grep -nE '^\s*\)\s*$' "$MAIN"; then echo "  (no lone \")\" lines)"; fi
echo "  closing-brace-only lines at:"
grep -nE '^\s*\}\s*$' "$MAIN" || echo "  (none)"

echo
echo "== AppNav() block range =="
awk '
BEGIN{inb=0;depth=0;start=0;seenBrace=0}
{
  if(inb==0 && $0 ~ /^[[:space:]]*fun[[:space:]]+AppNav[[:space:]]*\(/){
    inb=1; start=NR;
  }
  if(inb==1){
    ob=gsub(/\{/,"{"); cb=gsub(/\}/,"}");
    if(ob>0) seenBrace=1
    depth+=ob-cb;
    if(seenBrace==1 && depth<=0){ print "  AppNav range: " start " .. " NR; exit }
  }
}
END{ if(inb==1) print "  AppNav appears unterminated from line " start; }
' "$MAIN"

echo
echo "== Counts inside AppNav (Drawer/Scaffold/NavHost) =="
awk '
BEGIN{inb=0;depth=0;start=0;drawer=0;scaf=0;navh=0;seenBrace=0}
{
  if(inb==0 && $0 ~ /^[[:space:]]*fun[[:space:]]+AppNav[[:space:]]*\(/){
    inb=1; start=NR;
  }
  if(inb==1){
    if($0 ~ /ModalNavigationDrawer\(/) drawer++
    if($0 ~ /Scaffold\(/) scaf++
    if($0 ~ /NavHost\(/) navh++
    ob=gsub(/\{/,"{"); cb=gsub(/\}/,"}"); if(ob>0) seenBrace=1; depth+=ob-cb
    if(seenBrace==1 && depth<=0){
      printf("  Drawer=%d, Scaffold=%d, NavHost=%d  (lines %d..%d)\n", drawer, scaf, navh, start, NR)
      exit
    }
  }
}
END{ if(inb==1) printf("  Unterminated: Drawer=%d, Scaffold=%d, NavHost=%d\n", drawer, scaf, navh) }
' "$MAIN"

echo
echo "== Import checks =="
if grep -q 'currentBackStackEntryAsState' "$MAIN"; then
  if grep -q '^import androidx.navigation.compose.currentBackStackEntryAsState' "$MAIN"; then
    echo "  ✓ backstack import present"
  else
    echo "  ❌ Missing import: androidx.navigation.compose.currentBackStackEntryAsState"
  fi
else
  echo "  (backstack state not used)"
fi

echo
echo "== Manifest duplicates =="
if [ -f "$MANI" ]; then
  grep -n 'ACCESS_NETWORK_STATE' "$MANI" || true
  cnt=$(grep -c 'ACCESS_NETWORK_STATE' "$MANI" || true)
  [ "$cnt" -gt 1 ] && echo "  ⚠️ Duplicate permission detected ($cnt times)"
else
  echo "  (manifest not found)"
fi
