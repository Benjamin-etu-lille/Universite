<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Connexion - CampusTalk</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <link rel="stylesheet" href="css/styles.css">
</head>

<body>
  <header class="container-fluid d-flex justify-content-between align-items-center p-3 bg-white shadow">
    <div class="logo">C&T</div>
    <div class="settings">
      <button class="btn" type="button" aria-expanded="false">
        <a href="Controleur?action=allerAccueille"><i class="bi bi-chevron-left" style="font-size: 2rem;"></i></a>
      </button>
    </div>
  </header>

  <%
    String pseudoCookie = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("pseudo")) {
                pseudoCookie = cookie.getValue();
            }
        }
    }
  %>

  <main class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-5 shadow-lg">
      <h2 class="text-center mb-4">Connexion</h2>

      <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
      <% if (errorMessage != null) { %>
        <div class="alert alert-danger text-center"><%= errorMessage %></div>
      <% } %>

      <form action="Controleur" method="post">
        <input type="hidden" name="action" value="seConnecter">
        
        <% if(pseudoCookie.isEmpty() || pseudoCookie == null) {%>
          <div class="form-group">
            <label for="pseudo">Votre pseudo</label>
            <input type="text" class="form-control" name="pseudo" id="pseudo" placeholder="Entrez votre pseudo" required>
          </div>
        <% }else { %>
          <div class="form-group">
            <label for="pseudo">Pseudo :</label>
            <input type="text" class="form-control" id="pseudo" name="pseudo" value="<%= pseudoCookie %>">
          </div>
        <% }%>
        <div class="form-group mt-3">
          <label for="password">Mot de passe</label>
          <input type="password" class="form-control" name="password" id="password" placeholder="Entrez votre mot de passe" required>
        </div>
        
        <button type="submit" class="btn btn-dark btn-block mt-3">Se connecter</button>
      </form>

      <div class="text-center mt-3">
        <p>Pas encore de compte ? <a href="Controleur?action=sInscrire">Inscrivez-vous</a></p>
      </div>
    </div>
  </main>
</body>
</html>
