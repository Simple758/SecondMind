#!/usr/bin/env python3
"""Safe audiobook integration"""

with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'r') as f:
    lines = f.readlines()

print(f"Read {len(lines)} lines")

# Find last import
last_import = -1
for i, line in enumerate(lines):
    if line.strip().startswith('import '):
        last_import = i

print(f"Last import at line {last_import + 1}")

# Add imports
imports = [
    'import com.secondmind.minimal.presentation.audiobook.AudiobookScreen',
    'import com.secondmind.minimal.presentation.audiobook.AudiobookViewModel'
]
lines = lines[:last_import+1] + imports + lines[last_import+1:]
print("Added imports")

# Find developer composable
dev_idx = -1
for i, line in enumerate(lines):
    if 'composable("developer")' in line:
        dev_idx = i
        break

if dev_idx == -1:
    print("ERROR: No developer composable")
    exit(1)

print(f"Developer at line {dev_idx + 1}")

# Find closing brace
braces = 0
close_idx = -1
for i in range(dev_idx, len(lines)):
    braces += lines[i].count('{') - lines[i].count('}')
    if braces == 0 and i > dev_idx:
        close_idx = i
        break

if close_idx == -1:
    print("ERROR: No closing brace")
    exit(1)

print(f"Closing brace at line {close_idx + 1}")

# Add composable
route = [
    '',
    '            // Audiobook route',
    '            composable(NavigationRoutes.AUDIOBOOKS) {',
    '                val vm = remember { AudiobookViewModel() }',
    '                AudiobookScreen(vm = vm)',
    '            }'
]
lines = lines[:close_idx+1] + route + lines[close_idx+1:]
print("Added composable")

with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'w') as f:
    f.writelines(lines)

print(f"SUCCESS! {len(lines)} lines total (445 + 8 = 453 expected)")
