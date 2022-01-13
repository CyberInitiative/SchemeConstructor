package models;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElementConnector extends Line {
    ElementConnector(DoubleProperty startPointX, DoubleProperty startPointY, DoubleProperty endPointX, DoubleProperty endPointY){
        startXProperty().bind(startPointX);
        startYProperty().bind(startPointY);
        endXProperty().bind(endPointX);
        endYProperty().bind(endPointY);
        setStrokeWidth(1.5);
        setStroke(Color.BLACK);
        setMouseTransparent(true);
    }
}
