package models;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class Socket extends Circle implements ObserverInterface {

    public enum Role {
        Input, Output;
    }

    private Role role;

    private final static double RADIUS = 5;

    private ConnectionAnchor connector;

    private Movable ownerComponent = null;
    private boolean connected = false;

    Socket(Role role) {
        super(RADIUS);
        this.role = role;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        getStrokeDashArray().addAll(2d);
        setStrokeWidth(1);
        setOpacity(0.95);
        toFront();
    }

    Socket(DoubleProperty x, DoubleProperty y, Role role) {
        super(x.get(), y.get(), RADIUS);
        this.role = role;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        getStrokeDashArray().addAll(2d);
        setStrokeWidth(1);
        setOpacity(0.95);
        toFront();
        x.bind(centerXProperty());
        y.bind(centerYProperty());
    }

    public void bind(DoubleProperty x, DoubleProperty y) {
        x.unbind();
        y.unbind();
        x.bind(centerXProperty());
        y.bind(centerYProperty());
    }

    @Override
    public void update(ObservableInterface observable) {
        ConnectionAnchor anchor = (ConnectionAnchor) observable;
        anchor.setIdentificator(observable.hashCode());
        if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && connector == null) {
            anchor.centerXProperty().bind(this.centerXProperty());
            anchor.centerYProperty().bind(this.centerYProperty());
            anchor.setSocketsElementOwner(ownerComponent);
            connector = anchor;
            if (connector.getSecondEnd().getSocketsElementOwner() == connector.getSocketsElementOwner()) {
                /*
                Запретить соединение;
                
                 */
            }
        } else if ((this.getCenterX() != anchor.getCenterX() | this.getCenterY() != anchor.getCenterY()) && connector != null) {
            if (connector.getCenterX() != this.getCenterX() | connector.getCenterY() != this.getCenterY()) {
                connector.centerXProperty().unbind();
                connector.centerYProperty().unbind();
                connector = null;
            }
        }
        System.out.println("UPDATED: " + this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static double getRADIUS() {
        return RADIUS;
    }

    public Movable getOwnerElement() {
        return ownerComponent;
    }

    public void setOwnerComponent(Movable ownerComponent) {
        this.ownerComponent = ownerComponent;
    }

    public ConnectionAnchor getConnector() {
        return connector;
    }

    public void setConnector(ConnectionAnchor connector) {
        this.connector = connector;
    }

    @Override
    public String toString() {
        return "Socket{" + " connector=" + connector + ", ownerElement=" + ownerComponent + '}';
    }
}
