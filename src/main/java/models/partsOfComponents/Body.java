package models.partsOfComponents;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import models.schemeComponents.LogicComponent;

/**
 *
 * @author Miroslav Levdikov
 */
public class Body extends Rectangle implements IComponentsPiece {

    private LogicComponent mediator;

    public Body(int width, int height) {
        super(width, height);
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(2.5);
    }

    @Override
    public double getCorX() {
        return this.getLayoutX();
    }

    @Override
    public double getCotY() {
        return this.getLayoutY();
    }

    @Override
    public void setMediator(LogicComponent mediator) {
        this.mediator = mediator;
    }

    @Override
    public LogicComponent getMediator() {
        return mediator;
    }
}
