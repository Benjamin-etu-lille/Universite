// Liste des textes à alterner
const textes = [
  "Une plateforme innovante pour étudiants et enseignants",
  "Collaborez sur des projets académiques",
  "Discutez dans un cadre structuré et sécurisé",
  "Créez et gérez des fils de discussion",
  "Ajoutez des amis et échangez facilement"
];

let index = 0;
function changerTexte() {
  let textWrapper = document.querySelector('.ml7 .letters');
  textWrapper.innerHTML = textes[index].replace(/\S/g, "<span class='letter'>$&</span>");

  anime.timeline({ loop: false })
    .add({
      targets: '.ml7 .letter',
      opacity: [0, 1],
      translateY: ["1.1em", 0],
      translateX: ["0.55em", 0],
      translateZ: 0,
      rotateZ: [70, 0],
      duration: 150,
      easing: "easeOutExpo",
      delay: (el, i) => 28 * i
    });

  index = (index + 1) % textes.length;
}

changerTexte();
setInterval(changerTexte, 6000);

function gearRotation(rotation) {
  anime.timeline({ loop: false })
    .add({
      targets: '.bi-gear',
      rotateZ: [70, 0],
      duration: rotation
    });
}

document.addEventListener("DOMContentLoaded", function () {
  const gearIcon = document.querySelector("settings .bi-gear");

  gearIcon.addEventListener("mouseover", function () {
    gearRotation(150);
  });
  gearIcon.addEventListener("click", function () {
    gearRotation(360);
  });
});