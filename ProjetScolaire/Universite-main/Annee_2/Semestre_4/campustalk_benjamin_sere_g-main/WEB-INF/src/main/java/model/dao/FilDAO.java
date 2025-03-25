package model.dao;

import java.sql.*;
import java.util.*;
import exceptions.DAOException;
import model.DCM;
import model.dto.Fil;

/**
 * Implémentation de l'interface DAO pour l'entité Fil.
 * Permet d'effectuer des opérations CRUD (Create, Read, Update, Delete) sur la
 * base de données.
 * 
 * @author Benjamin
 */
public class FilDAO {

    /**
     * Sauvegarde un fil dans la base de données.
     * 
     * @param fil Le fil à sauvegarder.
     * @return true si le fil a été sauvegardé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      sauvegarde.
     */
    public int save(Fil fil) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Fil (titre, description) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, fil.getTitre());
            stmt.setString(2, fil.getDescription());
            System.out.println("FilDAO Requette : " + stmt.toString());

            stmt.executeUpdate();
    
            
            ResultSet rs = stmt.getGeneratedKeys(); // Récupération de la clé primaire générée
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new DAOException("Impossible de récupérer l'ID du fil inséré.");
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la sauvegarde du fil", e);
        }
    }
    
    /**
     * Met à jour un fil dans la base de données.
     * 
     * @param fil Le fil à mettre à jour.
     * @return true si le fil a été mis à jour avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de mise à
     *                      jour.
     */
    public boolean update(Fil fil) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE Fil SET titre = ?, description = ? WHERE fno = ?;");
            stmt.setString(1, fil.getTitre());
            stmt.setString(2, fil.getDescription());
            stmt.setInt(3, fil.getFno());
            System.out.println("FilDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la mise à jour du fil", e);
        }
    }

    /**
     * Supprime un fil de la base de données.
     * 
     * @param fno Identifiant du fil à supprimer.
     * @return true si le fil a été supprimé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      suppression.
     */
    public boolean delete(int fno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Fil WHERE fno = ?;");
            stmt.setInt(1, fno);
            System.out.println("FilDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la suppression du fil", e);
        }
    }

    /**
     * Recherche un fil en fonction de son identifiant.
     * 
     * @param fno Identifiant du fil.
     * @return Le fil trouvé, ou null si aucun fil n'est trouvé.
     * @throws DAOException Si une erreur survient lors de l'opération de recherche.
     */
    public Fil findById(int fno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Fil WHERE fno = ?;");
            stmt.setInt(1, fno);
            System.out.println("FilDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Fil(rs.getInt("fno"), rs.getString("titre"), rs.getString("description"),
                        rs.getTimestamp("dateCreation").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la recherche du fil", e);
        }
        return null;
    }

    /**
     * Récupère tous les fils de discussion.
     * 
     * @return Liste de tous les fils.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des fils.
     */
    public List<Fil> findAll() throws DAOException {
        List<Fil> fils = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Fil;");
            System.out.println("FilDAO Requette : " + stmt.toString());
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fils.add(new Fil(rs.getInt("fno"), rs.getString("titre"), rs.getString("description"),
                        rs.getTimestamp("dateCreation").toLocalDateTime()));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des fils", e);
        }
        return fils;
    }
}
