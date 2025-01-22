

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
public class AdminTest {

    Admin admin = new Admin();
    Recette r;
    Ingredient g1 ;
    Ingredient g2 ;
    Ingredient g3 ;
    Ingredient g4 ;
    ArrayList<Ingredient> list ;

@BeforeEach
    public void initialisation() {

    this.g1 = new LiquidIngredient("eau",0);
    this.g2 = new SolidIngredient("poulet", 20);
    this.g3 = new LiquidIngredient("jus d'orange",30);
    this.g4 = new SolidIngredient("purée",0);

    this.list= new ArrayList<Ingredient>();
    list.add(g1);
    list.add(g2);
    list.add(g3);
    list.add(g4); 
    this.r = new Recette("Poulet à l'eau", list, "Poulet assaisonné à l'eau");
    }

    @Test
    public void correctMdpTest(){
        assertFalse(this.admin.correctMdp("ouioui"));
        assertTrue(this.admin.correctMdp("cookup4life"));
        assertFalse(this.admin.correctMdp(""));
        assertFalse(this.admin.correctMdp("opgvvv,"));

    }
}
