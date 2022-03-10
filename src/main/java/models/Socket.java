package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.sockets;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class Socket extends Circle implements ObserverInterface, ObservableInterface {

    public enum Role {
        Input, Output;
    }

    private Role role;

    private final static double RADIUS = 5;

    private Signal signal;

    private ConnectionAnchor mainConnectedAnchor;
    private List<ConnectionAnchor> connectionAnchors = new ArrayList<>();

    private SchemeComponent ownerComponent = null;
    private boolean connected = false;

    private ObserverInterface[][] pointObservers;
    private PathPoint coveredPathPoint = null;

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
        if (this.getCenterX() == anchor.getCenterX() && this.getCenterY() == anchor.getCenterY() && mainConnectedAnchor == null) {
            mainConnectedAnchor = anchor;
            mainConnectedAnchor.setConnectedSocket(this);
            mainConnectedAnchor.centerXProperty().bind(this.centerXProperty());
            mainConnectedAnchor.centerYProperty().bind(this.centerYProperty());
            mainConnectedAnchor.setSocketsElementOwner(ownerComponent);
            if (this.getRole() == Role.Output) {
                mainConnectedAnchor.setStatus(true);
            } else if (this.getRole() == Role.Input) {
                mainConnectedAnchor.setStatus(false);
            }
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
            anchor.setSocketsElementOwner(ownerComponent);
            if (this.getRole() == Role.Output) {
                anchor.setStatus(true);
            } else if (this.getRole() == Role.Input) {
                /*
                Запретить подсоединение;
                 */
            }
        } else if ((this.getCenterX() != anchor.getCenterX() | this.getCenterY() != anchor.getCenterY()) && mainConnectedAnchor == anchor) {
            this.setMainConnectedAnchor(null);
            anchor.setConnectedSocket(null);
            if (!connectionAnchors.isEmpty()) {
                mainConnectedAnchor = connectionAnchors.get(0);
            }
        }
        //System.out.println("UPDATED: " + this);
    }

//    public void swapSignals(Socket socket) {
//        Signal temp;
//        temp = socket.getSignal();
//        socket.setSignal(this.getSignal());
//        this.setSignal(temp);
//    }
    @Override
    public void registerObserver(ObserverInterface observer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeObserver(ObserverInterface observer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyObservers() {
//        for (int i = 0; i < pointObservers.length; i++) {
//            for (int j = 0; j < pointObservers[0].length; j++) {
//                for (ObserverInterface socket : sockets) {
//                    pointObservers[i][j].update((ObservableInterface) socket);
//                }
////                for (Element elem : elements) {
////                    pointObservers[i][j].update(elem);
////                    System.out.println("P");
////                }
//            }
//        }
    }

    public void checkOwnerComponent() {
        if (ownerComponent != null) {
            System.out.println("Owner is not null");
        } else {
            System.out.println("Owner is null");
        }
    }

    public void requestToSetPathLineToNull(Pane source) {
        if (this.mainConnectedAnchor != null) {
            if (this.mainConnectedAnchor.requestConnectionPath() != null) {
                var pathLine = this.mainConnectedAnchor.requestConnectionPath().getPathPolyline();
                if (pathLine != null) {
                    source.getChildren().remove(pathLine);
                }
            }
            mainConnectedAnchor.requestConnectionLine().setVisible(true);
        }
    }  

    @Override
    public String toString() {
        return "Socket: " + "role: " + role + ", sig: " + signal + ", mainAnc: " + mainConnectedAnchor + ", connAnc:" + connectionAnchors
                + ", coveredPoint: " + coveredPathPoint + '}';
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

    public SchemeComponent getOwnerElement() {
        return ownerComponent;
    }

    public void setOwnerComponent(SchemeComponent ownerComponent) {
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

    public ObserverInterface[][] getPointObservers() {
        return pointObservers;
    }

    public void setPointObservers(ObserverInterface[][] pointObservers) {
        this.pointObservers = pointObservers;
    }

    public PathPoint getCoveredPathPoint() {
        return coveredPathPoint;
    }

    public void setCoveredPathPoint(PathPoint coveredPathPoint) {
        this.coveredPathPoint = coveredPathPoint;
    }
}