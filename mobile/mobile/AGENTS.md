# AGENTS.md — PeruPass (MenbresiaAI)

> Instructions for AI agents working in this repository.

## 1. Project Identity

- **Name**: PeruPass (aka MenbresiaAI / Vibe Pass)
- **Type**: Native Android App — instant access digital membership for nightlife and gastronomy in Cusco, Peru.
- **Package**: `com.fahed.perupass`
- **Architecture**: Clean Architecture + MVI (Model-View-Intent)
- **Backend**: 100% serverless (Firebase: Auth, Firestore, Storage, Analytics, Crashlytics, Remote Config)

## 2. Technology Stack

| Technology | Version | Notes |
|---|---|---|
| Kotlin | 2.0.21 | Main language |
| Compose BOM | 2024.09.00 | UI Framework |
| Material 3 | Via Compose BOM | Design System |
| AGP | 8.9.1 | Android Gradle Plugin |
| compileSdk | 36 | — |
| minSdk | 29 | Android 10+ |
| targetSdk | 35 | — |
| JDK | 11 | Source & target compatibility |
| DI (planned) | Dagger Hilt 2.53.1 | Dependency injection |
| Media (planned) | ExoPlayer / Media3 1.5.0 | Video playback + caching |
| Billing (planned) | Google Play Billing 7.1.1 | Subscriptions and passes |
| Maps (planned) | Google Maps SDK 19.0.0 | Geolocation and geofencing |

## 3. Reference Documentation — Read Before Coding

| File | Purpose |
|---|---|
| `ai-tools/PROJECT_ARCHITECTURE.md` | Full architecture, modules, dependencies, main flows, business rules |
| `ai-tools/CONTRIBUTING.md` | Conventions for branching, commits, PRs, CI/CD |
| `spec/{NN}-{feature}/spec.md` | Functional requirements and user stories per feature |
| `spec/{NN}-{feature}/plan.md` | Technical implementation plan |
| `spec/{NN}-{feature}/tasks.md` | Granular tasks for development |
| `README.md` | Project overview |

## 4. Repository Structure

```
PeruPass/
├── mobile/                        # Android Project (open in Android Studio)
│   ├── app/                       # Main module
│   │   └── src/main/java/com/fahed/perupass/
│   ├── build.gradle.kts           # Root build config
│   ├── settings.gradle.kts        # rootProject.name = "PeruPass"
│   └── gradle/libs.versions.toml  # Version catalog
├── spec/                          # Spec-Driven Development Specifications
│   ├── 01-vibe-feed/              # TikTok style vertical video feed
│   ├── 02-venue-detail/           # Venue details and benefits
│   ├── 03-pin-validation/         # Merchant PIN Validation
│   ├── 04-membership-tiers/       # Membership system
│   ├── 05-user-profile/           # User profile and onboarding
│   └── 06-admin-dashboard/        # Admin panel (web, out of scope for Android)
├── ai-tools/                      # Technical documentation for AI
├── screens/                       # Reference mockups (PNG)
└── README.md
```

### Target Module Structure (Clean Architecture)

```
mobile/
├── app/                   → Entry point, DI config (Hilt), main navigation
├── feature/               → UI Screens (Compose), ViewModels, MVI states
│   ├── screen/{feature}/  → FeedScreen, ValidationScreen, MembershipScreen...
│   ├── shared/            → Reusable components
│   └── navigation/        → AppNavHost
├── domain/                → Use cases, domain models, repository interfaces
├── data/                  → Repositories, DTOs, mappers, data sources
│   ├── repository/
│   ├── model/             → DTOs + Mappers
│   └── source/            → remote/ (Firebase) + local/ (Room/DataStore)
├── designsystem/          → Theming, base components, color palette
├── core/                  → Location, Media, Analytics, Security
└── build-logic/           → Gradle convention plugins
```

## 5. Coding Conventions — MANDATORY

### MVI Pattern per Screen

Each feature screen must strictly contain these files:
- `{Feature}Screen.kt` — UI Composable
- `{Feature}ViewModel.kt` — Logic, state management
- `State.kt` — Data class holding the UI state
- `Event.kt` — Sealed class with user events/intents

### State Updates

```kotlin
// ✅ CORRECT — Use copy() for immutable state updates
_state.update { it.copy(field = value) }
```

### File Naming Conventions

| Type | Pattern | Example |
|---|---|---|
| ViewModel | `{Feature}ViewModel.kt` | `FeedViewModel.kt` |
| Screen | `{Feature}Screen.kt` | `ValidationScreen.kt` |
| UseCase | `{Action}{Entity}UseCase.kt` | `ValidatePinUseCase.kt` |
| Repository | `{Entity}Repository.kt` | `VenueRepository.kt` |
| DTO | `{Entity}DTO.kt` | `VenueDTO.kt` |
| Mapper | `{Entity}Mapper.kt` | `MembershipMapper.kt` |
| Manager | `{Feature}Manager.kt` | `GeofenceManager.kt` |
| Helper | `{Feature}Helper.kt` | `ValidationHelper.kt` |
| Label | `{Feature}Label.kt` | `FeedLabel.kt` |

### Code Rules

- **Immutability**: Data classes use `val`, never `var`
- **DTOs**: Annotate with `@SerializedName` when using Gson
- **Nullability**: Use nullable types (`?`) only for optional fields
- **UI Strings**: Never hardcode strings — always use `stringResource(R.string.key)` in Compose or `strings.xml`
- **Content descriptions**: Always use `stringResource(R.string.key)`, never string literals
- **Languages**: Support ES (Spanish) and EN (English) in `strings.xml`
- **Principles**: Clean Code, DRY, YAGNI, KISS, SOLID

### ⛔ Prohibitions

| Prohibited | Use instead |
|---|---|
| `Spacer` in Compose | `Modifier.padding()` or `Arrangement.spacedBy()` |
| Hardcoded strings in UI | `stringResource(R.string.key)` |
| Unused imports | Remove before commit |
| Dead code | Remove before commit |
| `var` in data classes | Use `val` (immutability) |

## 6. Spec-Driven Workflow

When implementing a new feature, follow this order:

1. **Read** `spec/{NN}-{feature}/spec.md` → Understand requirements and user stories
2. **Consult** `spec/{NN}-{feature}/plan.md` → Review the technical plan
3. **Follow** `spec/{NN}-{feature}/tasks.md` → Implement granular tasks in order
4. **Implement** in layers: Data → Domain → Feature (bottom-up)
5. **Test** each layer before moving to the next

### Project Features

| # | Feature | Description |
|---|---|---|
| 01 | Vibe Feed | TikTok style vertical video feed with geolocation |
| 02 | Venue Detail | Venue details, benefits by tier, "Activate Pass" button |
| 03 | PIN Validation | Presential validation: numeric keypad, geofencing (<50m), rate limiting |
| 04 | Membership Tiers | Subscription system: Discovery, Vibe, Gold, Black |
| 05 | User Profile | User dashboard, tokens, digital stamps, onboarding |
| 06 | Admin Dashboard | Web management panel (out of scope for Android) |

## 7. Commits and Branching

### Commit Format

```
<type>: #<ISSUE-GITHUB> <CODE-TASK> <Brief description>
```

**Example**: `feat: #1 PP-10 Add vibe feed vertical scroll`

### Valid Types

`feat` | `fix` | `docs` | `style` | `refactor` | `perf` | `test` | `build` | `ci` | `chore` | `revert`

### Branch Format

```
<type>/#<ISSUE-GITHUB>-<CODE-TASK>-<brief-slug>
```

**Example**: `feat/#1-PP-10-vibe-feed-screen`

### PR Rules

- A single commit per PR (use `--amend` for updates)
- Target branch: `develop`
- Merge strategy: Rebase and merge
- Requires: GitHub Issue reference + CI passing + 1 approval

## 8. Testing

### Testing Stack

| Tool | Purpose |
|---|---|
| JUnit 4 | Test framework (migration to JUnit 5 pending) |
| MockK | Dependency mocking |
| Turbine | Testing Kotlin Flows and StateFlows |
| Coroutines Test | Testing asynchronous code |
| Compose Testing | Declarative UI tests (future) |
| Firebase Emulators | Local testing of Auth, Firestore |

### Mandatory Coverage per PR

| Layer | Required Tests |
|---|---|
| Mappers | Unit tests for DTO ↔ Domain transformations |
| UseCases | Unit tests for business logic + edge cases |
| Repositories | Unit tests with mocked data sources |
| ViewModels | Unit tests for state transitions (future) |

### Commands

```bash
cd mobile
./gradlew assembleDebug           # Compile
./gradlew testDebugUnitTest       # Unit tests
./gradlew lint                    # Static analysis
./gradlew connectedDebugAndroidTest  # Instrumented tests
```

## 9. Key Business Rules

### Membership Tiers

| Tier | Payment | Tokens |
|---|---|---|
| Discovery Pass | Single | 10 fixed (3 cafes, 3 bars, 2 restaurants, 2 discos) |
| Vibe Member | Monthly + Free Trial | 2/month |
| Gold / Select | Premium monthly | 4/month (high value) |
| Black / Founder | All-Access (Invite Only) | Unlimited |

### Merchant PIN Validation

- "Activate Pass" button is enabled **only** when GPS is < 50m from the venue
- 4-digit PIN, rotating (daily/weekly)
- Rate limiting: blocked after **3 failed attempts**
- Cooldown: **1 validation per venue every 12-24 hours** per user
- Offline: use last known location if inside the geofence

### Geofencing

- Activation radius: **50 meters** (configurable via Remote Config)
- Visual transition: gray (outside) → gold (inside)
- Offline fallback: last location in DataStore

## 10. Firestore Collections (MVP)

```
firestore/
├── users/{userId}/
│   ├── profile           # Name, email, avatar
│   ├── membership        # Tier, dates, remaining tokens
│   └── validations/      # Vibe Validations history
├── venues/{venueId}/
│   ├── info              # Name, category, GeoPoint, description
│   ├── media             # Video URLs in Storage
│   ├── benefits          # Map<tier, benefit>
│   └── pin               # Current PIN + last rotation
├── tiers/{tierId}/       # Tier config (price, tokens, benefits)
└── validations_log/      # Global log (B2B metrics)
```
