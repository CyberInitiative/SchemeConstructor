package models.tools;

import com.gluonapplication.views.Controller;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 *
 * @author Miroslav Levdikov
 * @param <T>
 */
public abstract class Tool<T> {

    /*
    Класс Tool является базовым классом для состояний;
     */
    public Tool() {

    }

    protected Controller context;

    protected double mouseX = 0; //координаты мыши; точка первого взаимодействия (нажатия);
    protected double mouseY = 0;

    protected double corX = 0; //координаты элемента в точке первого взаимодействия;
    protected double corY = 0;

    protected int gridX; //для расчета сдвига по сетке;
    protected int gridY;
    
    protected static Duration duration = Duration.millis(10);
    protected static double PX_JUMP = 5;
    protected static double AUTOMATIC_OFFSET = 30;

    protected Timeline xTimeline;
    protected Timeline yTimeline;

    public abstract void mouseClicked(MouseEvent event);

    public abstract void mousePressed(MouseEvent event);

    public abstract void mouseDragged(MouseEvent event);

    public abstract void mouseReleased(MouseEvent event);

    protected abstract void ensureVisible(ScrollPane scrollPane, T t);

    protected abstract void determineY(double tY, MouseEvent event, T t);

    protected abstract void determineX(double tX, MouseEvent event, T t);
    
    protected abstract void defineStartPointOfInteraction(double corX, double corY);

    protected Pair<Pair<Boolean, Boolean>, Pair<Boolean, Boolean>> isMouseInAutomaticDrag(MouseEvent event) {
        Node viewport = context.getScrollPane().lookup(".viewport");
        Bounds viewportSceneBounds = viewport.localToScene(viewport.getLayoutBounds());
        double eX = event.getSceneX();
        double eY = event.getSceneY();
        double vpMinX = viewportSceneBounds.getMinX();
        double vpMaxX = viewportSceneBounds.getMaxX();
        double vpMinY = viewportSceneBounds.getMinY();
        double vpMaxY = viewportSceneBounds.getMaxY();
        Pair<Boolean, Boolean> autoX = new Pair<>(eX <= vpMinX + AUTOMATIC_OFFSET, vpMaxX - AUTOMATIC_OFFSET <= eX);
        Pair<Boolean, Boolean> autoY = new Pair<>(eY <= vpMinY + AUTOMATIC_OFFSET, vpMaxY - AUTOMATIC_OFFSET <= eY);
        return new Pair<>(autoX, autoY);
    }

    protected boolean isMouseInDraggableViewPort(MouseEvent event) {
        var scrollPane = context.getScrollPane();
        Node viewport = scrollPane.lookup(".viewport");
        Bounds viewportSceneBounds = viewport.localToScene(viewport.getLayoutBounds());
        Bounds draggableBounds = new BoundingBox(viewportSceneBounds.getMinX() + AUTOMATIC_OFFSET,
                viewportSceneBounds.getMinY() + AUTOMATIC_OFFSET,
                viewportSceneBounds.getWidth() - (2 * AUTOMATIC_OFFSET),
                viewportSceneBounds.getHeight() - (2 * AUTOMATIC_OFFSET));
        return draggableBounds.contains(event.getSceneX(), event.getSceneY());
    }

    protected void clearTimelines() {
        stopXTimeline();
        stopYTimeline();
    }

    protected void stopXTimeline() {
        if (xTimeline != null) {
            xTimeline.stop();
        }
        xTimeline = null;
    }

    protected void stopYTimeline() {
        if (yTimeline != null) {
            yTimeline.stop();
        }
        yTimeline = null;
    }

    protected double getMouseX() {
        return mouseX;
    }

    protected void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    protected double getMouseY() {
        return mouseY;
    }

    protected void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setContext(Controller context) {
        this.context = context;
    }

    public Controller getContext() {
        return context;
    }   
}
