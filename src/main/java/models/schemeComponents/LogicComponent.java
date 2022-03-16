package models.schemeComponents;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.partsOfComponents.Body;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class LogicComponent implements IObservable {

    protected int bodyWidth;
    protected int bodyHeight;

    protected Body body;

    protected int maxNumOfSockets;
    protected int minNumOfSockets;
    protected int numOfSockets;

    protected ObservableList<ElementLine> connectionLines = FXCollections.observableArrayList();
    protected ObservableList<Socket> connectionSockets = FXCollections.observableArrayList();

    protected Text symbol = new Text("?");

    //координаты тела компонента (верхний левый угол);
    protected double corX = 0;
    protected double corY = 0;

    protected IObserver[][] pointObservers;

    public LogicComponent(int width, int height) {
        body = new Body(width, height);
    }

    protected abstract void createSockets();

    protected abstract void createLines();

    protected abstract void bindSymbol();

    public abstract void setVisualComponentsToFront();

    //public abstract void prepareToChangePosition(Pane source);
    //public abstract void changedPositionReport();
    public abstract void setMediator();

    protected abstract void setVisualDetails();

    public abstract void addOnWorkspace(Pane workspace, boolean tool);

    public abstract void removeFromWorkspace(Pane workspace);

    public int getBodyWidth() {
        return bodyWidth;
    }

    public int getBodyHeight() {
        return bodyHeight;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

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

    public ObservableList<ElementLine> getConnectionLines() {
        return connectionLines;
    }

    public void setConnectionLines(ObservableList<ElementLine> connectionLines) {
        this.connectionLines = connectionLines;
    }

    public ObservableList<Socket> getConnectionSockets() {
        return connectionSockets;
    }

    public void setConnectionSockets(ObservableList<Socket> connectionSockets) {
        this.connectionSockets = connectionSockets;
    }

    public int getMaxNumOfSockets() {
        return maxNumOfSockets;
    }

    public void setMaxNumOfSockets(int maxNumOfSockets) {
        this.maxNumOfSockets = maxNumOfSockets;
    }

    public int getMinNumOfSockets() {
        return minNumOfSockets;
    }

    public void setMinNumOfSockets(int minNumOfSockets) {
        this.minNumOfSockets = minNumOfSockets;
    }

    public int getNumOfSockets() {
        return numOfSockets;
    }

    public void setNumOfSockets(int numOfSockets) {
        this.numOfSockets = numOfSockets;
    }
}
