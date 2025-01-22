package fr.univlille.s302.knn;

import java.util.List;

import fr.univlille.s302.model.Data;

/**
 * Classe {@code ManhattanDistance} qui implémente l'interface {@link Distance}.
 *
 * Cette classe fournit une méthode pour calculer la distance de Manhattan
 * entre deux objets de type {@link Data}.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class ManhattanDistance implements Distance {
    private List<String> attributes;

    public ManhattanDistance(List<String> attributes){
        this.attributes = attributes;
    }
    /**
     * Calcule la distance de Manhattan entre deux objets de type {@link Data}.
     *
     * @param d1 le premier objet de données
     * @param d2 le deuxième objet de données
     * @return la distance de Manhattan entre {@code d1} et {@code d2}
     * @throws IllegalArgumentException si les tableaux des attributs de {@code d1} et {@code d2}
     *         n'ont pas la même taille
     */
    @Override
    public double distance(Data d1, Data d2) {
        // Vérification si les tailles des tableaux sont égales
        if (d1.attibutesToArray().length != d2.attibutesToArray().length) {
            throw new IllegalArgumentException("Les tableaux de double doivent être de la même taille.");
        }

        double res = 0;
        for (String attribut : attributes) {
            res += Math.abs(d1.getAttributeByName(attribut) - d2.getAttributeByName(attribut));
        }
        return res;
    }
}
