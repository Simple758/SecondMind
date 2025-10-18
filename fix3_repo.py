with open('app/src/main/java/com/secondmind/minimal/data/repository/AudiobookRepositoryImpl.kt', 'r') as f:
    lines = f.readlines()

output = []
skip = False
for i, line in enumerate(lines):
    if 'private suspend fun requestSummary' in line:
        output.append(line)
        output.append('        return "Summary: $chapterTitle - ${textHint.take(200)}..."
')
        output.append('    }
')
        skip = True
    elif skip:
        if line.strip() == '}' and i > 0 and '    }' in lines[i-1]:
            skip = False
    elif not skip:
        output.append(line)

with open('app/src/main/java/com/secondmind/minimal/data/repository/AudiobookRepositoryImpl.kt', 'w') as f:
    f.writelines(output)
print("✓ Fixed AudiobookRepositoryImpl")
