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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import models.connector.ConnectionAnchor;
import models.connector.ConnectionPath;
import models.connector.Connector;
import models.observer.IObserver;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.schemeComponents.LogicComponent;
import models.schemeComponents.LogicElement;
import models.schemeComponents.VariableBlock;

/**
 *
 * @author Miroslav Levdikov
 */
public final class ComponentsManager {

    public ComponentsManager(Pane workspace) {
        this.workspace = workspace;
        setListenerForElements();
        setListenerForConnectors();
        addListenerForTool1();
    }
    private Pane workspace;

    BooleanProperty tool1 = new SimpleBooleanProperty(false);

    private final ObservableList<LogicElement> elements = FXCollections.observableArrayList();
    private final ObservableList<VariableBlock> blocks = FXCollections.observableArrayList();
    private final ObservableList<Connector> connectors = FXCollections.observableArrayList();
    private final List<IObserver> sockets = new ArrayList<>();

    private PathPointsManager pointManager = new PathPointsManager();

    private void setListenerForElements() { //Отслеживает добавление/удаление новых элементов и добавляет/удаляет их на рабочую поверхность;
        elements.addListener(new ListChangeListener<LogicElement>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends LogicElement> c) {
                while (c.next()) {
                    for (LogicElement remitem : c.getRemoved()) {
                        if (remitem != null) {
                            remitem.removeFromWorkspace(workspace);
                            remitem = null;
                        }
                    }
                    for (LogicElement additem : c.getAddedSubList()) {
                        additem.addOnWorkspace(workspace, tool1.get());
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
        component.getConnectionSockets().addListener(new ListChangeListener<Socket>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Socket> c) {
                while (c.next()) {
                    for (Socket remitem : c.getRemoved()) {
                        if (remitem != null) {
                            sockets.remove(remitem);
                            workspace.getChildren().remove(remitem);
                            remitem = null;
                        }
                    }
                    for (Socket additem : c.getAddedSubList()) {
                        sockets.add(additem);
                        workspace.getChildren().add(additem);
                    }
                }
            }
        });
    }

    private void setListenerForLines(LogicComponent component) {
        component.getConnectionLines().addListener(new ListChangeListener<ElementLine>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ElementLine> c) {
                while (c.next()) {
                    for (ElementLine remitem : c.getRemoved()) {
                        if (remitem != null) {
                            workspace.getChildren().remove(remitem);
                            remitem = null;
                        }
                    }
                    for (ElementLine additem : c.getAddedSubList()) {
                        workspace.getChildren().add(additem);
                    }
                }
            }
        });
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

    public void addListenerForTool1() {
        tool1.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    for (LogicElement element : elements) {
                        for (Socket socket : element.getConnectionSockets()) {
                            socket.setVisible(false);
                        }
                    }
                } else if (newValue == false) {
                    for (LogicElement element : elements) {
                        for (Socket socket : element.getConnectionSockets()) {
                            socket.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    public void addLogicElement() {
        LogicElement logEl = new LogicElement(pointManager.getAllPoints());
        logEl.setMediator();
        elements.add(logEl);
    }

    public void removeLogicElement(LogicElement element) {
        elements.remove(element);
    }

    public void addConnector(Connector connector) {
        connectors.add(connector);
        connector.getStartConnectionAnchor().setSocketObservers(sockets);
        connector.getEndConnectionAnchor().setSocketObservers(sockets);
    }

    public void removeConnector(Connector connector) {
        connectors.remove(connector);
    }

    public Pane getWorkspace() {
        return workspace;
    }

    public void setTool1(boolean value) {
        tool1.setValue(value);
    }

    public PathPointsManager getPointManager() {
        return pointManager;
    }

    public void setPointManager(PathPointsManager pointManager) {
        this.pointManager = pointManager;
    }
}
