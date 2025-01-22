

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utilisateur {
    private List<Recette> favorite;
    private List<Ingredient> ingredients;
    protected List<Recette> recetteProposition;

    
    private Utilisateur(List<Recette> favoris, List<Ingredient> ingredients) {
        this.favorite = favoris;
        this.ingredients = ingredients;
    }

    public Utilisateur(){
        this(new ArrayList<Recette>(),new ArrayList<Ingredient>());
    }

    public List<Recette> getFavoris() {
        return this.favorite;
    }

    public void setFavoris(List<Recette> favoris) {
        this.favorite= favoris;
    }

    public void addFavoris(Recette recette){
        this.favorite.add(recette);
    }

    public List<Ingredient> getIngredientsList() {
        return this.ingredients;
    }

    public Ingredient getIngredient(int nb){
        return this.ingredients.get(nb);
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(List<String[]> finaList) {
        for(String[] f : finaList) {
            if(f[3].equals("liquide"))this.ingredients.add(new LiquidIngredient(f[0], Integer.parseInt(f[1]) , f[2] , Double.parseDouble(f[3])));
            else{this.ingredients.add(new SolidIngredient(f[0], Integer.parseInt(f[1]) , f[2] , Double.parseDouble(f[3])));}
        }
    }
    public void addPropositionRecette(Recette r){
        List<String> ingrédients = new ArrayList<String>();
        String ingrédient;
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez le nom de votre recette:");
        String nomRecette = sc.nextLine();
        sc.nextLine();
        do{
            System.out.println("Entrez les Ingredients nécessaire à votre nouvelle recette séparé par sinon entrez fin:"); 
            ingrédient = sc.nextLine();
            if (!ingrédient.equals("fin")) ingrédients.add(ingrédient);
            sc.nextLine();
        }while (!ingrédient.equals("fin"));
        
        System.out.println("Veuillez décrire le procédé de la recette");
        String descriptionRecette = sc.nextLine();
        Recette recette = new Recette(nomRecette, null, descriptionRecette);
        sc.close();
        this.recetteProposition.add(recette);
    }
}
