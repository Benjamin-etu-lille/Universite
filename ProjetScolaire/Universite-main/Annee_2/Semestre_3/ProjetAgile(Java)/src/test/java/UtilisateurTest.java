
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtilisateurTest {
    Utilisateur user;
    Ingredient g1 ;
    Ingredient g2 ;
    Ingredient g3 ;
    Ingredient g4; 

    @BeforeEach
    public void initialisation() {
    this.g1 = new LiquidIngredient("eau",0);
    this.g2 = new SolidIngredient("poulet", 20);
    this.g3 = new LiquidIngredient("jus d'orange",30);
    this.g4 = new SolidIngredient("purée",0);
    this.user = new Utilisateur();
    }

   @Test
    public void addTest(){
        user.addIngredient(this.g1.getName(), false);
        user.addIngredient(this.g2.getName(), true);
        user.addIngredient(this.g3.getName(), true);
        user.addIngredient(this.g4.getName(), true);
        assertEquals(4, user.getIngredientsList().size());
    }

    @Test
    public void getTest(){
        user.addIngredient(this.g2.getName(), true);
        assertEquals(g2.getName(), user.getIngredient(0).getName());
    }

}
