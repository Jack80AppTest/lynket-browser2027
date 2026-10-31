# Lynket Custom 2027 — P15, Android 15 target SDK 35

## Base de travail et sécurité
- **P14 validée par l'utilisateur sur Galaxy S23 Ultra** le 31 octobre 2026; branche `dev/3.0.0-targetsdk-p14`, HEAD exact `a73be68711572f51e0fc470b1b62cb20f65027da`.
- P15 sur branche **`dev/3.0.0-targetsdk-p15`**, issue directement de P14 validée. P14, P13 et les branches précédentes, `main`, stables, tag/Release 2.1.7 **inchangés**. Aucun merge ni publication.
- Ce palier est **autorisé par GO P15 explicite** et ne modifie ni l'identité ni la persistance historique des bulles.

## Modifications limitées
1. `android-app/app/build.gradle` : `targetSdkVersion 34 → 35`; `compileSdk 36`, `minSdk 16`, Gradle 8.11.1, AGP 8.10.1, Kotlin 2.2.21, Java 17/11 inchangés. `versionName 2.1.7`, `versionCode 62`, package Debug `com.jk8026.lynket2027.debug` inchangés.
2. `WebHeadsFloatingBubble.kt` : sur Android 15+ uniquement et **uniquement** si `ForegroundServiceStartNotAllowedException` empêche le démarrage du foreground service, enregistrer l'URL ordinaire en SQLite afin qu'elle soit restaurée au prochain lancement permis. Les URL incognito restent éphémères selon la politique P14. Aucune suppression de session à l'échec du service. Les autres exceptions sont propagées comme auparavant.
3. `ServiceManager.java` : si la même exception empêche le démarrage du service facultatif de détection d'applications sous Android 15+, différer ce démarrage au lieu de planter. Préférences par application conservées.
4. `res/values/styles.xml` : option Android 15 **temporaire** `android:windowOptOutEdgeToEdgeEnforcement=true` pour les thèmes racines de l'application, de lancement, d'article, d'image, de dialogue et translucide afin de maintenir l'apparence existante. Cette exemption ne sera pas une solution Android 16/API 36 : migration WindowInsets à traiter dans un palier futur distinct, après décision utilisateur.
5. `.github/workflows/lynket-v3-targetsdk-p15.yml` : workflow dédié, identique à P14 pour les validations Android 11/12/13/14 et les trois tests Robolectric; assertion de cible 35 et contrôles statiques Android 15; signature v2 et dépôt direct de l'APK **sans ZIP**.

## Audit des points non changés
- Le receiver `BubbleBootReceiver` utilise les services `specialUse` P14; ce type n'est pas dans la liste des catégories interdites au lancement `BOOT_COMPLETED` d'Android 15. Android peut tout de même limiter le lancement selon les autres conditions.
- `BubbleSessionRestorer` capture déjà les échecs de démarrage et garde la session sur disque.
- Les `PendingIntent` de notifications, Custom Tabs et barre inférieure sont conservés avec leur mutabilité / immutabilité P12. Les chemins de déclenchement depuis navigateur ou action utilisateur devront être validés sur Android 15 (l'absence de refonte est intentionnelle).
- Aucun changement dans `BubbleSessionStore`, `WebHeadService`, le manifeste, l'ordre de session, l'interface des WebHeads, le menu WEBSITES, thèmes clair/AMOLED ou les préférences. **MASQUER ≠ FERMER**.
- La sauvegarde de l'URL est possible seulement après un échec de lancement signalé au code appelant; une interruption système asynchrone ne peut pas être garantie par ce seul mécanisme.
- Le débogage de signature d'installation/migration Debug reste hors périmètre. Ne jamais désinstaller pour contourner une signature sans sauvegarder les données.

## Références Android officielles
- https://developer.android.com/about/versions/15/behavior-changes-15
- https://developer.android.com/about/versions/16/behavior-changes-16
- https://developer.android.com/develop/background-work/services/fgs/changes

## CI P15 et validation physique
État initial : compilation et contrôles en attente au moment de la création du rapport. Les résultats de CI, commit compilé, run et artefact seront ajoutés après contrôle effectif.
**P15 ne devient validée que sur confirmation explicite après tests utilisateur sur Galaxy S23 Ultra.**
Vérifier partage des liens, fournisseurs Chrome/Samsung, bulles/compteur/ordre, MASQUER et RESTAURER automatiques (sans doublon), fermeture explicite d'une bulle, relance, boot, notifications, options par application, recherche, historique et thèmes. Les changements de comportement propres à Android 15 nécessitent également un vrai test Android 15 (appareil ou émulateur), non couvert par une simple compilation.
Le rapport est passif, ignoré par la CI via `paths-ignore: docs/**`, et ne doit pas déclencher un build.
