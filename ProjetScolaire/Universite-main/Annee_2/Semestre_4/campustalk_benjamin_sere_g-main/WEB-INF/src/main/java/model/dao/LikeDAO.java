package model.dao;

import java.sql.*;
import java.util.*;
import exceptions.DAOException;
import model.DCM;
import model.dto.Like;

/**
 * Implémentation de l'interface DAO pour l'entité Like.
 * Permet d'effectuer des opérations CRUD (Create, Read, Delete) sur la base de données.
 * 
 * @author Benjamin
 */
public class LikeDAO {

    /**
     * Sauvegarde un like dans la base de données.
     * 
     * @param like Le like à sauvegarder.
     * @return true si le like a été sauvegardé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de sauvegarde.
     */
    public boolean save(Like like) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO \"Like\" (uno, mno) VALUES (?, ?);");
            stmt.setString(1, like.getUno());
            stmt.setInt(2, like.getMno());
            System.out.println("LikeDAO Requête : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la sauvegarde du like", e);
        }
    }

    /**
     * Supprime un like de la base de données.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @param mno Identifiant du message.
     * @return true si le like a été supprimé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de suppression.
     */
    public boolean delete(String uno, int mno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM \"Like\"  WHERE uno = ? AND mno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, mno);
            System.out.println("LikeDAO Requête : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du like", e);
        }
    }

    /**
     * Vérifie si un utilisateur a liké un message.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @param mno Identifiant du message.
     * @return true si l'utilisateur a liké le message, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de recherche.
     */
    public boolean exists(String uno, int mno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM \"Like\"  WHERE uno = ? AND mno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, mno);
            System.out.println("LikeDAO Requête : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la vérification du like", e);
        }
    }

    /**
     * Récupère tous les likes d'un message donné.
     * 
     * @param mno Identifiant du message.
     * @return Liste des likes associés au message.
     * @throws DAOException Si une erreur survient lors de l'opération de récupération.
     */
    public List<Like> findAllByMessage(int mno) throws DAOException {
        List<Like> likes = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM \"Like\"  WHERE mno = ? ;");
            stmt.setInt(1, mno);
            System.out.println("LikeDAO Requête : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                likes.add(new Like(rs.getString("uno"), rs.getInt("mno"), rs.getTimestamp("dateLike").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération des likes du message", e);
        }
        return likes;
    }

    /**
    * Récupère tous les likes reçus par un utilisateur sur tous les fils.
    * 
    * @param uno Identifiant de l'utilisateur.
    * @return Liste des likes reçus par l'utilisateur.
    * @throws DAOException Si une erreur survient lors de l'opération de récupération.
    */
    public List<Like> findAllByUtilisateur(String uno) throws DAOException {
        List<Like> likes = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT l.uno AS liker, l.mno, l.dateLike FROM \"Like\" l JOIN Message m ON l.mno = m.mno WHERE m.uno = ?;");
            stmt.setString(1, uno);

            System.out.println("LikeDAO Requête : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                likes.add(new Like(rs.getString("liker"), rs.getInt("mno"), rs.getTimestamp("dateLike").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération des likes reçus par l'utilisateur", e);
        }
        return likes;
    }

    /**
    * Récupère tous les likes reçus par un utilisateur dans un fil donné.
    * 
    * @param uno Identifiant de l'utilisateur.
    * @param fno Identifiant du fil.
    * @return Liste des likes reçus par l'utilisateur dans le fil spécifié.
    * @throws DAOException Si une erreur survient lors de l'opération de récupération.
    */
    public List<Like> findAllByUtilisateurAndFil(String uno, int fno) throws DAOException {
        List<Like> likes = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT l.uno, l.mno, l.dateLike FROM \"Like\" l JOIN Message m ON l.mno = m.mno WHERE m.uno = ? AND m.fno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, fno);
            System.out.println("LikeDAO Requête : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                likes.add(new Like(rs.getString("uno"), rs.getInt("mno"), rs.getTimestamp("dateLike").toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération des likes reçus par l'utilisateur dans le fil", e);
        }
        return likes;
    }
}
