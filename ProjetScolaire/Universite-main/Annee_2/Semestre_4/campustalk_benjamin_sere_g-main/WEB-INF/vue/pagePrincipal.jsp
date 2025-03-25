<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.dto.*" %>
<%@ page import="model.dao.*" %>
<%@ page import="controleur.*" %>
<%@ page import="service.*" %>
<%@ page import="model.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>


<%
    String pseudo = (String) session.getAttribute("pseudo");
    Integer current_fno = (Integer) session.getAttribute("current_fno");
    if (pseudo == null) {
        response.sendRedirect("Controleur?action=seConnecter");
        return;
    }
    FilService filService = new FilService();
    AbonnementDAO abonnementDAO = new AbonnementDAO();
    FilDAO filDAO = new FilDAO();
    MessageDAO messageDAO = new MessageDAO();
    NotificationDAO notificationDAO = new NotificationDAO();
    LikeDAO likeDAO = new LikeDAO();
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CampusTalk</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/pagePrincipal.css">
</head>
<body>

<%
    String goodMessage = (String) request.getAttribute("goodMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
    <!-- Les petits Toast -->
    <div class="toast-container position-fixed top-0 end-0 p-3">
        <% if (goodMessage != null && !goodMessage.trim().isEmpty()) { %>
            <div class="toast align-items-center text-white bg-success border-0 show" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
                <div class="d-flex">
                    <div class="toast-body">
                        <%= goodMessage %>
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        <% } %>
    
        <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
            <div class="toast align-items-center text-white bg-danger border-0 show" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">
                <div class="d-flex">
                    <div class="toast-body">
                        <%= errorMessage %>
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        <% } %>
    </div>
    
    <!-- nav pricipal -->
    <nav class="navbar navbar-light bg-light">
        <div class="container-fluid">
            <button class="btn btn-primary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasSidebar">☰</button>
            <span class="navbar-brand mb-0 h1">
                <a href="Controleur?action=allerAccueille" style="text-decoration: none; color: inherit;">CampusTalk</a>
            </span>
            <span>Bienvenue, <%= pseudo %>!</span>
        </div>
    </nav>

    <!-- Side barre -->
    <div class="offcanvas offcanvas-start" id="offcanvasSidebar">
        <div class="offcanvas-header">
            <h5 class="offcanvas-title">CampusTalk</h5>
            <button type="button" class="btn-close" data-bs-dismiss="offcanvas"></button>
        </div>
        <div class="offcanvas-body">
            <a href="Controleur?action=creerFil" class="btn btn-primary">Crée votre fil</a>
            <!--
            <div class="d-flex align-items-center my-3">
                <form class="d-flex" action="Controleur" method="get">
                    <input type="hidden" name="action" value="rechercherFil">
                    <input class="form-control ms-2" type="search" name="query" placeholder="Rechercher un fil..." aria-label="Rechercher">
                    <button class="btn btn-outline-secondary ms-2" type="submit">🔍</button>
                </form>
            </div>
            -->
            <hr>
            <h5>Fils auxquels vous êtes abonnés</h5>
            <div class="list-group overflow-auto" style="height: max-content;">
                <ul class="list-group">
                    <% 
                        List<Fil> fils = filService.getFilsUtilisateur(pseudo);
                        if (fils != null && !fils.isEmpty()) {
                            for (Fil fil : fils) {
                    %>
                        <li class="list-group-item">
                            <form action="Controleur" method="post" class="d-inline">
                                <input type="hidden" name="current_fno" value="<%= fil.getFno() %>">
                                <input type="hidden" name="action" value="chargerFilSelectionner">
                                <button type="submit" class="btn btn-link p-0 text-decoration-none">
                                    <%= fil.getTitre() %>
                                </button>
                            </form>
                        </li>
                    <% 
                            }
                        } else { 
                    %>
                        <li class="list-group-item">Aucun fil récent</li>
                    <% 
                        }
                    %>
                </ul>
            </div>
            <hr>
            <ul class="nav flex-column" id="bottom-offcanvas">
                <li class="nav-item"><a href="Controleur?action=deconnexion" class="nav-link">Déconnexion</a></li>
                <li class="nav-item"><a href="Controleur?action=vueDeProfil" class="nav-link">Votre profil</a></li>
            </ul>
        </div>
    </div>


    <div class="container-fluid p-4"> 
        <div class="row">
            <% if(current_fno == null && session.getAttribute("fno_banni").equals(current_fno)) { %>
            <div class="col-lg-8">
                <div class="p-3 border rounded bg-light">
                    <h1>Aucun fil selectionner !!</h1>
                </div>
            </div>
            <% }else{ %>
            <!-- Liste des membres -->
            <div class="col-lg-2">
                <div class="list-group p-3 border rounded bg-light">
                    <h5>Liste des membres </h5>
                    <% 
                        
                        List<Abonnement> abonnements =  abonnementDAO.findAllByFil(current_fno);
                        if (abonnements != null && !abonnements.isEmpty()) {
                            for (Abonnement abonnement : abonnements) {
                    %>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <% if(abonnementDAO.isAdmin(abonnement.getUno(), current_fno)) { %>
                                <span class="admin-label">
                                    <%= abonnement.getUno() %> <i class="bi bi-award"></i>
                                </span>
                            <% } else { %>
                                <%= abonnement.getUno() %>
                            <% } %>
                            <form action="Controleur" method="post" class="d-inline">
                                <input type="hidden" name="current_fno" value="<%= current_fno %>">
                                <input type="hidden" name="pseudo" value="<%= abonnement.getUno() %>">
                                <input type="hidden" name="action" value="voirProfilUtilisateurFil">
                                <button type="submit" class="btn btn-link p-0">
                                    <i class="bi bi-three-dots"></i>
                                </button> 
                            </form>
                        </li>
                    <% 
                            }
                        } else { 
                    %>
                        <li class="list-group-item">Aucun membres dans votre fil</li>
                    <% 
                        }
                    %>
                </div>
            </div>
            <!-- Box du chat -->
            <div class="col-lg-6">
                <div class="p-3 border rounded bg-light">
                    <% 
                        Fil fil = filDAO.findById(current_fno);
                    %>
                    <div class="d-flex justify-content-between align-items-center">
                        <h2 class="me-3"><%= fil.getTitre() %></h2> 
                        <% if(current_fno != null && abonnementDAO.isAdmin(pseudo, current_fno)) { %>
                        <div class="dropdown">
                            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"></a>
                            <ul class="dropdown-menu">
                                    <li class="dropdown-item"><a class="nav-link" href="Controleur?action=ajouterUtilisateurAuFil"><i class="bi bi-person-plus"></i> Ajouter des membres</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li class="dropdown-item"><a class="nav-link text-danger" href="Controleur?action=supprimerFil"><i class="bi bi-trash"></i> Supprimer le fil</a></li>
                            </ul>
                        </div>
                        <% } else { %>
                            <a class="nav-link text-danger" href="Controleur?action=quitterFil"><i class="bi bi-trash"></i>Quitter le fil</a>
                        <% } %>
                    </div>

                    <hr>
                    <!-- Liste des messages -->
                    <div class="chat-box message-list">
                        <%
                            List<Message> messages = messageDAO.findAllByFil(current_fno);
                        %>
                        <% if (messages.isEmpty()) { %>
                            <p class="text-muted">Aucun message pour le moment.</p>
                        <% } else { %>
                            <% for (Message message : messages) { %>
                                <%
                                int nbLike = likeDAO.findAllByMessage(message.getMno()).size();
                                boolean liker = likeDAO.exists(pseudo, message.getMno()); 
                                %>
                                <div class="message">
                                    <strong><%= message.getUno() %></strong> 
                                    <span class="message-time">- <%= message.getDateVisuelle() %></span>
                                    <div class="message-text"><%= message.getContenu() %></div>

                                    <% if (message.getImagePath() != null && !message.getImagePath().isEmpty()) { %>
                                        <div class="message-image">
                                            <img src="<%= message.getImagePath() %>" alt="Image du message" class="img-fluid" />
                                        </div>
                                    <% } %>

                                    <div class="like-container mt-2">
                                        <form action="Controleur" method="post">
                                            <button type="submit" class="btn btn-link p-0" style="border: none; background: transparent;">
                                                <i class="bi bi-heart<%= liker ? "-fill text-danger" : "" %> like-icon"></i>
                                            </button>
                                            <span class="nbLike"><%= nbLike %></span>
                                            <input type="hidden" name="action" value="<%= liker ? "supprimerLike" : "ajouterLike" %>">
                                            <input type="hidden" name="message_mno" value="<%= message.getMno() %>">
                                        </form>
                                    </div>
                                </div>
                        <% } } %>
                    </div>

                    <!-- Formulaire d'envoi de message -->
                    <div class="mt-3">
                        <form class="d-flex align-items-center gap-2" action="Controleur" method="post" enctype="multipart/form-data">
                            <label for="fileInput" class="btn btn-outline-primary d-flex align-items-center">
                                + 
                            </label>
                            <input type="file" id="fileInput" name="image" accept="image/*">
                            <textarea class="form-control flex-grow-1" name="message" rows="1" placeholder="Écrivez votre message ici..." required></textarea>
                            <input type="hidden" name="action" value="envoyerMessage">
                            <button type="submit" class="btn btn-primary">Envoyer</button>
                        </form>
                    </div>
                </div>
            </div>
            <% }%>    
            <div class="col-lg-4 col-md-12">
                <!-- Bloc Notifications -->
                <div class="p-3 border rounded bg-light shadow-sm mb-3">
                    <h5 class="text-secondary">Notifications</h5>
                    <ul class="list-group">
                        <%  
                            NotificationManager notificationManager = new NotificationManager(request.getSession());
                            List<Notification> notifications = notificationManager.getNotifications();
                            if (notifications == null || notifications.isEmpty()) {
                        %>
                            <p class="text-danger">Aucune notification dans vos fils !</p>
                        <%  
                            } else {
                                for (Notification notification : notifications) {
                                    Fil fil = filDAO.findById(notification.getFno());
                                    String type = notification.getType();
                                    LocalDateTime dateNotif = notification.getDateEnvoi(); 
                        %>
                            <li class="list-group-item notification-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <i class="bi bi-bell-fill text-primary me-2">
                                        <% if (type.equals("LIKE")) { %>
                                            Vous avez recu un nouveau like;
                                        <% } else if (type.equals("MESSAGE")) { %>
                                            Nouveau message 
                                        <% } %>
                                    </i> 
                                    <!-- Bouton pour supprimer la notification -->
                                    <form action="Controleur" method="post" class="d-inline">
                                        <input type="hidden" name="notification_nno" value="<%= notification.getNno() %>">
                                        <input type="hidden" name="action" value="supprimerNotification">
                                        <button class="btn btn-sm"><i class="bi bi-x-lg"></i></button>
                                    </form>
                                </div>
                            </li>
                        <%  
                                }
                            }
                        %>
                    </ul>
                </div>
    
                <!-- Bloc Invitations -->
                <div class="p-3 border rounded bg-light shadow-sm">
                    <h5 class="text-secondary">Invitations</h5>
                    <ul class="list-group">
                        <%  
                            List<Notification> notificationsInvitations = notificationManager.getInvitations(pseudo);
                            if (notificationsInvitations == null || notificationsInvitations.isEmpty()) {
                        %>
                            <p class="text-danger">Aucune invitation !</p>
                        <%  
                            } else {
                                for (Notification notification : notificationsInvitations) {
                                    Fil fil = filDAO.findById(notification.getFno());
                        %>
                            <li class="list-group-item notification-item d-flex justify-content-between align-items-center">
                                <div>
                                    <i class="bi bi-bell-fill text-primary me-2"></i> <%= notification.getType().toLowerCase() %> - <%= fil.getTitre() %>
                                </div>
                                <div>
                                    <form action="Controleur" method="post" class="d-inline">
                                        <input type="hidden" name="notification_nno" value="<%= notification.getNno() %>">
                                        <input type="hidden" name="notification_fno" value="<%= notification.getFno() %>">
                                        <input type="hidden" name="action" value="accepterInvitation">
                                        <button type="submit" class="btn btn-success btn-sm">Accepter</button>
                                    </form>
                                    <form action="Controleur" method="post" class="d-inline">
                                        <input type="hidden" name="notification_nno" value="<%= notification.getNno() %>">
                                        <input type="hidden" name="action" value="supprimerInvitation">
                                        <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
                                    </form>
                                </div>
                            </li>
                        <%  
                                }
                            }
                        %>
                    </ul>
                </div>
            </div>
        </div> 
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
