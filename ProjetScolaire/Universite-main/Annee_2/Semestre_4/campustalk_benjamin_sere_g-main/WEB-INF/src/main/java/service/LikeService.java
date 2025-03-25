package service;

import java.io.IOException;

import exceptions.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.LikeDAO;
import model.dao.MessageDAO;
import model.dao.NotificationDAO;
import model.dto.Like;
import model.dto.Message;
import model.dto.Notification;

/**
 * Service pour gérer les likes des messages.
 * <p>
 * Cette classe gère l'ajout, la suppression de likes pour les messages. 
 * Elle permet d'ajouter ou de retirer un like d'un message et de mettre à jour le nombre de likes associés.
 * </p>
 */
public class LikeService {
    private LikeDAO likeDAO;

    public LikeService() {
        this.likeDAO = new LikeDAO();
    }

    /**
     * Ajoute un like à un message pour un utilisateur.
     * <p>
     * Cette méthode récupère les paramètres nécessaires de la requête, comme l'ID du message et l'utilisateur, 
     * puis ajoute un like ou le retire si l'utilisateur a déjà aimé ce message. 
     * </p>
     *
     * @param req L'objet HttpServletRequest contenant les paramètres de la requête.
     * @param session L'objet HttpSession pour accéder aux données de la session utilisateur.
     * @return La page à afficher après l'exécution de la méthode : page principale si succès, page d'erreur si échec.
     * @throws IOException 
     */
    public String ajouterLike(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws DAOException, IOException {
    String paramMno = req.getParameter("message_mno");
    String pseudo = (String) session.getAttribute("pseudo");

    try {
        int mno = Integer.parseInt(paramMno);
        boolean existeDeja = likeDAO.exists(pseudo, mno);

        if (existeDeja) {
            likeDAO.delete(pseudo, mno);
        } else {
            Like like = new Like(pseudo, mno);
            likeDAO.save(like);

            Message message = new MessageDAO().findById(mno); 
            String auteurMessage = message.getUno();

            if (!pseudo.equals(auteurMessage)) {
                Notification notification = new Notification();
                notification.setUno(auteurMessage);
                notification.setFno(message.getFno());  
                notification.setType("LIKE");
                notification.setEtat("EN_ATTENTE");
                
                NotificationDAO notificationDAO = new NotificationDAO();
                notificationDAO.save(notification);
            }
        }

        res.sendRedirect("Controleur?action=accesPagePrincipal"); 
    } catch (DAOException e) {
        e.printStackTrace();
        throw new DAOException("Une erreur est survenue lors de l'ajout du like", e);
    }
    return null;
}


    /**
     * Supprime un like d'un message pour un utilisateur.
     * <p>
     * Cette méthode récupère les paramètres nécessaires de la requête et supprime un like si l'utilisateur 
     * a déjà aimé ce message.
     * </p>
     *
     * @param req L'objet HttpServletRequest contenant les paramètres de la requête.
     * @param session L'objet HttpSession pour accéder aux données de la session utilisateur.
     * @return La page à afficher après l'exécution de la méthode : page principale si succès, page d'erreur si échec.
     */
    public String supprimerLike(HttpServletRequest req, HttpSession session) throws DAOException {
        String paramMno = req.getParameter("message_mno");
        String pseudo = (String) session.getAttribute("pseudo");

        try {
            int mno = Integer.parseInt(paramMno);
            boolean existeDeja = likeDAO.exists(pseudo, mno);
            if (existeDeja) {
                likeDAO.delete(pseudo, mno);
            }
            return "WEB-INF/vue/pagePrincipal.jsp";
        } catch (DAOException e) {
            e.printStackTrace();
            throw new DAOException("Une erreur est survenue lors de la suppression du like", e);
        }
    }
}
