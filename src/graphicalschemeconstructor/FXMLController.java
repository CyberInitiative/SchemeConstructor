package graphicalschemeconstructor;

import graphicalschemeconstructor.classes.Element;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Miroslav Levdikov
 */
public class FXMLController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane workPane;
    @FXML
    private GridPane elementPicker;
    @FXML
    private ScrollPane pickerHolder;
    @FXML
    private ColumnConstraints column;
    @FXML
    private Rectangle rectangle;

    private int elementId = 0;

//    private double corX = 0; //координаты всего элемента
//    private double corY = 0;
//
//    private double mouseX = 0; //координаты мыши
//    private double mouseY = 0;
    private RowConstraints row1 = new RowConstraints(100, 100, 100);
    private RowConstraints row2 = new RowConstraints(100, 100, 100);
    private RowConstraints row3 = new RowConstraints(100, 100, 100);
    private RowConstraints row4 = new RowConstraints(100, 100, 100);
    private RowConstraints row5 = new RowConstraints(100, 100, 100);

    private Element AndPrototype = new Element(null, new Text("&"), 2);
    private Element OrPrototype = new Element(null, new Text("1"), 2);
    private Element NotPrototype = new Element(new Circle(5, Color.WHITE), null, 1);
    private Element AndNotPrototype = new Element(new Circle(5, Color.WHITE), new Text("&"), 2);
    private Element OrNotPrototype = new Element(new Circle(5, Color.WHITE), new Text("1"), 2);

    Element elem = new Element(null, null, 3);

    @FXML
    public void initialize() {
        elementPickerInitializer();
//        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
//            if (event.getSource() instanceof Element) {
//                Object obj = event.getSource();
//                Element element = (Element) obj;
//                Element clonedElem = element.copy(elementId++);
//
//                double nodeMinX = element.getLayoutBounds().getMinX();
//                double nodeMinY = element.getLayoutBounds().getMinY();
//                Point2D nodeInScene = element.localToScene(nodeMinX, nodeMinY);
//                Point2D nodeInMarkerLocal = clonedElem.sceneToLocal(nodeInScene);
//                Point2D nodeInMarkerParent = clonedElem.localToScene(nodeInMarkerLocal);
//
//                clonedElem.relocate(nodeInMarkerParent.getX(),
//                        nodeInMarkerParent.getY()
//                        + clonedElem.getLayoutBounds().getMinY());
//
//                anchorPane.getChildren().add(clonedElem);
//            }
//        };
//        AndPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
//        OrPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
//        NotPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        createElem();
    }

    public void createElem() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            if (event.getSource() instanceof Element) {
                Object obj = event.getSource();
                Element element = (Element) obj;
                Element clonedElem = element.copy(elementId++);

                clonedElem.setId(String.valueOf(elementId));
                System.out.println(clonedElem.getId());

                double nodeMinX = element.getLayoutBounds().getMinX();
                double nodeMinY = element.getLayoutBounds().getMinY();
                Point2D nodeInScene = element.localToScene(nodeMinX, nodeMinY);
                Point2D nodeInMarkerLocal = clonedElem.sceneToLocal(nodeInScene);
                Point2D nodeInMarkerParent = clonedElem.localToScene(nodeInMarkerLocal);

                clonedElem.relocate(nodeInMarkerParent.getX(), nodeInMarkerParent.getY() + clonedElem.getLayoutBounds().getMinY());

                anchorPane.getChildren().add(clonedElem);
                if (clonedElem.getId().equals(String.valueOf(elementId))) {
                    clonedElem.setMouseX(event.getSceneX());
                    clonedElem.setMouseY(event.getSceneY());

                    clonedElem.setCorX(clonedElem.getLayoutX());
                    clonedElem.setCorY(clonedElem.getLayoutY());
                    drag(clonedElem);
                    realeseEvent(clonedElem);
                }
            }
        };

        AndPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        OrPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        NotPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    private void drag(Element element) {
        EventHandler<MouseEvent> eventHandler1 = (MouseEvent event) -> {
            if (element.getId().equals(String.valueOf(elementId))) {
                double offsetX = event.getSceneX() - element.getMouseX(); //смещение по X
                double offsetY = event.getSceneY() - element.getMouseY();

                element.setCorX(element.getCorX() + offsetX);
                element.setCorY(element.getCorY() + offsetY);

                double scaledX = element.getCorX();
                double scaledY = element.getCorY();

                element.setLayoutX(scaledX);
                element.setLayoutY(scaledY);

                element.setMouseX(event.getSceneX());
                element.setMouseY(event.getSceneY());
             
                event.consume();
            }
        };
        AndPrototype.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler1);
    }

    private void realeseEvent(Element element) {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            if (element.getId().equals(String.valueOf(elementId))) {
                if (element.getBoundsInParent().intersects(workPane.getBoundsInParent())) {
                    double nodeMinX = element.getLayoutBounds().getMinX();
                    double nodeMinY = element.getLayoutBounds().getMinY();
                    Point2D elemLocalToScene = element.localToScene(nodeMinX, nodeMinY);
                    Point2D workPaneLocalToParent = workPane.localToParent(nodeMinX, nodeMinY);

                    workPane.getChildren().add(element);
                    element.relocate(elemLocalToScene.getX() - workPaneLocalToParent.getX() + nodeMinX, elemLocalToScene.getY() - workPaneLocalToParent.getY());

                    event.consume();
                }
            }
        };
        AndPrototype.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
    }

    private void elementPickerInitializer() {
        elementPicker.getRowConstraints().add(row1);
        elementPicker.getRowConstraints().add(row2);
        elementPicker.getRowConstraints().add(row3);
        elementPicker.getRowConstraints().add(row4);
        elementPicker.getRowConstraints().add(row5);
        elementPicker.add(AndPrototype, 0, 0);
        elementPicker.add(OrPrototype, 0, 1);
        elementPicker.add(NotPrototype, 0, 2);
        elementPicker.add(AndNotPrototype, 0, 3);
        elementPicker.add(OrNotPrototype, 0, 4);

        elementPicker.setGridLinesVisible(true);
        pickerHolder.setFitToWidth(true);
        for (int i = 0; i < elementPicker.getRowConstraints().size(); i++) {
            elementPicker.setHalignment(elementPicker.getChildren().get(i), HPos.CENTER);
        }
    }

    @FXML
    private void method() {
        if (!workPane.getChildren().isEmpty()) {
            for (int i = 0; i < workPane.getChildren().size(); i++) {
                System.out.println(workPane.getChildren().get(i));
            }
        } else {
            System.out.println("There is nothing on a pane");
        }
    }

}
