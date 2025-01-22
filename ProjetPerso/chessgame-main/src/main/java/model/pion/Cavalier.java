package model.pion;

import model.*;


import javax.swing.text.html.ImageView;
import java.util.ArrayList;
import java.util.List;

public class Cavalier extends Piece {

    public Cavalier(Coordonnees coordonnes, TypePiece typePiece) { // Changer PieceEnum en TypePiece
        super(coordonnes, typePiece);
    }

    @Override
    public String getSymbol() {
        return typePiece.getCouleur() == Couleur.BLANC ? "C" : "c"; // "C" pour le cavalier blanc, "c" pour le cavalier noir
    }

    @Override
    public String toString() {
        return getSymbol();
    }

    @Override
    public List<Coordonnees> getPossibilites(ChessGame game, Coordonnees coordonnesPieceCourrante) {
        List<Coordonnees> possibilities = new ArrayList<>();
        int x = coordonnesPieceCourrante.getX();
        int y = coordonnesPieceCourrante.getY();
        Couleur couleur = this.getTypePiece().getCouleur();

        // Définir les mouvements possibles du cavalier
        int[][] mouvements = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] mouvement : mouvements) {
            int newX = x + mouvement[0];
            int newY = y + mouvement[1];
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
