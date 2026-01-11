# Link Library Desktop

Desktop application built with Compose for Desktop.

## Running the App

### Using Gradle:

```bash
./gradlew :app:desktopApp:runDesktop
```

### Using IDEA:

1. Open the `desktopApp` module
2. Run the `main()` function in `Main.kt`

## Building Native Distributions

### macOS (.dmg):
```bash
./gradlew :app:desktopApp:packageDmg
```

### Windows (.exe):
```bash
./gradlew :app:desktopApp:packageMsi
```

### Linux (.deb/.rpm):
```bash
./gradlew :app:desktopApp:packageDeb
./gradlew :app:desktopApp:packageRpm
```

## Database Location

The desktop app stores the database at:
```
~/.LinkLibrary/database/link_library.db
```

## Platform Support

- ✅ macOS (Intel & Apple Silicon)
- ✅ Windows
- ✅ Linux

## Requirements

- JVM 17 or higher
