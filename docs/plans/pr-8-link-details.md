# PR 8 — `pr/8-link-details` → `pr/7-feed`

**Title:**
```
feat: Link details screen matches the Link details mock; edits save instantly
```

**Body:**
```markdown
## Summary
- Link details rebuilt to the Link details mock: hero with type chip, editable title, note card, category and tags fields, bottom action bar, ⋮ menu (copy URL, delete)
- No Save button: every edit (favorite, archived, opened, type, title, category, tags) writes straight to the DB; the live flows bring the change back
- Note autosaves 600 ms after the last keystroke and shows "Saving…"; a pending note isn't overwritten by a newer DB emission
- Share: system share sheet on Android (`rememberShareLink` expect/actual); JVM and wasmJs copy the URL instead
- Feed card: tapping the card now opens details; the ↗ button opens the link itself
- Small reuse tweaks: `CategoryField` takes a `label`; `AppBarPill` gets a rounded shape and end padding

## Commits
- feat: Link details screen matches the mock; edits save instantly

## Notes
- Stacked on `pr/7-feed` (which is stacked on `pr/6-save-screen`); push and open those first.
- Opening the link from details also marks it opened.
- A link deleted elsewhere while details is open closes the screen.

## How to test
1. `JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" ./gradlew :app:androidApp:assembleDebug`
2. Feed: tap a card → details opens; tap ↗ → the link opens in the browser.
3. In details, change the title, type, category, tags and favorite; go back and reopen: every change is kept.
4. Type a note, wait ~1 s, go back and reopen: the note is kept.
5. Share opens the Android share sheet; ⋮ → Delete removes the link and closes the screen.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
```
