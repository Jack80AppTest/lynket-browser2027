# Lynket 2027 v3.0 — Étape 2, palier 2 : Kotlin 1.3.72

**Date :** 30 octobre 2026  
**Statut :** compilation automatisée réussie, **test sur téléphone à faire / validation utilisateur attendue**.  
**Branche candidate :** `dev/3.0.0-toolchain-p2-kotlin` (ne pas fusionner sans accord explicite).  
**Parent fonctionnel validé physiquement par l'utilisateur :** `dev/3.0.0-toolchain-p1`.  
**Récupération immuable par convention :** Release/tag `2.1.7`, SHA `f11edd0718e758e06686b5540f6d9b38b16143fd`.

## Changement technique exact

Seule la version partagée Kotlin dans `android-app/build.gradle` est passée de **1.3.50** à **1.3.72**. Elle règle à la fois `kotlin-gradle-plugin` et `kotlin-stdlib`, via `$kotlin_version`.

| Élément | Palier 1 validé | Palier 2 candidat |
| --- | --- | --- |
| Kotlin plugin et standard library | 1.3.50 | **1.3.72** |
| Gradle wrapper | 5.6.4 | 5.6.4 |
| Android Gradle Plugin | 3.6.4 | 3.6.4 |
| Java du build | 8 | 8 |
| compileSdk / targetSdk / minSdk | 29 / 28 / 16 | **inchangés** |
| applicationId Debug | `com.jk8026.lynket2027.debug` | **inchangé** |
| App version / versionCode | `2.1.7` / 62 | **inchangés** |

Nouveaux fichiers de ce palier :
- `.github/workflows/lynket-v3-toolchain-p2.yml` (copie isolée du workflow validé, avec assertions de versions et sortie P2).
- Ce rapport `docs/V3_ETAPE_2_TOOLCHAIN_P2.md`.

Aucun code Java/Kotlin métier, fichier AndroidManifest, ressource UI, changement du thème ou des données persistantes n'a été modifié.

## Résultats automatisés

**Workflow :** [GitHub Actions #41150849677](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41150849677), résultat **SUCCESS**.

- **PASS** : `:app:assembleDebug` avec Kotlin 1.3.72.
- **PASS** : `BubbleSessionStoreTest` — sauvegarde, restauration, ordre, doublons, suppression explicite.
- **PASS** : `SavedBubblesCardModelTest` — Masquer / Restaurer.
- **PASS** : `SettingsGroupAdapterTest` — titres lisibles des thèmes clair/AMOLED.
- **PASS** : identité de l'application, version 2.1.7, signature Debug vérifiée.
- **PASS** : APK publié sans ZIP.

**APK candidat :** [Lynket_2027_v2.1.7_Toolchain_P2_Kotlin_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41150849677/artifacts/13022662947).

## Test physique et sécurité des données

Les tests GitHub ne peuvent pas garantir le comportement des WebHeads flottants, des overlays Android, des Custom Tabs ou des restaurations réelles après arrêt/redémarrage.

Avant de valider le palier, vérifier les opérations de la [checklist de l'étape 1](V3_BASELINE_ETAPE_1.md) : partage d'URL, ordre et restauration des bulles, Masquer/Restaurer, thèmes clair/AMOLED, navigation, historique et mode article.

**Attention :** les APK Debug de GitHub Actions peuvent avoir des signatures de clés différentes selon le runner. Si l'installation par-dessus l'ancienne version Debug est refusée, **ne pas désinstaller l'application contenant les sessions** avant d'avoir sauvegardé ses données. Aucune installation n'est requise si elle met les données en danger.

## Décision de passage au palier 3

La branche P2 reste candidate tant que l'utilisateur n'a pas validé les essais sur téléphone. Ne pas modifier `main`, `dev/3.0.0`, `stable/*`, le tag/Release `2.1.7` ou la branche P1. Chaque palier suivant doit être isolé, compilé, testé, puis soumis à confirmation de l'utilisateur. Ne pas supposer que ce palier rend l'application compatible avec les exigences récentes du Play Store.
