package model.dao;

import java.sql.*;
import java.util.*;
import exceptions.DAOException;
import model.DCM;
import model.dto.Notification;

/**
 * Classe NotificationDAO implémentant l'interface DAO pour gérer les opérations
 * CRUD liées aux notifications envoyées aux utilisateurs.
 * 
 * @author Benjamin
 */
public class NotificationDAO {

    /**
     * Sauvegarde une notification dans la base de données.
     * 
     * @param notification La notification à sauvegarder.
     * @return true si la notification a été sauvegardée avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      sauvegarde.
     */
    public boolean save(Notification notification) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Notification (uno, fno, type) VALUES (?, ?, ?);");
            stmt.setString(1, notification.getUno());
            stmt.setInt(2, notification.getFno());
            stmt.setString(3, notification.getType());
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la sauvegarde de la notification", e);
        }
    }
    

    /**
     * Met à jour l'état d'une notification dans la base de données.
     * 
     * @param notification La notification à mettre à jour.
     * @return true si la notification a été mise à jour avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de mise à
     *                      jour.
     */
    public boolean update(Notification notification) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE Notification SET etat = ? WHERE nno = ?;");
            stmt.setString(1, notification.getEtat());
            stmt.setInt(2, notification.getNno());
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la mise à jour de la notification", e);
        }
    }

    /**
     * Supprime une notification de la base de données.
     * 
     * @param nno L'identifiant de la notification à supprimer.
     * @return true si la notification a été supprimée avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      suppression.
     */
    public boolean delete(int nno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Notification WHERE nno = ?;");
            stmt.setInt(1, nno);
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la suppression de la notification", e);
        }
    }

    /**
     * Recherche une notification par son identifiant.
     * 
     * @param nno L'identifiant de la notification.
     * @return La notification trouvée, ou null si aucune notification n'est trouvée.
     * @throws DAOException Si une erreur survient lors de l'opération de recherche.
     */
    public Notification findById(int nno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Notification WHERE nno = ?;");
            stmt.setInt(1, nno);
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Notification(
                        rs.getInt("nno"),
                        rs.getString("uno"),
                        rs.getInt("fno"),
                        rs.getString("type"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la recherche de la notification", e);
        }
        return null;
    }

    /**
     * Récupère toutes les notifications d'un utilisateur donné.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @return Liste des notifications de l'utilisateur.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des notifications.
     */
    public List<Notification> findAllByUtilisateur(String uno) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Notification WHERE uno = ? AND etat = 'EN_ATTENTE';");
            stmt.setString(1, uno);
            System.out.println("NotificationDAO Requette : " + stmt.toString());


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("nno"),
                        rs.getString("uno"),
                        rs.getInt("fno"),
                        rs.getString("type"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des notifications de l'utilisateur", e);
        }
        return notifications;
    }

    /**
     * Récupère toutes les notifications d'un fil donné.
     * 
     * @param fno Identifiant du fil.
     * @return Liste des notifications du fil.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des notifications.
     */
    public List<Notification> findAllByFil(int fno) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Notification WHERE fno = ?;");
            stmt.setInt(1, fno);
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("nno"),
                        rs.getString("uno"),
                        rs.getInt("fno"),
                        rs.getString("type"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des notifications du fil", e);
        }
        return notifications;
    }

    /**
    * Récupère toutes les invitation (Notif en 'ATTENTE') en attente pour un utilisateur donné.
    * 
    * @param fno Identifiant du fil.
    * @param uno Identifiant de l'utilisateur.
    * @return Liste des notifications en attente pour l'utilisateur dans le fil.
    * @throws DAOException Si une erreur survient lors de l'opération de récupération des notifications.
    */
    public List<Notification> getInvitUtilisateurList(String uno) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Notification WHERE uno = ? AND etat = 'EN_ATTENTE' AND type = 'INVITATION';");
            stmt.setString(1, uno);
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("nno"),
                        rs.getString("uno"),
                        rs.getInt("fno"),
                        rs.getString("type"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des notifications en attente", e);
        }
        return notifications;
    }

    public List<Notification> getNotificationsFil(int fno, String pseudo) throws DAOException{
        List<Notification> notifications = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Notification WHERE fno = ? AND uno = ? AND etat = 'EN_ATTENTE' AND type IN('MESSAGE','LIKE');");
            stmt.setInt(1, fno);
            stmt.setString(2, pseudo);
            System.out.println("NotificationDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(new Notification(
                        rs.getInt("nno"),
                        rs.getString("uno"),
                        rs.getInt("fno"),
                        rs.getString("type"),
                        rs.getString("etat"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des notifications", e);
        }
        return notifications;
    }
}