# Prompt: Implement the Category Picker sheet (GRE-44)

You are working in the **Link Library KMP** repo (Kotlin Multiplatform, Compose, Room KMP, Koin, Navigation 3, MVVM + MoleculeViewModel).
Your task: rebuild the **Category picker** as a modal bottom sheet that matches the approved design exactly.

Before you write code, read:
1. `CLAUDE.md` and `claude-project/instruction.md`: the project rules and hard "don'ts" (no package renames, no wasmJs/extension work).
2. `docs/architecture/AGP9_AND_PATTERNS.md`: repositories return `Flow<Result<T>>`; collect them with `.collect {}`, not `.first()`.
3. Linear ticket **GRE-44**: the source of truth for the acceptance criteria. It depends on **GRE-42** (nested category data model) and shares rename/delete behaviour with **GRE-48**.

---

## Where the UI is

| What | Path |
|---|---|
| Screenshot (the target look) | `design/screens/Category picker search, recent, browse.png` |
| UI text strings | `design/text/category_picker.txt` |

Open the screenshot first. Match the layout, order, spacing and states. Do **not** copy the mock's hex colors: map every color to `MaterialTheme.colorScheme`.

## Existing code to build on (don't start from scratch)

- Screen: `bookmarks/src/commonMain/kotlin/com/greenrobotdev/lordofthelinks/bookmarks/screens/categoryPicker/CategoryPickerScreen.kt`
- State/events: `.../categoryPicker/CategoryPickerStateModels.kt` (`CategoryPickerState`, `CategoryPickerEvent`)
- ViewModel: `.../categoryPicker/CategoryPickerViewModel.kt`
- Domain model: `bookmarks/src/commonMain/kotlin/com/greenrobotdev/lordofthelinks/bookmarks/model/Category.kt` (has `path(categories)` for breadcrumbs)
- Repository: `database/src/commonMain/kotlin/com/greenrobotdev/lordofthelinks/database/repository/CategoryRepository.kt`. It already has `getCategoriesWithCounts`, `addCategory`, `renameCategory`, `moveCategory`, `deleteCategory`, `getCategoryForLink` and `assignLinkToCategory`. Reuse these. Add a repository method only when one is truly missing.
- Delete logic to share: `bookmarks/.../screens/manage/ManageViewModel.kt` (`PendingDelete`)
- Sheet pattern to copy (keyboard/IME handling, sheet behaviour): `bookmarks/.../screens/add/sheet/AddLinkSheet.kt`, `ImeNestedScroll.kt`
- Current navigation entry: `app/src/commonMain/kotlin/com/greenrobotdev/lordofthelinks/screens/organize/navigation/OrganizeTabNavigation.kt`
- Design system: `core/design/...`, including `components/textfield/SearchField.kt`, `components/selection/SelectionPill.kt`, `components/button/*`, `components/cards/FormCards.kt`, and `theme/Color.kt`

---

## Screen requirements

### 1. Sheet
- Modal bottom sheet with a drag handle at the top.
- The header shows the title **"Category"** (large, bold) and a round close button (✕) on the right.
- Close, swipe-down or back closes the sheet **without changing** the link's category.
- Single selection: a link has at most one category.

### 2. Search / create field
- A pill-shaped field with a search icon and the placeholder **"Search or create a category"**.
- Typing filters the tree live, case-insensitively, at any depth. Each result shows its full breadcrumb (e.g. `Health › Sleep`).
- If no category name matches the text exactly, show a **Create "<text>"** row. It creates the category inside the currently selected category, or at the top level if "No category" is selected, and then selects it.
- Clearing the field restores the full tree.

### 3. Recent row
- The label **"Recent"**, followed by a horizontally scrolling row of outlined chips, one per recently used category, each showing its breadcrumb (e.g. `Health › Sleep`, `Dev › Android`, `Study: JKU`).
- Tapping a chip selects that category and expands its ancestors in the tree.
- Hide the whole row when there's no history.
- Recent = categories most recently assigned to links. Limit it to 5 chips unless GRE-44 says otherwise.

### 4. Browse tree
- The first row, **"No category"**, clears the selection.
- One row per category, indented by depth.
- Parents show a chevron: `>` when collapsed, `v` when expanded. Tapping the chevron toggles expand/collapse. Tapping the row selects it.
- Each row shows its link count `[N]` on the right.
- The selected row is highlighted with a filled rounded container (`secondaryContainer`/`primaryContainer` with matching `on*` text) and bold text.
- On open, the link's current category is selected and its ancestors are expanded.
- A **"+ New top-level category"** button sits below the tree as a dashed-outline pill. It creates a root category; the full flow is in section 4a.

### 4a. New top-level category flow
Design: the "Category picker: new top-level category" canvas (https://claude.ai/artifact/8GGL3jNXvvwYJduRxdq4Lr, linked on GRE-44), with three states: typing, duplicate name, created.
1. **Name it:** tapping the dashed pill replaces it with an **inline name field at the root of the tree**.
   - The field is auto-focused with the keyboard open, and has a folder-plus icon, a primary 2dp outline and a clear (✕) icon button.
   - Hint below it: "Top level · you can add subcategories after".
   - The list scrolls so the field stays above the keyboard. Reuse the IME handling from `AddLinkSheet.kt` / `ImeNestedScroll.kt`.
   - The footer reads **"New top-level: <name>"**.
   - The action bar switches to **Cancel** (icon + label) and a primary **Create** pill. The keyboard's Done/✓ action also creates.
2. **Validation:** trim the name.
   - Create is disabled while the name is empty or matches a sibling's name (case-insensitive).
   - A duplicate shows the error state: an `colorScheme.error` outline and icon, plus the message *"<name>" already exists at the top level*.
3. **Created:** insert the category at the root, in alphabetical order, with count `[0]`, and **select and highlight it**.
   - The footer shows "Selected: <name>", and the normal action bar returns.
   - Creating does **not** assign the category to the link. The user still taps "Use this".
4. **Cancel:** Cancel, system back, or tapping outside discards the field and restores the previous selection. Nothing is written.
- **State:** add `newCategory: NewCategoryDraft?` (`name`, `error`) to `CategoryPickerState`, plus the events `StartNewTopLevel`, `NewNameChanged`, `ConfirmNewCategory` and `CancelNewCategory`.

### 5. Footer
- A line reading **"Selected: <breadcrumb>"**, with the breadcrumb in bold (e.g. `Selected: Health › Sleep`). Show "Selected: No category" when nothing is selected.
- A dark rounded action bar, pinned to the bottom (above the keyboard/navigation bar), containing:
  - **Add sub** (folder-plus icon): creates a child of the selected category. Disabled when the selected category is at depth 5.
  - **Rename** (pencil icon): renames the selected category. Reject empty names and names already used by a sibling.
  - **Remove** (trash icon, error color): opens the same confirm dialog as GRE-48: *Delete "<name>"?* … *"No links are deleted."* … **Cancel** / **Delete**. Links in the removed category become uncategorized. If the removed category was selected, the selection falls back to "No category".
  - **Use this** (check icon, primary/accent filled pill): assigns the selection to the link (`assignLinkToCategory`, or clears it for "No category") and closes the sheet.
- Add sub, Rename and Remove are disabled when "No category" is selected.

### 6. Rules and validation
- Maximum depth is **5 levels**. Enforce it in the UI (Add sub disabled, Create blocked) **and** in the data layer (GRE-42).
- Trim names. Reject empty or whitespace-only names. Names must be unique among siblings (case-insensitive).
- Show errors with the existing `error` / `ClearError` pattern in `CategoryPickerState`.
- Never lose data: a failed create, rename or delete leaves the tree unchanged and shows the error.

### 7. Open questions (check GRE-44 for answers before implementing; if still open, use the default and leave a `shortcut:` comment)
- Removing a category that has subcategories: **default = block it and show "Move or remove subcategories first."**
- How many Recent chips, and per-device or synced: **default = 5, per-device.**
- Search matching parent names: **default = match the category's own name only.**

---

## Design system rules (mandatory)

- Use `:core:design` components first. No raw `androidx.compose.material3.Card` in feature modules.
- Icons: `AppIcon` only (Regular/Compact), with no custom icon backgrounds.
- Inputs: `SearchField` / `AppTextFieldEnhanced` from `:core:design`.
- Colors: `MaterialTheme.colorScheme` only. Never hardcode the mock's hex values or brand colors.
- Shapes and spacing per `CLAUDE.md`: cards `RoundedCornerShape(24.dp)`, compact padding 16.dp, buttons 12.dp. The search field, chips and the "Use this" button are fully rounded pills, as in the mock.
- Light and dark themes must both pass WCAG AA contrast.
- Motion: follow the `compose-motion` skill and Material 3 Expressive (`material-3-expressive` skill) for expand/collapse and selection changes.
- Accessibility: every icon button has a content description, every tap target is at least 48dp, and expand state is announced (e.g. "Health, expanded, 3 links").

## State and architecture

- Extend `CategoryPickerState` with `query`, `expandedIds`, `recent`, the visible tree rows (flattened, with depth), and dialog state. Extend `CategoryPickerEvent` with `Search`, `ToggleExpand`, `AddSub`, `Rename`, `Remove`, `ConfirmRemove`, `UseThis` and `Dismiss`.
- Keep the business logic in the ViewModel/use cases. The composable only renders state and sends events.
- Wire the sheet so it opens from the Add-link sheet's Category field and from the Organize tab entry that already exists.

## Done when

- [ ] The sheet matches `design/screens/Category picker search, recent, browse.png` in light and dark themes.
- [ ] Every acceptance criterion in GRE-44 / sections 1–6 above works on Android and JVM.
- [ ] There are unit tests for the ViewModel's tree flattening, search filtering, the depth-5 limit and name validation, plus a DAO test (alongside `database/src/jvmTest/.../CategoryDaoTest.kt`) for any new query.
- [ ] `./gradlew :bookmarks:allTests :database:jvmTest` passes, and the Android app builds.
- [ ] Nothing has been committed, pushed or deployed without the owner's approval.
