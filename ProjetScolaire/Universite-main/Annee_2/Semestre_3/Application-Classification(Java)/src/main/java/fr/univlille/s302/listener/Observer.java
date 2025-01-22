package fr.univlille.s302.listener;

/**
 * Interface {@code Observer} qui représente un observateur dans le patron de conception
 * Observer.
 *
 * Les classes qui implémentent cette interface doivent définir la méthode {@code update()}
 * pour recevoir des notifications de changements d'état d'un objet observable.
 *
 * @author Benjamin Sere
 * @version 1.0
 */
public interface Observer {

        /**
         * Méthode appelée pour notifier l'observateur d'un changement d'état.
         */
        void update();
}