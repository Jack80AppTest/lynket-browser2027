# Lynket Custom 2027 — P9 toolchain modernization (Gradle / AGP / Android API 36)

## User-validated foundation
- **P8 explicitly validated on the user's Galaxy S23 Ultra on 2026-10-31** (confirmation in conversation).
- Immutable recovery baseline `dev/3.0.0-kotlin-2.2-p8` at `a73b671ce4bae84e38a5fa178e84b9f96c906128`, containing the P8 green CI documentation. Code verified at `4509febcfeb0604a0b82f9bcfa87613ba99e2a57`.
- Green P8 Actions run: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41191497314 .
- Prior P7/P6/P5/P4 and `main` remain unchanged.

## Isolated P9 scope
- Branch `dev/3.0.0-toolchain-p9` created directly from the user-validated P8 head.
- Gradle wrapper: **8.7 → 8.11.1**.
- Android Gradle Plugin: **8.5.2 → 8.10.1**.
- `compileSdk`: **34 → 36** in both `app` and `disk-cache`.
- Build-tools for CI: 35.0.0. Android SDK API 36 installed.
- Kotlin **2.2.21 unchanged**; Gradle executed on Java 17; legacy Robolectric 4.3 tests run using Java 11.
- `targetSdk 28`, `minSdk 16`, versionName 2.1.7, versionCode 62, debug application ID `com.jk8026.lynket2027.debug` **unchanged**.
- Do not change browsing, WebHeads, bubble persistence, UI, resources, permissions, or settings in this toolchain upgrade.

## Official toolchain compatibility
- AGP 8.10 supports compileSdk/API 36 and requires Gradle >= 8.11.1 and JDK 17:
  https://developer.android.com/build/releases/agp-8-10-0-release-notes
- Kotlin 2.2.21 supports Gradle 8.11.1 and AGP 8.10.1:
  https://kotlinlang.org/docs/gradle-configure-project.html

## Validation and approval policy (unchanged)
1. Run the P9-only GitHub Actions workflow `.github/workflows/lynket-v3-toolchain-p9.yml`.
2. Fix actual Gradle/AGP/API compatibility problems only on this experimental branch; do not disable existing tests.
3. All must pass: `:app:assembleDebug`; three established Robolectric test classes; APK package, version and signing verification; **direct standalone APK** upload, no ZIP.
4. Only after CI passes, have the user test P9 on their physical Galaxy S23 Ultra (sharing links, WebHeads, hide/restore, persistence after restart, search, tabs, history, menus, themes).
5. **Do not declare P9 validated or start P10 before explicit user phone-test confirmation. Do not merge into `main`, stable, or publish release without separate explicit approval.**

**Status: P9 in progress; not yet CI green or user validated.**
