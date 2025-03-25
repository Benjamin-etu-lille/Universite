<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="model.dto.*" %>
<%@ page import="model.dao.*" %>

<%
    String pseudo = (String) session.getAttribute("pseudo");
    if (pseudo == null) {
        response.sendRedirect("Controleur?action=seConnecter");
        return;
    }

    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    int current_fno = (int) session.getAttribute("current_fno");
    List<Utilisateur> utilisateurs = utilisateurDAO.getUtilisateursAInviter(current_fno);
%>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ajouter un membre à un fil - CampusTalk</title>
  
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
      <h2 class="text-center mb-4">Ajouter un membre à un fil</h2>

      <form action="Controleur" method="post">
        <input type="hidden" name="action" value="envoyerInvitation">
        <div class="form-group">
          <label for="utilisateur">Utilisateur</label>
          <select class="form-control" name="uno" id="utilisateur" required>
            <option value="">Sélectionnez un utilisateur</option>
            <% for (Utilisateur utilisateur : utilisateurs) { %>
              <option value="<%= utilisateur.getUno() %>"><%= utilisateur.getUno() %></option>
            <% } %>
          </select>
        </div>

        <button type="submit" class="btn btn-dark w-100 mt-3">Ajouter</button>
      </form>
    </div>
  </main>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
