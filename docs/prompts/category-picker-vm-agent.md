# Category Picker — ViewModel, Domain & Tests

## Context

You are working on the category picker bottom sheet (GRE-44) in a Kotlin Multiplatform project.
Branch: `gre-42-nested-categories`. Working dir: `/Volumes/Projects/KMP project/LinkLibrary`.

Another agent is handling all Compose UI changes in `CategoryPickerScreen.kt`. **You must not edit that file.** Your scope is the ViewModel, state models, tree helpers, domain/repository, and tests.

## Key files

- `bookmarks/src/commonMain/kotlin/.../categoryPicker/CategoryPickerViewModel.kt` — Molecule-based VM
- `bookmarks/src/commonMain/kotlin/.../categoryPicker/CategoryPickerStateModels.kt` — state, events, dialog model
- `bookmarks/src/commonMain/kotlin/.../categoryPicker/CategoryTree.kt` — tree helpers, validation
- `database/src/commonMain/kotlin/.../repository/CategoryRepository.kt` — Room repository
- Design canvas: https://claude.ai/artifact/Jj4cgMh3WxFWe4wozjkUt8 (boards: delete, rename, add-sub)

## Build

```bash
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :bookmarks:assembleDebug
```

## Tasks (in priority order)

### 1. Fix tree-collapse bug (HIGH PRIORITY)

Expanded tree rows collapse by themselves ~5 seconds after expanding. The ViewModel uses `MoleculeViewModel` with `moleculeFlow` + `stateIn(SharingStarted.Lazily)`. The VM is created with `remember{}` inside `CategoryPickerSheet`.

**Suspects:**
- The `getCategoriesWithCounts()` flow re-emits and the `state.copy(categories = categories)` on the non-loading path might reset `expandedIds` indirectly (it doesn't — but the re-emission itself triggers recomposition)
- MoleculeViewModel internal collect behavior with Lazily — does it restart?
- The `remember{}` scope might be getting recreated

**Debug logging is already in place** — look for `println("LLDBG ...")` lines in the ViewModel. Remove them all once the bug is fixed.

### 2. Delete flow — new state & events

The UI agent is building a swipe-to-delete track, an undo bar, and a parent-choice card. You need to provide the state and logic they'll read from.

**New state fields** (add to `CategoryPickerState`):
```kotlin
// Delete flow phases
val deletePhase: DeletePhase = DeletePhase.NONE,
val deletingCategory: Category? = null, // the category being deleted
val deletedLinkCount: Int = 0, // how many links lose this category
val parentDeleteChoice: ParentDeleteChoice? = null, // user's choice for children
```

**New enum/sealed types** (add to `CategoryPickerStateModels.kt`):
```kotlin
enum class DeletePhase { NONE, CONFIRM, SWIPING, UNDO }
enum class ParentDeleteChoice { KEEP_CHILDREN, DELETE_ALL }
```

**New events**:
```kotlin
object SwipeComplete : CategoryPickerEvent // user finished swiping → soft delete, start undo timer
object UndoDelete : CategoryPickerEvent // user tapped undo → restore
data class ParentDeleteChoice(val choice: ParentDeleteChoice) : CategoryPickerEvent
```

**Logic in VM:**
- `Remove` event → set `deletePhase = CONFIRM`, `deletingCategory`, compute `deletedLinkCount` from repository. If the category has children, also set state so the UI shows the parent-choice card.
- `SwipeComplete` → soft delete (remove from DB), set `deletePhase = UNDO`, start a 5-second coroutine. If timer expires → hard confirm (phase back to NONE). Selection falls to parent or null.
- `UndoDelete` → cancel the timer, re-insert the category, set `deletePhase = NONE`.
- `ParentDeleteChoice.KEEP_CHILDREN` → move children up one level before delete.
- `ParentDeleteChoice.DELETE_ALL` → delete the category and all descendants.

**Repository:** You may need `getCategoryLinkCount(categoryId): Flow<Result<Int>>` and `moveCategoryChildrenUp(categoryId)` if they don't exist.

### 3. Add-sub inline — new state support

The UI agent is replacing the old `InlineNamePanel` with an inline field in the tree. The existing `AddSub` event and `DialogName` handling already work. **No VM changes needed** unless the UI agent requests something — the existing ADD_SUB dialog flow with `categoryId != null` is sufficient.

One thing to verify: when `DialogName` creates the sub-category, the new category should auto-select and the parent should auto-expand. This already happens in the current code — just confirm it works.

### 4. Rename inline — verify existing flow

Same as add-sub: the UI agent is replacing `InlineNamePanel` for RENAME with an inline tree field. The existing `Rename` event and `DialogName(RENAME)` handling should work. Verify rename with validation (sibling name taken) works correctly.

### 5. Tests

Write tests for:
- **Delete flow:** confirm → swipe → undo within 5s restores; confirm → swipe → wait 5s → gone; parent with children → keep children moves them up; parent with children → delete all removes descendants
- **Add sub:** creates under parent, auto-selects, validates sibling name uniqueness
- **Rename:** renames, validates sibling uniqueness (excluding self), handles empty name
- **Tree collapse bug:** whatever the fix is, write a regression test

Test location: `bookmarks/src/commonTest/` (create if needed). Use the existing test patterns in the project.

## Do NOT

- Edit `CategoryPickerScreen.kt` (the UI agent owns that file)
- Change package names or module structure
- Add dependencies
- Work on wasmJs or browser extension
- Commit without being asked
