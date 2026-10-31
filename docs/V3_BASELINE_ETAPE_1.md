# Lynket 2027 v3.0 — Étape 1 : état de référence et règles de non-régression

**Date :** 30 octobre 2026  
**Statut :** diagnostic / référence, **aucune modernisation du code Android à cette étape**.  
**Source de vérité de la base fonctionnelle :** Release GitHub `2.1.7`, tag au commit `f11edd0718e758e06686b5540f6d9b38b16143fd`.  
**Branche de développement v3 :** `dev/3.0.0`, créée à partir de ce tag.  
**APK connu comme fonctionnel, validé par l'utilisateur :** `Lynket_2027_v2.1.7_Developer.apk`, [Release 2.1.7](https://github.com/Jack80AppTest/lynket-browser2027/releases/tag/2.1.7).  

## Garanties de départ

- Conserver intégralement la Release et le tag `2.1.7` : ce point de retour n'est pas modifié.
- Ne modifier ni `main`, ni `stable/v2.1.6`, ni `stable/v2.1.7`.
- À l'étape 1, le code applicatif `android-app/`, ses dépendances, son manifeste et ses numéros de version restent identiques au tag `2.1.7`.
- La seule addition technique de l'étape 1 est le workflow indépendant `.github/workflows/lynket-v3-baseline.yml` qui compile l'ancien code avant modernisation. Ce document est purement informatif.
- Aucun merge vers `main` et aucune publication de version 3 avant demande et validation explicites de l'utilisateur.

## Inventaire technique actuel (avant toute modernisation)

| Élément | Référence |
| --- | --- |
| Projet Android | `android-app/` ; modules `:app`, `:disk-cache` |
| Gradle wrapper | `5.6.2` |
| Android Gradle Plugin | `3.5.2` |
| Kotlin | `1.3.50` |
| Java compilation | Java 8 / bytecode 1.8 |
| compileSdk | 29 |
| targetSdk | 28 |
| minSdk | 16 |
| Version de l'application | `2.1.7`, code 62 |
| applicationId Release | `com.jk8026.lynket2027` |
| applicationId Developer | `com.jk8026.lynket2027.debug` |
| WebHeads | Stockage de session persistant + masquage/restauration |
| Thèmes | Clair d'origine, AMOLED noir optionnel |
| Dépendances sensibles | Android Extensions synthétiques Kotlin, KAPT, anciennes bibliothèques AndroidX/RxJava, dépôts JCenter, modules `master-SNAPSHOT` |

**Important :** la présence de `master-SNAPSHOT` et de dépôts anciens peut affecter la reproductibilité future; aucun changement de dépendance n'est autorisé à cette étape.

## Contrôles automatisés de référence

Workflow : [Build baseline v3](https://github.com/Jack80AppTest/lynket-browser2027/actions/workflows/lynket-v3-baseline.yml).  
Première exécution : [#41148488732](https://github.com/Jack80AppTest/lynket-browser2027/actions/runs/41148488732).  
**Ce document ne prétend pas que la compilation a réussi : vérifier le résultat de l'exécution.**

Le workflow vérifie `assembleDebug`, l'identité du paquet, la signature Debug et ces classes de tests existantes :

1. `BubbleSessionStoreTest` : ordre des URL sauvegardées, déduplication, retraits explicites, effacement complet.
2. `SavedBubblesCardModelTest` : disponibilité des boutons Masquer/Restaurer selon les sessions.
3. `SettingsGroupAdapterTest` : contraste des titres dans les thèmes clair et AMOLED.

**Limite :** les tests unitaires et la compilation ne prouvent pas le comportement réel des overlays, du redémarrage du téléphone ou des Custom Tabs. Ces aspects nécessitent des essais physiques.

## Contrat manuel de non-régression (à tester après chaque groupe de changements)

- [ ] Lancement normal, aucune perte de données ni de préférences.
- [ ] Partager un lien depuis une autre application ; ouverture correcte via le fournisseur configuré.
- [ ] Ouvrir plusieurs liens en WebHeads ; compteur et ordre des onglets corrects.
- [ ] Appui sur une bulle et appui prolongé / menu WEBSITES : aucune suppression de session non demandée.
- [ ] Fermeture explicite d'une seule bulle : seule son URL disparaît de la sauvegarde.
- [ ] Masquer les bulles sans fermer les onglets ; les restaurer avec les commandes existantes.
- [ ] Relancer l'application : restauration des URL persistantes.
- [ ] Refaire le test après arrêt du processus ; après redémarrage du téléphone, tenter la restauration sous réserve des restrictions Android.
- [ ] Vérifier le thème clair et AMOLED, le changement immédiat, les paramètres et les deux menus précédemment corrigés.
- [ ] Vérifier les Custom Tabs, le navigateur choisi, l'historique, le mode article et le partage.
- [ ] Vérifier le nom de paquet, la version et la signature ; éviter toute désinstallation qui ferait perdre les sessions.

## Risques identifiés et stratégie

1. **Migration Gradle/AGP/Kotlin :** incompatibilités possibles entre anciennes API Gradle, Kotlin Android Extensions, bibliothèques Kotlin et plugins de génération de code. Faire des changements ciblés et compiler à chaque palier.
2. **Cible Android moderne :** autorisations overlay, services en arrière-plan, notifications, receivers, lancement après démarrage et interface edge-to-edge doivent être étudiés séparément, sans modifier leur comportement silencieusement.
3. **Persistance des URL :** ne jamais considérer masquer une bulle, tuer le processus ou redémarrer le téléphone comme une fermeture explicite d'onglet.
4. **Signature :** les runners GitHub de développement peuvent signer chaque APK avec une clé Debug différente. Un APK Debug nouveau n'est **pas garanti installable par-dessus l'ancien**. Ne jamais désinstaller une version contenant des données importantes sans sauvegarde.
5. **Compatibilité Play Store :** le travail de conformité ne doit pas changer implicitement `applicationId`, les fonctionnalités ni la procédure de publication existante.
6. **Contribution à l'amont :** séparer les adaptations générales aux outils et Android des personnalités/fonctions propres à Lynket 2027 ; aucune Pull Request automatique.

## Validation pour passer à l'étape 2

- Le point de retour `2.1.7` et toutes les branches stables sont intacts.
- Le code Android de `dev/3.0.0` est identique à celui du tag `2.1.7`.
- La compilation de référence et les tests existants réussissent ; sinon résoudre seulement l'environnement de test sans modifier le fonctionnement.
- L'utilisateur autorise explicitement le prochain groupe de modernisation.

**Aucune mise à jour globale de Gradle, Kotlin, targetSdk, des dépendances ou de l'interface ne fait partie de l'étape 1.**
