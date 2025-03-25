<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="model.dto.*" %>
<%@ page import="model.dao.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    String pseudo = (String) session.getAttribute("pseudo");
    String vueDeUtilisateur = request.getParameter("pseudo");
    int current_fno = Integer.parseInt(request.getParameter("current_fno"));

    if (pseudo == null || vueDeUtilisateur == null) {
        response.sendRedirect("Controleur?action=seConnecter");
        return;
    }

    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    AbonnementDAO abonnementDAO = new AbonnementDAO();
    LikeDAO likeDAO = new LikeDAO();
    MessageDAO messageDAO = new MessageDAO();

    Utilisateur utilisateur = utilisateurDAO.findById(vueDeUtilisateur);
    boolean isAdmin = abonnementDAO.isAdmin(pseudo, current_fno);

    int nbMessagesEnvoyes = messageDAO.findAllByUtilisateurAndFil(vueDeUtilisateur,current_fno).size();
    int nbReactionsLike = likeDAO.findAllByUtilisateurAndFil(vueDeUtilisateur,current_fno).size();
%>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Profil de <%= utilisateur.getUno() %> - CampusTalk</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="css/styles.css">
  
  <style>
    .card-custom {
      border-radius: 15px;
      padding: 2rem;
    }
    .profile-header {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .profile-header img {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      border: 3px solid #007bff;
    }
    .stats-item {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 1.2rem;
    }
    .stats-item i {
      font-size: 1.8rem;
      color: #007bff;
    }
  </style>
</head>
<body class="bg-light">

  <header class="container-fluid d-flex justify-content-between align-items-center p-3 bg-white shadow">
    <div class="logo">C&T</div>
    <div class="settings">
      <a href="Controleur?action=accesPagePrincipal" class="btn">
        <i class="bi bi-chevron-left" style="font-size: 2rem;"></i>
      </a>
    </div>
  </header>

  <main class="container py-5">
    <div class="row">
      <!-- Profil Utilisateur -->
      <div class="col-lg-6">
        <div class="card shadow-lg card-custom bg-white">
          <div class="profile-header d-flex align-items-center">
            <i class="bi bi-person-circle text-primary" style="font-size: 3rem;"></i>
            <div>
              <h3 class="text-primary m-0"><%= utilisateur.getPrenom() + " " + utilisateur.getNom() %></h3>
            </div>
          </div>

          <hr>

          <div class="stats-item">
            <i class="bi bi-card-text "></i>
            <p class="m-0"><strong>Nom</strong> <%= utilisateur.getNom() %></p>
          </div>

          <div class="stats-item">
            <i class="bi bi-card-text "></i>
            <p class="m-0"><strong>Prénom</strong> <%= utilisateur.getPrenom() %></p>
          </div>

          <% if(isAdmin) { %>
            <hr>

            <div class="stats-item">
              <i class="bi bi-envelope "></i>
              <p class="m-0"><strong>Email</strong> <%= utilisateur.getEmail() %></p>
            </div>

            <div class="stats-item">
              <i class="bi bi-calendar "></i>
              <p class="m-0"><strong>Date de création</strong> <%= utilisateur.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) %></p>
            </div>

            <% if(!pseudo.equals(utilisateur.getUno())) { %>
              <div class="stats-item">
              <form action="Controleur" method="post" class="mt-3">
                <input type="hidden" name="current_fno" value="<%=current_fno%>">
                <input type="hidden" name="pseudo" value="<%= utilisateur.getUno() %>">
                <input type="hidden" name="action" value="bannireDuFil">
                <button type="submit" class="btn btn-danger w-100 d-flex align-items-center justify-content-center">
                  <i class="bi bi-ban me-2"></i> Bannir cet utilisateur
                </button>
              </form>
              </div>
            <% } %>
          <% } %>
        </div>
      </div>

      <!-- Statistiques Utilisateur -->
      <div class="col-lg-4">
        <div class="card shadow-lg card-custom bg-white">
          <h4 class="text-primary mb-3">Statistiques du fil</h4>
          
          <div class="stats-item">
            <i class="bi bi-chat-left-text"></i>
            <span><strong>Messages envoyés : </strong><%= nbMessagesEnvoyes %></span>
          </div>
          
          <div class="stats-item mt-2">
            <i class="bi bi-heart"></i>
            <span><strong>Likes reçues : </strong><%= nbReactionsLike %></span>
          </div>
          
          <div class="stats-item mt-2">
            <i class="bi bi-award"></i>
            <span><strong>Rôle : </strong> <%= abonnementDAO.isAdmin(vueDeUtilisateur, current_fno) ? "Admin" : "Membre" %></span>
          </div>
          
        </div>
      </div>

    </div>
  </main>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
