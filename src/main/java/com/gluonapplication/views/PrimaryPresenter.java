package com.gluonapplication.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.ConnectorsManagmentState;
import models.ElementManagmentState;
import models.Element;
import models.Grid;
import models.State;

public class PrimaryPresenter {

    @FXML
    private View primary;
    @FXML
    private Button createElementButton = new Button();
    @FXML
    private Button drawConnectionsButton = new Button();
    @FXML
    private Button generateSchemeButton = new Button();
    @FXML
    private Button simulateSignalsToolButton = new Button();
    @FXML
    private Button elementManagerToolButton = new Button();
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane workingSpace = new Pane();
    @FXML
    private GridPane buttonHolder;
    @FXML
    private GridPane elementPicker = new GridPane();
    @FXML
    private ScrollPane pickerHolder = new ScrollPane();
    private Grid grid;
    private ColumnConstraints buttonColumn = new ColumnConstraints(55, 55, 55);
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    public static ObservableList<Element> elements = FXCollections.observableArrayList();
    private State currentState = new ElementManagmentState();

    @FXML
    private Label label = new Label();
    AnimationTimer frameRateMeter = new AnimationTimer() {

        @Override
        public void handle(long now) {
            long oldFrameTime = frameTimes[frameTimeIndex];
            frameTimes[frameTimeIndex] = now;
            frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
            if (frameTimeIndex == 0) {
                arrayFilled = true;
            }
            if (arrayFilled) {
                long elapsedNanos = now - oldFrameTime;
                long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
                label.setText(String.format("Current frame rate: %.3f", frameRate));
            }
        }
    };

    public void initialize() {
        frameRateMeter.start();
        grid = new Grid(workingSpace.getPrefWidth(), workingSpace.getPrefHeight(), 12.5, 12.5, workingSpace);
        grid.drawGrid();
        scrollPane.setContent(workingSpace);
        scrollPane.setId("www");
        //scrollPane.setStyle("-fx-focus-color: transparent;");

        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setManaged(false); //скрыть автоматически появляющийся appBar
                appBar.setVisible(false); // 
            }
        });

        elements.addListener(new ListChangeListener<Element>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Element> c) {
                while (c.next()) {
                    for (Element remitem : c.getRemoved()) {
//                        contollPane.getChildren().remove(remitem);
                    }
                    for (Element additem : c.getAddedSubList()) {
                        workingSpace.getChildren().add(additem);
                        additem.toFront();
                        setPositionForNewElement(additem);
                    }
                }
            }

        });
        if (!elements.isEmpty()) {
            elements.get(0).layoutXProperty().addListener((obj, oldValue, newValue) -> {
                System.out.println("X " + newValue);
            });
        }
        clickEvent();
        pressEvent();
        dragEvent();
        releaseEvent();
    }

    private void setPositionForNewElement(Element element) {
        double screenX = (scrollPane.getViewportBounds().getWidth() / 2) - 50;
        double screenY = (scrollPane.getViewportBounds().getHeight() / 2) - 75;

        element.setLayoutX(screenX);
        element.setLayoutY(screenY);

        Point2D currentCenter = workingSpace.sceneToLocal(element.getLayoutX(), element.getLayoutY());

        element.setLayoutX(currentCenter.getX() + 75);
        element.setLayoutY(currentCenter.getY());
        //System.out.println(element.getChildren().get(0).getLayoutY());
        int gridx = (int) element.getLayoutX() / 24;
        int gridy = (int) element.getLayoutY() / 24;
        element.setLayoutX((25 / 30 + 25 * gridx));
        element.setLayoutY((25 / 30 + 25 * gridy));
        //element.relocate(screenX, screenY);
        //System.out.println(element.getChildren().get(0).getLayoutX());
        //System.out.println(element.getChildren().get(0).getLayoutY());
        //Bounds bounds = element.localToScene(element.getChildren().get(0).getBoundsInLocal());
        //System.out.println(bounds);
    }

    private Point2D getTrueCoordinates(Element node) {
        Point2D coorsOfAnchor = workingSpace.sceneToLocal(node.getCorX(), node.getCorY());
        return coorsOfAnchor;
    }

    private void clickEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.clickEvent(workingSpace);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private void pressEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.pressEvent(workingSpace);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    private void dragEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.dragEvent(workingSpace, scrollPane);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
    }

    private void releaseEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.releaseEvent(workingSpace);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
    }

    @FXML
    public void pressElementConnectorManagmentTool() {
        currentState = new ConnectorsManagmentState();
    }

    @FXML
    public void pressElementManagerTool() {
        currentState = new ElementManagmentState();
    }

    @FXML
    private void addNewElement() {
        Element element = new Element();
        elements.add(element);
    }
}
/*
public void pressEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            Object obj = event.getSource();
            Element element = (Element) obj;
            Element clonedElem = new Element();
//            cloneFromToElement(element, clonedElem);
            elements.add(clonedElem);
            System.out.println("Element added");
            System.out.println(elements.size());
            double nodeMinX = element.getLayoutBounds().getMinX();
            double nodeMinY = element.getLayoutBounds().getMinY();
            Point2D nodeInScene = element.localToScene(nodeMinX, nodeMinY);

            Point2D nodeInMarkerLocal = clonedElem.sceneToLocal(nodeInScene);
            Point2D coorsOfAnchor = workingSpace.sceneToLocal(element.getCorX(), element.getCorY());
            
            Point2D nodeInMarkerParent = clonedElem.localToScene(nodeInMarkerLocal);

            clonedElem.relocate(nodeInMarkerParent.getX(), nodeInMarkerParent.getY() + clonedElem.getLayoutBounds().getMinY());
            clonedElem.setMouseX(event.getSceneX());
            clonedElem.setMouseY(event.getSceneY());
            clonedElem.toFront();

            clonedElem.setCorX(clonedElem.getLayoutX());
            clonedElem.setCorY(clonedElem.getLayoutY());

        };

        andPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        orPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
        notPrototype.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }
 */
