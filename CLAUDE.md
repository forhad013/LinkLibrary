# Link Library KMP

Kotlin Multiplatform link/bookmark manager (Android, JVM, WASM). Room KMP + SQLite, Koin DI, Compose with Navigation 3.0, MVVM + UseCase.

**Read `claude-project/instruction.md`** — project goals, milestones, working preferences, and hard "don'ts" (no package renames, no wasmJs/extension work in milestone 1, manual deploy gates).

## Modules
- `app/` — main KMP app, screens, view models (Android wrapper in `app/androidApp`)
- `bookmarks/` — bookmark screens (add, addTag, addCollection, library, details, tags, collections)
- `core/design/` — design system (cards, icons, textfields, selection, theme)
- `core/utils/` — MoleculeViewModel
- `database/` — Room entities/DAOs, repositories, `di/DataModule.kt`
- `build-logic/` — convention plugins

## Versions
AGP 9.0.0 · Kotlin 2.2.10 · Gradle 9.1.0 · KSP 2.2.10-2.0.2 · JDK 17+

## AGP 9 & key patterns
See `docs/architecture/AGP9_AND_PATTERNS.md`. Highlights: KMP modules use `com.android.kotlin.multiplatform.library` (not `com.android.library`); repos return `Flow<Result<T>>` (reactive `.collect {}`, not `.first()`); never depend on `app` from `database`.

# Design System (TheLordsOfLinks — authoritative)

Material 3 Expressive, Royal Purple `#6D28D9` + Gold `#F59E0B`. Source of truth: `core/design/src/commonMain/kotlin/com/greenrobotdev/linklibrary/design/theme/Color.kt`.

## Mandatory
- **Cards**: always use templates from `:core:design` (`FormCard`, `ContentCard`, `SectionContentCard`, `ToggleCard`, `InputCard`). Never use raw `androidx.compose.material3.Card` in feature modules.
- **Icons**: always `AppIcon` (variants: Regular, Compact) — no custom icon backgrounds
- **Inputs**: use `AppTextFieldEnhanced`, `AppTextFieldWithCharacterLimit` from `:core:design`
- **Colors**: always `MaterialTheme.colorScheme` — never hardcode brand colors
- **Styling**: cards `RoundedCornerShape(24.dp)`, elevation 4.dp, border `1.dp outlineVariant.copy(alpha = 0.3f)`, padding 24.dp, content spacing `Arrangement.spacedBy(20.dp)`; compact 16.dp; buttons 12.dp

## When adding/modifying screens
1. Reuse `:core:design` components before writing custom UI
2. Migrate any raw `Card()` to templates
3. Test both light and dark themes (WCAG AA)

Last updated: 2026-11-12
