# Lynket Custom 2027 — P16, Android 16 targetSdk 36

## Autorisation et branche
- L'utilisateur demande de poursuivre à **P16 pendant qu'il valide P15 sur une tablette Android 16**. P16 est **candidate et non validée**.
- Source exacte : branche P15 `dev/3.0.0-targetsdk-p15`, commit de code du correctif Changelog `41470514079c48fab2481275ec9ed4047207fd73`. Ne jamais confondre P16 avec `dev/3.0.0-android14` (version Developer 3.0.0 stable côté Android 14).
- Branche dédiée `dev/3.0.0-targetsdk-p16`, sans toucher à P14, P15, à la version Developer 3.0.0 ou à `main`. Aucun merge ni GitHub Release.

## Changement P16
1. `android-app/app/build.gradle` : `targetSdkVersion 35 → 36`; garder compileSdk 36, minSdk 16, versionName 2.1.7, versionCode 62, identifiant Debug `com.jk8026.lynket2027.debug` et chaîne Gradle/Kotlin/AGP.
2. `res/values/styles.xml` : supprimer les six exemptions temporaires `android:windowOptOutEdgeToEdgeEnforcement` de P15, car Android 16 les **ignore** à targetSdk 36. Voir : https://developer.android.com/about/versions/16/behavior-changes-16
3. `EdgeToEdgeInsets.java` nouveau : à partir d'Android 15, injecter un inset prudent dans le conteneur d'écran pour éviter que les contrôles ne soient masqués par les barres système et les encoches. Les insets sont recalculés sans cumuler à chaque rotation/redimensionnement. Aucun changement sur Android 14 ni moins.
4. `BaseActivity` et `SubActivity` : appel de cette aide après chargement des écrans standards. Les écrans sur fond transparent, superpositions et vues WebHeads qui ne suivent pas ces bases ne sont **pas prétendus entièrement couverts**; contrôle sur appareil nécessaire.
5. `AndroidManifest.xml` : `android:enableOnBackInvokedCallback="false"` temporaire à l'échelle application pour garder les anciens `onBackPressed` fonctionnels sous Android 16. Cette mesure bloque uniquement l'animation prédictive; une migration complète devrait être une décision ultérieure.
6. Le Changelog corrigé de P15 est hérité **sans modification**. Persistance SQL, WebHeads, Masquer ≠ Fermer, navigateur sélectionné, liste WEBSITES, thèmes clair/AMOLED, notifications et réglages sont préservés dans le code.
7. Workflow `.github/workflows/lynket-v3-targetsdk-p16.yml` dérivé de P15 : compilations Android API 36, contrôles Android 11–14 et 16, scan Changelog, trois tests Robolectric, `aapt` targetSdk 36/package/version, `apksigner` v2 et **APK direct sans ZIP** via `upload-artifact@v7` / `archive:false`.

## Risques résiduels obligatoires
- À targetSdk 36 sur Android 16, les restrictions de rotation/redimensionnement peuvent être ignorées sur les tablettes (largeur minimale >= 600 dp). P16 n'impose pas d'opt-out temporaire de l'adaptabilité, pour permettre le test réel portrait/paysage, mais cela implique qu'**une compilation réussie ne garantit pas l'UI**. Voir : https://developer.android.com/develop/adaptive-apps/guides/app-orientation-aspect-ratio-resizability
- Inset central sur les écrans standards : efficace comme garde-fou, pas une migration complète de chaque barre d'app et des écrans translucides. Tester accueil, paramètres, recherche, À propos/Changelog, menus, bulles et WebView, en portrait/paysage, avec navigation gestuelle et trois boutons. Les vieux layouts peuvent encore nécessiter des corrections précises.
- P15 target35 sur Android16 peut être testé indépendamment sans valider la conformité target36. Résultats des tests P15 à prendre en compte avant validation P16.
- APK Debug GitHub : clé temporaire, ne pas désinstaller sans sauvegarde des bulles.
- **P16 n'est validée que sur confirmation explicite de l'utilisateur.** Aucune fusion dans `main` sans ordre distinct.

## CI P16
**GitHub Actions : tous les contrôles réussis**, [run `41210296330`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41210296330), job `124563742505`, état `completed / success`, **19 étapes réussies**.
- Commit exact compilé : `95c6a4adad1dce24a5d4921c2ad5ef37c098249f`. Le commit précédent `caeba8485267547e35690a0d5dde9a7278eb1754` a été remplacé par un correctif de syntaxe du manifeste; **seul le dernier run vert est une candidate de livraison**. Le premier run P16 `41210269465` a été annulé par la concurrence des workflows et ne doit pas être utilisé.
- `:app:assembleDebug` : réussite avec Kotlin 2.2.21, AGP 8.10.1, Gradle 8.11.1, `targetSdkVersion 36`, `compileSdk 36`, `minSdk 16`.
- Vérifications statiques Android 11–14, Android 16 (`targetSdk 36`, pas d'opt-out edge-to-edge, Android 16 Retour préservé temporairement, insets système+encoches et non-cumul, compatibilité WebHeads FGS) : **réussies**.
- Contrôle Changelog hérité de P15 : **réussi**, `loadDataWithBaseURL` après le callback, analyse des sources Java/Kotlin sans appel direct `.loadData`.
- Trois tests Robolectric `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` : **réussis**.
- `aapt` : package Debug `com.jk8026.lynket2027.debug`, versionCode **62**, versionName **2.1.7 - dev**, `targetSdkVersion:'36'` et manifeste : **réussis**. `apksigner` : signature APK scheme v2 vérifiée.
- **APK direct sans ZIP** : [Lynket_2027_v2.1.7_TargetSdk_P16_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41210296330/artifacts/13046371425), artefact `13046371425`, **8 691 915 octets**, `actions/upload-artifact@v7` `archive:false`.
- SHA-256 de l'artefact **rapporté par GitHub** (non recalculé à partir du fichier APK) : `14bc92ea28ebae19ceea86c98fb526f7412dd3d4fc791fa0e1efa6de15f33711`. Expiration affichée : **29 janvier 2027 à 14:28:41 UTC**.
- Cette mise à jour documentaire est passive; workflow ignore `docs/**`, donc le code testé ne change pas.
- **Non validé sur tablette Android 16** : la CI n'exécute ni interface réelle, ni émulateur/tablette Android 16. La migration d'insets est un garde-fou minimal susceptible de devoir être adaptée par écran après les tests réels; P16 reste une **candidate** et ne doit pas être publiée comme version validée.

