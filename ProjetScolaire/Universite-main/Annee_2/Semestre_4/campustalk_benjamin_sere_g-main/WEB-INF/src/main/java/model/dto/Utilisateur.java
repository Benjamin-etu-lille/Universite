package model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Représente un utilisateur du réseau social.
 * Un utilisateur possède un identifiant unique (uno), un nom, un prénom, une adresse e-mail, un mot de passe et une date de création.
 * Un utilisateur peut créer des fils de discussion, s’abonner à des fils existants et envoyer des messages.
 * @author Benjamin
 */
public class Utilisateur {
    private String uno;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy '-' HH':'mm:ss")
    private LocalDateTime dateCreation;

    public Utilisateur(){} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Utilisateur.
     * 
     * @param uno L'identifiant unique de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur.
     * @param mdp Le mot de passe de l'utilisateur.
     * @param email L'Email de l'utilisateur.
     * @param dateCreation La date de création du compte utilisateur.
     */
    public Utilisateur(String uno, String nom, String prenom, String email, String mdp, LocalDateTime dateCreation) {
        this.uno = uno;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.dateCreation = dateCreation;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Retourne la représentation sous forme de chaîne de caractères de l'utilisateur.
     * 
     * @return Une chaîne représentant l'utilisateur.
     */
    @Override
    public String toString() {
        return "Utilisateur [uno=" + uno + ", nom=" + nom + ", prenom=" + prenom + ", mdp=" + mdp + ", dateCreation="
                + dateCreation + "]";
    }
}