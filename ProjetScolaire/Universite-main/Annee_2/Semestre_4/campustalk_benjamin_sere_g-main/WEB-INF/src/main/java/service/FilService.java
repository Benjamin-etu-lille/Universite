package service;

import java.util.ArrayList;
import java.util.List;

import exceptions.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.EnumRole;
import model.dao.AbonnementDAO;
import model.dao.FilDAO;
import model.dto.Abonnement;
import model.dto.Fil;

/**
 * Contrôleur pour la gestion des fils de discussion et des abonnements des utilisateurs.
 * <p>
 * Cette classe gère les opérations relatives aux fils de discussion, telles que la création d'un fil, 
 * l'ajout d'un utilisateur à un fil, la récupération des fils auxquels un utilisateur est abonné, 
 * et la gestion des messages associés à ces fils. Elle interagit avec les couches de données pour 
 * manipuler les objets {@link Fil} et {@link Abonnement}.
 * </p>
 */

public class FilService{
    private FilDAO filDAO;
    private AbonnementDAO abonnementDAO;

    public FilService() {
        this.filDAO = new FilDAO();
        this.abonnementDAO = new AbonnementDAO();
    }

    /**
    * Crée un nouveau fil de discussion et abonne automatiquement l'utilisateur en tant qu'administrateur.
    * <p>
    * Cette méthode récupère le titre et la description du fil depuis la requête. 
    * Si ces informations sont valides, un nouvel objet {@link Fil} est créé et sauvegardé dans la base de données. 
    * Ensuite, l'utilisateur qui crée le fil est automatiquement inscrit en tant qu'administrateur du fil.
    * </p>
    * <p>
    * En cas d'erreur ou de champs vides, un message d'erreur est affiché et l'utilisateur reste sur la page de création du fil.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    * @param session L'objet {@link HttpSession} contenant les informations de session, notamment le pseudo de l'utilisateur.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *             <li>Si la création du fil réussit, redirige vers <b>"pagePrincipal.jsp"</b>.</li>
    *             <li>Si les champs sont vides, redirige vers <b>"pageCreeUnFil.jsp"</b> avec un message d'erreur.</li>
    *         </ul>
    */
    public String creerFil(HttpServletRequest req, HttpSession session) {
        String titre = req.getParameter("titre");
        String description = req.getParameter("description");

        if (titre == null || titre.trim().isEmpty() || description == null || description.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Le titre et la description sont obligatoires.");
            return "WEB-INF/vue/pageCreeUnFil.jsp";
        }

        Fil fil = new Fil(titre, description);
        try {
            int current_fno = filDAO.save(fil);
            String pseudo = (String) session.getAttribute("pseudo");

            Abonnement abonnement = new Abonnement(pseudo, current_fno, EnumRole.ADMIN);
            abonnementDAO.save(abonnement);
            
            req.setAttribute("goodMessage", "Fil crée avec succes.");
        } catch (DAOException e) {
            e.printStackTrace(); 
            req.setAttribute("errorMessage", "Erreur lors de la création du fil.");
            return "WEB-INF/vue/pageCreeUnFil.jsp";
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }

    /**
     * Ajoute un utilisateur à un fil de discussion en tant qu'ADMIN.
     * <p>
     * Cette méthode récupère le numéro du fil (fno) depuis la session ainsi que le pseudo du créateur du fil.
     * L'utilisateur à ajouter est sélectionné via un paramètre de la requête.
     * </p>
     * <p>
     * Si l'ajout est réussi, un message de succès est défini dans la requête. 
     * En cas d'erreur (utilisateur non connecté, pseudo non fourni, ou problème d'insertion en base), un message d'erreur est affiché.
     * </p>
     *
     * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
     * @param session L'objet {@link HttpSession} contenant les informations de session, notamment le fil en cours et l'utilisateur connecté.
     * @return La page vers laquelle rediriger l'utilisateur :
     *         <ul>
     *             <li>Si l'utilisateur n'est pas connecté, redirige vers <b>"pageConnection.jsp"</b>.</li>
     *             <li>Si aucun utilisateur n'est sélectionné, redirige vers <b>"ajouterUtilisateurAuFil.jsp"</b> avec un message d'erreur.</li>
     *             <li>Si l'ajout réussit, redirige vers <b>"pagePrincipal.jsp"</b> avec un message de confirmation.</li>
     *         </ul>
     */
    public String ajouterUtilisateurAuFil(HttpServletRequest req, HttpSession session) {
        try {
            int current_fno = (int) session.getAttribute("current_fno");
            String pseudoInviter = req.getParameter("uno");

            if (pseudoInviter == null || pseudoInviter.isEmpty()) {
                req.setAttribute("errorMessage", "Utilisateur non sélectionné !");
                return "WEB-INF/vue/ajouterUtilisateurAuFil.jsp";
            }
            Abonnement abonnement = new Abonnement(pseudoInviter, current_fno, EnumRole.ADMIN);
            abonnementDAO.save(abonnement);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Une erreur inattendue est survenue.");
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }

    /**
     * Supprime un fil de discussion et le retire de la session.
     * <p>
     * Cette méthode récupère le numéro du fil depuis la session et supprime le fil de la base de données.
     * Si la suppression est réussie, un message de confirmation est affiché à l'utilisateur.
     * </p>
     *
     * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
     * @param session L'objet {@link HttpSession} contenant les informations de session.
     * @return La page vers laquelle rediriger l'utilisateur après la suppression du fil.
     */
    public String supprimerFil(HttpServletRequest req, HttpSession session) {
        try {
            int current_fno = (int) session.getAttribute("current_fno");
            Fil current_fil = filDAO.findById(current_fno);
            
            if ( filDAO.delete(current_fno)) {
                req.setAttribute("goodMessage", "Vous avez supprimer le fil : "+current_fil.getTitre());
            } else {
                req.setAttribute("errorMessage", "Erreur lors de la suppression du fil");
            }
            session.setAttribute("current_fno", null);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Une erreur inattendue est survenue.");
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }

    /**
    * Récupère la liste des fils de discussion auxquels un utilisateur est abonné.
    * <p>
    * Cette méthode prend un pseudo utilisateur et charge tous les fils associés à ce pseudo en parcourant 
    * les abonnements dans la base de données. Elle renvoie une liste des objets {@link Fil} représentant 
    * les fils auxquels l'utilisateur est abonné.
    * </p>
    * <p>
    * Si le pseudo est invalide ou si aucun abonnement n'est trouvé pour l'utilisateur, la méthode renvoie 
    * une liste vide. Les erreurs lors de la récupération des abonnements ou des fils sont propagées sous forme 
    * de {@link DAOException}.
    * </p>
    *
    * @param pseudo Le pseudo de l'utilisateur pour lequel les abonnements doivent être récupérés.
    * @return La liste des fils de discussion auxquels l'utilisateur est abonné. Si l'utilisateur n'est abonné 
    *         à aucun fil, une liste vide est renvoyée.
    * @throws DAOException Si une erreur survient lors de l'accès aux données de l'utilisateur ou des fils.
    */
    public List<Fil> getFilsUtilisateur(String pseudo) throws DAOException {

        if (pseudo == null || pseudo.trim().isEmpty()) {
            throw new DAOException("Pseudo utilisateur invalide : " + pseudo);
        }
        List<Fil> fils = new ArrayList<>();
        try {

            List<Abonnement> abonnements = abonnementDAO.findAllByUtilisateur(pseudo);
            if (abonnements == null || abonnements.isEmpty()) {
                return fils; 
            }
            for (Abonnement abonnement : abonnements) {
                Fil fil = filDAO.findById(abonnement.getFno());
                if (fil != null) {
                    fils.add(fil);
                }
            }
        } catch (DAOException e) {
            throw e;
        }
        return fils;
    }

    /**
     * Permet à un utilisateur de quitter un fil de discussion.
     * <p>
     * Cette méthode récupère le numéro du fil depuis la session et supprime l'abonnement de l'utilisateur à ce fil.
     * Un message de confirmation est affiché si la suppression est réussie.
     * </p>
     *
     * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
     * @param session L'objet {@link HttpSession} contenant les informations de session.
     * @return La page vers laquelle rediriger l'utilisateur après avoir quitté le fil.
     */
    public String quitterFil(HttpServletRequest req, HttpSession session) {
        try {
            int current_fno = (int) session.getAttribute("current_fno");
            String uno = (String) session.getAttribute("pseudo");
            Fil current_fil = filDAO.findById(current_fno);
            
            if (abonnementDAO.delete(uno, current_fno)) {
                req.setAttribute("goodMessage", "Vous avez quitter le fil : "+current_fil.getTitre());
            } else {
                req.setAttribute("errorMessage", "Erreur lors de la tentative de quitter fil");
            }
            session.setAttribute("current_fno", null);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Une erreur inattendue est survenue.");
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }

    /**
    * Permet de bannir un utilisateur d'un fil de discussion.
    * <p>
    * Cette méthode récupère les paramètres nécessaires à l'exécution de l'action de bannissement depuis la requête HTTP,
    * à savoir le numéro du fil et le pseudo de l'utilisateur à bannir. Elle effectue la suppression de l'abonnement de l'utilisateur
    * à ce fil et affiche un message de confirmation en cas de succès, ou un message d'erreur en cas d'échec.
    * </p>
    * <p>
    * Une fois l'action effectuée, la session de l'utilisateur est mise à jour pour supprimer l'attribut représentant le numéro 
    * du fil courant.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP, incluant le numéro du fil 
    *                et le pseudo de l'utilisateur à bannir.
    * @param session L'objet {@link HttpSession} contenant les informations de session, notamment le numéro du fil courant.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *             <li>Si l'utilisateur a été banni avec succès, redirige vers <b>"pagePrincipal.jsp"</b> avec un message de confirmation.</li>
    *             <li>Si une erreur survient lors de l'opération, redirige vers <b>"pagePrincipal.jsp"</b> avec un message d'erreur.</li>
    *         </ul>
    */
    public String bannireDuFil(HttpServletRequest req, HttpSession session) {
        try {
            String param = req.getParameter("current_fno");
            int current_fno = Integer.parseInt(param);
            String uno = req.getParameter("pseudo");
            Fil current_fil = filDAO.findById(current_fno);
            session.setAttribute("fno_banni", current_fno);
            
            if (abonnementDAO.delete(uno, current_fno)) {
                req.setAttribute("goodMessage", "Vous avez banni du fil "+current_fil.getTitre()+" : "+uno);
            } else {
                req.setAttribute("errorMessage", "Erreur lors de la tentative de bannisement");
            }
            session.setAttribute("current_fno", null);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Une erreur inattendue est survenue.");
        }

        return "WEB-INF/vue/pagePrincipal.jsp";
    }


}
