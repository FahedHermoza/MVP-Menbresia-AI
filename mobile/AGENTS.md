# AGENTS.md — Menbresia AI (MVP)

> Instructions for AI agents working in this repository.

## 1. Project Identity

- **Name**: Menbresia AI
- **Type**: Native Android App — instant access digital membership for nightlife and gastronomy in Cusco, Peru.
- **Package**: `com.fahed.menbresiaai`
- **Architecture**: Clean Architecture + MVI (Model-View-Intent)
- **Backend**: 100% serverless (Firebase: Auth, Firestore, Storage)
- **MVP Scope**: 5-day reduced build focused on the Core Loop: **Discover → Arrive → Validate benefit**

## 2. Technology Stack

| Technology | Version | Notes |
|---|---|---|
| Kotlin | 2.1.10 | Main language |
| Compose BOM | 2025.03.01 | UI Framework |
| Material 3 | Via Compose BOM | Design System |
| AGP | 8.9.1 | Android Gradle Plugin |
| compileSdk | 36 | — |
| minSdk | 29 | Android 10+ |
| targetSdk | 35 | — |
| JDK | 11 | Source & target compatibility |
| Dagger Hilt | 2.53.1 | Dependency injection |
| Firebase Auth | BOM 33.12.0 | Google Sign-In |
| Firebase Firestore | BOM 33.12.0 | Venues, users, payments, validations |
| Firebase Storage | BOM 33.12.0 | Payment proof images |
| Coil | 2.4.0 | Image loading |
| Navigation Compose | 2.8.9 | Screen navigation |
| Play Services Location | 21.3.0 | GPS and geofencing |
| DataStore | 1.1.4 | Local preferences |

> **MVP Exclusions**: ExoPlayer (images instead of video), Google Play Billing (Yape/Plin manual payments), Room (no offline cache).

## 3. Reference Documentation — Read Before Coding

| File | Purpose |
|---|---|
| `ai-tools/PROJECT_ARCHITECTURE.md` | Full architecture, modules, dependencies, main flows, business rules |
| `ai-tools/CONTRIBUTING.md` | Conventions for branching, commits, PRs |
| `ai-tools/WORKFLOW_FEATURE.md` | Step-by-step workflow for implementing a feature |
| `spec/{feature}/spec.md` | Functional requirements, user stories, acceptance criteria |
| `spec/{feature}/plan.md` | Technical implementation plan per layer |
| `spec/{feature}/tasks.md` | Granular tasks with visual validation checkpoints |
| `spec/README.md` | Index of all specs with dependency graph |
| `Planification/PRD-MVP.md` | Product Requirements Document for the reduced MVP |

## 4. Repository Structure

```
MVP-MenbresiaAI/
├── ai-tools/                      # Technical documentation for AI agents
│   ├── AGENTS.md                  ← This file
│   ├── PROJECT_ARCHITECTURE.md    # Architecture, modules, dependencies
│   ├── CONTRIBUTING.md            # Branching, commits, PRs
│   └── WORKFLOW_FEATURE.md        # Feature implementation workflow
├── spec/                          # Spec-Driven Development Specifications
│   ├── README.md                  # Index with dependency graph
│   ├── auth/                      # SPEC-001: Google Sign-In
│   ├── venue-feed/                # SPEC-002: Vibe Feed (vertical list)
│   ├── venue-detail/              # SPEC-003: Venue Detail + Geofencing
│   ├── pin-validation/            # SPEC-004: Merchant PIN + Pass Active ⭐
│   └── payment-checkout/          # SPEC-005: Yape/Plin checkout
├── screens/                       # Reference mockups (PNG)
└── mobile/                        # Android Project (open in Android Studio)
    ├── app/                       # Main module
    │   └── src/main/java/com/fahed/menbresiaai/
    ├── build.gradle.kts           # Root build config
    ├── settings.gradle.kts
    └── gradle/libs.versions.toml  # Version catalog
```

### Target Module Structure (Clean Architecture)

```
mobile/app/src/main/java/com/fahed/menbresiaai/
├── app/                   → Entry point, DI config (Hilt), main navigation
├── feature/               → UI Screens (Compose), ViewModels, MVI states
│   ├── screen/
│   │   ├── auth/          → LoginScreen, LoginViewModel
│   │   ├── feed/          → VibeFeedScreen, VibeFeedViewModel
│   │   ├── detail/        → VenueDetailScreen, VenueDetailViewModel
│   │   ├── validation/    → PinValidationScreen, PassActiveScreen, PinValidationViewModel
│   │   ├── membership/    → MembershipScreen (plan selection)
│   │   └── checkout/      → CheckoutScreen, PaymentConfirmationScreen, CheckoutViewModel
│   ├── shared/            → Reusable screen components
│   └── navigation/        → AppNavHost, route definitions
├── domain/                → Use cases, domain models, repository interfaces
│   ├── model/             → User, Venue, ValidationResult, PaymentRecord
│   ├── repository/        → AuthRepository, VenueRepository, ValidationRepository, etc.
│   └── usecase/           → SignInWithGoogleUseCase, ValidatePinUseCase, etc.
├── data/                  → Repositories, DTOs, mappers, data sources
│   ├── repository/        → AuthRepositoryImpl, VenueRepositoryImpl, etc.
│   ├── model/             → DTOs (VenueDto, etc.) + Mappers
│   └── source/
│       └── remote/        → Firebase data sources (Auth, Firestore, Storage)
├── designsystem/          → Theming, color palette (#111111, #D4AF37), base components
│   └── component/         → ActivateButton, BottomNavBar, CountdownTimer
└── core/
    ├── common/            → Utilities
    └── location/          → LocationDataSource, distance calculations
```

## 5. Coding Conventions — MANDATORY

### MVI Pattern per Screen

Each feature screen must strictly contain these files:
- `{Feature}Screen.kt` — UI Composable
- `{Feature}ViewModel.kt` — Logic, state management
- `{Feature}UiState.kt` — Sealed class / Data class holding the UI state
- `{Feature}Intent.kt` — Sealed class with user intents / events

### State Updates

```kotlin
// ✅ CORRECT — Use copy() for immutable state updates
_state.update { it.copy(field = value) }
```

### File Naming Conventions

| Type | Pattern | Example |
|---|---|---|
| ViewModel | `{Feature}ViewModel.kt` | `VibeFeedViewModel.kt` |
| Screen | `{Feature}Screen.kt` | `PinValidationScreen.kt` |
| UiState | `{Feature}UiState.kt` | `CheckoutUiState.kt` |
| UseCase | `{Action}{Entity}UseCase.kt` | `ValidatePinUseCase.kt` |
| Repository (interface) | `{Entity}Repository.kt` | `VenueRepository.kt` |
| Repository (impl) | `{Entity}RepositoryImpl.kt` | `VenueRepositoryImpl.kt` |
| DTO | `{Entity}Dto.kt` | `VenueDto.kt` |
| Mapper | `{Entity}Mapper.kt` | `VenueMapper.kt` |
| DataSource | `{Entity}RemoteDataSource.kt` | `AuthRemoteDataSource.kt` |
| Component | `{ComponentName}.kt` | `ActivateButton.kt` |

### Code Rules

- **Immutability**: Data classes use `val`, never `var`
- **Nullability**: Use nullable types (`?`) only for optional fields
- **UI Strings**: Never hardcode strings — always use `stringResource(R.string.key)` in Compose
- **Content descriptions**: Always use `stringResource(R.string.key)`, never string literals
- **Languages**: Support ES (Spanish) and EN (English) in `strings.xml`
- **Principles**: Clean Code, DRY, YAGNI, KISS, SOLID

### Comments Philosophy

> **Good naming is the best documentation.** Write code that reads like prose, then only add comments where the code alone cannot explain the *why*.

| Rule | Example |
|---|---|
| ✅ Write self-documenting names | `resolveStartDestination()` needs no comment |
| ✅ Comment the *why*, not the *what* | `// Web client_type: 3 — do NOT use Android client_type: 1` |
| ✅ One-liner for non-obvious lifecycle/API contracts | `// Set in onResume(), cleared in onPause() to avoid leaks` |
| ⛔ No KDoc on obvious public APIs | A `data class User(val uid: String, ...)` needs no KDoc |
| ⛔ No inline comments restating the code | `val isLoggedIn = user != null // checks if user is logged in` |
| ⛔ No block comments describing entire classes | The class name + method names should do that job |

When in doubt: **delete the comment and improve the name instead.**

### ⛔ Prohibitions

| Prohibited | Use instead |
|---|---|
| `Spacer` in Compose | `Modifier.padding()` or `Arrangement.spacedBy()` |
| Hardcoded strings in UI | `stringResource(R.string.key)` |
| Unused imports | Remove before commit |
| Dead code | Remove before commit |
| `var` in data classes | Use `val` (immutability) |
| Over-engineering layers | Keep it simple, it's a 5-day MVP |

## 6. Spec-Driven Workflow

When implementing a new feature, follow this order:

1. **Read** `spec/{feature}/spec.md` → Understand requirements, user stories, acceptance criteria
2. **Consult** `spec/{feature}/plan.md` → Review the technical plan and files to create
3. **Follow** `spec/{feature}/tasks.md` → Implement granular tasks in order
4. **Implement** in layers: Data → Domain → Feature (bottom-up)
5. **Verify** each task has a visual result before moving to the next

### Project Features (MVP)

| Spec | Feature | Description | Day |
|---|---|---|---|
| SPEC-001 | **Auth** | Google Sign-In with Firebase Auth, session persistence | 1 |
| SPEC-002 | **Vibe Feed** | Vertical list of venues with images, GPS distance, benefits | 1-2 |
| SPEC-003 | **Venue Detail** | Venue info, benefits by tier, geofence-activated button | 2-3 |
| SPEC-004 | **PIN Validation** ⭐ | Merchant PIN pad, anti-fraud (3 attempts), Pass Active with timer | 3 |
| SPEC-005 | **Payment Checkout** | Plan selection, Yape/Plin QR, proof-of-payment upload | 4 |

### Dependency Graph

```
SPEC-001 (Auth) ──→ SPEC-002 (Feed) ──→ SPEC-003 (Detail) ──→ SPEC-004 (PIN) ⭐
       └──────────→ SPEC-005 (Payment) ─ ─ ─ ─activates pass─ ─ ─→ SPEC-003
```

> **SPEC-005 is independent** of the Feed→Detail→PIN chain and can be developed in parallel after SPEC-001.

## 7. Commits and Branching

### Commit Format

```
<type>: #<ISSUE-GITHUB> <CODE-TASK> <Brief description>
```

**Example**: `feat: #1 MA-10 Add vibe feed vertical scroll`

### Valid Types

`feat` | `fix` | `docs` | `style` | `refactor` | `perf` | `test` | `build` | `ci` | `chore` | `revert`

### Branch Format

```
<type>/#<ISSUE-GITHUB>-<CODE-TASK>-<brief-slug>
```

**Example**: `feat/#1-MA-10-vibe-feed-screen`

### PR Rules

- A single commit per PR (use `--amend` for updates)
- Target branch: `develop`
- Merge strategy: Rebase and merge
- Requires: GitHub Issue reference + CI passing

## 8. Testing

### Testing Stack

| Tool | Purpose |
|---|---|
| JUnit 4 | Test framework |
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
```

## 9. Key Business Rules (MVP)

### User State (Simplified for MVP)
Instead of a complex tier system, the MVP uses a binary state:
- `hasPase = false` → User has not paid, can browse the feed but cannot validate PINs.
- `hasPase = true` → User has paid (Discovery Pass via Yape/Plin), can validate PINs at venues.

### Merchant PIN Validation
- "Activate Pass" button is enabled **only** when GPS is < 50m from the venue
- PIN is **static** per venue (stored in Firestore), not rotating for MVP
- Rate limiting: blocked after **3 failed attempts** for 30 seconds
- Success screen: "PASS ACTIVE!" with 5-minute countdown timer
- Validation is logged in Firestore `validations_log`

### Geofencing
- Activation radius: **50 meters**
- Visual transition: gray button (outside) → gold gradient button (inside)
- Distance calculation: Haversine formula or `Location.distanceTo()`

### Payment Flow (Yape/Plin)
- No Google Play Billing in MVP — manual payment with proof upload
- Order code generated: `ORD-XXXX` (random 4 digits)
- User uploads screenshot of Yape/Plin transfer → Firebase Storage
- Record created in `pagos_pendientes` collection
- For demo: pass activated instantly after upload

## 10. Firestore Collections (MVP)

```
firestore/
├── users/{userId}/
│   ├── displayName              # From Google Sign-In
│   ├── email                    # From Google Sign-In
│   ├── photoUrl                 # From Google Sign-In
│   └── hasPase                  # Boolean: has active pass
├── venues/{venueId}/
│   ├── name                     # "CHANGU CLUB"
│   ├── address                  # "Avenida El Sol 123, Cusco"
│   ├── latitude / longitude     # GeoPoint coordinates
│   ├── imageUrl                 # Venue photo URL
│   ├── hours / days             # "10PM — 4AM" / "Thur–Sun"
│   ├── rating                   # 4.8
│   ├── isOpen                   # Boolean
│   ├── pin                      # Static PIN for MVP (e.g. "1234")
│   └── benefits                 # Map<tier, benefit_text>
├── pagos_pendientes/{orderId}/
│   ├── orderId                  # "ORD-1045"
│   ├── userId / userEmail
│   ├── plan                     # "discovery_pass"
│   ├── amount / currency        # 9.90 / "PEN"
│   ├── imageUrl                 # Storage URL of proof screenshot
│   ├── status                   # "pending" | "approved" | "rejected"
│   └── createdAt                # Timestamp
└── validations_log/{logId}/
    ├── userId / venueId / venueName
    ├── benefit                  # "2x1 Gin Tonic"
    ├── timestamp
    └── status                   # "success"
```

## 11. Design System Reference

### Color Palette
| Token | Value | Usage |
|---|---|---|
| Background | `#111111` | App background, screens |
| Surface | `#1C1C1C` | Cards, benefit rows |
| Surface Variant | `#282828` | Disabled buttons |
| Border | `#2A2A2A` | Dividers |
| Border Subtle | `#3A3A3A` | Disabled button border |
| Primary (Gold) | `#D4AF37` | Accent, active states, tier badges |
| Gold Light | `#F5D060` | Gradient start |
| Gold Dark | `#A07820` | Gradient end |
| Error | `#F44336` | LIVE badge |
| Success | `#4CAF50` | Pass Active check, timer |
| Text Primary | `#FFFFFF` | Main text |
| Text Secondary | `#A0A0A0` | Subtitles, labels |
| Text Disabled | `#666666` | Inactive text, hints |
| Indicator Empty | `#444444` | PIN dots unfilled |

### Typography
- **Font Family**: Space Grotesk (all UI), Inter (status bar only)
- **Title**: Bold 28-32px
- **Subtitle**: Semi-Bold 13-16px
- **Body**: Medium 12-13px
- **Caption**: Regular 9-10px, letter-spacing 0.5-1.5

### Active Button Gradient
```
LinearGradient(90°): #F5D060 → #D4AF37 → #A07820
Shadow: #D4AF3766, blur 20dp
```

## 12. Herramientas y Servicios Externos Configurados

### Firebase MCP (Model Context Protocol)

El servidor **Firebase MCP** (`firebase-mcp-server`) está instalado y configurado en **Gemini CLI** y **Antigravity**. Esto permite interactuar directamente con los servicios de Firebase sin salir del entorno de desarrollo.

| Propiedad | Valor |
|---|---|
| **Proyecto vinculado** | MenbresiaAI MVP |
| **Project ID** | `menbresia-ai-mvp` |
| **Usuario autenticado** | `hengar079@gmail.com` |
| **Servicios disponibles** | Auth, Firestore, Storage, Hosting |

**Operaciones disponibles vía MCP:**
- Consultar y modificar reglas de seguridad (Firestore, Storage, RTDB)
- Listar y crear apps (Android, iOS, Web)
- Obtener la configuración del SDK (`google-services.json`)
- Inicializar servicios de Firebase (`firebase_init`)
- Gestionar el entorno y proyecto activo

> **Uso**: Si necesitas interactuar con Firebase (por ejemplo, consultar reglas de seguridad o desplegar configuraciones), utiliza las herramientas del MCP de Firebase directamente en lugar de recurrir a la CLI de Firebase manualmente.

### GitHub CLI (`gh`)

La **GitHub CLI** está configurada y autenticada en el entorno local. Puedes utilizarla para automatizar operaciones del repositorio.

**Operaciones disponibles:**
- Crear y gestionar Issues (`gh issue create`, `gh issue list`)
- Crear y gestionar Pull Requests (`gh pr create`, `gh pr merge`)
- Consultar el estado de CI/CD (`gh run list`, `gh run view`)
- Gestionar releases y tags (`gh release create`)
- Interactuar con el repositorio remoto (`gh repo view`, `gh repo clone`)

> **Uso**: Prioriza los comandos de `gh` sobre operaciones manuales en la interfaz web de GitHub para mantener la trazabilidad y velocidad del flujo de desarrollo.
