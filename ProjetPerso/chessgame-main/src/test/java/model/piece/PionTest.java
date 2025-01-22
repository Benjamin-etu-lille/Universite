package model.piece;

import model.ChessGame;
import model.Coordonnees;
import model.TypePiece;
import model.pion.Pion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PionTest {
    private ChessGame chessGame;
    private Pion pionBlanc;
    private Pion pionNoir;

    @BeforeEach
    public void setUp() {
        // Initialiser l'échiquier et les pions
        chessGame = new ChessGame();
        pionBlanc = new Pion(new Coordonnees(0, 1), TypePiece.PION_BLANC);
        pionNoir = new Pion(new Coordonnees(0, 6), TypePiece.PION_NOIR);
        chessGame.setPiece(pionBlanc.getCoordonnes(), pionBlanc);
        chessGame.setPiece(pionNoir.getCoordonnes(), pionNoir);
    }

    @Test
    public void testDeplacementUnite() {
        // Test du déplacement de deux cases pour un pion blanc
        assertTrue(chessGame.deplacerPiece(new Coordonnees(0, 1), new Coordonnees(0, 3)),
                "Le pion devrait pouvoir avancer de deux cases.");
        assertEquals(pionBlanc, chessGame.getPiece(new Coordonnees(0, 3)), "Le pion blanc devrait être à (0, 3).");
        assertNull(chessGame.getPiece(new Coordonnees(0, 1)), "La case (0, 1) devrait être vide.");

        // Test de déplacement interdit en arrière
        assertFalse(chessGame.deplacerPiece(new Coordonnees(0, 3), new Coordonnees(0, 2)),
                "Le pion ne devrait pas pouvoir reculer.");

        // Test du déplacement d'un pion blanc
        assertTrue(chessGame.deplacerPiece(new Coordonnees(0, 3), new Coordonnees(0, 4)),
                "Le pion devrait pouvoir avancer d'une case.");
        assertEquals(pionBlanc, chessGame.getPiece(new Coordonnees(0, 4)), "Le pion blanc devrait être à (0, 4).");
        assertNull(chessGame.getPiece(new Coordonnees(0, 3)), "La case (0, 3) devrait être vide.");
    }

    @Test
    public void testCapture() {
        // Placer un pion noir à une position diagonale
        Pion pionNoirAdverse = new Pion(new Coordonnees(1, 2), TypePiece.PION_NOIR);
        chessGame.setPiece(pionNoirAdverse.getCoordonnes(), pionNoirAdverse);


        assertFalse(chessGame.deplacerPiece(new Coordonnees(1, 1), new Coordonnees(1, 2)),
                "Le pion blanc devrait pas pouvoir capturer le pion noir.");

        // Test de capture diagonale à droite
        assertTrue(chessGame.deplacerPiece(new Coordonnees(2, 1), new Coordonnees(1, 2)),
                "Le pion blanc devrait pouvoir capturer le pion noir.");
        assertNull(chessGame.getPiece(new Coordonnees(0, 4)), "La case (2, 1) devrait être vide.");
        //assertTrue(pionBlanc.equals(chessGame.getPiece(new Coordonnees(1, 2))), "Le pion blanc devrait être à (1, 2).");

    }

    @Test
    public void testDeplacementInterdit() {
        // Tenter de déplacer un pion sur une case occupée par une pièce alliée
        Pion pionBlancAlly = new Pion(new Coordonnees(1, 2), TypePiece.PION_BLANC);
        chessGame.setPiece(pionBlancAlly.getCoordonnes(), pionBlancAlly);

        assertFalse(chessGame.deplacerPiece(new Coordonnees(0, 4), new Coordonnees(1, 2)),
                "Le pion blanc ne devrait pas pouvoir se déplacer sur une case occupée par un allié.");
    }

    @Test
    public void testDeplacementHorsLimites() {
        // Tenter un déplacement hors du plateau
        assertFalse(chessGame.deplacerPiece(new Coordonnees(0, 1), new Coordonnees(0, -1)),
                "Le pion ne devrait pas pouvoir se déplacer en dehors du plateau.");
    }
}