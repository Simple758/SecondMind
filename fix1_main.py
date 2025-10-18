with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'r') as f:
    lines = f.readlines()

output = []
skip = 0
for i, line in enumerate(lines):
    if '// Audiobook feature' in line and i > 0:
        # Check if this is the second occurrence
        if any('// Audiobook feature' in output[j] for j in range(len(output))):
            skip = 1  # Start skipping
            continue
    
    if skip > 0:
        if line.strip().startswith('}') and 'composable' not in line:
            skip += 1
            if skip == 5:  # Skip the broken composable block
                skip = 0
            continue
    
    if skip == 0:
        output.append(line)

with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'w') as f:
    f.writelines(output)
print("✓ Fixed MainActivity")
