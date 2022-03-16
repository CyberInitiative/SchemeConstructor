package models.partsOfComponents;

import models.schemeComponents.LogicComponent;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.connector.ConnectionAnchor;
import models.PathPoint;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public class Socket extends Circle implements IObserver, IComponentsPiece {

    private LogicComponent mediator;

    public enum Role {
        Input, Output;
    }

    private Role role;

    private final static double RADIUS = 5;

    private Signal signal;

    private ConnectionAnchor mainConnectedAnchor;
    private List<ConnectionAnchor> connectionAnchors = new ArrayList<>();

    private LogicComponent ownerComponent = null;
    private boolean connected = false;

    private IObserver[][] pointObservers;
    private PathPoint coveredPathPoint = null;

    public Socket() {
        super(RADIUS);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        getStrokeDashArray().addAll(2d);
        setStrokeWidth(1);
        setOpacity(0.95);
        toFront();
    }

    public Socket(Role role, LogicComponent mediator) {
        super(RADIUS);
        this.role = role;
        this.mediator = mediator;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        getStrokeDashArray().addAll(2d);
        setStrokeWidth(1);
        setOpacity(0.95);
        toFront();
    }

    public Socket(Role role) {
        super(RADIUS);
        this.role = role;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
        getStrokeDashArray().addAll(2d);
        setStrokeWidth(1);
        setOpacity(0.95);
        toFront();
    }

    public Socket(DoubleProperty x, DoubleProperty y, Role role) {
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

    public void bindNewValues(DoubleProperty x, DoubleProperty y) {
        x.unbind();
        y.unbind();
        x.bind(centerXProperty());
        y.bind(centerYProperty());
    }

    @Override
    public void update(IObservable observable) {
        ConnectionAnchor anchor = (ConnectionAnchor) observable;
        if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && mainConnectedAnchor == null) {
            mainConnectedAnchor = anchor;
            mainConnectedAnchor.setConnectedSocket(this);
            mainConnectedAnchor.centerXProperty().bind(this.centerXProperty());
            mainConnectedAnchor.centerYProperty().bind(this.centerYProperty());
            if (this.getRole() == Role.Output) {
                System.out.println("OUT: " + this);
                mainConnectedAnchor.setIsOutput(true);
            } else if (this.getRole() == Role.Input) {
                System.out.println("IN: " + this);
                mainConnectedAnchor.setIsOutput(false);
            }
            mainConnectedAnchor.setConnectedValue(true);
            //if (mainAnchor.getSecondEnd().getSocketsElementOwner() == mainAnchor.getSocketsElementOwner()) {
            /*
                Запретить соединение;
                
             */
            //}
        } else if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && mainConnectedAnchor != null) {
            connectionAnchors.add(anchor);
            anchor.setConnectedSocket(this);
            anchor.centerXProperty().bind(this.centerXProperty());
            anchor.centerYProperty().bind(this.centerYProperty());
            if (this.getRole() == Role.Output) {
                anchor.setIsOutput(true);
            } else if (this.getRole() == Role.Input) {
                /*
                Запретить подсоединение;
                 */
            }
        } else if ((this.getCenterX() != anchor.getCenterX() | this.getCenterY() != anchor.getCenterY()) && mainConnectedAnchor == anchor) {
            mainConnectedAnchor.setIsOutput(null);
            mainConnectedAnchor.setConnectedValue(false);
            this.setMainConnectedAnchor(null);
            anchor.setConnectedSocket(null);
            if (!connectionAnchors.isEmpty()) {
                mainConnectedAnchor = connectionAnchors.get(0);
            }
        }
        //System.out.println("UPDATED: " + this);
    }

    @Override
    public double getCorX() {
        return this.getCenterX();
    }

    @Override
    public double getCotY() {
        return this.getCenterY();
    }

    @Override
    public void setMediator(LogicComponent mediator) {
        this.mediator = mediator;
    }

    @Override
    public LogicComponent getMediator() {
        return mediator;
    }

    public void checkOwnerComponent() {
        if (ownerComponent != null) {
            System.out.println("Owner is not null");
        } else {
            System.out.println("Owner is null");
        }
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

    public LogicComponent getOwnerElement() {
        return ownerComponent;
    }

    public void setOwnerComponent(LogicComponent ownerComponent) {
        this.ownerComponent = ownerComponent;
    }

    public ConnectionAnchor getMainConnectedAnchor() {
        return mainConnectedAnchor;
    }

    public void setMainConnectedAnchor(ConnectionAnchor mainConnectedAnchor) {
        this.mainConnectedAnchor = mainConnectedAnchor;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public IObserver[][] getPointObservers() {
        return pointObservers;
    }

    public void setPointObservers(IObserver[][] pointObservers) {
        this.pointObservers = pointObservers;
    }

    public PathPoint getCoveredPathPoint() {
        return coveredPathPoint;
    }

    public void setCoveredPathPoint(PathPoint coveredPathPoint) {
        this.coveredPathPoint = coveredPathPoint;
    }
}
