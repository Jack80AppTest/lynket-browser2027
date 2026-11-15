# P16 WebHeads portable backup — development candidate

## Goal

User requested **Save backup** and **Import backup** buttons on Lynket's existing Saved Bubbles home card, so open non-incognito tabs can be transferred to another Android device or restored after uninstalling/reinstalling the Debug app.

## Baseline and isolation

- Base branch: `dev/3.0.1-p16-websites-scroll`, commit `b5ed09b61ce565e784f604612d2fb2a784bf15aa`. That landscape/scroll fix was accepted by the user; no layout changes to the Websites dialog in this phase.
- New branch: `dev/3.0.1-p16-webheads-backup`.
- Stable `stable/v3.0.1` and `main` unchanged. Internal version **3.0.1**, versionCode **64**, Android minSdk **16**, compile/target SDK **36**, signing identity unchanged.

## Functionality

- Add **Save backup / Import backup** on existing home **Saved Bubbles** card, below Hide and Restore. Save is disabled if empty; Import is always enabled even on a fresh installation.
- Save creates **`lynket-webheads-backup.json`** through Android's system file picker (Android 4.4+ / API 19+). The file includes only the ordered non-incognito URLs and existing `from_amp` flags, not cookies, history or settings.
- On Android API 16–18, document creation is unavailable: fallback to Android's text sharing chooser to allow users to save the JSON through another app.
- Import uses `ACTION_OPEN_DOCUMENT` on Android API 19+ and `ACTION_GET_CONTENT` on API 16–18. Read document URI via ContentResolver (no added storage permissions).
- Accept this application's JSON backup as well as plain-text newline-separated or comma-separated http(s) links. Validate scheme/host, reject unknown JSON formats and oversize files, and avoid repeated links.
- Import **adds only missing URLs** using `BubbleSessionStore.save` and never clears or removes previously saved URLs. Trigger existing `BubbleSessionRestorer.restore` for an idempotent rebuild of overlays when permitted.
- Original single-link sharing behavior, the three existing sharing methods, Hide/Restore, Custom Tabs, Chrome Slide Over, WebHeads SQLite database schema and Websites Scroll remain untouched.

## Checks

- Robolectric test for JSON roundtrip, AMP metadata, duplicate/text import, unsupported schemes and invalid JSON. Existing card-model test includes the two buttons and Import-when-empty behavior.
- Retain existing three regression suites, Websites Scroll layout tests, Android compatibility and signature checks.
- Device checks: export 12 links, save file outside app, uninstall/reinstall (after confirmed safe copy), import, check count/order and opening/restoration, then import again and verify no duplicates. Also test on second device with WebHeads/overlay permission enabled.
- User must be cautioned that **uninstall deletes internal SQLite data**. A successful external backup is required before uninstall. Do not claim cookies, preferences, or browser sessions are restored; only the list of URLs.

## CI and delivery

**GitHub Actions: SUCCESS — 23/23 steps** ([run 43754309285](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/43754309285)). Exact compiled source commit: `bbad666606c457b7db3faeceacf027e9a386eecd`. All new JSON backup codec tests, the existing WebHeads storage/card tests, Websites Scroll tests, Android compatibility regression checks and APK signature/version controls passed.

**Exact Debug APK:** [Lynket_v3.0.1_P16_WebHeadsBackup_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/43754309285/artifacts/14177443868), GitHub Actions artifact `14177443868`, **8,701,323 bytes**, SHA-256 `e9b6eff1b4e6c7144f7b27684ca786b71b945388e33db51597db9e426003a35b`.

**ChatGPT delivery workflow:** the connector named the downloaded raw APK `Lynket_P16_WebHeadsBackup_artefact.zip`, but its internal structure was verified as a valid APK, containing `AndroidManifest.xml` and `classes*.dex`. A **new user-visible copy** `/mnt/data/Lynket_3.0.1_P16_WebHeads_Sauvegarde_Import_LIVRAISON.apk` was created via `python_user_visible.exec`; its size, integrity and SHA-256 were verified against GitHub. Actual download usability in the ChatGPT mobile UI requires user confirmation.

**Physical verification pending:** test Save backup and Import backup on the S23 Ultra, including fresh-install import and duplicate-free repeat import, plus restoration permissions on a second device. The UI and JSON backup formats are new; do not mark this branch stable solely on passing CI.

No automatic release, PR, tag or stable merge.
