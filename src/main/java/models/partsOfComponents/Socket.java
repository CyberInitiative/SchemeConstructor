package models.partsOfComponents;

import models.schemeComponents.LogicComponent;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.connector.ConnectionAnchor;
import models.PathPoint;
import models.connector.Connector;
import models.connector.IConnectorsPiece;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public class Socket extends Circle implements IObserver, IObservable, IComponentsPiece, IConnectorsPiece {

    private LogicComponent logicComponenetMediator;
    private Connector connectorMediator;

    public enum Role {
        Input, Output;
    }

    private Role role;

    private final static double RADIUS = 5;

    private Signal signal;

    private IObserver[][] pointObservers;
    private PathPoint coveredPathPoint = null;

    private List<Connector> connectorsList = new ArrayList<>();

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
        this.logicComponenetMediator = mediator;
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

    public boolean isConnected() {
        for (Connector connector : connectorsList) {
            if (connector.getStartSocket() == this || connector.getEndSocket() == this) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(IObservable observable) {
        ConnectionAnchor anchor = (ConnectionAnchor) observable;
        if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY()) {
            anchor.centerXProperty().bind(this.centerXProperty());
            anchor.centerYProperty().bind(this.centerYProperty());
            anchor.getMediator().setEndSocket(this);
            //anchor.setConnectedValue(true);
            this.getConnectorsList().add(anchor.getMediator());
            anchor.getMediator().getStartSocket().getConnectorsList().add(anchor.getMediator());
            System.out.println("HERE");

            //this.setFill(Color.rgb(138, 149, 151));
            //this.getStrokeDashArray().removeAll(2d);
        } else if ((this.getCenterX() != anchor.getCenterX() | this.getCenterY() != anchor.getCenterY())) {
            //anchor.setConnectedValue(false);
            if (anchor.getMediator().getEndSocket() == this) {
                if (connectorsList.contains(anchor.getMediator())) {
                    this.getConnectorsList().remove(anchor.getMediator());
                }
                if (this.getConnectorMediator().getOppositeSocket(this).getConnectorsList().contains(anchor.getMediator())) {
                    this.getConnectorMediator().getOppositeSocket(this).getConnectorsList().remove(anchor.getMediator());
                }
                anchor.getMediator().setEndSocket(null);
            }
        }
        //System.out.println("UPDATED: " + this);
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < pointObservers.length; i++) {
            for (int j = 0; j < pointObservers[0].length; j++) {
                pointObservers[i][j].update(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Socket{" + "role=" + role + ";X " + getCenterX() + ";Y " + getCenterY() + '}';
    }

    @Override
    public double getLayoutCorX() {
        return this.getCenterX();
    }

    @Override
    public double getCotLayoutCorY() {
        return this.getCenterY();
    }

    @Override
    public void setLogCompMediator(LogicComponent logicComponenetMediator
    ) {
        this.logicComponenetMediator = logicComponenetMediator;
    }

    @Override
    public void setMediator(Connector connectorMediator) {
        this.connectorMediator = connectorMediator;
    }

    @Override
    public LogicComponent getLogicComponenetMediator() {
        return logicComponenetMediator;
    }

//    public List<ConnectionAnchor> getConnectionAnchors() {
//        return connectionAnchors;
//    }
//
//    public void setConnectionAnchors(List<ConnectionAnchor> connectionAnchors) {
//        this.connectionAnchors = connectionAnchors;
//    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static double getRADIUS() {
        return RADIUS;
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

    public Connector getConnectorMediator() {
        return connectorMediator;
    }

    public void setConnectorMediator(Connector connectorMediator) {
        this.connectorMediator = connectorMediator;
    }

    public List<Connector> getConnectorsList() {
        return connectorsList;
    }

    public void setConnectorsList(List<Connector> connectorsList) {
        this.connectorsList = connectorsList;
    }   
}
