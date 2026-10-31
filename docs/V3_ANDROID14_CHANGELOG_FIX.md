# Lynket Custom 2027 — correction ciblée du Changelog (Android 14 Developer 3.0.0)

## Détection et justification
- Observation utilisateur sur Galaxy S23 Ultra : fenêtre « Changelog » vide/avec barre blanche dans À propos, **P9 fonctionnelle puis régression dès P10**.
- Audit du dépôt : entre `dev/3.0.0-toolchain-p9` (targetSdk 28) et `dev/3.0.0-targetsdk-p10` (targetSdk 29), `Changelog.java`, `changelog.xml`, `AboutFragment.kt` et la vue XML du Changelog sont **strictement identiques** (SHA Git identiques). Le changement de code fonctionnel du palier est `targetSdkVersion 28 → 29`.
- L'appel historique `WebView.loadData(activity.getString(R.string.changelog_text), "text/html", "utf-8")` transmet du HTML brut alors que la ressource contient des séquences CSS `#FF4081`. La documentation Android exige l'encodage du dièse dans `loadData` dès targetSdk 29, sinon le texte après le dièse peut être considéré comme un fragment : https://developer.android.com/reference/android/webkit/WebView.html#loadData(java.lang.String,java.lang.String,java.lang.String).
- Deuxième fragilité préexistante : installation du `WebViewClient` **après** le début du chargement, pouvant manquer `onPageFinished`, qui masque le spinner et rend la WebView visible.
- Diagnostic technique **fortement étayé** par le changement précis de cible et par la documentation, mais **validation visuelle sur téléphone nécessaire**.

## Périmètre autorisé et modifications
- User GO pour **corriger maintenant la version 3.0.0 Android 14 Developer**, tester le résultat, puis **attendre validation explicite avant d'appliquer le même correctif à P15**.
- Branche candidate existante `dev/3.0.0-android14`, avant correction HEAD `d341d95bdef2dd5a1370aefb0627c749421a5408`. La branche est une candidate non validée; sa base P14 validée, P15 et `main` restent intactes.
- `android-app/app/src/main/java/arun/com/chromer/about/changelog/Changelog.java` : remplacer `loadData` par `loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)`, après enregistrement du `WebViewClient`. **Aucun contenu du journal, style, layout ni bouton modifié**.
- CI Developer existante `.github/workflows/lynket-v3-android14-developer.yml` : nouvelle étape statique contrôlant la méthode, l'ordre du callback, la ressource HTML contenant des dièses, et balayant **tous les fichiers Java/Kotlin de production** pour signaler un autre `.loadData(...)` potentiellement dangereux.
- Tous les contrôles historiques Android 11/12/13/14, les trois tests Robolectric, les assertions `targetSdk 34`, versionCode **63**, versionName **3.0.0**, package Debug, signature v2 et le **même nom d'APK direct sans ZIP** restent présents et inchangés.
- Aucune modification à SQLite, persistance des bulles, choix du fournisseur, contenus HTML, autres parties de WebView, caractéristiques UI, version Android, numéro 3.0.0 ou identité de l'app.

## Limites du contrôle
- Le scan signale les appels directs `.loadData(...)` dans les sources Java/Kotlin. Il ne prouve pas l'absence d'autres régressions Android 10–16 non liées à cette API (autres WebView, permission, ciblage SDK, comportement OEM ou bibliothèques tierces).
- Le navigateur WebView principal `browsing/webview/WebViewActivity.kt` charge les URL ordinaires via `loadUrl()`; ce défaut des fragments CSS d'une chaîne HTML ne lui est pas directement applicable.
- Tests automatisés et signature assurent la cohérence technique, **pas un affichage réel**. Il faudra tester À propos → Journal des modifications sur Galaxy S23 Ultra, puis autoriser séparément le portage à P15.

## Résultats GitHub Actions
**GitHub Actions : `completed / success`, 18 étapes réussies**, run [`41208517887`](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41208517887), job `124558442676`. Code et workflow compilés au commit exact `8deeef8a4ca40017b1da13718b212fe40846e0f7`.
- Compilation Gradle `:app:assembleDebug` : **réussie**.
- Contrôles de compatibilité Android 11/12/13/14 : **réussis**.
- **Audit global Java/Kotlin** : étape « Regression check for changelog HTML and unsafe WebView data loads » **réussie**. Le contrôle analyse récursivement `android-app/app/src/main` et échoue si un appel direct `.loadData(...)` demeure dans le code applicatif. Conclusion vérifiable : **aucun autre appel direct `.loadData` détecté** dans le périmètre de ce scan, et séquence `WebViewClient` avant `loadDataWithBaseURL` validée. La ressource du journal contient toujours son HTML et son `#FF4081`.
- `BubbleSessionStoreTest`, `SavedBubblesCardModelTest`, `SettingsGroupAdapterTest` : **réussis**, aucune suppression de test.
- `aapt` / `apksigner` : package `com.jk8026.lynket2027.debug`, versionCode `63`, versionName `3.0.0` avec suffixe Debug `- dev`, targetSdk `34`, signature APK scheme v2 **vérifiés**.
- **Nouvel APK corrigé direct, sans ZIP** : [Lynket_Custom_2027_v3.0.0_Android14_Developer.apk](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41208517887/artifacts/13045632794), artefact `13045632794`, taille **8 690 722 octets**; `actions/upload-artifact@v7` avec `archive: false`.
- SHA-256 fourni par **les métadonnées d'artefact GitHub** (non recalculé localement depuis les octets de l'APK) : `4740f33fcb391958d0ab832370917b64a694134630ed3e81fb91d4309bfafc05`. Expiration de l'artefact indiquée par GitHub : **2027-01-29 14:05:35 UTC**.
- **L'APK antérieur** du run `41206713690` **n'inclut pas ce correctif**. Ne pas utiliser son lien pour tester le journal corrigé.
- Prochaine étape obligatoire : **test visuel** de À propos → Journal des modifications sur Galaxy S23 Ultra Android 14, ainsi que vérification de la conservation des bulles et des réglages. **P15 ne doit pas être corrigée tant que l'utilisateur n'a pas confirmé le bon résultat**.
- Mise à jour documentaire après le build seulement, sur cette branche; aucun code recompilé et aucune CI déclenchée par `docs/**` seul.
