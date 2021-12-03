package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
//import static com.gluonapplication.views.PrimaryPresenter.scrollPane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElementManagmentState extends State {

    MyThread thread = new MyThread();

    @Override
    public void clickEvent(Pane target) {

    }

    @Override
    public void pressEvent(Pane target) {
        target.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            for (Element element : elements) {
                for (int i = 0; i < element.getChildren().size(); i++) {
                    if (event.getTarget() == element.getChildren().get(i)) {
                        element.setDragging(true);
                        element.setMouseX(event.getSceneX());
                        element.setMouseY(event.getSceneY());
                        element.setCorX(element.getTranslateX());
                        element.setCorY(element.getTranslateY());
                        Bounds bound = element.getConnectionInputCircle().get(1).localToScene(element.getConnectionInputCircle().get(1).getBoundsInLocal());
                        System.out.println(bound);
                        Circle circle1 = new Circle(6.25);
                        Circle circle2 = new Circle(10);
                        target.getChildren().add(circle1);
                        target.getChildren().add(circle2);
                        circle1.relocate(bound.getMinX() - 50 - 6.25, bound.getMinY() - 0.25);
                        System.out.println("W " + element.getConnectionInputCircle().get(1).getCenterX());
                        //circle2.relocate(bound.getMinY(), bound.getMinY());
                        int gridx = (int) element.getConnectionInputCircle().get(1).getTranslateX() / 12;
                        int gridy = (int) element.getConnectionInputCircle().get(1).getTranslateY() / 12;
                        //element.getConnectionInputCircle().get(1).setTranslateX((12.5 * gridx));
                        //element.getConnectionInputCircle().get(1).setTranslateY((12.5 * gridy));
                        element.toFront();
                        circle1.toFront();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void dragEvent(Node target, ScrollPane scroll) {
        //thread.run();
        target.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
            for (Element element : elements) {
                if (element.isDragging() == true) {
                    element.setTranslateX(event.getSceneX() - (element.getMouseX() - element.getCorX()));
                    element.setTranslateY(event.getSceneY() - (element.getMouseY() - element.getCorY()));
                    int gridx = (int) element.getTranslateX() / 12;
                    int gridy = (int) element.getTranslateY() / 12;
                    element.setTranslateX((12.5 * gridx));
                    element.setTranslateY((12.5 * gridy));
                    break;
                }
            }

        });
    }

    @Override
    public void releaseEvent(Node target) {
        EventHandler eventhandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                for (Element element : elements) {
                    if (element.isDragging() == true) {
                        element.setDragging(false);
                        //System.out.println("REALESED");
                        Bounds bound = element.localToScene(element.getBoundsInLocal());
//                        Bounds bounds = element.localToScene(element.getChildren().get(0).getBoundsInLocal());
//                        System.out.println(bound);
//                        System.out.println(element.getLayoutX());
//                        System.out.println(element.getLayoutY());
//                        element.getInputsInElement().get(0).getTranslateX();
//                        System.out.println("CIRX " + element.getInputsInElement().get(0).getTranslateX());
//                        System.out.println("CIRY " + element.getInputsInElement().get(0).getTranslateY());
//                        System.out.println("XTR " + element.getTranslateX());
//                        System.out.println("XTY " + element.getTranslateY());
//                        System.out.println("CIRX " + element.getInputsInElement().get(0).localToParentTransformProperty().getValue().getTx());
//                        System.out.println("CIRY " + element.getInputsInElement().get(0).localToParentTransformProperty().getValue().getTy());
//                        System.out.println(element.localToParentTransformProperty().getValue().getTx());
//                        System.out.println(element.localToParentTransformProperty().getValue().getTy());
                        target.removeEventFilter(MouseEvent.MOUSE_RELEASED, this);
                        break;
                    }
                }
            }
        };
        target.addEventFilter(MouseEvent.MOUSE_RELEASED, eventhandler);
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
}
