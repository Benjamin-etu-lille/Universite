package fr.univlille.s302.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe {@code ErrorView} qui gère l'affichage des messages d'erreur.
 *
 * Cette classe utilise une boîte de dialogue JavaFX pour afficher les messages
 * d'erreur à l'utilisateur. Elle peut être utilisée dans n'importe quelle partie
 * de l'application où une erreur doit être signalée.
 *
 * Exemple d'utilisation :
 * <pre>
 *     new ErrorView("Une erreur est survenue lors du traitement.");
 * </pre>
 *
 * @author Benjamin Sere & Thibault Croisier
 * @version 1.0
 */
public class ErrorView {

    /**
     * Constructeur qui crée une nouvelle instance de {@code ErrorView} et affiche
     * un message d'erreur.
     *
     * @param error le message d'erreur à afficher
     */
    public ErrorView(String error) {
        showErrorPopup(error);
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec le message spécifié.
     *
     * @param message le message d'erreur à afficher
     */
    public void showErrorPopup(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error : " + message);

        alert.showAndWait();
    }
}