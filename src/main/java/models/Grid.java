package models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class Grid {

    public Grid(double sceneWidth, double sceneHeight, double cellWidth, double cellHeight, Pane scene) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.scene = scene;
    }

    private Pane scene;
    
    private double sceneHeight;
    private double sceneWidth;

    private  double cellWidth;
    private  double cellHeight;

    public Pane getScene() {
        return scene;
    }

    public void setScene(Pane scene) {
        this.scene = scene;
    }

    public double getSceneHeight() {
        return sceneHeight;
    }

    public void setSceneHeight(double sceneHeight) {
        this.sceneHeight = sceneHeight;
    }

    public double getSceneWidth() {
        return sceneWidth;
    }

    public void setSceneWidth(double sceneWidth) {
        this.sceneWidth = sceneWidth;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(double cellHeight) {
        this.cellHeight = cellHeight;
    }

    public void drawGrid() {
        for (int i = 0; i < (sceneHeight / cellHeight); i++) {
            Line line = new Line(i * cellWidth, 0, i * cellWidth, sceneHeight);
            scene.getChildren().add(line);
            line.setStroke(Color.rgb(128,128,128));
            line.setStrokeWidth(1);
            line.setOpacity(0.50);
            line.toFront();
        }

        for (int i = 0; i < (sceneWidth / cellWidth); i++) {
            Line line = new Line(0, i * cellHeight, sceneWidth, i * cellHeight);
            scene.getChildren().add(line);
            line.setStroke(Color.rgb(128,128,128));
            line.setStrokeWidth(1);
            line.setOpacity(0.50);
            line.toFront();
        }      
    }
}
