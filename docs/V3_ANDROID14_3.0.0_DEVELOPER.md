# Lynket Custom 2027 — version 3.0.0 Android 14 (Developer)

## Décision utilisateur et statut
- **GO explicite reçu** pour préparer la version **3.0.0 Android 14 Developer**, sans créer immédiatement de GitHub Release.
- Base **exclusive** : P14 validée en usage réel sur Galaxy S23 Ultra, branche immuable `dev/3.0.0-targetsdk-p14`, commit final `a73be68711572f51e0fc470b1b62cb20f65027da`.
- Nouvelle branche isolée : **`dev/3.0.0-android14`**, créée depuis ce commit exact, sans fusion ni modification de `main`, P14, P15 ou les anciennes Releases 2.1.6/2.1.7.
- Le statut « P14 validée » concerne les fonctions et la cible Android de la P14 d'origine. **La nouvelle version 3.0.0 devra être testée avant de la déclarer validée**, même si seules ses métadonnées changent.

## Différences applicatives autorisées
`android-app/app/build.gradle` :
- `versionName 2.1.7 → 3.0.0`
- `versionCode 62 → 63`
- `targetSdkVersion 34` **conservé**, soit Android 14 (API 34)
- `compileSdkVersion 36` et `minSdkVersion 16` inchangés
- `applicationId com.jk8026.lynket2027` inchangé; APK **Developer/Debug** utilise toujours `com.jk8026.lynket2027.debug` via `applicationIdSuffix '.debug'`.
- suffixe du numéro de version Debug `- dev`, nom d'application Debug « Lynket 2027 (Test) », signature de débogage du runner **inchangés**.
- Outils Gradle 8.11.1, AGP 8.10.1, Kotlin 2.2.21, Java 17 (build) / Java 11 (tests) inchangés.
- Aucune autre ligne applicative ni ressource Android modifiée; persistance SQLite, WebHeads, restauration auto, Masquer ≠ Fermer, menus WEBSITES, choix Custom Tabs, thèmes, historique, réglages et design **strictement inchangés**.

## Workflow Developer dédié
- `.github/workflows/lynket-v3-android14-developer.yml` : dérivé du workflow P14 réussi, mais lancé uniquement sur `dev/3.0.0-android14` ou manuellement; ne se déclenche pas sur les seules modifications de `docs/**`.
- Build Debug `:app:assembleDebug`, contrôles de compatibilité Android 11/12/13/14 et **trois** tests Robolectric réels :
  `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest`.
- Vérification `aapt` du package Debug, `versionCode 63`, `versionName 3.0.0` (suffixe Developer conservé), `targetSdkVersion 34` et du manifeste; vérification signature `apksigner` scheme **v2**.
- Nom de fichier : **`Lynket_Custom_2027_v3.0.0_Android14_Developer.apk`**.
- Distribution **APK direct, sans ZIP** via `actions/upload-artifact@v7`, `archive: false`. Aucune GitHub Release ni tag créé par ce workflow.

## Attention : identité, mises à jour, signature, sauvegardes
- Ce fichier est un **APK Debug/Developer**, pas un APK Release signé avec une clé stable. La signature Debug sur un runner éphémère GitHub **peut changer à chaque build**; une mise à jour en place n'est pas garantie même avec le même package. Ne jamais désinstaller l'application avec ses bulles / préférences avant une stratégie de sauvegarde et signature compatible.
- Il ne remplace ni Lynket original ni l'application Release `com.jk8026.lynket2027`, grâce au package distinct `com.jk8026.lynket2027.debug`.
- `targetSdkVersion 34` décrit la **cible des comportements de compatibilité**, pas une restriction d'installation à Android 14. Le fonctionnement sur Android 15/16 reste à vérifier.
- Aucune publication sur Google Play n'est demandée ni implicitement autorisée.

## Proposition de texte GitHub Release Developer — À publier plus tard uniquement sur décision utilisateur

**Tag proposé :** `3.0.0-android14-dev` (à créer au commit exact de cette branche lors de la publication autorisée).

**Titre :** `Lynket Custom 2027 v3.0.0 — Android 14 Developer`

**Description :**

> **Lynket Custom 2027 v3.0.0 — Android 14 (Developer)**
>
> Cette version Developer est issue directement de la P14, validée sur Galaxy S23 Ultra sous Android 14. Elle conserve le fonctionnement et les fonctionnalités déjà validés, avec une nouvelle numérotation 3.0.0.
>
> **Fonctionnalités incluses**
> - Bulles flottantes WebHeads avec sauvegarde persistante des liens ouverts.
> - Restauration automatique des bulles, y compris après masquage puis relance, dans les limites autorisées par Android.
> - **Masquer n'est pas fermer** : seules les fermetures explicites retirent les bulles sauvegardées.
> - Gestion des bulles, liste WEBSITES, compteur, copie/partage et fermeture individuelle.
> - Choix du navigateur compatible Custom Tabs, historique, réglages et thèmes existants, dont AMOLED.
>
> **Informations techniques**
> - Version : **3.0.0 Developer**
> - Cible Android : **14 (API 34)** ; compatibilité avec d'autres versions à confirmer par tests.
> - APK Debug indépendant : `com.jk8026.lynket2027.debug`.
> - Cette version Developer est destinée aux essais, **pas à une publication Google Play**.
>
> **Installation** : téléchargez l'APK joint. Attention : la signature Debug peut varier entre compilations et empêcher une mise à jour directe. Préservez vos données; évitez une désinstallation avant sauvegarde.
>
> **Statut** : à valider sur appareil avant annonce publique comme version stable.

## Suivi CI et validation matérielle
Résultats finaux de la CI Developer, commit construit, artefact direct et SHA à compléter uniquement après vérification dans GitHub Actions.
P14 est la référence validée; la version 3.0.0 Developer reste **candidate** en attente des tests sur Galaxy S23 Ultra.
