package models.connector;

import models.partsOfComponents.Socket;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionAnchor extends Circle implements IObservable, IConnectorsPiece {

    private Boolean isOutput = null;

    BooleanProperty connected = new SimpleBooleanProperty(false);

    private double corX = 0;
    private double corY = 0;

    private double mouseX = 0;
    private double mouseY = 0;

    private Connector mediator;

    private Socket connectedSocket; //Cокет, к которому цепляется якорь соединителя.

    private List<IObserver> socketObservers;

    public ConnectionAnchor(DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get(), 5);

        x.bind(centerXProperty());
        y.bind(centerYProperty());

        setFill(Color.rgb(138, 149, 151));
        setStroke(Color.BLACK);
    }

    public ConnectionAnchor(List<IObserver> socketObservers) {
        super(5);
        this.socketObservers = socketObservers;
        setFill(Color.rgb(138, 149, 151));
        setStroke(Color.BLACK);
    }

    public void bind(DoubleProperty x, DoubleProperty y) {
        x.bind(centerXProperty());
        y.bind(centerYProperty());
    }

    public ConnectionAnchor requestSecondAnchor(ConnectionAnchor anchor) {
        return mediator.getOppositeAnchor(anchor);
    }

    public ElementConnector requestConnectionLine() {
        return mediator.getConnectionLine();
    }

    public ConnectionPath requestConnectionPath() {
        return mediator.getConnectionPath();
    }

    public Socket getConnectedSocket() {
        return connectedSocket;
    }

    public void setConnectedSocket(Socket connectedSocket) {
        this.connectedSocket = connectedSocket;
    }

    @Override
    public void registerObserver(IObserver observer) { //Observer interface
        socketObservers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) { //Observer interface
        socketObservers.remove(observer);
    }

    @Override
    public void notifyObservers() { //Observer interface
        //System.out.println("SIZE: " + socketObservers.size() + "; SOCKET OBSERVER: " + socketObservers);
        for (IObserver observer : socketObservers) {
            observer.update(this);
        }
    }

    @Override
    public void setMediator(Connector mediator) { //Component interface
        this.mediator = mediator;
    }

    public Connector getMediator() {
        return mediator;
    }

    public double getCorX() {
        return corX;
    }

    public void setCorX(double corX) {
        this.corX = corX;
    }

    public double getCorY() {
        return corY;
    }

    public void setCorY(double corY) {
        this.corY = corY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public Boolean getIsOutput() {
        return isOutput;
    }

    public void setIsOutput(Boolean isOutput) {
        this.isOutput = isOutput;
    }

    public List<IObserver> getSocketObservers() {
        return socketObservers;
    }

    public void setSocketObservers(List<IObserver> socketObservers) {
        this.socketObservers = socketObservers;
    }

    public BooleanProperty IsConnected() {
        return connected;
    }

    public void setConnected(BooleanProperty connected) {
        this.connected = connected;
    }

    public void setConnectedValue(boolean value) {
        connected.setValue(value);
    }
}
