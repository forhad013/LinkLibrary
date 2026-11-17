## Summary
<!-- What changed and why, in 1–4 bullets. Mention tickets as "Refs GRE-xx" (no "Fixes"). -->
-

## Commits
<!-- `git log --format='- %s' <base>..HEAD` -->
-

## Screenshots
<!-- Light and dark theme for any UI change. Delete if none. -->

## Notes
<!-- Known gaps, follow-ups, anything a reviewer should look at closely. -->

## How to test
1. `./gradlew :app:androidApp:assembleDebug`
2.

## Checklist
- [ ] Uses `:core:design` components (cards, `AppIcon`, text fields); no raw `Card()` in feature modules
- [ ] Colors from `MaterialTheme.colorScheme`; checked light and dark themes
- [ ] Repos return `Flow<Result<T>>`; `database` does not depend on `app`
- [ ] No package renames; no wasmJs/extension work beyond keeping it compiling
- [ ] Android debug build passes
