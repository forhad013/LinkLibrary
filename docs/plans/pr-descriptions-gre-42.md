# PR titles & descriptions — `gre-42-nested-categories` split

Each body ends with the required attribution line. Paste as-is into the GitHub "Create PR" form.
Order matters: PR 1 → `main`, each later PR → the previous branch.

---

## PR 1 — `pr/1-nested-categories-nav` → `main`

**Title:**
```
feat: nested categories data model, new bottom nav, Feed, Library/Manage, wireframe theme
```

**Body:**
```markdown
## Summary
- Nested categories (up to 5 levels) with **one category per link** and new link states (GRE-42), plus an early category picker (GRE-44)
- New bottom navigation: Feed / Search / Library / Settings, with an empty Feed (GRE-49)
- Library tab (smart lists, category tree, tags) and a first-pass Manage screen
- Wireframe theme: light colors, Roboto Flex typography, extras via CompositionLocal

Refs GRE-42, GRE-44, GRE-49.

## Commits
- feat: nested categories, one category per link, new link states (GRE-42) + category picker (GRE-44)
- feat: new bottom nav Feed/Search/Library/Settings with empty Feed (GRE-49)
- feat: Feed shows link list with empty state only when no links (GRE-49 follow-up)
- feat: Library tab per new design (smart lists, categories, tags) + Manage screen
- feat: wireframe theme + Library/Manage layouts, recursive category tree, Move via category picker
- feat: Feed first-launch empty state per design (GRE-49)
- feat: wireframe theme — light colors, Roboto Flex typography, extras via CompositionLocal

## Notes
- The first commit (`7b88096`) mixes GRE-42 and GRE-44 work; the picker is expanded in PR 4.
- Manage's category flows are a first pass here — replaced by the embedded picker in PR 4.

## How to test
1. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:androidApp:assembleDebug`
2. Launch: bottom nav shows 4 tabs; Feed shows the empty state on first launch.
3. Library tab: smart-list tiles, category tree, tags render; categories nest when created under a parent.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

---

## PR 2 — `pr/2-add-link-sheet` → `pr/1-nested-categories-nav`

**Title:**
```
feat: Add-link modal bottom sheet
```

**Body:**
```markdown
## Summary
Add-a-link modal bottom sheet per the approved design:
- Sheet rises together with the keyboard; scrolling fixed while the keyboard is open
- Wired into root navigation; design mocks updated
- Adds `roboto_flex.ttf`

## Commits
- feat: Add a link modal bottom sheet per approved design
- feat: wire Add-link sheet into root navigation; update design mocks
- feat: Add link sheet rises together with the keyboard
- Fix scrolling effect when keyboard open with modal sheet
- Add roboto_flex.ttf
- test

## Notes
- ⚠️ The commit titled **"test"** (`b0bbf45`) is a real change — it simplifies `AddLinkSheet.kt`
  (−51/+10). The message is just misnamed; left as-is to avoid rewriting later SHAs.

## How to test
1. Build (see PR 1), launch, open the Add-link entry point.
2. The sheet opens as a modal; focus the URL field — the sheet tracks the keyboard; scrolling works with the keyboard open.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

---

## PR 3 — `pr/3-icon-docs-flow-fix` → `pr/2-add-link-sheet`

**Title:**
```
feat(GRE-50): Crown Bookmark launcher icon; docs; flow cancellation fix
```

**Body:**
```markdown
## Summary
- Adaptive launcher icon — Crown Bookmark (GRE-50)
- CLAUDE.md: device-automation safety rules for adb on the owner's phone
- Kotlin flows: rethrow `CancellationException` in flow builders so coroutine cancellation propagates correctly

Refs GRE-50.

## Commits
- feat(GRE-50): Crown Bookmark adaptive launcher icon
- docs: add device-automation safety rules to CLAUDE.md
- fix: rethrow CancellationException in flow builders

## How to test
1. Build (see PR 1); launcher icon shows the Crown Bookmark adaptive icon (light/dark backgrounds).
2. Cancel a screen mid-load (navigate away quickly) — no stuck loading states.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

---

## PR 4 — `pr/4-category-picker` → `pr/3-icon-docs-flow-fix`

**Title:**
```
feat(GRE-44): category picker sheet and Manage categories
```

**Body:**
```markdown
## Summary
The category picker (GRE-44), end to end:
- **Data layer**: DAO/repository support — depth checks, reparent, cascade & keep-children deletes, recent categories
- **Modal bottom sheet**: search-or-create, recent chips, tree browse with counts
- **Delete flow**: impact card, keep-children/delete-all choice, swipe-to-confirm bar, 5s undo
- **Inline editing**: rename, add-sub, new-top-level fields; row selection disabled while editing; tree-collapse fix
- **Refactor**: 1169-line screen split; reusable pieces (`AppActionBar`, `SwipeConfirmBar`, `AppUndoBar`, `BaseBottomSheet`, `DashedPillButton`) moved to `:core:design`
- **Manage**: Categories tab embeds the picker content directly (no sheet); `AddCategoryScreen` and its routes removed; Tags tab keeps the pill list
- Keyboard no longer pushes Manage content (`adjustResize` + inset fixes)

Refs GRE-44.

## Commits
- feat(database): category picker data layer
- feat(GRE-44): Category picker modal bottom sheet
- docs(GRE-44): category picker spec and design mocks
- feat(GRE-44): delete flow state, tree-collapse fix, and DAO support
- feat(GRE-44): inline rename, add-sub, and new-top-level fields
- fix(design): reduce text field padding, add clear button
- feat(GRE-44): delete flow UI — impact card, swipe bar, undo bar
- fix(GRE-44): disable row selection during inline editing
- refactor(GRE-44): split category picker; extract reusable components to :core:design
- feat(GRE-44): manage categories via embedded picker; remove AddCategoryScreen
- fix(GRE-44): keyboard no longer pushes manage screen content

## Notes
- Deleting a parent offers **Keep children** (reparent up a level) or **Delete all** (cascade); links are never deleted — they fall back to No category.
- Undo is a 5s in-memory window: the DB write happens only when the timer expires; closing the screen mid-window abandons the delete.
- `:bookmarks:jvmTest` doesn't run — pre-existing `viewModel {}` compile errors on JVM in unrelated screens (`:database:jvmTest` passes).

## How to test
1. Build (see PR 1).
2. Add-link → Category field → picker sheet: search/create/select; create nested categories (max depth 5 enforced).
3. Rename/add-sub inline; delete with swipe + undo within 5s.
4. Library tab → Manage → Categories tab: the same picker inline; bottom bar doesn't move when the keyboard opens.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```

---

## PR 5 — `pr/5-tag-picker-library` → `pr/4-category-picker`

**Title:**
```
feat(GRE-44): tag picker, Manage tags tab, Library restyle
```

**Body:**
```markdown
## Summary
- **Tag picker sheet**: search, create, rename, delete with undo (mirrors the category picker); `TagRepository.getTagsWithCounts()`
- **Add link**: a Tags field opens the tag picker (`AddLinkScreen` loses its `onAddTag` param)
- **Manage**: Tags tab embeds the tag picker (`ManageViewModel` removed); segmented tab switcher; 12dp bottom gap
- Standalone `CategoriesScreen` removed; Organize category routes open Manage
- Fix: root `consumeWindowInsets` so `imePadding` doesn't double-count the bottom nav bar
- Library tab restyled to the categories & tags mock (Manage pill kept, no ⋮ menus)

Refs GRE-44.

## Commits
- refactor(GRE-44): remove standalone CategoriesScreen
- feat(GRE-44): tag picker sheet with search, create, rename, delete + undo
- feat(GRE-44): pick link tags via the tag picker sheet in Add link
- feat(GRE-44): Manage tags tab uses the tag picker; restyle tab switcher
- fix(GRE-44): stop imePadding double-counting the bottom nav bar
- feat(GRE-44): restyle Library tab to the categories & tags mock

## Known gaps
- No "Recent" row for tags (assignment timestamps aren't tracked in the DB yet)
- Link details doesn't use the tag picker yet
- Old `TagsScreenEnhanced` / `AddTagScreen` are still routed
- No tests ran (`jvmTest` blocked by pre-existing JVM compile errors)

## How to test
1. Build (see PR 1).
2. Add link → Tags field → tag picker: create/rename/delete with undo; tags apply to the link.
3. Manage → Tags tab: same picker inline; keyboard doesn't double-pad above the bottom nav.
4. Library tab matches the categories & tags mock.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```
