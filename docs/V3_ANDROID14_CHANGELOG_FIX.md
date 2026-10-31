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
À compléter après exécution et vérification de la compilation spécifique à cette correction. Ne pas livrer le précédent APK (run `41206713690`) comme correction.
