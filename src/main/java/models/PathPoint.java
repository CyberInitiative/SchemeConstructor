package models;

import java.util.Comparator;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 *
 * @author Miroslav Levdikov
 */
public class PathPoint extends Point2D implements ObserverInterface {

    public enum PathPointStatus {
        Obstructuion, PartOfPath, Passable, Socket
    };

    public enum RelativeDirectionStatus {
        Left(3), Right(0), Up(1), Down(2);

        private RelativeDirectionStatus(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public RelativeDirectionStatus getDir() {
            return RelativeDirectionStatus.this;
        }

        private int priority;
    }

    PathPoint previousPoint = null;

    private Shape coveringObject = null;

    private PathPointStatus status = PathPointStatus.Passable;
    private RelativeDirectionStatus relativeDir = null;

    private int g; //значение g - это энергия, которая затрачена на передвижение из клетки X в текущую
    private int h; //значение h - это примерное кол-во энергии, затрачиваемое на передвежение от текущей клетки к конечной
    private int f; //значение f - это сумма значений g и h. Это окончательное число, которое указывает нам, к какому узлу нужно перейти.
    private int col;
    private int row;

    private int nodePriority;

    public PathPoint(double corX, double corY, int col, int row) {
        super(corX, corY);
        this.col = col;
        this.row = row;

    }

    public void setPriority() {
        this.nodePriority = getRelativeDir().getPriority();
    }

    public PathPoint(PathPointStatus status, double corX, double corY, int col, int row) {
        super(corX, corY);
        this.status = status;
        this.col = col;
        this.row = row;
    }

    public void calculateHeuristic(PathPoint endPathPoint) {
        this.h = ((Math.abs(this.getCol() - endPathPoint.getCol()) + Math.abs(this.getRow() - endPathPoint.getRow())) * 10);
    }

    public void calculateF() {
        this.f = this.g + this.h;
    }

    @Override
    public void update(ObservableInterface observable) {
        Shape shape = (Shape) observable;
        //System.out.println("SHAPE " + shape);
        if (shape.getBoundsInParent().contains(this)) {
            if (shape.getClass() == Socket.class) {
                Socket socket = (Socket) shape;
                socket.setCoveredPathPoint(this);
                this.setStatus(PathPointStatus.Socket);
//                System.out.println(socket);
            } else {
                this.setStatus(PathPoint.PathPointStatus.Obstructuion);
            }
            coveringObject = shape;
        } else if (!shape.getBoundsInParent().contains(this)) {
            if (coveringObject != null) {
                if (!coveringObject.getBoundsInParent().contains(this)) {
//                    if (shape.getClass() == Socket.class) {
//                        Socket socket = (Socket) shape;
//                        socket.setCoveredPathPoint(null);
//                        System.out.println(socket);
//                    }
                    coveringObject = null;
                    this.setStatus(PathPoint.PathPointStatus.Passable);
                }
            }
        }
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public PathPointStatus getStatus() {
        return status;
    }

    public void setStatus(PathPointStatus status) {
        this.status = status;
    }

    public RelativeDirectionStatus getRelativeDir() {
        return relativeDir;
    }

    public void setRelativeDir(RelativeDirectionStatus relativeDir) {
        this.relativeDir = relativeDir;
    }

    public PathPoint getPreviousPoint() {
        return previousPoint;
    }

    public void setPreviousPoint(PathPoint previousPoint) {
        this.previousPoint = previousPoint;
    }

    public int getNodePriority() {
        return nodePriority;
    }

    public void setNodePriority(int nodePriority) {
        this.nodePriority = nodePriority;
    }

    @Override
    public String toString() {
        return "PathPoint{" + "X: " + getX() + "; Y: " + getY() + "; status=" + status + ", g=" + g + ", h=" + h + ", f=" + f + ", col=" + col + ", row=" + row + ", nodePriority=" + nodePriority
                + ", direction: " + getRelativeDir() + '}';
    }

}
