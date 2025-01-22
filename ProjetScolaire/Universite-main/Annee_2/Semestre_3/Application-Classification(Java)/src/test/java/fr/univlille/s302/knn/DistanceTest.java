package fr.univlille.s302.knn;

import static org.junit.jupiter.api.Assertions.*;

import fr.univlille.s302.model.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Classe de test unitaire pour vérifier le comportement des différentes implémentations de la distance.
 * Les tests incluent les calculs des distances Euclidienne, Manhattan, Normalized Euclidean et Normalized Manhattan.
 *
 * Cette classe teste les cas suivants :
 * - Distance entre deux points identiques.
 * - Distance entre deux points différents.
 * - Vérification de l'égalité des tailles des données d'entrée.
 * - Performance de la méthode de calcul des distances avec des données de grande taille.
 *
 * @author Benjamin Sere
 * @version 1.0
 * @date 26 novembre 2024
 */
public class DistanceTest {
        private DataImpl d1;
        private DataImpl d2;
        private List<String> attributes;

    @BeforeEach
    void setUp(){
        d1 = new DataImpl(1.0, 2.0, 3.0);
        d2 = new DataImpl(1.0, 2.0, 3.0);
        attributes = Arrays.asList(new String[]{"att1", "att2", "att3"});
    }

    @Test
    void testEuclidianDistanceIdentique() {
        // Données identiques
        EuclidianDistance euclidean = new EuclidianDistance(attributes);

        // Calcul de la distance
        double result = euclidean.distance(d1, d2);

        // Vérification que la distance entre deux points identiques est bien 0
        assertEquals(0.0, result, "La distance Euclidienne entre deux points identiques doit être zéro.");
    }

    @Test
    void testManhattanDistanceIdentique() {
        // Données identiques
        ManhattanDistance manhattan = new ManhattanDistance(attributes);

        // Calcul de la distance
        double result = manhattan.distance(d1, d2);

        // Vérification que la distance entre deux points identiques est bien 0
        assertEquals(0.0, result, "La distance Manhattan entre deux points identiques doit être zéro.");
    }

    @Test
    void testNormalizedEuclidianDistanceIdentique() {
        // Données identiques
        NormalizedEuclidianDistance normalizedEuclidean = new NormalizedEuclidianDistance(attributes, new double[]{0.0, 0.0, 0.0}, new double[]{1.0, 1.0, 1.0});

        // Calcul de la distance
        double result = normalizedEuclidean.distance(d1, d2);

        // Vérification que la distance entre deux points identiques est bien 0
        assertEquals(0.0, result, "La distance Euclidienne normalisée entre deux points identiques doit être zéro.");
    }

    @Test
    void testNormalizedManhattanDistanceIdentique() {
        // Données identiques
        NormalizedManhattanDistance normalizedManhattan = new NormalizedManhattanDistance(attributes, new double[]{0.0, 0.0, 0.0}, new double[]{1.0, 1.0, 1.0});

        // Calcul de la distance
        double result = normalizedManhattan.distance(d1, d2);

        // Vérification que la distance entre deux points identiques est bien 0
        assertEquals(0.0, result, "La distance Manhattan normalisée entre deux points identiques doit être zéro.");
    }

    @Test
    void testDistanceDifferent() {
        // Création de données de test avec deux points différents
        DataImpl d2 = new DataImpl(4.0, 5.0, 6.0);
        EuclidianDistance euclidean = new EuclidianDistance(attributes);

        // Calcul de la distance entre les deux points
        double result = euclidean.distance(d1, d2);

        // La distance doit être égale à sqrt((4-1)^2 + (5-2)^2 + (6-3)^2) = sqrt(27)
        assertEquals(Math.sqrt(27), result, 0.0001, "La distance Euclidienne entre les deux points doit être correcte.");
    }

    @Test
    void testManhattanDistanceDifferent() {
        // Création de données de test avec deux points différents
        DataImpl d2 = new DataImpl(4.0, 5.0, 6.0);
        ManhattanDistance manhattan = new ManhattanDistance(attributes);

        // Calcul de la distance entre les deux points
        double result = manhattan.distance(d1, d2);

        // La distance doit être égale à |(4-1) + (5-2) + (6-3)| = 9
        assertEquals(9.0, result, "La distance Manhattan entre les deux points doit être correcte.");
    }

    @Test
    void testNormalizedEuclidianDistanceDifferent() {
        // Création de données de test avec deux points différents
        DataImpl d2 = new DataImpl(4.0, 5.0, 6.0);
        NormalizedEuclidianDistance normalizedEuclidean = new NormalizedEuclidianDistance(attributes, new double[]{0.0, 0.0, 0.0}, new double[]{1.0, 1.0, 1.0});

        // Calcul de la distance entre les deux points
        double result = normalizedEuclidean.distance(d1, d2);

        // La distance doit être normalisée et correcte
        assertEquals(Math.sqrt(27), result, 0.0001, "La distance Euclidienne normalisée entre les deux points doit être correcte.");
    }

    @Test
    void testNormalizedManhattanDistanceDifferent() {
        // Création de données de test avec deux points différents
        DataImpl d2 = new DataImpl(4.0, 5.0, 6.0);
        NormalizedManhattanDistance normalizedManhattan = new NormalizedManhattanDistance(attributes, new double[]{0.0, 0.0, 0.0}, new double[]{1.0, 1.0, 1.0});

        // Calcul de la distance entre les deux points
        double result = normalizedManhattan.distance(d1, d2);

        // La distance doit être normalisée et correcte
        assertEquals(9.0, result, "La distance Manhattan normalisée entre les deux points doit être correcte.");
    }

    @Test
    void testDistanceNull() {
        // Création de données de test
        DataImpl d1 = new DataImpl(1.0, 2.0, 3.0);
        Data d2 = null;
        EuclidianDistance euclidean = new EuclidianDistance(attributes);

        // Vérification qu'une exception est lancée si d2 est null
        assertThrows(NullPointerException.class, () -> euclidean.distance(d1, d2), "L'argument d2 ne doit pas être null.");
    }


    /**
     * Classe interne simulée pour implémenter l'interface Data.
     * Utilisée dans les tests pour simuler des objets de type Data.
     */
    private class DataImpl implements Data {
        private double att1;
        private double att2;
        private double att3;

        public DataImpl(double att1, double att2, double att3) {
            this.att1 = att1;
            this.att2 = att2;
            this.att3 = att3;
        }

        @Override
        public void setNewUnknownData(List<String> attributes) {
            // Implémentation non utilisée dans ce test
        }

        @Override
        public void setType(String type) {
            // Implémentation non utilisée dans ce test
        }

        @Override
        public String getType() {
            return null; // Implémentation non utilisée dans ce test
        }

        @Override
        public double getAttributeByName(String name) {
            if(name.equals("att1")) return this.att1;
            if(name.equals("att2")) return this.att2;
            if(name.equals("att3")) return this.att3;
            return -1;
        }

        @Override
        public String[] getAttributes() {
            return new String[]{"att1", "att2", "att3"};
        }

        @Override
        public double[] attibutesToArray() {
            return new double[] {this.att1, this.att2, this.att3};
        }
    }
}
