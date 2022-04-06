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

    //BooleanProperty connected = new SimpleBooleanProperty(false);

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
    public void notifyObservers() {
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

    public List<IObserver> getSocketObservers() {
        return socketObservers;
    }

    public void setSocketObservers(List<IObserver> socketObservers) {
        this.socketObservers = socketObservers;
    }

//    public BooleanProperty IsConnected() {
//        return connected;
//    }
//
//    public void setConnected(BooleanProperty connected) {
//        this.connected = connected;
//    }
//
//    public void setConnectedValue(boolean value) {
//        connected.setValue(value);
//    }
}
