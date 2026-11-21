# iOS Implementation Plan (researched 2026-11-18)

Goal: run the shared Compose Multiplatform app on a physical iOS device.
Status: **not started** — this is the pre-researched plan. Per `claude-project/instruction.md`, iOS is Phase 4; this document exists so implementation starts with zero re-research.

---

## 0. Blocker first: Xcode is not installed on this Mac

`xcode-select` points at Command Line Tools only; no `Xcode.app` found (checked `/Applications` + Spotlight), and `xcrun --sdk iphoneos` fails. Kotlin/Native cannot compile iOS targets without it.

1. Install Xcode from the App Store (or xcodes).
2. `sudo xcode-select -s /Applications/Xcode.app/Contents/Developer`
3. Open Xcode once, install "iOS Platform" components, sign in with Apple ID for device signing.

**Nothing below can be verified until this is done.** The `iosMain` code that already exists in `core/design` and `core/utils` has *never been compiled* (no iOS SDK ever available on this machine) — expect first-build surprises there.

---

## 1. Current state (verified)

### What works / exists
| Piece | State |
|---|---|
| `core/design` iOS target | `iosArm64()` + `iosSimulatorArm64()` declared, framework baseName `design`. `iosMain` actuals exist: `AppMotion.ios.kt`, `Breakpoints.ios.kt` (returns `Compact`), `PlatformImage.ios.kt` (**placeholder box, no real image loading**) |
| `core/utils` iOS target | Targets declared, framework baseName `utils`. Actuals exist: `MoleculeViewModel.ios.kt` (plain class, Main-dispatcher scope — not AndroidX ViewModel), `SettingsFactory.ios.kt` (`NSUserDefaults`, no `initialize()` needed) |
| `settings.gradle.kts` | `:app:iosApp` already included |
| Desktop (jvm) | Full app works: `startKoin { modules(databaseModule) }` + `RootScreen()` — proof common code is target-clean |

### What is broken / missing
1. **`app`, `bookmarks`, `database` modules have no iOS targets** (android + jvm only).
2. **`app/iosApp/` is the untouched KMP wizard template**:
   - `ContentView.swift` calls `Greeting()` — that class exists nowhere in the project.
   - The Xcode build phase runs `./gradlew :shared:embedAndSignAppleFrameworkForXcode` — there is no `:shared` module, and `cd "$SRCROOT/.."` lands in `app/` where `gradlew` doesn't exist (repo root is one level up).
   - `FRAMEWORK_SEARCH_PATHS` points at `shared/build/xcode-frameworks` — wrong module, wrong path.
   - `PRODUCT_BUNDLE_IDENTIFIER = orgIdentifier.iosApp` — placeholder.
3. **Stale test that will break the build once an iOS target exists**: `app/src/iosTest/kotlin/.../IosGreetingTest.kt` references the nonexistent `Greeting`. Delete it when adding the target. (`CommonGreetingTest` is empty and harmless.)
4. No CocoaPods anywhere (good — use direct framework embedding, no plugin needed).

### expect/actual seams that must be filled for iOS
Found by grepping all `expect` declarations:

| Expect | Module | iOS actual needed | Mirror of |
|---|---|---|---|
| `createHttpClient()` | app | Darwin engine + timeouts | `HttpClientFactory.jvm.kt` |
| `rememberAppVersion()` | app | `NSBundle` CFBundleShortVersionString | `AppVersion.jvm.kt` |
| `rememberTextFileSaver(...)` | app | temp file + share sheet | `TextFileSaver.jvm.kt` |
| `rememberShareLink()` | bookmarks | UIActivityViewController | `ShareLink.jvm.kt` |
| `Modifier.imeNestedScroll()` / `imeAnimationTargetInsets()` | bookmarks | no-ops | `ImeNestedScroll.jvm.kt` |
| `platformModule: Module` | database | provides `IosDatabaseBuilder` | `DataModule.jvm.kt` |

(`MoleculeViewModel`, `SettingsFactory`, `rememberReducedMotion`, `rememberWindowSizeClass`, `PlatformAsyncImage` already have iOS actuals in `core`.)

---

## 2. Implementation steps

### Phase A — Gradle targets (3 files)

**`database/build.gradle.kts`** — add inside `kotlin { }`:
```kotlin
listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework { baseName = "database"; isStatic = true }
}
```
And extend the KSP Room compiler deps (Room generates the `LinkDatabaseConstructor` actual per target):
```kotlin
add("kspIosArm64", libs.androidx.room.compiler)
add("kspIosSimulatorArm64", libs.androidx.room.compiler)
```
Room 2.8.4 + `sqlite-bundled` (already in commonMain) fully support iOS — no new dependencies.

**`bookmarks/build.gradle.kts`** — same target block (baseName `bookmarks`), plus:
```kotlin
val iosMain by getting {
    dependencies {
        implementation(project(":database"))
        implementation(libs.molecule.runtime)
    }
}
```

**`app/build.gradle.kts`** — same target block **with `baseName = "shared"`** (keeps the Xcode `-framework shared` linker flag), plus:
```kotlin
val iosMain by getting {
    dependencies {
        implementation(project(":database"))
        implementation(libs.molecule.runtime)
        implementation(libs.ktor.client.darwin)
    }
}
```
Add to `gradle/libs.versions.toml`:
```toml
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktorClientCore" }
```

Notes:
- Only `:app`'s framework is consumed by Xcode; per-module framework blocks in database/bookmarks are harmless but optional (design/utils already have them).
- Do **not** copy `core/utils`' manual `val iosMain by creating { dependsOn(commonMain) }` wiring — the default hierarchy template creates `iosMain` automatically from the targets. (If the first iOS config ever fails inside `core/utils`/`core/design` over duplicate source sets, delete their manual wiring too.)
- `iosMain` inherits `commonMain` dependencies automatically — no need to redeclare Compose/Koin/Ktor/Navigaton3.

### Phase B — iosMain sources (~7 new files)

**`database/src/iosMain/kotlin/.../room/IosDatabaseBuilder.kt`** — mirror of `JvmDatabaseBuilder.kt`:
```kotlin
class IosDatabaseBuilder : DatabaseBuilder {
    override fun getDatabaseBuilder(): RoomDatabase.Builder<LinkDatabase> {
        val docDir = NSFileManager.defaultManager
            .URLForDirectory(NSDocumentDirectory.toLong(), NSUserDomainMask.toLong(), null, false, null)
            ?.path ?: error("No Documents directory")
        return Room.databaseBuilder<LinkDatabase>(name = "$docDir/${LinkDatabase.DATABASE_NAME}")
            .setDriver(BundledSQLiteDriver())
            .addMigrations(*ALL_MIGRATIONS)
            .fallbackToDestructiveMigration(dropAllTables = true)
    }
}
```

**`database/src/iosMain/kotlin/.../di/DataModule.ios.kt`**:
```kotlin
actual val platformModule: Module = module {
    single<DatabaseBuilder> { IosDatabaseBuilder() }
}
```

**`app/src/iosMain/kotlin/.../di/HttpClientFactory.ios.kt`** — Darwin engine, ContentNegotiation + Logging identical to the JVM file; timeouts via `engine { configureRequest { setTimeoutInterval(30.0) } }`.

**`app/src/iosMain/kotlin/.../screens/settings/AppVersion.ios.kt`**:
```kotlin
@Composable
actual fun rememberAppVersion(): String = remember {
    (NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String) ?: "dev"
}
```

**`app/src/iosMain/kotlin/.../screens/settings/TextFileSaver.ios.kt`** — v1: write to `NSTemporaryDirectory()`, present `UIActivityViewController` from the root view controller (`UIApplication.sharedApplication.keyWindow.rootViewController`), call `onDone(true/false)`. If presenting fights with Compose, ship v1 as `onDone(false)` no-op (shortcut note in code) and wire the share sheet later.

**`bookmarks/src/iosMain/.../screens/details/ShareLink.ios.kt`** — `UIActivityViewController` with the URL. (JVM-style clipboard copy is the fallback if the share sheet is fussy.)

**`bookmarks/src/iosMain/.../screens/add/sheet/ImeNestedScroll.ios.kt`** — copy the JVM no-op file verbatim.

**`app/src/iosMain/kotlin/.../MainViewController.kt`** — the iOS entry point (equivalent of `MainActivity`):
```kotlin
private val appKoin = startKoin { modules(appModule, databaseModule) } // top-level val => runs once per process

fun MainViewController(): UIViewController = ComposeUIViewController {
    // Same theme-polling pattern as MainActivity (Settings changes the preference;
    // polling every second applies it without restart)
    var darkTheme by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        val prefs = ThemePreferenceManager()
        while (true) {
            darkTheme = prefs.isDarkModeEnabled()
            delay(1000)
        }
    }
    LinkLibraryTheme(darkTheme = darkTheme ?: isSystemInDarkTheme()) {
        RootScreen() // sharedContent = null: no iOS share-intent handling in v1
    }
}
```
(`ThemePreferenceManager.isDarkModeEnabled()` is the suspend API MainActivity uses — check its exact signature; it exists in `core/utils` commonMain.)

### Phase C — cleanup
- Delete `app/src/iosTest/kotlin/com/greenrobotdev/lordofthelinks/IosGreetingTest.kt` (references nonexistent `Greeting`; compiles the moment an iOS target exists).

### Phase D — Xcode project (`app/iosApp/iosApp.xcodeproj/project.pbxproj` + Swift)
1. Build phase shell script →
   ```
   cd "$SRCROOT/../.."
   ./gradlew :app:embedAndSignAppleFrameworkForXcode
   ```
   (`../../` = repo root; task moves from `:shared:` to `:app:`.)
2. `FRAMEWORK_SEARCH_PATHS` (Debug **and** Release) → `$(SRCROOT)/../build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)` (framework output of the `:app` module).
3. `OTHER_LDFLAGS -framework shared` stays (Phase A set baseName `shared`).
4. Replace `ContentView.swift`:
   ```swift
   import SwiftUI
   import shared

   struct ComposeView: UIViewControllerRepresentable {
       func makeUIViewController(context: Context) -> UIViewController =
           MainViewControllerKt.MainViewController()
       func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
   }

   struct ContentView: View {
       var body: some View { ComposeView().ignoresSafeArea() }
   }
   ```
5. In Xcode (manual, once): set `PRODUCT_BUNDLE_IDENTIFIER` to a real id (suggest `com.greenrobotdev.lordofthelinks.ios`), select your signing team, set the app icon in `Assets.xcassets`.
6. `IPHONEOS_DEPLOYMENT_TARGET = 16.0` already set at target level — fine for Compose MP.

### Phase E — build & run
```bash
# compile check from CLI (does not need the Xcode project):
./gradlew :app:linkDebugFrameworkIosArm64          # device
./gradlew :app:linkDebugFrameworkIosSimulatorArm64 # simulator

# full run: open app/iosApp/iosApp.xcodeproj in Xcode → scheme iosApp →
# select the iPhone → Run. The build phase runs the Gradle embedAndSign task itself.
```

### Phase F — verification checklist
- [ ] App launches to the Feed (RootScreen), bottom nav works
- [ ] Add a link → metadata fetch works (Darwin engine + ATS: plain http sites will fail; https fine)
- [ ] Links survive an app restart (Room on device — Documents dir db)
- [ ] Tags/collections screens work
- [ ] Dark mode toggle in Settings applies
- [ ] Export bookmarks (share sheet appears, file saves)
- [ ] Share button on link details opens UIActivityViewController
- [ ] Welcome tour shows on first launch

---

## 3. Known limitations shipped in v1 (acceptable, by design)

| Limitation | Why | Fix later |
|---|---|---|
| Link thumbnails/favicons are gray placeholder boxes | `PlatformImage.ios.kt` is a stub; Coil 2.7 is Android/JVM-only | Migrate to `coil3` (KMP) in commonMain |
| No share-intent ("open from Safari share") handling | v1 scope | iOS share extension later |
| `rememberWindowSizeClass()` always `Compact` | Existing stub in design | Read UIScreen when iPad matters |
| `rememberReducedMotion()` always false | Existing stub | `UIAccessibility.isReduceMotionEnabled` |
| Plain-HTTP metadata fetches fail | iOS App Transport Security | Per-domain ATS exception if ever needed |

## 4. Effort estimate
With Xcode installed: **~1 day** for Phases A–D, plus debugging time for the never-compiled `core/*` iosMain code. Biggest unknowns: first Kotlin/Native compile of `core/design`+`core/utils` iOS targets, and Room KSP codegen on iOS.

## 5. What this plan deliberately does NOT do
- No CocoaPods (direct framework embedding is simpler and already half-wired).
- No share extension, no widgets, no App Store packaging — device run only.
- No changes to Android/JVM/desktop/wasm behavior; all edits are additive targets + new `iosMain` dirs + Xcode files.
