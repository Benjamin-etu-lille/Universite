package model.dao;

import java.sql.*;
import java.util.*;

import exceptions.DAOException;
import model.DCM;
import model.dto.Utilisateur;

/**
 * Implémentation de l'interface DAO pour l'entité Utilisateur.
 * Permet d'effectuer des opérations CRUD (Create, Read, Update, Delete) sur la
 * base de données.
 * 
 * @author Benjamin
 */
public class UtilisateurDAO {

    /**
     * Sauvegarde un utilisateur dans la base de données.
     * 
     * @param utilisateur L'utilisateur à sauvegarder.
     * @return true si l'utilisateur a été correctement sauvegardé, false sinon.
     * @throws DAOException Si une erreur survient lors de l'exécution de la requête
     *                      SQL.
     */
    public boolean save(Utilisateur utilisateur) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Utilisateur (uno, nom, prenom, email, mdp) VALUES (?, ?, ?, ?, ?);");
            stmt.setString(1, utilisateur.getUno());
            stmt.setString(2, utilisateur.getNom());
            stmt.setString(3, utilisateur.getPrenom());
            stmt.setString(4, utilisateur.getEmail());
            stmt.setString(5, utilisateur.getMdp());
            System.out.println("UtilisateurDAO Requette : " + stmt.toString());
                
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    /**
     * Met à jour un utilisateur dans la base de données.
     * 
     * @param utilisateurs L'utilisateur à mettre à jour.
     * @return true si l'utilisateur a été correctement mis à jour, false sinon.
     * @throws DAOException Si une erreur survient lors de l'exécution de la requête
     *                      SQL.
     */
    public boolean update(Utilisateur utilisateur) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE Utilisateur SET nom = ?, prenom = ?, email = ?, mdp = ? WHERE uno = ?;");
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMdp());
            stmt.setString(5, utilisateur.getUno());
            System.out.println("UtilisateurDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    /**
     * Supprime un utilisateur de la base de données.
     * 
     * @param uno L'identifiant de l'utilisateur à supprimer.
     * @return true si l'utilisateur a été correctement supprimé, false sinon.
     * @throws DAOException Si une erreur survient lors de l'exécution de la requête
     *                      SQL.
     */
    public boolean delete(String uno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Utilisateur WHERE uno = ?;");
            stmt.setString(1, uno);
            System.out.println("UtilisateurDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    /**
     * Recherche un utilisateur par son identifiant.
     * 
     * @param uno L'identifiant de l'utilisateur à rechercher.
     * @return L'utilisateur trouvé ou null si aucun utilisateur n'est trouvé.
     * @throws DAOException Si une erreur survient lors de l'exécution de la requête
     *                      SQL.
     */
    public Utilisateur findById(String uno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Utilisateur WHERE uno = ?;");
            stmt.setString(1, uno);
            System.out.println("UtilisateurDAO Requette : "+ stmt);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(rs.getString("uno"), rs.getString("nom"), rs.getString("prenom"),
                        rs.getString("email"), rs.getString("mdp"), rs.getTimestamp("dateCreation").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la recherche de l'utilisateur", e);
        }
        return null;
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     * 
     * @return Une liste d'utilisateurs.
     * @throws DAOException Si une erreur survient lors de l'exécution de la requête
     *                      SQL.
     */
    public List<Utilisateur> findAll() throws DAOException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Utilisateur;");
            System.out.println("UtilisateurDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(rs.getString("uno"), rs.getString("nom"), rs.getString("prenom"),
                        rs.getString("email"), rs.getString("mdp"), rs.getTimestamp("dateCreation").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération de la liste des utilisateurs", e);
        }
        return utilisateurs;
    }

    /**
    * Récupère les utilisateurs qui ne sont pas abonnés au fil courant et qui n'ont pas 
    * encore reçu de notification pour ce fil.
    * <p>
    * Cette méthode retourne une liste d'utilisateurs qui remplissent les critères suivants :
    * </p>
    * <ul>
    *     <li>Ils ne sont pas abonnés au fil spécifié ({@code currentFno}).</li>
    *     <li>Ils n'ont pas encore reçu de notification pour ce fil.</li>
    * </ul>
    * 
    * @param currentFno L'identifiant du fil courant.
    * @return Une liste d'utilisateurs correspondant aux critères.
    * @throws DAOException Si une erreur survient lors de l'exécution de la requête SQL.
    */
    public List<Utilisateur> getUtilisateursAInviter(int currentFno) throws DAOException {
        List<Utilisateur> utilisateurs = new ArrayList<>();

        String sql = "SELECT * "
               + "FROM Utilisateur AS u "
               + "WHERE u.uno NOT IN ("
               + "    SELECT a.uno "
               + "    FROM Abonnement a "
               + "    WHERE a.fno = ? "
               + ") "
               + "AND (u.uno, ?) NOT IN ("
               + "    SELECT n.uno, n.fno FROM Notification AS n WHERE n.etat = 'EN_ATTENTE'"
               + ")";

        try (Connection con = DCM.getConnection(); 
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, currentFno);
            stmt.setInt(2, currentFno);
            System.out.println("UtilisateurDAO Requette : " + stmt.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(new Utilisateur(rs.getString("uno"), rs.getString("nom"), rs.getString("prenom"),
                            rs.getString("email"), rs.getString("mdp"), rs.getTimestamp("dateCreation").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des utilisateurs qui peuvent etre invitée", e);
        }

        return utilisateurs;
    }
}
