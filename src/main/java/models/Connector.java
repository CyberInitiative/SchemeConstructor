package models;

import com.gluonapplication.views.PrimaryPresenter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class Connector {

    private ConnectionAnchor startConnectionAnchor;
    private ConnectionAnchor endConnectionAnchor;
    private Line lineConnector = new Line();

    private boolean isStartConnected = false;
    private boolean isEndConnected = false;

    ConnectionPath connectionPath = null;

    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty endX;
    private DoubleProperty endY;

    private DoubleProperty centerX;
    private DoubleProperty centerY;

    //double one; //= (startConnector.getCenterX() + endConnector.getCenterX()) / 2;
    //double two;// = (startConnector.getCenterY() + endConnector.getCenterY()) / 2;
    //int gridx; //= (int) one / 12;
    //int gridy;// = (int) two / 12;
    public Connector(Circle circle) {
        startX = new SimpleDoubleProperty(circle.getCenterX());
        startY = new SimpleDoubleProperty(circle.getCenterY());
        endX = new SimpleDoubleProperty(circle.getCenterX());
        endY = new SimpleDoubleProperty(circle.getCenterY());

        startConnectionAnchor = new ConnectionAnchor(startX, startY, PrimaryPresenter.sockets);
        endConnectionAnchor = new ConnectionAnchor(endX, endY, PrimaryPresenter.sockets);

        startConnectionAnchor.setSecondEnd(endConnectionAnchor);
        endConnectionAnchor.setSecondEnd(startConnectionAnchor);

        lineConnector = new ElementConnector(startX, startY, endX, endY);

        //startConnectionAnchor.centerXProperty().bind(circle.centerXProperty());
        //startConnectionAnchor.centerYProperty().bind(circle.centerYProperty());
        startConnectionAnchor.setConnectionLine(lineConnector);
        endConnectionAnchor.setConnectionLine(lineConnector);

        setAppearance();
        //listenerForCenter();
    }

    private void setAppearance() {
        startConnectionAnchor.setStroke(Color.BLACK);
        endConnectionAnchor.setStroke(Color.BLACK);
        lineConnector.setStrokeWidth(2);
        lineConnector.setMouseTransparent(true);
        endConnectionAnchor.toFront();
    }

    public void add(Pane pane) {
        pane.getChildren().add(lineConnector);
        pane.getChildren().add(startConnectionAnchor);
        pane.getChildren().add(endConnectionAnchor);
    }

    public ConnectionPath getConnectionPath() {
        return connectionPath;
    }

    public void setConnectionPath(ConnectionPath connectionPath) {
        this.connectionPath = connectionPath;
    }

    public Line getLineConnector() {
        return lineConnector;
    }

    public void setLineConnector(Line lineConnector) {
        this.lineConnector = lineConnector;
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
