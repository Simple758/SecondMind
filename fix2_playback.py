with open('app/src/main/java/com/secondmind/minimal/presentation/audiobook/components/PlaybackControls.kt', 'r') as f:
    content = f.read()

content = content.replace('.toLong())', '.toInt())')

with open('app/src/main/java/com/secondmind/minimal/presentation/audiobook/components/PlaybackControls.kt', 'w') as f:
    f.write(content)
print("✓ Fixed PlaybackControls")
