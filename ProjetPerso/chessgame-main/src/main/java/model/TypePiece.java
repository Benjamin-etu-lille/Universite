package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Couleur;

import java.io.InputStream;

public enum TypePiece {
    PION_BLANC("Pion", Couleur.BLANC, "pieces-basic-png/white-pawn.png", 1),
    PION_NOIR("Pion", Couleur.NOIR, "pieces-basic-png/black-pawn.png", 1),
    TOUR_BLANC("Tour", Couleur.BLANC, "pieces-basic-png/white-rook.png", 5),
    TOUR_NOIR("Tour", Couleur.NOIR, "pieces-basic-png/black-rook.png", 5),
    CAVALIER_BLANC("Cavalier", Couleur.BLANC, "pieces-basic-png/white-knight.png", 3),
    CAVALIER_NOIR("Cavalier", Couleur.NOIR, "pieces-basic-png/black-knight.png", 3),
    FOU_BLANC("Fou", Couleur.BLANC, "pieces-basic-png/white-bishop.png", 3),
    FOU_NOIR("Fou", Couleur.NOIR, "pieces-basic-png/black-bishop.png", 3),
    REINE_BLANCHE("Reine", Couleur.BLANC, "pieces-basic-png/white-queen.png", 9),
    REINE_NOIRE("Reine", Couleur.NOIR, "pieces-basic-png/black-queen.png", 9),
    ROI_BLANC("Roi", Couleur.BLANC, "pieces-basic-png/white-king.png", 0),
    ROI_NOIR("Roi", Couleur.NOIR, "pieces-basic-png/black-king.png", 0);

    private final String name;
    private final Couleur couleur;
    private final String imagePath; // Remplacer par le chemin de l'image
    private final int valeur;

    TypePiece(String name, Couleur couleur, String imagePath, int valeur) {
        this.name = name;
        this.couleur = couleur;
        this.imagePath = imagePath;
        this.valeur = valeur;
    }

    public ImageView defImage(String imagePath) {
        ImageView imageViewTemp = null;
        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.out.println("Image non trouvée pour le chemin : " + imagePath);
                return null;
            }
            Image image = new Image(imageStream);
            imageViewTemp = new ImageView(image);
            imageViewTemp.setFitWidth(48);
            imageViewTemp.setFitHeight(50);
            imageViewTemp.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Erreur du chargement du TypePiece " + name + " : " + e.getMessage());
        }
        return imageViewTemp;
    }

    public ImageView getImageView() {
        return defImage(imagePath);  // Créer un nouvel ImageView à chaque appel
    }

    public String getName() {
        return name;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }
}
