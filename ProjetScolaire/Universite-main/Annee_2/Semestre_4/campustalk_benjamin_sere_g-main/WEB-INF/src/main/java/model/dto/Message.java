package model.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Représente un message envoyé par un utilisateur dans un fil de discussion.
 * Un message possède un identifiant unique (mno), un contenu, une date d'envoi,
 * et est lié à un utilisateur (uno) et à un fil de discussion (fno).
 * @author Benjamin
 */
public class Message {
    private int mno;
    private String contenu;
    private String imagePath;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy '-' HH':'mm:ss")
    private LocalDateTime dateEnvoi;
    private String uno;
    private int fno;

    public Message(){} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Message.
     * 
     * @param mno L'identifiant unique du message.
     * @param contenu Le contenu du message.
     * @param imagePath liens d'une image.
     * @param dateEnvoi La date d'envoi du message.
     * @param uno L'identifiant unique de l'utilisateur.
     * @param fno L'identifiant du fil de discussion.
     */
    public Message(int mno, String contenu, String imagePath, LocalDateTime dateEnvoi, String uno, int fno) {
        this.mno = mno;
        this.contenu = contenu;
        this.imagePath = imagePath;
        this.dateEnvoi = dateEnvoi;
        this.uno = uno;
        this.fno = fno;
    }

    // Constructeur pour une messages avec une images
    public Message(String contenu, String imagePath, String uno, int fno) {
        this(0, contenu, imagePath, null, uno, fno);
    }

    // Constucteur pour un messages sans images 
    public Message(String contenu, String uno, int fno) {
        this(0, contenu,null, null, uno, fno);
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
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

    /**
     * Retourne une représentation lisible de la date d'envoi pour l'utilisateur.
     * 
     * @return Une chaîne représentant la date d'envoi sous un format lisible.
     */
    public String getDateVisuelle() {
        if (dateEnvoi == null) {
            return "Date non disponible";
        }
        LocalDateTime now = LocalDateTime.now();
        long daysDifference = ChronoUnit.DAYS.between(dateEnvoi, now);
        if (daysDifference == 0) {
            return dateEnvoi.format(DateTimeFormatter.ofPattern("HH:mm"));
        }else if (daysDifference == 1) {
            return "Hier à " + dateEnvoi.format(DateTimeFormatter.ofPattern("HH:mm"));
        }else if (daysDifference < 365) {
            return dateEnvoi.format(DateTimeFormatter.ofPattern("dd MMM à HH:mm"));
        }else {
            return dateEnvoi.format(DateTimeFormatter.ofPattern("dd MMM yyyy à HH:mm"));
        }
    }

    /**
     * Retourne la représentation sous forme de chaîne de caractères du message.
     * 
     * @return Une chaîne représentant le message.
     */
    @Override
    public String toString() {
        return "Message [mno=" + mno + ", contenu=" + contenu + ", dateEnvoi=" + dateEnvoi + ", uno=" + uno + ", fno="
                + fno + "]";
    }
}