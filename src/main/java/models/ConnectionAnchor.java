package models;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionAnchor extends Circle implements ObservableInterface {

    private Boolean status = null;

    private boolean isConnected = false;
    private boolean isDragging = false;

    private double corX = 0;
    private double corY = 0;

    private double mouseX = 0;
    private double mouseY = 0;

    private Line connectionLine;
    private ConnectionAnchor secondEnd;
    private Movable socketsElementOwner;

    private int identificator = this.hashCode();

    private List<ObserverInterface> socketObservers;

    ConnectionAnchor(DoubleProperty x, DoubleProperty y, List<ObserverInterface> socketObservers) {
        super(x.get(), y.get(), 5);
        this.socketObservers = socketObservers;

        x.bind(centerXProperty());
        y.bind(centerYProperty());

        setFill(Color.rgb(138, 149, 151));
        setStroke(Color.BLACK);
    }

    @Override
    public void registerObserver(ObserverInterface observer) {
        socketObservers.add(observer);
    }

    @Override
    public void removeObserver(ObserverInterface observer) {
        socketObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ObserverInterface observer : socketObservers) {
            observer.update(this);
        }
    }

    public int getIdentificator() {
        return identificator;
    }

    public void setIdentificator(int identificator) {
        this.identificator = identificator;
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

    public Line getConnectionLine() {
        return connectionLine;
    }

    public void setConnectionLine(Line connectionLine) {
        this.connectionLine = connectionLine;
    }

    public boolean isIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public ConnectionAnchor getSecondEnd() {
        return secondEnd;
    }

    public void setSecondEnd(ConnectionAnchor secondEnd) {
        this.secondEnd = secondEnd;
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

    public Movable getSocketsElementOwner() {
        return socketsElementOwner;
    }

    public void setSocketsElementOwner(Movable socketsElementOwner) {
        this.socketsElementOwner = socketsElementOwner;
    }

}
