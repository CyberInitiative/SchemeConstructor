package models;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Miroslav Levdikov
 */
public class Grid {

    public Grid() {

    }

    public void drawGrid(double cellWidth, double cellHeight, Pane workspace) {
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
}
