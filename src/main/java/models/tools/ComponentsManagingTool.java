package models.tools;

import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Pair;
import models.connector.Connector;
import models.partsOfComponents.IComponentsPiece;
import models.schemeComponents.LogicComponent;
import models.partsOfComponents.Socket;
import models.schemeComponents.LogicElement;

/**
 *
 * @author Miroslav Levdikov
 */
public class ComponentsManagingTool extends Tool<LogicComponent> {

    private boolean readyToDrag = false;
    private boolean inDragingProcess = false;
    private boolean wasDraggin = false;

    /*
    Класс ComponentsMover реализует интрумент, который предназначен для перемещения компонентов на экране;
     */
    public ComponentsManagingTool() {
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
    private LogicComponent previousComponent = null;

    private long timeOfClick = 0;
    private int clicks = 0;

    public void mouseClicked(MouseEvent event) {
        if (checkIfDoubleClick(event)) {
            if (currentComponent.getClass() == LogicElement.class) {
                LogicElement logEl = (LogicElement) currentComponent;
                itemE1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        logEl.addSocket();
                        currentComponent.notifyObservers();
                        context.getCompManager().rebuildPathes();
                        context.getCompManager().redrawComponent(currentComponent);
                        currentComponent = null;
                    }
                });
                itemE2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        logEl.removeSocket();
                        currentComponent.notifyObservers();
                        context.getCompManager().rebuildPathes();
                        currentComponent = null;
                    }
                });
                itemE3.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        context.getCompManager().removeLogicElement(logEl);
                        currentComponent = null;
                    }
                });

                Point2D point = currentComponent.getBody().localToScreen(event.getSceneX(), event.getSceneY());
                Point2D scre = currentComponent.getBody().screenToLocal(point.getX(), point.getY());
                double f = point.getX() - scre.getX();
                double s = point.getY() - scre.getY();
                contextMenuElement.show(currentComponent.getBody(), f, s);
                timeOfClick = 0;
                clicks = 0;
            }
        } else {
            currentComponent = null;
        }
    }

    private boolean checkIfDoubleClick(MouseEvent event) {
        if (event.getTarget() instanceof IComponentsPiece) {
            IComponentsPiece partOfComponent = (IComponentsPiece) event.getTarget();
            currentComponent = partOfComponent.getLogicComponenetMediator();
            if (wasDraggin) {
                timeOfClick = 0;
                clicks = 0;
                wasDraggin = false;
                return false;
            }
            if (timeOfClick == 0 && clicks == 0) {
                timeOfClick = System.currentTimeMillis();
                clicks++;
            } else if (clicks == 1) {
                long diff = System.currentTimeMillis() - timeOfClick;
                if (diff < 500 && diff > 130) {
                    return true;
                } else if (diff > 500) {
                    timeOfClick = System.currentTimeMillis();
                    clicks = 1;
                }
            } else {
                timeOfClick = 0;
                clicks = 0;
            }
        }
        return false;
    }

    @Override
    protected void ensureVisible(ScrollPane scrollPane, LogicComponent comp) {
        var dragNode = comp.getBody();
        final Bounds viewport = scrollPane.getViewportBounds();
        double vpMinY = viewport.getMinY() * -1;
        double vpHeight = viewport.getHeight();
        double vpMinX = viewport.getMinX() * -1;
        double vpWidth = viewport.getWidth();
        final Bounds contentBounds = scrollPane.getContent().getLayoutBounds();
        final double layoutY = comp.getBody().getLayoutY();
        final double layoutX = comp.getBody().getLayoutX();

        final double visibleYInViewport = vpMinY + (vpHeight - dragNode.getHeight());
        double totalHeightToScroll = contentBounds.getHeight() - vpHeight;
        if (layoutY > visibleYInViewport) {
            double heightToScroll = vpMinY + (layoutY - visibleYInViewport);
            scrollPane.setVvalue(heightToScroll / totalHeightToScroll);
        } else if (layoutY < vpMinY) {
            double heightToScroll = layoutY;
            scrollPane.setVvalue(heightToScroll / totalHeightToScroll);
        }

        final double visibleXInViewport = vpMinX + (vpWidth - dragNode.getWidth());
        double totalWidthToScroll = contentBounds.getWidth() - vpWidth;
        if (layoutX > visibleXInViewport) {
            double widthToScroll = vpMinX + (layoutX - visibleXInViewport);
            scrollPane.setHvalue(widthToScroll / totalWidthToScroll);
        } else if (layoutX < vpMinX) {
            double widthToScroll = layoutX;
            scrollPane.setHvalue(widthToScroll / totalWidthToScroll);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (context.getCompManager().getCurrentComponent() != null) {
            context.getCompManager().visualizeUnselection(context.getCompManager().getCurrentComponent());
            context.getCompManager().setCurrentComponent(null);
        }
        if (event.getTarget() instanceof IComponentsPiece) {
            IComponentsPiece partOfComponent = (IComponentsPiece) event.getTarget();
            context.getCompManager().setCurrentComponent(partOfComponent.getLogicComponenetMediator());
            context.getCompManager().fixPointOfInteraction(event.getScreenX(), event.getScreenY());
            context.getCompManager().visualizeSelectedtComponent(currentComponent);
            currentComponent.setVisualComponentsToFront();

            readyToDrag = true;
        } else {
            if (previousComponent != null) {
                previousComponent.getBody().setEffect(null);
                previousComponent = null;
            }
        }
    }

    @Override
    protected void defineStartPointOfInteraction(double corX, double corY) {
        mouseX = corX;
        mouseY = corY;

        this.corX = currentComponent.getBody().getLayoutX();
        this.corY = currentComponent.getBody().getLayoutY();
    }

//    private void makeGlowChosenObject() {
//        if (currentComponent != null) {
//            currentComponent.makeGlowing();
//        }
//        if (previousComponent != null) {
//            if (previousComponent != currentComponent) {
//                previousComponent.getBody().setEffect(null);
//            }
//        }
//    }
    private void pressProcessingSocketsSetUp() {
        for (Socket socket : currentComponent.getConnectionSockets()) {
            if (!socket.getConnectorsList().isEmpty()) {
                for (Connector connector : socket.getConnectorsList()) {
                    connector.getConnectionLine().setVisible(true);
                    var pathLinesList = connector.getConnectionPath().getPathLinesList();
                    if (pathLinesList != null && !pathLinesList.isEmpty()) {
                        pathLinesList.clear();
                    }
                }
            }
        }
    }

    private void dragInterpretation(MouseEvent event) {
        var scrollPane = context.getScrollPane();

        double offsetX = event.getScreenX() - mouseX;
        double offsetY = event.getScreenY() - mouseY;

        var tX = corX + offsetX;
        var tY = corY + offsetY;

        Bounds contentBounds = scrollPane.getContent().getLayoutBounds();
        if (tX >= 0 && tX <= (contentBounds.getWidth() - currentComponent.getBody().getWidth()) - 40) {
            //System.out.println("TX: " + tY);
            //System.out.println("contBOUNDS HX: " + contentBounds.getWidth());
            determineX(tX, event, currentComponent);
        } else if (tX < 0) {
            currentComponent.getBody().setLayoutX(0);
        } else {
            currentComponent.getBody().setLayoutX(contentBounds.getWidth() - currentComponent.getBody().getWidth() - 40);
            gridX = (int) currentComponent.getBody().getLayoutX() / 10;
            currentComponent.getBody().setLayoutX((10 * gridX));
        }

        if (tY >= 0 && tY <= (contentBounds.getHeight() - currentComponent.getBody().getHeight())) {
            //System.out.println("TY: " + tY);
            //System.out.println("contBOUNDS HY: " + contentBounds.getHeight());
            determineY(tY, event, currentComponent);
        } else if (tY < 0) {
            currentComponent.getBody().setLayoutY(0);
        } else {
            currentComponent.getBody().setLayoutY(contentBounds.getHeight() - currentComponent.getBody().getHeight());
            gridY = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutY((10 * gridY));
        }

        if (readyToDrag == true && inDragingProcess == false) {
            inDragingProcess = true;
            pressProcessingSocketsSetUp();
        }
        currentComponent.getSymbol().toFront();

        ensureVisible(scrollPane, currentComponent);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (currentComponent != null) {
            if (isMouseInDraggableViewPort(event)) {
                dragInterpretation(event);
                ensureVisible(context.getScrollPane(), currentComponent);
                clearTimelines();
            } else {
                Pair<Pair<Boolean, Boolean>, Pair<Boolean, Boolean>> auto = isMouseInAutomaticDrag(event);
                Pair<Boolean, Boolean> xAuto = auto.getKey();
                if (xAuto.getKey() || xAuto.getValue()) {
                    if (xAuto.getKey()) { // towards left
                        if (xTimeline == null) {
                            xTimeline = new Timeline(new KeyFrame(duration, e -> {
                                if (currentComponent.getBody().getLayoutX() > 0) {
                                    currentComponent.getBody().setLayoutX(currentComponent.getBody().getLayoutX() - PX_JUMP);
                                    corX = currentComponent.getBody().getLayoutX();
                                    mouseX = event.getScreenX();
                                    ensureVisible(context.getScrollPane(), currentComponent);
                                }
                            }));
                            xTimeline.setCycleCount(Animation.INDEFINITE);
                            xTimeline.play();
                        }
                    } else if (xAuto.getValue()) { // towards right
                        if (xTimeline == null) {
                            xTimeline = new Timeline(new KeyFrame(duration, e -> {
                                final Bounds contentBounds = context.getScrollPane().getContent().getLayoutBounds();
                                if (currentComponent.getBody().getLayoutX() < contentBounds.getWidth() - currentComponent.getBody().getWidth()) {
                                    currentComponent.getBody().setLayoutX(currentComponent.getBody().getLayoutX() + PX_JUMP);
                                    corX = currentComponent.getBody().getLayoutX();
                                    mouseX = event.getScreenX();
                                    ensureVisible(context.getScrollPane(), currentComponent);
                                }
                            }));
                            xTimeline.setCycleCount(Animation.INDEFINITE);
                            xTimeline.play();
                        }
                    }
                } else {
                    stopXTimeline();
                }

                Pair<Boolean, Boolean> yAuto = auto.getValue();
                if (yAuto.getKey() || yAuto.getValue()) {
                    if (yAuto.getKey()) { // towards top
                        if (yTimeline == null) {
                            yTimeline = new Timeline(new KeyFrame(duration, e -> {
                                if (currentComponent.getBody().getLayoutY() > 0) {
                                    currentComponent.getBody().setLayoutY(currentComponent.getBody().getLayoutY() - PX_JUMP);
                                    corY = currentComponent.getBody().getLayoutY();
                                    mouseY = event.getScreenY();
                                    ensureVisible(context.getScrollPane(), currentComponent);
                                }
                            }));
                            yTimeline.setCycleCount(Animation.INDEFINITE);
                            yTimeline.play();
                        }
                    } else if (yAuto.getValue()) { // towards bottom
                        if (yTimeline == null) {
                            yTimeline = new Timeline(new KeyFrame(duration, e -> {
                                final Bounds contentBounds = context.getScrollPane().getContent().getLayoutBounds();
                                if (currentComponent.getBody().getLayoutY() < contentBounds.getHeight() - currentComponent.getBody().getHeight()) {
                                    currentComponent.getBody().setLayoutY(currentComponent.getBody().getLayoutY() + PX_JUMP);
                                    corY = currentComponent.getBody().getLayoutY();
                                    mouseY = event.getScreenY();
                                    ensureVisible(context.getScrollPane(), currentComponent);
                                }
                            }));
                            yTimeline.setCycleCount(Animation.INDEFINITE);
                            yTimeline.play();
                        }
                    }
                } else {
                    stopYTimeline();
                }
            }
            event.consume();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (inDragingProcess) {
            wasDraggin = true;
        }
        if (currentComponent != null) {
            currentComponent.notifyObservers();
            releaseProcessingSocketsSetUp();
            previousComponent = currentComponent;
            currentComponent = null;
            readyToDrag = false;
            inDragingProcess = false;
            clearTimelines();
        }
        System.out.println("RELEASED 1");
    }

    private void releaseProcessingSocketsSetUp() {
        for (Socket socket : currentComponent.getConnectionSockets()) {
            if (!socket.getConnectorsList().isEmpty()) {
                for (Connector connector : socket.getConnectorsList()) {
                    System.out.println("COn: " + connector);
                    connector.buildPath();
                    connector.getConnectionLine().setVisible(false);
                }
            }
        }
    }

    @Override
    protected void determineY(double tY, MouseEvent event, LogicComponent component) {
        var scrollPane = context.getScrollPane();

        final Bounds viewport = scrollPane.getViewportBounds();
        double vpMinY = viewport.getMinY() * -1;
        double vpHeight = viewport.getHeight();
        final double visibleYInViewport = vpMinY + (vpHeight - component.getBody().getHeight());
        if (tY >= vpMinY && tY <= visibleYInViewport) {
            component.getBody().setLayoutY(tY);
            gridY = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutY((10 * gridY));
        } else if (tY < vpMinY) {
            component.getBody().setLayoutY(vpMinY);
            gridY = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutY((10 * gridY));
        } else {
            component.getBody().setLayoutY(visibleYInViewport);
            gridY = (int) currentComponent.getBody().getLayoutY() / 10;
            currentComponent.getBody().setLayoutY((10 * gridY));

        }
    }

    @Override
    protected void determineX(double tX, MouseEvent event, LogicComponent component) {
        var scrollPane = context.getScrollPane();

        final Bounds viewport = scrollPane.getViewportBounds();
        double vpMinX = viewport.getMinX() * -1;
        double vpWidth = viewport.getWidth();
        final double visibleXInViewport = vpMinX + (vpWidth - component.getBody().getHeight());
        if (tX >= vpMinX && tX <= visibleXInViewport) {
            component.getBody().setLayoutX(tX);
            gridX = (int) currentComponent.getBody().getLayoutX() / 10;
            currentComponent.getBody().setLayoutX((10 * gridX));

        } else if (tX < vpMinX) {
            component.getBody().setLayoutX(vpMinX);
            gridX = (int) currentComponent.getBody().getLayoutX() / 10;
            currentComponent.getBody().setLayoutX((10 * gridX));

        } else {
            component.getBody().setLayoutX(visibleXInViewport);
            gridX = (int) currentComponent.getBody().getLayoutX() / 10;
            currentComponent.getBody().setLayoutX((10 * gridX));

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
