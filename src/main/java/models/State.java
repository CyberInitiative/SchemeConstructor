package models;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class State {
    public abstract void clickEvent(Pane target);
    public abstract void pressEvent(Pane target);
    public abstract void dragEvent(Node target, ScrollPane scroll);
    public abstract void releaseEvent(Node target);
    public abstract void setControlls();
    
}
