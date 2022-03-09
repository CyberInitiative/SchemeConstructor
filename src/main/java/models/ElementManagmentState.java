package models;

import com.gluonapplication.views.PrimaryPresenter;
//import static com.gluonapplication.views.PrimaryPresenter.elements;
//import static com.gluonapplication.views.PrimaryPresenter.pointManager;
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

    public ElementManagmentState() {
        super();
    }

    public ElementManagmentState(PrimaryPresenter context) {
        super(context);
    }

    private MenuItem itemE1 = new MenuItem("Add Input");
    private MenuItem itemE2 = new MenuItem("Remove Input");
    private MenuItem itemE3 = new MenuItem("Delete");

    private ContextMenu contextMenuElement = new ContextMenu(itemE1, itemE2, itemE3);

    private ContextMenu contextMenuVariable = new ContextMenu();

    private MenuItem itemV1 = new MenuItem("Input mode");
    private MenuItem itemV2 = new MenuItem("Output mode");

    private SchemeComponent currentComponent = null;

    public void clickProcessing(MouseEvent event) {
        if (event.getTarget().getClass() == Element.class) {
            //Element elem = (Element) event.getTarget();
            //System.out.println("GET SIG: " + elem.getConnectionOutputSocket().getSignal().getVariable());

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
        if (event.getTarget() instanceof SchemeComponent) {
            currentComponent = (SchemeComponent) event.getTarget();
            //currentComponent.prepareToChangePosition(context); // CHANGE
            //currentComponent.prepareToChangePosition(source);
            //Pane pane = (Pane) event.getSource();
            //pressProcessingSocketsSetUp(currentComponent, pane);
            currentComponent.setMouseX(event.getSceneX());
            currentComponent.setMouseY(event.getSceneY());
            currentComponent.setCorX(currentComponent.getLayoutX());
            currentComponent.setCorY(currentComponent.getLayoutY());
            currentComponent.setVisualComponentsToFront();
        }
    }

    private void pressProcessingSocketsSetUp(SchemeComponent comp, Pane pane) {
        pane.getChildren().remove(comp.getConnectionOutputSocket().getMainConnectedAnchor().requestConnectionPath().getPathPolyline());
//        for (Socket socket : sockets) {
//            if (socket.getMainConnectedAnchor() != null) {
//                if (socket.getMainConnectedAnchor().requestConnectionPath() != null) {
//                    var pathLine = socket.getMainConnectedAnchor().requestConnectionPath().getPathPolyline();
//                    if (pathLine != null) {
//                        source.getChildren().remove(pathLine);
//                    }
//                }
//                socket.getMainConnectedAnchor().requestConnectionLine().setVisible(true);
//            }
//        }
    }

    @Override
    public void dragProcessing(MouseEvent event) {
        if (currentComponent != null) {

            currentComponent.setLayoutX(event.getSceneX() - (currentComponent.getMouseX() - currentComponent.getCorX()));
            currentComponent.setLayoutY(event.getSceneY() - (currentComponent.getMouseY() - currentComponent.getCorY()));
            int gridx = (int) currentComponent.getLayoutX() / 10;
            int gridy = (int) currentComponent.getLayoutY() / 10;

            currentComponent.setLayoutX((10 * gridx));
            currentComponent.setLayoutY((10 * gridy));
            currentComponent.getSymbol().toFront();

        }
    }

    @Override
    public void releaseProcessing(MouseEvent event) {
        if (currentComponent != null) {
            SchemeComponent schemeComponent = (SchemeComponent) event.getTarget();
            schemeComponent.notifyObservers();
            //releaseProcessingSocketsSetUp(movableInstance.getInputSockets());
            //releaseProcessingSocketsSetUp(movableInstance.getOutputSockets());
            currentComponent = null;
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
                    //socket.getMainConnectedAnchor().requestConnectionPath().buildPath(ancA, ancB, source); //CHANGE
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
