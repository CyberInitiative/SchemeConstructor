package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectorsManagmentState extends State {

    @Override
    public void clickEvent(Pane target) {
        target.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            elements.forEach(element -> {
                for (int i = 0; i < element.getChildren().size(); i++) {
                    if (event.getTarget() == element.getChildren().get(i) && element.getChildren().get(i) instanceof Circle) {
                        Circle circle = (Circle) element.getChildren().get(i);
                        if (circle.getRadius() == 7) {                           
                            Bounds boundsInScene = element.getChildren().get(i).localToScene(element.getChildren().get(i).getBoundsInParent());
                            System.out.println(boundsInScene.getCenterX());
                            System.out.println(boundsInScene.getCenterY());
                            DoubleProperty startX = new SimpleDoubleProperty(element.getChildren().get(i).localToParentTransformProperty().getValue().getTx());
                            DoubleProperty startY = new SimpleDoubleProperty(element.getChildren().get(i).localToParentTransformProperty().getValue().getTy());
                            DoubleProperty endX = new SimpleDoubleProperty(circle.getCenterX());
                            DoubleProperty endY = new SimpleDoubleProperty(circle.getLayoutX());
                            System.out.println(element.getLayoutX());
                            System.out.println(element.getLayoutY());
                            ConnectorAnchor start = new ConnectorAnchor(startX, startY);
                            start.relocate(element.getChildren().get(i).localToParentTransformProperty().getValue().getTx(), element.getChildren().get(i).localToParentTransformProperty().getValue().getTy());
                            target.getChildren().add(start);
                            return;
                        }
                    }
                }
            });
        });
    }

    @Override
    public void pressEvent(Pane target) {
//        target.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
//            elements.forEach(element -> {
//                for (int i = 0; i < element.getChildren().size(); i++) {
//                    if (event.getTarget() == element.getChildren().get(i) && element.getChildren().get(i) instanceof Circle) {
//                        Circle circle = (Circle) element.getChildren().get(i);
//                        if (circle.getRadius() == 7) {
//                            DoubleProperty startX = new SimpleDoubleProperty(circle.getLayoutX());
//                            DoubleProperty startY = new SimpleDoubleProperty(circle.getLayoutY());
//                            DoubleProperty endX = new SimpleDoubleProperty(circle.getCenterX());
//                            DoubleProperty endY = new SimpleDoubleProperty(circle.getLayoutX());
//                            
//                            ConnectorAnchor start = new ConnectorAnchor(startX, startY);
//                            ConnectorAnchor end = new ConnectorAnchor(endX, endY);
//                            Line line = new ElementConnector(startX, startY, endX, endY);
//                            target.getChildren().add(start);
//                            start.toFront();
//                            System.out.println("st " + start.centerXProperty());
//                        }
//                    }
//                }
//            });
//        });
    }

    @Override
    public void dragEvent(Node target, ScrollPane scroll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void releaseEvent(Node target) {
        EventHandler eventhandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                elements.forEach(element -> {
                    for (int i = 0; i < element.getChildren().size(); i++) {
                        if (event.getTarget() == element.getChildren().get(i) && element.getChildren().get(i) instanceof Circle) {
                            Circle circle = (Circle) element.getChildren().get(i);
                            if (circle.getRadius() == 7) {
                                System.out.println("RELEASED 2");
                                target.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                                return;
                            }
                        } else {
                            target.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                        }
                    }
                });
            }
        };
        target.addEventFilter(MouseEvent.MOUSE_RELEASED, eventhandler);

    }

    @Override
    public void setControlls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
/*
@Override
    public void pressEvent(Node target) {
        target.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            elements.forEach(element -> {
                for (int i = 0; i < element.getChildren().size(); i++) {
                    if (event.getTarget() == element.getChildren().get(i) && element.getChildren().get(i) instanceof Circle) {
                        Circle circle = (Circle) element.getChildren().get(i);
                        if (circle.getRadius() == 7) {
                            System.out.println("PRESS 2");
                        }
                    }
                }
            });
        });
    }

    @Override
    public void dragEvent(Node target, ScrollPane scroll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void releaseEvent(Node target) {
        EventHandler eventhandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                elements.forEach(element -> {
                    for (int i = 0; i < element.getChildren().size(); i++) {
                        if (event.getTarget() == element.getChildren().get(i) && element.getChildren().get(i) instanceof Circle) {
                            Circle circle = (Circle) element.getChildren().get(i);
                            if (circle.getRadius() == 7) {
                                System.out.println("RELEASED 2");
                                target.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                                return;
                            }
                        } else {
                            target.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                        }
                    }
                });
            }
        };
        target.addEventFilter(MouseEvent.MOUSE_RELEASED, eventhandler);

    }

    @Override
    public void setControlls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/