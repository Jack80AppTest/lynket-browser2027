# Lynket Custom 2027 — Version interne 3.0.1 / Android 16

## Objet et autorisation

À la demande expresse de l'utilisateur le 31 octobre 2026, ajuster **uniquement les informations de version affichées dans l'application et son journal des modifications**, et les assertions/noms de l'artefact GitHub Actions. Le code P16 était déjà validé physiquement sur Galaxy S23 Ultra (Android 14) et Galaxy Tab A11+ (Android 16).

## Base et protection

- Source : branche `stable/v3.0.1`, commit `9918f69fcad5a18df27beb6ab2cab3ff07ec7e00` (code P16 testé `7d68ffa573fc9241b1e646a2387b9acbb4904fbc`).
- Travail isolé : `dev/3.0.1-versioning`. La branche de référence `stable/v3.0.1` et `main` ne doivent pas être altérées avant l'étape d'intégration explicitement validée.
- Version Android compilée avant changement : `2.1.7 - dev`, versionCode `62`.
- Version Android attendue après changement : **`3.0.1`, versionCode `64`**, avec applicationId Debug `com.jk8026.lynket2027.debug` toujours distinct d'une Release signée.
- Android targetSdk / compileSdk : 36 / 36 ; minSdk 16. Kotlin 2.2.21, AGP 8.10.1, Gradle 8.11.1 conservés.

## Changements expressément limités

1. `android-app/app/build.gradle` : `versionName 3.0.1`, `versionCode 64`, sans suffixe de version Debug, afin que **À propos → Version** affiche exactement `3.0.1` grâce à `BuildConfig.VERSION_NAME`. Le suffixe `.debug` de l'identifiant Android reste conservé, sans nouvelle clé de signature.
2. `android-app/app/src/main/res/values/changelog.xml` : titres et résumés 3.0.1 et 3.0.0 en tête; historique 2.1.7 et antérieur conservé.
3. Nouveau workflow GitHub Actions dédié `.github/workflows/lynket-v3-3.0.1.yml` sur branche de travail : copie contrôlée des vérifications P16, trois tests Robolectric et contrôles aapt/apksigner, validation des informations de version et livraison directe de `Lynket_Custom_2027_v3.0.1_Android16_Debug.apk` sans ZIP.

## Fonctionnalités inchangées

Aucun changement de `ShareInterceptActivity`, `BottomBarReceiver`, `DefaultTabsManager`, `TabsManager`, WebHeads, SQLite, restaurations, navigateur, mode Article, thèmes, affichages/insets, permissions, manifeste ou identités Android. Aucun changement de `Changelog.java` : la correction `loadDataWithBaseURL` déjà validée demeure en place.

## Résultat des tests

**GitHub Actions 3.0.1 : SUCCESS**, [run 41226767899](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41226767899), job 124612434523, **21 étapes réussies**. Code exact compilé : `96c2a143867432bd263c0de4f389110de1291899`. La mise à jour documentaire après le build ne change aucun octet applicatif. Les trois tests Robolectric ont réussi; contrôles Android 11–16, Custom Tabs/Slide Over, absence de double insets, XML Changelog, `aapt` (versionName `3.0.1`, code `64`, targetSdk `36`, identifiant Debug) et `apksigner` (v2) ont réussi.

- **APK direct, sans ZIP :** [Lynket_Custom_2027_v3.0.1_Android16_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41226767899/artifacts/13051949706), ID `13051949706`, **8 693 525 octets**.
- SHA256 des métadonnées GitHub de l'artefact, et non recalculé depuis les octets APK : `31e50e9f2c1cad51b7df738450fdddaef855a4ba608bc38aa55f6e9522e7f708`.
- Appareils : **fonctionnalités P16 déjà validées physiquement** avant le changement de numéro. La présentation 3.0.1 du nouvel APK reste à vérifier physiquement dans À propos et Changelog après installation. Ne pas prétendre que ce nouvel APK est déjà retesté sur les deux appareils.
- Publication : la Release/tag 3.0.1 n'est pas encore créée; `main` inchangée, signature Debug maintenue.

## Livraison et publication

À chaque livraison : lien de ce rapport Markdown et lien de l'APK natif sans ZIP de **ce même commit compilé**. Le build reste **Debug** (`com.jk8026.lynket2027.debug`), pas une signature Release/Google Play. Ne pas créer automatiquement de tag `3.0.1`, GitHub Release, PR ou fusion vers `main`. Toute éventuelle mise à jour de `stable/v3.0.1` doit conserver l'historique par fast-forward après validation.
