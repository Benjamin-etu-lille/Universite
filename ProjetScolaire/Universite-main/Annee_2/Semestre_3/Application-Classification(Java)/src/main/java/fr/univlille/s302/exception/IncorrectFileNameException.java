package fr.univlille.s302.exception;

/**
 * Exception levée lorsque le nom du fichier chargé n'est pas valide.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class IncorrectFileNameException extends Exception {

    /**
     * Constructeur par défaut pour l'exception NoDataToClassifyException.
     */
    public IncorrectFileNameException() {
        super();
    }

    /**
     * Retourne le message d'erreur associé à cette exception.
     *
     * @return un message indiquant qu'il n'y a pas de données à classifier.
     */
    @Override
    public String getMessage() {
        return "Incorrect file name. The file name must contain the type of data you want to classify (ex : pokemon.csv)";
    }
}
