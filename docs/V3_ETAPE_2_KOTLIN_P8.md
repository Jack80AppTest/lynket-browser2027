# Lynket Custom 2027 — P8 Kotlin 2.2 upgrade

## Base and user validation
- P7 was **explicitly confirmed working on Samsung Galaxy S23 Ultra by the user on 2026-10-31**.
- Preserve P7 code rollback baseline `a61b55a7a1efaa2a510cd5bb76eb73be57bddf5c` (green CI [run 41165033378](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41165033378)); documentation-only P7 branch head `253cc03495d5cfce0a222c8a0125405184ea6142`.
- P7 completed View Binding migration and removed Kotlin Android Extensions from the app. P6/P5/P4 and main remain untouched.

## P8 scope
- Branch `dev/3.0.0-kotlin-2.2-p8` created directly from user-validated P7 documentation head.
- **Only functional configuration change for initial attempt**: Kotlin Gradle plugin and stdlib version `1.7.22` -> `2.2.21` by existing `ext.kotlin_version`. No Kotlin synthetics, no targetSdk bump, no UI or storage changes.
- Keep Gradle 8.7, AGP 8.5.2, compileSdk 34, targetSdk 28, minSdk 16, Java 17 build / Java 11 Robolectric 4.3 tests, app version 2.1.7 code62, debug id `com.jk8026.lynket2027.debug`.
- Kotlin 2.2.21 is officially supported with Gradle 8.7 and AGP 8.5.2: https://kotlinlang.org/docs/gradle-configure-project.html .
- New independent workflow `.github/workflows/lynket-v3-kotlin-p8.yml` and direct APK delivery, never ZIP.

## Mandatory workflow
1. Run dedicated GitHub Actions P8 CI and diagnose genuine compilation errors from logs.
2. Make minimal isolated P8 fixes for legacy libraries or API signatures. Do not disable regression tests, change user-visible behavior, or bypass checks to get green CI.
3. Require assembleDebug, all established test classes, APK package/version/signature check, direct APK published.
4. User must physically verify tabs, WebHeads, hide/restore persistence, custom search, menus, history, theme, share links on S23 Ultra before P8 is validated.
5. No merge into main or any stable branch, no automatic release or proceeding to P9 without explicit user validation.

**Status:** P8 initial code staged; GitHub CI and phone validation pending.

## CI P8 — full success, physical-phone test pending

- Complete successful GitHub Actions run: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41191497314
- Exact code commit tested: `4509febcfeb0604a0b82f9bcfa87613ba99e2a57`.
- Kotlin Gradle Plugin / stdlib: **2.2.21**. Gradle 8.7 and AGP 8.5.2 remain unchanged. KAPT generated Dagger/Glide sources successfully.
- Build `:app:assembleDebug`: **passed**.
- Existing unit tests (bubble session store, saved bubbles card, settings theme): **passed**.
- Android package/version/signature checked with aapt and apksigner: **passed**.
- APK uploaded as standalone file (not zipped by workflow): [Lynket_2027_v2.1.7_Kotlin_2_2_P8_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41191497314/artifacts/13038524846) (8,685,889 bytes).
- First attempt [41191236619](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41191236619) failed on two Kotlin 2.2 errors from deprecated case conversion calls. Corrected in `4509febcfeb0604a0b82f9bcfa87613ba99e2a57`: `toLowerCase(Locale.getDefault())` -> `lowercase(Locale.getDefault())` and `toUpperCase()` -> `uppercase(java.util.Locale.getDefault())` to preserve device-locale behavior.
- No WebHeads/session data serialization changes, permissions, UI layouts, Android targetSdk, app version, or plugin build-tool version changes.
- **STATUS: P8 CI-green, not yet user validated on Galaxy S23 Ultra.** Wait for explicit physical-device test confirming WebHeads, hide/restore and tab persistence, custom search, browsing/article pages, sharing, tabs, menus, history and theme. P7 remains validated rollback.
- **No merge** into main, stable release or protected branch, and no P9 until user explicitly approves P8.
