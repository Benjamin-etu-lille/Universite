package fr.univlille.s302.knn;

import java.util.List;

import fr.univlille.s302.model.Data;

/**
 * Classe {@code NormalizedEuclidianDistance} qui implémente l'interface {@link Distance}.
 *
 * Cette classe fournit une méthode pour calculer la distance euclidienne normalisée
 * entre deux objets de type {@link Data}. La normalisation est effectuée en utilisant
 * les valeurs minimales et maximales des attributs pour chaque dimension.
 *
 * @author BEDU Louis
 * @version 1.0
 */
public class NormalizedEuclidianDistance implements Distance {

    private List<String> attributes;
    private double[] minAttribute;
    private double[] maxAttribute;

    /**
     * Constructeur pour initialiser la distance euclidienne normalisée avec les valeurs minimales
     * et maximales des attributs.
     *
     * @param minAttributes les valeurs minimales des attributs
     * @param maxAttributes les valeurs maximales des attributs
     * @throws IllegalArgumentException si les tableaux de valeurs minimales et maximales
     *         ne sont pas de la même taille
     */
    public NormalizedEuclidianDistance(List<String> attributes, double[] minAttributes, double[] maxAttributes) {
        if (minAttributes.length != maxAttributes.length) {
            throw new IllegalArgumentException("Les tableaux de valeurs minimales et maximales doivent être de la même taille.");
        }
        this.attributes = attributes;
        this.minAttribute = minAttributes;
        this.maxAttribute = maxAttributes;
    }

    /**
     * Calcule la distance euclidienne normalisée entre deux objets de type {@link Data}.
     *
     * @param d1 le premier objet de données
     * @param d2 le deuxième objet de données
     * @return la distance euclidienne normalisée entre {@code d1} et {@code d2}
     * @throws IllegalArgumentException si les tableaux des attributs de {@code d1} et {@code d2}
     *         n'ont pas la même taille
     */
    @Override
    public double distance(Data d1, Data d2) {
        double res = 0;
        int i = 0;
        for (String attribut : attributes) {
            res += Math.pow((d1.getAttributeByName(attribut) - d2.getAttributeByName(attribut)) /
                    (this.maxAttribute[i] - this.minAttribute[i]), 2);
            i++;
        }
        return Math.sqrt(res);
    }
}
