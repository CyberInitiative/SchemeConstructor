package models.tools;

import java.util.ArrayList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import models.partsOfComponents.IComponentsPiece;
import models.schemeComponents.LogicComponent;
import models.partsOfComponents.Socket;

/**
 *
 * @author Miroslav Levdikov
 */
public class ComponentsMover extends Tool {

    /*
    Класс ComponentsMover реализует интрумент, который предназначен для перемещения компонентов на экране;
     */
    public ComponentsMover() {
        super();
    }

    private MenuItem itemE1 = new MenuItem("Add Input");
    private MenuItem itemE2 = new MenuItem("Remove Input");
    private MenuItem itemE3 = new MenuItem("Delete");

    private ContextMenu contextMenuElement = new ContextMenu(itemE1, itemE2, itemE3);

    private ContextMenu contextMenuVariable = new ContextMenu();

    private MenuItem itemV1 = new MenuItem("Input mode");
    private MenuItem itemV2 = new MenuItem("Output mode");

    private LogicComponent currentComponent = null;

    public void clickProcessing(MouseEvent event) {
//        if (event.getTarget().getClass() == Element.class) {
//            //Element elem = (Element) event.getTarget();
//            //System.out.println("GET SIG: " + elem.getConnectionOutputSocket().getSignal().getVariable());
//
//        }
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
        if (event.getTarget() instanceof IComponentsPiece) {
            IComponentsPiece partOfComponent = (IComponentsPiece) event.getTarget();
            currentComponent = partOfComponent.getMediator();

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            currentComponent.setCorX(currentComponent.getBody().getLayoutX());
            currentComponent.setCorY(currentComponent.getBody().getLayoutY());
            pressProcessingSocketsSetUp();
            currentComponent.setVisualComponentsToFront();
        }
    }

    private void pressProcessingSocketsSetUp() {
        for (Socket socket : currentComponent.getConnectionSockets()) {
            if (socket.getMainConnectedAnchor() != null) {
                if (socket.getMainConnectedAnchor().requestConnectionPath() != null) {
                    var pathLinesList = socket.getMainConnectedAnchor().requestConnectionPath().getPathLinesList();
                    if (pathLinesList != null && !pathLinesList.isEmpty()) {
                        pathLinesList.clear();
                    }
                }
                socket.getMainConnectedAnchor().requestConnectionLine().setVisible(true);
            }
        }
    }

    @Override
    public void dragProcessing(MouseEvent event) {
        if (currentComponent != null) {
            currentComponent.getBody().setLayoutX(event.getSceneX() - (mouseX - currentComponent.getCorX()));
            currentComponent.getBody().setLayoutY(event.getSceneY() - (mouseY - currentComponent.getCorY()));
            int gridx = (int) currentComponent.getBody().getLayoutX() / 10;
            int gridy = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutX((10 * gridx));
            currentComponent.getBody().setLayoutY((10 * gridy));
            currentComponent.getSymbol().toFront();
        }
    }

    @Override
    public void releaseProcessing(MouseEvent event) {
        if (currentComponent != null) {
            currentComponent.notifyObservers();
            releaseProcessingSocketsSetUp();
            currentComponent = null;
        }
        System.out.println("RELEASED 1");
    }

    private void releaseProcessingSocketsSetUp() {
        for (Socket socket : currentComponent.getConnectionSockets()) {
            if (socket.getMainConnectedAnchor() != null) {
                socket.getMainConnectedAnchor().requestConnectionLine().setVisible(false);
                if (socket.getMainConnectedAnchor().requestConnectionPath() != null) {
                    socket.getMainConnectedAnchor().requestConnectionPath().buildPath();
                }
            }
        }
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
}
