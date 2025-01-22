
import java.io.IOException;
import java.util.ArrayList;

public class UseAdmin {
    
    public static void main(String[] args) throws IOException {
        Admin admin = new Admin();

        Ingredient g1 = new LiquidIngredient("eau",0);
        Ingredient g2 = new SolidIngredient("poulet", 20);
        Ingredient g3 = new LiquidIngredient("jus d'orange",30);
        Ingredient g4 = new SolidIngredient("purée",0);
    
        ArrayList<Ingredient> list= new ArrayList<Ingredient>();
        list.add(g1);
        list.add(g2);
        list.add(g3);
        list.add(g4); 
        Recette r = new Recette("Poulet à l'eau", list, "Poulet assaisonné à l'eau");
        

        admin.addRecette(r);
    }
}
