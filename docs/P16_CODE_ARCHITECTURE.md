# Lynket Custom 2027 — developer map (Android 16 / P16)

This is a **human-readable reference**, not a build input, configuration file or automated workflow. It describes the validated **v3.0.1 / P16** implementation. It does not replace source inspection or tests.

## Scope and lineage

- Fork of the original [Lynket / Chromer](https://github.com/arunkumar9t2/lynket-browser); retain its original copyright notices and GPL license.
- Validated version: `3.0.1` (`versionCode 64`), `compileSdkVersion 36`, `targetSdkVersion 36`, `minSdkVersion 16`.
- Source of the validated build: `stable/v3.0.1`. The P16 code has been tested on Galaxy S23 Ultra (Android 14) and Galaxy Tab A11+ (Android 16); the subsequent version-label build passed CI. Read [`V3_0_1_VERSIONING.md`](V3_0_1_VERSIONING.md) for the exact build and testing distinction.
- Android module is under `android-app/app/`, not at repository root. Android code uses Kotlin and Java, MVVM/Dagger and RxJava. Existing dependencies are deliberately **not** upgraded by the code cleanup.

## Source map

| Feature / responsibility | Source entry point | What to examine before changing |
| --- | --- | --- |
| Application setup, injection | `android-app/app/src/main/java/arun/com/chromer/Chromer.kt` | Dagger application component, initialization |
| Main screen and saved bubbles controls | `.../home/HomeActivity.kt` and `.../home/epoxycontroller/` | Home lifecycle, list/cards, restoration entry points |
| Decide how URLs are opened | `.../tabs/TabsManager.kt` and `.../tabs/DefaultTabsManager.kt` | Distinguish the normal asynchronous route, sharing route and WebHeads route |
| Incoming Android share sheet | `.../browsing/shareintercept/ShareInterceptActivity.kt` | `ACTION_SEND`, `ACTION_PROCESS_TEXT`, URL extraction and completion before `finish()` |
| Chrome Custom Tabs commands | `.../browsing/customtabs/bottombar/BottomBarReceiver.kt` | Some Chrome callbacks omit the current URL; preserve the saved original-URL fallback |
| Persistent WebHeads SQLite | `.../bubbles/webheads/BubbleSessionStore.java` | Saved non-incognito URLs and insertion ordering; deletions are explicit |
| Restore saved WebHeads | `.../bubbles/webheads/BubbleSessionRestorer.java` | Restoration may be deferred when overlay permission or background-start rules prevent launch |
| On-screen bubble lifecycle | `.../bubbles/webheads/WebHeadService.java` | Reconcile saved URLs with visible views; distinguish hide/service teardown from explicit closing |
| System bar insets | `.../shared/base/activity/EdgeToEdgeInsets.java` | Android 15+ compatibility; avoid applying padding twice |
| About and changelog | `.../about/AboutFragment.kt`, `.../about/changelog/Changelog.java`, `android-app/app/src/main/res/values/changelog.xml` | Version name comes from `BuildConfig.VERSION_NAME`; HTML is displayed with `loadDataWithBaseURL` |
| Build identity and dependencies | `android-app/app/build.gradle` | Debug package `com.jk8026.lynket2027.debug`; don't change application ID, SDK or version accidentally |

In the table, **`...` stands for `android-app/app/src/main/java/arun/com/chromer/`**.

## Essential runtime contracts

### 1. Hide is not close

- `BubbleSessionStore` owns the SQLite records of active **non-incognito** WebHeads. Overlay views are disposable representations, not the source of truth.
- `WebHeadService.restoreSavedWebHeads()` iterates saved records and creates only views that are not currently loaded.
- When an incoming eligible link is accepted, `WebHeadService.processIntent()` saves its URL before adding the WebHead.
- Temporary HIDE / service teardown releases overlay views but leaves SQLite intact. `onDestroy()` must not delete the saved session.
- `WebHeadService.onWebHeadDestroyed()` handles explicit individual closure; `closeAll()` explicitly clears the saved list. Do **not** call the storage `remove()` or `clear()` method from generic UI dismissal paths.
- A URL opened in a page does not silently delete its saved bubble. Restoration is intended to be repeatable without duplicate visible bubbles.
- Incognito pages are excluded from persistent storage.

### 2. URL launching and Custom Tabs

- `DefaultTabsManager.openUrl()` uses the established asynchronous launch path for ordinary navigation.
- `ShareInterceptActivity` calls `tabsManager.openSharedUrl(this, Website(url))` while the receiving activity is still alive. It finishes on completion or handles error. This avoids racing an Android background-activity launch after the share receiver has closed.
- `BottomBarReceiver` reads the URL provided by the Custom Tabs callback, falling back to the original URL stored in its PendingIntent when necessary. `Minimize` uses that original URL when present.
- The app cannot directly control every aspect of the **external browser's** Custom Tab; if a browser-specific problem appears, reproduce it on devices and gather logs before changing activity flow.

### 3. Modern Android layout and services

- `EdgeToEdgeInsets.apply()` is guarded for Android API 35+. One centralized inset strategy protects layouts from system bars; additional per-screen padding can double offsets.
- Android overlay permissions and foreground-service rules can delay or block a background restoration attempt without deleting saved URLs.
- User preferences, Article mode, selected Custom Tabs provider, history and themes are separate features. Keep their integration behavior unchanged unless a change is explicitly requested.

## Build, checks, and change control

Run commands from `android-app/`:

```sh
./gradlew --no-daemon :app:assembleDebug
./gradlew --no-daemon :app:testDebugUnitTest \
  --tests arun.com.chromer.bubbles.webheads.BubbleSessionStoreTest \
  --tests arun.com.chromer.home.epoxycontroller.model.SavedBubblesCardModelTest \
  --tests arun.com.chromer.settings.SettingsGroupAdapterTest
```

- Repository CI also checks compatibility rules for Android 11–16, the Changelog WebView, system-bar insets, Chrome share/minimize behavior, APK package/version/targetSdk and APK Signature Scheme v2.
- A green CI run is **not** a substitute for manual checks on both a phone and a tablet. Especially verify minimize, sharing, bubble hide/restore, orientation and Changelog if those areas change.
- Never remove, close, or overwrite a user's saved tabs as part of a cosmetic change. Do not uninstall an existing Debug app if preserving its local data matters.
- `stable/v3.0.1` is a reference branch; develop on a separate branch. Do not merge into `main`, publish, tag, sign or alter existing releases without an explicit separate instruction.
- Legacy development reports in `docs/` are historical evidence: they are *passive*, not compile-time dependencies. This map is also passive and is **not** to be read or rewritten automatically by CI, release scripts or builds.

## Known limits / non-goals

- This document is a **source-navigation guide**. It does not assert that every class has been reviewed or that every legacy dependency is safe/current.
- The code-cleanup stage is documentation and comment-only: no source instructions, dependencies, permissions, version values or application behavior should change. See [`P16_CODE_CLEANUP.md`](P16_CODE_CLEANUP.md).
