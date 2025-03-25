package service;

import model.dao.UtilisateurDAO;
import model.dto.Utilisateur;
import exceptions.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UtilisateurService {
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    /**
     * Modifie le profil de l'utilisateur.
     * 
     * @param req     L'objet HttpServletRequest contenant les informations du formulaire.
     * @param session L'objet HttpSession contenant la session de l'utilisateur connecté.
     * @throws DAOException Si une erreur survient lors de la mise à jour dans la base de données.
     * @return Le chemin de la page à afficher après la mise à jour.
     */
    public String modifierProfil(HttpServletRequest req, HttpSession session) throws DAOException {
        String pseudo = (String) session.getAttribute("pseudo");
        Utilisateur utilisateur = utilisateurDAO.findById(pseudo);
        
        if (utilisateur == null) {
            throw new DAOException("Utilisateur non trouvé.");
        }
        String nouveauPseudo = req.getParameter("uno");
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String email = req.getParameter("email");
        String mdp = req.getParameter("mdp");
    
        if (nouveauPseudo != null && !nouveauPseudo.equals(utilisateur.getUno())) {
            if (utilisateurDAO.findById(nouveauPseudo) != null) {
                throw new DAOException("Le pseudo " + nouveauPseudo + " est déjà pris. Veuillez en choisir un autre.");
            }
            utilisateur.setUno(nouveauPseudo);  
        }
        boolean isUpdated = false;
        if (nom != null && !nom.equals(utilisateur.getNom())) {
            utilisateur.setNom(nom);
            isUpdated = true;
        }
        if (prenom != null && !prenom.equals(utilisateur.getPrenom())) {
            utilisateur.setPrenom(prenom);
            isUpdated = true;
        }
        if (email != null && !email.equals(utilisateur.getEmail())) {
            utilisateur.setEmail(email);
            isUpdated = true;
        }
        if (mdp != null && !mdp.isEmpty()) {
            utilisateur.setMdp(mdp);
            isUpdated = true;
        }
        if (isUpdated) {
            boolean isSaved = utilisateurDAO.update(utilisateur);
            if (!isSaved) {
                throw new DAOException("La mise à jour du profil a échoué.");
            }
        }
        session.setAttribute("pseudo", utilisateur.getUno());
        session.setAttribute("utilisateur", utilisateur);
        return "WEB-INF/vue/pageVuDeProfil.jsp";
    }  
}
