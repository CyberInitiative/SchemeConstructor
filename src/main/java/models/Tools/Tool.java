package models.Tools;

import com.gluonapplication.views.PrimaryPresenter;
import com.gluonapplication.views.PrimaryView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class Tool {

    /*
    Класс Tool является базовым классом для состояний;
     */
    protected PrimaryPresenter context;

    public Tool() {

    }

    public abstract void clickProcessing(MouseEvent event);

    public abstract void pressProcessing(MouseEvent event);

    public abstract void dragProcessing(MouseEvent event);

    public abstract void releaseProcessing(MouseEvent event);

    public void setContext(PrimaryPresenter context) {
        this.context = context;
    }

    public PrimaryPresenter getContext() {
        return context;
    }
}
