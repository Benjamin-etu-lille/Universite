<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Accueil - CampusTalk</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://getbootstrap.com/docs/5.3/assets/css/docs.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <link rel="stylesheet" href="css/styles.css">
  
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/animejs/2.0.2/anime.min.js"></script>
</head>

<body>
  <header class="container-fluid d-flex justify-content-between align-items-center p-3 bg-white shadow">
    <div class="logo">C&T</div>
    <div class="settings">
      <button class="btn" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        <i class="bi bi-gear" style="font-size: 2rem;"></i>
      </button>
      <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="Controleur?action=seConnecter">Se connecter</a></li>
        <li><a class="dropdown-item" href="Controleur?action=sInscrire">S'inscrire</a></li>
      </ul>
    </div>
  </header>

  <main class="container my-5 text-center">
    <h1>Campus Talk</h1>
    <p class="ml7">
      <span class="text-wrapper">
        <span class="letters">Une plateforme innovante pour étudiants et enseignants</span>
      </span>
    </p>

    <h3>
      <a class="dropdown-item" href="Controleur?action=accesPagePrincipal">
            Commencer à discuter <i class="bi bi-chat-left-text" style="font-size: 2rem;"></i>
      </a>
    </h3>

    <section class="project-presentation my-5">
      <h2>Présentation du projet</h2>
      <p>
        CampusTalk est une application web de réseau social destinée aux étudiants et enseignants
        pour faciliter la communication au sein de leur établissement.
      </p>
      <p>
        Inspirée de plateformes comme Discord et WhatsApp, cette application permet aux utilisateurs
        de créer et de gérer des fils de discussion privés ou publics, d’envoyer et recevoir des
        messages textuels et de gérer leurs relations sociales (ajout d'amis, abonnements, etc.).
      </p>

      <h2>Objectifs et Enjeux</h2>

      <div class="objectives-container">
        <div class="objective-card">
          <h3>🔒 Sécurité</h3>
          <p>Assurer une gestion sécurisée des utilisateurs avec un système d’authentification robuste.</p>
        </div>

        <div class="objective-card">
          <h3>💬 Chatter</h3>
          <p>Permettre la création, la gestion et la participation à des fils de discussion.</p>
        </div>

        <div class="objective-card">
          <h3>🎨 Interface</h3>
          <p>Fournir une expérience fluide et intuitive avec une navigation simplifiée.</p>
        </div>
      </div>
    </section>
  </main>
  <script src="js/animationText.js"></script>
</body>
</html>
