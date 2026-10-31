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
