package model.dto;

import java.time.LocalDateTime;

/**
 * Représente un "Like" donné par un utilisateur à un message.
 * Chaque "Like" est identifié par la combinaison de l'identifiant de l'utilisateur et du message.
 * Il possède également une date de création.
 * 
 * @author Benjamin
 */
public class Like {
    private String uno;
    private int mno;
    private LocalDateTime dateLike;

    public Like() {} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Like.
     * 
     * @param uno L'identifiant unique de l'utilisateur.
     * @param mno L'identifiant du message aimé.
     * @param dateLike La date et l'heure du like.
     */
    public Like(String uno, int mno, LocalDateTime dateLike) {
        this.uno = uno;
        this.mno = mno;
        this.dateLike = dateLike;
    }

    public Like(String uno, int mno) {
        this(uno, mno, null);
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getMno() {
        return mno;
    }

    public void setMno(int mno) {
        this.mno = mno;
    }

    public LocalDateTime getDateLike() {
        return dateLike;
    }

    public void setDateLike(LocalDateTime dateLike) {
        this.dateLike = dateLike;
    }

    /**
     * Retourne la représentation sous forme de chaîne de caractères du like.
     * 
     * @return Une chaîne représentant le like.
     */
    @Override
    public String toString() {
        return "Like [uno=" + uno + ", mno=" + mno + ", dateLike=" + dateLike + "]";
    }
}
