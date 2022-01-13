package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.connectors;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectorsManagmentState extends State {

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
            targetConnectionAnchor.getConnectionLine().setVisible(true);

        }
        if (event.getTarget().getClass() == Socket.class) {
            Connector connector = new Connector((Socket) event.getTarget());
            pressManipulation(connector.getEndConnectionAnchor(), event.getSceneX(), event.getSceneY());
            connector.getStartConnectionAnchor().notifyObservers();
            connectors.add(connector);
            targetConnectionAnchor = connector.getEndConnectionAnchor();
            targetConnectionAnchor.getConnectionLine().setVisible(true);
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
            System.out.println("RELEASED 2");
            Pane pane = (Pane) event.getSource();
            ConnectionPath path = new ConnectionPath(targetConnectionAnchor, targetConnectionAnchor.getSecondEnd(), pane);
        }

        targetConnectionAnchor = null;
    }

    @Override
    public void setControlls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void changeState(State change, State state) {

    }
}
