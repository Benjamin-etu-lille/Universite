package fr.univlille.s302.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de tests unitaires pour la classe DataSet.
 *
 * Ces tests vérifient le bon fonctionnement des principales méthodes de la classe DataSet, 
 * y compris le chargement des données, la classification des données inconnues, 
 * le calcul de la robustesse, et la gestion des types de données. 
 * Chaque méthode de test s'assure que les résultats correspondent aux attentes 
 * et que les exceptions sont correctement gérées. 
 *
 * @author BEDU Louis
 * @version 2.0
 * @date 26 Novembre 2024
 */
public class TestDataSet {
    private DataSet dataSet;
    private List<String> attributes;

    @BeforeEach
    void setUp() throws IOException {
        this.dataSet = new DataSet("res/test.csv", Iris.class);
        this.attributes = Arrays.asList(new String[]{"Sepal length", "Sepal width", "Petal length", "Petal width"});
    }

    /**
     * Teste la méthode getTypes() pour s'assurer qu'elle renvoie
     * une collection de types de données non nulle et non vide.
     */
    @Test
    void testGetTypes() {
        Collection<String> types = dataSet.getTypes();
        assertNotNull(types);
        assertFalse(types.isEmpty());
    }

    /**
     * Teste la méthode getUnknownData() pour vérifier qu'elle
     * renvoie uniquement des données de type "Unknown".
     */
    @Test
    void testGetUnknownData() {
        List<Data> unknownData = dataSet.getUnknownData();
        assertNotNull(unknownData);
        for (Data d : unknownData) {
            assertEquals("Unknown", d.getType());
        }
    }

    /**
     * Teste la méthode robustnessAlgo() pour vérifier que le calcul de la
     * robustesse renvoie une valeur comprise entre 0 et 1.
     */
    @Test
    void testRobustnessAlgo() {
        // Cas avec un k valid (par exemple k = 3)
        dataSet.robustnessAlgo(3, attributes);
        double robustness = dataSet.getRobustness();
        assertTrue(robustness >= 0 && robustness <= 1,
                "La robustesse doit être un nombre compris entre 0 et 1");
    }
    
    /**
     * Teste la méthode loadNewData() pour s'assurer que le chargement
     * de nouvelles données fonctionne correctement et que les types
     * de données sont disponibles après le chargement.
     */
    @Test
    void testLoadNewData() throws IOException {
        dataSet.loadNewData("res/test2.csv");
        assertNotNull(dataSet.getTypes());
    }
}
