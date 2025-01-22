package model.pion;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Reine extends Piece {

    public Reine(Coordonnees coordonnes, TypePiece typePiece) { // Changer PieceEnum en TypePiece
        super(coordonnes, typePiece);
    }

    @Override
    public String getSymbol() {
        return typePiece.getCouleur() == Couleur.BLANC ? "Q" : "q"; // "Q" pour la reine blanche, "q" pour la reine noire
    }

    public String toString() {
        return getSymbol();
    }

    @Override
    public List<Coordonnees> getPossibilites(ChessGame game, Coordonnees coordonnesPieceCourrante) {
        List<Coordonnees> possibilities = new ArrayList<>();
        int x = coordonnesPieceCourrante.getX();
        int y = coordonnesPieceCourrante.getY();
        Couleur couleur = this.getTypePiece().getCouleur();

        // Directions de mouvement (horizontal, vertical, diagonal)
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}, // Horizontal et vertical
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonales
        };

        // Parcourir chaque direction
        for (int[] direction : directions) {
            int newX = x;
            int newY = y;

            // Continuer dans la direction jusqu'à ce que l'on sorte des limites de l'échiquier ou rencontre une pièce
            while (true) {
                newX += direction[0];
                newY += direction[1];
                Coordonnees nouvelleCoordonnees = new Coordonnees(newX, newY);

                // Vérifiez si la nouvelle coordonnée est valide
                if (!isValidCoord(nouvelleCoordonnees)) {
                    break; // Sortir si hors des limites
                }

                Piece pieceCible = game.getPiece(nouvelleCoordonnees);
                if (pieceCible == null) {
                    possibilities.add(nouvelleCoordonnees); // Case vide
                } else {
                    // Si la case contient une pièce de l'adversaire, ajoutez-la aux possibilités et arrêtez le mouvement
                    if (pieceCible.getTypePiece().getCouleur() != couleur) {
                        possibilities.add(nouvelleCoordonnees);
                    }
                    break; // Arrêtez le mouvement si une pièce est rencontrée
                }
            }
        }

        return possibilities;
    }

    private boolean isValidCoord(Coordonnees coord) {
        return coord.getX() >= 0 && coord.getX() <= 7 && coord.getY() >= 0 && coord.getY() <= 7;
    }
}
