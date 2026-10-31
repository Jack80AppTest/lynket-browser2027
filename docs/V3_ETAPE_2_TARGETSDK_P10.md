# Lynket Custom 2027 — P10 targetSdk API 29 (Android 10)

## User validated baseline
- User confirmed **P9 works on their Galaxy S23 Ultra** on 2026-10-31.
- P9 branch `dev/3.0.0-toolchain-p9` remains the immutable rollback reference at `0a9a96d76fffdbd93290f185f588bd381540885c`; successful P9 code commit `6d301a89e1d16a9df8b09ea72f3c16fac599523e`.
- P9 GitHub Actions full success: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41192891587 .
- P8, P7, P6, earlier experimental branches, stable branches, tag/release 2.1.7 and `main` untouched.

## P10 isolated scope
- P10 branch: `dev/3.0.0-targetsdk-p10`, created exactly from validated P9 head.
- **Only Android app configuration change:** `targetSdkVersion 28` → `targetSdkVersion 29` in `android-app/app/build.gradle`.
- Keep `minSdkVersion 16`, `compileSdkVersion 36`, `versionName 2.1.7`, `versionCode 62`, debug package `com.jk8026.lynket2027.debug`.
- Keep Kotlin 2.2.21, AGP 8.10.1, Gradle 8.11.1, Gradle JVM 17, Java 11 test launcher and Robolectric 4.3.
- Preserve Java/Kotlin application code, UI and layouts, all permissions, receiver/service declarations, bubble database and save/hide/restore behavior; do not introduce unrelated changes.

## Target-SDK-specific risks and reviews
- This is an **Android runtime behavior upgrade** rather than another toolchain-only bump. Android 10 target API 29 changes privacy, background activity launch behavior, and storage policy.
- The P9 application manifest does **not** request `READ_EXTERNAL_STORAGE` or `WRITE_EXTERNAL_STORAGE`, and active bubble sessions are persisted in a private SQLite database (`BubbleSessionStore`); no additional legacy-storage opt-out has been added reflexively.
- Do not raise directly to API 30+ yet: scoped storage and later Android foreground-service restrictions require separate, deliberate compatibility reviews.
- Android documentation:
  - https://developer.android.com/about/versions/10/behavior-changes-10
  - https://developer.android.com/about/versions/11/privacy/storage
  - https://developer.android.com/develop/background-work/services/fgs/changes

## Established validation workflow — unchanged
1. Push changes only to `dev/3.0.0-targetsdk-p10` and use standalone workflow `.github/workflows/lynket-v3-targetsdk-p10.yml`.
2. Run `:app:assembleDebug`, existing 3 Robolectric test classes (`BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest`).
3. Verify debug Android package, app version, **targetSdk 29** (via `aapt dump badging`) and APK signature (`apksigner`).
4. Publish an **APK directly, not a ZIP**, for physical testing.
5. **Do not declare P10 validated** until the user confirms tests on Galaxy S23 Ultra: link sharing, Custom Tabs/WebHeads overlay permission, bubbles count/order, hide/restore, SQLite persistence after app/process relaunch, menu, custom search, article, history, both themes, foreground/background behavior and notifications where relevant.
6. **Never merge or release into `main`, `stable/*`, or any production release without explicit user approval**. P9 remains the validated rollback baseline.

**Status: P10 in development; CI and physical phone verification pending.**

## P10 CI result — complete success / physical-device test pending

- **GitHub Actions success:** https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41193822471
- Exact tested app code commit: `11519037023be5bcbcb0b280b15d72a993c21dd2`.
- App `:app:assembleDebug`: **success** with Android `targetSdkVersion 29`.
- Existing Robolectric unit tests — persistent WebHeads session store, saved bubbles card, settings/theme: **success**, no disabled tests.
- aapt badging confirms `com.jk8026.lynket2027.debug`, version `2.1.7` / code 62, **`targetSdkVersion:29`**; `apksigner verify`: **success**.
- Direct standalone APK: [Lynket_2027_v2.1.7_TargetSdk_P10_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41193822471/artifacts/13039963359), **8,689,535 bytes**; no ZIP uploaded by workflow.
- Source changes: `targetSdkVersion 28` → `29` only, plus dedicated workflow and documentation; no app source, WebHeads/session DB, layout, permissions, other Android tool versions or runtime business logic changed.
- **Status: P10 CI-green, NOT user-validated on phone**. User must confirm that sharing links, WebHeads overlay, saving and restoring bubbles, app restart, Custom Tabs, search, navigation, tabs/history, themes, and background behavior all work on S23 Ultra.
- **P9 remains latest validated rollback base**, `dev/3.0.0-toolchain-p9` at `0a9a96d76fffdbd93290f185f588bd381540885c`. Do not start P11 or merge/release without new explicit user approval.
