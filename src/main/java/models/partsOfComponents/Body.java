package models.partsOfComponents;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import models.observer.IObservable;
import models.observer.IObserver;
import models.schemeComponents.LogicComponent;

/**
 *
 * @author Miroslav Levdikov
 */
public class Body extends Rectangle implements IComponentsPiece, IObservable {

    private LogicComponent mediator;

    private IObserver[][] pointObservers;

    public Body(int width, int height) {
        super(width, height);
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(2.5);
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < pointObservers.length; i++) {
            for (int j = 0; j < pointObservers[0].length; j++) {
                pointObservers[i][j].update(this);
            }
        }
    }

    @Override
    public double getLayoutCorX() {
        return this.getLayoutX();
    }

    @Override
    public double getCotLayoutCorY() {
        return this.getLayoutY();
    }

    @Override
    public void setLogCompMediator(LogicComponent mediator) {
        this.mediator = mediator;
    }

    @Override
    public LogicComponent getLogicComponenetMediator() {
        return mediator;
    }

    public IObserver[][] getPointObservers() {
        return pointObservers;
    }

    public void setPointObservers(IObserver[][] pointObservers) {
        this.pointObservers = pointObservers;
    }
}
