import java.io.FileWriter;
import java.io.IOException;

public class Admin extends Utilisateur{
    
    private final static String MDP = "cookup4life";

    public Admin(){
        super();
    }

    public void seeRecette(){
        if(this.recetteProposition.size()==0)System.out.println("Aucune recette n'est en attente d'ajout");
        else{
            if(this.recetteProposition.size()==1)System.out.println("Voici la recette en attente d'ajout");
            else{
                System.out.println("Voici les recettes en attente d'ajout");
            }
            for(int i=0; i< this.recetteProposition.size();i++){
                System.out.println(i+1 + "." + this.recetteProposition.get(i).getName());
            }
        }
    }

    public void addRecette(Recette recette) throws IOException{
        FileWriter out = new FileWriter("res/recette.csv",true);
        out.write(recette.getName() + ";");
        for(int i=0; i< recette.getIngredients().size();i++){
            out.write(((Recette) recette.getIngredients().get(i)).getName() + ";");
        }
        out.write(recette.getDescription());
        out.close();    
    }

    public static boolean correctMdp(String mdp){
        if(mdp.equals(Admin.MDP))return true;
        return false;
    }
}
