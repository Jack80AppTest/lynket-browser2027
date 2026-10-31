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

**Pending verification.** Record only after a confirmed run, including run, commit actually compiled, APK artifact ID and result. User should receive **two distinct links together**: this GitHub Markdown report and the **direct APK without ZIP**.

## Publication policy

No merge to `main`, no PR, no new tag/release, no edits to `stable/v3.0.1` without a further explicit instruction. The version 3.0.1 release already created by the user remains unchanged.
