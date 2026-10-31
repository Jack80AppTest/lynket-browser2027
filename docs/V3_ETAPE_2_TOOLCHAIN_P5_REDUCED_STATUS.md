# Lynket Custom 2027 v3 — Étape 2, palier P5 réduit : état technique au 30 octobre 2026 (Québec)

**Statut :** corrections de compilation réussies; tests automatisés NON validés; aucun APK P5 délivré; validation sur téléphone en attente.

## Base et isolement
- Base P4 validée sur Galaxy S23 Ultra par l'utilisateur : `dev/3.0.0-toolchain-p4`, commit `82458d9d6fadccae45b79ebed5077ea878ad38cd`.
- Branche isolée de travail : `dev/3.0.0-toolchain-p5-reduced`.
- Branche P5 maximal : `dev/3.0.0-toolchain-p5`, non retenue actuellement, car la migration Kotlin 2.2 exige l'élimination de nombreuses Kotlin Android Extensions.
- `main`, `dev/3.0.0`, branches stables, tag et Release 2.1.7 à préserver. **Aucun merge autorisé sans accord explicite.**

## Versions du P5 réduit
- Gradle wrapper 8.0.2; Android Gradle Plugin 8.0.2; Java 17 pour CI; Kotlin 1.7.22.
- compileSdk 33; targetSdk 28; minSdk 16 conservés.
- versionName 2.1.7; versionCode 62; identifiant Debug `com.jk8026.lynket2027.debug` conservé.
- `kotlin-android-extensions` conservé dans cette variante; migration View Binding reportée à un chantier distinct.
- Options AGP 8 adaptées : namespace, génération BuildConfig et AIDL, constantes R et classes R transitives.

## Corrections de code Kotlin réalisées
L'exécution [41158485383](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41158485383) échouait sur **20 erreurs de compilation Kotlin dans 8 fichiers**. Corrections dans :
- `util/ActivityLifeCycleCallbackAdapter.kt` : signatures Activity non nulles et Bundle nullable seulement pour onActivityCreated.
- `browsing/backgroundloading/BrowsingBackgroundLoadingStrategy.kt` et `extenstions/LiveDataExtensions.kt` : overrides des événements lifecycle.
- `browsing/menu/MenuDelegate.kt` : vérification des ComponentName éventuellement nuls.
- `browsing/webview/WebViewActivity.kt` : accès stable à l'URL et gestion des URL nulles.
- `extenstions/Context.kt` : vérification du nom de package nul/vide.
- `extenstions/View.kt` : signature Animator non nullable.
- `perapp/PerAppSettingsActivity.kt` : vue de bouton éventuellement absente.

Commits : `1e5af86e72f9a0838a98b87864e05f164c3adc73` puis `9a4d578886b459302f348b74621c8d71247720f1` (correction ciblée `onActivityCreated(Bundle?)`).

## Erreurs suivantes corrigées / état de la CI
1. [Exécution 41159322233](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41159322233) : Kotlin OK; Java échoue sur interface AIDL `com.android.vending.billing.IInAppBillingService`; définition AIDL présente dans le dépôt.
2. Commit `35ac9daaf33958c5654c0cabc1f77d8ce8649eab` : `aidl true` dans `android-app/app/build.gradle`. [Exécution 41159598546](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41159598546) : **`:app:assembleDebug` réussi**, mais **6 tests Robolectric** échouent avec `InaccessibleObjectException` sous Java 17; 2 tests non-Robolectric réussis. La CI bloque donc avant la vérification de signature et la livraison APK.
3. Commit `edbaa98f568062fa4817da68835cbc6d6e7f4fa8` : Robolectric 4.3 → 4.11.1, paramètres JVM `--add-opens` documentés pour Java 17. [Exécution 41159999487](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41159999487) : **assemblage réussi**, mais 6 tests Robolectric échouent avec `IllegalArgumentException` dans `ClassVisitor`; une ancienne règle Gradle forçait encore `org.ow2.asm:asm:7.0`.
4. Commit `3a0cabc637308dede25853a659a82e49a92c00d9` : force ASM 9.5 pour les dépendances. [Exécution 41160419166](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41160419166) : **échec avant compilation**, Maven Central renvoie HTTP **429 Too Many Requests** pour plusieurs dépendances Kotlin 1.7.22, dont le plugin buildSrc. Donc la correction ASM 9.5 n'est **pas encore validée**; le log ne permet pas de conclure à une régression du code.

## Reprise exacte sans improvisation
- **Ne pas relancer immédiatement en boucle** tant que Maven Central applique des réponses 429. Reprendre la CI GitHub Actions P5 réduit lorsque le service répond de nouveau.
- Si compilation et tests passent, vérifier `applicationId`, version, signature Debug et obtenir l'artefact APK **direct** sans ZIP via le workflow établi.
- Tester ensuite manuellement sur téléphone : partage Custom Tabs, WebHeads, ordre/fermeture, Masquer/Restaurer, restauration après relance, menus et thèmes. **Ne pas déclarer P5 validé sur téléphone avant le résultat explicite de l'utilisateur.**
- Si les tests Robolectric échouent encore, diagnostiquer leur stack trace avant de toucher aux classes WebHeads ou de contourner les tests; ne jamais ignorer le test pour faire passer la CI.
- Aucun changement de format des sessions ou des règles de suppression des bulles n'a été demandé ou implémenté dans ce palier.
