package models;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class ElementLine extends Line {

    private DoubleProperty startX;
    private DoubleProperty startY;
    private DoubleProperty endX;
    private DoubleProperty endY;

    private DoubleBinding bindToStartX;
    private DoubleBinding bindToStartY;
    private DoubleBinding bindToEndX;
    private DoubleBinding bindToEndY;

    ElementLine(){
        
    }
    
    ElementLine(double startXd, double startYd, double endXd, double endYd, DoubleBinding bindToStartX, DoubleBinding bindToStartY, DoubleBinding bindToEndX, DoubleBinding bindToEndY) {
        startX = new SimpleDoubleProperty(startXd);
        startY = new SimpleDoubleProperty(startYd);
        endX = new SimpleDoubleProperty(endXd);
        endY = new SimpleDoubleProperty(endYd);

        this.bindToStartX = bindToStartX;
        this.bindToStartY = bindToStartY;
        this.bindToEndX = bindToEndX;
        this.bindToEndY = bindToEndY;

        setStrokeWidth(2);
        setStroke(Color.BLACK);

        bind();
    }

    public void setNewBinds(double startXd, double startYd, double endXd, double endYd, DoubleBinding bindToStartX, DoubleBinding bindToStartY, DoubleBinding bindToEndX, DoubleBinding bindToEndY) {
        startX = new SimpleDoubleProperty(startXd);
        startY = new SimpleDoubleProperty(startYd);
        endX = new SimpleDoubleProperty(endXd);
        endY = new SimpleDoubleProperty(endYd);

        this.bindToStartX = bindToStartX;
        this.bindToStartY = bindToStartY;
        this.bindToEndX = bindToEndX;
        this.bindToEndY = bindToEndY;
        bind();
    }

    public void bind() {
        startXProperty().bind(startX);
        startYProperty().bind(startY);
        endXProperty().bind(endX);
        endYProperty().bind(endY);

        startX.bind(bindToStartX);
        startY.bind(bindToStartY);
        endX.bind(bindToEndX);
        endY.bind(bindToEndY);
    }

    public void unbind() {
        startXProperty().unbind();
        startYProperty().unbind();
        endXProperty().unbind();
        endYProperty().unbind();

        startX.unbind();
        startY.unbind();
        endX.unbind();
        endY.unbind();
    }

}
