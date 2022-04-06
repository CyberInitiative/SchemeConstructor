package selector;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Miroslav Levdikov
 */
public class SelectorPoint extends Circle {

    private double corX;
    private double corY;

    public SelectorPoint(double x, double y, double radius) {
        super(x, y, radius);

        setStroke(Color.rgb(0, 103, 165));
        setFill(Color.rgb(59, 131, 189));
        setStrokeWidth(1.5);
        setOpacity(2);
    }

    public double getCorX() {
        return corX;
    }

    public void setCorX(double corX) {
        this.corX = corX;
    }

    public double getCorY() {
        return corY;
    }

    public void setCorY(double corY) {
        this.corY = corY;
    }

}
