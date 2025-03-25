package controleur.endpoints;

import java.util.Base64;

import exceptions.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.UtilisateurDAO;
import model.dto.Utilisateur;

public class BasicAuth {
    
    public static boolean auth(HttpServletRequest req, UtilisateurDAO udao) {
        String authorization = req.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic")) return false;
        // on décode le token
        String token = authorization.substring("Basic".length()).trim();
        byte[] base64 = Base64.getDecoder().decode(token);
        String[] lm = (new String(base64)).split(":");
        String login = lm[0];
        String pwd = lm[1];
        
        // Vérification des paramètres de connexion
        if (login == null || login.trim().isEmpty() || pwd == null || pwd.trim().isEmpty()) {
            return false;
        }
        
        Utilisateur utilisateur = null;
        try {
            // Recherche de l'utilisateur par son pseudo
            utilisateur = udao.findById(login);
        } catch (DAOException e) {
            e.printStackTrace();
            return false;
        }

        if (utilisateur != null) {
            // Vérification du mot de passe
            if (utilisateur.getMdp().equals(pwd)) {
                // Si les informations sont correctes, return false;
                return true;
            } else {
                // Si le mot de passe est incorrect
                return false;
            }
        } else {
            // Si l'utilisateur n'existe pas
            return false;
        }
    }
}
