# CategoryPickerSheet refactor plan (GRE-44 follow-up)

**Status: DRAFT — awaiting approval. No code will be changed until you approve.**

`CategoryPickerScreen.kt` is 1169 lines mixing five concerns: sheet hosts, picker
orchestration, tree rows, an action-bar family, and a delete-flow UI (impact card,
parent choice, swipe track, undo bar). This plan splits it along the seams, moving
generic pieces to `:core:design` so other modules can reuse them, and extracting a
presenter from the ViewModel following the existing `AddCategory*` pattern.

---

## Reference pattern (as requested)

`addCategory/` today:

| File | Role |
|---|---|
| `AddCategoryViewModel.kt` | thin `MoleculeViewModel` shell, delegates to presenter |
| `AddCategoryUseCase.kt` | `AddCategoryPresenter()` — event→state reducer + repository writes, deps via `koinInject()` |
| `AddCategoryStateModels.kt` | state + events |
| `AddCategoryScreen.kt` | UI only |

The picker will mirror this shape exactly. Note one deliberate deviation:
`AddCategoryScreen` uses `viewModel(key…)` (AndroidX); the picker's two hosts keep
`remember { CategoryPickerViewModel(...) }` because the sheet must be cheap and
disposable per open, and changing that is not part of this refactor.

## What is reusable vs picker-specific

Audited every private composable in the file:

| Component | Reusable? | Destination |
|---|---|---|
| `dashBorder()` (DashBorder.kt) | ✅ generic modifier | `design/extensions/ModifierExt.kt` (move file content, delete file) |
| `DashedPill` | ✅ generic ("+ New …" affordance) | `design/components/button/DashedPillButton.kt` |
| `BarContainer` / `BarPill` / `BarAction` | ✅ generic action bar family | `design/components/layout/ActionBarFamily.kt` → public `AppActionBar`, `AppBarAction`, `AppBarPill` |
| `SwipeDeleteBar` | ✅ generic confirm-to-delete track (Keep + drag-to-confirm) | `design/components/layout/SwipeConfirmBar.kt` → `SwipeConfirmBar` (labels/icons/confirm threshold parameterized) |
| `UndoBar` | ⚠️ mostly generic; "deleted / uncategorized" copy + 5s ring | `design/components/layout/UndoBar.kt` → `AppUndoBar(title, subtitle, durationMillis, onUndo)` |
| Sheet host scaffolding (fixed 92%-height, ime/nav padding, header + round close button) | ✅ any bottom sheet needs this | `design/components/layout/BaseBottomSheet.kt` → `BaseBottomSheet(title, onClose, content)` |
| Rounded tonal icon button (close X) | ✅ | folded into `BaseBottomSheet` header (single consumer for now; not a separate public API) |
| `RecentChip` | ⚠️ generic outline pill chip, but trivial | stays picker-local (10 lines; promote when a 2nd consumer appears) |
| `SelectableRow` (indent gutter, chevron, selected/danger states) | ⚠️ structural but domain-flavored | stays picker-local **in this pass** — its params encode tree semantics (`depth`, `expandable`). Promote to design later as `TreeRow` if the tag-tree screen needs one |
| `CreateRow`, `InlineTreeField`, `ImpactCard`, `ParentChoiceCard` | ❌ picker-specific copy/layout | stay in bookmarks, split into their own file |
| `PickerItem`/`buildPickerItems`, hosts, `CategoryPickerContent` | ❌ orchestration | stays in bookmarks |

Rule applied: **hardcoded colors are the line.** Anything moving to `:core:design`
must use only `MaterialTheme.colorScheme.*`. The delete-flow UI currently hardcodes
~8 hex colors (`0xFFFBE9E7`, `0xFF9A2418`, `0xFF3A3D45`, `0xFFFFB4A6`, `0xFFD32F2F`,
`0xFFF3C9C2`, `0xFFFFCDD2`, white). They map 1:1 to existing scheme roles
(`errorContainer`/`onErrorContainer` light values are exactly `FBE9E7`/`9A2418`,
`inverseSurface` dark bar = `3A3D45` family, thumb armed = `error`). CLAUDE.md
mandate: "Colors: always MaterialTheme.colorScheme — never hardcode". Migration is
part of the move; visual output stays identical in light theme by construction, and
dark theme gets *more* correct (currently dark mode shows the light-red literals).

## Target file layout

```
core/design/.../components/layout/
  ActionBarFamily.kt      (new) AppActionBar + AppBarAction + AppBarPill
  SwipeConfirmBar.kt      (new) generic Keep + drag-to-confirm bar
  UndoBar.kt              (new) AppUndoBar with countdown ring
  BaseBottomSheet.kt      (new) title + close + fixed-height padded scaffold
core/design/.../components/button/
  DashedPillButton.kt     (new)
core/design/.../extensions/
  ModifierExt.kt          (+dashBorder)

bookmarks/.../categoryPicker/
  CategoryPickerSheet.kt   (renamed from CategoryPickerScreen.kt) hosts only (~90 lines)
  CategoryPickerContent.kt (new) orchestration + footer + bar switching (~300 lines)
  CategoryPickerRows.kt    (new) SelectableRow, RecentChip, CreateRow, InlineTreeField (~280 lines)
  CategoryPickerDeleteUi.kt(new) ImpactCard, ParentChoiceCard (~180 lines)
  CategoryPickerViewModel.kt → thin shell (~30 lines)
  CategoryPickerPresenter.kt (new) event→state reducer + repo writes (~280 lines)
  CategoryPickerStateModels.kt, CategoryTree.kt, DashBorder.kt(deleted) unchanged otherwise
```

The sheet file drops 1169 → ~90 lines; the largest picker file becomes ~300.

## Phases (each compiles + tests green, committed separately for bisectability)

1. **Design-module extractions** — move `dashBorder`; add `AppActionBar` family,
   `DashedPillButton`, `BaseBottomSheet`, `SwipeConfirmBar`, `AppUndoBar` to
   `:core:design` with hex→colorScheme migration. Picker consumes them; no behavior
   change.
2. **Presenter split** — `CategoryPickerViewModel` becomes the thin shell;
   reducer/repository logic moves to `CategoryPickerPresenter.kt`
   (`initialState`, `events`, `categoryRepository = koinInject()` signature, same
   as `AddCategoryPresenter`). Pure helpers already in `CategoryTree.kt` stay put.
   The `undoJob` timer moves into the presenter via `rememberCoroutineScope`-
   equivalent molecule pattern (LaunchedEffect on phase change).
3. **File split** — break the screen into the four files above. Pure moves, no
   signature changes to `CategoryPickerSheet`/`CategoryPickerScreen` public APIs.
4. **Verification** — `:bookmarks:compileAndroidMain`, `:bookmarks:compileKotlinMetadata`,
   `:database:jvmTest` green; manual device check of sheet open/select/create/rename/delete
   flows (deploy gate: I'll ask before installing).

## Non-goals

- No behavior changes: same events, same state, same visuals (light theme pixel-identical).
- No `viewModel{}` migration for the picker hosts (sheet lifecycle stays `remember`).
- No `SelectableRow`/`RecentChip` promotion to design (no second consumer yet).
- No package renames, no wasmJs work, no new dependencies.

## Risks

- **Dark theme will change** where the delete-flow hex literals previously leaked
  light-red into dark mode — that's a fix, but it is a visible change.
- `BaseBottomSheet` bakes the 92%-height + ime-padding conventions; if a future
  sheet needs different metrics they become parameters (only when asked).
- The UI agent (if still active on this file) must rebase onto phase 1–3; landing
  order matters if their work is uncommitted.

---

**Approve to proceed, or tell me what to change.** Default on approval: phases 1→4
in order, pausing if any compile/test gate fails.
