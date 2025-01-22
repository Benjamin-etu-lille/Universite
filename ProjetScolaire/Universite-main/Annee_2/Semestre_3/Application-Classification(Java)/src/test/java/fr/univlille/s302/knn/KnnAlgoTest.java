package fr.univlille.s302.knn;

import static org.junit.jupiter.api.Assertions.*;

import fr.univlille.s302.model.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Classe de test unitaire pour la classe KnnAlgo.
 *
 * Cette classe teste l'algorithme des K plus proches voisins (KNN), en vérifiant si la méthode getKnn
 * retourne correctement les k plus proches voisins selon les distances calculées.
 *
 * @author Benjamin Sere
 * @version 1.0
 * @date 26 novembre 2024
 */
public class KnnAlgoTest {
        private List<String> attributes;
    /**
     * Teste la méthode getKnn avec un ensemble de données.
     * Cette méthode vérifie si les k plus proches voisins sont correctement renvoyés.
     */

    @BeforeEach
    void setUp(){
        attributes = Arrays.asList(new String[]{"att1", "att2", "att3"});
    }

    @Test
    void testGetKnn() {
        // Création d'une instance de KnnAlgo avec k = 2
        KnnAlgo knn = new KnnAlgo(2);

        // Création de l'objet "inconnu"
        DataImpl unknown = new DataImpl(1.0, 2.0, 3.0);

        // Création d'un ensemble de données
        List<Data> dataSet = Arrays.asList(
                new DataImpl(1.0, 2.0, 3.0), // Identique à "unknown"
                new DataImpl(4.0, 5.0, 6.0),
                new DataImpl(7.0, 8.0, 9.0),
                new DataImpl(2.0, 3.0, 4.0)
        );

        // Calcul des KNN (les 2 plus proches voisins)
        List<Data> result = knn.getKnn(unknown, dataSet, new EuclidianDistance(attributes));

        // Vérification que le nombre de voisins retourné est bien k = 2
        assertEquals(2, result.size(), "Le nombre de voisins doit être égal à k.");

        // Vérification que les voisins retournés sont corrects (selon la distance)
        // Les deux plus proches voisins doivent être le premier (identique à "unknown") et le quatrième (plus proche).
        assertTrue(result.contains(dataSet.get(0)), "Le premier voisin doit être 'unknown'.");
        assertTrue(result.contains(dataSet.get(3)), "Le deuxième voisin doit être le point le plus proche après 'unknown'.");
    }

    /**
     * Teste la méthode getKnn avec des données comportant des objets de type "Unknown".
     * Cette méthode vérifie que seuls les objets dont le type n'est pas "Unknown" sont pris en compte.
     */
    @Test
    void testGetKnnWithUnknownType() {
        // Création d'une instance de KnnAlgo avec k = 3
        KnnAlgo knn = new KnnAlgo(3);

        // Création d'un objet "inconnu"
        DataImpl unknown = new DataImpl(1.0, 2.0, 3.0);

        // Création d'un ensemble de données avec un objet "Unknown"
        List<Data> dataSet = Arrays.asList(
                new DataImpl(1.0, 2.0, 3.0), // Identique à "unknown"
                new DataImpl(4.0, 5.0, 6.0),
                new DataImpl(7.0, 8.0, 9.0),
                new DataImpl(2.0, 3.0, 4.0),
                new DataImpl(8.0, 9.0, 10.0),
                new DataImpl(1.5, 2.5, 3.5) // "Unknown" type, pas pris en compte
        );

        // L'élément avec le type "Unknown" ne doit pas être inclus dans les voisins
        List<Data> result = knn.getKnn(unknown, dataSet, new EuclidianDistance(attributes));

        // Vérification que le nombre de voisins retourné est bien k = 3
        assertEquals(3, result.size(), "Le nombre de voisins retourné doit être égal à k.");
    }

    /**
     * Teste le comportement de la méthode getKnn avec des valeurs de k égales à 1.
     * Cette méthode vérifie que le seul voisin retourné est celui qui est le plus proche de l'objet inconnu.
     */
    @Test
    void testGetKnnKIsOne() {
        // Création d'une instance de KnnAlgo avec k = 1
        KnnAlgo knn = new KnnAlgo(1);

        // Création de l'objet "inconnu"
        DataImpl unknown = new DataImpl(1.0, 2.0, 3.0);

        // Création d'un ensemble de données
        List<Data> dataSet = Arrays.asList(
                new DataImpl(1.0, 2.0, 3.0), // Identique à "unknown"
                new DataImpl(4.0, 5.0, 6.0),
                new DataImpl(7.0, 8.0, 9.0)
        );

        // Calcul du KNN (1 plus proche voisin)
        List<Data> result = knn.getKnn(unknown, dataSet, new EuclidianDistance(attributes));

        // Vérification que seul un voisin est retourné
        assertEquals(1, result.size(), "Le nombre de voisins retourné doit être égal à 1.");

        // Vérification que le voisin retourné est le premier (identique à "unknown")
        assertTrue(result.contains(dataSet.get(0)), "Le seul voisin retourné doit être celui identique à 'unknown'.");
    }

    /**
     * Classe interne simulée pour implémenter l'interface Data.
     * Utilisée dans les tests pour simuler des objets de type Data.
     */
    private class DataImpl implements Data {
        private double att1;
        private double att2;
        private double att3;
        private String type = "Known"; // Par défaut, type "Known"

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
            this.type = type;
        }

        @Override
        public String getType() {
            return this.type;
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
