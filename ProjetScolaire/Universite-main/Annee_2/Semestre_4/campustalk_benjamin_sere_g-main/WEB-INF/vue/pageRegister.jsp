<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Inscription - CampusTalk</title>

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

  <main class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-5 shadow-lg" style="max-width: 500px; width: 100%; border-radius: 15px;">
      <h2 class="text-center mb-4">Inscription</h2>
      
      <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
      <% if (errorMessage != null) { %>
        <div class="alert alert-danger text-center"><%= errorMessage %></div>
      <% } %>

      <form action="Controleur" method="post">
        <input type="hidden" name="action" value="sInscrire">

        <div class="form-group">
          <label for="pseudo">Pseudo</label>
          <input type="text" class="form-control" name="pseudo" id="pseudo" placeholder="Entrez votre pseudo" required autocomplete="username">
        </div>

        <div class="form-group mt-3">
          <label for="nom">Nom</label>
          <input type="text" class="form-control" name="nom" id="nom" placeholder="Entrez votre nom" required>
        </div>

        <div class="form-group mt-3">
          <label for="prenom">Prénom</label>
          <input type="text" class="form-control" name="prenom" id="prenom" placeholder="Entrez votre prénom" required>
        </div>

        <div class="form-group mt-3">
          <label for="email">Adresse Email</label>
          <input type="email" class="form-control" name="email" id="email" placeholder="Entrez votre email" required autocomplete="email">
        </div>

        <div class="form-group mt-3">
          <label for="mdp">Mot de passe</label>
          <input type="password" class="form-control" name="mdp" id="mdp" placeholder="Créez un mot de passe" required minlength="1" autocomplete="new-password">
        </div>

        <button type="submit" class="btn btn-dark w-100 mt-3">S'inscrire</button>
      </form>


      <div class="text-center mt-3">
        <p>Déjà un compte ? <a href="Controleur?action=seConnecter">Connectez-vous</a></p>
      </div>
    </div>
  </main>
</body>
</html>
