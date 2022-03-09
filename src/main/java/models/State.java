package models;

import com.gluonapplication.views.PrimaryPresenter;
import com.gluonapplication.views.PrimaryView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class State {

    protected PrimaryPresenter context;

    public State() {

    }

    public State(PrimaryPresenter context) {
        this.context = context;
    }

    public abstract void clickProcessing(MouseEvent event);

    public abstract void pressProcessing(MouseEvent event);

    public abstract void dragProcessing(MouseEvent event);

    public abstract void releaseProcessing(MouseEvent event);

    public abstract void setControlls();

    public abstract void changeState(State change, State state);

    public PrimaryPresenter getContext() {
        return context;
    }

    public void setContext(PrimaryPresenter context) {
        this.context = context;
    }
}
