package models.schemeComponents;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.PathPoint;
import models.connector.Connector;
import models.partsOfComponents.Body;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public abstract class LogicComponent {

    protected int bodyWidth;
    protected int bodyHeight;

    protected Body body;

    protected int maxNumOfSockets;
    protected int minNumOfSockets;
    protected int numOfSockets;

    protected ObservableList<ElementLine> connectionLines = FXCollections.observableArrayList();
    protected ObservableList<Socket> connectionSockets = FXCollections.observableArrayList();

    protected Text symbol = new Text("?");

    protected IObserver[][] pointObservers;

    public LogicComponent(int width, int height) {
        body = new Body(width, height);
    }

    protected abstract void createSockets();

    protected abstract void createLines();

    protected abstract void bindSymbol();

    public abstract void setVisualComponentsToFront();

    public abstract void setMediator();

    protected abstract void setVisualSettings();

    //public abstract void addGraphElemsOnWorkspace(Pane workspace, boolean tool);
    public abstract ArrayList<Node> getComponentsNodes();

    //public abstract void remGraphlElemsFromWorkspace(Pane workspace);

    public void notifyObservers() {
        body.notifyObservers();
        for (Socket socket : connectionSockets) {
            socket.notifyObservers();
        }
    }

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
