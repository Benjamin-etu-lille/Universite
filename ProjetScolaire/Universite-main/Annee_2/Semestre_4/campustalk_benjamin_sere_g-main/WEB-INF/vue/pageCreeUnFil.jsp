<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="model.dto.*" %>
<%@ page import="model.dao.*" %>

<%
    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    String pseudo = (String) session.getAttribute("pseudo");
    if (pseudo == null) {
        response.sendRedirect("Controleur?action=seConnecter");
        return;
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Créer un Fil - CampusTalk</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="css/styles.css">
</head>
<body>
  <header class="container-fluid d-flex justify-content-between align-items-center p-3 bg-white shadow">
    <div class="logo">C&T</div>
    <div class="settings">
      <a href="Controleur?action=allerAccueille" class="btn">
        <i class="bi bi-chevron-left" style="font-size: 2rem;"></i>
      </a>
    </div>
  </header>

  <main class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-5 shadow-lg">
      <h2 class="text-center mb-4">Créer un Fil</h2>
      
      <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
      <% if (errorMessage != null) { %>
        <div class="alert alert-danger text-center"><%= errorMessage %></div>
      <% } %>
      
      <form action="Controleur" method="post">
        <input type="hidden" name="action" value="creerFil">

        <div class="form-group">
          <label for="titre">Titre du Fil</label>
          <input type="text" class="form-control" name="titre" id="titre" placeholder="Entrez le titre du fil" required>
        </div>

        <div class="form-group mt-3">
          <label for="description">Description</label>
          <textarea class="form-control" name="description" id="description" placeholder="Entrez une description" required></textarea>
        </div>

        <button type="submit" class="btn btn-dark w-100 mt-3">Créer</button>
      </form>
    </div>
  </main>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
