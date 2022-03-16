package models.partsOfComponents;

import javafx.geometry.Bounds;
import models.schemeComponents.LogicComponent;

/**
 *
 * @author Miroslav Levdikov
 */
public interface IComponentsPiece {

    double getCorX();

    double getCotY();

    void setMediator(LogicComponent mediator);

    LogicComponent getMediator();
    
    Bounds getBoundsInParent();
}