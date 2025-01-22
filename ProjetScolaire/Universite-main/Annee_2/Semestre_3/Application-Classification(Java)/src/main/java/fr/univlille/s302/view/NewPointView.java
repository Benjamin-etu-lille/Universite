package fr.univlille.s302.view;

import java.util.ArrayList;
import java.util.List;

import fr.univlille.s302.controller.Controller;
import fr.univlille.s302.model.DataSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * NewPointView
 *
 * Cette classe représente la vue permettant d'ajouter un nouveau point (enregistrement de données).
 * Elle inclut un formulaire pour saisir les valeurs des attributs et deux boutons : "Ok" et "Cancel".
 * La fenêtre dispose également d'une barre de défilement lorsque les champs de texte excèdent la taille de la fenêtre.
 *
 * @author Benjamin Sere
 * @version 2.0
 * @date 26 novembre 2024
 */
public class NewPointView extends Stage {

    private DataSet model;
    private Controller myController;
    private Scene scene;
    private VBox root;
    private HBox settings;
    private Button validateButton;
    private Button cancelButton;

    public NewPointView(DataSet model, Controller myController) {
        this.model = model;
        this.myController = myController;

        // Initialisation des boutons avec styles
        this.cancelButton = createStyledButton("Cancel", "#ff4d4d");
        this.validateButton = createStyledButton("Ok", "#28a745");

        // Conteneur principal VBox
        this.root = new VBox(15);  // Espacement de 15 entre les éléments
        this.root.setPadding(new Insets(20));
        this.root.setStyle("-fx-background-color: #f9f9f9; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, #aaa, 10, 0, 2, 2);");

        // Ajout des TextField pour chaque attribut
        for (String s : this.model.getAttributes()) {
            TextField tmp = new TextField();
            tmp.setPromptText(s);
            tmp.setStyle("-fx-border-radius: 5; -fx-padding: 10;");
            this.root.getChildren().add(tmp);
        }

        // Créer un ScrollPane pour la VBox (pour permettre le défilement si nécessaire)
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.root);  // Ajouter la VBox dans le ScrollPane
        scrollPane.setFitToHeight(true);   // Ajuster automatiquement la hauteur
        scrollPane.setFitToWidth(true);    // Ajuster automatiquement la largeur

        // Création de la HBox pour les boutons
        this.settings = new HBox(20); // Espacement de 20 entre les boutons
        this.settings.setAlignment(Pos.CENTER);
        this.settings.getChildren().addAll(cancelButton, validateButton);

        // Ajouter la HBox dans la VBox principale
        this.root.getChildren().add(settings);

        // Créer la scène avec un ScrollPane et une taille ajustée
        this.scene = new Scene(scrollPane, 400, 350);
        this.setScene(scene);
        this.setTitle("Add New Point");

        // Affichage de la fenêtre
        this.show();

        // Actions des boutons
        this.cancelButton.setOnAction(e -> this.close());
        this.validateButton.setOnAction(e -> {
            this.myController.addNewPoint(this.getTextFieldData());
            this.close(); // Fermer la fenêtre après ajout du point
        });
    }

    // Fonction pour récupérer les données des champs de texte
    public List<String> getTextFieldData() {
        List<String> res = new ArrayList<>();
        for (Node n : this.root.getChildren()) {
            if (n instanceof TextField) {
                TextField tmp = (TextField) n;
                res.add(tmp.getText());
            }
        }
        return res;
    }

    // Méthode pour styliser les boutons
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-border-radius: 5;");
        return button;
    }
}
