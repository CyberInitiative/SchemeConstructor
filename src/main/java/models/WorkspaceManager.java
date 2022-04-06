package models;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.connector.Connector;
import models.observer.IObserver;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.schemeComponents.LogicComponent;
import models.schemeComponents.LogicElement;
import models.schemeComponents.VariableBlock;
import selector.SelectionArea;

/**
 *
 * @author Miroslav Levdikov
 */
public final class WorkspaceManager {

    public WorkspaceManager(Pane space, ScrollPane scroll) {
        workspace = space;
        scrollPane = scroll;
//        setListenerForLogicComponents();
        setListenerForConnectors();
//        addListenerForComponentsManagingTool();
        gridSystem = new GridSystem(space);
        //System.out.println("PATH: " + gridSystem.getAllPoints().length);
    }
    private Pane workspace;
    private ScrollPane scrollPane;

    private BooleanProperty componentsManagintToolActivated = new SimpleBooleanProperty(false);

    private LogicComponent currentComponent;
    private Connector currentConnector;
    private SelectionArea selectArea;

    private final ObservableList<LogicComponent> logicComponents = FXCollections.observableArrayList();
    private final ObservableList<Connector> connectors = FXCollections.observableArrayList();
    private final List<IObserver> sockets = new ArrayList<>();

    private GridSystem gridSystem;

    private double componentCorX;
    private double componentCorY;

    private double mouseCorX;
    private double mouseCorY;

    private void setListenerForLogicComponents() { //Отслеживает добавление/удаление новых элементов и добавляет/удаляет их на рабочую поверхность;
        logicComponents.addListener(new ListChangeListener<LogicComponent>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends LogicComponent> c) {
                while (c.next()) {
                    for (LogicComponent remitem : c.getRemoved()) {
                        if (remitem != null) {
                            //remitem.remGraphlElemsFromWorkspace(workspace);
                            remitem = null;
                        }
                    }
                    for (LogicComponent additem : c.getAddedSubList()) {
                        //additem.addGraphElemsOnWorkspace(workspace, componentsManagintToolActivated.get());
                        sockets.addAll(additem.getConnectionSockets());
                        //Отдельные слушатели для линий и сокетов нужны для операций добавления/удаления входов элемента;
                        setListenerForSockets(additem);
                        setListenerForLines(additem);
                    }
                }
            }
        });
    }

    private void setListenerForSockets(LogicComponent component) {
//        component.getConnectionSockets().addListener(new ListChangeListener<Socket>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends Socket> c) {
//                while (c.next()) {
//                    for (Socket remitem : c.getRemoved()) {
//                        if (remitem != null) {
//                            sockets.remove(remitem);
//                            workspace.getChildren().remove(remitem);
//                            remitem = null;
//                        }
//                    }
//                    for (Socket additem : c.getAddedSubList()) {
//                        sockets.add(additem);
//                        workspace.getChildren().add(additem);
//                    }
//                }
//            }
//        });
    }

    private void setListenerForLines(LogicComponent component) {
//        component.getConnectionLines().addListener(new ListChangeListener<ElementLine>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends ElementLine> c) {
//                while (c.next()) {
//                    for (ElementLine remitem : c.getRemoved()) {
//                        if (remitem != null) {
//                            workspace.getChildren().remove(remitem);
//                            remitem = null;
//                        }
//                    }
//                    for (ElementLine additem : c.getAddedSubList()) {
//                        workspace.getChildren().add(additem);
//                    }
//                }
//            }
//        });
    }

    private void setListenerForConnectors() {
        connectors.addListener(new ListChangeListener<Connector>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Connector> c) {
                while (c.next()) {
                    for (Connector remitem : c.getRemoved()) {
                        if (remitem != null) {
                            remitem.removeFromWorkspace(workspace);
                            remitem = null;
                        }
                    }
                    for (Connector additem : c.getAddedSubList()) {
                        additem.addOnWorkspace(workspace);
                        setListenerForVisualLines(additem);
                    }
                }
            }
        });
    }

    private void setListenerForVisualLines(Connector connector) {
        connector.getConnectionPath().getPathLinesList().addListener(new ListChangeListener<Line>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Line> c) {
                while (c.next()) {
                    for (Line remitem : c.getRemoved()) {
                        if (remitem != null) {
                            workspace.getChildren().remove(remitem);
                        }
                    }
                    for (Line additem : c.getAddedSubList()) {
                        workspace.getChildren().add(additem);
                        additem.toBack();
                    }
                }
            }
        });
    }

    public void addListenerForComponentsManagingTool() {
        componentsManagintToolActivated.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    for (LogicComponent element : logicComponents) {
                        for (Socket socket : element.getConnectionSockets()) {
                            socket.setVisible(false);
                        }
                    }
                } else if (newValue == false) {
                    for (LogicComponent element : logicComponents) {
                        for (Socket socket : element.getConnectionSockets()) {
                            socket.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    public void addLogicElement() { //{z1}
        LogicElement logEl = new LogicElement(gridSystem.getAllPoints());
        logEl.setMediator();
        workspace.getChildren().addAll(logEl.getComponentsNodes());
        sockets.addAll(logEl.getConnectionSockets());
        logicComponents.add(logEl);
    }

    public void removeLogicElement(LogicElement element) { //{z2}
        logicComponents.remove(element);
        workspace.getChildren().removeAll(element.getComponentsNodes());
    }

    public void redrawComponent(LogicComponent component) { //{z3}
        workspace.getChildren().removeAll(component.getComponentsNodes());
        workspace.getChildren().addAll(component.getComponentsNodes());
    }

    public void addVariableBlock() { //{z4}
        VariableBlock varBlock = new VariableBlock(gridSystem.getAllPoints());
        varBlock.setMediator();
        logicComponents.add(varBlock);
    }

    public void removeVariableBlock(VariableBlock block) { //{z5}
        logicComponents.remove(block);
    }

    public Connector addConnector(Socket target) { //{z6}
        Connector connector = new Connector(target, gridSystem.getAllPoints(), sockets);
        connectors.add(connector);
        connector.primaryRegistration();
        return connector;
    }

    public void removeConnector(Connector connector) { //{z7}
        connectors.remove(connector);
    }

    public void rebuildPathes() { //{z8}
        for (Connector connector : connectors) {
            connector.buildPath();
        }
    }

    public void fixPointOfInteraction(double screenX, double screenY) { //{z9}
        componentCorX = currentComponent.getBody().getLayoutX();
        componentCorY = currentComponent.getBody().getLayoutY();
        
        mouseCorX = screenX;
        mouseCorY = screenY;
    }

    public ArrayList<LogicComponent> checkIfInSelectedArea(Rectangle area) { //{z10}
        ArrayList<LogicComponent> selected = new ArrayList<>();
        if (area != null) {
            for (LogicComponent comp : logicComponents) {
                if (area.getBoundsInParent().contains(comp.getBody().getBoundsInParent())) {
                    selected.add(comp);
                    visualizeSelectedtComponent(comp);
                }
            }
        }
        return selected;
    }
    
    public void visualizeSelectedtComponent(LogicComponent component){ //{z11}     
        int depth = 30;
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.rgb(31, 117, 254));
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        component.getBody().setEffect(borderGlow);
    }
    
    public void visualizeUnselection(LogicComponent component){ //{z12}
        component.getBody().setEffect(null);
    }
    
    public void handleDraggingProcess(){
        
    }
    
    public LogicComponent getCurrentComponent() {
        return currentComponent;
    }

    public void setCurrentComponent(LogicComponent currentComponent) {
        this.currentComponent = currentComponent;
    }

    public Connector getCurrentConnector() {
        return currentConnector;
    }

    public void setCurrentConnector(Connector currentConnector) {
        this.currentConnector = currentConnector;
    }
    
    public Pane getWorkspace() {
        return workspace;
    }

    public void setTool1(boolean value) {
        componentsManagintToolActivated.setValue(value);
    }
}
