package models.Tools;

import models.Tools.Tool;
import com.gluonapplication.views.PrimaryPresenter;
import static com.gluonapplication.views.PrimaryPresenter.connectors;
import static com.gluonapplication.views.PrimaryPresenter.sockets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.ConnectionAnchor;
import models.ConnectionPath;
import models.Connector;
import models.Socket;

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

        }
        if (event.getTarget().getClass() == Socket.class) {
            Socket socket = (Socket) event.getTarget();
            //System.out.println("SIGNAL: " + socket.getSignal());
            Connector connector = new Connector((Socket) event.getTarget());
            connector.registerComponents();
            pressManipulation(connector.getEndConnectionAnchor(), event.getSceneX(), event.getSceneY());
            connector.getStartConnectionAnchor().notifyObservers();
            connectors.add(connector);
            targetConnectionAnchor = connector.getEndConnectionAnchor();
            targetConnectionAnchor.requestConnectionLine().setVisible(true);
            System.out.println("PRESS 2");
        }
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
            /*
            Необходимо узнать, соединен ли данный Socket с помощью других коннекторов, чтобы исходя из этого
            выбрать нужный конструктор для создания маршрута. 
             */
            if (targetConnectionAnchor.getConnectedSocket() != null && targetConnectionAnchor.getConnectedSocket().getMainConnectedAnchor() != targetConnectionAnchor) {
                targetConnectionAnchor.requestConnectionLine().setVisible(false);
                var point = targetConnectionAnchor.requestSecondAnchor(targetConnectionAnchor);
                var clP = targetConnectionAnchor.getConnectedSocket().getMainConnectedAnchor().getMediator().getConnectionPath().getClosestPoint(point.getCenterX(), point.getCenterY());
                ConnectionPath path = new ConnectionPath(point, clP, context.getWorkingSpace()); //CHANGE
            } else {
                targetConnectionAnchor.getMediator().getConnectionPath().buildPath(targetConnectionAnchor, targetConnectionAnchor.requestSecondAnchor(targetConnectionAnchor), context.getWorkingSpace()); //CHANGE
            }
            System.out.println("RELEASED 2");
            //targetConnectionAnchor.notifyObservers();
        }
        targetConnectionAnchor = null;
    }
}
