

import java.io.File;


;

public class ChargerRecette {

    final static String SEPARATOR = System.getProperty("file.separator");
    final static String FILENAME = "res" + SEPARATOR + "recettes.csv";
    
    private static final String FICHIER_ingredient_USER = "res/ingredientUser.csv";

    public boolean chargerDonnee() {
        File fichier = new File(FICHIER_ingredient_USER);

        // Vérifier si le fichier existe
        if (!fichier.exists()) {
            // Si le fichier n'existe pas, envoyer un message à l'utilisateur
            System.out.println("Le fichier 'ingredientUser' n'existe pas dans le répertoire 'res'. Veuillez ajouter des ingrédients dans la liste.");
            return false;
        }
        return true;
    }

}
