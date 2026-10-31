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
**CI P15 terminée avec succès** — https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41204588299 (`completed / success`, job `124546974411`).
- Commit EXACT du code / workflow compilé : **`4481625fde21be4b7bee17f0de78a08ff26c2331`**. Le HEAD suivant, si documentaire seulement, n'est pas un nouveau code applicatif.
- `:app:assembleDebug` **réussi**, Kotlin 2.2.21 / Gradle 8.11.1 / AGP 8.10.1; `targetSdk 35`, `compileSdk 36` et `minSdk 16`.
- Contrôles Android 11 (visibilité des applications), Android 12 (`PendingIntent`), Android 13 (notification), Android 14 (FGS `specialUse` et receiver), Android 15 (retard de démarrage FGS, non-persistance incognito, opt-out visuel temporaire) : **tous réussis**.
- Tests Robolectric : **`BubbleSessionStoreTest`**, **`SavedBubblesCardModelTest`**, **`SettingsGroupAdapterTest`** — les trois réussis, aucun test désactivé.
- Contrôles `aapt` : **package `com.jk8026.lynket2027.debug`**, versionName `2.1.7` (suffixe de développement), versionCode **62**, `targetSdkVersion 35`, manifeste empaqueté; signature **APK Signature Scheme v2 vérifiée** par `apksigner`.
- **APK P15 direct, sans ZIP** : [Lynket_2027_v2.1.7_TargetSdk_P15_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41204588299/artifacts/13044535734), artefact GitHub Actions ID `13044535734`, taille **8 691 771 octets**. `archive: false` dans le workflow P15.
- Hash de l'artefact publié par GitHub : `sha256:c33fc7294b177371a9bed11af1a648397555b1381a97f0809473b8459cf272fd` (**métadonnée de GitHub**, non vérifiée localement sur le fichier). `expired: false` au 31 octobre 2026; expiration indiquée **29 janvier 2027 à 13:13:24 UTC**.
- **Compilation CI ≠ validation sur appareil. P15 reste candidate tant que l'utilisateur ne l'a pas testée sur son Galaxy S23 Ultra.** Les particularités du comportement Android 15 nécessitent en plus un test sur Android 15; aucune exécution de l'APK sur un émulateur Android 15 n'a été effectuée par ce workflow.

**P15 ne devient validée que sur confirmation explicite après tests utilisateur sur Galaxy S23 Ultra.**
Vérifier partage des liens, fournisseurs Chrome/Samsung, bulles/compteur/ordre, MASQUER et RESTAURER automatiques (sans doublon), fermeture explicite d'une bulle, relance, boot, notifications, options par application, recherche, historique et thèmes. Les changements de comportement propres à Android 15 nécessitent également un vrai test Android 15 (appareil ou émulateur), non couvert par une simple compilation.
Le rapport est passif, ignoré par la CI via `paths-ignore: docs/**`, et ne doit pas déclencher un build.


## Correctif Changelog reporté à P15 — 31 octobre 2026
- Autorisation expresse de reporter le correctif 3.0.0 Android 14 Developer à P15, avant reprise de P16.
- **Aucun changement de cible/version ni de fonctionnement des WebHeads** : `targetSdkVersion 35`, `compileSdk 36`, `versionCode 62`, `versionName 2.1.7` conservés.
- Correctif de `android-app/app/src/main/java/arun/com/chromer/about/changelog/Changelog.java` repris **à l'identique** depuis la branche Developer 3.0.0 corrigée : `WebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)` et `WebViewClient` enregistré **avant** le chargement HTML. La source P9/P10 était identique; son comportement a divergé au passage target 28 → 29, probablement à cause des `#` non échappés du CSS.
- Le workflow P15 ajoute le même contrôle statique parcourant tous les Java/Kotlin de production pour détecter des appels directs `.loadData(`; aucun appel direct restant, contrôle réussi.
- **CI définitive verte :** [run `41209946048`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41209946048), job terminé `completed/success`, **19 étapes réussies** : build, contrôles Android 11–15, audit Changelog, trois tests Robolectric, `aapt` targetSdk 35/package/version et `apksigner` v2.
- **Commit exact compilé :** `41470514079c48fab2481275ec9ed4047207fd73`. Mise à jour documentaire ultérieure seulement, hors code compilé.
- **APK direct corrigé, sans ZIP :** [Lynket_2027_v2.1.7_TargetSdk_P15_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41209946048/artifacts/13045922510), artefact ID `13045922510`, **8 691 558 octets**.
- SHA-256 fourni dans les métadonnées GitHub de l'artefact (non recalculé directement depuis le fichier) : `b69355a3b29aac796a07800a86b547c96c283748f54e0971f6575b2f3cc050ed`; expiration indiquée **2027-01-29 14:23:58 UTC**.
- L'APK P15 original du run `41204588299` **ne contient pas** le correctif. Tester uniquement le nouvel APK.
- **P15 en attente de validation manuelle** sur tablette Android 16. Un APK ciblant API 35 sur Android 16 peut tourner, mais cela ne valide pas à lui seul les changements propres à une cible API 36.
- Aucun merge dans `main`, et P14 / version Developer 3.0.0 restent intacts.


## Correctif P15 — recherche masquée par la barre système (tablette) et chevauchement paysage (téléphone)

### Retours utilisateur et diagnostic
- Captures Galaxy S23 Ultra : en portrait la recherche reste visible, mais en paysage elle peut recouvrir la carte « Bulles sauvegardées » et les boutons MASQUER / RESTAURER. Le défilement est déjà en bas.
- Captures Galaxy Tab A11+ Android 16 (clair / AMOLED, portrait / paysage) : recherche invisible ou partiellement visible **sous la barre système / barre des tâches du bas**. Le contenu ne permet pas de la dégager par défilement.
- La cause probable dans `activity_main.xml` est un `MaterialSearchView` épinglé au bas de son `ConstraintLayout` tandis que `homeFeedRecyclerView` est également contraint jusqu'au bas du parent, sans réserver la hauteur de la recherche; une marge négative `-8dp` du conteneur peut accentuer le débordement. Sur Android 15/16, les barres système peuvent masquer les vues lorsque les insets ne sont pas traités. Référence officielle : https://developer.android.com/develop/ui/views/layout/edge-to-edge
- **Correctif P15 uniquement**, explicitement demandé avant tout changement P16 : aucune modernisation P16 n'est intégrée dans cette branche.

### Modifications minimales
1. `activity_main.xml` : le `homeFeedRecyclerView` s'arrête à `@id/materialSearchView` au lieu de `parent` pour que les cartes restent défilables au-dessus de la barre de recherche; marge négative du conteneur CardView remise à `0dp`. La recherche garde son ancrage inférieur `8dp`.
2. `HomeActivity.kt` : exclusivement sur Android 15+ (SDK >= 35), appliquer au conteneur racine les insets système (statut, navigation / taskbar, découpe d'écran). Le calcul part des paddings d'origine et ne s'accumule pas après rotation/changement d'écran; **Android 14 et moins n'ont pas ce nouvel ajustement**.
3. `.github/workflows/lynket-v3-targetsdk-p15.yml` : nouveau contrôle XML/Kotlin statique pour le positionnement, l'espace de défilement et les insets, tout en conservant les contrôles Android 11–15, l'audit Changelog, les trois tests Robolectric, identité/signature APK et upload direct sans ZIP.
4. Version `2.1.7`, versionCode `62`, targetSdk `35`, minSdk `16`, package Debug, style AMOLED, WebHeads, sauvegarde SQLite et comportement MASQUER ≠ FERMER **inchangés**.

### Validation et portage futur
- Ce correctif de layout n'a pas encore été validé visuellement par l'utilisateur : **ne pas appeler P15 validée tant que les captures sur téléphone/tablette ne sont pas conformes**. Vérifier les deux orientations, thèmes clair/AMOLED, boutons MASQUER/RESTAURER, focus, clavier et recherche.
- La branche P16 `dev/3.0.0-targetsdk-p16` était déjà créée avant ces retours; **aucune modification sur P16** dans ce correctif. Après validation P15, intégrer les ajustements de façon contrôlée à P16, vérifier la compatibilité avec son traitement global des insets (éviter tout double padding), puis reconstruire P16. Ne pas utiliser son APK précédent comme preuve du correctif P15.
- Une CI verte vérifie le code et des assertions statiques, **pas l'affichage réel sous One UI/Android 16**.

### CI du correctif positionnement
**GitHub Actions P15 — résultat définitif vert** : [run `41216859339`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41216859339), statut `completed/success`, **20 étapes réussies**.
- **Commit exact compilé** : `275cdfa356f697df34de4672cd66433f760d9882`. Toute mise à jour documentaire suivante ne modifie pas le code testé.
- `:app:assembleDebug` : **réussi**. Tous les contrôles Android 11/12/13/14/15, le scan Changelog et la nouvelle vérification XML/Kotlin du layout recherche/insets tablette : **réussis**.
- Trois tests Robolectric : `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` **réussis**. Package Debug `com.jk8026.lynket2027.debug`, `targetSdk 35`, `versionCode 62`, `versionName 2.1.7 - dev`, et signature APK v2 vérifiés par `aapt` / `apksigner`.
- **Nouvel APK direct sans ZIP** : [Lynket_2027_v2.1.7_TargetSdk_P15_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41216859339/artifacts/13048307165), ID `13048307165`, **8 693 045 octets**, `archive:false`.
- Empreinte SHA-256 d'artefact fournie par GitHub (non recalculée localement depuis le fichier APK) : `3c22df6772e963499a26629b490027b832817bc77f81eb40f090603f2138537a`. Date d'expiration indiquée **2027-01-29 15:51:47 UTC**.
- Les APK P15 précédents (runs `41204588299` et `41209946048`) **n'incluent pas la correction recherche/tablette**. Tester le nouvel APK uniquement.
- **Validation physique en attente** sur Galaxy S23 Ultra (paysage/portrait) et Galaxy Tab A11+ Android 16 (paysage/portrait, thèmes clair et AMOLED). La réussite des assertions statiques ne garantit pas que One UI réserve correctement l'espace de sa barre des tâches ni que le clavier reste accessible.
- P16 reste **gelée sur son commit antérieur** et ses insets existants; après confirmation P15 seulement, analyser le portage en évitant de cumuler les paddings système.



## P15 — correctif ciblé Introduction + Historique + Onglets (GO utilisateur explicite)

### Constat visuel et périmètre
L'utilisateur a confirmé que la recherche de l'accueil P15 est maintenant **corrigée sur téléphone et tablette**. Il a ensuite constaté que les boutons de l'**introduction initiale** (Passer/Suivant/Terminer), et les boutons flottants d'**Historique** et d'**Onglets** sont cachés ou chevauchés par la barre des tâches Android 16 sur la tablette. Les captures montrent que les barres inférieures du **navigateur WebView** et du **mode Article** fonctionnent : elles ne sont **pas modifiées**.

### Adaptations limitées P15
1. Nouvelle classe `TargetedSystemBarInsets.java`, utilisée seulement pour ces **trois activités**. Elle ne s'applique qu'à Android API >= 35, force explicitement un seul contrat d'affichage bord à bord avec `WindowCompat.setDecorFitsSystemWindows(window,false)`, puis ajoute aux paddings d'origine les `WindowInsets.Type.systemBars() | displayCutout()`. Le calcul à partir des valeurs d'origine évite le cumul à chaque rotation, ouverture/fermeture de taskbar ou redimensionnement.
2. `ChromerIntroActivity.kt` : appliquer cette protection au conteneur racine `android.R.id.content` qui englobe la bibliothèque AppIntro et ses commandes, plutôt qu'aux fragments des diapositives uniquement. **Aucune modification de la bibliothèque, du nombre de diapositives ni de la logique Passer / Suivant / Terminer**.
3. `HistoryActivity.kt` et `TabsActivity.kt` : appliquer la protection à leur `CoordinatorLayout` qui contient la toolbar, la liste et le FAB, sans changer les dimensions du bouton ni son action.
4. Étape statique GitHub Actions dédiée : vérifier les trois utilisations explicites, la présence des commandes d'introduction, l'ancrage des FAB, la garde API >= 35, la gestion des barres et découpes, ainsi que **l'absence du nouveau mécanisme dans HomeActivity/WebView/Article**. Tous les contrôles hérités, dont Changelog, recherche, trois tests Robolectric, signature v2, APK direct sans ZIP, restent actifs.

**Branches protégées :** P14, branche Developer 3.0.0 Android 14, P16 et `main` ne sont pas modifiées. `targetSdkVersion 35`, `compileSdk 36`, `minSdk 16`, versionName `2.1.7`, versionCode `62` et package Debug inchangés. Aucun changement à SQLite, WebHeads, restauration, masquage et fermeture des bulles.

### Risques et validation physique
Le traitement fait passer ces seules trois vues sous un contrat explicite edge-to-edge et applique la zone sûre avant de rendre les commandes accessibles. Il reste à **vérifier physiquement** sur la Galaxy Tab A11+ Android 16 le haut et le bas de chaque écran (portrait, paysage et taskbar), l'usage de Passer/Suivant/Terminer, l'effacement de l'historique, la fermeture d'onglets ainsi que la navigation et le mode Article. Sur Galaxy S23 Ultra Android 14, la garde API protège la disposition précédente. Une compilation CI verte ne suffit pas à valider visuellement.

### CI et artefact de ce correctif
**GitHub Actions P15 corrigée : réussite complète**, [run `41219490319`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41219490319), job `124590872764`, statut `completed/success`, **21 étapes réussies**.
- Commit **applicatif exact compilé** : `90d5bf5f01a521fbee063611e2e5330f7d63aaf7`. La seule différence avec le commit précédent en échec `da6e6c3a4f0f74f0eb95a137840243d1f5ec8b7f` est le remplacement de `WindowCompat` par l'API native d'Android, l'assertion CI correspondante et la documentation.
- `:app:assembleDebug` : **réussi**. Audits Android 11/12/13/14/15, contrôle Changelog, protection de la recherche de l'accueil et vérification des trois nouvelles zones sûres Introduction / Historique / Onglets : **réussis**.
- Trois tests Robolectric : `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` — **tous réussis**. `aapt` confirme `com.jk8026.lynket2027.debug`, versionName `2.1.7 - dev`, versionCode `62`, `targetSdkVersion 35`; `apksigner` confirme la signature APK v2.
- **APK P15 direct sans ZIP**, à tester : [Lynket_2027_v2.1.7_TargetSdk_P15_Debug.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41219490319/artifacts/13049068743), artefact ID `13049068743`, taille **8 692 678 octets**, `actions/upload-artifact@v7` + `archive:false`.
- SHA256 **métadonnée d'artefact GitHub** (non recalculée depuis les octets téléchargés) : `5dbec0bf9dff8041601bf8567ed679f10d3c26d18e1ed803a95a137b3623694b`. Expiration affichée **2027-01-29 16:23:54 UTC**.
- Les anciens APK P15, y compris celui du run `41216859339`, **ne contiennent pas la correction Introduction + Historique + Onglets**. Le premier run échoué `41219161065` n'a livré aucun APK utilisable.
- Le rapport est mis à jour par commit documentaire ultérieur seulement, ignoré par le workflow `paths-ignore: docs/**`; ce commit ne constitue pas un nouveau build.
- **Validation physique encore requise** sur tablette Android 16 : boutons Passer / Suivant / Terminer, FAB Historique et Onglets, portrait / paysage, thème clair/AMOLED et passage entre écrans. Une CI verte ne confirme pas le résultat visuel.
- **P16 toujours intacte** jusqu'à confirmation utilisateur, ainsi que P14, Developer 3.0.0 Android 14 et `main`.

### Incident CI initial et correction compatibilité
Le premier run du code `da6e6c3a4f0f74f0eb95a137840243d1f5ec8b7f` (GitHub Actions [`41219161065`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41219161065)) a échoué lors de `:app:compileDebugJavaWithJavac` : notre version installée d'AndroidX Core ne fournit pas `WindowCompat.setDecorFitsSystemWindows`. Correction *strictement technique* : appel natif Android `activity.getWindow().setDecorFitsSystemWindows(false)` (API >= 30, exécuté uniquement >= 35), sans mettre à jour les dépendances ni élargir le périmètre. L'assertion CI est alignée sur l'appel natif; seul le dernier run réussi compte pour la livraison.
