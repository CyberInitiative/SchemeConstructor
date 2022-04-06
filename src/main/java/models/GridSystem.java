package models;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public final class GridSystem {
    
    double cellWidth = 10;
    double cellHeight = 10;
    
    public GridSystem(Pane workspace){
        drawGrid(workspace);
        generatePoints();
    }
    
    private final PathPoint[][] allPoints = new PathPoint[100][100];

    public void generatePoints() {
        for (int i = 0; i < (1000 / cellHeight); i++) {
            for (int j = 0; j < (1000 / cellWidth); j++) {
                PathPoint point = new PathPoint(cellHeight * i, cellWidth * j, i, j);
                allPoints[i][j] = point;
            }
        }
    }
    
    public void drawGrid(Pane workspace) {
        double sceneWidth = workspace.getPrefWidth();
        double sceneHeight = workspace.getPrefHeight();
        for (int i = 0; i < (sceneHeight / cellHeight); i++) {
            Line line = new Line(i * cellWidth, 0, i * cellWidth, sceneHeight);
            workspace.getChildren().add(line);
            line.setStroke(Color.rgb(128, 128, 128));
            line.setStrokeWidth(1);
            line.setOpacity(0.50);
            line.toFront();
        }

        for (int i = 0; i < (sceneWidth / cellWidth); i++) {
            Line line = new Line(0, i * cellHeight, sceneWidth, i * cellHeight);
            workspace.getChildren().add(line);
            line.setStroke(Color.rgb(128, 128, 128));
            line.setStrokeWidth(1);
            line.setOpacity(0.50);
            line.toFront();
        }
    }

    public PathPoint[][] getAllPoints() {
        return allPoints;
    }  

    @Override
    public String toString() {
        return "GridSystem{" + "cellWidth=" + cellWidth + ", cellHeight=" + cellHeight + ", allPoints=" + allPoints + '}';
    }
}
