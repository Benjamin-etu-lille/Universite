package model.dto;

import java.time.LocalDateTime;

/**
 * Représente une notification envoyée à un utilisateur concernant un fil de discussion.
 * Une notification peut indiquer différents événements tels qu'une invitation à rejoindre un fil
 * ou l'envoi d'un message dans un fil auquel l'utilisateur est abonné. Elle contient des informations 
 * sur l'utilisateur destinataire, le type de notification, son état et la date d'envoi.
 * 
 * @author Benjamin
 */
public class Notification {
    
    private int nno;            
    private String uno;         
    private int fno;           
    private String type;        
    private String etat;        
    private LocalDateTime dateEnvoi; 

    public Notification(){} // Ne pas enlever Utile pour le ObjectMapper !!

    /**
     * Constructeur de la classe Notification.
     * 
     * @param nno L'identifiant unique de la notification.
     * @param uno L'identifiant de l'utilisateur destinataire de la notification.
     * @param fno L'identifiant du fil concerné par la notification.
     * @param type Le type de notification (INVITATION, MESSAGE).
     * @param etat L'état de la notification ( EN_ATTENTE, ACCEPTEE, REFUSEE).
     * @param dateEnvoi La date d'envoi de la notification.
     */
    public Notification(int nno, String uno, int fno, String type, String etat, LocalDateTime dateEnvoi) {
        this.nno = nno;
        this.uno = uno;
        this.fno = fno;
        this.type = type;
        this.etat = etat;
        this.dateEnvoi = dateEnvoi;
    }

    public Notification(String uno, int fno, String type) {
        this(0, uno, fno, type, null,null);
    }

    public int getNno() {
        return nno;
    }

    public void setNno(int nno) {
        this.nno = nno;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    // Méthode toString pour l'affichage (optionnel)
    @Override
    public String toString() {
        return "Notification{" +
                "nno=" + nno +
                ", uno='" + uno + '\'' +
                ", fno=" + fno +
                ", type='" + type + '\'' +
                ", etat='" + etat + '\'' +
                ", dateEnvoi=" + dateEnvoi +
                '}';
    }
}

