# PR plan: split `gre-42-nested-categories` into 5 stacked PRs

Repo: `forhad013/LinkLibrary` (remote `origin`). Base: `main` @ `622d1a16121f95a764d0c03e8e6d358e0a9865d6`.
Source branch: `gre-42-nested-categories` — 33 commits ahead of `main`, 0 behind (no rebase needed).

**Scope: create PRs only.** Do not close, move or comment on Linear tickets. Do not merge. Do not enable auto-merge.

## Why stacked
Commits build on each other, so each PR targets the previous PR's branch, not `main`. PR 1 targets `main`. After PR N merges, retarget PR N+1 to `main` (GitHub usually does this automatically when the base branch is deleted on merge).

## Prerequisites
1. `gh` CLI installed and signed in (`gh auth status` must pass). If it isn't, stop and ask the owner. Never sign in on their behalf.
2. A JDK for Gradle: `export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"` (there's no system Java).
3. A clean working tree on `gre-42-nested-categories`.

## Step 0: verify the build (do this before pushing)
The last two commits (`a096495`, `489d5eb`: Manage tab switcher and Library tab restyle) were never compiled.
```bash
./gradlew :app:androidApp:assembleDebug
```
If it fails, stop and report. Don't fix it silently inside a PR branch.
Note: `:bookmarks:compileKotlinJvm` / `jvmTest` already fail on `main`-era files (`viewModel {}` intersection-type errors in LibraryScreen, LinkDetailScreen, AddTagScreen, …). That's pre-existing and not caused by these PRs. Use the Android build as the check.

## Step 1: create the branches (boundary commits, no rewriting)

| PR | Branch | Tip commit (inclusive) | Base |
|---|---|---|---|
| 1 | `pr/1-nested-categories-nav` | `a262b9c6fa68f44ff1de023848150ded3d1a664d` | `main` |
| 2 | `pr/2-add-link-sheet` | `b0bbf45e95a5936bb7bdb1ee1336a32e075253a1` | `pr/1-nested-categories-nav` |
| 3 | `pr/3-icon-docs-flow-fix` | `1b832775909e56232462f548eb3e7800e14b6bda` | `pr/2-add-link-sheet` |
| 4 | `pr/4-category-picker` | `2d520252df2feb52571f8bd0acad44c023245aea` | `pr/3-icon-docs-flow-fix` |
| 5 | `pr/5-tag-picker-library` | `489d5ebb0d44f0ec96f5dbe446c43c4266f3628f` | `pr/4-category-picker` |

```bash
git branch pr/1-nested-categories-nav a262b9c6fa68f44ff1de023848150ded3d1a664d
git branch pr/2-add-link-sheet        b0bbf45e95a5936bb7bdb1ee1336a32e075253a1
git branch pr/3-icon-docs-flow-fix    1b832775909e56232462f548eb3e7800e14b6bda
git branch pr/4-category-picker       2d520252df2feb52571f8bd0acad44c023245aea
git branch pr/5-tag-picker-library    489d5ebb0d44f0ec96f5dbe446c43c4266f3628f
git push -u origin pr/1-nested-categories-nav pr/2-add-link-sheet pr/3-icon-docs-flow-fix pr/4-category-picker pr/5-tag-picker-library
```
Never force-push. Don't modify or delete `gre-42-nested-categories`.

Optional, ask the owner first: commit `b0bbf45` is titled just "test" but contains a real change (AddLinkSheet.kt, −51/+10). Rewording it would rewrite every later SHA, so **leave it as is** unless the owner says otherwise; mention it in PR 2's body.

## Step 2: open the PRs
For each PR: `gh pr create --base <base> --head <branch> --title "<title>" --body-file <file>`.
Write the body from the commit messages (`git log --format='- %s' <base>..<branch>`) plus the notes below. Keep it short: a summary, then a commit list, then notes, then how to test. Every body must end with:
```
🤖 Generated with [Claude Code](https://claude.com/claude-code)
```
Bodies must not use a bare `@name`.

### PR 1: `feat: nested categories data model, new bottom nav, Feed, Library/Manage, wireframe theme`
Commits `7b88096` → `a262b9c` (7):
- nested categories, one category per link, new link states (GRE-42), plus an early category picker (GRE-44)
- bottom nav Feed / Search / Library / Settings, with an empty Feed (GRE-49) and its follow-up
- Library tab and Manage screen, first pass; recursive category tree; Move via the category picker
- Feed first-launch empty state (GRE-49)
- wireframe theme: light colors, Roboto Flex typography, extras via CompositionLocal

Notes: refs GRE-42, GRE-49 (mention only; don't use closing keywords like "Fixes"). `7b88096` mixes GRE-42 and GRE-44 work.

### PR 2: `feat: Add-link modal bottom sheet`
Commits `802b566` → `b0bbf45` (6): Add-a-link modal sheet per the approved design; wiring into root navigation and updated mocks; the sheet rises with the keyboard; scroll fix with the keyboard open; adds `roboto_flex.ttf`; `b0bbf45` ("test") simplifies AddLinkSheet.kt (flag this commit message to reviewers).

### PR 3: `feat(GRE-50): Crown Bookmark launcher icon; docs; flow cancellation fix`
Commits `b78b037` → `1b83277` (3): adaptive launcher icon (GRE-50); device-automation safety rules in CLAUDE.md; rethrow `CancellationException` in flow builders.

### PR 4: `feat(GRE-44): category picker sheet and Manage categories`
Commits `032515d` → `2d52025` (12): category picker data layer; the modal bottom sheet; spec and mocks; delete-flow state, tree-collapse fix, DAO support; inline rename / add-sub / new top-level; text field padding and clear button; delete-flow UI (impact card, swipe bar, undo bar); row selection disabled while editing; picker split and reusable pieces moved to `:core:design`; Manage categories via the embedded picker (AddCategoryScreen removed); keyboard no longer pushes Manage content.

### PR 5: `feat(GRE-44): tag picker, Manage tags tab, Library restyle`
Commits `80fcdd5` → `489d5eb` (6):
- remove the standalone CategoriesScreen; Organize category routes open Manage
- tag picker sheet: search, create, rename, delete with undo; `TagRepository.getTagsWithCounts()`
- Add link: a Tags field opens the tag picker (`AddLinkScreen` loses its `onAddTag` param)
- Manage Tags tab embeds the tag picker (ManageViewModel removed); segmented tab switcher; 12dp bottom gap
- fix: root `consumeWindowInsets` so `imePadding` doesn't double-count the bottom nav bar
- Library tab restyled to the categories & tags mock (Manage pill kept, no ⋮ menus)

Known gaps to list in the body: no "Recent" row for tags (not tracked in the DB); link-details doesn't use the tag picker yet; old `TagsScreenEnhanced` and `AddTagScreen` are still routed; no tests ran (`jvmTest` blocked by the pre-existing JVM compile errors).

## Step 3: report back
Give the owner the 5 PR URLs (full `https://github.com/forhad013/LinkLibrary/pull/N` links), the build result from Step 0, and anything skipped.
