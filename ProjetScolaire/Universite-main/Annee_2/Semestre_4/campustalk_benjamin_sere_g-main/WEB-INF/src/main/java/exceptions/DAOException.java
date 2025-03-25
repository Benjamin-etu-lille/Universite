package exceptions;

/**
 * Exception personnalisée pour la gestion des erreurs liées aux opérations DAO.
 * Cette exception permet d'encapsuler des exceptions sous-jacentes et de fournir des messages d'erreur détaillés.
 */
public class DAOException extends Exception {

    /**
     * Constructeur pour une exception sans cause.
     *
     * @param message Le message détaillé de l'exception.
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructeur pour une exception avec une cause sous-jacente.
     *
     * @param message Le message détaillé de l'exception.
     * @param cause   La cause sous-jacente de l'exception.
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
