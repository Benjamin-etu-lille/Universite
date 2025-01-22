package view;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import model.ChessGame;
import model.Coordonnees;
import model.Couleur;
import model.Piece;

import java.util.ArrayList;
import java.util.List;

public class ChessView extends GridPane {
    private Couleur joueurActuel = Couleur.BLANC;
    private int size;
    private Piece selectedPiece = null;
    private double offsetX;
    private double offsetY;
    private StackPane selectedStack = null;
    private Rectangle highlightedSquare = null; // Pour la surbrillance de la case
    private Rectangle draggingIndicator; // Pour le rectangle creux
    private ChessGame board;
    private List<Node> moveIndicators = new ArrayList<>();
    private Piece lastMovedPiece; // Dernière pièce jouée
    private Rectangle lastMovedHighlight;

    public ChessView(int size, ChessGame board) {
        this.size = size;
        this.board = board;
        drawBoard();
        updateBoard(board);
    }

    private void drawBoard() {
        Color lightColor = Color.web("#B0E0E6");
        Color darkColor = Color.web("#4682B4");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Rectangle square = new Rectangle(60, 60);
                square.setFill((i + j) % 2 == 0 ? lightColor : darkColor);
                add(square, j, i);
            }
        }
    }

    public void updateBoard(ChessGame board) {
        getChildren().removeIf(node -> node instanceof StackPane);
        Piece[][] echiquer = board.getEchiquer();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = echiquer[x][y];
                if (piece != null) {
                    Node pieceView = createPieceView(piece);
                    StackPane stack = new StackPane(pieceView);
                    stack.setUserData(new Coordonnees(x, y));
                    stack.setOnMousePressed(event -> handlePieceSelection(event, piece, stack));
                    stack.setOnMouseDragged(event -> handlePieceDragging(event));
                    stack.setOnMouseReleased(event -> handlePieceRelease(event, piece));
                    add(stack, x, y);
                }
            }
        }
    }

    private void handlePieceSelection(MouseEvent event, Piece piece, StackPane stack) {
        if (piece.getTypePiece().getCouleur() != joueurActuel) return;

        // Retirer la surbrillance de l'ancienne case d'origine
        removeOriginHighlight();

        // Initialiser le rectangle de surbrillance
        draggingIndicator = new Rectangle(56, 56);
        draggingIndicator.setStroke(Color.YELLOWGREEN.deriveColor(0, 1, 1, 0.4));
        draggingIndicator.setFill(null); // Transparent
        draggingIndicator.setStrokeWidth(4); // Épaisseur du contour
        add(draggingIndicator, 0, 0); // Ajouter à la grille, position temporaire

        selectedPiece = piece;
        selectedStack = stack;
        offsetX = event.getX();
        offsetY = event.getY();

        // Mettre en surbrillance la case d'origine
        highlightOriginSquare(stack);

        showPossibleMoves(piece.getPossibilites(board, piece.getCoordonnes())); // Afficher les mouvements possibles
        selectedStack.toFront();
    }

    private void showPossibleMoves(List<Coordonnees> possibilities) {
        clearMoveIndicators();
        for (Coordonnees coord : possibilities) {
            int x = coord.getX();
            int y = coord.getY();
            if(board.mangerPiece(coord)) {
                StackPane moveIndicatorPane = new StackPane();
                Circle indicator = new Circle(25.5);
                indicator.setFill(null);
                indicator.setStroke(Color.DARKBLUE.deriveColor(0, 1, 1, 0.35));
                indicator.setStrokeWidth(7);
                moveIndicatorPane.getChildren().add(indicator);
                add(moveIndicatorPane, x, y);
                moveIndicators.add(moveIndicatorPane);

            }else {
                StackPane moveIndicatorPane = new StackPane();
                Circle indicator = new Circle(10);
                indicator.setFill(Color.DARKBLUE.deriveColor(0, 1, 1, 0.35));
                moveIndicatorPane.getChildren().add(indicator);
                add(moveIndicatorPane, x, y);
                moveIndicators.add(moveIndicatorPane);
            }
        }
    }

    private void highlightOriginSquare(StackPane stack) {
        int origX = GridPane.getColumnIndex(stack);
        int origY = GridPane.getRowIndex(stack);
        highlightedSquare = new Rectangle(60, 60);
        highlightedSquare.setFill(Color.YELLOWGREEN.deriveColor(0, 1, 1, 0.5));
        add(highlightedSquare, origX, origY);
        highlightedSquare.toFront();
    }

    private void handlePieceDragging(MouseEvent event) {
        if (selectedPiece != null && selectedStack != null) {
            double newTranslateX = event.getSceneX() - offsetX - selectedStack.getLayoutX();
            double newTranslateY = event.getSceneY() - offsetY - selectedStack.getLayoutY();

            // Vérifiez que selectedStack est bien visible
            //System.out.println("Dragging to: (" + newTranslateX + ", " + newTranslateY + ")");

            selectedStack.setTranslateX(newTranslateX);
            selectedStack.setTranslateY(newTranslateY);

            // Mettre à jour la position du rectangle creux
            updateDraggingIndicator(event);
        }
    }

    private void updateDraggingIndicator(MouseEvent event) {
        if (draggingIndicator != null) {
            int x = Math.max(0, Math.min(7, (int) (event.getSceneX() / 60)));
            int y = Math.max(0, Math.min(7, (int) (event.getSceneY() / 60)));

            // Mettre à jour la position du rectangle creux
            draggingIndicator.setTranslateX(x * 60);
            draggingIndicator.setTranslateY(y * 60);
        }
    }

    private void handlePieceRelease(MouseEvent event, Piece piece) {
        if (selectedPiece != null && selectedStack != null) {
            // Retirer le rectangle de surbrillance
            if (draggingIndicator != null) {
                getChildren().remove(draggingIndicator);
                draggingIndicator = null; // Réinitialiser
            }

            // Retirer la surbrillance de la case d'origine
            removeOriginHighlight();

            int targetX = Math.max(0, Math.min(7, (int) (event.getSceneX() / 60)));
            int targetY = Math.max(0, Math.min(7, (int) (event.getSceneY() / 60)));

            // Afficher les coordonnées cibles
            //System.out.println("Target coordinates: (" + targetX + ", " + targetY + ")");

            Coordonnees targetCoord = new Coordonnees(targetX, targetY);
            Coordonnees originalCoord = selectedPiece.getCoordonnes();

            // Afficher les coordonnées d'origine
            System.out.println("Original coordinates: (" + originalCoord.getX() + ", " + originalCoord.getY() + ")");

            List<Coordonnees> possibleMoves = selectedPiece.getPossibilites(board, originalCoord);

            // Afficher les mouvements possibles
            System.out.println("Possible moves: " + possibleMoves);

            if (possibleMoves.contains(targetCoord)) {
                // Si le mouvement est valide, déplacer la pièce
                board.deplacerPiece(originalCoord, targetCoord);
                selectedPiece.setCoordonnes(targetCoord);
                lastMovedPiece = selectedPiece; // Mettre à jour la dernière pièce jouée

                selectedStack.setUserData(targetCoord);
                animatePieceMovement(selectedStack, targetX, targetY);
                updateBoard(board);

                // Mettre en surbrillance la case de la dernière pièce jouée
                highlightLastMovedSquare(originalCoord);

                joueurActuel = (joueurActuel == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
            } else {
                // Si le mouvement n'est pas valide, faire une animation de retour à la position d'origine
                animatePieceMovement(selectedStack, originalCoord.getX(), originalCoord.getY());
                // Mettez à jour le plateau après avoir annulé le mouvement
                updateBoard(board);
            }

            clearMoveIndicators();
            selectedPiece = null;
            selectedStack = null;
        }
    }

    public void testAnimation(StackPane piece) {
        animatePieceMovement(piece, 4, 4); // Déplacez la pièce à la position (4, 4)
    }

    private void highlightLastMovedSquare(Coordonnees coord) {
        int origX = coord.getX();
        int origY = coord.getY();
        lastMovedHighlight = new Rectangle(60, 60);
        lastMovedHighlight.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.5)); // Couleur rouge pour la dernière pièce
        add(lastMovedHighlight, origX, origY);
        lastMovedHighlight.toFront();
    }

    private void removeOriginHighlight() {
        if (highlightedSquare != null) {
            getChildren().remove(highlightedSquare);
            highlightedSquare = null; // Réinitialiser
        }
        if (lastMovedHighlight != null) {
            getChildren().remove(lastMovedHighlight);
            lastMovedHighlight = null; // Réinitialiser
        }
    }

    private void animatePieceMovement(StackPane piecePane, int targetX, int targetY) {
        //System.out.println("animatePieceMovement targetX : " + targetX + " targetY : " + targetY);

        // Récupérer les coordonnées actuelles dans le GridPane
        int currentX = GridPane.getColumnIndex(piecePane);
        int currentY = GridPane.getRowIndex(piecePane);
        //System.out.println("Current coordinates: (" + currentX + ", " + currentY + ")");

        // Calculer la position cible
        double targetPosX = targetX * 60; // Position cible X
        double targetPosY = targetY * 60; // Position cible Y

        // Créer la transition
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), piecePane);

        // Position de départ : prendre en compte la translation actuelle
        double startX = (currentX * 60) + piecePane.getTranslateX();
        double startY = (currentY * 60) + piecePane.getTranslateY();

        // Ajuster la translation pour l'animation
        transition.setFromX(startX);
        transition.setFromY(startY);
        transition.setToX(targetPosX - startX); // Ajuster pour la destination
        transition.setToY(targetPosY - startY); // Ajuster pour la destination

        // Déboguer la position de départ et d'arrivée
        //System.out.println("Animating from: (" + startX + ", " + startY + ") to: (" + targetPosX + ", " + targetPosY + ")");
        //System.out.println("Before animation: translateX = " + piecePane.getTranslateX() + ", translateY = " + piecePane.getTranslateY());

        transition.setOnFinished(e -> {
            // Réinitialiser la translation à zéro après l'animation
            piecePane.setTranslateX(0);
            piecePane.setTranslateY(0);
            // Mettre à jour la position dans le GridPane
            GridPane.setColumnIndex(piecePane, targetX);
            GridPane.setRowIndex(piecePane, targetY);
            piecePane.toFront();
        });

        transition.play();
    }

    private Node createPieceView(Piece piece) {
        return piece.getTypePiece().getImageView();
    }

    private void clearMoveIndicators() {
        for (Node indicator : moveIndicators) {
            getChildren().remove(indicator);
        }
        moveIndicators.clear();
    }

    public StackPane getSelectedStack() {
        return selectedStack;
    }
}