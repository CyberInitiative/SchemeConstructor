package com.gluonapplication.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.tools.ConnectionManagingTool;
import models.tools.ComponentsManagingTool;
import models.shemeGenerator.Facade;
import models.Grid;
import models.GridSystem;
import models.WorkspaceManager;
import models.tools.Tool;
import models.schemeComponents.VariableBlock;
import models.tools.SelectionTool;

public class Controller {

    /*
    DrawingController - автоматный объект;
     */
    @FXML
    private View primary;

    @FXML
    private Button componentsManagingToolButton = new Button();
    @FXML
    private Button selectionToolButton = new Button();
    @FXML
    private Button connectionManagingToolButton = new Button();
    @FXML
    private Button createElementButton = new Button();
    @FXML
    private Button generateSchemeButton = new Button();
    @FXML
    private Button simulateSignalsToolButton = new Button();
    @FXML
    private Button test = new Button();

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane workspace = new Pane();
    @FXML
    private GridPane buttonHolder;
    @FXML
    private GridPane elementPicker = new GridPane();
    @FXML
    private ScrollPane pickerHolder = new ScrollPane();

    private Grid grid = new Grid();
    private ColumnConstraints buttonColumn = new ColumnConstraints(55, 55, 55);
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    private final ConnectionManagingTool connectionManagingTool = new ConnectionManagingTool();
    private final ComponentsManagingTool componentsManagingTool = new ComponentsManagingTool();
    private final SelectionTool selectionTool = new SelectionTool();
    private Tool currentTool;
    
    private Facade facade = new Facade();

    private WorkspaceManager compManager;

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
        connectionManagingToolPressed();
        hideAppBar();
        grid.drawGrid(10, 10, workspace);
        compManager = new WorkspaceManager(workspace, scrollPane);
        connectionManagingTool.setContext(this);
        componentsManagingTool.setContext(this);
        selectionTool.setContext(this);
        currentTool = componentsManagingTool;

//        System.out.println("SIZE: " + pointManager.getAllPoints().length);
//        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
//            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
//                System.out.println("P: " + pointManager.getAllPoints()[i][j]);
//            }
//        }

        //pointManager.setOpenedList(pointManager.getAllPathPoints());        
        frameRateMeter.start();

        scrollPane.setContent(workspace);
        scrollPane.setId("www");

        addClickEventFilter();
        addPressEventFilter();
        addDragEventFilter();
        mouseReleased();
        componentsManagingToolButton.fire();
        //blocks.addAll(facade.getGenerator().getBlocks());
        //facade.getGenerator().setConnectionPaths();
        //connectors.addAll(facade.getGenerator().getConnectors());
        //System.out.println("1ARRAY: " + facade.getArr());
        //System.out.println("SOCKETs: " + sockets);
        //System.out.println("ELEMENTS: " + elements);
    }

    private void hideAppBar() {
        primary.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setManaged(false); //скрыть автоматически появляющийся appBar
                appBar.setVisible(false); // 
            }
        });
    }

    private void addClickEventFilter() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentTool.mouseClicked(event);
        };
        workspace.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private void addPressEventFilter() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentTool.mousePressed(event);
        };
        workspace.addEventFilter(MouseEvent.MOUSE_PRESSED, eventHandler);
    }

    private void addDragEventFilter() {
        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
            currentTool.mouseDragged(event);
        };
        workspace.addEventFilter(MouseEvent.MOUSE_DRAGGED, eventHandler);
    }

    private void mouseReleased() {
        workspace.setOnMouseReleased((event) -> {
            currentTool.mouseReleased(event);
        });      
    }
        
        
//        EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
//            currentTool.releaseProcessing(event);
//        };
//        workspace.addEventFilter(MouseEvent.MOUSE_RELEASED, eventHandler);
//    }

    private void connectionManagingToolPressed() {
        connectionManagingToolButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setCurrentToolComponentsManagingTool();
            }
        });
    }

    @FXML
    private void setCurrentToolConnectionManagingTool() {
        currentTool = connectionManagingTool;
        compManager.setTool1(false);
    }

    @FXML
    private void setCurrentToolSelectionTool() {
        currentTool = selectionTool;
    }

    @FXML
    private void setCurrentToolComponentsManagingTool() {
        currentTool = componentsManagingTool;
        //compManager.setTool1(true);
    }

    @FXML
    private void addNewElement() {
        System.out.println("ADDED");
        compManager.addLogicElement();
    }

    @FXML
    private void addNewVariableBlock() {
        compManager.addVariableBlock();
    }

    @FXML
    private void test1() {
        System.out.println("TEST 1");
    }

    @FXML
    private void test2() {
        System.out.println("TEST 2");
    }

    public Pane getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Pane workspace) {
        this.workspace = workspace;
    }

    public WorkspaceManager getCompManager() {
        return compManager;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
