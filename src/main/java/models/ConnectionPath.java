/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import models.PathPoint.PathPointStatus;
import models.PathPoint.RelativeDirectionStatus;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionPath {

    Comparator<PathPoint> comparator = (o1, o2) -> ((Integer) o1.getF()).compareTo((Integer) o2.getF());

    private final int DEF_MOV_COST = 10;
    private int numberOfPoints = 0;

    private PathPoint startPathPoint;
    private PathPoint endPathPoint;
    private Polyline pathPolyline = new Polyline();

    List<PathPoint> openList = new ArrayList<>();
    List<PathPoint> closedList = new ArrayList<>();

    List<PathPoint> currentNeighbours = new ArrayList<>();

    List<PathPoint> forks = new ArrayList<>();
    //List<ArrayList<PathPoint>> lists = new ArrayList<>();
    //HashMap<PathPoint, PathPoint> frk = new HashMap<>();

    public ConnectionPath(ConnectionAnchor startAnchor, ConnectionAnchor endAnchor, Pane pane) {
        //System.out.println("StartAnch: " + startAnchor);
        //System.out.println("EndAnch: " + endAnchor);
        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
                if (startAnchor.getBoundsInParent().contains(pointManager.getAllPoints()[i][j])) {
                    startPathPoint = pointManager.getAllPoints()[i][j];
                    System.out.println("Start: " + startPathPoint);
                } else if (endAnchor.getBoundsInParent().contains(pointManager.getAllPoints()[i][j])) {
                    endPathPoint = pointManager.getAllPoints()[i][j];
                    System.out.println("ENDL: " + endPathPoint);
                }
            }
        }
        defineMovementPriority();
        generatePath(startPathPoint);
        generatePolulinePath(pane, startPathPoint, endPathPoint);
//        for (PathPoint p : forks) {
//            ConnectionPath cp = new ConnectionPath(p.getX(), p.getY(), endPathPoint.getX(), endPathPoint.getY(), pane);
//        }
    }

    public ConnectionPath(double fX, double fY, double sX, double sY, Pane pane) {
        //System.out.println("StartAnch: " + startAnchor);
        //System.out.println("EndAnch: " + endAnchor);
        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
                if (fX == pointManager.getAllPoints()[i][j].getX() && fY == pointManager.getAllPoints()[i][j].getY()) {
                    startPathPoint = pointManager.getAllPoints()[i][j];
                    System.out.println("Start: " + startPathPoint);
                } else if (sX == pointManager.getAllPoints()[i][j].getX() && sY == pointManager.getAllPoints()[i][j].getY()) {
                    endPathPoint = pointManager.getAllPoints()[i][j];
                    System.out.println("ENDL: " + endPathPoint);
                }
            }
        }
        defineMovementPriority();
        generatePath(startPathPoint);
        generatePolulinePath(pane, startPathPoint, endPathPoint);
    }

    Pane pane = new Pane();

    public ConnectionPath(PathPoint startPathPoint, PathPoint endPathPoint, Pane pane) {
        this.pane = pane;
        this.startPathPoint = startPathPoint;
        this.endPathPoint = endPathPoint;

        generatePath2(startPathPoint);

        generatePolulinePath(pane, startPathPoint, endPathPoint);
    }

    private void defineMovementPriority() {
        if (startPathPoint.getRow() == endPathPoint.getRow() && startPathPoint.getCol() < endPathPoint.getCol()) {
            RelativeDirectionStatus.Right.setPriority(0);
            RelativeDirectionStatus.Down.setPriority(1);
            RelativeDirectionStatus.Up.setPriority(2);
            RelativeDirectionStatus.Left.setPriority(3);
        } else if (startPathPoint.getRow() == endPathPoint.getRow() && startPathPoint.getCol() > endPathPoint.getCol()) {
            RelativeDirectionStatus.Left.setPriority(0);
            RelativeDirectionStatus.Down.setPriority(1);
            RelativeDirectionStatus.Up.setPriority(2);
            RelativeDirectionStatus.Right.setPriority(3);
        } else if (startPathPoint.getRow() < endPathPoint.getRow() && startPathPoint.getCol() == endPathPoint.getCol()) {
            RelativeDirectionStatus.Down.setPriority(0);
            RelativeDirectionStatus.Left.setPriority(1);
            RelativeDirectionStatus.Right.setPriority(2);
            RelativeDirectionStatus.Up.setPriority(3);
        } else if (startPathPoint.getRow() > endPathPoint.getRow() && startPathPoint.getCol() == endPathPoint.getCol()) {
            RelativeDirectionStatus.Up.setPriority(0);
            RelativeDirectionStatus.Left.setPriority(1);
            RelativeDirectionStatus.Right.setPriority(2);
            RelativeDirectionStatus.Down.setPriority(3);
        } else if (startPathPoint.getRow() < endPathPoint.getRow() && startPathPoint.getCol() < endPathPoint.getCol()) {
            RelativeDirectionStatus.Right.setPriority(0); //Нижний правый угол
            RelativeDirectionStatus.Down.setPriority(1);
            RelativeDirectionStatus.Up.setPriority(2);
            RelativeDirectionStatus.Left.setPriority(3);
            System.out.println("Нижний правый");
        } else if (startPathPoint.getRow() > endPathPoint.getRow() && startPathPoint.getCol() < endPathPoint.getCol()) {
            RelativeDirectionStatus.Down.setPriority(0); //Верхний правый угол
            RelativeDirectionStatus.Right.setPriority(1);
            RelativeDirectionStatus.Left.setPriority(2);
            RelativeDirectionStatus.Up.setPriority(3);
            System.out.println("Верхний правый");
        } else if (startPathPoint.getRow() > endPathPoint.getRow() && startPathPoint.getCol() > endPathPoint.getCol()) {
            RelativeDirectionStatus.Left.setPriority(0); //Верхний левый угол
            RelativeDirectionStatus.Up.setPriority(1);
            RelativeDirectionStatus.Right.setPriority(2);
            RelativeDirectionStatus.Down.setPriority(3);
            System.out.println("Верхний левый");
        } else if (startPathPoint.getRow() < endPathPoint.getRow() && startPathPoint.getCol() > endPathPoint.getCol()) {
            RelativeDirectionStatus.Left.setPriority(0); //Нижний левый угол
            RelativeDirectionStatus.Down.setPriority(1);
            RelativeDirectionStatus.Up.setPriority(2);
            RelativeDirectionStatus.Right.setPriority(3);
            System.out.println("Нижний левый");
        }

//        System.out.println("PRIOR L " +  RelativeDirectionStatus.Left.getPriority());
//        System.out.println("PRIOR R " +  RelativeDirectionStatus.Right.getPriority());
//        System.out.println("PRIOR D " +  RelativeDirectionStatus.Down.getPriority());
//        System.out.println("PRIOR U " +  RelativeDirectionStatus.Up.getPriority());
    }

    private void generateAltarnative() {

    }

    private void generatePath(PathPoint startPathPoint) {
        PathPoint currentPathPoint = startPathPoint; //Первая текущая точка - это стартовая точка пути;
        openList.add(currentPathPoint);
        while (currentPathPoint != endPathPoint) {
            Circle circle = new Circle(currentPathPoint.getX(), currentPathPoint.getY(), 5);
            pane.getChildren().add(circle);
            getAdjacentPoints(currentPathPoint); //Поиск соседних по отношению к текущей допустимых точек;      
            Collections.sort(currentNeighbours, comparator);
            openList.remove(currentPathPoint);
            closedList.add(currentPathPoint);
            currentPathPoint = getApplicants(currentPathPoint);
            currentNeighbours.clear();
        }
    }

    private void generatePath2(PathPoint startPathPoint) {
        List<PathPoint> alterOpenList = new ArrayList<>();
        List<PathPoint> alterClosedList = new ArrayList<>();
        PathPoint currentPathPoint = startPathPoint; //Первая текущая точка - это стартовая точка пути;
        alterOpenList.add(currentPathPoint);
        while (currentPathPoint != endPathPoint) {
            getAdjacentPoints(currentPathPoint, alterOpenList, alterClosedList); //поиск соседних допустимых точек            
            Collections.sort(currentNeighbours, comparator);
            alterOpenList.remove(currentPathPoint);
            alterClosedList.add(currentPathPoint);
            currentPathPoint = getApplicants2();
            currentNeighbours.clear();
        }
    }

//    private void generatePathE(PathPoint startPathPoint) {
//        PathPoint currentPathPoint = startPathPoint; //Первая текущая точка - это стартовая точка пути;
//        int i = 0;
//        while (currentPathPoint != endPathPoint) {
//            i++;
//            getAdjacentPoints(currentPathPoint); //поиск соседних допустимых точек            
//            Collections.sort(currentNodes, comparator);
//            openList.remove(currentPathPoint);
//            closedList.add(currentPathPoint);
//            currentPathPoint = getApplicants();
//            //currentPathPoint = currentNodes.get(0);
//            partOfPath.add(currentPathPoint);
//            currentNodes.clear();
//            System.out.println("I: " + i + " ; current p: " + currentPathPoint);
//        }
//    }
    private PathPoint getApplicants(PathPoint currentPoint) {
        List<PathPoint> applicants = new ArrayList<>();
        applicants.add(currentNeighbours.get(0));
        if (currentNeighbours.size() > 1) {
            for (int i = 1; i < currentNeighbours.size(); i++) {
                if (currentNeighbours.get(i).getF() == currentNeighbours.get(0).getF()) {
                    applicants.add(currentNeighbours.get(i));
                }
            }
        }
        if (applicants.size() > 2) {
            applicants.remove(currentNeighbours.get(0));
            for (PathPoint point : applicants) {
                point.setF(0);
                point.setG(0);
                point.setH(0);
                if (forks.size() != 2) {
                    System.out.println("FRK SIZE: " + forks.size());
                    forks.add(point);
                }
                //ConnectionPath alt = new ConnectionPath(point.getX(), point.getY(), endPathPoint.getX(), endPathPoint.getY(), pane);
            }

        } else {
            PathPoint min = Collections.min(applicants, Comparator.comparing(s -> s.getRelativeDir().getPriority()));
            return min;
        }
        return currentNeighbours.get(0);
    }

    private PathPoint getApplicants2() {
        List<PathPoint> apllicants = new ArrayList<>();
        apllicants.add(currentNeighbours.get(0));
        if (currentNeighbours.size() > 1) {
            for (int i = 1; i < currentNeighbours.size(); i++) {
                if (currentNeighbours.get(i).getF() == currentNeighbours.get(0).getF()) {
                    apllicants.add(currentNeighbours.get(i));
                }
            }

        }
        PathPoint min = Collections.min(apllicants, Comparator.comparing(s -> s.getRelativeDir().getPriority()));
        return min;
    }

    private void getAdjacentPoints(PathPoint currentPoint) {
        int currCol = currentPoint.getCol();
        int currRow = currentPoint.getRow();

        int downR = currRow + 1;
        int upR = currRow - 1;
        int leftC = currCol - 1;
        int rightC = currCol + 1;

        checkThisPoint(currentPoint, pointManager.getAllPoints()[leftC][currRow]);//Левый сосед
        pointManager.getAllPoints()[currCol - 1][currRow].setRelativeDir(RelativeDirectionStatus.Left);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[rightC][currRow]);//Правый сосед
        pointManager.getAllPoints()[currCol + 1][currRow].setRelativeDir(RelativeDirectionStatus.Right);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][downR]);//Нижний сосед
        pointManager.getAllPoints()[currCol][currRow + 1].setRelativeDir(RelativeDirectionStatus.Down);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][upR]);//Верхний сосед
        pointManager.getAllPoints()[currCol][currRow - 1].setRelativeDir(RelativeDirectionStatus.Up);

        System.out.println("L: " + pointManager.getAllPoints()[leftC][currRow]);
        System.out.println("R: " + pointManager.getAllPoints()[rightC][currRow]);
        System.out.println("D: " + pointManager.getAllPoints()[currCol][downR]);
        System.out.println("U: " + pointManager.getAllPoints()[currCol][upR]);
        System.out.println("@@@@@");
    }

    private void getAdjacentPoints(PathPoint currentPoint, List<PathPoint> openList, List<PathPoint> closedList) {
        int currCol = currentPoint.getCol();
        int currRow = currentPoint.getRow();

        int downR = currRow + 1;
        int upR = currRow - 1;
        int leftC = currCol - 1;
        int rightC = currCol + 1;

        checkThisPoint(currentPoint, pointManager.getAllPoints()[leftC][currRow], openList, closedList);//Левый сосед
        pointManager.getAllPoints()[currCol - 1][currRow].setRelativeDir(RelativeDirectionStatus.Left);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[rightC][currRow], openList, closedList);//Правый сосед
        pointManager.getAllPoints()[currCol + 1][currRow].setRelativeDir(RelativeDirectionStatus.Right);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][downR], openList, closedList);//Нижний сосед
        pointManager.getAllPoints()[currCol][currRow + 1].setRelativeDir(RelativeDirectionStatus.Down);
        checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][upR], openList, closedList);//Верхний сосед
        pointManager.getAllPoints()[currCol][currRow - 1].setRelativeDir(RelativeDirectionStatus.Up);

        System.out.println("ALT L: " + pointManager.getAllPoints()[leftC][currRow]);
        System.out.println("ALT R: " + pointManager.getAllPoints()[rightC][currRow]);
        System.out.println("ALT D: " + pointManager.getAllPoints()[currCol][downR]);
        System.out.println("ALT U: " + pointManager.getAllPoints()[currCol][upR]);
        System.out.println("@@@@@");
    }

    private void checkThisPoint(PathPoint activePoint, PathPoint checkingPoint) {
        if (!closedList.contains(checkingPoint)) {
            if (checkingPoint.getStatus() != PathPointStatus.Obstructuion) {
                if (!openList.contains(checkingPoint)) {
                    openList.add(checkingPoint);
                    checkingPoint.setPreviousPoint(activePoint);
                    checkingPoint.calculateHeuristic(endPathPoint);
                    checkingPoint.setG(DEF_MOV_COST + checkingPoint.getPreviousPoint().getG());
                    checkingPoint.calculateF();
                    currentNeighbours.add(checkingPoint);
                } else {
                    if ((activePoint.getG() + DEF_MOV_COST) < checkingPoint.getG()) {
                        checkingPoint.setG(activePoint.getG() + DEF_MOV_COST);
                        checkingPoint.calculateF();
                        checkingPoint.setPreviousPoint(activePoint);
                        currentNeighbours.add(checkingPoint);
                    }
                }
            } else {
                closedList.add(checkingPoint);
            }
        }

    }

    private void checkThisPoint(PathPoint activePoint, PathPoint checkingPoint, List<PathPoint> openList, List<PathPoint> closedList) {
        if (!closedList.contains(checkingPoint)) {
            if (checkingPoint.getStatus() != PathPointStatus.Obstructuion) {
                if (!openList.contains(checkingPoint)) {
                    openList.add(checkingPoint);
                    checkingPoint.setPreviousPoint(activePoint);
                    checkingPoint.calculateHeuristic(endPathPoint);
                    checkingPoint.setG(DEF_MOV_COST + checkingPoint.getPreviousPoint().getG());
                    checkingPoint.calculateF();
                    currentNeighbours.add(checkingPoint);
                } else {
                    if ((activePoint.getG() + DEF_MOV_COST) < checkingPoint.getG()) {
                        checkingPoint.setG(activePoint.getG() + DEF_MOV_COST);
                        checkingPoint.calculateF();
                        checkingPoint.setPreviousPoint(activePoint);
                        currentNeighbours.add(checkingPoint);
                    }
                }
            } else {
                closedList.add(checkingPoint);
            }
        }

    }

    private void generatePolulinePath(Pane pane, PathPoint startPathPoint, PathPoint endPathPoint) {
        pathPolyline.setStrokeWidth(2);
        pathPolyline.setStroke(Color.BLACK);

        PathPoint currentPoint = endPathPoint;
        while (currentPoint != startPathPoint) {
            pathPolyline.getPoints().add(currentPoint.getX());
            pathPolyline.getPoints().add(currentPoint.getY());
            currentPoint = currentPoint.getPreviousPoint();
        }
        pathPolyline.getPoints().add(startPathPoint.getX());
        pathPolyline.getPoints().add(startPathPoint.getY());

        pane.getChildren().add(pathPolyline);
        pathPolyline.toBack();
    }

    public PathPoint getStartPathPoint() {
        return startPathPoint;
    }

    public void setStartPathPoint(PathPoint startPathPoint) {
        this.startPathPoint = startPathPoint;
    }

    public PathPoint getEndPathPoint() {
        return endPathPoint;
    }

    public void setEndPathPoint(PathPoint endPathPoint) {
        this.endPathPoint = endPathPoint;
    }

    public Polyline getPathPolyline() {
        return pathPolyline;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

//    private PathPoint getApplicants() {
//        List<PathPoint> apllicants = new ArrayList<>();
//        apllicants.add(currentNeighbours.get(0));
//        if (currentNeighbours.size() > 1) {
//            for (int i = 1; i < currentNeighbours.size(); i++) {
//                if (currentNeighbours.get(i).getF() == currentNeighbours.get(0).getF()) {
//                    apllicants.add(currentNeighbours.get(i));
//                    System.out.println("AP1: " + apllicants);
//                }
//            }
////            if (apllicants.size() > 1) {
////                for (int j = 1; j < apllicants.size(); j++) {
////                    PathPoint copy = apllicants.get(j);
////                    copy.setF(0);
////                    copy.setH(0);
////                    copy.setG(0);
////
////                }
////            }
//        }
//        PathPoint min = Collections.min(apllicants, Comparator.comparing(s -> s.getRelativeDir().getPriority()));
//        return min;
//    }
}
