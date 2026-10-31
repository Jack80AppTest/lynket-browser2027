# Lynket Custom 2027 — P14 Android 14 target SDK API 34

## Base et autorisation

- P13 **validée fonctionnelle sur Galaxy S23 Ultra par l'utilisateur** dans cette conversation (31 octobre 2026).
- Branche précédente préservée : `dev/3.0.0-targetsdk-p13` au HEAD documentaire `48b72554df400525a4aae2f361fc5535816bacca`, commit d'application compilé `029412c0ce9518ab984634f5fb6c715c0563ea72`, workflow CI vert [41198734017](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41198734017).
- **GO P14 explicite** donné après présentation du périmètre cible Android 14 (API 34), types FGS, récepteurs dynamiques et tests de navigation.
- Branche isolée : `dev/3.0.0-targetsdk-p14`, démarrée exactement de P13. Aucun merge `main`, ni changement `stable/*` ou release de secours.

## Modifications Android 14

1. `android-app/app/build.gradle` : `targetSdkVersion 33 → 34`; `compileSdkVersion 36`, `minSdkVersion 16`, versionName `2.1.7`, versionCode `62`, applicationId Debug `com.jk8026.lynket2027.debug` **inchangés**. Kotlin 2.2.21, AGP 8.10.1, Gradle 8.11.1 et Java 17/11 conservés.
2. `AndroidManifest.xml` : permission normale `android.permission.FOREGROUND_SERVICE_SPECIAL_USE` et `android:foregroundServiceType="specialUse"` pour les deux vrais services foreground :
    - `WebHeadService` : bulles WebHeads persistantes et superposées, démarrées volontairement;
    - `AppDetectService` : détection optionnelle de l'application active pour les paramètres navigateur par application.
   Les deux déclarent `android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE` avec justification concrète et gardent `android:exported="false"`. Le type `specialUse` correspond à l'absence d'une catégorie standard adaptée; **toute future mise en ligne sur Google Play nécessitera une évaluation et justification par Google**. Ne pas assimiler une CI verte à un accord de publication.
3. `WebHeadService.java` : récepteur dynamique de commandes de notification enregistré avec `Context.RECEIVER_NOT_EXPORTED` sur Android 13+ (API 33+) et méthode historique en deçà; PendingIntent WebHeads restreints au package déjà depuis P12. Le récepteur `AppDetectService` ne traite que les broadcasts système `SCREEN_ON/SCREEN_OFF`, exception documentée à l'obligation de flags : **aucune modification**.
4. Tous les `PendingIntent` mutables nécessaires au navigateur sont déjà **explicites** depuis P12 (`CustomTabs.java`, barre inférieure et bulle native) : préservés. Aucune autorisation de lancement d'activité depuis l'arrière-plan accordée sans besoin prouvé.
5. Nouveau workflow indépendant `.github/workflows/lynket-v3-targetsdk-p14.yml` : `:app:assembleDebug`, validations d'anciennes étapes Android 11-13 et de P14 (deux types FGS, sous-types, permissions et receiver), trois tests Robolectric existants, `aapt` pour le vrai APK SDK 34 et signature APK v2, livraison d'un **APK direct non ZIP**.
6. Aucune modification des données SQLite, des flux Masquer/Restaurer, des onglets, de l'interface, des thèmes, des providers, de `main` ou des releases. **Masquer ≠ Fermer** reste une exigence impérative.

## Vérifications sur Galaxy S23 Ultra

- Ouverture et partage des liens (Chrome/Samsung), Copy/Share/Open in Browser et bouton de la barre inférieure si utilisé.
- WebHeads en superposition : ouverture, compteur, Masquer/Restaurer, fermeture explicite, restauration lors de la relance et après reboot **dans les limites autorisées par Android**.
- Notification WebHeads : fermer l'overlay, Gérer, Nouvel onglet (avec note : le déclenchement d'activités depuis un receiver reste soumis aux restrictions Android, vérifier les deux derniers boutons).
- Détection d'application par application si activée; QS Tiles WebHeads, mode incognito, article.
- Refus d'autorisation des notifications : ne jamais perdre les bulles enregistrées.
- Installation par-dessus P13 et absence de perte de données; **ne pas désinstaller** l'ancienne version pour corriger une éventuelle erreur de signature Debug.

## État initial

P14 candidate à compiler et tester via GitHub Actions. Ne pas présenter comme validée avant les résultats d'automatisation puis les tests explicites de l'utilisateur sur le téléphone. **P13 reste la dernière version validée**.

Références officielles :
- https://developer.android.com/about/versions/14/behavior-changes-14
- https://developer.android.com/about/versions/14/changes/fgs-types-required
- https://developer.android.com/develop/background-work/services/fgs/service-types

Ce rapport est passif; le workflow ignore `docs/**`, aucun script de build ne dépend du document.
