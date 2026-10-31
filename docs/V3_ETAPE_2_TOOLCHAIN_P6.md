# Lynket Custom 2027 — Toolchain P6 (candidate)

## Baseline confirmed by user
- User validated P5 reduced on Galaxy S23 Ultra on 2026-10-30 (local date).
- P5 preserved as immutable rollback point: `dev/3.0.0-toolchain-p5-reduced` at `39fdd96ba5d8c171f62771a5f9be410213e1e3a9`.
- P5 GitHub Actions complete success: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41161654250

## P6 scope
- Isolated branch `dev/3.0.0-toolchain-p6`, forked from validated P5 reduced.
- Gradle 8.0.2 -> 8.7; Android Gradle Plugin 8.0.2 -> 8.5.2; compileSdk 33 -> 34 in app and disk-cache.
- Kotlin 1.7.22 unchanged. Gradle JDK 17 unchanged; Robolectric unit tests continue using Java 11 test launcher and Robolectric 4.3.
- targetSdk 28, minSdk 16, versionName 2.1.7, versionCode 62, debug applicationId `com.jk8026.lynket2027.debug` unchanged.
- Preserve UI, provider selection, WebHeads overlays and automatic bubble persistence/restoration; no new behaviors or unrelated changes.

## Validation workflow
1. Compile Android APK with GitHub Actions in P6-specific workflow.
2. Run all existing regression test classes with P5 test settings (including bubble/session tests).
3. Verify app package identity, version, signature. Publish standalone APK (no source ZIP by default).
4. Wait for explicit user validation on physical Galaxy S23 Ultra before calling P6 stable or starting P7.
5. Never merge to `main` / stable branches or alter existing P5 or release 2.1.7 without separately explicit user instruction.

## Important migration note
The Kotlin Android Extensions synthetic view plugin is removed in recent Kotlin releases. A future isolated migration to View Binding is necessary before any major Kotlin upgrade; do not silently disable legacy view binding here.

## CI result — compilation and regression tests passed
- CI green: [P6 GitHub Actions run 41163043807](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41163043807); tested commit `78e14819b8cc626b4f0a7d7deaa7a63049667cfa`.
- Build debug APK: success.
- Regression tests (bubble store, saved bubbles card, settings theme): success.
- APK package/version/signature verification: success.
- Published APK (standalone; not source ZIP): [Lynket_2027_v2.1.7_Toolchain_P6_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41163043807/artifacts/13027629242).
- Android debug app ID `com.jk8026.lynket2027.debug`, unchanged from P5. Android versionName still 2.1.7 with debug suffix, versionCode 62.
- Fixes needed on top of first P6 toolchain commit:
  - `457837a0cf0ff3a1c7c4779f314d25fce8e2a670`: compile Gradle buildSrc with Gradle 8.7 embedded Kotlin via `kotlin-dsl`, while keeping application Kotlin 1.7.22.
  - `88fd6feb2cf1857e75807693fe47fb35df597f6e` and `9fdb063d592198fd799ab0211fef3b68501d37b2`: adapt two `allprojects` lambdas in `buildSrc` to Gradle Kotlin DSL receiver syntax.
  - `78e14819b8cc626b4f0a7d7deaa7a63049667cfa`: accept nullable preference key for Android API 34 in `BottomBarPreferenceFragment.kt`.

**Status: P6 CI-green / phone verification still pending.** Do not declare P6 user-validated until the user explicitly confirms testing on the Galaxy S23 Ultra. Do not merge into main or stable without separate explicit permission. P5 remains the user-validated rollback baseline.
