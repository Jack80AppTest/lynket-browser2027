# Lynket 2027 v3.0 — Étape 2, palier P4 : Gradle 7 / AGP 7 / Kotlin 1.7

**Date :** 30 octobre 2026 (Québec)  
**Statut :** vérifications automatisées réussies, validation manuelle sur téléphone **en attente**.  
**Branche candidate isolée :** `dev/3.0.0-toolchain-p4`.  
**Dernière référence validée par l'utilisateur sur téléphone :** `dev/3.0.0-toolchain-p3`.  
**Sauvegarde de secours :** [Release 2.1.7](https://github.com/Jack80AppTest/lynket-browser2027/releases/tag/2.1.7) et son tag, commit `f11edd0718e758e06686b5540f6d9b38b16143fd`.

## Versions comparées

| Élément | Palier P3 validé | Palier P4 candidat |
| --- | --- | --- |
| Gradle wrapper | 6.7.1 | **7.5.1** |
| Android Gradle Plugin | 4.2.2 | **7.4.2** |
| Kotlin de l'application | 1.5.31 | **1.7.22** |
| Kotlin buildSrc | 1.3.31 | **1.7.22** |
| JDK de compilation GitHub | 11 | 11 |
| minSdk / targetSdk / compileSdk | 16 / 28 / 29 | **inchangés** |
| versionName / versionCode | 2.1.7 / 62 | **inchangés** |
| applicationId Debug | `com.jk8026.lynket2027.debug` | **inchangé** |

## Modifications contrôlées

1. `android-app/gradle/wrapper/gradle-wrapper.properties` : mise à jour vers Gradle 7.5.1.
2. `android-app/build.gradle` : AGP 7.4.2 et Kotlin 1.7.22.
3. `android-app/buildSrc/build.gradle.kts` : plugin Kotlin JVM 1.7.22, ajout de Maven Central sans supprimer JCenter (compatibilité des anciennes dépendances).
4. `.github/workflows/lynket-v3-toolchain-p4.yml` : workflow de compilation indépendant, assertions des versions et mêmes tests de non-régression que P3.
5. Cinq **branches neutres `else -> Unit`**, ajoutées aux blocs `when` dans :
   - `BrowsingActivity.kt`
   - `ArticleActivity.kt`
   - `HomeFeedController.kt`
   - `HomeFragment.kt`
   - `HomeScreenShortcutCreatorActivity.kt`

La migration Kotlin 1.7 a rendu obligatoires ces cas exhaustifs dans les expressions. Les nouveaux cas sont des opérations neutres pour les états que les versions précédentes ignoraient déjà. **Aucune modification de données sauvegardées, de leur format, de leurs règles de suppression, des services WebHeads, du manifeste, des permissions ou des thèmes.**

## Historique CI

- [Exécution initiale #41153608778](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41153608778) : échec à `:app:compileDebugKotlin` ; cinq `when` non exhaustifs.
- Les changements individuels ont déclenché des exécutions intermédiaires annulées par la politique de concurrence.
- **[Exécution finale #41153945165](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41153945165) : SUCCESS**.
- **PASS** : compilation Android Debug avec Gradle 7.5.1 / AGP 7.4.2 / Kotlin 1.7.22.
- **PASS** : `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest`.
- **PASS** : identité `com.jk8026.lynket2027.debug`, code 62, nom de version 2.1.7, signature Debug vérifiée.
- **PASS** : publication directe d'APK sans ZIP.

**APK candidat :** [Lynket_2027_v2.1.7_Toolchain_P4_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41153945165/artifacts/13024920165).

## Contrôles manuels indispensables

Le succès de la CI ne prouve pas le comportement réel sur Android. Vérifier la [checklist de l'étape 1](V3_BASELINE_ETAPE_1.md), principalement :
- partage de liens et démarrage des Custom Tabs ;
- WebHeads multiples, conservation de l'ordre, fermeture individuelle, sauvegarde et restauration après relance ;
- Masquer/Restaurer sans effacement de sessions ;
- pages Web, mode article, raccourcis d'écran d'accueil, carte Recents ;
- thèmes clair/AMOLED et rafraîchissement immédiat des menus ;
- comportement après redémarrage sous réserve des restrictions d'Android.

**Signature :** chaque build Debug GitHub peut utiliser une clé différente. Ne pas désinstaller une installation avec des sessions importantes sans sauvegarde.

## Limites et suite

La version Kotlin 1.7.22 **conserve encore Kotlin Android Extensions**, qui est obsolète et supprimé à partir de Kotlin 1.8. La migration de ces extensions vers View Binding et/ou Kotlin Parcelize fera l'objet d'un palier séparé ; ne pas passer à Kotlin 1.8+ sans cette migration.

La conformité Google Play n'est pas atteinte : `targetSdk` est toujours 28. Aucune modification de l'identité Android de l'application, aucun déploiement Play Store et **aucun merge vers `main`, `dev/3.0.0`, `stable/*` ou la Release 2.1.7** ne sont autorisés sans accord explicite.

**Décision :** attendre le test et la validation de l'utilisateur sur téléphone avant de créer le prochain palier.
