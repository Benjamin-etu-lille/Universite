package fr.univlille.s302.model;

import java.util.List;

/**
 * Interface {@code Data} représentant un objet de données utilisé dans le cadre
 * de l'algorithme de classification.
 *
 * Cette interface définit les méthodes nécessaires pour manipuler les attributs
 * des objets de données, y compris la possibilité de définir des données inconnues,
 * d'obtenir des attributs par nom, et de récupérer les attributs sous différentes formes.
 *
 * @author Thibault Croisier
 * @version 2.0
 */
public interface Data {

    /**
     * Définit de nouvelles données inconnues à partir d'une liste d'attributs.
     *
     * @param attributes la liste d'attributs à définir pour les données inconnues
     */
    void setNewUnknownData(List<String> attributes);

    /**
     * Définit le type de l'objet de données.
     *
     * @param type le type à définir
     */
    void setType(String type);

    /**
     * Obtient le type de l'objet de données.
     *
     * @return le type de l'objet de données
     */
    String getType();

    /**
     * Récupère la valeur d'un attribut par son nom.
     *
     * @param name le nom de l'attribut
     * @return la valeur de l'attribut correspondant au nom fourni
     * @throws IllegalArgumentException si l'attribut n'existe pas
     */
    double getAttributeByName(String name);

    /**
     * Récupère tous les attributs sous forme de tableau de chaînes.
     *
     * @return un tableau contenant tous les attributs de l'objet de données
     */
    String[] getAttributes();

    /**
     * Convertit les attributs en un tableau de doubles.
     *
     * @return un tableau de doubles représentant les attributs
     */
    double[] attibutesToArray();
}