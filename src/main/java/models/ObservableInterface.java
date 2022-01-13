package models;

import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author Miroslav Levdikov
 */
public interface ObservableInterface {
    void registerObserver(ObserverInterface observer);
    void removeObserver(ObserverInterface observer);
    void notifyObservers();
}
