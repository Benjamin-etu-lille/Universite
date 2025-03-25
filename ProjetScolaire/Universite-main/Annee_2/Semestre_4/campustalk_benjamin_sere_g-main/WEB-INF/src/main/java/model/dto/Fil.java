package model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Représente un fil de discussion.
 * Un fil de discussion possède un identifiant unique (fno), un titre, une description et une date de création.
 * Un fil peut être suivi par plusieurs utilisateurs.
 * @author Benjamin
 */
public class Fil {
    private int fno;
    private String titre;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy '-' HH':'mm:ss")
    private LocalDateTime dateCreation;

    public Fil(){} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Fil.
     * 
     * @param fno L'identifiant unique du fil de discussion.
     * @param titre Le titre du fil de discussion.
     * @param description La description du fil de discussion.
     * @param dateCreation La date de création du fil de discussion.
     */
    public Fil(int fno, String titre, String description, LocalDateTime dateCreation) {
        this.fno = fno;
        this.titre = titre;
        this.description = description;
        this.dateCreation = dateCreation;
    }

    public Fil(String titre, String description) {
        this(0, titre, description, null);
    }

    public int getFno() {
        return fno;
    }

    public void setFno(int fno) {
        this.fno = fno;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Retourne la représentation sous forme de chaîne de caractères du fil de discussion.
     * 
     * @return Une chaîne représentant le fil de discussion.
     */
    @Override
    public String toString() {
        return "Fil [fno=" + fno + ", titre=" + titre + ", description=" + description + ", dateCreation="
                + dateCreation + "]";
    }
}