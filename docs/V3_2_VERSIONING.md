# Lynket 3.2 — Versioning, changelog and stable-source preparation

## Authorization and objective
User explicitly authorized a new **3.2** stable branch based on the latest accepted P16/WebHeads Backup Settings implementation, with the new **3.2 entry in the app's own changelog**. Prepare an installation candidate, text for a future GitHub Release, and an exhaustive Markdown handoff. Do **not** create a GitHub Release, tag, pull request, or merge into `main` without a separate explicit user GO.

## Baseline and branch policy
- Fork: https://github.com/Jack80AppTest/lynket-browser2027 (upstream: https://github.com/arunkumar9t2/lynket-browser).
- Approved source: `dev/3.0.1-p16-webheads-backup` at `9c6d70e8443dda4ae15687bce9ba7d99bf665bcb`. Previous passed CI on UI commit `f7643c4d5848befa9d0ca0fab8991af5af22685e`, run `43756049150`, 23 checks, Debug APK size 8,708,230 bytes.
- Isolated preparation: `dev/3.2-release-prep`. After successful CI, create immutable new source reference `stable/v3.2` from the **exact tested code commit**.
- Old `stable/v3.0.1` at `55b82079f7fedb3aa9e3da4cbe6680b4278969c5` and `main` at `c3e6f3ffe2b5e8c9e818e019d1386772d84a161d` must remain untouched.

## Intended changes
1. `android-app/app/build.gradle`: versionName `3.2`, monotonically increment versionCode from 64 to **65**; maintain `applicationId com.jk8026.lynket2027`, Debug suffix `.debug`, minSdk **16**, compileSdk **36**, targetSdk **36**.
2. `android-app/app/src/main/res/values/changelog.xml`: prepend a **3.2 — Lynket** HTML section about local URL export/import under Settings, duplicate-safe import, fixed WEBSITES scrolling/landscape, preservation of old modernizations. Retain 3.0.1, 3.0.0, 2.1.7 and the entire original changelog history. Do not rename visible application `Lynket`.
3. `README.md`: update current stable source reference, retain upstream README, author credit and license.
4. New development-only GitHub Actions workflow `.github/workflows/lynket-v3-3.2.yml`, inheriting the complete P16 tests and adding exact version/changelog/manifest/APK identity checks.
5. Produce an external release-notes text and **complete transfer Markdown**, delivered as verified local artifacts. This documentation must clearly distinguish successful CI from on-device import/export tests not yet conducted.

## Functional boundaries
No alterations to WebHeads persistence, import/export algorithms, file pickers, popup layout, Custom Tabs, Slide Over, themes, user credits or main/stable branches in the version bump. **HIDE is not CLOSE**, and removing overlays must not delete SQLite URLs.

## CI evidence
Pending run on `dev/3.2-release-prep`. Verify all required tests, complete workflow success, aapt versionName `3.2`/code `65`/targetSdk `36`, `apksigner` v2, raw direct APK artifact (not ZIP wrapping), size and SHA-256. The Debug signature is not a permanent production signing identity. Never claim publication to Play Store or a production-signed APK.
