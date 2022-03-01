package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElementManagmentState extends State {

    MenuItem itemE1 = new MenuItem("Add Input");
    MenuItem itemE2 = new MenuItem("Remove Input");
    MenuItem itemE3 = new MenuItem("Delete");

    ContextMenu contextMenuElement = new ContextMenu(itemE1, itemE2, itemE3);

    ContextMenu contextMenuVariable = new ContextMenu();

    MenuItem itemV1 = new MenuItem("Input mode");
    MenuItem itemV2 = new MenuItem("Output mode");

    @Override
    public void clickProcessing(MouseEvent event) {
        if (event.getTarget().getClass() == Element.class) {
            Element elem = (Element) event.getTarget();
            System.out.println("GET SIG: " + elem.getConnectionOutputSocket().getSignal().getVariable());

        }
//        if (event.getTarget().getClass() == Element.class) {
//            Element elem = (Element) event.getTarget();
//            itemE2.setOnAction(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent e) {
//                    elem.deleteInput();
//                }
//            });
//
//            Point2D point = elem.localToScreen(event.getSceneX(), event.getSceneY());
//            Point2D scre = elem.screenToLocal(point.getX(), point.getY());
//            double f = point.getX() - scre.getX();
//            double s = point.getY() - scre.getY();
//            contextMenuElement.show(elem, f, s);
//
//        }
    }

    @Override
    public void pressProcessing(MouseEvent event) {
        if (event.getTarget() instanceof Movable) {
            Shape shape = (Shape) event.getTarget();
            Movable movableInstance = (Movable) event.getTarget();
            Pane pane = (Pane) event.getSource();

            pressProcessingSocketsSetUp(movableInstance.getInputSockets(), pane);
            pressProcessingSocketsSetUp(movableInstance.getOutputSockets(), pane);

            movableInstance.setMouseX(event.getSceneX());
            movableInstance.setMouseY(event.getSceneY());
            movableInstance.setCorX(shape.getLayoutX());
            movableInstance.setCorY(shape.getLayoutY());
            movableInstance.setToFront();
        }
    }

    private void pressProcessingSocketsSetUp(ArrayList<Socket> sockets, Pane pane) {
        for (Socket socket : sockets) {
            if (socket.getMainConnectedAnchor() != null) {
                if (socket.getMainConnectedAnchor().requestConnectionPath() != null) {
                    var pathLine = socket.getMainConnectedAnchor().requestConnectionPath().getPathPolyline();
                    if (pathLine != null) {
                        pane.getChildren().remove(pathLine);
                    }
                }
                socket.getMainConnectedAnchor().requestConnectionLine().setVisible(true);
            }
        }
    }

    @Override
    public void dragProcessing(MouseEvent event) {
        if (event.getTarget() instanceof Movable) {
            Shape shape = (Shape) event.getTarget();
            Movable mov = (Movable) event.getTarget();

            shape.setLayoutX(event.getSceneX() - (mov.getMouseX() - mov.getCorX()));
            shape.setLayoutY(event.getSceneY() - (mov.getMouseY() - mov.getCorY()));
            int gridx = (int) shape.getLayoutX() / 10;
            int gridy = (int) shape.getLayoutY() / 10;

            shape.setLayoutX((10 * gridx));
            shape.setLayoutY((10 * gridy));
            mov.getSymbol().toFront();

        }
    }

    @Override
    public void releaseProcessing(MouseEvent event) {
        if (event.getTarget() instanceof ObservableInterface & event.getTarget() instanceof Movable) {
            ObservableInterface observable = (ObservableInterface) event.getTarget();
            observable.notifyObservers();
            Movable movableInstance = (Movable) event.getTarget();

            releaseProcessingSocketsSetUp(movableInstance.getInputSockets());
            releaseProcessingSocketsSetUp(movableInstance.getOutputSockets());
        }

        System.out.println("RELEASED 1");
    }

    private void releaseProcessingSocketsSetUp(ArrayList<Socket> sockets) {
        for (Socket socket : sockets) {
            if (socket.getMainConnectedAnchor() != null) {
                socket.getMainConnectedAnchor().requestConnectionLine().setVisible(false);
                if (socket.getMainConnectedAnchor().requestConnectionPath() != null) {
                    var ancA = socket.getMainConnectedAnchor().requestConnectionPath().getMediator().getStartConnectionAnchor();
                    var ancB = socket.getMainConnectedAnchor().requestConnectionPath().getMediator().getEndConnectionAnchor();
                    socket.getMainConnectedAnchor().requestConnectionPath().buildPath(ancA, ancB);
                }
            }
        }
    }

    @Override
    public void setControlls() {

    }

//    Thread taskThread = new Thread(new Runnable() {
//        
//        private String myParam;
//
//        public Runnable init(String myParam) {
//            this.myParam = myParam;
//            return this;
//        }
//        
//      @Override
//      public void run() {
//          System.out.println(this.myParam);
//      }
//    });
    @Override
    public void changeState(State change, State state) {
        change = state;
    }
}
