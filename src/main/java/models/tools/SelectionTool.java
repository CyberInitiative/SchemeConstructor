package models.tools;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import models.connector.Connector;
import models.partsOfComponents.IComponentsPiece;
import models.partsOfComponents.Socket;
import models.schemeComponents.LogicComponent;
import selector.SelectionArea;
import selector.SelectorPoint;

/**
 *
 * @author Miroslav Levdikov
 */
public class SelectionTool extends Tool<SelectorPoint> {

    SelectorPoint drag = null;
    SelectionArea area = null;
    ArrayList<LogicComponent> selected = new ArrayList<>();
    Point2D realCoordinates;
    LogicComponent currentComponent = null;
    boolean readyToDrag = false;
    boolean inDragProcess = false;
    boolean wasDraggin = false;

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();

        realCoordinates = context.getWorkspace().sceneToLocal(mouseX, mouseY);
        if (event.getTarget() instanceof IComponentsPiece) {
            if (!selected.isEmpty()) {
                IComponentsPiece partOfComponent = (IComponentsPiece) event.getTarget();
                currentComponent = partOfComponent.getLogicComponenetMediator();
             
                for (LogicComponent logicComponent : selected) {
                    if (logicComponent != currentComponent) {
                        Point2D pointX = new Point2D(currentComponent.getBody().getLayoutX(), logicComponent.getBody().getLayoutY());
                        Point2D pointY = new Point2D(logicComponent.getBody().getLayoutX(), currentComponent.getBody().getLayoutY());

                        double width = Math.sqrt(Math.pow((pointX.getX() - logicComponent.getBody().getLayoutX()), 2) + Math.pow((pointX.getY() - logicComponent.getBody().getLayoutY()), 2));
                        double height = Math.sqrt(Math.pow((pointY.getX() - logicComponent.getBody().getLayoutX()), 2) + Math.pow((pointY.getY() - logicComponent.getBody().getLayoutY()), 2));

                        if (currentComponent.getBody().getLayoutX() > logicComponent.getBody().getLayoutX()) {
                            logicComponent.getBody().layoutXProperty().bind(currentComponent.getBody().layoutXProperty().subtract(width));
                        } else if (currentComponent.getBody().getLayoutX() < logicComponent.getBody().getLayoutX()) {
                            logicComponent.getBody().layoutXProperty().bind(currentComponent.getBody().layoutXProperty().add(width));
                        } else {
                            logicComponent.getBody().layoutXProperty().bind(currentComponent.getBody().layoutXProperty());
                        }

                        if (currentComponent.getBody().getLayoutY() > logicComponent.getBody().getLayoutY()) {
                            logicComponent.getBody().layoutYProperty().bind(currentComponent.getBody().layoutYProperty().subtract(height));
                        } else if (currentComponent.getBody().getLayoutY() < logicComponent.getBody().getLayoutY()) {
                            logicComponent.getBody().layoutYProperty().bind(currentComponent.getBody().layoutYProperty().add(height));
                        } else {
                            logicComponent.getBody().layoutYProperty().bind(currentComponent.getBody().layoutYProperty());
                        }

                    }
                }
                readyToDrag = true;
            }
        } else {
            if (!selected.isEmpty()) {
                for (LogicComponent logicComponent : selected) {
                    logicComponent.getBody().setEffect(null);
                }
                selected.clear();
            }
            if (currentComponent != null) {
                currentComponent = null;
            }
            area = new SelectionArea(realCoordinates.getX(), realCoordinates.getY());
            drag = area.getDragingPoint();

            drag.setCorX(drag.getCenterX());
            drag.setCorY(drag.getCenterY());

            drag.setCenterX(realCoordinates.getX());
            drag.setCenterY(realCoordinates.getY());
            drag.toFront();

            context.getCompManager().getWorkspace().getChildren().add(area.getStartPoint());
            context.getCompManager().getWorkspace().getChildren().add(area.getDragingPoint());
            context.getCompManager().getWorkspace().getChildren().add(area.getxDependetPoint());
            context.getCompManager().getWorkspace().getChildren().add(area.getyDependetPoint());
        }
    }

    private void pressProcessingSocketsSetUp(LogicComponent comp) {
        for (Socket socket : comp.getConnectionSockets()) {
            if (!socket.getConnectorsList().isEmpty()) {
                for (Connector connector : socket.getConnectorsList()) {
                    connector.getConnectionLine().setVisible(true);
                    var pathLinesList = connector.getConnectionPath().getPathLinesList();
                    if (pathLinesList != null && !pathLinesList.isEmpty()) {
                        pathLinesList.clear();
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (currentComponent != null) {
            //currentComponent.getBody().setLayoutX(event.getSceneX() - (mouseX - currentComponent.getCorX()));
            //currentComponent.getBody().setLayoutY(event.getSceneY() - (mouseY - currentComponent.getCorY()));
            int gridx = (int) currentComponent.getBody().getLayoutX() / 10;
            int gridy = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutX((10 * gridx));
            currentComponent.getBody().setLayoutY((10 * gridy));

            if (readyToDrag == true && inDragProcess == false) {
                inDragProcess = true;
                for (LogicComponent logicComponent : selected) {
                    pressProcessingSocketsSetUp(logicComponent);
                }
            }

            currentComponent.getSymbol().toFront();
        }
        if (area != null) {
            if (area.getAreaSelector() != null) {
                context.getCompManager().getWorkspace().getChildren().remove(area.getAreaSelector());
            }
        }
        if (drag != null) {
            drag.setCenterX(event.getSceneX() - (mouseX - drag.getCorX()));
            drag.setCenterY(event.getSceneY() - (mouseY - drag.getCorY()));
            area.calculateParams();

            context.getCompManager().getWorkspace().getChildren().add(area.getAreaSelector());
            drag.toFront();
        }
    }

    private void releaseProcessingSocketsSetUp(LogicComponent comp) {
        for (Socket socket : comp.getConnectionSockets()) {
            if (!socket.getConnectorsList().isEmpty()) {
                for (Connector connector : socket.getConnectorsList()) {
                    connector.buildPath();
                    connector.getConnectionLine().setVisible(false);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (currentComponent != null) {
            currentComponent = null;
            for (LogicComponent logicComponent : selected) {
                logicComponent.notifyObservers();
                releaseProcessingSocketsSetUp(logicComponent);
                logicComponent.getBody().layoutXProperty().unbind();
                logicComponent.getBody().layoutYProperty().unbind();
            }
            readyToDrag = false;
            inDragProcess = false;
        } else {
            if (area != null) {
                drag = null;
                context.getCompManager().getWorkspace().getChildren().remove(area.getAreaSelector());
                context.getCompManager().getWorkspace().getChildren().remove(area.getStartPoint());
                context.getCompManager().getWorkspace().getChildren().remove(area.getDragingPoint());
                context.getCompManager().getWorkspace().getChildren().remove(area.getxDependetPoint());
                context.getCompManager().getWorkspace().getChildren().remove(area.getyDependetPoint());
                selected.addAll(context.getCompManager().checkIfInSelectedArea(area.getAreaSelector()));
                //System.out.println("SEL: " + selected);
                area = null;
            }
        }
    }

    @Override
    protected void determineY(double tY, MouseEvent event, SelectorPoint t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void determineX(double tX, MouseEvent event, SelectorPoint t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void ensureVisible(ScrollPane scrollPan, SelectorPoint t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void defineStartPointOfInteraction(double corX, double corY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
}
