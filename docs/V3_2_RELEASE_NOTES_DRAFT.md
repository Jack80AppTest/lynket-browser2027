# Lynket 3.2 — WebHeads Backup & Android 16 Improvements

Lynket 3.2 brings a portable backup workflow for floating WebHeads, a more usable Websites panel, and the Android compatibility improvements developed across recent versions of this fork.

## What's new
- **WebHeads backup and import:** Go to **Settings → WebHeads backup & import** to export saved website URLs to a JSON file, or import them after reinstalling the app or on another device.
- **Safe import:** Existing WebHeads are retained. Duplicate URLs are skipped, and imported URLs can be restored as separate floating bubbles. Comma-separated and newline-separated URL text files are also supported.
- **Better Websites dialog:** Long lists scroll without hiding **Copy All**, **Share All**, or **Hide Overlay**. The popup now makes better use of available height in landscape.
- **Android 16 modernization:** Compiled and targeted for **Android API 36** while preserving the project's minimum **Android 4.1 (API 16)** support.
- **Other improvements preserved:** Restart recovery of non-private WebHeads, **Hide ≠ Close**, optional AMOLED theme, Chrome Custom Tabs Minimize fallback, and Chrome sharing / Slide Over lifecycle fixes.

## Important notes
The backup saves **URLs and their AMP indicators only**, not cookies, login sessions, history, or the full app configuration. Before uninstalling, save the backup outside the application's private data and confirm the file exists.

**Build type:** The provided CI APK is a **Debug/test build** with package `com.jk8026.lynket2027.debug`, not a production-signed Play Store release. Debug signatures may differ between CI runs and may require reinstalling. Keep a backup of your URLs first.

**Version:** 3.2 · `versionCode 65` · minSdk 16 · target/compile SDK 36.

Original Lynket authorship and GPL licensing remain preserved. This fork's Android modernization and maintenance are credited to John Krauza.

**Source:** https://github.com/Jack80AppTest/lynket-browser2027/tree/stable/v3.2

> Note: publish this text only after final validation and an explicit release-publication GO.
