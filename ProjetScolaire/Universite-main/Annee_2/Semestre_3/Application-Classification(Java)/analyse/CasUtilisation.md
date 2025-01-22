# Système : Outil de chargement et d'affichage de données (Jalon n°1)

## Cas d'utilisation 1 : Charger les données

- **Acteur principal :** Utilisateur
- **Déclencheur :** 
- **Préconditions :** L'application est ouverte et prête à recevoir des fichiers.

### Garanties
- **Garanties en cas de succès :** Les données sont correctement chargées et prêtes à être visualisées.
- **Garanties minimales :** L'utilisateur est renvoyé vers l'application principale sans aucune perte de données. (Dans le cas ou il avait charger des données avant).

### Scénario nominal
1. L'utilisateur appuie sur le bouton "File Explorer".
2. L'utilisateur choisit un fichier à charger.
3. Le système charge le fichier choisi.

### Scénarios alternatifs
- **Scénario 1 :** Si le fichier sélectionné n'est pas un CSV valide, le système affiche un message d'erreur et ramène l'utilisateur à l'application principale.

---

## Cas d'utilisation 2 : Classifier une nouvelle donnée

- **Acteur principal :** Utilisateur
- **Déclencheur :** 
- **Préconditions :** Des données doivent avoir été préalablement chargées et modélisées.

### Garanties
- **Garanties en cas de succès :** La nouvelle donnée est ajoutée au nuage de points avec sa catégorie "prédite".
- **Garanties minimales :** Si la classification échoue, l'état du système reste inchangé et aucun point n'est ajouté au nuage.

### Scénario nominal
1. L’utilisateur visualise un graphique.
2. L'utilisateur choisit les axes (abscisse et ordonnée) à l'aide de deux listes déroulantes. Les attributs sélectionnés ne doivent pas être identiques.
3. L'utilisateur sélectionne la fonctionnalité "Classifier une nouvelle donnée".
4. Le système affiche un formulaire de saisie pour la nouvelle donnée, demandant les attributs nécessaires.   
Petite idée: Les champs obligatoires correspondent aux attributs sélectionnés pour les axes, tandis que les autres champs sont facultatifs.
5. L'utilisateur renseigne les attributs.
6. L'utilisateur valide la saisie. (Le système gère les erreurs en cas de saisie invalide.)
7. Le système changent de couleur les points ajoutés en fonction de leur catégorie, avec une couleur distincte pour chaque catégorie.
8. L'utilisateur clique sur un point ajoutée, un petit text affiche sa catégorie "prédite" et ces valeurs(x et y).

### Scénarios alternatifs
- **Scénario 1 :** Si l'utilisateur ne renseigne pas tous les attributs requis, le système affiche un message d'erreur et invite à compléter les informations ou à retourner à l’application principale.

---

Diagramme des classes : 

thermo
├── documentation
├── main.java
├── fr
│   └── univlille
│       └── iut
│           └── r304
│               |
│               ├── model
│               │   ├── DataItem.java
│               │   └── Model.java
│               ├── view
│               │   └── ScatterView.java
│               │   
│               ├── utils
│               |    ├── ConnectableProperty.java
│               |    ├── Observable.java
│               |    ├── ObservableProperty.java
│               |    └── Observer.java
                └── Main.java
Recuperer 