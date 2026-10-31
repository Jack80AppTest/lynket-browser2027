# Lynket 2027 v3.0 — Étape 2, palier 3 : saut de génération des outils

**Date :** 30 octobre 2026 (Amérique/Toronto)  
**Statut :** **CI réussie**, **validation sur téléphone en attente**.  
**Branche d'essai :** `dev/3.0.0-toolchain-p3` (aucune fusion).  
**Référence utilisateur validée sur téléphone :** `dev/3.0.0-toolchain-p2-kotlin`.  
**Sauvegarde durable :** Release + tag GitHub `2.1.7` (commit `f11edd0718e758e06686b5540f6d9b38b16143fd`).

## Versions

| Composant | P2 validé | P3 candidat |
| --- | --- | --- |
| Gradle wrapper | 5.6.4 | **6.7.1** |
| Android Gradle Plugin | 3.6.4 | **4.2.2** |
| Kotlin (plugin + bibliothèque standard) | 1.3.72 | **1.5.31** |
| Java pour Gradle | 8 | **11** (workflow GitHub) |
| Kotlin buildSrc | 1.3.31 | **inchangé**, compilation validée |
| compileSdk / targetSdk / minSdk | 29 / 28 / 16 | **inchangés** |
| versionName / versionCode | 2.1.7 / 62 | **inchangés** |
| applicationId Debug | com.jk8026.lynket2027.debug | **inchangé** |

## Modification détaillée et diagnostic des tentatives

Les seules modifications au projet, en plus du workflow P3 autonome, sont :

- `android-app/build.gradle` : mises à jour de versions AGP/Kotlin.
- `android-app/gradle/wrapper/gradle-wrapper.properties` : Gradle 6.7.1.
- `android-app/gradle.properties` : suppression de trois options héritées et invalides/inappropriées pour AGP 4.2 :
  - `android.enableUnitTestBinaryResources=true` — bloquait la configuration ; le fichier `app/build.gradle` utilise déjà `testOptions.unitTests.includeAndroidResources = true`.
  - `android.enableSeparateAnnotationProcessing=true` — supprimée depuis AGP 4.0.
  - `android.enableR8=false` — ancienne désactivation de R8 retirée pendant la migration ; le comportement de la variante Release et son minificateur devront être vérifiés distinctement avant toute publication.

**Le code métier Java/Kotlin, l'interface et les ressources Android, le manifeste, les identifiants et versions produit, les WebHeads et leurs données n'ont pas été modifiés.**

Exécutions GitHub Actions :
- [#41152299059](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41152299059) : échec de configuration causé par `android.enableUnitTestBinaryResources`.
- [#41152473948](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41152473948) : échec de configuration causé par `android.enableSeparateAnnotationProcessing`.
- **[#41152640623](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41152640623) : SUCCESS**, après nettoyage des anciennes options.

## Vérifications automatisées P3

- **PASS** : `:app:assembleDebug` sur Java 11, Gradle 6.7.1, AGP 4.2.2, Kotlin 1.5.31.
- **PASS** : `BubbleSessionStoreTest` — persistance et suppression explicite.
- **PASS** : `SavedBubblesCardModelTest` — boutons Masquer et Restaurer.
- **PASS** : `SettingsGroupAdapterTest` — lisibilité clair/AMOLED.
- **PASS** : identité de paquet, version 2.1.7/62 et signature APK Debug.
- **PASS** : APK distribué directement sans ZIP.

**APK :** [Lynket_2027_v2.1.7_Toolchain_P3_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41152640623/artifacts/13023443856).

**Important :** chaque compilation Debug GitHub peut avoir une clé de signature différente ; ne jamais désinstaller une application avec onglets importants sans sauvegarde. Les tests CI ne remplacent pas les essais sur appareil, notamment superposition WebHeads, partage, restauration après redémarrage, accès aux pages, mode article et changements AMOLED.

## Décision et garde-fous

**Ne pas intégrer P3 à `dev/3.0.0`, `main` ou une branche stable, et ne pas modifier le tag/Release `2.1.7`, sans instruction explicite.**

La compatibilité Google Play n'est **pas** obtenue à ce palier : la cible Android reste 28, les anciennes dépendances et Kotlin Android Extensions demeurent. Ce palier constitue uniquement une modernisation contrôlée de la chaîne de compilation, destinée à être testée sur appareil avant de continuer.

## Prochaine vérification manuelle

Suivre la [checklist de non-régression v3](V3_BASELINE_ETAPE_1.md), notamment : partages, WebHeads multiples, hide/restore, persistance, fermeture explicite, menus, thèmes, historique, Custom Tabs et mode article. Signaler tout changement de comportement avant de continuer.
