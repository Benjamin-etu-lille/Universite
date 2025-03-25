package service;

import exceptions.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.EnumRole;
import model.dao.AbonnementDAO;
import model.dao.NotificationDAO;
import model.dto.Abonnement;
import model.dto.Notification;

/**
 * Contrôleur pour la gestion des notifications d'abonnement.
 * <p>
 * Cette classe gère les opérations liées aux invitations d'abonnement pour un utilisateur. Elle permet à l'utilisateur
 * de supprimer ou d'accepter une invitation à un fil de discussion.
 * </p>
 */
public class NotificationService {
    private AbonnementDAO abonnementDAO;
    private NotificationDAO notificationDAO;

    public NotificationService() {
        this.abonnementDAO = new AbonnementDAO();
        this.notificationDAO = new NotificationDAO();
    }

    /**
    * Supprime une invitation d'abonnement pour un utilisateur.
    * <p>
    * Cette méthode récupère le paramètre "notification_fno" de la requête, qui représente le numéro du fil 
    * pour lequel l'invitation doit être supprimée. Si le paramètre est valide, l'abonnement de l'utilisateur 
    * pour ce fil est mis à jour pour supprimer son rôle, et l'utilisateur est redirigé vers la page principale.
    * En cas d'erreur ou si le paramètre est invalide, l'utilisateur est redirigé vers la page d'erreur.
    * </p>
    *
    * @param req L'objet HttpServletRequest contenant les paramètres de la requête.
    * @param session L'objet HttpSession pour accéder aux données de la session utilisateur.
    * @return La page à afficher après l'exécution de la méthode : page principale si succès, page d'erreur si échec.
    */
    public String supprimerInvitation(HttpServletRequest req, HttpSession session) throws DAOException {
        String paramNno = req.getParameter("notification_nno");
    
        try {
            int nno = Integer.parseInt(paramNno);
            Notification notification = notificationDAO.findById(nno);
            notification.setEtat("REFUSEE");
            notificationDAO.update(notification);

            return "WEB-INF/vue/pagePrincipal.jsp";
        } catch (DAOException e) {
            e.printStackTrace();
            throw new DAOException("Une erreur est survenue lors du refue de l'invitation",e);
        }
    }

    /**
    * Supprime une notification.
    * <p>
    * Cette méthode récupère le paramètre "notification_fno" de la requête, qui représente le numéro du fil 
    * pour lequel l'invitation doit être supprimée. Si le paramètre est valide, l'abonnement de l'utilisateur 
    * pour ce fil est mis à jour pour supprimer son rôle, et l'utilisateur est redirigé vers la page principale.
    * En cas d'erreur ou si le paramètre est invalide, l'utilisateur est redirigé vers la page d'erreur.
    * </p>
    *
    * @param req L'objet HttpServletRequest contenant les paramètres de la requête.
    * @param session L'objet HttpSession pour accéder aux données de la session utilisateur.
    * @return La page à afficher après l'exécution de la méthode : page principale si succès, page d'erreur si échec.
    */
    public String supprimernotification(HttpServletRequest req, HttpSession session) throws DAOException {
        String paramNno = req.getParameter("notification_nno");
    
        try {
            int nno = Integer.parseInt(paramNno);
            Notification notification = notificationDAO.findById(nno);
            notification.setEtat("VUE");
            notificationDAO.update(notification);

            return "WEB-INF/vue/pagePrincipal.jsp";
        } catch (DAOException e) {
            e.printStackTrace();
            throw new DAOException("Une erreur est survenue lors du refue de l'invitation",e);
        }
    }

    /**
     * Accepte une invitation à un fil de discussion pour un utilisateur.
     * <p>
     * Cette méthode récupère le paramètre "notification_fno" de la requête, qui représente le numéro du fil 
     * pour lequel l'utilisateur souhaite accepter l'invitation. Si le paramètre est valide, l'abonnement de 
     * l'utilisateur est mis à jour pour lui attribuer le rôle "USER", et l'utilisateur est redirigé vers la page principale.
     * En cas d'erreur ou si le paramètre est invalide, l'utilisateur est redirigé vers la page d'erreur.
     * </p>
     *
     * @param req L'objet HttpServletRequest contenant les paramètres de la requête.
     * @param session L'objet HttpSession pour accéder aux données de la session utilisateur.
     * @return La page à afficher après l'exécution de la méthode : page principale si succès, page d'erreur si échec.
     */
    public String accepterInvitation(HttpServletRequest req, HttpSession session) throws DAOException {
        String paramFno = req.getParameter("notification_fno");
        String paramNno = req.getParameter("notification_nno");
    
        try {
            int fno = Integer.parseInt(paramFno);
            int nno = Integer.parseInt(paramNno);
            String pseudo = (String) session.getAttribute("pseudo");
    
            Abonnement abonnement = new Abonnement(pseudo, fno, EnumRole.INVITER);
            Notification notification = notificationDAO.findById(nno);

            notification.setEtat("ACCEPTEE");
            notificationDAO.update(notification);
            abonnementDAO.save(abonnement);

            // Ne fonctionne pas encore l'objectif est de notifier le propriétaire du file quand l'utilisateur accepte sont infiviattion.
            // AbonnementDAO abonnementDAO = new AbonnementDAO();
            // String unoAdmin = abonnementDAO.findAdmin(fno);
            // Notification notifNouvelleUtilisateurAuFil = new Notification(unoAdmin, fno, "INVITATION");
            // notificationDAO.save(notifNouvelleUtilisateurAuFil);
            
            return "WEB-INF/vue/pagePrincipal.jsp";
        } catch (DAOException e) {
            e.printStackTrace();
            throw new DAOException("Notification services ERREUR lors dans accepterInvitation ",e);
        }
    }

    /**
    * Envoie une invitation à un utilisateur pour rejoindre un fil de discussion.
    * <p>
    * Cette méthode récupère l'identifiant du fil de discussion en cours depuis la session 
    * et l'identifiant de l'utilisateur à inviter depuis les paramètres de la requête.
    * Une notification d'invitation est créée et enregistrée en base de données avec l'état 
    * <b>"EN_ATTENTE"</b>. En cas de succès ou d'échec, un message correspondant est ajouté 
    * à la requête.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    *                Il doit inclure le paramètre <b>"uno"</b>, représentant l'identifiant de l'utilisateur invité.
    * @param session La session HTTP de l'utilisateur, contenant l'attribut <b>"current_fno"</b> 
    *                qui représente l'identifiant du fil de discussion sélectionné.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *           <li><b>"WEB-INF/vue/pagePrincipal.jsp"</b> si l'invitation est envoyée avec succès.</li>
    *           <li><b>"WEB-INF/vue/ajouterUtilisateurAuFil.jsp"</b> si aucun utilisateur n'est sélectionné.</li>
    *         </ul>
    * @throws DAOException Si une erreur survient lors de l'enregistrement de la notification.
    */
    public String inviter(HttpServletRequest req, HttpSession session) throws DAOException{
        try {
            int current_fno = (int) session.getAttribute("current_fno");

            String unoUtilisateur = req.getParameter("uno");
            if (unoUtilisateur == null || unoUtilisateur.isEmpty()) {
                req.setAttribute("errorMessage", "Utilisateur non sélectionné !");
                return "WEB-INF/vue/ajouterUtilisateurAuFil.jsp";
            }

            Notification notification = new Notification(unoUtilisateur,current_fno,"INVITATION");

            
            // Lors de la sauvgarde la l'état de la notification et automatiquement defini a 'EN_ATTENTE'
            if (notificationDAO.save(notification)) {
                req.setAttribute("goodMessage", "Invitation envoyée");
            } else {
                req.setAttribute("errorMessage", "Erreur lors de l'ajout de l'utilisateur au fil !");
            }
        } catch (DAOException e) {
            e.printStackTrace();
            throw new DAOException("Notification services ERREUR lors dans accepterInvitation ",e);
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }
}
