package models.tools;

import com.gluonapplication.views.PrimaryPresenter;
import javafx.scene.input.MouseEvent;

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

    protected double mouseX = 0; //координаты мыши;
    protected double mouseY = 0;

    public abstract void clickProcessing(MouseEvent event);

    public abstract void pressProcessing(MouseEvent event);

    public abstract void dragProcessing(MouseEvent event);

    public abstract void releaseProcessing(MouseEvent event);

    protected double getMouseX() {
        return mouseX;
    }

    protected void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    protected double getMouseY() {
        return mouseY;
    }

    protected void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setContext(PrimaryPresenter context) {
        this.context = context;
    }

    public PrimaryPresenter getContext() {
        return context;
    }
}
