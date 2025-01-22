package fr.univlille.s302.controller;

import java.util.List;

import fr.univlille.s302.exception.IncorrectFileNameException;
import fr.univlille.s302.model.Data;
import fr.univlille.s302.model.DataSet;
import fr.univlille.s302.model.Iris;
import fr.univlille.s302.model.Pokemon;
import fr.univlille.s302.view.ErrorView;
import fr.univlille.s302.view.FileExplorerView;
import fr.univlille.s302.view.NewPointView;
import fr.univlille.s302.view.ScatterView;

/**
 * Classe Controlleur qui gère les interactions entre le modèle (DataSet)
 * et les vues. Elle fournit des méthodes pour charger des données, classer
 * des données, ajouter de nouveaux points et gérer différentes vues pour
 * l'affichage de graphiques et la gestion des erreurs.
 *
 * @author Thibault Croisier & Louis Bedu & Benjamin Sere
 * @version 2.0
 */
public class Controller {
    
    private DataSet dataSet;

    /**
     * Constructeur du Controller. Initialise le jeu de données en chargeant
     * les données depuis la vue du sélecteur de fichiers et instancie une
     * nouvelle fenêtre de graphique.
     */
    public Controller() {
        try {
            String path = new FileExplorerView().open();
            this.dataSet = new DataSet(path, this.getCurrentClass(path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new ErrorView(e.getMessage());
        }
        this.newGraphWindow();
    }

    private Class<? extends Data> getCurrentClass(String path) throws IncorrectFileNameException{
        if(path.toLowerCase().contains("pokemon")) return Pokemon.class;
        if(path.toLowerCase().contains("iris")) return Iris.class;
        throw new IncorrectFileNameException();
    }

    /**
     * Crée et affiche une nouvelle fenêtre ScatterView pour visualiser le jeu
     * de données.
     */
    public void newGraphWindow() {
        new ScatterView(dataSet, this);
    }

    /**
     * Crée et affiche une nouvelle fenêtre NewPointView pour ajouter un nouveau
     * point de données dans le jeu de données.
     */
    public void newPointWindow() {
        new NewPointView(dataSet, this);
    }

    /**
     * Ajoute un nouveau point au jeu de données. Les attributs du point sont fournis
     * sous forme d'une liste de chaînes de caractères. La méthode gère les exceptions
     * qui pourraient survenir lors de l'ajout de ces données.
     *
     * @param attributes Liste de chaînes de caractères représentant les attributs du
     *                  nouveau point de données.
     */
    public void addNewPoint(List<String> attributes) {
        try {
            this.dataSet.addUnknownData(attributes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new ErrorView(e.getMessage());
        }
    }

    /**
     * Classe les données inconnues dans le jeu de données en utilisant la méthode
     * de classification KNN avec la valeur de k fournie. En cas d'erreur, un message
     * d'erreur est affiché.
     *
     * @param knnValue La valeur de k pour l'algorithme KNN.
     */
    public void classifyData(int knnValue, List<String> attributes) {
        try {
            // Tente de classer les données inconnues à l'aide de l'algorithme KNN
            this.dataSet.classifyUnknownData(knnValue, attributes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new ErrorView(e.getMessage());
        }
    }

    /**
     * Affiche un aperçu de la robustesse de l'algorithme en utilisant la méthode
     * de robustesse avec la valeur de k fournie. En cas d'erreur, un message d'erreur
     * est affiché.
     *
     * @param knnValue La valeur de k pour l'algorithme KNN.
     */
    public void robustessPreview(int knnValue, List<String> attributes) {
        try {
            this.dataSet.robustnessAlgo(knnValue, attributes);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            new ErrorView(e.getMessage());
        }
    }

    /**
     * Charge un nouveau jeu de données en ouvrant un fichier via la vue FileExplorerView.
     * En cas d'erreur, un message d'erreur est affiché.
     */
    public void loadNewData() {
        try {
            this.dataSet.loadNewData(new FileExplorerView().open());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new ErrorView(e.getMessage());
        }
    }

    public void setDistance(String distance, List<String> attributes){
        try {
            this.dataSet.setDistanceByName(distance, attributes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            new ErrorView(e.getMessage());
        }
    }


}
