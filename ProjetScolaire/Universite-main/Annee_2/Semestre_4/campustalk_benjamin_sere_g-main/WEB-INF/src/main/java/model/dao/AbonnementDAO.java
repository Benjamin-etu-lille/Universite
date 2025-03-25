package model.dao;

import java.sql.*;
import java.util.*;
import exceptions.DAOException;
import model.DCM;
import model.EnumRole;
import model.dto.Abonnement;

/**
 * Classe AbonnementDAO implémentant l'interface DAO pour gérer les opérations
 * CRUD liées aux abonnements des utilisateurs dans les fils de discussion.
 * Fournit des méthodes pour sauvegarder, mettre à jour, supprimer et récupérer des abonnements.
 * 
 * @author Benjamin
 */
public class AbonnementDAO {

    /**
     * Sauvegarde un abonnement dans la base de données.
     * 
     * @param abonnement L'abonnement à sauvegarder.
     * @return true si l'abonnement a été sauvegardé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      sauvegarde.
     */
    public boolean save(Abonnement abonnement) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Abonnement (uno, fno, role) VALUES (?, ?, ?) ON CONFLICT (uno, fno) DO NOTHING;");
            stmt.setString(1, abonnement.getUno());
            stmt.setInt(2, abonnement.getFno());
            stmt.setString(3, abonnement.getRole().toString());
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la sauvegarde de l'abonnement", e);
        }
    }

    /**
     * Met à jour un abonnement dans la base de données.
     * 
     * @param abonnement L'abonnement à mettre à jour.
     * @return true si l'abonnement a été mis à jour avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de mise à
     *                      jour.
     */
    public boolean update(Abonnement abonnement) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("UPDATE Abonnement SET role = ? WHERE uno = ? AND fno = ?;");
            stmt.setString(1, abonnement.getRole().toString());
            stmt.setString(2, abonnement.getUno());
            stmt.setInt(3, abonnement.getFno());
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la mise à jour de l'abonnement", e);
        }
    }

    /**
     * Supprime un abonnement d'un utilisateur dans un fil.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @param fno Identifiant du fil.
     * @return true si l'abonnement a été supprimé avec succès, false sinon.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      suppression.
     */
    public boolean delete(String uno, int fno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Abonnement WHERE uno = ? AND fno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, fno);
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la suppression de l'abonnement", e);
        }
    }

    /**
    * Vérifie si un utilisateur est administrateur d'un fil donné.
    * 
    * @param uno Identifiant de l'utilisateur.
    * @param fno Identifiant du fil.
    * @return true si l'utilisateur est administrateur, false sinon.
    * @throws DAOException Si une erreur survient lors de l'opération de vérification.
    */
    public boolean isAdmin(String uno, int fno) throws DAOException {
        String query = "SELECT COUNT(*) FROM Abonnement WHERE uno = ? AND fno = ? AND role = 'ADMIN'";

        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, uno);
            stmt.setInt(2, fno);
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la vérification du rôle administrateur", e);
        }
        return false;
    }

    /**
     * Recherche un abonnement spécifique d'un utilisateur dans un fil.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @param fno Identifiant du fil.
     * @return L'abonnement trouvé, ou null si aucun abonnement n'est trouvé.
     * @throws DAOException Si une erreur survient lors de l'opération de recherche.
     */
    public Abonnement findByUserAndFil(String uno, int fno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Abonnement WHERE uno = ? AND fno = ?;");
            stmt.setString(1, uno);
            stmt.setInt(2, fno);
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String roleStr = rs.getString("role");
                EnumRole role = EnumRole.valueOf(roleStr);
                return new Abonnement(rs.getString("uno"), rs.getInt("fno"), role);
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la recherche de l'abonnement", e);
        }
        return null;
    }

    /**
     * Récupère tous les abonnements d'un fil donné.
     * 
     * @param fno Identifiant du fil.
     * @return Liste des abonnements du fil.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des abonnements.
     */
    public List<Abonnement> findAllByFil(int fno) throws DAOException {
        List<Abonnement> abonnements = new ArrayList<>();
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Abonnement WHERE fno = ?;");
            stmt.setInt(1, fno);
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roleStr = rs.getString("role");
                EnumRole role = EnumRole.valueOf(roleStr);
                abonnements.add(new Abonnement(rs.getString("uno"), rs.getInt("fno"), role));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des abonnements", e);
        }
        return abonnements;
    }

    /**
     * Récupère tous les abonnements d'un utilisateur donné.
     * 
     * @param uno Identifiant de l'utilisateur.
     * @return Liste des abonnements de l'utilisateur.
     * @throws DAOException Si une erreur survient lors de l'opération de
     *                      récupération des abonnements.
     */
    public List<Abonnement> findAllByUtilisateur(String uno) throws DAOException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM Abonnement WHERE uno = ?";

        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, uno);
            System.out.println("AbonnementDAO Requette : " + stmt.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roleStr = rs.getString("role");
                EnumRole role = EnumRole.valueOf(roleStr);
                abonnements.add(new Abonnement(rs.getString("uno"), rs.getInt("fno"), role));
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération des abonnements de l'utilisateur", e);
        }

        return abonnements;
    }

    /**
     * Récupère l'identifiant de l'administrateur d'un fil.
     * 
     * @param fno Identifiant du fil.
     * @return L'identifiant de l'administrateur, ou null si aucun administrateur n'est trouvé.
     * @throws DAOException Si une erreur survient lors de l'opération de récupération de l'administrateur.
     */
    public String findAdmin(int fno) throws DAOException {
        try {
            Connection con = DCM.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT uno FROM Abonnement WHERE fno = ? AND role = ?;");
            stmt.setInt(1, fno);
            stmt.setString(2, EnumRole.ADMIN.toString());
            System.out.println("AbonnementDAO Requette : " + stmt.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("uno");
                return null;
            }
        } catch (SQLException e) {
            e.getLocalizedMessage();
            throw new DAOException("Erreur lors de la récupération de l'admin de ce fil", e);
        }
    }
}
