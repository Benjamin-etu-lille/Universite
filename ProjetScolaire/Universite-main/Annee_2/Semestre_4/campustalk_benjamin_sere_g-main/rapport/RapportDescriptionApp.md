# **Présentation Générale de l’Application – CampusTalk**

## **Sommaire**

### 1. [Introduction](#1-introduction)
### 2. [Description du Projet](#2-description-du-projet)
### 3. [Explication de l'implémentation des différentes fonctionnalités](#explication-de-limplémentation-des-différentes-fonctionnalités)
**3.1 [Fonctionnalités Partie 1](#fonctionnalités-partie-1)**
- [Création et authentification d’un compte utilisateur](#création-et-authentification-dun-compte-utilisateur)
- [Gestion des fils de discussion](#gestion-des-fils-de-discussion)
- [Envoi et réception de messages](#envoi-et-réception-de-messages)

**3.2 [Fonctionnalités Partie 2](#fonctionnalités-partie-2)**
- [Interface utilisateur responsive](#interface-utilisateur-responsive)
- [Gestion avancée des fils de discussion](#gestion-avancée-des-fils-de-discussion)
- [Développement d'une API REST](#développement-dune-api-rest)
- [Définition d’une durée de vie des messages](#définition-dune-durée-de-vie-des-messages)
- [Possibilité de poster des images](#possibilité-de-poster-des-images)
- [Gestion des likes sur les messages](#gestion-des-likes-sur-les-messages)

**3.3 [Fonctionnalités Partie 3](#fonctionnalités-partie-3)**
- [Gestion des invitations et notifications](#gestion-des-invitations-et-notifications)
- [Gestion du profil de l'utilisateur avec ses statistiques](#gestion-du-profil-de-lutilisateur-avec-ses-statistiques)
- [Liste des membres du fil](#liste-des-membres-du-fil)
- [Gestion des cookies](#gestion-des-cookies)
### 4. [Conclusion](#4-conclusion)

## **1. Introduction**

Ce rapport présente une vue d’ensemble du projet **CampusTalk**, en détaillant sa conception, son développement et l’implémentation de ses différentes fonctionnalités. Nous suivrons une approche chronologique (Partie) pour expliquer les différentes étapes du projet, en mettant en évidence :  

- **Les choix techniques et les décisions de conception** adoptés à chaque phase.  
- **Les évolutions et modifications** apportées en fonction des contraintes rencontrées.  
- **Les problèmes rencontrés et les solutions** mises en place pour les résoudre.  
- **Les améliorations possibles** à envisager pour optimiser l’application à l’avenir.  

Bonne lecture.


## **2. Description du Projet**

CampusTalk repose sur une architecture MVC et une base de données relationnelle.

### **2.1 Fonctionnalités primitives demandées dans le sujet (Partie 1)**
- Création et authentification d’un compte utilisateur
- Gestion des fils de discussion simple 
- Envoi et réception de messages

### **2.2 Fonctionnalités supplémentaires demandées dans le sujet (Partie 2)**
- Interface utilisateur responsive
- Gestion des fils de discussion avancer 
- Développement d'une API REST
- Définition d’une durée de vie des messages
- Possibilité de poster des images
- Gestion des likes sur les messages

### **2.3 Fonctionnalités ajoutées (Partie 3)**
- Gestion des invitations
- Affichage du profil de l'utilisateur avec ses statistiques
- Liste des membres du fil
- Gestion des notifications
- Gestion des cookies

## **3. Explication de l'implémentation des différentes fonctionalitées**

### **3.1 Fonctionnalités Partie 1**  

#### Création et authentification d’un compte utilisateur

La première étape du développement de **CampusTalk** a été la gestion de l’authentification des utilisateurs. Nous avons considéré que cette fonctionnalité était essentielle pour garantir une base solide avant d’implémenter le reste des fonctionnalités.  

Le principe est le suivant :  

1. **Page d’accueil sans authentification obligatoire**  
   - Une page d’accueil a été conçue pour présenter le projet.  
   - Elle permet de tester si le contrôleur fonctionne correctement et si l’application est accessible avant d’implémenter l’authentification stricte.  

2. **Mise en place des pages JSP d’inscription et de connexion**  
   - Deux pages JSP ont été développées : l’une pour l’inscription, l’autre pour la connexion.  
   - Deux méthodes correspondantes, `sInscrire` et `seConnecter`, ont été implémentées dans le contrôleur.  
   - Ces méthodes vérifient les identifiants fournis et créent une session utilisateur.  

3. **Gestion de l’attribut de session `pseudo`**  
   - Lorsqu’un utilisateur se connecte, un attribut de session `pseudo` est défini.  
   - Cet attribut est utilisé dans chaque page JSP pour vérifier si l’utilisateur est connecté ou non.  
   - Exemple de code utilisé en haut des JSP pour rediriger les utilisateurs non connectés :  

   ```java 
   // Vérification de la session en haut de chaque page JSP
   String pseudo = (String) session.getAttribute("pseudo");
   if (pseudo == null) {
       response.sendRedirect("Controleur?action=seConnecter");
       return;
   }
   ```  

4. **Durée de vie de la session**  
   - La session est créée par défaut si elle n’existe pas.  
   - Sa durée de vie est celle définie par le serveur d’application.


#### Gestion des fils de discussion : 

Nous avons décidé d'implémenter la sélection d'un fil avant l'envoi des messages. Nous sommes partis du principe que si nous parvenons à identifier quel fil de discussion est sélectionné, alors nous pourrons facilement afficher les messages correspondants, car la table des messages contient une clé étrangère `fno` (clé primaire d'un fil de discussion).

1. **La création d'un fil** :  
   Qui dit nouvelle fonctionnalité, dit nouvelle méthode dans le contrôleur. La méthode `creerFil` utilise le `FilDAO` pour créer et sauvegarder un fil à partir des attributs de la requête.  
   C'est en écrivant cette méthode que je me suis rendu compte qu'il serait judicieux de chaîner les constructeurs des DTO, car beaucoup d'informations lors de la création d'un fil sont mises à `null` car elles sont gérées côté base de données. Pour éviter les erreurs, notamment avec la clé primaire qui est gérée par la base de données avec `SERIAL`, j'ai décidé de chaîner les constructeurs au minimum.

```java
public Fil(int fno, String titre, String description, LocalDateTime dateCreation) {
    this.fno = fno;
    this.titre = titre;
    this.description = description;
    this.dateCreation = dateCreation;
}

public Fil(String titre, String description) {
    this(0, titre, description, null);
}
```

2. **Sélection du fil et chargement du fil** :  
   Une fois la création de fil fonctionnelle, il faut permettre à l'utilisateur de sélectionner le fil qu'il vient de créer.  
   Il faut donc afficher les fils auxquels l'utilisateur est abonné. Étant donné que la table `abonnement` contient une clé étrangère `fno`, il est très facile de récupérer les informations des fils auxquels l'utilisateur est abonné, via la méthode suivante :  
   
```java
abonnementDAO.findAllByUtilisateur(String uno)
```

Cependant, après une heure de débogage, je me suis rendu compte d'un détail important : par défaut, lorsqu'on crée un fil, l'utilisateur doit être automatiquement abonné à ce fil.  

Pour faciliter la récupération du `fno` à partir de l'abonnement, nous avons décidé de stocker le `fno` dans la session via `setAttribute("current_fno", fno)`. La méthode dans le contrôleur `chargerFilSelectionner()` permet de charger le fil actuellement sélectionné par l'utilisateur. Étant donné que cette information est stockée dans la session, elle persiste même si l'utilisateur quitte et recharge la page. Ainsi, le fil sélectionné restera affiché, offrant une meilleure expérience utilisateur lors de l'affichage du fil sélectionné.

À ce stade, ma page permet de créer un fil. Une fois le fil créé, j'ai ma liste des titres de fils associés à leur heure `fno`. Lorsqu'on clique sur le titre du fil, un formulaire envoie le `fno` et le contrôleur appelle la méthode `chargerFilSelectionner()`, charge la session et renvoie l'utilisateur sur la page principale. Comme par magie, le titre du fil sélectionné apparaît.

#### Envoi et réception de messages : 

Pour terminer la partie 1, nous avons réalisé l'envoi et la réception des messages, ce qui a été relativement simple à implémenter, car le plus difficile a été géré avec la gestion des fils.

Voici comment cela fonctionne :

1. **Écriture du formulaire d'envoi du message** :  
   Dans le formulaire d'envoi du message, nous incluons un paramètre `message`, qui correspond au contenu du message. Pour savoir qui l'a envoyé, cela est simple : c'est nécessairement l'utilisateur connecté sur la session.  
   Nous avons donc écrit une petite méthode dans le contrôleur, `envoyerMessage()`, qui va créer le message avec les paramètres de la requête et sauvegarder le message dans la base de données.

2. **Affichage des messages** :  
   Comme nous pouvons envoyer des messages dans un fil, il serait utile de pouvoir les afficher. Pour cela, nous avons décidé d'écrire une nouvelle méthode dans `MessageDAO` : `findAllByFil()`. Cette méthode renvoie une liste de messages correspondant à un fil donné.  
   Grâce à cela, dans la page principale, nous parcourons cette liste et affichons les messages :

```jsp
<%
    List<Message> messages = messageDAO.findAllByFil(current_fno);
%>
<% if (messages.isEmpty()) { %>
    <p class="text-muted">Aucun message pour le moment.</p>
<% } else { %>
    <% for (Message message : messages) { %>
        <%
        int nbLike = likeDAO.findAllByMessage(message.getMno()).size();
        boolean liker = likeDAO.exists(pseudo, message.getMno()); 
        %>
        <div class="message">
            <strong><%= message.getUno() %></strong> 
            <span class="message-time">- <%= message.getDateVisuelle() %></span>
            <div class="message-text"><%= message.getContenu() %></div>
        </div>
    <% } %>
<% } %>
```

Durant cette étape, nous avons rencontré un petit problème : lorsqu'on envoyait un message et qu'on rafraîchissait la page, le même message était renvoyé. Je pense que cela était dû aux informations passées en paramètre dans la requête, qui restent toujours présentes.  
Pour corriger cela, dans la méthode `envoyerMessage()` du contrôleur, j'ai placé un `sendRedirect()` directement dans la méthode. Cela m'a un peu perturbé, car c'est la première fois que je ne respecte pas le même schéma habituel, ce qui fait que la méthode renvoie `null` au lieu de renvoyer le chemin de la page à afficher.


### **3.2 Fonctionnalités Partie 2**  

#### Interface utilisateur responsive :  

Pour l'interface utilisateur, nous avons utilisé Bootstrap. L'application est composée de plusieurs blocs alignés en colonnes. Lorsqu'on passe à une vue mobile, les éléments s'empilent automatiquement.  
Ensuite, pour obtenir une interface plus aérée et visuellement agréable, nous avons ajouté un off-canvas latéral gauche.

#### Gestion avancée des fils de discussion  

Actuellement, la gestion des fils de discussion est limitée. On ne peut pas :  
- Supprimer un fil  
- Quitter un fil si on est membre  
- Bannir des membres si on est administrateur  
- Inviter des utilisateurs à rejoindre un fil    
   
1. **Ajout de membres**    
L'ajout de membres est une action réservée aux administrateurs. Il faut donc différencier les utilisateurs ayant un abonnement de type **ADMIN** de ceux ayant un abonnement de type **USER**.  
Heureusement, notre table `abonnement` possède un champ `role` qui permet cette distinction.  

Nous avons donc ajouté une interface permettant aux administrateurs d’inviter des utilisateurs à rejoindre un fil. Pour cela, une page `ajouterUtilisateurAuFil.jsp` affiche la liste des utilisateurs qui ne sont pas encore membres du fil en cours.  

Un lien d’accès à cette page est ajouté sur l’interface principale, visible uniquement par les administrateurs.  

Du côté du contrôleur, nous avons ajouté une nouvelle méthode qui permet d’abonner un utilisateur sélectionné via le formulaire. Toutefois, **forcer un utilisateur à rejoindre un fil sans son consentement semble étrange**. Une amélioration possible serait donc d'implémenter un **système d’invitation**, où l’utilisateur pourrait accepter ou refuser.  

2. **Suppression d’un fil**  
Un administrateur doit pouvoir supprimer un fil de discussion. Pour cela, un bouton "Supprimer le fil" redirige vers une action du contrôleur qui exécute la méthode `supprimerFil()`.  

Cette méthode :  
- Récupère le fil actuel via la session  
- Supprime le fil de la base de données avec `filDAO.delete(current_fno)`  

Grâce à la contrainte **ON DELETE CASCADE**, tous les abonnements liés au fil sont également supprimés automatiquement.  

3. **Quitter un fil**  
Un utilisateur non administrateur doit pouvoir quitter un fil à tout moment.  

Si l’utilisateur n’est pas administrateur, un bouton "Quitter le fil" est affiché sur l’interface. La méthode `quitterFil()` dans le contrôleur désabonne simplement l’utilisateur courant du fil en appelant `abonnementDAO.delete(pseudo, current_fno)`.  

4. **Refactorisation avec les services**  
À ce stade, le contrôleur devenait **beaucoup trop volumineux et complexe**, rendant le code difficile à maintenir. Il y avait également un risque de confusion entre différentes méthodes similaires.  

Pour résoudre ce problème, nous avons mis en place un **design basé sur des classes de services**. Ces classes permettent de factoriser la logique du contrôleur en regroupant les actions par domaine.  

Par exemple, nous avons créé une classe `FilService` qui centralise toutes les méthodes liées aux fils de discussion. Nous avons appliqué le même principe pour d'autres **DAO** et **DTO**, ce qui a donné naissance à un package `service`.  

Après cette refactorisation, notre contrôleur est devenu beaucoup plus clair et structuré :  

```java
@Override
protected void service(HttpServletRequest req, HttpServletResponse res) {
   try {
      traiterRequete(req, res);
   } catch (DAOException e) {
      e.printStackTrace();
   } catch (Exception e) {
      e.printStackTrace();
   }
}

private void traiterRequete(HttpServletRequest req, HttpServletResponse res) {
    String action = req.getParameter("action");
    HttpSession session = req.getSession();
    String pseudoSession = (String) session.getAttribute("pseudo");

    if (pseudoSession != null && !pseudoSession.isEmpty()) {
        action = (action == null) ? "accesPagePrincipal" : action;
    } else {
        action = (action == null) ? "allerAccueil" : action;
    }

    String vue = "WEB-INF/vue/ErrorPage.jsp";
    try {
        vue = executerAction(action, req, session, res);
        if (vue != null) {
            System.out.println("Vue sélectionnée : " + vue);
            req.getRequestDispatcher(vue).forward(req, res);
        }
    } finally {
        DCM.closeConnection();
    }
}

private String executerAction(String action, HttpServletRequest req, HttpSession session, HttpServletResponse res) throws ServletException, IOException, DAOException {
    switch (action) {
        case "creerFil":
            return filService.creerFil(req, session);
        case "supprimerFil":
            return filService.supprimerFil(req, session);
        case "quitterFil":
            return filService.quitterFil(req, session);
        default:
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Action non supportée");
            return null;
    }
}
```

Cette approche nous permet d’aller encore plus loin dans la gestion des fils de discussion et de faciliter l'implémentation future d'autres fonctionnalités comme **les invitations ou les systèmes de modération avancés**.

#### Développement d'une API REST :

En plus de la récupération en GET des fils d'un membre donné et des messages postés sur ces fils, des services REST on été réalisés pour couvrir la majorité des DTO avec toutes les méthodes HTTP: GET, POST, PUT, DELETE et PATCH. Nous avons rencontré des difficultés pour gérer correctement les renvois de code d'erreur HTTP, ainsi que pour faire fonctionner correctement l'ObjectMapper, qui peut se réveler capricieux quand on manipule des types complexes, comme LocalDateTime.

#### Définition d’une durée de vie des messages  

**Choix de conception**  
Nous avons décidé que les messages auront une **durée de vie illimitée**. En effet, la plupart des applications de messagerie permettent aux utilisateurs de consulter leurs anciens messages, et nous ne voyons pas l’intérêt d’imposer une limite de temps. Notre application doit offrir la même flexibilité.  

**Fonctionnalité optionnelle : messages temporaires ou planifiés**  
Bien qu’une durée de vie illimitée soit notre choix par défaut, une fonctionnalité avancée pourrait permettre :  
- **L’envoi programmé d’un message** (exemple : envoyer un message dans 5 minutes)  
- **L'affichage temporaire d’un message** (exemple : un message visible pendant une durée déterminée avant de disparaître)  

Pour implémenter cela, nous pourrions ajouter un champ `dureeVieMessage` dans notre modèle `Message` :  
- Si `dureeVieMessage` est `NULL` ou `INFINI`, le message reste visible en permanence.  
- Si une valeur est définie, elle représenterait la durée de vie du message en minutes, heures ou jours.  
- Un message dont la date d’expiration est dépassée pourrait être supprimé ou masqué automatiquement.  

Cela ouvrirait la porte à des fonctionnalités comme les **messages éphémères** ou les **messages programmés**, offrant plus de flexibilité aux utilisateurs.

#### Possibilité de poster des images  

Pour gérer l'envoi d'images dans les messages, nous avons étudié deux solutions :  

1. **Stocker l'image directement dans la base de données** en ajoutant un champ `image` de type `BYTEA` à la table `Message`.  
2. **Stocker l'image sur le serveur** dans un répertoire dédié et enregistrer son chemin d'accès dans la table `Message`.  

Nous avons choisi la **deuxième solution** pour sa simplicité et pour éviter de surcharger la base de données avec des fichiers volumineux.  

**Mise en œuvre**  

Nous avons mis à jour la méthode `MessageService.envoyerMessage()` et le formulaire d'envoi de message en ajoutant un champ d'upload d'image. L'implémentation a nécessité plusieurs ajustements :  

- Encodage correct de l'image pour assurer sa compatibilité avec **Tomcat**.  
- Récupération et stockage du fichier dans le répertoire `images/` sur le serveur.  
- Enregistrement du **chemin du fichier** dans la base de données plutôt que son contenu binaire.  

**Problème rencontré avec Tomcat**  
Au début, Tomcat ne gérait pas correctement l’upload d’images. Nous avons dû ajouter la configuration suivante dans le contrôleur pour permettre la gestion des fichiers envoyés via un formulaire :  

```java
@WebServlet(urlPatterns = {"/Controleur"}, loadOnStartup = 1)
@MultipartConfig(
    maxFileSize = 1000000, 
    maxRequestSize = 10000000, 
    fileSizeThreshold = 10240
) // Obligatoire pour la gestion des fichiers envoyés
```

**Gestion des conflits de noms de fichiers**  

Un problème potentiel est la **collision de noms de fichiers** : si deux utilisateurs envoient des images portant le même nom, l'une peut écraser l'autre. Pour éviter cela, nous générons un **nom unique** en ajoutant un timestamp au nom du fichier.   

Bien que nous ayons initialement choisi cette approche pour minimiser les changements dans la base de données, nous avons réalisé qu’elle simulait en quelque sorte une **table `Image`**.  

Une **approche plus propre** aurait été de créer une table `Image` avec :  
- Un champ `idImage` (clé primaire, auto-incrémentée).  
- Un champ `image` pour stocker le flux binaire de l'image.  
- Une **clé étrangère** vers `Message` (`idMessage`).  

#### Gestion des likes sur les messages  

Pour la gestion des likes, nous avons décidé de créer une nouvelle table `Like`. Un problème est apparu dès le choix du nom, car **PostgreSQL** interprète `like` comme un mot-clé SQL. Pour éviter cette confusion, il faut entourer le nom de la table de guillemets (`"Like"`), bien que l'utilisation d'un nom plus explicite comme `MessageLike` soit recommandée pour éviter ce type de conflit.  

Cette table contient :  
- **`uno`** : l'identifiant de l'utilisateur qui a liké le message.  
- **`mno`** : l'identifiant du message liké.  
- **Clé primaire** : une clé composite sur (`uno`, `mno`) pour garantir qu'un utilisateur ne peut liker un message qu'une seule fois.  

Grâce à cette structure, on peut facilement identifier **quel message a été liké** et **par qui**.  

**Implémentation technique**  

Nous avons ajouté :  
- **Un DTO** `Like` pour représenter les données.  
- **Un DAO** `LikeDAO` pour interagir avec la base de données.  
- **Un service** `LikeService` pour gérer la logique métier.  

Lors de l'affichage d'un message, un **compteur de likes** est affiché avec une icône de cœur :  
- **Rouge** si l'utilisateur a liké le message.  
- **Gris** s'il ne l'a pas liké.  

Un **formulaire** permet d'ajouter ou supprimer un like. Lorsqu'un utilisateur clique dessus, `LikeService` récupère les informations nécessaires (message et utilisateur connecté via la session) et met à jour la table `Like`.  

**Gestion du comptage des likes**  

Pour compter le **nombre** de likes d'un message, une méthode est écrite : elle liste les likes appartenant à **un** message **particulier**. Il suffit de récupérer la taille de cette liste, et le tour est joué.

Un point à noter : **un utilisateur peut liker son propre message**. Nous avons décidé de ne pas restreindre cette possibilité, à l’image de plateformes comme **TikTok**, où un créateur peut aimer son propre contenu.  

### **3.3 Fonctionnalités Partie 3**  

Voici une version améliorée et corrigée de ton texte, en le rendant plus fluide, clair et structuré :  

---

#### Gestion des invitations et notifications  

Comme mentionné précédemment, nous souhaitons que l'utilisateur soit **notifié** lorsqu'un nouveau message est posté dans un fil, lorsqu'un de ses messages est liké et surtout lorsqu'il **reçoit une invitation** à rejoindre un fil (plutôt que d'y être ajouté de force).  

Pour cela, nous avons créé une table `Notification` avec les champs suivants :  
- **`id`** : identifiant unique de la notification.  
- **`fno`** : clé étrangère vers le fil concerné (si applicable).  
- **`uno`** : clé étrangère vers l’utilisateur recevant la notification.  
- **`type`** : permet de distinguer les différentes notifications (`LIKE`, `MESSAGE`, `INVITATION`). Cette distinction est nécessaire, car chaque type de notification a un comportement différent. Par exemple, une **invitation** doit pouvoir être **acceptée ou refusée**, alors qu'un **like** peut simplement être marqué comme **vu** et supprimé.  
- **`etat`** : indique l’état de la notification (`EN_ATTENTE`, `VUE`, `ACCEPTEE`, `REFUSEE`). Par défaut, une notification est créée avec l'état **"EN_ATTENTE"**.  

**Gestion des invitations**  

Nous avons dû modifier la méthode d'ajout d'un utilisateur à un fil. Désormais, lorsqu'on sélectionne un utilisateur à inviter, la méthode `inviterUtilisateur()` de `NotificationService` est appelée. Cette méthode crée une **notification de type "INVITATION"** avec l'état **"EN_ATTENTE"**.  

Sur la page principale, nous affichons les notifications **d'invitation** en attente pour un utilisateur donné.  

L'utilisateur peut alors **accepter ou refuser l’invitation**. Pour cela, nous avons ajouté deux nouvelles méthodes dans `NotificationService` :  
- `accepterInvitation()` : change l’état de la notification en **"ACCEPTÉE"** et inscrit l’utilisateur dans le fil.  
- `refuserInvitation()` : change l’état en **"REFUSÉE"** pour que la notification ne soit plus affichée.  

**Gestion des autres notifications**  

Nous avons ensuite ajouté des notifications pour informer l'utilisateur lorsqu'un **nouveau message** est posté dans un fil ou lorsqu'un **like** est ajouté sur l’un de ses messages.  
- Dans `LikeService.ajouterLike()`, une notification de type **"LIKE"** est créée.  
- Dans `MessageService.envoyerMessage()`, une notification de type **"MESSAGE"** est créée pour chaque utilisateur du fil.  

Les notifications de **like et de message** peuvent être marquées comme **vues** grâce à `NotificationService.supprimerNotification()`, qui met simplement leur état à **"VUE"** pour qu'elles n'apparaissent plus dans la liste des notifications actives.  

**Amélioration possible**  

Après coup, nous réalisons que notre approche est **complexe et peu optimisée**. Nous aurions dû **différencier les invitations des notifications** en créant une table `Invitation`.  
- Cela aurait évité de surcharger la table `Notification` avec des cas d'utilisation trop variés.  
- Les requêtes SQL et la gestion des états auraient été plus simples et plus claires.  

Cette séparation aurait permis une gestion plus propre et plus efficace des invitations et notifications.  

#### Gestion du profil de l'utilisateur avec ses statistiques  

Une autre amélioration apportée au projet est la **gestion du profil utilisateur**, permettant :  
- **La modification des informations personnelles** (pseudo, email, etc.).  
- **L'affichage des statistiques** de l'utilisateur, comme :  
  - Le **nombre de messages envoyés**.  
  - Le **nombre de fils créés**.  
  - La **date de création du compte**.  

Pour cela, nous avons créé une nouvelle page JSP : **`pageVueDeProfil.jsp`**.  
Cette page récupère le **pseudo** de l’utilisateur connecté via l’**attribut de session** et affiche **proprement** ses statistiques.  

Elle intègre également un **formulaire** permettant de **modifier les informations personnelles**.  

Lorsqu’un utilisateur soumet le formulaire, la méthode `UtilisateurService.modifierProfil()` est appelée. Cette méthode :  
1. **Compare les nouvelles valeurs** avec les données actuelles de l'utilisateur.  
2. **Met à jour uniquement les champs modifiés** dans la table `Utilisateur`.  

Cela garantit une mise à jour **optimisée** et **évite d'écraser inutilement des informations inchangées**.  

#### Liste des membres du fil : 

Le processus pour lister les membres d'un fil de discussion suit une approche similaire à celle utilisée pour afficher les fils. L'objectif est également de permettre à l'utilisateur d'afficher le profil d'un membre lorsqu'il clique dessus. En cas d'accès administrateur, le profil affichera des informations plus détaillées, et l'admin pourra bannir l'utilisateur si nécessaire.

Pour ce faire, nous utiliserons la méthode `abonnementDAO.findAllByFil(current_fno)` pour récupérer tous les abonnés au fil en cours. Comme un abonnement contient l'identifiant d'un utilisateur, il est possible de récupérer facilement ces informations en passant l'identifiant de l'utilisateur et le numéro du fil comme paramètres. Ces paramètres seront ensuite utilisés dans le formulaire de la page de profil utilisateur, `pageProfilUtilisateurFil.jsp.`

#### Gestion des cookies :

En suivant les principes que nous avons appris en cours, la gestion des cookies peut être mise en place pour améliorer l'expérience utilisateur. Bien que le site ne prévienne pas de l'utilisation des cookies, ce n'est pas un problème dans le cadre de ce projet. L'objectif principal est de permettre, lors de la connexion, de pré-remplir automatiquement le formulaire avec le pseudo de l'utilisateur, si celui-ci a déjà été saisi précédemment.

**Mise en place du cookie de connexion :**

1. **Création du cookie** : Lors de la connexion, dans la méthode `sessionService.seConnecter()`, nous vérifions si un cookie existe déjà. Si ce n'est pas le cas, nous en créons un avec le pseudo de l'utilisateur, et ce cookie est ensuite ajouté à la session.
   
2. **Utilisation du cookie dans le formulaire de connexion** : Dans le formulaire de connexion, nous vérifions la présence du cookie. Si ce dernier est `null`, l'utilisateur verra un champ de saisie classique. Si un cookie est trouvé, le champ "pseudo" sera automatiquement pré-rempli avec la valeur du cookie.

**À propos de `SessionService` :**

Nous avons conçu la classe `SessionService` dans le but de centraliser toutes les actions liées à la gestion de la session. Ainsi, la méthode de connexion se trouve dans cette classe. Cependant, après réflexion, nous avons constaté que la logique de gestion des sessions dans `SessionService` et `UtilisateurService` est similaire, et cela pourrait entraîner une certaine redondance. Les deux services gèrent en effet des aspects similaires, à savoir les informations liées à l'utilisateur et la session. Une possible amélioration serait d'intégrer ces deux services, ou de refactoriser la logique afin de ne pas dupliquer les opérations.

Pour éviter la duplication de la logique, il serait judicieux de bien définir les responsabilités de chaque service. `SessionService` pourrait être dédié à la gestion de la session et des cookies, tandis que `UtilisateurService` se concentrerait sur les opérations liées à l'utilisateur (par exemple, la récupération de ses informations depuis la base de données).

## **4. Conclusion**

### 4.1 Idées d'améliorations

#### Suppression de messages

Une fonctionnalité majeure que nous n'avons pas pu ajouter, par manque de temps, est la suppression des messages.

#### Gestion avancée du fil de discussion

Actuellement, il est possible de créer un fil et d'ajouter des utilisateurs à ce fil. Il serait logique, à l'instar de la modification du profil utilisateur et de l'affichage du profil, de pouvoir afficher une page de gestion du fil où l'on pourrait modifier le titre, la description, et paramétrer la durée de vie des messages. Dans cette logique, la gestion des membres serait incluse dans cette page avec les mêmes fonctionnalités qu'actuellement.

#### Gestion des rôles avancée

Actuellement, il n'existe que deux rôles : administrateur et utilisateur. Il serait pertinent d'ajouter une fonctionnalité permettant à l'administrateur d'ajouter des modérateurs, capables de bannir ou de supprimer des messages.

### 4.2 Idées d'optimisations

En analysant les classes de mon projet, on constate qu'il existe trois grandes familles : les classes de services, les DAO (Data Access Object) et les DTO (Data Transfer Object). Cependant, un intrus fait son apparition : la classe **NotificationManager**. Cette classe est une factorisation de **NotificationDAO** , tandis que les JSP interagissent avec ce manager. L'ajout de cette couche d'abstraction permet de mieux organiser la gestion des notifications, en particulier dans un système complexe tel que celui que nous avons abordé précédemment.

Avec l'ajout progressif de nouvelles fonctionnalités, il serait judicieux que chaque DAO ait son propre manager, ou que chaque DAO dispose d'une implémentation dédiée. Par exemple, il serait pertinent de créer une implémentation spécifique pour gérer un fil de discussion, ainsi que les messages associés à ce fil.
L'objectif ici est de simplifier le code Java dans les JSP, d'éviter les incohérences et les vérifications de type ou de valeurs dans ces dernières. Il reste à déterminer la solution la plus adaptée en fonction de nos besoins.

De plus, comme mentionné précédemment, la gestion des images et des notifications doit être améliorée. Cela inclut une gestion plus fluide des images et des notifications pour renforcer l'expérience utilisateur.

Enfin, une approche de gestion multi-contrôleurs pourrait être envisagée. Comme on peut le constater, le code de mon contrôleur principal commence à devenir volumineux. Il serait donc pertinent de transformer les méthodes de service en sous-contrôleurs, avec un contrôleur principal qui redirige vers le sous-contrôleur concerné par l'action. Cela permettrait de mieux structurer le code et de rendre la maintenance plus facile à long terme.


