package fr.univlille.s302.knn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.univlille.s302.model.Data;

/**
 * Classe {@code KnnAlgo} qui implémente l'algorithme des k plus proches voisins (k-NN).
 *
 * Cette classe permet de trouver les k voisins les plus proches d'un point de données inconnu
 * dans un ensemble de données donné en utilisant une méthode de calcul de distance spécifiée.
 *
 * @author Thibault Croisier & Benjamin Sere
 * @version 1.0
 */
public class KnnAlgo {

    private int k;

    /**
     * Constructeur pour initialiser l'algorithme avec le nombre de voisins à considérer.
     *
     * @param k le nombre de voisins à considérer
     * @throws IllegalArgumentException si {@code k} est inférieur ou égal à zéro
     */
    public KnnAlgo(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("Le nombre de voisins k doit être supérieur à zéro.");
        }
        this.k = k;
    }

    /**
     * Trouve les k plus proches voisins d'un point de données inconnu.
     *
     * @param unknown le point de données inconnu
     * @param dataSet l'ensemble de données à partir duquel trouver les voisins
     * @param distance la méthode de calcul de distance à utiliser
     * @return une liste des k voisins les plus proches
     */
    public List<Data> getKnn(Data unknown, List<Data> dataSet, Distance distance) {
        return getKnnFromDistances(getDistances(unknown, dataSet, distance));
    }

    /**
     * Calcule les distances entre un point inconnu et tous les points de l'ensemble de données.
     *
     * @param unknown le point de données inconnu
     * @param dataSet l'ensemble de données
     * @param distance la méthode de calcul de distance
     * @return une carte associant chaque point de données à sa distance par rapport à {@code unknown}
     */
    private Map<Data, Double> getDistances(Data unknown, List<Data> dataSet, Distance distance) {
        Map<Data, Double> distances = new HashMap<>();
        for (Data d : dataSet) {
            if (!d.getType().equals("Unknown")) {
                distances.put(d, distance.distance(unknown, d));
            }
        }
        return distances;
    }

    /**
     * Récupère les k voisins les plus proches à partir des distances calculées.
     *
     * @param distances une carte des distances entre le point inconnu et les points de l'ensemble de données
     * @return une liste des k voisins les plus proches
     */
    private List<Data> getKnnFromDistances(Map<Data, Double> distances) {
        return distances.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue()) // Tri par valeur croissante
                .limit(this.k) // Limiter aux k premières
                .map(Map.Entry::getKey) // Récupérer uniquement les clés
                .collect(Collectors.toList());
    }
}
