package models.connector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public final class Connector {

    private ConnectionAnchor startConnectionAnchor;
    private ConnectionAnchor endConnectionAnchor;

    private ElementConnector connectionLine;

    private boolean isStartConnected = false;
    private boolean isEndConnected = false;

    private ConnectionPath connectionPath = new ConnectionPath();

    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty endX;
    private DoubleProperty endY;

    public Connector(Circle circle) {
        startX = new SimpleDoubleProperty(circle.getCenterX());
        startY = new SimpleDoubleProperty(circle.getCenterY());
        endX = new SimpleDoubleProperty(circle.getCenterX());
        endY = new SimpleDoubleProperty(circle.getCenterY());

        startConnectionAnchor = new ConnectionAnchor(startX, startY); //rework
        endConnectionAnchor = new ConnectionAnchor(endX, endY); //rework
        connectionLine = new ElementConnector(startX, startY, endX, endY);

        endConnectionAnchor.toFront();
        registerComponents();
    }

    public Connector(Circle firstCircle, Circle secondCircle) {
        startX = new SimpleDoubleProperty(firstCircle.getCenterX());
        startY = new SimpleDoubleProperty(firstCircle.getCenterY());
        endX = new SimpleDoubleProperty(secondCircle.getCenterX());
        endY = new SimpleDoubleProperty(secondCircle.getCenterY());

        startConnectionAnchor = new ConnectionAnchor(startX, startY); //rework
        endConnectionAnchor = new ConnectionAnchor(endX, endY); //rework
        connectionLine = new ElementConnector(startX, startY, endX, endY);

        endConnectionAnchor.toFront();
    }

    public void registerComponents() {
        startConnectionAnchor.setMediator(this);
        endConnectionAnchor.setMediator(this);
        connectionLine.setMediator(this);
        connectionPath.setMediator(this);
    }

    public void registerPath(ConnectionPath path) {
        path.setMediator(this);
        setConnectionPath(path);
    }

    public ConnectionAnchor getOppositeAnchor(ConnectionAnchor anchor) {
        if (anchor == startConnectionAnchor) {
            return endConnectionAnchor;
        }
        if (anchor == endConnectionAnchor) {
            return startConnectionAnchor;
        }
        return null;
    }

    public void addOnWorkspace(Pane pane) {
        pane.getChildren().add(this.connectionLine);
        pane.getChildren().add(this.startConnectionAnchor);
        pane.getChildren().add(this.endConnectionAnchor);
    }

    public void removeFromWorkspace(Pane pane) {
        pane.getChildren().remove(connectionLine);
        pane.getChildren().remove(startConnectionAnchor);
        pane.getChildren().remove(endConnectionAnchor);
        pane.getChildren().removeAll(connectionPath.getPathLinesList());

        if (startConnectionAnchor != null) {
            if (startConnectionAnchor.getConnectedSocket() != null) {
                startConnectionAnchor.getConnectedSocket().setMainConnectedAnchor(null);
            }
            startConnectionAnchor = null;
        }
        if (endConnectionAnchor != null) {
            if (endConnectionAnchor.getConnectedSocket() != null) {
                endConnectionAnchor.getConnectedSocket().setMainConnectedAnchor(null);
            }
            endConnectionAnchor = null;
        }
        connectionLine = null;
        connectionPath = null;

    }

    @Override
    public String toString() {
        return "Connector{" + "startConnectionAnchor=" + startConnectionAnchor + ", endConnectionAnchor=" + endConnectionAnchor + ", connectionLine=" + connectionLine + ", isStartConnected=" + isStartConnected + ", isEndConnected=" + isEndConnected + ", connectionPath=" + connectionPath + ", startX=" + startX + ", startY=" + startY + ", endX=" + endX + ", endY=" + endY + '}';
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

    public boolean isIsStartConnected() {
        return isStartConnected;
    }

    public void setIsStartConnected(boolean isStartConnected) {
        this.isStartConnected = isStartConnected;
    }

    public boolean isIsEndConnected() {
        return isEndConnected;
    }

    public void setIsEndConnected(boolean isEndConnected) {
        this.isEndConnected = isEndConnected;
    }

    public ConnectionAnchor getStartConnectionAnchor() {
        return startConnectionAnchor;
    }

    public void setStartConnectionAnchor(ConnectionAnchor startConnectionAnchor) {
        this.startConnectionAnchor = startConnectionAnchor;
    }

    public ConnectionAnchor getEndConnectionAnchor() {
        return endConnectionAnchor;
    }

    public void setEndConnectionAnchor(ConnectionAnchor endConnectionAnchor) {
        this.endConnectionAnchor = endConnectionAnchor;
    }

}
