package model;


import model.pion.*;
import view.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChessGame {
    private Piece[][] echiquer;
    private static final Logger logger = Logger.getLogger(ChessGame.class.getName());
    private final List<Piece> piecesNoir = new ArrayList<>();
    private final List<Piece> piecesBlanche = new ArrayList<>();
    private static int nbTour = 0;

    public ChessGame() {
        echiquer = new Piece[8][8];
        setData();
    }

    public void setData() {
        Timer.measureExecutionTime(() -> {
        // Initialiser les pions
        for (int i = 0; i < 8; i++) {
            echiquer[i][1] = new Pion(new Coordonnees(i, 1), TypePiece.PION_BLANC); // Pion blanc
            echiquer[i][6] = new Pion(new Coordonnees(i, 6), TypePiece.PION_NOIR);  // Pion noir
        }
        // Initialiser les autres pièces
        echiquer[0][0] = new Tour(new Coordonnees(0, 0), TypePiece.TOUR_BLANC);   // Tour blanche
        echiquer[7][0] = new Tour(new Coordonnees(7, 0), TypePiece.TOUR_BLANC);   // Tour blanche
        echiquer[0][7] = new Tour(new Coordonnees(0, 7), TypePiece.TOUR_NOIR);    // Tour noire
        echiquer[7][7] = new Tour(new Coordonnees(7, 7), TypePiece.TOUR_NOIR);    // Tour noire

        echiquer[1][0] = new Cavalier(new Coordonnees(1, 0), TypePiece.CAVALIER_BLANC);   // Cavalier blanc
        echiquer[6][0] = new Cavalier(new Coordonnees(6, 0), TypePiece.CAVALIER_BLANC);   // Cavalier blanc
        echiquer[1][7] = new Cavalier(new Coordonnees(1, 7), TypePiece.CAVALIER_NOIR);    // Cavalier noir
        echiquer[6][7] = new Cavalier(new Coordonnees(6, 7), TypePiece.CAVALIER_NOIR);    // Cavalier noir

        echiquer[2][0] = new Fou(new Coordonnees(2, 0), TypePiece.FOU_BLANC);   // Fou blanc
        echiquer[5][0] = new Fou(new Coordonnees(5, 0), TypePiece.FOU_BLANC);   // Fou blanc
        echiquer[2][7] = new Fou(new Coordonnees(2, 7), TypePiece.FOU_NOIR);    // Fou noir
        echiquer[5][7] = new Fou(new Coordonnees(5, 7), TypePiece.FOU_NOIR);    // Fou noir

        echiquer[3][0] = new Reine(new Coordonnees(3, 0), TypePiece.REINE_BLANCHE); // Reine blanche
        echiquer[3][7] = new Reine(new Coordonnees(3, 7), TypePiece.REINE_NOIRE);  // Reine noire

        echiquer[4][0] = new Roi(new Coordonnees(4, 0), TypePiece.ROI_BLANC);   // Roi blanc
        echiquer[4][7] = new Roi(new Coordonnees(4, 7), TypePiece.ROI_NOIR);    // Roi noir
        },"setData");
    }


    public boolean deplacerPiece(Coordonnees coordonneesPieceCourante, Coordonnees coordonneesVisee) {
        return Timer.measureExecutionTime(() -> {
        //logger.info("\n ----------------------------------------------------------- \n Entrée dans la méthode deplacerPiece. \n -----------------------------------------------------------");
        System.out.println("Déplacement de " + this + " de " + coordonneesPieceCourante + " à " + coordonneesVisee);
        try {
            Piece pieceCourante = getPiece(coordonneesPieceCourante);

            // Vérifiez que les coordonnées sont valides
            if (!isCoordonneesValides(coordonneesPieceCourante) || !isCoordonneesValides(coordonneesVisee)) {
                logger.severe(String.format("Erreur : Coordonnées hors limites. Actuelles: %s, Destination: %s", coordonneesPieceCourante, coordonneesVisee));
                return false;
            }
            nbTour += 1;
            System.out.println("\n[TOUR: "+nbTour+"]--------------------------------------------------------------");
            List<Coordonnees> possibilites = pieceCourante.getPossibilites(this, coordonneesPieceCourante);
            logger.info("Liste des possibilités : " + possibilites);

            if (possibilites.contains(coordonneesVisee)) {
                // Capture de la pièce si elle est là
                mangerPiece(coordonneesVisee);
                System.out.println("Piece noir capturer :" + piecesBlanche.toString());
                System.out.println("Piece blanche capturer :" + piecesNoir.toString());
                Piece pieceMangee = getPiece(coordonneesVisee);
                if (pieceMangee != null) {
                    System.out.println("Pièce mangée : " + pieceMangee.getCoordonnes().toString());
                    supprimerPiece(coordonneesVisee); // Supprimer la pièce capturée
                }
                setPiece(coordonneesVisee, pieceCourante); // Déplacer la pièce
                supprimerPiece(coordonneesPieceCourante); // Supprimer la pièce de la case d'origine
                uptateEchiquer(); // Mettre à jour l'échiquier
                //System.out.println("--Pièce déplacée--");
                return true;
            }

            nbTour -= 1;
            logger.warning(String.format("Erreur : Déplacement non autorisé vers %s. Options valides : %s", coordonneesVisee, possibilites));
            return false;
        } catch (Exception e) {
            logger.severe(String.format("Erreur lors du déplacement : %s", e.getMessage()));
            return false;
        }
        },"deplacerPiece");
    }


    public boolean mangerPiece(Coordonnees coordonneesVisee) {
        return Timer.measureExecutionTime(() -> {
            Piece pieceVisee = getPiece(coordonneesVisee);
            if (pieceVisee != null) {
                if (pieceVisee.getTypePiece().getCouleur() == Couleur.BLANC) {
                    piecesNoir.add(pieceVisee);
                    return true;
                } else {
                    piecesBlanche.add(pieceVisee);
                    return true;
                }
            }
            return false;
        }, "mangerPiece");
    }





    // Méthode pour vérifier si les coordonnées sont valides
    private boolean isCoordonneesValides(Coordonnees coordonnees) {
        return coordonnees.getX() >= 0 && coordonnees.getX() <= 7 &&
                coordonnees.getY() >= 0 && coordonnees.getY() <= 7;
    }

    public String afficherEchiquierAvecPossibilites(Coordonnees coordonneesSelectionnees) {

        //logger.info("\n ----------------------------------------------------------- \n Entrée dans la méthode afficherEchiquierAvecPossibilites. \n -----------------------------------------------------------");
        StringBuilder sb = new StringBuilder();
        Timer.measureExecutionTime(() -> {
            // En-têtes de colonnes (coordonnées X)
            sb.append("  ");
            for (int x = 0; x < 8; x++) {
                sb.append(x).append(" ");
            }
            sb.append("\n");

            // Récupérer la pièce à la position sélectionnée
            Piece selectedPiece = getPiece(coordonneesSelectionnees);
            List<Coordonnees> possibilities = new ArrayList<>();

            // Si une pièce est sélectionnée, récupérer ses possibilités
            if (selectedPiece != null) {
                possibilities = selectedPiece.getPossibilites(this,coordonneesSelectionnees);
                logger.info(String.format("Liste des possibilités pour la pièce %s : %s", selectedPiece, possibilities));
            } else {
                logger.info(String.format("Aucune pièce sélectionnée à la position : %s", coordonneesSelectionnees));
            }

            // Affichage des lignes de l'échiquier
            for (int y = 0; y < 8; y++) {
                sb.append(y).append(" "); // Afficher les coordonnées Y de 0 à 7

                for (int x = 0; x < 8; x++) {
                    Coordonnees currentCoords = new Coordonnees(x, y);
                    Piece piece = getPiece(currentCoords); // Récupérer la pièce à la position (x, y)

                    // Vérifier si la case est une possibilité
                    if (possibilities.stream().anyMatch(coords -> coords.equals(currentCoords))) {
                        sb.append("o "); // Marquer comme possibilité
                    } else if (piece != null) {
                        sb.append(piece.getSymbol()).append(" "); // Afficher le symbole de la pièce
                    } else {
                        sb.append(". "); // Afficher un point pour les cases vides
                    }
                }
                sb.append("\n"); // Nouvelle ligne après chaque rangée
            }
        }, "afficherEchiquierAvecPossibilites");
        return sb.toString();
    }

    public void uptateEchiquer() {
        afficherEchiquier();
    }

    // Getters & Setters

    public Piece[][] getEchiquer() {
        return echiquer;
    }

    public List<Piece> getPiecesBlanche() {
        return piecesBlanche;
    }

    public List<Piece> getPiecesNoir() {
        return piecesNoir;
    }

    public Piece getPiece(Coordonnees coordonnees) {
        return echiquer[coordonnees.getX()][coordonnees.getY()];
    }

    public Piece setPiece(Coordonnees coordonnees, Piece piece) {
        return echiquer[coordonnees.getX()][coordonnees.getY()] = piece;
    }

    public void supprimerPiece(Piece piece) {
        if (piece != null) {
            setPiece(piece.getCoordonnes(), null);
        }
    }

    public void supprimerPiece(Coordonnees coordonnees) {
        if (getPiece(coordonnees) != null) {
            setPiece(coordonnees, null);
        }
    }

    public String afficherEchiquier() {
        StringBuilder sb = new StringBuilder();
        Timer.measureExecutionTime(() -> {
            // Afficher les indices de colonne
            sb.append("  0 1 2 3 4 5 6 7 \n");

            for (int y = 0; y < 8; y++) {
                sb.append(y); // Afficher l'indice de ligne
                for (int x = 0; x < 8; x++) {
                    // Vérifier si la case contient une pièce
                    if (echiquer[x][y] != null) {
                        sb.append(" ").append(echiquer[x][y].getSymbol()); // Afficher le symbole de la pièce
                    } else {
                        sb.append(" ."); // Case vide
                    }
                }
                sb.append("\n"); // Nouvelle ligne après chaque rangée
            }
        }, "afficherEchiquierAvecPossibilites");
        return sb.toString();
    }

}
