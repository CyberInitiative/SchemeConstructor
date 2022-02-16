package models;

import java.util.ArrayList;
import java.util.List;
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

    private Signal signal;

    private ConnectionAnchor mainAnchor;
    private List<ConnectionAnchor> connectionAnchors = new ArrayList<>();

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
        if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && mainAnchor == null) {
            mainAnchor = anchor;
            mainAnchor.setConnectedSocket(this);
            mainAnchor.centerXProperty().bind(this.centerXProperty());
            mainAnchor.centerYProperty().bind(this.centerYProperty());
            mainAnchor.setSocketsElementOwner(ownerComponent);
            if (this.getRole() == Role.Output) {
                mainAnchor.setStatus(true);
            } else if (this.getRole() == Role.Input) {
                mainAnchor.setStatus(false);
            }
            //if (mainAnchor.getSecondEnd().getSocketsElementOwner() == mainAnchor.getSocketsElementOwner()) {
            /*
                Запретить соединение;
                
             */
            //}
        } else if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && mainAnchor != null) {
            System.out.println("here");
            connectionAnchors.add(anchor);
            anchor.setConnectedSocket(this);
            anchor.centerXProperty().bind(this.centerXProperty());
            anchor.centerYProperty().bind(this.centerYProperty());
            anchor.setSocketsElementOwner(ownerComponent);
            if (this.getRole() == Role.Output) {
                anchor.setStatus(true);
            } else if (this.getRole() == Role.Input) {
                /*
                Запретить подсоединение;
                 */
            }
        } else if ((this.getCenterX() != anchor.getCenterX() | this.getCenterY() != anchor.getCenterY()) && mainAnchor == anchor) {
            anchor.setConnectedSocket(null);
            mainAnchor = null;
            if (!connectionAnchors.isEmpty()) {
                mainAnchor = connectionAnchors.get(0);
            }
        }
        //System.out.println("UPDATED: " + this);
    }

    public void checkOwnerComponent() {
        if (ownerComponent != null) {
            System.out.println("Owner is not null");
        } else {
            System.out.println("Owner is null");
        }
    }

    @Override
    public String toString() {
        return "Socket: " + "role: " + role + ", signa: " + signal + ", mainAnchor=" + mainAnchor + ", connectionAnchors=" + connectionAnchors + '}';
    }

    public List<ConnectionAnchor> getConnectionAnchors() {
        return connectionAnchors;
    }

    public void setConnectionAnchors(List<ConnectionAnchor> connectionAnchors) {
        this.connectionAnchors = connectionAnchors;
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

    public ConnectionAnchor getMainAnchor() {
        return mainAnchor;
    }

    public void setMainAnchor(ConnectionAnchor mainAnchor) {
        this.mainAnchor = mainAnchor;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }
}
