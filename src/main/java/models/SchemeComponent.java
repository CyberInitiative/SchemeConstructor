package models;

import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class SchemeComponent extends Rectangle implements ObservableInterface {

    protected Line outputLine;
    protected Socket connectionOutputSocket;

    protected Text symbol = new Text("?");

    protected double corX = 0; //координаты компонента (верхний левый угол);
    protected double corY = 0;

    protected double mouseX = 0; //координаты мыши;
    protected double mouseY = 0;

    protected ObserverInterface[][] pointObservers;

    public SchemeComponent(int width, int height) {
        super(width, height);
    }

    protected abstract void setNodeVisualDetails();

    protected abstract void bindSymbol();

    protected abstract void bindOutput();

    public abstract void setVisualComponentsToFront();

    public abstract void prepareToChangePosition(Pane source);

    public abstract void changedPositionReport();

    public Text getSymbol() {
        return symbol;
    }

    public void setCorX(double corX) {
        this.corX = corX;
    }

    public void setCorY(double corY) {
        this.corY = corY;
    }

    public double getCorX() {
        return corX;
    }

    public double getCorY() {
        return corY;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public Line getOutputLine() {
        return outputLine;
    }

    public Socket getConnectionOutputSocket() {
        return connectionOutputSocket;
    }

}