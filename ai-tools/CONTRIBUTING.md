# Contributing to PeruPass (MenbresiaAI)

## Quick Summary

- **Branching Strategy**: Trunk-Based Development with `develop` (daily trunk) and `master` (releases only)
- **Branch Naming**: `<type>/#<ISSUE-GITHUB>-<CODE-TASK>-<brief-slug>` (e.g., `feat/#1-PP-10-vibe-feed-screen`)
- **Commit Format**: `<type>: #<ISSUE-GITHUB> <CODE-TASK> <Brief description>` (e.g., `feat: #1 PP-10 Add vibe feed vertical scroll`)
- **Development Flow**:
  1. Create short-lived branches from `develop`
  2. Use single commit per PR (`--amend` for updates)
  3. Target PRs to `develop`
- **Required for PRs**: GitHub Issue reference, passing CI checks, code review approval

Thank you for your interest in contributing to the PeruPass Android project. This document outlines the guidelines and processes to follow when contributing.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Branching Strategy](#branching-strategy)
3. [Development Workflow](#development-workflow)
4. [Code Style and Standards](#code-style-and-standards)
5. [Commit Guidelines](#commit-guidelines)
6. [Pull Request Process](#pull-request-process)
7. [CI/CD Pipeline](#cicd-pipeline)
8. [Issue Tracking](#issue-tracking)

## Getting Started

To get started with development:

1. Clone the repository:
   ```bash
   git clone https://github.com/fahedhermoza/PeruPass.git
   ```

2. Checkout the `develop` branch and ensure it is up to date:
   ```bash
   git checkout develop
   git pull origin develop
   ```

3. Create a new feature branch:
   ```bash
   git checkout -b feat/#1-PP-10-vibe-feed-screen
   ```

4. Open the `mobile/` directory in Android Studio and sync Gradle.

5. Build the project to verify your setup:
   ```bash
   cd mobile
   ./gradlew assembleDebug
   ```

### Prerequisites

| Tool | Minimum Version |
|------|-----------------|
| Android Studio | Ladybug (2024.2+) |
| JDK | 11 |
| Android SDK | compileSdk 36, minSdk 29, targetSdk 35 |
| Kotlin | 2.0.21 |
| Gradle | AGP 8.9.1 |

## Branching Strategy

We use a modified **Trunk-Based Development (TBD)** approach to keep the codebase healthy and deployable:

- **master**: Production branch, used only for releases. Any PR to this branch requires explicit approval.
- **develop**: Main development trunk, serves as the integration branch for day-to-day development. All feature branches merge back here frequently.
- **Feature branches**: Short-lived branches (ideally < 2 days) created from `develop` for individual tasks.

### Branch Naming Convention

All branches should follow this naming pattern:
```
<type>/#<ISSUE-GITHUB>-<CODE-TASK>-<brief-slug>
```

Where:
- `<type>` is one of the commit types listed in [Commit Guidelines](#commit-guidelines)
- `#<ISSUE-GITHUB>` is the GitHub Issue number (e.g., `#1`)
- `<CODE-TASK>` is the internal task code (e.g., `PP-10`)
- `<brief-slug>` is a short, kebab-case description

Examples (based on real PeruPass issues):
- `feat/#1-PP-10-vibe-feed-screen` — Feature: Vibe Feed (#1)
- `feat/#2-PP-20-venue-detail-benefits` — Feature: Venue Detail (#2)
- `fix/#3-PP-35-pin-validation-crash` — Fix: PIN Validation (#3)
- `feat/#4-PP-40-membership-tier-selection` — Feature: Membership Tiers (#4)
- `feat/#5-PP-50-user-profile-dashboard` — Feature: User Profile (#5)
- `docs/#6-PP-60-admin-dashboard-spec` — Docs: Admin Dashboard (#6)

## Development Workflow

1. **Always start from the latest `develop` branch**:
   ```bash
   git checkout develop
   git pull origin develop
   ```

2. **Create a feature branch**:
   ```bash
   git checkout -b feat/#1-PP-10-vibe-feed-screen
   ```

3. **Make small, focused changes**:
   - Keep branches short-lived (merge within 1–2 days)
   - One branch per task or GitHub Issue
   - Use feature flags for partially completed features that need to be merged

4. **Use a single commit per PR**:
   ```bash
   git add .
   git commit -m "feat: #1 PP-10 Add vibe feed vertical scroll"
   
   # For additional changes to the same PR:
   git add .
   git commit --amend --no-edit
   git push --force-with-lease
   ```

5. **Create a Pull Request targeting the `develop` branch**

## Code Style and Standards

### Static Analysis

Before submitting a PR, ensure your code passes:

1. **Android Lint**:
   ```bash
   ./gradlew lint
   ```
   Fix all warnings and errors. Suppressions require a comment explaining why.

2. **Detekt** *(planned — to be integrated)*:
   Once configured, run:
   ```bash
   ./gradlew detekt
   ```

3. **Kotlin Compiler Warnings**:
   Treat warnings as errors. Ensure zero warnings before pushing.

### Code Quality Checklist

- [ ] No hardcoded strings in UI — use `strings.xml` resources
- [ ] No unused imports or dead code
- [ ] Data classes use `val` (immutability)
- [ ] DTOs annotated with `@SerializedName` when using Gson
- [ ] Nullable types used appropriately for optional fields
- [ ] Code follows engineering principles: **Clean Code**, **DRY**, **YAGNI**, **KISS**, and **SOLID**

## Commit Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/) with GitHub Issue and Task Code references.

### Format

```
<type>: <ISSUE-GITHUB> <CODE-TASK> <Brief description>
```

Where:
- `<type>`: The category of the change (see table below)
- `<ISSUE-GITHUB>`: GitHub Issue reference (e.g., `#123`)
- `<CODE-TASK>`: Internal task code (e.g., `PP-45`)
- `<Brief description>`: Short, imperative description of the change

### Types

| Type | Description |
|------|-------------|
| feat | New features |
| fix | Bug fixes |
| docs | Documentation changes only |
| style | Code formatting or styling (no logic changes) |
| refactor | Code changes that are neither bug fixes nor new features |
| perf | Performance improvements |
| test | Adding or updating tests |
| build | Changes to build scripts or dependencies |
| ci | CI configuration and pipeline changes |
| chore | Tasks not related to development |
| revert | Reverting previous commits |

### Examples (based on real PeruPass issues)

```
feat: #1 PP-10 Add vibe feed vertical scroll
feat: #2 PP-20 Implement venue detail benefits by tier
fix: #3 PP-35 Fix PIN validation crash on incorrect input
feat: #4 PP-40 Add membership tier selection screen
feat: #5 PP-50 Implement user profile dashboard
docs: #6 PP-60 Add admin dashboard spec documentation
test: #3 PP-36 Add unit tests for ValidatePinUseCase
chore: #1 PP-15 Update Compose BOM to 2025.03.01
build: #4 PP-42 Add Detekt static analysis plugin
```

## Pull Request Process

1. **Create a Pull Request (PR)** targeting the `develop` branch

2. **PR Title** must follow the commit format:
   ```
   feat: #1 PP-10 Add vibe feed vertical scroll
   ```

3. **Fill in the PR description** with:
   - Clear summary of what changed and why
   - Reference to the related GitHub Issue (auto-linked with `Closes #1`)
   - Screenshots or screen recordings for UI changes *(future improvement)*
   - Test evidence (unit test results, manual QA notes)
   - Any specific review instructions or areas of concern

4. **Ensure all checks pass**:
   - Code compiles without errors or warnings
   - All existing unit tests pass
   - Lint checks pass
   - New code has corresponding tests
   - Commit message follows the required format

5. **Code Review**:
   - At least **1 approval** is required before merging
   - Address all reviewer comments
   - Use `--amend` to keep a single commit per PR
   - Use `--force-with-lease` to update the PR branch

6. **Merge strategy**:
   - Use **"Rebase and merge"** to keep a linear, clean history on `develop`
   - Delete the feature branch after merging

## CI/CD Pipeline

Each PR triggers the CI/CD pipeline (GitHub Actions) that:

- Compiles the project (`assembleDebug`)
- Runs unit tests (`testDebugUnitTest`)
- Performs Lint analysis (`lint`)
- Validates commit message format
- Builds debug APK for testing

Ensure all pipeline checks pass before requesting review.

### Local Verification Before Push

Run these commands locally to catch issues early:
```bash
cd mobile

# Compile
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Run lint
./gradlew lint
```

## Issue Tracking

We use **GitHub Issues** as the single source of truth for all work items.

### GitHub Issues

- **Purpose**: Track all tasks — features, bugs, technical debt, documentation
- **Labels**: Use labels to categorize issues (e.g., `bug`, `feature`, `refactor`, `documentation`)
- **Milestones**: Group issues by release milestones (e.g., `MVP 1.0`)
- **Assignees**: Every issue should have an assignee before work begins

### Task Codes

- **Format**: `PP-XXX` (PeruPass task codes)
- **Purpose**: Internal tracking code for cross-referencing with project management tools
- **Usage**: Include in commit messages and PR titles alongside the GitHub Issue number

### Every PR Must Have an Issue

- All changes must be associated with a GitHub Issue
- Reference the issue in branch names, commit messages, and PR descriptions
- Use `Closes #<issue-number>` in the PR description to auto-close the issue on merge

## Testing Requirements

### Minimum Coverage

All PRs must include tests for the code being changed:

| Layer | Required Tests |
|-------|---------------|
| **Mappers** | Unit tests for all DTO ↔ Domain transformations |
| **UseCases** | Unit tests for business logic and edge cases |
| **Repositories** | Unit tests with mocked data sources |
| **ViewModels** | Unit tests for state transitions and event handling *(future improvement)* |

### Testing Stack

| Tool | Purpose |
|------|---------|
| JUnit 4 | Test framework (migrating to JUnit 5) |
| MockK | Mocking dependencies |
| Turbine | Testing Kotlin Flows and StateFlows |
| Coroutines Test | Testing suspend functions and coroutines |
| Compose Testing | UI tests for composable functions *(future improvement)* |
| Firebase Emulators | Local testing of Auth, Firestore, Storage |

### Running Tests

```bash
cd mobile

# Unit tests
./gradlew testDebugUnitTest

# Instrumented tests (requires emulator/device)
./gradlew connectedDebugAndroidTest
```

## Additional Resources

- [Project Architecture](./PROJECT_ARCHITECTURE.md)
- [Project README](../README.md)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Jetpack Compose Guidelines](https://developer.android.com/develop/ui/compose)