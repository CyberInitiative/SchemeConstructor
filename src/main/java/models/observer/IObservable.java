package models.observer;

import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public interface IObservable {
    void registerObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void notifyObservers();
}
