import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.ChessGame;

import model.Coordonnees;
import model.Couleur; // Assurez-vous d'importer la classe Couleur
import view.ChessView;
import view.Timer;


import java.util.Scanner;

public class Main extends Application {
    private static ChessGame chessGame = new ChessGame();

    @Override
    public void start(Stage primaryStage) {
        Timer.setTimingEnabled(false);

        int size = 8;


        ChessView chessView = new ChessView(size, chessGame);

        Scene scene = new Scene(chessView, 480, 480);
        primaryStage.setTitle("Jeu d'échecs");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lancer l'application


//        Scanner scanner = new Scanner(System.in);
//        Couleur joueurActuel = Couleur.BLANC; // Commence avec les blancs
//
//
//        while (true) {
//            System.out.println(chessGame.afficherEchiquier());
//            System.out.println("Tour du joueur : " + joueurActuel);
//            System.out.println("Piece noir capturere : " + chessGame.getPiecesBlanche().toString());
//            System.out.println("Piece blache capturere : " + chessGame.getPiecesNoir().toString());
//
//            System.out.println("Entrez les coordonnées de la pièce à déplacer (x y) ou 'q' pour quitter :");
//            String input = scanner.nextLine();
//
//            if (input.equalsIgnoreCase("q")) {
//                System.out.println("Fin du jeu.");
//                break;
//            }
//
//            String[] coords = input.split(" ");
//            if (coords.length != 2) {
//                System.out.println("Entrée invalide. Veuillez entrer deux coordonnées.");
//                continue;
//            }
//
//            try {
//                int x = Integer.parseInt(coords[0]);
//                int y = Integer.parseInt(coords[1]);
//                Coordonnees coordonneesPiece = new Coordonnees(x, y);
//
//                if (chessGame.getPiece(coordonneesPiece) == null) {
//                    System.out.println("Aucune pièce à cette position.");
//                    continue;
//                }
//
//                // Vérifiez que la pièce appartient au joueur actuel
//                if (chessGame.getPiece(coordonneesPiece).getTypePiece().getCouleur() != joueurActuel) {
//                    System.out.println("Ce n'est pas votre pièce.");
//                    continue;
//                }
//
//                System.out.println(chessGame.afficherEchiquierAvecPossibilites(coordonneesPiece));
//                System.out.println("Entrez les coordonnées de destination (x y) :");
//                String destInput = scanner.nextLine();
//                String[] destCoords = destInput.split(" ");
//                if (destCoords.length != 2) {
//                    System.out.println("Entrée invalide. Veuillez entrer deux coordonnées.");
//                    continue;
//                }
//
//                int destX = Integer.parseInt(destCoords[0]);
//                int destY = Integer.parseInt(destCoords[1]);
//                Coordonnees coordonneesDestination = new Coordonnees(destX, destY);
//
//                // Déplacez la pièce et vérifiez si le mouvement est réussi
//                if (chessGame.deplacerPiece(coordonneesPiece, coordonneesDestination)) {
//                    // Changer de joueur après un mouvement réussi
//                    joueurActuel = (joueurActuel == Couleur.BLANC) ? Couleur.NOIR : Couleur.BLANC;
//                } else {
//                    System.out.println("Déplacement non autorisé.");
//                }
//
//            } catch (NumberFormatException e) {
//                System.out.println("Erreur : Entrée invalide. Coordonnées doivent être des entiers.");
//            } catch (IllegalArgumentException e) {
//                System.out.println("Erreur : " + e.getMessage());
//            } catch (Exception e) {
//                System.out.println("Erreur inattendue : " + e.getMessage());
//            }
//        }
//
//        scanner.close();
    }
}