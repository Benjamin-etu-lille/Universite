package model.pion;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Roi extends Piece {

    public Roi(Coordonnees coordonnes, TypePiece typePiece) { // Changer PieceEnum en TypePiece
        super(coordonnes, typePiece);
    }

    @Override
    public String getSymbol() {
        return typePiece.getCouleur() == Couleur.BLANC ? "K" : "k"; // "K" pour le roi blanc, "k" pour le roi noir
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

        // Directions de mouvement (une case dans toutes les directions)
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        // Parcourir chaque direction
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            Coordonnees nouvelleCoordonnees = new Coordonnees(newX, newY);

            // Vérifiez si la nouvelle coordonnée est valide
            if (isValidCoord(nouvelleCoordonnees)) {
                Piece pieceCible = game.getPiece(nouvelleCoordonnees);
                // Si la case est vide ou contient une pièce de l'adversaire, ajoutez-la aux possibilités
                if (pieceCible == null || pieceCible.getTypePiece().getCouleur() != couleur) {
                    possibilities.add(nouvelleCoordonnees);
                }
            }
        }

        return possibilities;
    }

    // Méthode pour vérifier la validité des coordonnées
    private boolean isValidCoord(Coordonnees coord) {
        return coord.getX() >= 0 && coord.getX() <= 7 && coord.getY() >= 0 && coord.getY() <= 7;
    }
}
