# AGP 9 Rules & Key Patterns

## AGP 9 rules (critical)
- KMP modules must use `id("com.android.kotlin.multiplatform.library")` — **not** `kotlin("multiplatform")` + `com.android.library`
- Both `android {}` (namespace, sdk, compileOptions) and `androidTarget {}` (compilerOptions) blocks required
- No `kotlinOptions` — use `compilerOptions` in `androidTarget`
- Compose via `org.jetbrains.kotlin.plugin.compose`; no `buildFeatures.compose`

## Key patterns
- Repos return `Flow<Result<T>>`; use reactive `.collect {}` for data that updates during interaction, `.first()` only for one-shot reads
- `database` module must not depend on `app` models — use extension functions in `app/src/commonMain/kotlin/com/greenrobotdev/linklibrary/model/` for entity↔domain conversion
- Tags/collections live in relationship tables (`LinkTagEntity`), not JSON fields — use `getLinksWithTags()`
- Navigation: `Navigator.navigateToNew(route)` removes same-type route before adding (fixes FAB reuse)
