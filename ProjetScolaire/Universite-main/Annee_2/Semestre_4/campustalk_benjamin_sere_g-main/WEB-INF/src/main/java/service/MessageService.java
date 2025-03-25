package service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.dao.AbonnementDAO;
import model.dao.MessageDAO;
import model.dao.NotificationDAO;
import model.dto.Abonnement;
import model.dto.Message;
import model.dto.Notification;

/**
 * Service responsable de la gestion des messages dans l'application.
 * <p>
 * Cette classe permet l'envoi de messages par un utilisateur dans un fil de discussion.
 * Elle gère également l'ajout d'images associées aux messages et leur sauvegarde en base de données.
 * </p>
 */
public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
    * Gère l'envoi d'un message par un utilisateur dans un fil de discussion.
    * <p>
    * Cette méthode récupère le contenu du message ainsi que, si disponible, une image jointe.
    * Si le message est valide, il est enregistré en base de données et l'utilisateur est redirigé
    * vers la page principale. En cas d'erreur ou de message vide, un message d'erreur est affiché.
    * </p>
    *
    * @param req     L'objet {@link HttpServletRequest} contenant les informations de la requête HTTP.
    *                Il doit inclure le paramètre <b>"message"</b> représentant le texte du message.
    * @param res     L'objet {@link HttpServletResponse} utilisé pour rediriger l'utilisateur après l'opération.
    * @param session La session HTTP associée à l'utilisateur, contenant :
    *                <ul>
    *                  <li><b>"pseudo"</b> : Identifiant de l'utilisateur.</li>
    *                  <li><b>"current_fno"</b> : Identifiant du fil de discussion actif.</li>
    *                </ul>
    * @return Toujours {@code null} car la méthode redirige systématiquement l'utilisateur après l'opération.
    * @throws ServletException Si une erreur survient lors du traitement de la requête.
    * @throws IOException      Si une erreur survient lors de la gestion du fichier image.
    */
    public String envoyerMessage(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws ServletException, IOException {
        try {
            String contenuMessage = req.getParameter("message");

            if (contenuMessage != null && !contenuMessage.trim().isEmpty()) {
                String uno = (String) session.getAttribute("pseudo");
                int fno = (int) session.getAttribute("current_fno");

                Part imagePart = req.getPart("image");
                String imagePath = null;

                if (imagePart != null && imagePart.getSize() > 0) {
                    String fileName = imagePart.getSubmittedFileName();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));

                    String uniqueFileName = "image_" + System.currentTimeMillis() + fileExtension; // Nom unique en se servant du temps =)

                    String uploadDir = "/home/infoetu/benjamin.sere.etu/tomcat/webapps/campustalk_benjamin_sere_g/images/";
                    File dir = new File(uploadDir);

                    // Si le répertoire images n'existe pas, on le crée !!
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String uploadPath = uploadDir + uniqueFileName;
                    imagePart.write(uploadPath);
                    imagePath = "/campustalk_benjamin_sere_g/images/" + uniqueFileName;
                }

                Message message = new Message(contenuMessage, imagePath, uno, fno);
                messageDAO.save(message);
                AbonnementDAO abonnementDAO = new AbonnementDAO();
                List<Abonnement> utilisateursDuFil = abonnementDAO.findAllByFil(fno);

                // Envoi de la notification à chaque utilisateur du fil, sauf à l'expéditeur
                NotificationDAO notificationDAO = new NotificationDAO();
                for (Abonnement abonnement : utilisateursDuFil) {
                    if (!abonnement.getUno().equals(uno)) { // Ne pas envoyer la notification à l'expéditeur
                        Notification notification = new Notification();
                        notification.setUno(abonnement.getUno());
                        notification.setFno(abonnement.getFno());  
                        notification.setType("MESSAGE");
                        notification.setEtat("EN_ATTENTE");

                        notificationDAO.save(notification);
                    }
                }

                // Redirection après l'envoi du message
                res.sendRedirect("Controleur?action=accesPagePrincipal");
                return null;

            } else {
                req.setAttribute("errorMessage", "Le contenu du message est vide");
                res.sendRedirect("Controleur?action=accesPagePrincipal");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Erreur lors de l'envoi du message.");
            res.sendRedirect("Controleur?action=accesPagePrincipal");
        }
        return null;
    }
}
