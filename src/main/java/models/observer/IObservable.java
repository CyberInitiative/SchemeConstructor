package models.observer;

import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public interface IObservable {
    void notifyObservers();
}
