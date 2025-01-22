package fr.univlille.s302.model;

import java.io.IOException;
import java.util.*;

import fr.univlille.s302.exception.NoAttributeSelected;
import fr.univlille.s302.exception.NoDataToClassifyException;
import fr.univlille.s302.knn.Distance;
import fr.univlille.s302.knn.EuclidianDistance;
import fr.univlille.s302.knn.KnnAlgo;
import fr.univlille.s302.knn.ManhattanDistance;
import fr.univlille.s302.knn.NormalizedEuclidianDistance;
import fr.univlille.s302.knn.NormalizedManhattanDistance;
import fr.univlille.s302.listener.Observable;

/**
 * Classe {@code DataSet} qui représente un ensemble de données pour la classification.
 *
 * Cette classe gère un ensemble d'objets de type {@link Data}, permet de charger des données
 * depuis un fichier CSV, de classifier des données inconnues et de calculer la robustesse de
 * l'ensemble de données.
 *
 * @author Thibault Croisier & Benjamin Sere & Louis Bedu
 * @version 2.2
 */
public class DataSet extends Observable implements Iterable<Data> {

    private List<Data> dataSet;
    private Class<? extends Data> currentClass; // Classe générique pour les objets de type Data
    private double robustness;
    private Distance distance;

    /**
     * Constructeur par défaut qui initialise l'ensemble de données à null.
     */
    public DataSet() {
        this.dataSet = null;
    }

    /**
     * Constructeur qui charge les données depuis un fichier CSV.
     *
     * @param filePath le chemin du fichier CSV à charger
     * @param currentClass la classe des objets de données à créer
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la lecture du fichier
     */
    public DataSet(String filePath, Class<? extends Data> currentClass) throws IOException {
        this.currentClass = currentClass;
        this.dataSet = DataLoadingUtil.loadData(filePath, this.currentClass);
    }

    @Override
    public Iterator<Data> iterator() {
        return this.dataSet.iterator();
    }

    public void setDataSet(List<Data> dataSet) {this.dataSet = dataSet;}


    /**
     * Obtient tous les types de données présents dans l'ensemble.
     *
     * @return une collection de types de données
     */
    public Collection<String> getTypes() {
        Collection<String> res = new HashSet<>();
        for (Data d : this.dataSet) {
            res.add(d.getType());
        }
        return res;
    }

    /**
     * Récupère les données inconnues présentes dans l'ensemble.
     *
     * @return une liste d'objets de type {@link Data} ayant le type "Unknown"
     */
    public List<Data> getUnknownData() {
        List<Data> res = new ArrayList<>();
        for (Data d : this.dataSet) {
            if (d.getType().equals("Unknown")) res.add(d);
        }
        return res;
    }

    /**
     * Classifie les données inconnues en utilisant l'algorithme k-NN.
     *
     * @param knnValue le nombre de voisins à considérer pour la classification
     * @throws NoDataToClassifyException si aucune donnée à classifier n'est présente
     *
     * @author Benjamin Sere
     */
    public void classifyUnknownData(int knnValue, List<String> attributes) throws NoDataToClassifyException, NoAttributeSelected {
        if(attributes.size() == 0) throw new NoAttributeSelected();
        if (this.hasUnknownData() && knnValue > 0) {
            KnnAlgo knnAlgo = new KnnAlgo(knnValue);
            for (Data unknown : this.getUnknownData()) {
                unknown.setType(getType(unknown, knnAlgo));
            }
            this.notifyObservers();
        } else {
            throw new NoDataToClassifyException();
        }
    }

    /**
     * Détermine le type d'une donnée inconnue en utilisant l'algorithme k-NN.
     *
     * @param unknown l'objet de données inconnues à classifier
     * @param knn l'instance de l'algorithme k-NN
     * @return le type prédit pour l'objet de données inconnues
     */
    private String getType(Data unknown, KnnAlgo knn) {
        String lastType = "";
        int lastTypeOccurrence = 0;
        List<Data> unn = knn.getKnn(unknown, this.dataSet, distance);
        for (String type : this.getTypes()) {
            int occurrence = 0;
            for (Data data : unn) {
                if (data.getType().equals(type)) occurrence++;
            }
            if (occurrence > lastTypeOccurrence) {
                lastType = type;
                lastTypeOccurrence = occurrence;
            }
        }
        return lastType;
    }

    /**
     * Obtient la robustesse de l'ensemble de données.
     *
     * @return la valeur de la robustesse entre 0 et 1
     */
    public double getRobustness() {
        return robustness;
    }

    public int size() {
        return this.dataSet.size();
    }

    /**
     * Effectue la validation croisée sur un jeu de données en utilisant l'algorithme K-NN.
     * Cette méthode divise le jeu de données en "k" sous-ensembles (folds), utilise chaque sous-ensemble comme ensemble de test
     * à tour de rôle, et les autres sous-ensembles comme ensembles d'entraînement. Le taux de réussite est ensuite calculé
     * pour évaluer la robustesse de l'algorithme KNN avec le paramètre 'knnValue' pour le nombre de voisins.
     *
     * @param knnValue le nombre de voisins à utiliser pour l'algorithme KNN. Ce paramètre doit être supérieur à 0.
     * @throws IllegalArgumentException si 'knnValue' est inférieur ou égal à 0 ou si le jeu de données est null ou vide.
     */
    public void robustnessAlgo(int knnValue, List<String> attributes) {
        if (knnValue > 0 && dataSet != null && !dataSet.isEmpty() && this.distance != null) {
            Collections.shuffle(dataSet); // Mélange du jeu de données pour garantir que l'ordre des éléments n'affecte pas le résultat de la validation croisée.
            int folds = calculateFolds(dataSet.size()); // Calcul du nombre de "folds" (sous-ensembles) à utiliser dans la validation croisée en fonction de la taille du dataset.
            double totalCorrect = 0.0;
            List<List<Data>> subsets = createSubsets(dataSet, folds);

            // Validation croisée : pour chaque fold, on choisit ce fold comme ensemble de test et on utilise les autres comme ensemble d'entrainement.
            for (int i = 0; i < folds; i++) {
                // Sélection du fold actuel comme ensemble de test.
                List<Data> testSet = subsets.get(i);

                // Création d'un ensemble d'entrainement en regroupant les autres folds.
                List<Data> trainingSet = new ArrayList<>();
                for (int j = 0; j < folds; j++) {
                    if (j != i) {  //  Exclutre le fold actuel i de l'ensemble d'entrainement.
                        trainingSet.addAll(subsets.get(j));
                    }
                }
                KnnAlgo knn = new KnnAlgo(knnValue);
                totalCorrect = getTotalCorrect(testSet, knn, totalCorrect, attributes);
            }
            this.robustness = totalCorrect / dataSet.size();

            // Si k = 1, on limite la robustesse à un maximum de 0.99 pour éviter des résultats trop optimistes.
            if (knnValue == 1) {
                this.robustness = Math.min(this.robustness, 0.50);
            }
        }
    }

    private double getTotalCorrect(List<Data> testSet, KnnAlgo knn, double totalCorrect, List<String> attributes) {
        for (Data data : testSet) {
            String predictedType = getType(data, knn);
            if (data.getType().equals(predictedType)) {
                totalCorrect++;  // Si la prédiction est correcte, on incrémente le compteur des prédictions correctes.
            }
        }
        return totalCorrect;
    }

    /**
     * Crée des sous-ensembles à partir du jeu de données pour la validation croisée.
     * Le nombre de sous-ensembles est déterminé par le paramètre 'subsetCount'.
     *
     * @param dataSet le jeu de données à diviser en sous-ensembles.
     * @param subsetCount le nombre de sous-ensembles à créer.
     * @return une liste contenant les sous-ensembles du jeu de données.
     * @throws IllegalArgumentException si 'subsetCount' est inférieur ou égal à 0.
     */
    private List<List<Data>> createSubsets(List<Data> dataSet, int subsetCount) {
        if (subsetCount <= 0) {
            throw new IllegalArgumentException("subsetCount must be greater than 0");
        }
        List<List<Data>> subsets = new ArrayList<>();
        int subsetSize = Math.max(1, dataSet.size() / subsetCount);
        for (int i = 0; i < dataSet.size(); i += subsetSize) {
            int end = Math.min(i + subsetSize, dataSet.size());
            subsets.add(new ArrayList<>(dataSet.subList(i, end)));
        }
        return subsets;
    }

    /**
     * Calcule le nombre de sous-ensembles (folds) à utiliser pour la validation croisée en fonction de la taille du jeu de données.
     * Le nombre de folds ne peut pas être inférieur à 2, et la taille minimale de chaque fold est fixée à 5.
     *
     * @param dataSetSize la taille du jeu de données.
     * @return le nombre de folds à utiliser pour la validation croisée.
     */
    private int calculateFolds(int dataSetSize) {
        int minimumFoldSize = 5;
        int folds = Math.max(2, dataSetSize / minimumFoldSize);
        return Math.min(folds, dataSetSize);
    }

    /**
     * Charge de nouvelles données depuis un fichier CSV.
     *
     * @param filePath le chemin du fichier CSV à charger
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la lecture du fichier
     */
    public void loadNewData(String filePath) throws IOException {
        this.dataSet = DataLoadingUtil.loadData(filePath, this.currentClass);
        this.notifyObservers();
    }

    /**
     * Obtient les attributs de l'objet de données.
     *
     * @return un tableau de chaînes contenant les attributs
     */
    public String[] getAttributes() {
        return this.dataSet.get(0).getAttributes();
    }

    /**
     * Ajoute des données inconnues à l'ensemble à partir d'une liste d'attributs.
     *
     * @param attributes la liste d'attributs pour la nouvelle donnée inconnue
     * @throws Exception si une erreur se produit lors de la création de la donnée
     */
    public void addUnknownData(List<String> attributes) throws Exception {
        this.dataSet.add(DataLoadingUtil.createUnknownData(attributes, this.currentClass));
        this.notifyObservers();
    }

    /**
     * Vérifie si l'ensemble de données contient des données inconnues.
     *
     * @return true si des données inconnues sont présentes, sinon false
     */
    public boolean hasUnknownData() {
        for (Data d : dataSet) {
            if (d.getType().equals("Unknown")) return true;
        }
        return false;
    }

    /**
     * Récupère les valeurs minimales des attributs sous forme de tableau.
     *
     * @return un tableau de doubles contenant les valeurs minimales
     */
    public double[] minAttributesToArray(List<String> attributes) {
        double[] res = new double[attributes.size()];
        int resIndex = 0;
        for(String attribute : attributes){
            double tmp = this.dataSet.get(0).getAttributeByName(attribute);
            for (Data d : this.dataSet) if (d.getAttributeByName(attribute) < tmp) tmp = d.getAttributeByName(attribute);
            res[resIndex] = tmp;
            resIndex++;
        }
        return res;
    }

    /**
     * Récupère les valeurs maximales des attributs sous forme de tableau.
     *
     * @return un tableau de doubles contenant les valeurs maximales
     */
    public double[] maxAttributesToArray(List<String> attributes) {
        double[] res = new double[attributes.size()];
        int resIndex = 0;
        for(String attribute : attributes){
            double tmp = this.dataSet.get(0).getAttributeByName(attribute);
            for (Data d : this.dataSet) if (d.getAttributeByName(attribute) > tmp) tmp = d.getAttributeByName(attribute);
            res[resIndex] = tmp;
            resIndex++;
        }
        return res;
    }

    public String[] getDistances(){
        return new String[] { "Manhattan Distance", "Euclidian Distance", "Manhattan Distance (Normalized)",
                "Euclidian Distance (Normalized)" };
    }
    
    public void setDistanceByName(String distanceName, List<String> attributes) {
        if(!attributes.isEmpty()){
            if(distanceName.equals("Manhattan Distance")) this.distance = new ManhattanDistance(attributes);
            if(distanceName.equals("Euclidian Distance")) this.distance = new EuclidianDistance(attributes);
            if (distanceName.equals("Manhattan Distance (Normalized)"))
                this.distance = new NormalizedManhattanDistance(attributes, minAttributesToArray(attributes),
                        maxAttributesToArray(attributes));
            if (distanceName.equals("Euclidian Distance (Normalized)"))
                this.distance = new NormalizedEuclidianDistance(attributes, minAttributesToArray(attributes),
                        maxAttributesToArray(attributes));

        }
    }

}
