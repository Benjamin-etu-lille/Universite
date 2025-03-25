package model;

/**
 * Enumération représentant les rôles d'un utilisateur dans l'application.
 * Cette énumération définit les différents rôles qu'un utilisateur peut avoir
 * et permet de gérer les permissions et les actions associées à chaque rôle.
 */
public enum EnumRole {
    /**
     * Rôle représentant un utilisateur normal, avec des permissions standard.
     */
    USER, 
    
    /**
     * Rôle représentant un administrateur, avec des permissions étendues pour gérer l'application.
     */
    ADMIN, 
    
    /**
     * Rôle représentant un utilisateur banni, avec des permissions limitées.
     */
    BANNI, 
    
    /**
     * Rôle représentant un utilisateur invité, avec des permissions limitées et souvent temporaire.
     */
    INVITER;
}
