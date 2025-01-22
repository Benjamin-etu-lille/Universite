import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AjouterIngredient {
    final static String SEPARATOR = System.getProperty("file.separator");
    final static String FILENAME = "res" + SEPARATOR + "ingredient.csv";
    final static String FICHIER_ingredient_USER = "res" + SEPARATOR + "ingredientUser.csv";
    public static final String CLEAR_TERMINAL = "\033\143";

    private ArrayList<String[]> listeingredientUser = new ArrayList<>();
    private ArrayList<String[]> finaList = new ArrayList<>();

    public boolean valideeingredient(String ingredient, double quantite) throws IOException {
        for (Object[] ingredientCSV : Ventilateur.ventilation(FILENAME)) {
            if (ingredient.equalsIgnoreCase(ingredientCSV[0].toString())) {
                if (ingredientCSV.length >= 4) {
                    ingredientCSV[3] = String.valueOf(quantite);
                } else {
                    String[] temp = new String[4];
                    System.arraycopy(ingredientCSV, 0, temp, 0, ingredientCSV.length);
                    temp[3] = String.valueOf(quantite);
                    ingredientCSV = temp;
                }
                finaList.add((String[])ingredientCSV);
                return true;
            }
        }
        return false;
    }

    public boolean chargerDonnee() {
        File fichier = new File(FICHIER_ingredient_USER);
        if (!fichier.exists()) {
            System.out.println("Le fichier 'ingredientUser.csv' n'existe pas dans le répertoire 'res'. Veuillez ajouter des ingrédients dans la liste.");
            return false;
        }
        return true;
    }

    public void ajouterIngredient() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean condition = true;

        while (condition) {
            System.out.println(CLEAR_TERMINAL);
            System.out.println("\n==============================");
            System.out.println("      Ajouter un Ingrédient    ");
            System.out.println("==============================\n");

            String ingredient = demanderNomIngredient(scanner);
            double quantite = demanderQuantite(scanner);
            String unite = demanderUnite(scanner);

            if (valideeingredient(ingredient, quantite)) {
                listeingredientUser.add(new String[]{ingredient, String.valueOf(quantite), unite});
                System.out.println("Ingrédient ajouté : " + ingredient + ", Quantité : " + quantite + " " + unite);
            } else {
                System.out.println("Ingrédient invalide. Veuillez entrer un ingrédient valide.");
            }

            condition = demanderSiContinuer(scanner);
        }
    }

    private String demanderNomIngredient(Scanner scanner) {
        System.out.println("Veuillez entrer le nom de l'ingrédient :");
        return scanner.nextLine().trim();
    }

    private double demanderQuantite(Scanner scanner) {
        double quantite = -1;
        while (quantite < 0) {
            System.out.println("Veuillez entrer la quantité de cet ingrédient (en nombre décimal) :");
            String quantiteStr = scanner.nextLine().trim();
            try {
                quantite = Double.parseDouble(quantiteStr);
                if (quantite < 0) {
                    System.out.println("La quantité ne peut pas être négative. Veuillez entrer une valeur valide.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Quantité invalide. Veuillez entrer un nombre décimal.");
            }
        }
        return quantite;
    }

    private String demanderUnite(Scanner scanner) {
        String unite = "";
        boolean uniteValide = false;
        while (!uniteValide) {
            System.out.println("Sélectionnez une unité svp : ");
            for (Unitees unitees : Unitees.values()) {
                System.out.println(unitees);
            }

            unite = scanner.nextLine().trim().toUpperCase();
            if (isValidUnite(unite)) {
                uniteValide = true;
            } else {
                System.out.println("Écrivez une unité valide !");
            }
        }
        return unite;
    }

    private boolean demanderSiContinuer(Scanner scanner) {
        boolean reponseValide = false;
        boolean continuer = false;
        while (!reponseValide) {
            System.out.println("Voulez-vous entrer un autre ingrédient ? (Oui ou Non)");
            String reponse = scanner.nextLine().trim();

            if (reponse.equalsIgnoreCase("Oui")) {
                reponseValide = true;
                continuer = true;
            } else if (reponse.equalsIgnoreCase("Non")) {
                reponseValide = true;
                continuer = false;
            } else {
                System.out.println("Réponse invalide. Veuillez entrer 'Oui' ou 'Non'.");
            }
        }
        return continuer;
    }

    public void saveIngredient() throws IOException {
        Ventilateur.saveToCSV(FICHIER_ingredient_USER, listeingredientUser);
    }

    public static boolean isValidUnite(String userInput) {
        try {
            Unitees.valueOf(userInput);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static void afficherIngredients() throws IOException {
        List<Object[]> listeIngrediant = Ventilateur.ventilation(FICHIER_ingredient_USER);

        // En-tête du tableau
        System.out.println("Liste des ingrédients ajoutés :");
        System.out.println("_____________________________________________________");
        System.out.println("|       INGREDIENTS       |        QUANTITES        |");
        System.out.println("|_________________________|_ _____(en grammes)______|");

        // Affichage des ingrédients formatés
        for (Object[] ingredient : listeIngrediant) {
            String ingredientName = longueurString(ingredient[0].toString(), 23); // Ajuster pour une colonne de 25 caractères
            String ingredientQuantity = longueurString(ingredient[1].toString(), 23); // Ajuster pour une colonne de 25 caractères

            System.out.println("| " + ingredientName + " | " + ingredientQuantity + " |");
        // Fin du tableau
        System.out.println("|_________________________|_________________________|");
        }
    }

    // Méthode pour ajuster la longueur de la chaîne avec des espaces ou la tronquer
    public static String longueurString(String chaine, int taille) {
        // Tronque la chaîne si elle est trop longue
        if (chaine.length() > taille) {
            return chaine.substring(0, taille);
        }
        // Ajoute des espaces jusqu'à la taille demandée
        return String.format("%-" + taille + "s", chaine);
    }
}
