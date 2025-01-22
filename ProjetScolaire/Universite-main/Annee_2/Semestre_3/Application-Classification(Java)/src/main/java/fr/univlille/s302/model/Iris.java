package fr.univlille.s302.model;

import java.util.List;
import java.util.Objects;

import com.opencsv.bean.CsvBindByName;

/**
 * Classe {@code Iris} représentant une instance de données pour le jeu de données Iris.
 *
 * Cette classe implémente l'interface {@link Data} et permet de manipuler les attributs
 * des fleurs Iris, notamment la longueur et la largeur des sépales et des pétales, ainsi
 * que la variété de la fleur.
 *
 * @author Thibault Croisier & Benjamin Sere & Louis Bedu
 * @version 2.0
 */
public class Iris implements Data {

    @CsvBindByName(column = "sepal.length")
    private Double sepalLength;
    @CsvBindByName(column = "sepal.width")
    private Double sepalWidth;
    @CsvBindByName(column = "petal.length")
    private Double petalLength;
    @CsvBindByName(column = "petal.width")
    private Double petalWidth;
    @CsvBindByName(column = "variety")
    private String variety;


    public Iris() {
    }

    public Iris(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth, String variety) {
        this.variety = variety;
        this.sepalLength = sepalLength;
        this.petalLength = petalLength;
        this.sepalWidth = sepalWidth;
        this.petalWidth = petalWidth;

    }

    public Iris(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth) {
        this(sepalLength, sepalWidth, petalLength, petalWidth, "Unknown");
    }

    @Override
    public void setNewUnknownData(List<String> attributes) {
        this.variety = "Unknown";
        this.sepalLength = Double.parseDouble(attributes.get(0));
        this.sepalWidth = Double.parseDouble(attributes.get(1));
        this.petalLength = Double.parseDouble(attributes.get(2));
        this.petalWidth = Double.parseDouble(attributes.get(3));
    }


    @Override
    public String toString() {
        return "Iris{" +
                "variety='" + variety + '\'' +
                ", sepalLength=" + sepalLength +
                ", sepalWidth=" + sepalWidth +
                ", petalLength=" + petalLength +
                ", petalWidth=" + petalWidth +
                '}' + "\n";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iris iris)) return false;
        return Double.compare(sepalLength, iris.sepalLength) == 0
                && Double.compare(sepalWidth, iris.sepalWidth) == 0
                && Double.compare(petalLength, iris.petalLength) == 0
                && Double.compare(petalWidth, iris.petalWidth) == 0 
                && Objects.equals(variety, iris.variety);
    }

    @Override
    public String getType() {
        return variety;
    }

    @Override
    public void setType(String variety) {
        this.variety = variety;
    }

    @Override
    public double getAttributeByName(String name) {
        if (name.equals("Sepal length")) return this.sepalLength;
        if (name.equals("Sepal width")) return this.sepalWidth;
        if (name.equals("Petal length")) return this.petalLength;
        if (name.equals("Petal width")) return this.petalWidth;
        return -1;
    }

    @Override
    /**
    * Obtient les noms des attributs de l'objet Iris, les attribut doivent etre dans le meme ordre
     * dans les deux méthode getAttributes() & attibutesToArray().
    *
    * @return un tableau de chaînes contenant les noms des attributs :
    *         "Sepal length", "Sepal width", "Petal length", "Petal width".
    */
    public String[] getAttributes() {
        return new String[]{"Sepal length", "Sepal width", "Petal length", "Petal width"};
    }

    @Override
    /**
    * Convertit les attributs de l'objet Iris en un tableau de doubles.
    *
    * @return un tableau de doubles contenant les valeurs des attributs :
    *         [sepalLength, sepalWidth, petalLength, petalWidth].
    */
    public double[] attibutesToArray() {
        return new double[]{this.sepalLength, this.sepalWidth, this.petalLength, this.petalWidth};
    }

}
