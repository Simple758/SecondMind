BEGIN{inwc=0}
{
  # Enter withContext(Dispatchers.IO) { block
  if (bash ~ /val[ \t]+res[ \t]*=[ \t]*withContext\(Dispatchers\.IO\)[ \t]*\{[ \t]*$/) { inwc=1; print; next }

  if (inwc==1) {
    # fetch + closing brace on the SAME line -> split it
    if (bash ~ /^[ \t]*NewsApi\.fetchTopHeadlines\(.*\)[ \t]*\}[ \t]*$/) {
      sub(/[ \t]*\}[ \t]*$/, "", bash); print; print "            }"; inwc=0; next
    }
    # closing brace on its own line
    if (bash ~ /^[ \t]*\}[ \t]*$/) { inwc=0; print; next }
    print; next
  }

  # Drop orphan "contentScale = ContentScale.Crop" followed by a lone ")"
  if (bash ~ /^[ \t]*contentScale[ \t]*=[ \t]*ContentScale\.Crop[ \t]*$/) {
    if (getline nxt) { if (nxt ~ /^[ \t]*\)[ \t]*$/) { next } else { print nxt; next } }
    else { next }
  }

  print
}
