package fr.univlille.s302.knn;

import fr.univlille.s302.model.Data;

/**
 * Interface représentant une méthode de calcul de distance entre deux objets de type {@link Data}.
 *
 * Différentes implémentations peuvent définir des mesures de distance variées (par exemple,
 * distance euclidienne, distance de Manhattan, etc.).
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public interface Distance {

    /**
     * Calcule la distance entre deux objets de type {@link Data}.
     *
     * @param d1 le premier objet de données
     * @param d2 le deuxième objet de données
     * @return la distance entre {@code d1} et {@code d2}
     */
    double distance(Data d1, Data d2);
}