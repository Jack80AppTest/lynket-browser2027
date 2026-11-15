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

**Pending GitHub Actions confirmation.** Only share the exact compiled commit's APK, via `python_user_visible.exec` copied into a fresh user-facing path with verified SHA-256 and ZIP/APK structure, per the project APK delivery workflow. No automatic release, PR, tag or stable merge.
