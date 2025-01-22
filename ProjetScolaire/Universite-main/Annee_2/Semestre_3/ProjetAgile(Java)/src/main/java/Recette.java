

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class Recette<T> {

    final static String SEPARATOR = System.getProperty("file.separator");
    final static String FILENAME = "res" + SEPARATOR + "recettes.csv";
    private static final String FICHIER_ingredient_USER = "res/ingredientUser.csv";


    private String name;
    //private ArrayList<String> ingredients;
    private List<T> ingredients;
    private String description;
    private URL source;
    private int calories;

    public Recette(){}
    
    //potentiellement pouvoir rajouter une recette avec le lien directement
    public Recette(String name, List<T> ingredients, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;

        for(int i=0; i<this.ingredients.size(); i++){
            //this.calories+= this.ingredients.get(i).getCalories(); on a pas encore ajouter les calorie dans les recettes 
        }
    }

    // Méthode pour lire le CSV et retourner une liste de recettes
    public static List<Recette> parseCSV(String filePath) throws IOException {
        List<Recette> recettes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    List<String> ingredients = new ArrayList<>();
                    String[] ingParts = parts[1].trim().split(";");
                    for (String ing : ingParts) {
                        ingredients.add(ing.trim());
                    }
                    String description = parts[2].trim();

                    // Créer une recette sans source URL
                    recettes.add(new Recette(name, ingredients, description));
                } else if (parts.length == 4) {
                    String name = parts[0].trim();
                    List<String> ingredients = new ArrayList<>();
                    String[] ingParts = parts[1].trim().split(";");
                    for (String ing : ingParts) {
                        ingredients.add(ing.trim());
                    }
                    String description = parts[2].trim();

                    // Créer une recette avec une source URL
                    recettes.add(new Recette(name, ingredients, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recettes;
    }

    public String toString() {
        return "Recette{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", description='" + description + '\'' +
                ", source=" + source +
                ", calories=" + calories +
                '}';
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getIngredients() {
        return this.ingredients;
    }

    public void setingredients(List<T> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getSource() {
        return this.source;
    }

    public void setSource(URL source) {
        this.source = source;
    }

    public int getCalories() {
        return this.calories;
    }

    public void chercherRecette() throws IOException {
        List<Recette> recettes = Recette.parseCSV(FILENAME);
        List<T[]> ingredientUser = Ventilateur.ventilation(FICHIER_ingredient_USER);

        for (Recette<T> recette : recettes) {
            List<T> ingredientsManquants = getIngredientsManquants(recette, ingredientUser);
            int nbIngredientsDisponibles = recette.getIngredients().size() - ingredientsManquants.size();
            double pourcentageDispo = calculatePourcentageDisponible(nbIngredientsDisponibles, recette.getIngredients().size());

            afficherRecette(recette, nbIngredientsDisponibles, ingredientsManquants, pourcentageDispo);
        }
    }

    // Vérifie les ingrédients manquants pour une recette donnée
    private List<T> getIngredientsManquants(Recette<T> recette, List<T[]> ingredientUser) {
        List<T> ingredientsManquants = new ArrayList<>();
        for (T ingredientRecette : recette.getIngredients()) {
            boolean found = false;
            for (T[] ingredient : ingredientUser) {
                if (ingredient[0].equals(ingredientRecette)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                ingredientsManquants.add(ingredientRecette);
            }
        }
        return ingredientsManquants;
    }

    // Calcule le pourcentage d'ingrédients disponibles
    private double calculatePourcentageDisponible(int nbIngredientsDisponibles, int totalIngredients) {
        return (double) nbIngredientsDisponibles / totalIngredients;
    }

    // Affiche les informations sur la recette
    private void afficherRecette(Recette<T> recette, int nbIngredientsDisponibles, List<T> ingredientsManquants, double pourcentageDispo) {
        if (nbIngredientsDisponibles == recette.getIngredients().size()) {
            System.out.println("\n\u001B[32mVous avez tous les ingrédients pour la recette : " + recette.getName() + "\u001B[0m");
        } else if (pourcentageDispo >= 0.70) {
            System.out.println("\n\u001B[33mRecette trouvée : " + recette.getName() + "\u001B[0m");
            System.out.println("Il vous manque " + ingredientsManquants.size() + " ingrédient(s) :");
            for (T ingredientManquant : ingredientsManquants) {
                System.out.println(" - " + ingredientManquant.toString());
            }
        } else {
            System.out.println("\n\u001B[31mRecette : " + recette.getName() + " - Vous n'avez pas suffisamment d'ingrédients\u001B[0m");
        }
    }  
}
