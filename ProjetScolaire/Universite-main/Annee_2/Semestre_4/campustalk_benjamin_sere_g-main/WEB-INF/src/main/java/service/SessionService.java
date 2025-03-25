package service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UtilisateurDAO;
import model.dto.Utilisateur;

/**
 * Classe contrôleur responsable de la gestion des sessions utilisateur.
 * <p>
 * La classe gère l'authentification, l'inscription et la déconnexion des utilisateurs. Elle communique avec le
 * {@link UtilisateurDAO} pour effectuer des opérations sur les utilisateurs, et elle gère les sessions
 * HTTP pour suivre l'état de connexion des utilisateurs.
 * </p>
 */
public class SessionService {
    private UtilisateurDAO utilisateurDAO;

    public SessionService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
    * Gère l'authentification d'un utilisateur.
    * <p>
    * Cette méthode vérifie les informations de connexion (pseudo et mot de passe).
    * Si l'authentification réussit :
    * <ul>
    *   <li>Le pseudo est stocké dans la session.</li>
    *   <li>Un cookie est créé pour mémoriser le pseudo pendant 30 jours.</li>
    *   <li>L'utilisateur est redirigé vers la page principale.</li>
    * </ul>
    * En cas d'échec (pseudo ou mot de passe incorrect), l'utilisateur est redirigé
    * vers la page de connexion avec un message d'erreur.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    *                Il doit inclure les paramètres <b>"pseudo"</b> et <b>"password"</b>.
    * @param session La session HTTP associée à l'utilisateur.
    *                <ul>
    *                  <li>Si l'authentification réussit, l'attribut <b>"pseudo"</b> est ajouté à la session.</li>
    *                  <li>Aucune modification de session en cas d'échec.</li>
    *                </ul>
    * @param resp    L'objet {@link HttpServletResponse} utilisé pour ajouter un cookie de mémorisation du pseudo.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *           <li><b>"WEB-INF/vue/pagePrincipal.jsp"</b> si l'authentification réussit.</li>
    *           <li><b>"WEB-INF/vue/pageConnection.jsp"</b> en cas d'échec, avec un message d'erreur.</li>
    *         </ul>
    */
    public String seConnecter(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        try {
            String pseudo = req.getParameter("pseudo");
            String password = req.getParameter("password");
    
            if (pseudo == null || pseudo.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                req.setAttribute("errorMessage", "Le pseudo et le mot de passe sont requis.");
                return "WEB-INF/vue/pageConnection.jsp";
            }
    
            Utilisateur utilisateur = utilisateurDAO.findById(pseudo);
            if (utilisateur != null && utilisateur.getMdp().equals(password)) {
                session.setAttribute("pseudo", pseudo);
    
                Cookie cookie = new Cookie("pseudo", pseudo);
                cookie.setMaxAge(60 * 60 * 24 * 30); // Durée de vie 30 on va pas abusée quand meme 
                cookie.setPath("/");
                resp.addCookie(cookie);
    
                return "WEB-INF/vue/pagePrincipal.jsp";
            } else {
                req.setAttribute("errorMessage", "Pseudo ou mot de passe incorrect !");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Une erreur est survenue lors de la connexion.");
        }
        return "WEB-INF/vue/pageConnection.jsp";
    }
    
    

    /**
    * Gère l'inscription d'un nouvel utilisateur.
    * <p>
    * Cette méthode vérifie les informations fournies par l'utilisateur, s'assure que le pseudo
    * n'est pas déjà utilisé, enregistre le nouvel utilisateur en base de données et met à jour
    * la session ainsi qu'un cookie pour mémoriser le pseudo.
    * En cas d'erreur (champs vides, pseudo déjà pris ou exception), un message d'erreur est ajouté
    * à la requête et l'utilisateur est redirigé vers la page d'inscription.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    *                Il doit inclure les paramètres <b>"pseudo"</b>, <b>"nom"</b>, <b>"prenom"</b>,
    *                <b>"email"</b> et <b>"mdp"</b>.
    * @param session La session HTTP associée à l'utilisateur.
    *                <ul>
    *                  <li>Si l'inscription réussit, l'attribut <b>"pseudo"</b> est ajouté à la session.</li>
    *                  <li>Aucune modification de session en cas d'échec.</li>
    *                </ul>
    * @param resp    L'objet {@link HttpServletResponse} utilisé pour ajouter un cookie mémorisant le pseudo.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *           <li><b>"WEB-INF/vue/pagePrincipal.jsp"</b> si l'inscription réussit.</li>
    *           <li><b>"WEB-INF/vue/pageRegister.jsp"</b> en cas d'échec, avec un message d'erreur.</li>
    *         </ul>
    */
    public String sInscrire(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        try {
            String pseudo = req.getParameter("pseudo");
            String nom = req.getParameter("nom");
            String prenom = req.getParameter("prenom");
            String email = req.getParameter("email");
            String mdp = req.getParameter("mdp");
    
            if (pseudo == null || pseudo.trim().isEmpty() || nom == null || prenom == null || email == null || mdp == null) {
                req.setAttribute("errorMessage", "Tous les champs doivent être remplis.");
                return "WEB-INF/vue/pageRegister.jsp";
            }
    
            if (utilisateurDAO.findById(pseudo) != null) {
                req.setAttribute("errorMessage", "Ce pseudo est déjà pris !");
                return "WEB-INF/vue/pageRegister.jsp";
            }
    
            Utilisateur newUser = new Utilisateur(pseudo, nom, prenom, email, mdp, null);
            utilisateurDAO.save(newUser);
    
            session.setAttribute("pseudo", pseudo);
    
            Cookie cookie = new Cookie("pseudo", pseudo);
            cookie.setMaxAge(60 * 60 * 24 * 30); // Durée de vie 30 on va pas abusée quand meme 
            cookie.setPath("/");
            res.addCookie(cookie);
    
            return "WEB-INF/vue/pagePrincipal.jsp";
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Une erreur est survenue lors de l'inscription.");
        }
        return "WEB-INF/vue/pageRegister.jsp";
    }
    

    /**
    * Gère la déconnexion de l'utilisateur.
    * <p>
    * Cette méthode invalide la session en cours, si elle existe, et supprime le cookie
    * contenant le pseudo de l'utilisateur. L'utilisateur est ensuite redirigé vers la
    * page principale.
    * </p>
    *
    * @param req L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    * @param res L'objet {@link HttpServletResponse} utilisé pour supprimer le cookie de l'utilisateur.
    * @return La page vers laquelle rediriger l'utilisateur après la déconnexion 
    *         (<b>"WEB-INF/vue/pagePrincipal.jsp"</b>).
    */
    public String deconnexion(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie deleteCookie = new Cookie("pseudo", "");
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        res.addCookie(deleteCookie);
        return "WEB-INF/vue/pagePrincipal.jsp";
    }

    /**
    * Charge le fil de discussion sélectionné et met à jour la session.
    * <p>
    * Cette méthode récupère l'identifiant du fil de discussion depuis les paramètres
    * de la requête, le convertit en entier et l'ajoute à la session sous l'attribut 
    * <b>"current_fno"</b>. Si une erreur survient (par exemple, un paramètre invalide), 
    * l'utilisateur est redirigé vers une page d'erreur.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    *                Il doit inclure le paramètre <b>"current_fno"</b>, représentant l'identifiant du fil.
    * @param session La session HTTP de l'utilisateur, mise à jour avec l'identifiant du fil sélectionné.
    * @return La page vers laquelle rediriger l'utilisateur :
    *         <ul>
    *           <li><b>"WEB-INF/vue/pagePrincipal.jsp"</b> si l'opération réussit.</li>
    *           <li><b>"WEB-INF/vue/ErrorPage.jsp"</b> en cas d'erreur.</li>
    *         </ul>
    */
    public String chargerFilSelectionner(HttpServletRequest req, HttpSession session) {
        try {
            String parametre = req.getParameter("current_fno");
            int current_fno = Integer.valueOf(parametre);

            session.setAttribute("current_fno", current_fno);
            return "WEB-INF/vue/pagePrincipal.jsp";
        }catch(Exception e) {
            e.getStackTrace();
        }
        return "WEB-INF/vue/ErrorPage.jsp";
    }
}
