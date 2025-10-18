with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'r') as f:
    content = f.read()

# Check if routes already exist
if 'NavigationRoutes.AUDIOBOOKS' in content:
    print("✓ Routes already exist")
else:
    lines = content.split('
')
    output = []
    for i, line in enumerate(lines):
        output.append(line)
        if 'composable("wiki")' in line and 'WikiScreen()' in line:
            output.append('')
            output.append('          // Audiobook feature')
            output.append('          composable(NavigationRoutes.AUDIOBOOKS) {')
            output.append('            val vm = remember { AudiobookViewModel() }')
            output.append('            AudiobookScreen(vm = vm, onOpenPlayer = { nav.navigate("audiobook_player") })')
            output.append('          }')
            output.append('          composable("audiobook_player") {')
            output.append('            val vm = remember { AudiobookViewModel() }')
            output.append('            val state by vm.state.collectAsState()')
            output.append('            state.current?.let { AudiobookPlayerScreen(book = it) }')
            output.append('          }')
    
    with open('app/src/main/java/com/secondmind/minimal/MainActivity.kt', 'w') as f:
        f.write('
'.join(output))
    print("✓ Navigation routes added")
