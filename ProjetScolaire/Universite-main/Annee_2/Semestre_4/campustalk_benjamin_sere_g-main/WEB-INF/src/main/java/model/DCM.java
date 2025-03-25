package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire de connexions à la base de données PostgreSQL (DatabaseConnectionManager).
 * <p>
 * Cette classe implémente le pattern Singleton afin de garantir qu'il n'y ait qu'une seule instance 
 * de la connexion à la base de données dans toute l'application. Elle permet de centraliser et de gérer
 * de manière efficace l'accès à la base de données en fournissant des méthodes pour obtenir une connexion
 * active et pour la fermer une fois l'opération terminée. 
 * </p>
 * 
 * <p>
 * La méthode {@link #getConnection()} permet d'obtenir une connexion à la base de données, 
 * en initialisant la connexion si elle n'existe pas déjà. La méthode {@link #closeConnection()} permet
 * de fermer la connexion une fois que les opérations de base de données sont terminées.
 * </p>
 * 
 * <p>
 * Il est important de fermer la connexion lorsque vous avez terminé vos opérations afin d'éviter des 
 * fuites de ressources et de maintenir une gestion optimale des connexions à la base de données.
 * </p>
 * 
 * <p>
 * Exemple d'utilisation :
 * <pre>
 *    Connection con = DCM.getConnection();
 *    // Utilisez la connexion pour exécuter des requêtes SQL ou des opérations de base de données
 *    // ...
 *    // Fermez la connexion après utilisation
 *    DCM.closeConnection();
 * </pre>
 * </p>
 * 
 * @author Benjamin
 */

public class DCM {
    private static final String URL = "jdbc:postgresql://psqlserv/but2";
    private static final String USERNAME = "benjaminsereetu";
    private static final String PASSWORD = "moi";
    private static Connection connection;

    private DCM() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'instance unique de la connexion à la base de données.
     * Si la connexion n'est pas encore établie, elle est créée.
     *
     * @return La connexion à la base de données.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données si elle est ouverte.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
