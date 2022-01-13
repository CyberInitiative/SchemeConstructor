package models;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;

/**
 *
 * @author Miroslav Levdikov
 */
public class PathPointsManager {
    private List<PathPoint> allPathPoints = new ArrayList<>();
    
    private List<PathPoint> starts = new ArrayList<>();
    private List<PathPoint> ends = new ArrayList<>();
    
    //private List<PathPoint> closedList = new ArrayList<>();
    //private List<PathPoint> openedList = new ArrayList<>();
    //private List<PathPoint> partOfPathPoints = new ArrayList<>();
    private PathPoint[][] allPoints = new PathPoint[100][100];

    public void generatePoints(double sceneWidth, double sceneHeight, double cellWidth, double cellHeight) {
        for (int i = 0; i < (sceneHeight / cellHeight); i++) {
            for (int j = 0; j < (sceneWidth / cellWidth); j++) {
                PathPoint point = new PathPoint(cellHeight * i, cellWidth * j, i, j);
                allPoints[i][j] = point;
                //allPathPoints.add(point);
            }
        }
    }

    public List<PathPoint> getAllPathPoints() {
        return allPathPoints;
    }

    public void setAllPathPoints(List<PathPoint> allPathPoints) {
        this.allPathPoints = allPathPoints;
    }

//    public List<PathPoint> getClosedList() {
//        return closedList;
//    }

//    public void setClosedList(List<PathPoint> closedList) {
//        this.closedList = closedList;
//    }

//    public List<PathPoint> getOpenedList() {
//        return openedList;
//    }

//    public void setOpenedList(List<PathPoint> openedList) {
//        this.openedList = openedList;
//    }

    public PathPoint[][] getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(PathPoint[][] allPoints) {
        this.allPoints = allPoints;
    }
  
//    public List<PathPoint> getPartOfPathPoints() {
//        return partOfPathPoints;
//    }
//
//    public void setPartOfPathPoints(List<PathPoint> partOfPathPoints) {
//        this.partOfPathPoints = partOfPathPoints;
//    }
 
    @Override
    public String toString() {
        return "PathPointsManager{" + "pathPoints=" + allPathPoints + '}';
    }    
}
