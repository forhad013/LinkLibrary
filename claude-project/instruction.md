# Lords of Links — Project Instructions

## Description (one-liner for the project)
Lords of Links: Save & Find — a Pocket-style mobile-first content saver built with Kotlin Multiplatform & Compose.

---

## Custom Instructions

### What this project is
Lords of Links is a professional link/content management app aiming to be a better Pocket — one place for all of a user's online content: links, videos, and important info. It's also a learning vehicle for AWS and modern Android UI/animation skills.

### Tech stack
- **Language/framework:** Kotlin Multiplatform (KMP) with Jetpack Compose
- **Targets:** Android (primary, milestone 1), wasmJs web app + Chrome extension (milestone 2, after accounts and sync), Desktop (functional), iOS (later)
- **CI/CD:** GitHub Actions — signed AAB build on push to `main`, Play Store publishing gated behind a `production` environment with required reviewers
- **Cloud (planned):** AWS serverless — Lambda, HTTP API Gateway, DynamoDB, Cognito, S3, CloudFront; deployed via AWS CDK
- **DNS:** Cloudflare (domain: thelordofthelinks.com, under consideration)
- **Project management:** Linear (workspace: greenrobotdev, 2-week cycles)

### Milestones
- **Phase 0 – Define:** MVP scope, feature specs, wireframes, visual identity, data model
- **Phase 1 – Android MVP (offline):** works on one phone with no backend
- **Phase 2 – Accounts, sync + web:** accounts and sync first, then the web app (see and manage saved links) and the Chrome extension (save from the desktop browser). Both depend on sync.
- **Phase 3 – Smart layer:** auto-categorize, AI summaries, semantic search, reminders
- **Phase 4 – Expand:** iOS and desktop

### How I work
- Give me practical, step-by-step guidance with explicit commands — not abstract overviews.
- I use `nano` as my shell editor on Mac.
- I prefer manual promotion gates (environment-based locks) over fully automated production deploys.
- I defer non-critical targets to keep current work focused. The web app and Chrome extension are scheduled for milestone 2, not milestone 1.
- This is a pet project and learning opportunity — explain the "why" behind infrastructure choices so I actually learn.

### Current focus (Nov 2026)
- First Linear cycle starting ~10–11 Nov 2026
- AWS infrastructure planning (six-phase plan: naming → domain/DNS → AWS setup → backend → S3/CloudFront → KMP client integration)
- Modern Android UI — showcase animation-building skills; final UI decided after MVP scope is finalized

### Code & architecture guidelines
- Android-first, but keep KMP shared modules clean for the web app and Chrome extension in milestone 2 and for later targets
- CI/CD changes should respect the existing GitHub Actions workflow structure (`android-release.yml`)
- AWS resources should target free-tier at hobby-traffic levels; use CDK for infra-as-code
- When suggesting dependencies or libraries, prefer well-maintained KMP-compatible ones

### What NOT to do
- Don't suggest renaming the app or changing the package ID — it's locked in for Play Store / CI reasons
- Don't propose fully automated production deploys — manual gates are intentional
- Don't pull wasmJs web or Chrome extension work into milestone 1. They belong to milestone 2 (Phase 2), after accounts and sync.