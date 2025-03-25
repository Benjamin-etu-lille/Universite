package model.dto;

import model.EnumRole;

/**
 * Représente un abonnement d'un utilisateur à un fil de discussion.
 * Un abonnement possède un rôle spécifique (ADMIN, MEMBRE, BANNI) pour l'utilisateur
 * dans un fil de discussion donné.
 * @author Benjamin 
 */
public class Abonnement {
    private String uno;
    private int fno;
    private EnumRole role;

    public Abonnement(){} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Abonnement.
     * 
     * @param uno L'identifiant unique de l'utilisateur.
     * @param fno L'identifiant du fil de discussion.
     * @param role Le rôle de l'utilisateur dans le fil de discussion.
     */
    public Abonnement(String uno, int fno, EnumRole role) {
        this.uno = uno;
        this.fno = fno;
        this.role = role;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getFno() {
        return fno;
    }

    public void setFno(int fno) {
        this.fno = fno;
    }

    public EnumRole getRole() {
        return role;
    }

    public void setRole(EnumRole role) {
        this.role = role;
    }

    /**
     * Retourne la représentation sous forme de chaîne de caractères de l'abonnement.
     * 
     * @return Une chaîne représentant l'abonnement.
     */
    @Override
    public String toString() {
        return "Abonnement [uno=" + uno + ", fno=" + fno + ", role=" + role + "]";
    }

}
