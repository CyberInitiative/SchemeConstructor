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
public class ConnectionMediator {

//    private ConnectionAnchor firstAnchor;
//    private ConnectionAnchor secondAnchor;
//    private ElementConnector connectionLine;
//    private ConnectionPath path;
//
//    public ConnectionMediator(Circle circle) {
//        DoubleProperty startX = new SimpleDoubleProperty(circle.getCenterX());
//        DoubleProperty startY = new SimpleDoubleProperty(circle.getCenterY());
//        DoubleProperty endX = new SimpleDoubleProperty(circle.getCenterX());
//        DoubleProperty endY = new SimpleDoubleProperty(circle.getCenterY());
//
//        firstAnchor = new ConnectionAnchor(startX, startY, PrimaryPresenter.sockets);
//        secondAnchor = new ConnectionAnchor(endX, endY, PrimaryPresenter.sockets);
//
//        connectionLine = new ElementConnector(startX, startY, endX, endY);
//        setAppearance();
//    }
//
//    private void setAppearance() {
//        firstAnchor.setStroke(Color.BLACK);
//        secondAnchor.setStroke(Color.BLACK);
//        connectionLine.setStrokeWidth(2);
//        connectionLine.setMouseTransparent(true);
//        secondAnchor.toFront();
//    }
//
//    public void registerComponent(ConnectionComponent component) {
//        //component.setMediator(mediator);
//        if (component instanceof ConnectionAnchor && firstAnchor == null) {
//            firstAnchor = (ConnectionAnchor) component;
//        } else if (component instanceof ConnectionAnchor && firstAnchor != null && secondAnchor == null) {
//            secondAnchor = (ConnectionAnchor) component;
//        } else if (component instanceof ElementConnector) {
//            connectionLine = (ElementConnector) component;
//        }
//    }
//
//    public ConnectionAnchor getOppositeAnchor(ConnectionAnchor anchor) {
//        if (anchor == firstAnchor) {
//            return secondAnchor;
//        }
//        if (anchor == secondAnchor) {
//            return firstAnchor;
//        }
//        return null;
//    }
//
//    public void bindComponents(Circle circle) {
//        DoubleProperty startX = new SimpleDoubleProperty(circle.getCenterX());
//        DoubleProperty startY = new SimpleDoubleProperty(circle.getCenterY());
//        DoubleProperty endX = new SimpleDoubleProperty(circle.getCenterX());
//        DoubleProperty endY = new SimpleDoubleProperty(circle.getCenterY());
//        connectionLine.bind(startX, startY, endX, endY);
//        firstAnchor.bind(startX, startY);
//        secondAnchor.bind(endX, endY);
//    }
//
//    public void add(Pane pane) {
//        pane.getChildren().add(firstAnchor);
//        pane.getChildren().add(secondAnchor);
//        pane.getChildren().add(connectionLine);
//    }
//
//    public ConnectionAnchor getFirstAnchor() {
//        return firstAnchor;
//    }
//
//    public void setFirstAnchor(ConnectionAnchor firstAnchor) {
//        this.firstAnchor = firstAnchor;
//    }
//
//    public ConnectionAnchor getSecondAnchor() {
//        return secondAnchor;
//    }
//
//    public void setSecondAnchor(ConnectionAnchor secondAnchor) {
//        this.secondAnchor = secondAnchor;
//    }
//
//    public ElementConnector getConnectionLine() {
//        return connectionLine;
//    }
//
//    public void setConnectionLine(ElementConnector connectionLine) {
//        this.connectionLine = connectionLine;
//    }
//
//    public ConnectionPath getPath() {
//        return path;
//    }
//
//    public void setPath(ConnectionPath path) {
//        this.path = path;
//    }

}
