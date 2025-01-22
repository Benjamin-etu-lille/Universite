package fr.univlille.s302.view;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Classe {@code FileExplorerView} qui permet d'ouvrir un explorateur de fichiers
 * pour sélectionner des fichiers.
 *
 * Cette classe étend {@link Stage} et utilise {@link FileChooser} pour permettre
 * à l'utilisateur de choisir un fichier CSV. Elle configure le dialog d'ouverture
 * de fichier avec des filtres d'extension appropriés et un répertoire initial.
 *
 * Exemple d'utilisation :
 * <pre>
 *     FileExplorerView fileExplorer = new FileExplorerView();
 *     String filePath = fileExplorer.open();
 * </pre>
 *
 * @author Louis Bedu
 * @version 1.0
 */
public class FileExplorerView extends Stage {
    private FileChooser fileChooser = new FileChooser();

    /**
     * Ouvre une boîte de dialogue pour sélectionner un fichier.
     *
     * @return le chemin absolu du fichier sélectionné, ou {@code null} si aucun fichier
     *         n'a été sélectionné.
     */
    public String open() {
        fileChooser.setTitle("Sélectionnez un fichier (le nom du fichier doit contenir le type de donné à classifier ex:pokemon.csv)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma-separated values", "*.csv"));
        fileChooser.setInitialDirectory(new File("res"));
        File selectedFile = fileChooser.showOpenDialog(this);
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }
}
