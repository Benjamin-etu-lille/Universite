package model;

import java.util.List;

public abstract class Piece {
    protected Coordonnees coordonnes;
    protected TypePiece typePiece; // Utiliser TypePiece à la place de PieceEnum

    public Piece(Coordonnees coordonnes, TypePiece typePiece) {
        this.coordonnes = coordonnes;
        this.typePiece = typePiece;
    }

    public abstract String getSymbol();

    public abstract List<Coordonnees> getPossibilites(ChessGame game,Coordonnees coordonnesPieceCourrante);

    public TypePiece getTypePiece() { // Changer le type de retour en TypePiece
        return typePiece;
    }

    public Coordonnees getCoordonnes() {
        return coordonnes;
    }

    public void setCoordonnes(Coordonnees nouvellesCoordonnees) {
        this.coordonnes = nouvellesCoordonnees;
    }
}
