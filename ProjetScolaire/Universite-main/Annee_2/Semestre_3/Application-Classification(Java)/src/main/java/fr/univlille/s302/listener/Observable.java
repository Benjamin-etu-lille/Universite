package fr.univlille.s302.listener;

import java.util.Collection;
import java.util.HashSet;

/**
 * Classe abstraite {@code Observable} qui représente un objet pouvant être observé par des {@link Observer}.
 *
 * Cette classe fournit des méthodes pour attacher et détacher des observateurs, ainsi que pour
 * notifier tous les observateurs attachés lors d'un changement d'état.
 *
 * @author Benjamin Sere
 * @version 1.0
 */
public abstract class Observable {

    protected Collection<Observer> attached = new HashSet<>();
    protected Collection<Observer> toDetach = new HashSet<>();

    public void attach(Observer obs) {
        attached.add(obs);
    }

    public void detach(Observer obs) {
        this.toDetach.add(obs);
    }

    protected void notifyObservers() {
        this.updateList();
        for (Observer o : attached) {
            o.update();
        }
    }

    private void updateList() {
        this.attached.removeAll(toDetach);
        this.toDetach.clear();
    }

}
