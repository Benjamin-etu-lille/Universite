package fr.univlille.s302;

import java.io.IOException;

import fr.univlille.s302.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale {@code Main} de l'application.
 *
 * Cette classe étend {@link Application} et sert de point d'entrée pour l'application
 * JavaFX. Elle lance l'application et initialise le contrôleur principal qui gère
 * la logique de l'application.
 *
 * @author Thibault Croisier & Benjamin Sere & Louis Bedu
 * @version 2.2
 */
public class Main extends Application {

	/**
	 * Méthode principale qui démarre l'application.
	 *
	 * @param args les arguments de ligne de commande
	 * @throws IOException si une erreur d'entrée/sortie se produit
	 */
	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	/**
	 * Méthode appelée au démarrage de l'application.
	 *
	 * @param primaryStage la fenêtre principale de l'application
	 * @throws Exception si une erreur se produit lors de l'initialisation
	 */
	public void start(Stage primaryStage) throws Exception {
		new Controller();
	}
}