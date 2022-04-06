package models;

import models.partsOfComponents.Socket;
import java.util.Comparator;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import models.observer.IObservable;
import models.observer.IObserver;
import models.partsOfComponents.Body;
import models.partsOfComponents.IComponentsPiece;
import models.schemeComponents.LogicComponent;

/**
 *
 * @author Miroslav Levdikov
 */
public class PathPoint extends Point2D implements IObserver {

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

    private PathPoint previousPoint = null;

    private IComponentsPiece coveringObject = null; //Объект, который находится на точке;

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
    public void update(IObservable observable) {
        IComponentsPiece component = (IComponentsPiece) observable;

        if (component.getBoundsInParent().contains(this)) {
            if (component.getClass() == Socket.class) {
                Socket socket = (Socket) component;
                this.setStatus(PathPointStatus.Socket);
                socket.setCoveredPathPoint(this);
            } else if (component.getClass() == Body.class) {
                this.setStatus(PathPointStatus.Obstructuion);
            }
            this.coveringObject = component;

        } else if (!component.getBoundsInParent().contains(this) && coveringObject != null) {
            if (coveringObject == component) {
                coveringObject = null;
                this.setStatus(PathPointStatus.Passable);
            }
        }
    }

    /*
    LogicComponent component = (LogicComponent) observable;

        if (component.getBody().getBoundsInParent().contains(this)) {
            this.setStatus(PathPointStatus.Obstructuion);
            this.coveringObject = component.getBody();
        } else if (!component.getBody().getBoundsInParent().contains(this) && coveringObject == null) {
            if (coveringObject == component.getBody()) {
                coveringObject = null;
                this.setStatus(PathPointStatus.Passable);
            }
        }

        for (Socket socket : component.getConnectionSockets()) {
            if (socket.getBoundsInParent().contains(this)) {
                this.setStatus(PathPointStatus.Socket);
                coveringObject = socket;
                socket.setCoveredPathPoint(this);
            } else if (!socket.getBoundsInParent().contains(this) && coveringObject != null) {
                if (coveringObject == socket) {
                    coveringObject = null;
                    this.setStatus(PathPointStatus.Passable);
                }
            }
        }
     */
 /*
    @Override
    public void update(IObservable observable) {
        LogicComponent component = (LogicComponent) observable;

        if (component.getBody().getBoundsInParent().contains(this)) {
            this.setStatus(PathPointStatus.Obstructuion);
            this.coveringObject = component.getBody();
            System.out.println("OBJ CON");
        } else if (!component.getBody().getBoundsInParent().contains(this) && coveringObject != null) {
            if (coveringObject == component.getBody() && !coveringObject.getBoundsInParent().contains(this)) {
                this.coveringObject = null;
                this.setStatus(PathPoint.PathPointStatus.Passable);
                System.out.println("OBJ UNCON");
            }
        } else if (!component.getBody().getBoundsInParent().contains(this) && coveringObject == null) {
            this.setStatus(PathPoint.PathPointStatus.Passable);
        }

        for (Socket socket : component.getConnectionSockets()) {
            if (socket.getBoundsInParent().contains(this)) {
                this.setStatus(PathPointStatus.Socket);
                coveringObject = socket;
                socket.setCoveredPathPoint(this);
                System.out.println("SOCKET CON " + socket);
            } else if (!socket.getBoundsInParent().contains(this) && this.coveringObject != null) {
                if (!coveringObject.getBoundsInParent().contains(this) && coveringObject == socket) {
                    socket.setCoveredPathPoint(null);
                    coveringObject = null;
                    this.setStatus(PathPoint.PathPointStatus.Passable);
                    System.out.println("SOCKET UNCON " + socket);
                }
            } else if (!socket.getBoundsInParent().contains(this) && coveringObject == null) {
                this.setStatus(PathPoint.PathPointStatus.Passable);
            }
        }
        //System.out.println("POINT: " + this);
    }
     */
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
                + ", direction: " + getRelativeDir() + ", cover: " + coveringObject + '}';
    }

}
