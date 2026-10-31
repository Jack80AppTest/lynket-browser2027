# Lynket Custom 2027 — P11 Android 11 target SDK API 30

## Validated starting point
- P10 **explicitly confirmed fully functional on physical Galaxy S23 Ultra** by the user on 2026-10-31.
- Immutable P10 rollback baseline: branch `dev/3.0.0-targetsdk-p10` HEAD `7af8b398544eb53387e29097b779a6b7e311fc50`; tested app-code commit `11519037023be5bcbcb0b280b15d72a993c21dd2`.
- Successful P10 GitHub Actions: https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41193822471
- P9, P8, P7, earlier stable branches, release/tag 2.1.7 and `main` remain unchanged.

## P11 changes — deliberate and isolated
- Branch `dev/3.0.0-targetsdk-p11`, based **exactly on validated P10**, no merge.
- `android-app/app/build.gradle`: `targetSdkVersion 29` → `30`, leaving `compileSdk 36`, `minSdk 16`, Android versionName 2.1.7/versionCode 62, debug package `com.jk8026.lynket2027.debug`, Kotlin 2.2.21, Gradle 8.11.1, AGP 8.10.1 unchanged.
- Android 11 filters PackageManager queries; static audit identified Lynket's queries for installed browser apps (`ACTION_VIEW` http/https), all launchable apps (`ACTION_MAIN` with `CATEGORY_LAUNCHER`), Custom Tabs services (`android.support.customtabs.action.CustomTabsService`), voice search (`android.speech.action.RECOGNIZE_SPEECH`) and the seven existing built-in provider package names.
- Added a **narrow, purpose-specific** `<queries>` block in the application manifest for these integrations, plus `android.speech.RecognitionService`, which Android documents for speech recognition. Explicitly **no** `QUERY_ALL_PACKAGES`.
- The source contains a private SQLite `BubbleSessionStore` for persisted WebHeads; application manifest has no external-storage read/write permissions, so no `requestLegacyExternalStorage` opt-out and no storage-access permission was introduced.
- No change to app logic, bubble session storage, UI, layouts, overlay permissions or receivers/services.
- Reference: https://developer.android.com/about/versions/11/behavior-changes-11
- Reference: https://developer.android.com/training/package-visibility/declaring
- Reference: https://developer.android.com/about/versions/11/privacy/storage

## CI requirements — same established workflow
1. Independent P11 workflow: `.github/workflows/lynket-v3-targetsdk-p11.yml` on `dev/3.0.0-targetsdk-p11`; legacy P10 CI unchanged.
2. Build `:app:assembleDebug`; run all three existing Robolectric regression test classes (WebHeads session DB, saved bubble controls, settings theme). Also validate exact package visibility declarations and absence of broad querying.
3. `aapt` must verify debug package/version and **targetSdkVersion 30**, check packaged `<queries>`; `apksigner` must verify signature with **APK Signature Scheme v2**.
4. Publish **standalone APK directly, not ZIP**, as in P10.
5. Device acceptance: user tests Android 14 Samsung S23 Ultra: browser/provider list (Chrome, Samsung Internet, etc.), app blacklist/incognito list, voice search, link sharing, Custom Tabs, WebHeads overlay/background functionality, hide/restore, saved tabs after relaunch, default/secondary browser and themes. Check updates install safely without data wipe; do not lose sessions.
6. **Do not call P11 validated or proceed to P12 until user explicitly confirms testing on physical phone. Do not merge to `main`/stable or publish release without explicit instruction.**

**Status: P11 candidate staged; CI and device validation pending.**

## P11 CI result — full success; physical-device validation pending

- **Verified green GitHub Actions:** https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41195000275
- Exact tested application code commit: `e5479dc0b50f1e6e69444a029b8fcbce72823bb5`.
- `:app:assembleDebug`: **success** on Kotlin 2.2.21, Gradle 8.11.1, AGP 8.10.1, compileSdk 36, **targetSdk 30**.
- Dedicated XML check for manifest `<queries>`, browser VIEW http/https, launcher apps, Custom Tabs service, voice recognition, built-in browser providers, no `QUERY_ALL_PACKAGES`: **success**.
- Existing automated tests (`BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest`): **success** unchanged.
- APK package ID `com.jk8026.lynket2027.debug`, Android version 2.1.7/code62, targetSdk30, packed `<queries>` and signature using v2 APK scheme: **success**.
- **Direct APK artifact, not ZIP**: [Lynket_2027_v2.1.7_TargetSdk_P11_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41195000275/artifacts/13040149654), **8,689,972 bytes**.
- No functional code, database, overlay, menus, layouts or UI changes apart from intended Android package visibility manifest declaration and targetSdk.
- **P11 STATUS: CI green, NOT yet validated on physical Samsung Galaxy S23 Ultra.**
- Important phone-test additions compared to P10: custom-tab browser provider **discovery and selection**, installed-app lists in per-app settings, voice search/recognition, secondary-browser actions; in addition to sharing URL, WebHeads overlay, hide/restore, tab persistence on app process relaunch, settings and themes.
- **P10 remains latest explicitly user-validated rollback** at `7af8b398544eb53387e29097b779a6b7e311fc50`. No P12, no merge into `main`/stable, no release without further explicit user approval.
