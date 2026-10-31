# Lynket Custom 2027 — Android 16 reference (fork)

This repository also contains **Lynket Custom 2027**, a maintained fork of the original [Lynket / Chromer](https://github.com/arunkumar9t2/lynket-browser). The original project description, credits, contribution instructions and GPL license are preserved **below** for historical context; links to the original Play Store listing refer to the original app, not to this fork.

**Current validated source reference:** [`stable/v3.0.1`](https://github.com/Jack80AppTest/lynket-browser2027/tree/stable/v3.0.1) — internal version `3.0.1` (`versionCode 64`), `compileSdk 36`, `targetSdk 36`. This is a development-signed Android build, **not** a Google Play production artifact. See the version-specific build report in [`docs/V3_0_1_VERSIONING.md`](docs/V3_0_1_VERSIONING.md).

## Android modernization contribution

**John Krauza — Android modernization and compatibility contributor.**

John coordinated and validated the modernization work that keeps Lynket compatible with recent Android releases, including changes to Android build tools, API compatibility, UI insets and navigation behavior. He also specified and tested the fork's preserved WebHeads sessions, Hide/Restore behavior, AMOLED theme and Chrome navigation fixes on real devices.

This acknowledgment recognizes modernization and testing contributions to **Lynket**, not authorship of the original app or a change to the app's name. Original authorship and credits are preserved below. No personal contact information is published.

See [`CONTRIBUTORS.md`](CONTRIBUTORS.md) for a brief attribution of the fork and upstream project.

**Where to start as a developer:**
- [**Architecture and maintenance map**](docs/P16_CODE_ARCHITECTURE.md): source entry points, navigation, WebHeads persistence, Android compatibility, and regression tests.
- [**P16 code-cleanup report**](docs/P16_CODE_CLEANUP.md): documents exactly what was changed (documentation/comments only).
- Android app module: [`android-app/app/`](android-app/app/); Gradle wrapper and project settings: [`android-app/`](android-app/).
- Source reference history is in [`docs/`](docs/). Older P14/P15/P16 reports are development history, not instructions to change the current implementation.

**Behavioral contract:** hiding WebHeads only removes the overlay. It does **not** delete saved tabs. Explicit close actions remove persisted URLs; incognito tabs are not saved. Chrome Custom Tabs and sharing use separate paths; preserve their activity lifecycle semantics when making changes.

**Build and tests:** from `android-app/`, the Android Debug build is `./gradlew :app:assembleDebug`. The project CI workflows include specific Robolectric tests and Android compatibility checks. The current fork Debug application ID ends with `.debug`; do not confuse it with a production-signed package.

---

## Original Lynket README (upstream text retained)

# Lynket
**Android browser app based on Custom Tabs protocol.**

<img src="android-app/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="left"
width="200"
    hspace="10" vspace="10">

Lynket utilizes Chrome Custom Tab API to create a customized browsing experience while adding innovative features like background loading with floating bubbles, article mode and multitasking using Android's recent menu.

Lynket is available for free on the Google Play Store.


<a href="https://play.google.com/store/apps/details?id=arun.com.chromer">
    <img alt="Get it on Google Play"
        height="80"
        src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />
</a>

If you have got some time, read the introduction post on [my blog](https://arunkumar.dev/Lynket-browser-2nd-anniversary-update/) or watch intro [Video](https://www.youtube.com/watch?v=PbB6ChrkMnE)

![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/chromer_screenshots.png)

## Features
| Features| Demo|
|--|--|
| **Web heads** - Floating bubbles to load multiple links and launch them.  | ![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Web%20heads%20intro.gif) |
| **Web heads** - Intelligently manages background loading of Custom Tabs using Android's overview screen. Uses Lollipop Document API.| ![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Background%20Loading.gif) |
| **Provider** - Lynket works with any custom tab compatible browser. This means features like adblock, Google Account sync, data saver are inherited.  | ![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Provider%20Selection.gif) |
| **Article mode** - Filters all unnecessary content and renders web page on a RecyclerView efficiently. Uses Crux library.  | ![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Article%20Mode.gif) |
| **Minimize and Tabs** - Lynket can minimize the tab and also show all active tabs for you to quickly switch to.  |  ![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Multitasking.gif)|


## Architecture and Code Structure
Lynket is written on a MVVM Architecture and uses Dagger 2 for dependency injection. The source code is packaged based on feature.

```
app/src/java/<package>
	<feature_1>\
	<feature_2>\
	shared\
	utils\
```
### Architecture
Below outlines sample structure of most screens. 
![enter image description here](https://raw.githubusercontent.com/arunkumar9t2/chromer/master/art/Chromer%20Architecture.png)


### Code Style

 - [mNo Hungarion notation.](http://jakewharton.com/just-say-no-to-hungarian-notation/)
 - No `Impl` suffixes for interface implementations. Instead name based on what it does. Ex: `AppStore` and implementations `AppDiskStore` and `AppSystemStore`.
 - All new modules are preferred to be written in `Kotlin`
 - Formatting - Android Studio Default.

## Contributions
You are more than welcome to contribute to Lynket's development. New features are branched off of `develop` . PRs are welcome against the `develop` branch. 
How you can help:

 - Grab an issue from [issues](https://github.com/arunkumar9t2/chromer/issues) section that interests you.
 - Unit testing using Roboelectric and Dagger 2 is already setup but coverage is rather low. Adding tests to existing features would greatly help! Refer [src/test/](https://github.com/arunkumar9t2/chromer/tree/master/app/src/test/java/arun/com/chromer).
 - Break the ice. Feel free to create an issue to discuss your ideas.

## License

Lynket is licensed under the [GNU v3 Public License.](LICENSE)

