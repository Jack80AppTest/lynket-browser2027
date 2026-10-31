# Lynket Custom 2027 — Étape 2, palier P13 (targetSdk API 33 / Android 13)

## Base et autorisation

- **P12 validée explicitement sur le Galaxy S23 Ultra par l'utilisateur** dans la conversation de reprise, le 31 octobre 2026.
- Branche immuable de repli `dev/3.0.0-targetsdk-p12`, HEAD de référence `aa91a92725bc5c06e7467120d15952cbecc87cc4`, code APK P12 testé `62c2d8fa4918409458565fc2ac983b807bce6ea5`, run réussi [41197537619](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41197537619).
- GO explicite pour la prochaine étape : modernisation Android 13, sans changer le workflow (branche isolée, GitHub Actions, APK direct, validation téléphone).
- Nouvelle branche isolée : `dev/3.0.0-targetsdk-p13`, dérivée exactement de la P12 validée; aucun merge, aucune publication.

## Périmètre technique

1. `android-app/app/build.gradle` : `targetSdkVersion 31 → 33` pour Android 13. Android 12L API 32 est traversée sans changement supplémentaire constaté dans ce périmètre.
2. `android-app/app/src/main/AndroidManifest.xml` : ajout de l'unique permission `android.permission.POST_NOTIFICATIONS`, nécessaire pour que l'utilisateur puisse autoriser les notifications visibles de Lynket sous Android 13+.
3. `HomeActivity.kt` : demande système standard une seule fois après ouverture du vrai écran d'accueil, uniquement sous Android 13+, si la permission n'est pas accordée et si l'écran d'introduction n'est pas en cours. La décision est enregistrée dans les préférences locales pour éviter les demandes répétées. Si l'utilisateur refuse, ne pas bloquer Lynket ni toucher aux URL en SQLite; l'affichage de la notification dans le volet peut être supprimé par Android, mais le démarrage des services de premier plan reste possible sous les conditions système habituelles. L'utilisateur peut réactiver les notifications dans les paramètres Android.
4. Workflow GitHub Actions propre à P13 : vérifications héritées (visibilité des applications, exported, mutabilité des PendingIntent) + contrôle API 33 du manifeste et de la demande conditionnelle + trois tests Robolectric + analyse `aapt`/signature APK v2 + artefact APK direct sans ZIP.
5. Ne pas modifier `BubbleSessionStore`, `BubbleSessionRestorer`, les WebHeads, Custom Tabs, les layouts, les menus, les thèmes ou l'identité / signature / version Android. `compileSdk 36`, `minSdk 16`, Kotlin 2.2.21, Gradle 8.11.1, AGP 8.10.1, JVM 17 pour Gradle / Java 11 pour tests, versionName 2.1.7, code 62, Debug `com.jk8026.lynket2027.debug` sont conservés.

## Particularités Android 13 à tester manuellement

- Première ouverture de l'accueil : éventuelle demande **Notifications** sur Android 13+; accepter/refuser est un choix de l'utilisateur; aucun prompt répété prévu. Si les notifications sont refusées, vérifier l'existence de la session sauvegardée, indépendamment de la présence de la notification dans le volet.
- Partage de liens et ouverture Custom Tabs; fournisseurs Chrome, Samsung Internet; actions copier/partager/navigateur secondaire.
- WebHeads, Masquer/Restaurer et compteur, ordre et fermeture explicite, restauration après relance et au reboot dans les limites Android.
- Notifications WebHeads, actions Nouvel onglet/Gérer et app-detection quand l'autorisation est accordée; vérifier le mode de bulles natives si utilisé.
- Mode article et WebView sous thèmes clair/AMOLED (Android 13 adapte automatiquement le schéma de couleurs de contenus WebView compatibles).
- En cas de **restriction batterie** sous Android 13, `BOOT_COMPLETED` peut ne pas être livré tant que l'app n'a pas été relancée : conserver la base SQLite et le retour automatique au prochain lancement/partage.
- Contrôler la mise à jour sans désinstaller et sans perte de données. Un succès `apksigner` n'atteste pas la compatibilité du certificat avec une ancienne installation Debug.

## État

Candidat P13. Ne déclarer une réussite automatisée qu'après le run GitHub Actions correspondant au commit; ne déclarer P13 validée qu'après le test explicite sur le Galaxy S23 Ultra. P12 reste la référence fonctionnelle validée; `main`, `stable/*`, le tag/Release 2.1.7 et les branches précédentes restent inchangés.

Références officielles :
- https://developer.android.com/about/versions/13/behavior-changes-13
- https://developer.android.com/about/versions/13/behavior-changes-all
- https://developer.android.com/develop/ui/views/notifications/notification-permission

Ce rapport est purement documentaire, exclu des étapes CI/CD et ne déclenche aucun build par lui-même.

## CI P13 — succès complet, essai téléphone en attente

- GitHub Actions : [run 41198734017](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41198734017), `completed / success`, aucun échec.
- **Commit de code effectivement compilé** : `029412c0ce9518ab984634f5fb6c715c0563ea72`.
- `:app:assembleDebug` avec Kotlin 2.2.21 / Gradle 8.11.1 / AGP 8.10.1 / `compileSdk 36` / **`targetSdk 33`** : réussi.
- Contrôles hérités Android 11 (visibilité) et Android 12 (`android:exported`, `PendingIntent`) : réussis.
- Contrôle Android 13 (`POST_NOTIFICATIONS` + demande conditionnelle au démarrage de l'accueil) : réussi.
- Tests Robolectric existants `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` : réussis.
- Contrôle APK : `com.jk8026.lynket2027.debug`, version 2.1.7 / code 62, target 33, permission empaquetée et schéma de signature v2 : réussi.
- **Artefact APK direct sans ZIP** : [Lynket_2027_v2.1.7_TargetSdk_P13_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41198734017/artifacts/13041841979), 8 690 366 octets, artifact ID `13041841979`, expiration annoncée le 29 janvier 2027.
- SHA-256 tel que retourné dans les métadonnées GitHub : `ac39a3d76978c005ba73baa6422a2e0e990d733c4c459f031da759229e9110fd` (non recalculé localement).
- Ce rapport sera enregistré dans un commit de documentation uniquement, distinct du commit applicatif compilé, sans déclencher de build (filtre `docs/**`).
- **État : CI verte, P13 PAS ENCORE VALIDÉE sur le Galaxy S23 Ultra.** P12 est la dernière base validée sur téléphone; aucun P14, merge ni publication autorisé sans décision explicite.
