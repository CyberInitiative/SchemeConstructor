package models.connector;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import java.util.Collections;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import models.PathPoint;
import models.PathPoint.PathPointStatus;
import models.PathPoint.RelativeDirectionStatus;
import models.observer.IObservable;
import models.observer.IObserver;
import models.partsOfComponents.Socket;

/**
 *
 * @author Miroslav Levdikov
 */
public class ConnectionPath implements IObserver, IConnectorsPiece {

    private Comparator<PathPoint> comparator = (o1, o2) -> ((Integer) o1.getF()).compareTo((Integer) o2.getF());

    private Connector mediator;

    private final int DEF_MOV_COST = 10;

    private PathPoint startPathPoint;
    private PathPoint endPathPoint;

    //private Polyline pathPolyline = new Polyline();
    private List<PathPoint> pathPointsList = new ArrayList<>();

    protected ObservableList<Line> pathLinesList = FXCollections.observableArrayList();

    private List<PathPoint> openList = new ArrayList<>();
    private List<PathPoint> closedList = new ArrayList<>();

    public ConnectionPath() {

    }

    public boolean buildPath() {
        if (mediator.getStartConnectionAnchor().getConnectedSocket() != null && mediator.getEndConnectionAnchor().getConnectedSocket() != null) {
            if (mediator.getStartConnectionAnchor().getIsOutput() && !mediator.getEndConnectionAnchor().getIsOutput()) {
                startPathPoint = mediator.getStartConnectionAnchor().getConnectedSocket().getCoveredPathPoint();
                endPathPoint = mediator.getEndConnectionAnchor().getConnectedSocket().getCoveredPathPoint();
            } else if (!mediator.getStartConnectionAnchor().getIsOutput() && mediator.getEndConnectionAnchor().getIsOutput()) {
                endPathPoint = mediator.getStartConnectionAnchor().getConnectedSocket().getCoveredPathPoint();
                startPathPoint = mediator.getEndConnectionAnchor().getConnectedSocket().getCoveredPathPoint();
            } else {
                return false;
            }
            pathLinesList.clear();
            pathPointsList.clear();
            openList.clear();
            closedList.clear();

            generatePath(startPathPoint);
            visualizePath();
            startPathPoint = null;
            endPathPoint = null;
            return true;
        }
        return false;
    }

    public void definePathPoint(ConnectionAnchor anchor) {
        if (anchor.getIsOutput() == true) {
            startPathPoint = anchor.getConnectedSocket().getCoveredPathPoint();
        } else if (anchor.getIsOutput() == false) {
            endPathPoint = anchor.getConnectedSocket().getCoveredPathPoint();
        }
    }

//    public ConnectionPath(ConnectionAnchor startAnchor, PathPoint endPathPoint, Pane pane) {
//        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
//            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
//                if (startAnchor.getBoundsInParent().contains(pointManager.getAllPoints()[i][j])) {
//                    startPathPoint = pointManager.getAllPoints()[i][j];
//                }
//            }
//        }
//        this.endPathPoint = endPathPoint;
//        //System.out.println("№ START POINT: " + startPathPoint);
//        //System.out.println("№ END POINT: " + endPathPoint);
//        //defineMovementPriority();
//        generatePath(startPathPoint);
//        generatePolylinePath();
//        Circle circle = new Circle(endPathPoint.getX(), endPathPoint.getY(), 3);
//        pane.getChildren().add(circle);
//    }
//    public ConnectionPath(ConnectionAnchor startAnchor, PathPoint endPathPoint) {
//        for (int i = 0; i < pointManager.getAllPoints().length; i++) {
//            for (int j = 0; j < pointManager.getAllPoints()[0].length; j++) {
//                if (startAnchor.getBoundsInParent().contains(pointManager.getAllPoints()[i][j])) {
//                    startPathPoint = pointManager.getAllPoints()[i][j];
//                }
//            }
//        }
//        this.endPathPoint = endPathPoint;
//        //defineMovementPriority();
//        generatePath(startPathPoint);
//        generatePolylinePath(pane);
//        Circle circle = new Circle(endPathPoint.getX(),endPathPoint.getY(), 3);
//        //pane.getChildren().add(circle);
//    }
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
    }

    private void generatePath(PathPoint startPathPoint) {
        PathPoint currentPathPoint = startPathPoint; //Первая текущая точка - это стартовая точка пути; 
        openList.add(startPathPoint); //Добавляем стартовую точку в открытый список;
        while (currentPathPoint != endPathPoint) {
            //defineMovementPriority(); //Не убирать;
            currentPathPoint = findMinimalF();
            openList.remove(currentPathPoint);
            closedList.add(currentPathPoint);
            getAdjacentPoints(currentPathPoint); //Поиск соседних допустимых точек;
            if (openList.isEmpty()) {
                System.out.println("THERE IS NO WAY");
                //TODO Реализовать удаление соединителя;
                break;
            }
        }
    }

    private PathPoint findMinimalF() {
        List<PathPoint> applicants = new ArrayList<>(); //Массив претендентов на становление текущим узлом;
        Collections.sort(openList, comparator);
        applicants.add(openList.get(0)); //Узел с минимальным значением F;
        if (openList.size() > 1) {
            for (int i = 1; i < openList.size(); i++) {
                if (openList.get(0).getF() == openList.get(i).getF()) {
                    applicants.add(openList.get(i));
                }
            }
        }
        PathPoint min = Collections.min(applicants, Comparator.comparing((PathPoint s) -> s.getH()).thenComparing(s -> s.getRelativeDir().getPriority()));
        /*
        Если узлов с минимальным значением F несколько, отбирается тот узел, который имеет более приоритетное направление;
         */
        return min;
    }

    private void getAdjacentPoints(PathPoint currentPoint) {
        int currCol = currentPoint.getCol();
        int currRow = currentPoint.getRow();

        int downR = currRow + 1; //Ряд ниже;
        int upR = currRow - 1;  //Ряд выше;
        int leftC = currCol - 1; //Ряд слева;
        int rightC = currCol + 1; //Ряд справа;

        if (leftC >= 0) {
            checkThisPoint(currentPoint, pointManager.getAllPoints()[leftC][currRow]);//Проверка левого соседа;
            pointManager.getAllPoints()[leftC][currRow].setRelativeDir(RelativeDirectionStatus.Left);
            pointManager.getAllPoints()[leftC][currRow].setPriority();
        }
        if (rightC < pointManager.getAllPoints()[0].length) {
            checkThisPoint(currentPoint, pointManager.getAllPoints()[rightC][currRow]);//Проверка правого соседа;
            pointManager.getAllPoints()[rightC][currRow].setRelativeDir(RelativeDirectionStatus.Right);
            pointManager.getAllPoints()[rightC][currRow].setPriority();
        }
        if (downR < pointManager.getAllPoints().length) {
            checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][downR]);//Проверка нижнего соседа;
            pointManager.getAllPoints()[currCol][downR].setRelativeDir(RelativeDirectionStatus.Down);
            pointManager.getAllPoints()[currCol][downR].setPriority();
        }
        if (upR >= 0) {
            checkThisPoint(currentPoint, pointManager.getAllPoints()[currCol][upR]);//Проверка верхнего соседа;
            pointManager.getAllPoints()[currCol][upR].setRelativeDir(RelativeDirectionStatus.Up);
            pointManager.getAllPoints()[currCol][upR].setPriority();
        }
    }

    private void checkThisPoint(PathPoint activePoint, PathPoint checkingPoint) {
        if (!closedList.contains(checkingPoint)) {
            if (checkingPoint.getStatus() != PathPointStatus.Obstructuion) {
                if (checkingPoint.getStatus() == PathPointStatus.Socket) {
                    if (checkingPoint != startPathPoint && checkingPoint != endPathPoint) {
                        closedList.add(checkingPoint);
                        return;
                    }
                }
                if (!openList.contains(checkingPoint)) {
                    openList.add(checkingPoint);
                    checkingPoint.setPreviousPoint(activePoint);
                    checkingPoint.calculateHeuristic(endPathPoint);
                    checkingPoint.setG(DEF_MOV_COST + checkingPoint.getPreviousPoint().getG());
                    checkingPoint.calculateF();
                }
            } else {
                //Пересчет значения F;
                if ((activePoint.getG() + DEF_MOV_COST) < checkingPoint.getG()) {
                    checkingPoint.setG(activePoint.getG() + DEF_MOV_COST);
                    checkingPoint.calculateF();
                    checkingPoint.setPreviousPoint(activePoint);
                }
            }
        } else {
            closedList.add(checkingPoint);
        }
    }

    private void visualizePath() {
        PathPoint currentPoint = endPathPoint;
        while (currentPoint != startPathPoint) {
            Line line = new Line(currentPoint.getX(), currentPoint.getY(), currentPoint.getPreviousPoint().getX(), currentPoint.getPreviousPoint().getY());
            line.setStrokeWidth(2.5);
            pathLinesList.add(line);
            currentPoint = currentPoint.getPreviousPoint();
        }
    }

//    private void generatePolylinePath() { //OLD METHOD
//        pathPolyline.setStrokeWidth(2.5);
//        pathPolyline.setStroke(Color.BLACK);
//
//        PathPoint currentPoint = endPathPoint;
//        while (currentPoint != startPathPoint) {
//            pathPolyline.getPoints().add(currentPoint.getX());
//            pathPolyline.getPoints().add(currentPoint.getY());
//            pathPointsList.add(currentPoint);
//            currentPoint = currentPoint.getPreviousPoint();
//        }
//        pathPolyline.getPoints().add(startPathPoint.getX());
//        pathPolyline.getPoints().add(startPathPoint.getY());
//        pathPointsList.add(startPathPoint);
//
//        pathPolyline.toBack();
//    }
    public PathPoint getClosestPoint(double x, double y) {
        //System.out.println("X: " + x);
        //System.out.println("Y: " + y);
        double prevDistance = 100000;
        PathPoint returnedPoint = null;
        for (PathPoint pPoint : pathPointsList) {
            double distance = Math.sqrt(Math.pow(pPoint.getX() - x, 2) + Math.pow(pPoint.getY() - y, 2));
            //System.out.println("DIST: " + distance);
            if (distance < prevDistance) {
                prevDistance = distance;
                returnedPoint = pPoint;
            }
        }
        //System.out.println("RETUrNED POINT: " + returnedPoint);
        return returnedPoint;
    }

    @Override
    public void update(IObservable observable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMediator(Connector mediator) {
        this.mediator = mediator;
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

//    public Polyline getPathPolyline() {
//        return pathPolyline;
//    }
    public Connector getMediator() {
        return mediator;
    }

    public ObservableList<Line> getPathLinesList() {
        return pathLinesList;
    }

    public List<PathPoint> getPathPointsList() {
        return pathPointsList;
    }

}
