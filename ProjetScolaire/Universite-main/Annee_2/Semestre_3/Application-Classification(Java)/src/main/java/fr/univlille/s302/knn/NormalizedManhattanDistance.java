package fr.univlille.s302.knn;

import java.util.List;

import fr.univlille.s302.model.Data;

/**
 * Classe {@code NormalizedManhattanDistance} qui implémente l'interface {@link Distance}.
 *
 * Cette classe fournit une méthode pour calculer la distance de Manhattan normalisée
 * entre deux objets de type {@link Data}. La normalisation est effectuée en utilisant
 * les valeurs minimales et maximales des attributs pour chaque dimension.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class NormalizedManhattanDistance implements Distance {

    private List<String> attributes;
    private double[] minAttribute;
    private double[] maxAttribute;

    /**
     * Constructeur pour initialiser la distance de Manhattan normalisée avec les valeurs minimales
     * et maximales des attributs.
     *
     * @param minAttribute les valeurs minimales des attributs
     * @param maxAttribute les valeurs maximales des attributs
     * @throws IllegalArgumentException si les tableaux de valeurs minimales et maximales
     *         ne sont pas de la même taille
     */
    public NormalizedManhattanDistance(List<String> attributes, double[] minAttribute, double[] maxAttribute) {
        if (minAttribute.length != maxAttribute.length) {
            throw new IllegalArgumentException("Les tableaux de valeurs minimales et maximales doivent être de la même taille.");
        }
        this.attributes = attributes;
        this.minAttribute = minAttribute;
        this.maxAttribute = maxAttribute;
    }

    /**
     * Calcule la distance de Manhattan normalisée entre deux objets de type {@link Data}.
     *
     * @param d1 le premier objet de données
     * @param d2 le deuxième objet de données
     * @return la distance de Manhattan normalisée entre {@code d1} et {@code d2}
     * @throws IllegalArgumentException si les tableaux des attributs de {@code d1} et {@code d2}
     *         n'ont pas la même taille
     */
    @Override
    public double distance(Data d1, Data d2) {
        if (d1.attibutesToArray().length != d2.attibutesToArray().length) {
            throw new IllegalArgumentException("Les tableaux d'attributs doivent être de la même taille.");
        }

        double res = 0;
        int i = 0;
        for (String attribut : attributes) {
            res += Math.abs((d1.getAttributeByName(attribut) - d2.getAttributeByName(attribut)) /
                    (this.maxAttribute[i] - this.minAttribute[i]));
            i++;
        }
        return res;
    }
}
