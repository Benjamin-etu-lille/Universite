package model.dao;

import java.sql.*;
import java.util.*;
import exceptions.DAOException;
import model.DCM;
import model.dto.Message;

/**
 * Implémentation de l'interface DAO pour l'entité Message.
 * Permet d'effectuer des opérations CRUD (Create, Read, Update, Delete) sur la
 * base de données.
 * 
 * @author Benjamin
 */
public class MessageDAO {

    /**
     * Sauvegarde un message dans la base de données.
     * 
     * @param message Le message à sauvegarder.
     * @return true si le message a été sauvegardé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      sauvegarde.
     */
    public boolean save(Message message) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Message (contenu, imagePath, uno, fno) VALUES (?, ?, ?, ?);");
            stmt.setString(1, message.getContenu());
            stmt.setString(2, message.getImagePath());
            stmt.setString(3, message.getUno());
            stmt.setInt(4, message.getFno());
            System.out.println("MessageDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la sauvegarde du message", e);
        }
    }

    /**
     * Met à jour un message dans la base de données.
     * 
     * @param message Le message à mettre à jour.
     * @return true si le message a été mis à jour avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de mise à
     *                      jour.
     */
    public boolean update(Message message) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE Message SET contenu = ? WHERE mno = ?;");
            stmt.setString(1, message.getContenu());
            stmt.setInt(2, message.getMno());
            System.out.println("MessageDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la mise à jour du message", e);
        }
    }

    /**
     * Supprime un message de la base de données.
     * 
     * @param mno Identifiant du message à supprimer.
     * @return true si le message a été supprimé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      suppression.
     */
    public boolean delete(int mno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Message WHERE mno = ?;");
            stmt.setInt(1, mno);
            System.out.println("MessageDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la suppression du message", e);
        }
    }

    /**
     * Recherche un message en fonction de son identifiant.
     * 
     * @param mno Identifiant du message.
     * @return Le message trouvé, ou null si aucun message n'est trouvé.
     * @throws DAOException Si une erreur survient lors de l'opération de recherche.
     */
    public Message findById(int mno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Message WHERE mno = ?;");
            stmt.setInt(1, mno);
            System.out.println("MessageDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("mno"), rs.getString("contenu"),rs.getString("imagePath"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime(),
                        rs.getString("uno"), rs.getInt("fno"));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la recherche du message", e);
        }
        return null;
    }

    /**
     * Récupère tous les messages d'un fil donné.
     * 
     * @param fno Identifiant du fil de discussion.
     * @return Liste des messages du fil.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des messages.
     */
    public List<Message> findAllByFil(int fno) throws DAOException {
        List<Message> messages = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Message WHERE fno = ?;");
            stmt.setInt(1, fno);
            System.out.println("MessageDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("mno"), rs.getString("contenu"),rs.getString("imagePath"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime(),
                        rs.getString("uno"), rs.getInt("fno")));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des messages du fil", e);
        }
        return messages;
    }

    /**
     * Récupère tous les messages que l'utilisateur a envoyée.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @return Liste des messages envoyer par l'utilisateur.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des messages.
     */
    public List<Message> findAllByUtilisateur(String uno) throws DAOException {
        List<Message> messages = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Message WHERE uno = ?;");
            stmt.setString(1, uno);
            System.out.println("MessageDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("mno"), rs.getString("contenu"),rs.getString("imagePath"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime(),
                        rs.getString("uno"), rs.getInt("fno")));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des messages envoyer par l'utilisateur", e);
        }
        return messages;
    }

    /**
     * Récupère tous les messages que l'utilisateur a envoyée dans un fil particulier.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @param fno Identifiant du fil de discussion.
     * @return Liste des messages envoyer par l'utilisateur dans un fil préci.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des messages.
     */
    public List<Message> findAllByUtilisateurAndFil(String uno, int fno) throws DAOException {
        List<Message> messages = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Message WHERE uno = ? AND fno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, fno);
            System.out.println("MessageDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("mno"), rs.getString("contenu"),rs.getString("imagePath"),
                        rs.getTimestamp("dateEnvoi").toLocalDateTime(),
                        rs.getString("uno"), rs.getInt("fno")));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des messages envoyer par l'utilisateur dans un fil particulier", e);
        }
        return messages;
    }
}
