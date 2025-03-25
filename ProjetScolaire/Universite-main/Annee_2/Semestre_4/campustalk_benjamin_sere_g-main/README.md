# README - Projet CampusTalk

Bienvenue dans le projet **CampusTalk** ! Ce document a pour but de détailler les différents aspects du projet, de la modélisation de la base de données aux points techniques rencontrés lors du développement. Vous trouverez ci-dessous une table des matières pour naviguer facilement entre les différentes sections du projet.

## Table des matières

- [La description générale de l’application](rapport/RapportDescriptionApp.md)  
  Ce rapport présente l'équivalent de la `liste des entrées du/des contrôleurs avec leurs fonctionnalités`, ainsi que `les points techniques difficiles et leur résolution`. Ces éléments sont détaillés dans la section **Explication de l'implémentation des différentes fonctionnalités**, que vous retrouverez dans le sommaire.

- [Rapport Base de Données](rapport/RapportSQL.md)

- [Comment démarrer l'application (avec le VPN)](rapport/DemarrerTomcat.md)


Bonne lecture !

## L’arborescence globale de l’application : 

```bash
.
├── campustalk_benjamin_sere_g.iml
├── css
│   ├── pagePrincipal.css
│   └── styles.css
├── js
│   └── animationText.js
├── PowerAMC
│   ├── CampusTalk.mcd
│   ├── CampusTalkMLD.mld
│   └── CampusTalkSQL.mpd
├── rapport
│   ├── DemarrerTomcat.md
│   ├── RapportDescriptionApp.md
│   └── RapportSQL.md
├── README.md
├── res
│   ├── img
│   │   └── CampusTalkMVC.PNG
│   └── SAe_S4_sujet_ReseauSocial.pdf
├── sql
│   ├── peuplement.sql
│   ├── requetes.sql
│   └── script.sql
└── WEB-INF
    ├── classes
    ├── src
    │   └── main
    │       └── java
    │           ├── controleur
    │           │   ├── Controleur.java
    │           │   └── endpoints
    │           │       ├── AbonnementRestAPI.java
    │           │       ├── BasicAuth.java
    │           │       ├── FilRestAPI.java
    │           │       ├── MessageRestAPI.java
    │           │       └── UtilisateurRestAPI.java
    │           ├── exceptions
    │           │   └── DAOException.java
    │           ├── model
    │           │   ├── dao
    │           │   │   ├── AbonnementDAO.java
    │           │   │   ├── FilDAO.java
    │           │   │   ├── LikeDAO.java
    │           │   │   ├── MessageDAO.java
    │           │   │   ├── NotificationDAO.java
    │           │   │   └── UtilisateurDAO.java
    │           │   ├── DCM.java
    │           │   ├── dto
    │           │   │   ├── Abonnement.java
    │           │   │   ├── Fil.java
    │           │   │   ├── Like.java
    │           │   │   ├── Message.java
    │           │   │   ├── Notification.java
    │           │   │   └── Utilisateur.java
    │           │   ├── EnumRole.java
    │           │   └── NotificationManager.java
    │           └── service
    │               ├── FilService.java
    │               ├── LikeService.java
    │               ├── MessageService.java
    │               ├── NotificationService.java
    │               ├── SessionService.java
    │               └── UtilisateurService.java
    └── vue
        ├── ajouterUtilisateurAuFil.jsp
        ├── ErrorPage.jsp
        ├── pageAccueille.jsp
        ├── pageConnection.jsp
        ├── pageCreeUnFil.jsp
        ├── pagePrincipal.jsp
        ├── pageProfilUtilisateurFil.jsp
        ├── pageRegister.jsp
        └── pageVuDeProfil.jsp

25 directories, 59 files
```


**Utilisation de ChatGPT :**

Nous préférons être transparent avec vous concernant l'utilisation de ChatGPT dans ce projet. Voici les domaines dans lesquels nous avons sollicité son aide :

- **Correction des fautes d'orthographe** dans la documentation du rapport.
- **Aide à la rédaction de la JavaDoc**, afin de mieux structurer et documenter le code.
- **Aide à la gestion des images**, notamment avec Tomcat. J'ai cherché des informations dans le cours sans trouver de solution adéquate. Voici l'extrait de code que j'ai utilisé pour gérer l'envoi d'images :

```java
@WebServlet(urlPatterns = {"/Controleur"}, loadOnStartup = 1)
@MultipartConfig(maxFileSize = 1000000, maxRequestSize = 10000000, fileSizeThreshold = 10240) // Obligatoire pour la gestion des images
```
