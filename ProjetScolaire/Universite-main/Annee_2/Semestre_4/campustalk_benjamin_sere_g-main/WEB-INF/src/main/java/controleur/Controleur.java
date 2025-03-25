package controleur;

import java.io.IOException;
import java.util.Enumeration;

import exceptions.DAOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DCM;
import service.FilService;
import service.LikeService;
import service.MessageService;
import service.NotificationService;
import service.SessionService;
import service.UtilisateurService;

/**
 * Contrôleur principal du système de gestion des requêtes HTTP pour l'application.
 * <p>
 * Cette classe est responsable de l'acheminement des requêtes vers les différents contrôleurs (sessions, fils, messages, notifications).
 * Elle intercepte les actions demandées par l'utilisateur, exécute l'action correspondante, et redirige l'utilisateur vers la vue appropriée.
 * Le contrôleur utilise des services pour la gestion des utilisateurs, des fils de discussion, des messages et des notifications.
 * </p>
 */
@WebServlet(urlPatterns = {"/Controleur"}, loadOnStartup = 1)
@MultipartConfig(maxFileSize = 1000000, maxRequestSize = 10000000, fileSizeThreshold = 10240) // Obligatoire pour le gestion des images 
public class Controleur extends HttpServlet {
    
    private SessionService sessionService;
    private FilService filService;
    private MessageService messageService;
    private NotificationService notificationService;
    private LikeService likeService;
    private UtilisateurService utilisateurService;

    @Override
    public void init() {
        sessionService = new SessionService();
        filService = new FilService();
        messageService = new MessageService();
        notificationService = new NotificationService();
        likeService = new LikeService();
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            traiterRequete(req, res);
        } catch (DAOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void traiterRequete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, DAOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        String pseudoSession = (String) session.getAttribute("pseudo");

        if (pseudoSession != null && !pseudoSession.isEmpty()) {
            action = (action == null) ? "accesPagePrincipal" : action;
        } else {
            action = (action == null) ? "allerAccueille" : action;
        }
    
        String vue = "WEB-INF/vue/ErrorPage.jsp";
        try {
            vue = executerAction(action, req, session, res);
            if (vue != null) {
                System.out.println("Vue selectionner : " + vue);
                req.getRequestDispatcher(vue).forward(req, res);
            }
        } finally {
            DCM.closeConnection();
        }
        
    }

    private String executerAction(String action, HttpServletRequest req, HttpSession session, HttpServletResponse res) throws ServletException, IOException, DAOException {
        switch (action) {
            case "allerAccueille":
                return "WEB-INF/vue/pageAccueille.jsp";
            case "accesPagePrincipal":
                return "WEB-INF/vue/pagePrincipal.jsp";
            case "vueDeProfil":
                return "WEB-INF/vue/pageVuDeProfil.jsp";
            case "voirProfilUtilisateurFil":
                return "WEB-INF/vue/pageProfilUtilisateurFil.jsp";
            case "modifierProfil":
                return utilisateurService.modifierProfil(req,session);
            case "creerFil":
                return filService.creerFil(req, session);
            case "supprimerFil":
                return filService.supprimerFil(req, session);
            case "quitterFil":
                return filService.quitterFil(req, session);
            case "bannireDuFil":
                return filService.bannireDuFil(req,session);
            case "ajouterUtilisateurAuFil":
                return filService.ajouterUtilisateurAuFil(req, session);
            case "seConnecter":
                return sessionService.seConnecter(req, res, session);
            case "sInscrire":
                return sessionService.sInscrire(req, res, session);
            case "deconnexion":
                return sessionService.deconnexion(req, res);
            case "chargerFilSelectionner":
                return sessionService.chargerFilSelectionner(req, session);
            case "envoyerMessage":
                return messageService.envoyerMessage(req, res, session);
            case "ajouterLike":
                return likeService.ajouterLike(req, res, session);
            case "supprimerLike":
                return likeService.supprimerLike(req, session);
            case "supprimerNotification":
                return notificationService.supprimerInvitation(req, session);
            case "envoyerInvitation":
                return notificationService.inviter(req, session);
            case "accepterInvitation":
                return notificationService.accepterInvitation(req, session);
            case "supprimerInvitation":
                return notificationService.supprimerInvitation(req, session);
            default:
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Action non supportée");
                return null;
        }
    }

    public static void clear() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
