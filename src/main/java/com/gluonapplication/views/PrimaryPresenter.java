package com.gluonapplication.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import models.Connector;
import models.ConnectorsManagmentState;
import models.ElementManagmentState;
import models.Element;
import models.Facade;
import models.Grid;
import models.ObserverInterface;
import models.PathPoint;
import models.PathPointsManager;
import models.Socket;
import models.State;
import models.VariableBlock;

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
    public static ObservableList<VariableBlock> blocks = FXCollections.observableArrayList();
    public static ObservableList<Connector> connectors = FXCollections.observableArrayList();
    public static PathPointsManager pointManager = new PathPointsManager();
    public static List<ObserverInterface> sockets = new ArrayList<>();

    private final ConnectorsManagmentState connState = new ConnectorsManagmentState();
    private final ElementManagmentState elemState = new ElementManagmentState();
    private State currentState = elemState;
    private Facade facade = new Facade();

    @FXML
    Button addElement;

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
        pointManager.generatePoints(workingSpace.getPrefWidth(), workingSpace.getPrefHeight(), 10, 10);
        //pointManager.setOpenedList(pointManager.getAllPathPoints());        
        Image img = new Image("plusEl.png");
        ImageView view = new ImageView(img);

        addElement.setGraphic(view);
        frameRateMeter.start();
        grid = new Grid(workingSpace.getPrefWidth(), workingSpace.getPrefHeight(), 10, 10, workingSpace);
        grid.drawGrid();

//        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
//            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
//                for(Element elem : elements){
//                    if(elem.getBoundsInParent().contains(pointManager.getAllPoints()[i][j])){
//                        pointManager.getAllPoints()[i][j].setStatus(PathPoint.PathPointStatus.Obstructuion);
//                        //System.out.println("OBSTR");
//                    }
//                }
//                //System.out.println(pointManager.getAllPoints()[i][j]);
//            }
//        }
        //workingSpace.getChildren().addAll(pointManager.getAllPoints());
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
                        //System.out.println(elements.size());
                        workingSpace.getChildren().add(additem);
//                        workingSpace.getChildren().add(additem.getBody());
                        workingSpace.getChildren().add(additem.getOutputLine());
                        workingSpace.getChildren().add(additem.getSymbol());
                        workingSpace.getChildren().addAll(additem.getInputLines());
                        workingSpace.getChildren().addAll(additem.getConnectionInputSockets());
                        workingSpace.getChildren().add(additem.getConnectionOutputSocket());
                        additem.getInputLines().addListener(new ListChangeListener<Line>() {
                            @Override
                            public void onChanged(ListChangeListener.Change<? extends Line> c) {
                                while (c.next()) {
                                    for (Line remitem : c.getRemoved()) {
                                        workingSpace.getChildren().remove(remitem);
                                    }
                                    for (Line additem : c.getAddedSubList()) {
                                        workingSpace.getChildren().add(additem);
                                    }
                                }
                            }

                        });
                        additem.getConnectionInputSockets().addListener(new ListChangeListener<Socket>() {
                            @Override
                            public void onChanged(ListChangeListener.Change<? extends Socket> c) {
                                while (c.next()) {
                                    for (Socket remitem : c.getRemoved()) {
                                        workingSpace.getChildren().remove(remitem);
                                    }
                                    for (Socket additem : c.getAddedSubList()) {
                                        if (additem.getSignal() != null) {
                                            workingSpace.getChildren().add(additem);
                                        }
                                    }
                                }
                            }
                        });
                        additem.notifyObservers();
                    }
                }
            }
        });

        blocks.addListener(new ListChangeListener<VariableBlock>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends VariableBlock> c) {
                while (c.next()) {
                    for (VariableBlock remitem : c.getRemoved()) {
                        workingSpace.getChildren().remove(remitem);
                    }
                    for (VariableBlock additem : c.getAddedSubList()) {
                        workingSpace.getChildren().add(additem);
                        if (additem.getSymbol() != null) {
                            workingSpace.getChildren().add(additem.getSymbol());
                        }
                        if (additem.getInputLine() != null && additem.getConnectionInputSocket() != null) {
                            workingSpace.getChildren().add(additem.getInputLine());
                            workingSpace.getChildren().add(additem.getConnectionInputSocket());
                        }
                        if (additem.getOutputLine() != null && additem.getConnectionOutputSocket() != null) {
                            workingSpace.getChildren().add(additem.getOutputLine());
                            workingSpace.getChildren().add(additem.getConnectionOutputSocket());
                        }
                        additem.notifyObservers();
                    }
                }
            }

        });

        connectors.addListener(new ListChangeListener<Connector>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Connector> c) {
                while (c.next()) {
                    for (Connector remitem : c.getRemoved()) {
//                        contollPane.getChildren().remove(remitem);
                    }
                    for (Connector additem : c.getAddedSubList()) {

                        additem.add(workingSpace);
//                        if (additem.getConnectionPath() != null) {
//                            workingSpace.getChildren().add(additem.getConnectionPath().getPathPolyline());
//                        }

                    }
                }
            }

        });

        facade.getGenerator().setPane(workingSpace);
        facade.buildTheScheme(workingSpace);

        clickEvent();
        pressEvent();
        dragEvent();
        releaseEvent();
        //elements.addAll(facade.getArr()); //удалить
        blocks.addAll(facade.getGenerator().getBlocks());
        facade.getGenerator().setConnectionPaths();
        connectors.addAll(facade.getGenerator().getConnectors());
        //System.out.println("1ARRAY: " + facade.getArr());
        //System.out.println("SOCKETs: " + sockets);
        System.out.println("ELEMENTS: " + elements);
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
        int gridx = (int) element.getLayoutX() / 10;
        int gridy = (int) element.getLayoutY() / 10;
        element.setLayoutX((10 * gridx));
        element.setLayoutY((10 * gridy));
        //element.relocate(screenX, screenY);
        //System.out.println(element.getChildren().get(0).getLayoutX());
        //System.out.println(element.getChildren().get(0).getLayoutY());
        //Bounds bounds = element.localToScene(element.getChildren().get(0).getBoundsInLocal());
        //System.out.println(bounds);
    }

    @FXML
    private void clearAll() {
        workingSpace.getChildren().clear();
    }

    private Point2D getTrueCoordinates(Element node) {
        Point2D coorsOfAnchor = workingSpace.sceneToLocal(node.getCorX(), node.getCorY());
        return coorsOfAnchor;
    }

    private void clickEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.clickProcessing(event);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private void pressEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.pressProcessing(event);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    private void dragEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.dragProcessing(event);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
    }

    private void releaseEvent() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentState.releaseProcessing(event);
        };
        workingSpace.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
    }

    @FXML
    public void pressElementConnectorManagmentTool() {
        //currentState = new ConnectorsManagmentState(); 
        currentState = connState;
    }

    @FXML
    public void pressElementManagerTool() {
        currentState = elemState;
    }

    @FXML
    private void addNewElement() {
        Element element = new Element(pointManager.getAllPoints());
        element.setOwnerForAllSockets();
        elements.add(element);
    }

    @FXML
    private void addNewVariableBlock() {
        VariableBlock block = new VariableBlock(pointManager.getAllPoints());
        block.setOwnerForSocket(block.getConnectionInputSocket());
        blocks.add(block);
    }

    @FXML
    private void getSocketInfo() {
//        for (Element elem : elements) {
//            System.out.println(elem.getConnectionInputSockets());
//            System.out.println(elem.getConnectionOutputSocket());
//            System.out.println("@@@@@@");
//        }
        elements.get(0).deleteInput();
        System.out.println("DELETED");
    }

    @FXML
    private void add() {
        elements.get(0).addInput();
        System.out.println("ADDED");
    }
}
