package fr.univlille.s302.model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

/**
 * Classe utilitaire {@code DataLoadingUtil} pour le chargement de données à partir de fichiers CSV.
 *
 * Cette classe fournit des méthodes pour charger des données sous forme de liste d'objets
 * de type {@link Data} et pour créer des instances d'objets de données inconnues à partir
 * d'une liste d'attributs.
 *
 * @author Thibault Croisier & Benjamin Sere
 * @version 1.0
 */
public class DataLoadingUtil {

    /**
     * Charge des données à partir d'un fichier CSV et les retourne sous forme de liste.
     *
     * @param fileName le nom du fichier CSV à charger
     * @param clazz la classe des objets de données à créer
     * @return une liste d'objets de type {@link Data} chargés depuis le fichier CSV
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la lecture du fichier
     */
    public static List<Data> loadData(String fileName, Class<? extends Data> clazz) throws IOException {
        return new CsvToBeanBuilder<Data>(Files.newBufferedReader(Paths.get(fileName)))
                .withSeparator(',')
                .withType(clazz)
                .build().parse();
    }

    /**
     * Crée une instance d'objet de données inconnue à partir d'une liste d'attributs.
     *
     * @param attributes la liste des attributs à définir pour l'objet de données
     * @param clazz la classe de l'objet de données à créer
     * @return une instance d'objet de type {@link Data} avec les attributs définis
     */
    public static Data createUnknownData(List<String> attributes, Class<? extends Data> clazz)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        Data d = clazz.getDeclaredConstructor().newInstance();
        d.setNewUnknownData(attributes);
        return d;
    }
}