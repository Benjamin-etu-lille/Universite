## Tuto pour demarrer Tomcat

### Démarrer mon serveur Tomcat correctement  

1. **Ajouter les archives suivantes dans `tomcat/lib`** :  
   - `servlet-api.jar`  
   - `postgresql-42.7.4.jar`  
   - `jackson-annotations-2.15.3.jar`  
   - `jackson-databind-2.15.3.jar`  
   - `jackson-datatype-jsr310-2.18.3.jar`  
   - `jackson-core-2.15.3.jar`  

   📥 [Télécharger la librairie `jackson-datatype-jsr310`](../res/jackson-datatype-jsr310-2.18.3.jar)  

2. **Exécuter le script SQL pour créer les tables**  
   - Ouvrir un terminal et exécuter le script SQL situé dans `sql/script.sql`.  
   - Ce script crée les tables nécessaires au fonctionnement de l'application.  
   ⚠ **Attention** : L'exécution de ce script supprimera les tables existantes portant le même nom que celles créées. Pensez à sauvegarder vos données si nécessaire.  

3. **Configurer la connexion à la base de données**  
   - L'application doit pouvoir interagir avec les tables générées.  
   - Modifier les lignes suivantes dans la classe `DCM`, située dans `WEB-INF/src/main/java/model/DCM.java` :  

   ```java
   public class DCM {
       private static final String URL = "jdbc:postgresql://host:port/database";
       private static final String USERNAME = "votre_utilisateur";
       private static final String PASSWORD = "votre_mot_de_passe";
       ...
   }
   ```

4. **Démarrer Tomcat**  
   - Se rendre dans le répertoire `tomcat/bin`.  
   - Exécuter la commande suivante :  

   ```bash
   ./catalina.sh run
   ```   

### Pour démarrer mon serveur Tomcat depuis une autre machine :
1. Se connecter en SSH à une machine, par exemple `ssh acajou02`.
2. Aller dans le répertoire `tomcat/bin`.
3. Exécuter la commande suivante :
   ```bash
   ./catalina.sh run
   ```

### Adresse d'accès au serveur :

1. Serveur lancer en local :
``` 
http://localhost:1826/campustalk_benjamin_sere_g/Controleur
```
2. Si vous utiliser un vpn :
Une fois le serveur démarré, l'adresse d'accès est, dans cet exemple :
```
http://acajou02.iutinfo.fr:1826/campustalk_benjamin_sere_g/Controleur
```
