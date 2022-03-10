package models;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionAnchor extends Circle implements ObservableInterface, ConnectionComponent {

    //private String name;
    private Boolean status = null; //status is?

    private boolean isConnected = false;
    private boolean isDragging = false;

    private double corX = 0;
    private double corY = 0;

    private double mouseX = 0;
    private double mouseY = 0;

    private Connector mediator;

    private SchemeComponent socketsElementOwner;
    private Socket connectedSocket;

    private List<ObserverInterface> socketObservers;

    ConnectionAnchor(DoubleProperty x, DoubleProperty y, List<ObserverInterface> socketObservers) {
        super(x.get(), y.get(), 5);
        this.socketObservers = socketObservers;

        x.bind(centerXProperty());
        y.bind(centerYProperty());

        setFill(Color.rgb(138, 149, 151));
        setStroke(Color.BLACK);
        //listener();
    }

    ConnectionAnchor(List<ObserverInterface> socketObservers) {
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
    public void registerObserver(ObserverInterface observer) { //Observer interface
        socketObservers.add(observer);
    }

    @Override
    public void removeObserver(ObserverInterface observer) { //Observer interface
        socketObservers.remove(observer);
    }

    @Override
    public void notifyObservers() { //Observer interface
        for (ObserverInterface observer : socketObservers) {
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

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isIsDragging() {
        return isDragging;
    }

    public void setIsDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ObserverInterface> getSocketObservers() {
        return socketObservers;
    }

    public void setSocketObservers(List<ObserverInterface> socketObservers) {
        this.socketObservers = socketObservers;
    }

    public SchemeComponent getSocketsElementOwner() {
        return socketsElementOwner;
    }

    public void setSocketsElementOwner(SchemeComponent socketsElementOwner) {
        this.socketsElementOwner = socketsElementOwner;
    }

}
