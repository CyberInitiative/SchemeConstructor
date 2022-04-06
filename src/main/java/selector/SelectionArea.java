package selector;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Miroslav Levdikov
 */
public class SelectionArea {
    
    private SelectorPoint startPoint;
    private SelectorPoint dragingPoint;
    private SelectorPoint xDependetPoint;
    private SelectorPoint yDependetPoint;
    
    private Rectangle areaSelector;
       
    private double width = 0;
    private double height = 0;
    
    public SelectionArea(double xCor, double yCor) {
        startPoint = new SelectorPoint(xCor, yCor, 0.1);
        dragingPoint = new SelectorPoint(xCor, yCor, 5);
        xDependetPoint = new SelectorPoint(xCor, yCor, 0.1);
        yDependetPoint = new SelectorPoint(xCor, yCor, 0.1);
        
        xDependetPoint.setVisible(false);
        yDependetPoint.setVisible(false);
        startPoint.setVisible(false);
               
        xDependetPoint.centerXProperty().bind(dragingPoint.centerXProperty());
        xDependetPoint.centerYProperty().bind(startPoint.centerYProperty());
        
        yDependetPoint.centerYProperty().bind(dragingPoint.centerYProperty());
        yDependetPoint.centerXProperty().bind(startPoint.centerXProperty());
    }
    
    public void calculateParams() {
        var A = startPoint;
        var B = xDependetPoint;
        var C = yDependetPoint;
        var D = dragingPoint;
        
        width = Math.sqrt(Math.pow((B.getCenterX() - A.getCenterX()), 2) + Math.pow((B.getCenterY() - A.getCenterY()), 2));
        height = Math.sqrt(Math.pow((C.getCenterX() - A.getCenterX()), 2) + Math.pow((C.getCenterY() - A.getCenterY()), 2));
        
        if (D.getCenterX() < A.getCenterX() && D.getCenterY() < A.getCenterY()) {
            areaSelector = new Rectangle(D.getCenterX(), D.getCenterY(), width, height);
        } else if (D.getCenterX() > A.getCenterX() && D.getCenterY() < A.getCenterY()) {
            areaSelector = new Rectangle(C.getCenterX(), C.getCenterY(), width, height);
        } else if (D.getCenterX() < A.getCenterX() && D.getCenterY() > A.getCenterY()) {
            areaSelector = new Rectangle(B.getCenterX(), B.getCenterY(), width, height);
        } else {
            areaSelector = new Rectangle(A.getCenterX(), A.getCenterY(), width, height);
        }
        areaSelector.setStroke(Color.rgb(0, 83, 138));
        areaSelector.setStrokeWidth(1);
        areaSelector.setFill(Color.rgb(34, 113, 179, 0.5));      
    }
    
    public SelectorPoint getStartPoint() {
        return startPoint;
    }
    
    public void setStartPoint(SelectorPoint startPoint) {
        this.startPoint = startPoint;
    }
    
    public SelectorPoint getDragingPoint() {
        return dragingPoint;
    }
    
    public void setDragingPoint(SelectorPoint dragingPoint) {
        this.dragingPoint = dragingPoint;
    }
    
    public SelectorPoint getxDependetPoint() {
        return xDependetPoint;
    }
    
    public void setxDependetPoint(SelectorPoint xDependetPoint) {
        this.xDependetPoint = xDependetPoint;
    }
    
    public SelectorPoint getyDependetPoint() {
        return yDependetPoint;
    }
    
    public void setyDependetPoint(SelectorPoint yDependetPoint) {
        this.yDependetPoint = yDependetPoint;
    }
    
    public double getWidth() {
        return width;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public Rectangle getAreaSelector() {
        return areaSelector;
    }
    
    public void setAreaSelector(Rectangle areaSelector) {
        this.areaSelector = areaSelector;
    }    
}
