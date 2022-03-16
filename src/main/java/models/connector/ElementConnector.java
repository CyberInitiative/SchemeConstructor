package models.connector;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElementConnector extends Line implements IConnectorsPiece {

    private Connector mediator;

    public ElementConnector(DoubleProperty startPointX, DoubleProperty startPointY, DoubleProperty endPointX, DoubleProperty endPointY) {
        startXProperty().bind(startPointX);
        startYProperty().bind(startPointY);
        endXProperty().bind(endPointX);
        endYProperty().bind(endPointY);
        setStrokeWidth(2.5);
        setStroke(Color.BLACK);
        setMouseTransparent(true);
    }

    public ElementConnector(Connector mediator) {
        setStrokeWidth(1.5);
        setStroke(Color.BLACK);
        setMouseTransparent(true);
        this.mediator = mediator;
    }

    @Override
    public void setMediator(Connector mediator) {
        this.mediator = mediator;
    }

    public Connector getMediator() {
        return mediator;
    }

    public void bind(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
        startXProperty().bind(startX);
        startYProperty().bind(startY);
        endXProperty().bind(endX);
        endYProperty().bind(endY);
    }

}
