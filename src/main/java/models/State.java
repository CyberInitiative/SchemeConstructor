package models;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class State {
    public abstract void clickProcessing(MouseEvent event);
    public abstract void pressProcessing(MouseEvent event);
    public abstract void dragProcessing(MouseEvent event);
    public abstract void releaseProcessing(MouseEvent event);
    public abstract void setControlls();
    public abstract void changeState(State change, State state);
       
}
