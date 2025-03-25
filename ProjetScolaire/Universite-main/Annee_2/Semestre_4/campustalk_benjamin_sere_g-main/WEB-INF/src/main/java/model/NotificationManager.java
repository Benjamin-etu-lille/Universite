package model;

import java.util.List;
import exceptions.DAOException;
import jakarta.servlet.http.HttpSession;
import model.dao.NotificationDAO;
import model.dto.Notification;

/**
 * Gestionnaire des notifications liées à un utilisateur.
 * Cette classe permet de récupérer les notifications des fils de discussion et des pages associées à un utilisateur.
 * Elle interagit avec la couche de données pour récupérer les notifications pertinentes à afficher.
 */
public class NotificationManager {
    private final HttpSession session;

    public NotificationManager(HttpSession session) throws DAOException {
        this.session = session;
    }

    /**
     * Récupère les notifications en fonction de l'état actuel de l'utilisateur.
     * Cette méthode choisit de récupérer soit les notifications associées à un fil spécifique,
     * soit les notifications générales de l'utilisateur, en fonction des informations de session.
     *
     * @return Liste des notifications associées à l'utilisateur. Si l'utilisateur n'est pas connecté ou si
     *         aucun fil n'est sélectionné, une liste vide est retournée.
     * @throws DAOException En cas d'erreur lors de la récupération des notifications.
     */
    public List<Notification> getNotifications() throws DAOException {
        Integer fno = (Integer) session.getAttribute("current_fno");
        String pseudo = (String) session.getAttribute("pseudo");

        if (pseudo != null && fno != null){
            return getFilNotifications(fno, pseudo);
        }
        return null;
    }

    /**
     * Récupère les notifications liées à un fil de discussion spécifique.
     * Cette méthode est appelée lorsque l'utilisateur est abonné à un fil spécifique, et que des notifications
     * associées à ce fil doivent être récupérées.
     *
     * @param fno    Identifiant du fil de discussion.
     * @param pseudo Pseudo de l'utilisateur.
     * @return Liste des notifications liées au fil de discussion.
     * @throws DAOException En cas d'erreur lors de l'accès aux données.
     */
    private List<Notification> getFilNotifications(Integer fno, String pseudo) throws DAOException {
        try {
            NotificationDAO notificationDAO = new NotificationDAO();
            return notificationDAO.getNotificationsFil(fno, pseudo);
        } catch (DAOException e) {
            throw new DAOException("Erreur lors de la récupération des notifications dans getFilNotifications : " + e.getLocalizedMessage(), e);
        }
    }


    /**
     * Récupère les invitations envoyées à l'utilisateur.
     * Cette méthode est utilisée pour récupérer les notifications d'invitations à rejoindre des fils de discussion.
     *
     * @param pseudo Pseudo de l'utilisateur.
     * @return Liste des invitations envoyées à l'utilisateur.
     * @throws DAOException En cas d'erreur lors de l'accès aux données.
     */
    public List<Notification> getInvitations(String pseudo) throws DAOException{
        try {
            NotificationDAO notificationDAO = new NotificationDAO();
            return notificationDAO.getInvitUtilisateurList(pseudo);
        } catch (DAOException e) {
            throw new DAOException("Erreur lors de la récupération des notifications dans getPageNotification : " + e.getLocalizedMessage(), e);
        }
    }
}