package models.partsOfComponents;

import javafx.geometry.Bounds;
import models.schemeComponents.LogicComponent;

/**
 *
 * @author Miroslav Levdikov
 */
public interface IComponentsPiece {

    double getLayoutCorX();

    double getCotLayoutCorY();

    void setLogCompMediator(LogicComponent mediator);

    LogicComponent getLogicComponenetMediator();
    
    Bounds getBoundsInParent();
}