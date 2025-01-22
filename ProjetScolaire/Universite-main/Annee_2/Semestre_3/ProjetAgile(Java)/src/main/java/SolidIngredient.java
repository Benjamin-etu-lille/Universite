

public class SolidIngredient implements Ingredient{
    
    private String name;
    private int calories;
    private String type;
    private Double quantite;

    public SolidIngredient(String name, int calories, String type, Double quantite) {
        this.name = name;
        this.calories = calories;
        this.type = type;
        this.quantite = quantite;
        
    }

    public SolidIngredient(String name, int calories){
        this(name, calories, null, null);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantite() {
        return quantite;
    }

    public String getType() {
        return type;
    }

    public int getCalories() {
        return this.calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getEtat(){
        return false;
    }
}
