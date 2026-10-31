# Lynket Custom 2027 — P7 migration Kotlin Android Extensions → View Binding

## Baseline
- User explicitly validated P6 on Galaxy S23 Ultra (2026-10-30 local time).
- Immutable P6 rollback baseline: commit `74c24d9f374a44e29371989079ac820098ca7739`, workflow [41163043807](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41163043807).
- P7 isolated branch: `dev/3.0.0-kotlin-extensions-p7`. P6, P5, P4, main, stable tag/release unchanged; no merge authorized.

## Inventory and goal
- P6 application main Kotlin scan: 139 Kotlin files; 32 still import `kotlinx.android.synthetic` and/or `kotlinx.android.extensions.LayoutContainer`.
- P7 goal: migrate all synthetic view access and LayoutContainer usages to View Binding / type-safe code, then remove the obsolete `kotlin-android-extensions` plugin.
- Keep application Kotlin 1.7.22 during migration, Gradle 8.7, AGP 8.5.2, compileSdk34, targetSdk28, versionName2.1.7/code62 and debug ID com.jk8026.lynket2027.debug.
- Do not increase Kotlin version until no synthetics remain; maintain Java17 build + Java11 test launcher with the unchanged Robolectric regression suite.

## First cohesive P7 code batch
- Enabled AGP `viewBinding true` while keeping legacy Kotlin Android Extensions temporarily for unconverted screens.
- Migrate activities: HomeActivity, TabsActivity, HistoryActivity, ProviderSelectionActivity, ImageViewActivity (5/32 affected files), with binding on already-inflated views or standard View Binding inflation, preserving existing layouts and click handlers.
- Dedicated P7 GitHub Actions workflow: `.github/workflows/lynket-v3-kotlin-extensions-p7.yml`.
- This batch is **not full P7 validation**: remaining synthetic/ LayoutContainer files must still be migrated and regression-tested. Do not call P7 user-validated until user tests the completed P7 APK on the device.

## Test and release contract
1. Commit cohesive migrations on P7 branch only; trigger dedicated workflow, diagnose actual errors and make targeted corrections.
2. Keep tests enabled; package identity and signature verification mandatory.
3. APK standalone artifact without ZIP, according to proven P6 workflow.
4. When all 32 files are migrated, remove old plugin, compile/test, produce P7 APK; ask for user's physical-device validation before treating P7 as stable.
5. Do not touch or merge P6 or main without explicit permission.
