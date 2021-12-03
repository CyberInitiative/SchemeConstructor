package models;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectorAnchor extends Circle{
    ConnectorAnchor(DoubleProperty x, DoubleProperty y){
        super(x.get(), y.get(), 7);
        setFill(Color.BLUE);
        setStroke(Color.BLACK);
        setStrokeWidth(1);
        x.bind(centerXProperty());
        y.bind(centerYProperty());
    }
}
