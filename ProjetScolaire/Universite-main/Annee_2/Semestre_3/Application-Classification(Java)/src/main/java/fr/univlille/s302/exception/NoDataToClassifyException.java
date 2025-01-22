package fr.univlille.s302.exception;

/**
 * Exception levée lorsque aucune donnée n'est disponible pour la classification.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class NoDataToClassifyException extends Exception {

    /**
     * Constructeur par défaut pour l'exception NoDataToClassifyException.
     */
    public NoDataToClassifyException() {
        super();
    }

    /**
     * Retourne le message d'erreur associé à cette exception.
     *
     * @return un message indiquant qu'il n'y a pas de données à classifier.
     */
    @Override
    public String getMessage() {
        return "There is no data to classify!\nAdd a new point to classify.";
    }
}