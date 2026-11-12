# New UI — Linear Tickets (from `design/Lords of Links UI.html`)

Source: new Pocket-style design (10 screens, extracted to `design/extracted/*.html`).
Biggest structural change: **collections → nested categories (max 5 levels)**, plus new per-link state: `opened`, `favorite`, `archived`, `type` (video/article/social), auto-fetched description.

Suggested order: #1 (model) first, then feed/search/details, then polish screens.

---

## 1. Data model: nested categories + new link states
**Type:** Chore · **Priority:** Urgent
- Replace flat `Collection` with nested `Category` (parent pointer, max depth 5, enforced on insert/move)
- Add link fields: `opened: Boolean`, `favorite: Boolean`, `archived: Boolean`, `type` (video/article/social/other), `fetchedDescription: String?` (read-only)
- Migration for existing Room schema; map old collections → top-level categories
- Update repositories (`getLinksWithTags`, new queries for smart lists/counts)

## 2. Feed screen (new home)
**Type:** Feature · **Priority:** High
- Filter chips: All / Unopened / Articles / Videos / Social
- Category and Tags filter, "Show archived" toggle
- Card variants: video (with duration), article, social, no-preview (raw URL fallback)
- Empty-filter state: "No items match — Clear filters"
- Undo snackbar (Archive/Deleted, 5s)

## 3. Feed first-launch empty state
**Type:** Feature · **Priority:** High
- 3-step onboarding (Open a page → Tap Share → Choose Lords of Links)
- "Or paste a link here" input
- Bottom nav: Feed / Search / Library / Settings

## 4. Add link — 2-step flow
**Type:** Feature · **Priority:** High
- Step 1: URL field with validation ("This is not a valid link"), Paste-from-clipboard button, Cancel/Continue
- Step 2: optional category, tags, note (details on save screen pattern)
- Replaces current single-form `AddLinkScreen`

## 5. Save screen (share sheet entry)
**Type:** Feature · **Priority:** High
- Entry from Android share sheet; "Already saved. Edit it below." state
- Preview fetch with "Fetching preview…" / "No preview" / Retry
- Optional category / tags / note; Discard / Save
- Fetched description shown read-only, max 2 lines

## 6. Category picker
**Type:** Feature · **Priority:** Medium
- Single selection, breadcrumb path display ("Health › Sleep")
- "New" creates a category inside the selected parent
- Enforces max 5 levels

## 7. Link details screen
**Type:** Feature · **Priority:** Medium
- Type badge + domain + duration, "Saved today · Opened/Unopened"
- Category path, tags with inline "+ Tag" add
- Editable note + read-only auto-fetched description ("Saved automatically")
- "Open link" action

## 8. Search screen
**Type:** Feature · **Priority:** High
- Full-text search across title, URL, notes, tags
- Match highlighting per result; note matches shown as "Your note: …"
- Archived results included, shown last with "Archived" badge
- Recent searches with Clear; empty + no-results states

## 9. Library screen (smart lists)
**Type:** Feature · **Priority:** Medium
- Smart lists with counts: Unopened / Favorites / Archived / Uncategorized
- Categories tree with per-category counts (roll-up: parent includes children)
- Tags section (cloud/list)
- "Manage" entry point

## 10. Card action menu (long-press)
**Type:** Feature · **Priority:** Medium
- Long-press menu on feed cards: Favorite / Archive / Share / Details / Mark as opened / Delete
- Archive and Delete show Undo for 5 seconds

## 11. Manage categories & tags
**Type:** Feature · **Priority:** Low
- Rename / Move / Delete for categories and tags
- Delete confirm dialog ("No links are deleted.")
