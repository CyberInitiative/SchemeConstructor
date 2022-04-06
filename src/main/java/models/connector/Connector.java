package models.connector;

import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.PathPoint;
import models.observer.IObserver;
import models.partsOfComponents.Socket;

/**
 *
 * @author Miroslav Levdikov
 */
public final class Connector {

    private ConnectionAnchor anchor;

    private ElementConnector connectionLine;

    private Socket startSocket;
    private Socket endSocket;

    private ConnectionPath connectionPath = new ConnectionPath();

    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty endX;
    private DoubleProperty endY;

    protected PathPoint[][] pathPoints;

    public Connector(Socket socket, PathPoint pathPoints[][], List<IObserver> sockets) {
        startSocket = socket;
        this.pathPoints = pathPoints;
        startX = new SimpleDoubleProperty(startSocket.getCenterX());
        startY = new SimpleDoubleProperty(startSocket.getCenterY());
        endX = new SimpleDoubleProperty(startSocket.getCenterX());
        endY = new SimpleDoubleProperty(startSocket.getCenterY());

        endX.bind(startSocket.centerXProperty());
        endY.bind(startSocket.centerYProperty());

        anchor = new ConnectionAnchor(startX, startY);
        anchor.setSocketObservers(sockets);

        connectionLine = new ElementConnector(startX, startY, endX, endY);

        anchor.toFront();
    }

    public boolean tryToConnect() {
        if (endSocket != null) {
            if (endSocket.getRole() != startSocket.getRole()) {
                if (endSocket.getLogicComponenetMediator() != startSocket.getLogicComponenetMediator()) {
                    registerComponents();
                    connectionPath.buildPath();
                    System.out.println("END: " + endSocket.getRole());
                    System.out.println("Start: " + startSocket.getRole());
                    return true;
                }
            }else{
                System.out.println("NO 1");
            }
        }
        if (endX != null && endY != null) {
            endX.unbind();
            endY.unbind();
        }
        System.out.println("RETURN FALSE");
        return false;
    }
    
    public void buildPath(){
        if(connectionPath != null){
            connectionPath.buildPath();
        }
        else{
            System.out.println("ConnectionPath in " + this + " is null, path can't be built");
        }
    }

    public void primaryRegistration() {
        anchor.setMediator(this);
        startSocket.setMediator(this);
        connectionLine.setMediator(this);
    }

    public void registerComponents() {
        startSocket.setMediator(this);
        endSocket.setMediator(this);
        connectionPath.setMediator(this);
        connectionPath.setPathPoints(pathPoints);
    }   

    public Socket getOppositeSocket(Socket socket) {
        if (socket == startSocket) {
            return endSocket;
        }
        if (socket == endSocket) {
            return startSocket;
        }
        System.out.println("RETURNED NULL");
        return null;
    }

    public void addOnWorkspace(Pane pane) {
        pane.getChildren().add(this.connectionLine);
        pane.getChildren().add(anchor);
    }

    public void removeFromWorkspace(Pane pane) {
        pane.getChildren().remove(connectionLine);
        pane.getChildren().remove(anchor);
        pane.getChildren().removeAll(connectionPath.getPathLinesList());

        connectionLine = null;
        connectionPath = null;

    }

    public ConnectionPath getConnectionPath() {
        return connectionPath;
    }

    public void setConnectionPath(ConnectionPath connectionPath) {
        this.connectionPath = connectionPath;
    }

    public ElementConnector getConnectionLine() {
        return connectionLine;
    }

    public void setLineConnector(ElementConnector connectionLine) {
        this.connectionLine = connectionLine;
    }

    public Socket getStartSocket() {
        return startSocket;
    }

    public void setStartSocket(Socket startSocket) {
        this.startSocket = startSocket;
    }

    public Socket getEndSocket() {
        return endSocket;
    }

    public void setEndSocket(Socket endSocket) {
        this.endSocket = endSocket;
    }

    public ConnectionAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(ConnectionAnchor anchor) {
        this.anchor = anchor;
    }
}
