package models.tools;

import javafx.scene.input.MouseEvent;
import models.connector.ConnectionAnchor;
import models.connector.Connector;
import models.partsOfComponents.Socket;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionDrawer extends Tool {

    public ConnectionDrawer() {
        super();
    }

    private ConnectionAnchor targetConnectionAnchor = null;

    @Override
    public void clickProcessing(MouseEvent event) {

    }

    @Override
    public void pressProcessing(MouseEvent event) {
        if (event.getTarget().getClass() == ConnectionAnchor.class) {
            ConnectionAnchor t1 = (ConnectionAnchor) (event.getTarget());
            pressManipulation(t1, event.getSceneX(), event.getSceneY());
            targetConnectionAnchor = t1;
            targetConnectionAnchor.requestConnectionLine().setVisible(true);
            var pathList = targetConnectionAnchor.getMediator().getConnectionPath().getPathLinesList();
            if (pathList != null && !pathList.isEmpty()) {
                pathList.clear();
            }

        }
        if (event.getTarget().getClass() == Socket.class) {
            Connector connector = new Connector((Socket) event.getTarget());
            context.getCompManager().addConnector(connector);
            connector.registerComponents();
            pressManipulation(connector.getEndConnectionAnchor(), event.getSceneX(), event.getSceneY());
            connector.getStartConnectionAnchor().notifyObservers();
            targetConnectionAnchor = connector.getEndConnectionAnchor();
            targetConnectionAnchor.requestConnectionLine().setVisible(true);
        }
        System.out.println("PRESS 2");
    }

    private void pressManipulation(ConnectionAnchor anchor, double sceneX, double sceneY) {
        anchor.setMouseX(sceneX);
        anchor.setMouseY(sceneY);
        anchor.setCorX(anchor.getCenterX());
        anchor.setCorY(anchor.getCenterY());
        anchor.toFront();
    }

    @Override
    public void dragProcessing(MouseEvent event) {
        if (targetConnectionAnchor != null) {
            dragManipulation(targetConnectionAnchor, event.getSceneX(), event.getSceneY());
        }
    }

    private void dragManipulation(ConnectionAnchor anchor, double eventSceneX, double eventSceneY) {
        anchor.centerXProperty().unbind();
        anchor.centerYProperty().unbind();
        anchor.setCenterX(eventSceneX - (anchor.getMouseX() - anchor.getCorX()));
        anchor.setCenterY(eventSceneY - (anchor.getMouseY() - anchor.getCorY()));
        int gridx = (int) anchor.getCenterX() / 10;
        int gridy = (int) anchor.getCenterY() / 10;
        anchor.setCenterX((10 * gridx));
        anchor.setCenterY((10 * gridy));
    }

    @Override
    public void releaseProcessing(MouseEvent event) {
        if (event.getTarget().getClass() == Socket.class || event.getTarget().getClass() == ConnectionAnchor.class) {
            targetConnectionAnchor.notifyObservers();
            targetConnectionAnchor.requestConnectionLine().setVisible(false);
            if (!targetConnectionAnchor.getMediator().getConnectionPath().buildPath()) {
                context.getCompManager().removeConnector(targetConnectionAnchor.getMediator());
            }
        }
        targetConnectionAnchor = null;
        System.out.println("RELEASED 2");
    }

}
