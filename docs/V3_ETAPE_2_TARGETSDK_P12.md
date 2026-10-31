# Lynket Custom 2027 — P12 Android 12 target SDK API 31

## Base validée et périmètre autorisé

- P11 confirmée fonctionnelle sur Galaxy S23 Ultra par l'utilisateur le 31 octobre 2026.
- Branche P11 préservée : `dev/3.0.0-targetsdk-p11` au HEAD `f322e76d0bc1f5082da2b84110cf60ef0bec0859`, code P11 compilé `e5479dc0b50f1e6e69444a029b8fcbce72823bb5`.
- P12 sur branche indépendante `dev/3.0.0-targetsdk-p12` issue du HEAD P11; aucun merge autorisé.
- Changement visé : `targetSdkVersion 30 → 31` (Android 12). `compileSdk 36`, `minSdk 16`, Kotlin 2.2.21, Gradle 8.11.1, AGP 8.10.1, Java Gradle 17 / tests Java 11, versionName 2.1.7 / versionCode 62 et package Debug `com.jk8026.lynket2027.debug` inchangés.

## Adaptations ciblées Android 12

1. `android-app/app/build.gradle` : cible API 31.
2. `AndroidManifest.xml` : `android:exported="true"` explicite pour les quatre Quick Settings tiles, l'alias VIEW Web et l'activité SEND/PROCESS_TEXT. Leur exposition correspond aux points d'entrée externes déjà utilisés. Le boot receiver reste non exporté, les composants sans filtre conservent leur état.
3. `CustomTabs.java` : callbacks navigateur capables de recevoir l'URL courante en `FLAG_MUTABLE` seulement sur Android 12+, intents à contenu fixé en `FLAG_IMMUTABLE` sur Android 6+, sans changer les URL, le routing ou les fournisseurs.
4. `BottomBarManager.java` : `FLAG_MUTABLE` sur Android 12+ afin de préserver l'URL et l'ID de bouton injectés par le navigateur.
5. `BubbleNotificationUtil.kt` : PendingIntent de bulle Android native mutable sur Android 12+, comme nécessaire au mécanisme de bulle.
6. `WebHeadService.java` : PendingIntent de notification sans fill-in rendu immuable sur Android 6+, broadcasts restreints au package de l'application; aucune modification du stockage ni de l'affichage des WebHeads.
7. Workflow GitHub Actions P12 indépendant : compilation, tests existants et assertions supplémentaires sur le manifeste / les flags, contrôle package/version/cible/signature, livraison de l'APK **directement sans ZIP**.

## Points à valider sur téléphone

- Ouverture et partage de liens; fournisseurs Chrome/Samsung Internet/autres; boutons Custom Tabs (partage, copier, secondaire, menu, barre inférieure si activée).
- WebHeads : compteur, menu WEBSITES, Masquer/Restaurer, ferme uniquement sur action explicite, restauration après relance et après reboot si Android autorise.
- Actions de la notification WebHeads : masquer/arrêter l'overlay, gérer les bulles, ouvrir un nouvel onglet. Android 12 restreint les notifications qui lancent une activité depuis un receiver (notification trampoline); surveiller particulièrement ces deux boutons, et corriger de manière isolée si régression.
- Mode natif de bulles si utilisé; raccourcis rapides système; réception des liens et du partage externe; historique, recherche, mode article et thèmes.
- Pas de désinstallation ni d'effacement des données pour contourner un éventuel problème de signature Debug.

## État

P12 compilée, vérifiée et distribuée par GitHub Actions; validation sur téléphone encore en attente. La base P11 reste préservée.

Références : https://developer.android.com/about/versions/12/behavior-changes-12 ; https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start ; https://developer.android.com/reference/androidx/browser/customtabs/CustomTabsIntent .

Ce document est passif; aucun script de build ou workflow ne doit le lire ou le mettre à jour automatiquement.

## CI P12 — succès complet, validation téléphone en attente

- Workflow : https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41197537619 — `completed / success`.
- **Commit de code compilé et testé** : `62c2d8fa4918409458565fc2ac983b807bce6ea5`.
- `:app:assembleDebug` : **réussi** avec `targetSdk 31`, Kotlin 2.2.21, AGP 8.10.1, Gradle 8.11.1.
- Validation XML du bloc `<queries>` et des attributs `android:exported` : **réussie**.
- Contrôle Android 12 des flags `PendingIntent` : **réussi** (contrôles statiques complémentaires; ne remplace pas les tests sur appareil).
- Trois tests Robolectric existants : `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` : **réussis**.
- Identifiant `com.jk8026.lynket2027.debug`, version `2.1.7` / code `62`, `targetSdkVersion:31`, manifeste empaqueté et signature APK v2 : **réussis**.
- **APK livré directement, sans ZIP** : [Lynket_2027_v2.1.7_TargetSdk_P12_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41197537619/artifacts/13041805234) — 8 690 001 octets, artifact ID `13041805234`.
- Digest déclaré par GitHub : `sha256:07ee42013887e5c59e5876791b47c239bfe336d299720182f69b536691bba753` (non recalculé localement).
- **Statut : CI P12 entièrement verte; tests sur Galaxy S23 Ultra toujours en attente.** P11 reste la dernière base validée sur téléphone. Aucune fusion ni publication autorisée.
- Le présent commit de documentation se situe après le commit applicatif compilé; il ne modifie pas l'APK testé et n'est pas déclencheur du workflow P12 (`docs/**` ignoré).
