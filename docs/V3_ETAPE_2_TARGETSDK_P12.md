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

Candidat P12 : compilation et tests GitHub Actions à confirmer par le run du commit d'application. Aucun test sur téléphone revendiqué. P11 demeure la dernière base validée tant que l'utilisateur n'a pas confirmé P12.

Références : https://developer.android.com/about/versions/12/behavior-changes-12 ; https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start ; https://developer.android.com/reference/androidx/browser/customtabs/CustomTabsIntent .

Ce document est passif; aucun script de build ou workflow ne doit le lire ou le mettre à jour automatiquement.
