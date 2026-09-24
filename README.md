# GenEvent (Interface "IN STAGE")

GenEvent est une application de bureau conçue en Java et JavaFX pour la **gestion complète et l'optimisation financière de concerts et d'événements musicaux**.

L'application accompagne les organisateurs d'événements de la création du concert jusqu'à l'optimisation de la billetterie, en passant par la gestion logistique et matérielle.

## 🚀 Fonctionnalités Principales

* **Gestion des Concerts** : Création, modification et suppression d'événements. Exportation des dates au format agenda standard (`.ics`).
* **Recherche de Salles** : Catalogue de salles avec filtres (capacité, tarif, disponibilité aux dates souhaitées) et visualisation interactive sur une carte web.
* **Logistique & Staff** :
    * Gestion du **matériel** technique (Son, Lumière, etc.) et de ses coûts fixes.
    * Création d'**équipes** de staff et assignation du personnel.
* **Ventes Annexes (Buvettes & Merch)** : Gestion des points de vente, création de produits personnalisés, et calcul d'une marge de vente fixe (20%). Prise en charge des **Cartes Cashless / Pass VIP**.
* **Simulateur Financier (Dashboard)** :
    * Calcul automatique des dépenses totales (frais fixes + coûts d'achat des stocks).
    * Simulation du **seuil de rentabilité** (nombre de billets à vendre pour rembourser les frais, en déduisant les gains de la buvette/merch).
    * Simulation du **bénéfice maximal** avec la salle pleine.

---

## 🛠️ Installation et Lancement

### Prérequis
* **JDK 17 ou supérieur** (L'application utilise Java et les modules JavaFX).
* Un IDE comme [IntelliJ IDEA](https://www.jetbrains.com/fr-fr/idea/) configuré pour exécuter des projets JavaFX.

### Comment tester l'application rapidement ?
Pour faciliter la découverte de l'application, des données de test sont générées automatiquement au premier lancement :
1. Lancez la classe principale `Main.java` située dans `src/fr/uga/iut2/genevent/Main.java`.
2. Sur la page de connexion, utilisez le compte de test pré-généré :
    * **Email :** `test@test.com`
    * **Mot de passe :** `1234`
3. Vous aurez accès à un tableau de bord contenant déjà des concerts pré-remplis (Rock Festival, Electro Night) avec leurs salles, équipes, matériel et tableaux financiers complets !

---

## 📁 Organisation du dépôt

Le code fourni est distribué comme un module Java, regroupé dans le *package* racine `fr.uga.iut2.genevent`.
Les fichiers sources sont dans le dossier `src`, et le dossier `persistence` est l'endroit où l'application stocke les données locales.

```console
$ tree -a -d -L 1 -I .git --gitignore --noreport -- genevent
genevent
├── doc
├── .idea
├── persistence
└── src
```

---

## 💾 Persistence des données

L'application ne nécessite pas de base de données externe. Un mécanisme de sérialisation binaire (classe `genevent.util.Persisteur`) est utilisé pour sauvegarder l'état de l'application localement.

* Les données sont stockées dans le fichier `persistence/genevent.bdd`.
* Ce fichier est mis à jour à chaque fermeture propre de l'application.
* Pour qu'un objet puisse être sauvegardé, sa classe doit implémenter l'interface `java.io.Serializable`.

> [!warning] ATTENTION (Pour les développeurs)
> À chaque fois que vous ajoutez/supprimez des attributs ou des classes du modèle, vous devez supprimer le fichier `genevent.bdd` situé dans le dossier `persistence/` pour éviter un plantage (`InvalidClassException`). Alternativement, vous pouvez gérer manuellement l'attribut `serialVersionUID`.

---

## 🏗️ Architecture et Code Source

Le projet suit rigoureusement le patron de conception **MVC (Modèle, Vue, Contrôleur)**.
Les composants sont séparés dans les *packages* respectifs `genevent.modele`, `genevent.vue` et `genevent.controleur`.

![Diagramme de classe détaillé](doc/imgs/diagramme-classe_détaillé.svg)

Dans cette implémentation, le **Contrôleur** est l'objet central qui pilote l'exécution de l'application et fait le lien entre les données (`GenEvent`) et l'interface (`IHM`).

![Diagramme de séquence général](doc/imgs/diagramme-séquence_général.svg)
![Diagramme de séquence «création d'un évènement»](doc/imgs/diagramme-séquence_création-évènement.svg)

### IHM et JavaFX

La classe `IHM` est une interface abstraite. L'implémentation graphique actuelle est `JavaFXGUI`.

> [!warning] ATTENTION
> Afin de permettre au contrôleur de piloter l'exécution, la classe `JavaFXGUI` n'est pas une spécialisation directe de `javafx.application.Application`. Le contrôleur démarre l'interface manuellement via `JavaFXGUI#demarrerInteraction()`.

Les vues sont conçues au format **FXML** (ex: `couts-view.fxml`, `dashboard-view.fxml`).
Ces vues ne définissent *pas* leur contrôleur en dur dans le fichier XML. Le contrôleur graphique (souvent `JavaFXGUI` lui-même) est injecté dynamiquement à l'exécution, ce qui permet à la vue d'appeler directement les méthodes de l'application :

```java
FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
mainViewLoader.setController(this);  // Injection du contrôleur à l'exécution
Scene mainScene = new Scene(mainViewLoader.load());
```

