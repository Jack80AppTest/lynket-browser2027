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

## Final approved UI — Settings instead of Home (15 November 2026)

**This section supersedes the earlier Home-card button placement.** After reviewing a screenshot, the user expressly approved a fourth Settings category **WebHeads — Sauvegarde et importation**, below Browsing options. It opens a dedicated page with **Exporter les WebHeads** and **Importer les WebHeads**, each with an explanatory subtitle. The existing home Saved Bubbles card is restored to exactly **Masquer / Restaurer**; the premature backup buttons were removed.

The JSON format, Android picker fallback (API 16–18 using sharing/GET_CONTENT), duplicate-free import and the BubbleSessionStore database remain unchanged. New activity is internal and non-exported; the original three Settings choices stay in their original order.

**Latest GitHub Actions SUCCESS — 23/23 steps**, [run 43756049150](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/43756049150), exact compiled source commit `f7643c4d5848befa9d0ca0fab8991af5af22685e`. Includes regression test of fourth Settings entry, existing backup codec/storage, old Android guards, package version/signature.

**New exact compiled APK:** artifact [14178546400](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/43756049150/artifacts/14178546400) — `Lynket_v3.0.1_P16_WebHeadsBackup_Settings_Debug.apk`, **8,708,230 bytes**, SHA-256 `59ad60ca213d6ed4272273ed4de5277023933d3fa8ade57bbd3a7ae707cbd2d3`. The GitHub artifact was downloaded, recognized by its root AndroidManifest.xml and classes*.dex, passed ZIP integrity checks, and copied into a new ChatGPT attachment using `python_user_visible.exec`: `/mnt/data/Lynket_3.0.1_P16_WebHeads_Settings_Importer_Exporter_LIVRAISON.apk`; exact size and SHA-256 verified. **User has not yet confirmed the mobile link works.**

Physical tests still required: the two Settings commands; Save to a file **outside** the app, check actual JSON exists, then reinstall/import/duplicate-free second import. **Never uninstall before confirming the backup exists and is readable.**

**Stable and main unchanged.** No release, tag or PR.
