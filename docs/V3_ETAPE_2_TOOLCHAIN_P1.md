# Lynket 2027 v3.0 — Étape 2, palier 1 : migration contrôlée des outils

**Date :** 30 octobre 2026  
**Résultat automatisé :** RÉUSSI — [GitHub Actions #41149216229](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41149216229)  
**Branche candidate (non fusionnée) :** `dev/3.0.0-toolchain-p1`  
**Base préservée :** `dev/3.0.0`  
**Retour de secours immuable par convention :** tag/Release `2.1.7` (`f11edd0718e758e06686b5540f6d9b38b16143fd`).

## Portée exacte

| Outil | Avant | Candidat |
| --- | --- | --- |
| Gradle wrapper | 5.6.2 | **5.6.4** |
| Android Gradle Plugin | 3.5.2 | **3.6.4** |
| Kotlin | 1.3.50 | **inchangé** |
| JDK du build | 8 | **inchangé** |
| compileSdk | 29 | **inchangé** |
| targetSdk | 28 | **inchangé** |
| minSdk | 16 | **inchangé** |
| Version Android | 2.1.7 / code 62 | **inchangée** |
| Identifiant Debug | com.jk8026.lynket2027.debug | **inchangé** |

Seuls les fichiers `android-app/build.gradle`, `android-app/gradle/wrapper/gradle-wrapper.properties` et le nouveau workflow `.github/workflows/lynket-v3-toolchain-p1.yml` ont été modifiés/ajoutés pour la compilation. Aucun code, manifeste, permission ou ressource de l'application n'a été modifié.

Gradle 5.6.4 est le minimum officiel requis pour Android Gradle Plugin 3.6.4 selon [Android Developers](https://developer.android.com/build/releases/about-agp).

## Vérification GitHub Actions

Workflow autonome sur la branche candidate, avec exactement les mêmes tests que notre base fonctionnelle :

- **PASS** : `:app:assembleDebug` avec Gradle 5.6.4 et AGP 3.6.4
- **PASS** : `BubbleSessionStoreTest` (sauvegarde, doublons, suppression)
- **PASS** : `SavedBubblesCardModelTest` (Masquer/Restaurer)
- **PASS** : `SettingsGroupAdapterTest` (couleurs clair/AMOLED)
- **PASS** : signature APK Debug, ID `com.jk8026.lynket2027.debug`, version 2.1.7 / 62
- **PASS** : publication d'un APK directement téléchargeable sans ZIP

**APK candidat :** [Lynket_2027_v2.1.7_Toolchain_P1_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41149216229/artifacts/13022990142) (fichier GitHub Actions temporaire).

## Tests matériels restant à faire

Les tests automatisés ne garantissent pas le bon fonctionnement de l'overlay Android, de WebHeads, des Custom Tabs, de la restauration réelle après redémarrage ou du changement immédiat de thème. Appliquer la checklist complète [Étape 1](../docs/V3_BASELINE_ETAPE_1.md) à l'APK candidat **seulement après avoir sécurisé les données de l'installation de test**.

**Attention signature :** chaque build Debug de GitHub peut être signé avec une clé différente. Il peut donc être impossible d'installer cet APK par-dessus une ancienne installation Debug sans désinstaller celle-ci, ce qui supprimerait les données de l'application. Ne pas désinstaller ni effacer les données pour réaliser ce palier.

## Décision

Ce palier a passé les validations automatiques, mais reste une **candidature non fusionnée**. Il ne prouve pas la conformité Google Play. Aucune migration Kotlin, aucune montée de `targetSdk`, aucune nouvelle version produit ni modification de `main`, `stable/*`, de la Release 2.1.7 ou de `dev/3.0.0`.

**Prochaine action uniquement après validation explicite de l'utilisateur** : accepter ce palier comme point de départ puis aborder un autre groupe d'outils de compilation dans une nouvelle branche d'essai.
