package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import javafx.event.ActionEvent;
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
            itemE2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    elem.deleteInput();
                }
            });

            Point2D point = elem.localToScreen(event.getSceneX(), event.getSceneY());
            Point2D scre = elem.screenToLocal(point.getX(), point.getY());
            double f = point.getX() - scre.getX();
            double s = point.getY() - scre.getY();
            contextMenuElement.show(elem, f, s);

        }
    }

    @Override
    public void pressProcessing(MouseEvent event) {
        if (event.getTarget() instanceof Movable) {
            Shape shape = (Shape) event.getTarget();
            Movable mov = (Movable) event.getTarget();
            mov.setMouseX(event.getSceneX());
            mov.setMouseY(event.getSceneY());
            mov.setCorX(shape.getLayoutX());
            mov.setCorY(shape.getLayoutY());
            mov.setToFront();
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
        }
        System.out.println("RELEASED 1");
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
