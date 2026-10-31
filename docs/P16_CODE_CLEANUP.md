# Lynket Custom 2027 — P16 Code Cleanup

## Purpose and authorization

User authorized **GO** for a minimal readability-only cleanup on the already validated Android 16 / P16 source. No functional code, library, SDK, permission, version or UI changes are authorized. Preserve all existing behavior, especially WebHeads persistence (**HIDE is not CLOSE**), restoration, Custom Tabs minimize, Chrome/Samsung share Slide Over, Article mode and themes.

## Baseline and isolation

- Known-good branch: `stable/v3.0.1`, initial commit `cc9ac043050bc17ef7863c4f774dc7a72e8a2956`.
- Working branch: `dev/3.0.1-p16-code-cleanup`.
- Identity remains `versionName 3.0.1`, `versionCode 64`, `compileSdkVersion 36`, `targetSdkVersion 36`, `minSdkVersion 16`.
- Original `README.md`, GPL licensing, historical files and author credits retained. No existing P16/stable branch overwritten.

## Changes made — documentation and comments only

1. `README.md`: add a visible fork/developer introduction and references to the architecture guide; preserve the upstream README text underneath.
2. `docs/P16_CODE_ARCHITECTURE.md`: a **passive, human-facing** source map, navigation path explanation, persistence safety rules, and testing pointers. Never parsed by a build, release script or CI.
3. `android-app/app/src/main/java/arun/com/chromer/shared/base/activity/EdgeToEdgeInsets.java`: replace a P16-era inline comment with an explanation of why duplicate system-bar padding is unsafe; **no executable code modified**.
4. `android-app/app/src/main/java/arun/com/chromer/bubbles/webheads/WebHeadService.java`: add two lines explaining the restoration/database contract; **no executable code modified**.
5. Dedicated GitHub Actions workflow, derived from the already successful 3.0.1 workflow, with identical build/tests/compatibility checks, selected for this **working branch only**, and a distinct APK filename. The new workflow is a development/build convenience; it does **not** change APK runtime configuration.

## Double-check requirements

- Git comparison against initial `stable/v3.0.1` must show only the files enumerated above plus this report and the new workflow.
- Both Java source files must be **byte-identical to the baseline after undoing exactly their comment-only edits**. No code tokens/logic, imports, methods, resource XML, dependencies, signatures, version codes, permissions or runtime configurations may change.
- `:app:assembleDebug`, the three existing Robolectric tests and all existing Android 11–16 / APK v2 verification controls must remain successful.
- The APK must remain `com.jk8026.lynket2027.debug`, `3.0.1` / `64`, `targetSdk 36`.
- The cleaned build is **not physically validated** merely because CI passes; user controls approval to mark it stable.

## CI / delivery result

**GitHub Actions: COMPLETED / SUCCESS**, [run 41236975471](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41236975471), job `124642680276`, **21 green steps**. This includes `:app:assembleDebug`, the three existing Robolectric tests, Android 11–16 static compatibility/regression controls, package identification (`3.0.1`, code `64`, Debug package, targetSdk `36`), APK v2 signature verification and direct non-ZIP artifact upload.

- **Exact compiled commit:** `3b3ec2733599db1de67a6586ad4e4566cc2df94c`. The following report-only commit is not included in the APK; no application code changed after this build.
- **Direct APK, not ZIP:** [Lynket_Custom_2027_v3.0.1_P16_CodeCleanup_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41236975471/artifacts/13055738685) — artifact ID `13055738685`, size **8,693,567 bytes**, `archive:false`.
- GitHub artifact metadata SHA-256 (not locally rehashed): `f4f8405a29c20c3a82b5de8a5040d7adbba4c10061afe88abec7042f6f751790`.
- Double-check on GitHub: **exactly six changed paths** against `stable/v3.0.1`: `README.md`, `EdgeToEdgeInsets.java`, `WebHeadService.java`, `docs/P16_CODE_ARCHITECTURE.md`, this report, and a branch-specific workflow. Both modified Java files are byte-identical to the baseline after reversing only the intentionally changed comments. No executable statements altered, no build/app/runtime configuration changed.
- **Physical validation of the cleanup APK:** not yet performed; the previously validated 3.0.1 functionality remains the reference until the user verifies the cleanup candidate. No PR/merge/tag/release created for the cleanup.

## Additional explicitly approved contributor credit — 31 October 2026

After the original comment-only cleanup, the user explicitly requested an **in-app attribution** and approved a visual example matching the existing Credits list. This is an intentional **UI text-only exception** to the original documentation-only scope; no navigation, business logic, WebHeads persistence or SDK settings were changed.

- `android-app/app/src/main/java/arun/com/chromer/about/AboutFragment.kt`: prepend a static, non-linked `John Krauza` entry to the existing Credits adapter. Keep Arunkumar in the independent **Author** section and preserve the Patryk, Max and beta tester rows. No personal portrait or email address is added.
- `android-app/app/src/main/res/values/strings.xml` and `values-fr/strings.xml`: add English and French text describing **modernization and compatibility with recent Android versions**.
- `README.md` and `CONTRIBUTORS.md`: correct the attribution to *Android modernization and compatibility contributor*. `Lynket Custom 2027` remains a development fork name; the **installed app name, package identifiers and icon remain unchanged**.
- No update to `app/build.gradle`, manifest, original credit/author code, external library versions, or Android SDK. Version remains **3.0.1** / code **64**, Android API **36**.
- Compared `AboutFragment.kt` against `stable/v3.0.1`: exactly one new string-valued item and one new display case, no existing code changed. Each strings file adds only its new localized entry. This UI text addition requires a **new APK compilation and on-device visual check**, unlike the prior purely comment-only cleanup.

**Attribution build:** [GitHub Actions run 41240713796](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41240713796), compiled revision `89822cdee647cdbe3cbedcb23dccae3ddcdb1e74`. Build result and direct APK artifact link: **pending actual completion** (do not substitute earlier cleanup APK `13055738685`).

**Next step:** after this candidate is validated by the user, discuss preparing an upstream pull request for the original Lynket developer, who would independently decide if and how to integrate modernized code and publish to Play Store. Do not create a PR, release or tag without explicit authorization.

## Publication policy

No merge to `main`, no PR, no new tag/release, no edits to `stable/v3.0.1` without a further explicit instruction. The version 3.0.1 release already created by the user remains unchanged.
