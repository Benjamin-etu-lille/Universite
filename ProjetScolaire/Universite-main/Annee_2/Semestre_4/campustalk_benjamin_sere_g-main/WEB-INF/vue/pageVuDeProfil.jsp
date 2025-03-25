<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="model.dto.*" %>
<%@ page import="model.dao.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    String pseudo = (String) session.getAttribute("pseudo");
    if (pseudo == null) {
        response.sendRedirect("Controleur?action=seConnecter");
        return;
    }

    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    AbonnementDAO abonnementDAO = new AbonnementDAO();
    MessageDAO messageDAO = new MessageDAO();
    LikeDAO likeDAO = new LikeDAO();

    Utilisateur utilisateur = utilisateurDAO.findById(pseudo);

    int nbFilsDiscussion = abonnementDAO.findAllByUtilisateur(pseudo).size();
    int nbMessagesEnvoyes = messageDAO.findAllByUtilisateur(pseudo).size();
    int nbReactionsLike = likeDAO.findAllByUtilisateur(pseudo).size();
%>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Votre Profil - CampusTalk</title>

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
      <button class="btn" type="button" aria-expanded="false">
        <a href="Controleur?action=accesPagePrincipal"><i class="bi bi-chevron-left" style="font-size: 2rem;"></i></a>
      </button>
    </div>
  </header>

  <main class="container py-5">
    <div class="row flex-column flex-md-row gap-4">

      <!-- Profil Utilisateur -->
      <div class="col-md-6">
        <div class="card shadow-lg card-custom bg-white">
          <div class="profile-header">
            <!--<img src="https://via.placeholder.com/80" alt="Avatar">-->
            <div>
              <h3 class="text-primary m-0"><%= utilisateur.getPrenom() + " " + utilisateur.getNom() %></h3>
            </div>
          </div>

          <hr>

          <!-- Formulaire de modification de profil -->
          <form action="Controleur?action=modifierProfil" method="post">
            <div class="mb-3 stats-item">
              <i class="bi bi-card-text"></i>
              <label for="nom" class="form-label"><strong>Nom</strong></label>
              <input type="text" id="nom" name="nom" class="form-control" value="<%= utilisateur.getNom() %>" required />
            </div>
            <div class="mb-3 stats-item">
              <i class="bi bi-card-text"></i>
              <label for="prenom" class="form-label"><strong>Prénom</strong></label>
              <input type="text" id="prenom" name="prenom" class="form-control" value="<%= utilisateur.getPrenom() %>" required />
            </div>
            <div class="mb-3 stats-item">
              <i class="bi bi-envelope"></i>
              <label for="email" class="form-label"><strong>Email</strong></label>
              <input type="email" id="email" name="email" class="form-control" value="<%= utilisateur.getEmail() %>" required />
            </div>
            <div class="mb-3 stats-item">
              <i class="bi bi-lock"></i>
              <label for="mdp" class="form-label"><strong>Mot de passe</strong></label>
              <input type="password" id="mdp" name="mdp" class="form-control" placeholder="Changer de mot de passe" />
            </div>

            <div class="d-flex justify-content-between mt-4">
              <button type="submit" class="btn btn-primary btn-custom px-4">Sauvegarder</button>
              <a href="Controleur?action=deconnexion" class="btn btn-danger btn-custom px-4">Se déconnecter</a>
            </div>
          </form>
        </div>
      </div>

      <!-- Statistiques Utilisateur -->
      <div class="col-md-5">
        <div class="card shadow-lg card-custom bg-white">
          <h4 class="text-primary mb-3">📊 Statistiques</h4>
          <hr>
          
          <div class="stats-item">
            <i class="bi bi-chat-left-text"></i>
            <span><strong>Fils de discussion créés : </strong> <%= nbFilsDiscussion %></span>
          </div>
          
          <div class="stats-item mt-2">
            <i class="bi bi-send"></i>
            <span><strong>Messages envoyés : </strong> <%= nbMessagesEnvoyes %></span>
          </div>

          <div class="stats-item mt-2">
            <i class="bi bi-send"></i>
            <span><strong>Likes reçues : </strong><%= nbReactionsLike %></span>
          </div>
          
          <div class="stats-item mt-2">
            <i class="bi bi-calendar"></i>
            <span><strong>Date de création : </strong> <%= utilisateur.getDateCreation().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
          </div>
        </div>
      </div>

    </div>
  </main>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
