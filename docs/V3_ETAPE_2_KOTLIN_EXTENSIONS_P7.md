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

## Complete synthetic source migration implementation (candidate, CI pending)
- Expanded from initial 5 activity files to all **32 synthetics-affected Kotlin files** in main application source, including fragments, custom search view, RecyclerView holders, Epoxy holders and LayoutContainer implementations.
- Removed `kotlin-android-extensions` application plugin and `androidExtensions { experimental = true }`; kept `viewBinding true` and all existing Android layouts and user-visible behavior.
- No Kotlin language/plugin version jump in this migration: continue Kotlin 1.7.22 until new P7 baseline passes.
- This complete migration must pass P7 GitHub Actions, Robolectric tests, verified APK packaging and subsequent user physical-device validation. P6 remains the last user-validated baseline.

## Final P7 CI result — green; phone validation pending
- Successful GitHub Actions run: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41165033378
- Tested code commit: `a61b55a7a1efaa2a510cd5bb76eb73be57bddf5c`.
- `:app:assembleDebug`: **success**, with View Binding and no `kotlin-android-extensions` plugin.
- Regression tests for bubble session persistence and saved-bubbles UI, and settings theme: **success**.
- APK identity / package / signature verification: **success**.
- Direct APK artifact: [Lynket_2027_v2.1.7_ViewBinding_P7_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41165033378/artifacts/13028691671); about 8.57 MB.
- No feature/layout changes intended, no Kotlin version bump (1.7.22 retained), no targetSdk bump.
- Corrections after full migration commit `4941fade78d102f6f157e27973f8faea39464c3c`: `94850e643f5d147b6c9767f9fd956010539686ab` (restore callback parameter), `a61b55a7a1efaa2a510cd5bb76eb73be57bddf5c` (use `checkNotNull(view)` compatible with legacy AndroidX instead of `requireView()`).
- **Status: P7 CI green, NOT user-validated on device.** User must test URL sharing, WebHeads overlay, hide/restore, saved tabs after restart, browsing/article view, custom search field, tabs lists, history and settings. Do not proceed to P8 before explicit user approval.
- **P6 remains immutable last user-validated baseline** at `74c24d9f374a44e29371989079ac820098ca7739`. Never merge P7 to `main` without explicit permission.
