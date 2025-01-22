package fr.univlille.s302.exception;

/**
 * Exception levée lorsque aucun attribut n'est sélectionné pour la classification.
 *
 * @author Thibault Croisier
 * @version 1.0
 */
public class NoAttributeSelected extends Exception {

    /**
     * Constructeur par défaut pour l'exception NoDataToClassifyException.
     */
    public NoAttributeSelected() {
        super();
    }

    /**
     * Retourne le message d'erreur associé à cette exception.
     *
     * @return un message indiquant qu'il n'y a pas de données à classifier.
     */
    @Override
    public String getMessage() {
        return "There is no attribut selected\n Please select at least one attribut";
    }
}
