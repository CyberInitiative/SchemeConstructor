package models.tools;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.connector.ConnectionAnchor;
import models.connector.Connector;
import models.partsOfComponents.Socket;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionManagingTool extends Tool<AnchorPane> {

    public ConnectionManagingTool() {
        super();
    }

    private ConnectionAnchor targetConnectionAnchor = null;
    private Socket currentSocket = null;

    @Override
    public void mouseClicked(MouseEvent event) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getTarget().getClass() == Socket.class) {
            currentSocket = (Socket) event.getTarget();
            currentSocket.setFill(Color.rgb(138, 149, 151));
            currentSocket.setStroke(Color.BLACK);
            currentSocket.getStrokeDashArray().removeAll(2d);

            if (currentSocket.getRole() == Socket.Role.Output) {
                connectorsSetUp(event);
            } else if (currentSocket.getRole() == Socket.Role.Input) {
                if (currentSocket.getConnectorMediator() == null) {
                    connectorsSetUp(event);
                } else if (currentSocket.getConnectorMediator() != null) {
                    if (currentSocket.getRole() == Socket.Role.Input) {
                        currentSocket.setFill(Color.WHITE);
                        currentSocket.getStrokeDashArray().addAll(2d);
                    }
                    targetConnectionAnchor = currentSocket.getConnectorMediator().getAnchor();
                    targetConnectionAnchor.requestConnectionLine().setVisible(true);
                    targetConnectionAnchor.setVisible(true);
                    targetConnectionAnchor.toFront();
                }
            }
        }
        System.out.println("PRESS 2");
    }

    private void connectorsSetUp(MouseEvent event) {
        Connector connector = context.getCompManager().addConnector((Socket) event.getTarget());
        pressManipulation(connector.getAnchor(), event);

        targetConnectionAnchor = connector.getAnchor();
        targetConnectionAnchor.requestConnectionLine().setVisible(true);
        currentSocket.toFront();
    }

    private void pressManipulation(ConnectionAnchor anchor, MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
        corX = anchor.getCenterX();
        corY = anchor.getCenterY();
        anchor.toFront();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (targetConnectionAnchor != null) {
            setOnMousePosition(targetConnectionAnchor, event.getSceneX(), event.getSceneY());
        }
    }

    private void setOnMousePosition(ConnectionAnchor anchor, double eventSceneX, double eventSceneY) {
        anchor.centerXProperty().unbind();
        anchor.centerYProperty().unbind();
        anchor.setCenterX(eventSceneX - (mouseX - corX));
        anchor.setCenterY(eventSceneY - (mouseY - corY));
        int gridx = (int) anchor.getCenterX() / 10;
        int gridy = (int) anchor.getCenterY() / 10;
        anchor.setCenterX((10 * gridx));
        anchor.setCenterY((10 * gridy));
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (currentSocket != null) {
            targetConnectionAnchor.notifyObservers();
            var op = currentSocket.getConnectorMediator().getOppositeSocket(currentSocket);
            if (targetConnectionAnchor.getMediator().tryToConnect() == false) {
                if (op != null) {
                    if (op.getConnectorsList().isEmpty()) {
                        op.setFill(Color.WHITE);
                        op.getStrokeDashArray().addAll(2d);
                    }
                }
                if (currentSocket.getConnectorsList().isEmpty()) {
                    currentSocket.setFill(Color.WHITE);
                    currentSocket.getStrokeDashArray().addAll(2d);
                }
                System.out.println(op.getConnectorsList().size());
                currentSocket.getConnectorsList().remove(currentSocket.getConnectorsList().get(currentSocket.getConnectorsList().size() - 1));
                op.getConnectorsList().remove(op.getConnectorsList().size()-1);
                context.getCompManager().removeConnector(targetConnectionAnchor.getMediator());
                targetConnectionAnchor = null;
                currentSocket = null;
                return;
            }
            if (op != null) {
                op.setFill(Color.rgb(138, 149, 151));
                op.setStroke(Color.BLACK);
                op.getStrokeDashArray().removeAll(2d);
                op.toFront();
            }
            currentSocket.setFill(Color.rgb(138, 149, 151));
            currentSocket.setStroke(Color.BLACK);
            currentSocket.getStrokeDashArray().removeAll(2d);
            currentSocket.toFront();

            targetConnectionAnchor.requestConnectionLine().setVisible(false);
            targetConnectionAnchor.setVisible(false);
            targetConnectionAnchor = null;
            currentSocket = null;

            System.out.println("RELEASED 2");
        }
    }

    @Override
    protected void determineY(double tY, MouseEvent event, AnchorPane t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void determineX(double tX, MouseEvent event, AnchorPane t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void ensureVisible(ScrollPane scrollPan, AnchorPane t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void defineStartPointOfInteraction(double corX, double corY) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
