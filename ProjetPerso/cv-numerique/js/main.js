
document.addEventListener("DOMContentLoaded", function () {
  const navbarPlaceholder = document.getElementById("navbar-placeholder");
  fetch("../html-tools/nav.html")
    .then(response => response.text())
    .then(data => {
      navbarPlaceholder.innerHTML = data;
    })
    .catch(error => console.error("Erreur lors du chargement de la navigation :", error));
});

document.addEventListener("DOMContentLoaded", function () {
  const navbarPlaceholder = document.getElementById("footer-placeholder");
  fetch("../html-tools/footer.html")
    .then(response => response.text())
    .then(data => {
      navbarPlaceholder.innerHTML = data;
    })
    .catch(error => console.error("Erreur lors du chargement de la navigation :", error));
});

