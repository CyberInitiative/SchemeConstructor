package models.schemeComponents;

import com.gluonapplication.views.PrimaryPresenter;
//import static com.gluonapplication.views.PrimaryPresenter.elements;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public final class VariableBlock extends LogicComponent implements IObservable {

    public enum Position {
        Source, Receiver;
    }

    private Position position;

    public VariableBlock(IObserver pointObservers[][]) {
        super(40, 40);
        this.pointObservers = pointObservers;
        position = Position.Source;

        maxNumOfSockets = 1;
        minNumOfSockets = 1;
        numOfSockets = minNumOfSockets;

        createSockets();
        createLines();
        bindSocket();

        bindSymbol();
    }

    @Override
    public void setMediator() {
        body.setMediator(this);
    }

    @Override
    protected void createSockets() {
        if (connectionSockets.size() != numOfSockets) {
            connectionSockets.add(new Socket());
        }
    }

    @Override
    protected void createLines() {
        if (connectionLines.size() != numOfSockets) {
            connectionLines.add(new ElementLine());
        }
    }

    @Override
    protected void bindSymbol() {
        DoubleProperty coordX = new SimpleDoubleProperty((40 / 2) - symbol.getTabSize() / 2.3);
        DoubleProperty coordY = new SimpleDoubleProperty(40 / 1.6);

        symbol.layoutXProperty().bind(coordX);
        symbol.layoutYProperty().bind(coordY);

        coordX.bind(body.layoutXProperty().add((40 / 2) - symbol.getTabSize() / 2.3));
        coordY.bind(body.layoutYProperty().add(40 / 1.6));
    }

    @Override
    protected void setVisualDetails() {
        symbol.setFont(new Font(16));
        symbol.toFront();
        symbol.setMouseTransparent(true);

    }

    private void bindSocket() {
        if (position == Position.Source) {
            //ElementLine set up;
            DoubleProperty startX = new SimpleDoubleProperty(body.getWidth());
            DoubleProperty startY = new SimpleDoubleProperty(body.getHeight());
            connectionLines.get(0).setNewBindingValues(body.getWidth(), (body.getHeight() / 2), (body.getWidth() + 10), (body.getHeight() / 2), body.layoutXProperty().add(body.getWidth()), body.layoutYProperty().add(body.getHeight() / 2), body.layoutXProperty().add(body.getWidth() + 10), body.layoutYProperty().add(body.getHeight() / 2));
            //Socket set up;
            connectionSockets.get(0).bindNewValues(startX, startY);
            connectionSockets.get(0).setRole(Socket.Role.Output);
            connectionSockets.get(0).centerXProperty().bind(body.layoutXProperty().add(body.getWidth() + 10));
            connectionSockets.get(0).centerYProperty().bind(body.layoutXProperty().add(body.getHeight() / 2));
        } else if (position == Position.Receiver) {
            //ElementLine set up;
            DoubleProperty startX = new SimpleDoubleProperty(-10);
            DoubleProperty startY = new SimpleDoubleProperty(body.getHeight() / 2);
            connectionLines.get(0).setNewBindingValues(0, (body.getHeight() / 2), -10, (body.getHeight() / 2), body.layoutXProperty().add(0), body.layoutYProperty().add(body.getHeight() / 2), body.layoutXProperty().subtract(10), body.layoutYProperty().add(body.getHeight() / 2));
            //Socket set up;
            connectionSockets.get(0).bindNewValues(startX, startY);
            connectionSockets.get(0).setRole(Socket.Role.Input);
            connectionSockets.get(0).centerXProperty().bind(body.layoutXProperty().add(-10));
            connectionSockets.get(0).centerYProperty().bind(body.layoutXProperty().add(body.getHeight() / 2));
        }
    }

    @Override
    public void addOnWorkspace(Pane workspace, boolean tool) {
        workspace.getChildren().add(body);
        workspace.getChildren().add(symbol);
        workspace.getChildren().addAll(connectionLines);
        workspace.getChildren().addAll(connectionSockets);
        if (tool) {
            for (Socket socket : connectionSockets) {
                socket.setVisible(false);
            }
        } else {
            for (Socket socket : connectionSockets) {
                socket.setVisible(true);
            }
        }
    }

    @Override
    public void removeFromWorkspace(Pane workspace
    ) {
        workspace.getChildren().remove(body);
        workspace.getChildren().remove(symbol);
        workspace.getChildren().removeAll(connectionLines);
        workspace.getChildren().removeAll(connectionSockets);
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < pointObservers.length; i++) {
            for (int j = 0; j < pointObservers[0].length; j++) {
//                for (VariableBlock block : PrimaryPresenter.blocks) {
//                    pointObservers[i][j].update(block);
//                    if (connectionSockets.get(0) != null) {
//                        pointObservers[i][j].update(connectionSockets.get(0));
//                    }
//                } //rework
            }
        }
    }

    @Override
    public void registerObserver(IObserver observer
    ) {
        //Не определено;
    }

    @Override
    public void removeObserver(IObserver observer
    ) {
        //Не определено;
    }

    @Override
    public void setVisualComponentsToFront() {
        for (ElementLine line : connectionLines) {
            line.toFront();
        }
        for (Socket socket : connectionSockets) {
            socket.toFront();
            if (socket.getMainConnectedAnchor() != null) {
                socket.getMainConnectedAnchor().toFront();
            }
        }

        body.toFront();
        symbol.toFront();
    }

    public IObserver[][] getPointObservers() {
        return pointObservers;
    }

    public void setPointObservers(IObserver[][] pointObservers) {
        this.pointObservers = pointObservers;
    }

    @Override
    public double getCorX() {
        return corX;
    }

    @Override
    public void setCorX(double corX) {
        this.corX = corX;
    }

    @Override
    public double getCorY() {
        return corY;
    }

    @Override
    public void setCorY(double corY) {
        this.corY = corY;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    @Override
    public Text getSymbol() {
        return symbol;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

//    @Override
//    public void prepareToChangePosition(Pane source) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void changedPositionReport() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
