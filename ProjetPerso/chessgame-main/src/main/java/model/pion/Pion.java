package model.pion;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Pion extends Piece {
    private static final Logger logger = Logger.getLogger(Pion.class.getName());
    private boolean previousMoveWasTwoSteps = false;

    public Pion(Coordonnees coordonnes, TypePiece typePiece) { // Changer PieceEnum en TypePiece
        super(coordonnes, typePiece);
    }

    public List<Coordonnees> getPossibilites(ChessGame game, Coordonnees coordonnesPieceCourrante) {
        List<Coordonnees> possibilities = new ArrayList<>();
        //logger.info("\n ----------------------------------------------------------- \n Entrée dans la méthode getPossibilites : \n -----------------------------------------------------------");

        int x = coordonnesPieceCourrante.getX();
        int y = coordonnesPieceCourrante.getY();
        Couleur couleur = getTypePiece().getCouleur();

        // Déterminer la direction de mouvement
        int direction = (couleur == Couleur.BLANC) ? 1 : -1; // Blanc avance vers le haut, Noir vers le bas

        // Avancer d'une case
        int newY = y + direction;
        if (isValidCoord(new Coordonnees(x, newY))) {
            if (game.getPiece(new Coordonnees(x, newY)) == null) {
                possibilities.add(new Coordonnees(x, newY)); // Avancer d'une case
            }
        }

        // Vérifiez si c'est le premier mouvement (deux cases)
        if ((couleur == Couleur.BLANC && y == 1) || (couleur == Couleur.NOIR && y == 6)) {
            if (isValidCoord(new Coordonnees(x, y + direction * 2))) {
                // Vérifiez que les deux cases sont vides
                if (game.getPiece(new Coordonnees(x, newY)) == null && game.getPiece(new Coordonnees(x, newY + direction)) == null) {
                    possibilities.add(new Coordonnees(x, y + direction * 2)); // Avancer de deux cases
                }
            }
        }

        // Prises en diagonale
        int captureY = y + direction; // La ligne de prise
        if (isValidCoord(new Coordonnees(x - 1, captureY))) { // Prise gauche
            Piece pieceGauche = game.getPiece(new Coordonnees(x - 1, captureY));
            if (pieceGauche != null && pieceGauche.getTypePiece().getCouleur() != couleur) {
                possibilities.add(new Coordonnees(x - 1, captureY));
            }
        }
        if (isValidCoord(new Coordonnees(x + 1, captureY))) { // Prise droite
            Piece pieceDroite = game.getPiece(new Coordonnees(x + 1, captureY));
            if (pieceDroite != null && pieceDroite.getTypePiece().getCouleur() != couleur) {
                possibilities.add(new Coordonnees(x + 1, captureY));
            }
        }

        // Interdire le retour en arrière
        // Enlever toute possibilité de mouvement vers l'arrière
        //possibilities.removeIf(coord -> coord.getY() < y);

        return possibilities;
    }






    // Méthode pour vérifier la validité des coordonnées
    private boolean isValidCoord(Coordonnees coord) {
        return coord.getX() >= 0 && coord.getX() <= 7 && coord.getY() >= 0 && coord.getY() <= 7;
    }

    @Override
    public String getSymbol() {
        return typePiece.getCouleur() == Couleur.BLANC ? "P" : "p"; // "P" pour le pion blanc, "p" pour le pion noir
    }

    public boolean getPreviousMoveWasTwoSteps() {
        return previousMoveWasTwoSteps;
    }

    public void setPreviousMoveWasTwoSteps(boolean previousMoveWasTwoSteps) {
        this.previousMoveWasTwoSteps = previousMoveWasTwoSteps;
    }

    @Override
    public String toString() {
        return getSymbol();
    }
}
