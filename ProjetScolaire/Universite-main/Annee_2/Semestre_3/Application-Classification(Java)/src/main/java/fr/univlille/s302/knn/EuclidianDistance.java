package fr.univlille.s302.knn;

import java.util.List;

import fr.univlille.s302.model.Data;

/**
 * Classe {@code EuclidianDistance} qui implémente l'interface {@link Distance}.
 *
 * Cette classe fournit une méthode pour calculer la distance euclidienne
 * entre deux objets de type {@link Data}.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class EuclidianDistance implements Distance {

    private List<String> attributes;

    public EuclidianDistance(List<String> attributes){
        this.attributes = attributes;
    }

    /**
     * Calcule la distance euclidienne entre deux objets de type {@link Data}.
     *
     * @param d1 le premier objet de données
     * @param d2 le deuxième objet de données
     * @return la distance euclidienne entre {@code d1} et {@code d2}
     * @throws IllegalArgumentException si les tableaux des attributs de {@code d1} et {@code d2}
     *         n'ont pas la même taille
     */
    @Override
    public double distance(Data d1, Data d2) {
        // Vérification si les tailles des tableaux sont égales
        if (d1.attibutesToArray().length != d2.attibutesToArray().length) {
            throw new IllegalArgumentException("Le tableaux de double doit être de la même taille.");
        }

        // Calcul de la distance euclidienne
        double res = 0;
        for (String attribut : attributes) {
            res += Math.pow(d1.getAttributeByName(attribut) - d2.getAttributeByName(attribut), 2);
        }
        return Math.sqrt(res);
    }
}
