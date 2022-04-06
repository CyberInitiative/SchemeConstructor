package models.schemeComponents;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.connector.Connector;
import models.partsOfComponents.ElementLine;
import models.partsOfComponents.Socket;
import models.observer.IObservable;
import models.observer.IObserver;

/**
 *
 * @author Miroslav Levdikov
 */
public final class LogicElement extends LogicComponent {

    private Circle inversionDesignation; //обозначение инверсии на элементе;

    private boolean included = false;
    private boolean included2 = false;

    public LogicElement() {
        super(50, 80);
    }

    public LogicElement(IObserver pointObservers[][]) {
        super(50, 80);
        this.pointObservers = pointObservers;

        maxNumOfSockets = 5;
        minNumOfSockets = 2;
        numOfSockets = minNumOfSockets;

        createSockets();
        createLines();
        bindOutput();
        bindInputs();

        bindSymbol();

        setVisualSettings();
    }

    @Override
    public void setMediator() {
        body.setLogCompMediator(this);
        for (ElementLine line : connectionLines) {
            line.setLogCompMediator(this);
        }
        for (Socket socket : connectionSockets) {
            socket.setLogCompMediator(this);
            socket.setPointObservers(pointObservers);
        }
        body.setPointObservers(pointObservers);
    }

    @Override
    protected void createSockets() {
        if (connectionSockets.size() != numOfSockets) {
            for (int i = 0; i < numOfSockets; i++) {
                connectionSockets.add(new Socket());
            }
        }
    }

    @Override
    protected void createLines() {
        if (connectionLines.size() != numOfSockets) {
            for (int i = 0; i < numOfSockets; i++) {
                connectionLines.add(new ElementLine());
            }
        }
    }

    private void bindOutput() {
        var outputSocket = connectionSockets.get(0);
        outputSocket.setRole(Socket.Role.Output);
        var outputLine = connectionLines.get(0);
        DoubleProperty startX = new SimpleDoubleProperty(body.widthProperty().get());
        DoubleProperty startY = new SimpleDoubleProperty(body.heightProperty().get() / 2);
        outputLine.setNewBindingValues(body.getWidth(), (body.getHeight() / 2), (body.getWidth() + 10), (body.getHeight() / 2), body.layoutXProperty().add(body.getWidth()), body.layoutYProperty().add(body.getHeight() / 2), body.layoutXProperty().add(body.getWidth() + 10), body.layoutYProperty().add(body.getHeight() / 2));
        outputSocket.bindNewValues(startX, startY);
        outputSocket.centerXProperty().bind(body.layoutXProperty().add(body.getWidth() + 10));
        outputSocket.centerYProperty().bind(body.layoutYProperty().add(body.getHeight() / 2));
    }

    private void bindInputs() {
        int previousVar = 0;
        for (int i = 1; i < numOfSockets; i++) {
            int value = findLimit(i - 1, previousVar);
            previousVar = value;

            //Lines binding
            DoubleProperty startX = new SimpleDoubleProperty(body.getLayoutX());
            DoubleProperty startY = new SimpleDoubleProperty(value);
            DoubleProperty endX = new SimpleDoubleProperty(body.getLayoutX() + 10);
            DoubleProperty endY = new SimpleDoubleProperty(value);

            connectionLines.get(i).startXProperty().bind(startX);
            connectionLines.get(i).startYProperty().bind(startY);
            connectionLines.get(i).endXProperty().bind(endX);
            connectionLines.get(i).endYProperty().bind(endY);

            startX.bind(body.layoutXProperty());
            startY.bind(body.layoutYProperty().add(value));
            endX.bind(body.layoutXProperty().subtract(10));
            endY.bind(body.layoutYProperty().add(value));

            //Sockets binding
            if (connectionSockets.get(i).getRole() == null) {
                connectionSockets.get(i).setRole(Socket.Role.Input);
            }

            DoubleProperty startXs = new SimpleDoubleProperty(connectionLines.get(i).getEndX());
            DoubleProperty startYs = new SimpleDoubleProperty(connectionLines.get(i).getEndY());

            connectionSockets.get(i).bindNewValues(startXs, startYs);

            connectionSockets.get(i).centerXProperty().bind(connectionLines.get(i).endXProperty());
            connectionSockets.get(i).centerYProperty().bind(connectionLines.get(i).endYProperty());
        }
    }

    @Override
    protected void bindSymbol() {
        DoubleProperty coordX = new SimpleDoubleProperty((body.getWidth() / 2) - symbol.getTabSize() / 2.3);
        DoubleProperty coordY = new SimpleDoubleProperty(body.getHeight() / 2);

        symbol.layoutXProperty().bind(coordX);
        symbol.layoutYProperty().bind(coordY);

        coordX.bind(body.layoutXProperty().add((body.getWidth() / 2) - symbol.getTabSize() / 2.3));
        coordY.bind(body.layoutYProperty().add(body.getHeight() / 2));
    }

    @Override
    protected void setVisualSettings() {
        if (inversionDesignation != null) {
            inversionDesignation.setStrokeType(StrokeType.INSIDE);
            inversionDesignation.setStrokeWidth(1);
            inversionDesignation.setStroke(Color.BLACK);
            inversionDesignation.toFront();
        }

        symbol.setFont(new Font(16));
        symbol.toFront();
        symbol.setMouseTransparent(true);

    }

    
    @Override
    public ArrayList<Node> getComponentsNodes() {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(body);
        nodes.add(symbol);
        nodes.addAll(connectionLines);
        nodes.addAll(connectionSockets);
        if (inversionDesignation != null) {
            nodes.add(inversionDesignation);
        }

        return nodes;
    }
    
    
    
    /*
    public ArrayList<Node> addGraphElemsOnWorkspace(Pane workspace, boolean tool) {
        ArrayList<Node> nodes = new ArrayList<>();
        workspace.getChildren().add(body);
        workspace.getChildren().add(symbol);
        workspace.getChildren().addAll(connectionLines);
        workspace.getChildren().addAll(connectionSockets);
        if (inversionDesignation != null) {
            workspace.getChildren().add(inversionDesignation);
        }
        if (tool) {
            for (Socket socket : connectionSockets) {
                socket.setVisible(false);
            }
        } else {
            for (Socket socket : connectionSockets) {
                socket.setVisible(true);
            }
        }
        return nodes;
    
    */

    //@Override
    public void remGraphlElemsFromWorkspace(Pane workspace) {
        workspace.getChildren().remove(body);
        workspace.getChildren().remove(symbol);
        workspace.getChildren().removeAll(connectionLines);
        workspace.getChildren().removeAll(connectionSockets);
        if (inversionDesignation != null) {
            workspace.getChildren().remove(inversionDesignation);
        }
    }

    @Override
    public void setVisualComponentsToFront() {
        for (ElementLine line : connectionLines) {
            line.toFront();
        }
        for (Socket socket : connectionSockets) {
            socket.toFront();
//            if (socket.getMainConnectedAnchor() != null) {
//                socket.getMainConnectedAnchor().toFront();
//            }
        }

        body.toFront();
        symbol.toFront();
        if (inversionDesignation != null) {
            inversionDesignation.toFront();
        }
    }

    public void addSocket() {
        if (numOfSockets < maxNumOfSockets) {
            numOfSockets++;
            connectionLines.add(new ElementLine(this));
            for (int i = 1; i < connectionLines.size(); i++) {
                connectionLines.get(i).startXProperty().unbind();
                connectionLines.get(i).startYProperty().unbind();
            }
            Socket socket = new Socket(Socket.Role.Input, this);
            socket.setPointObservers(pointObservers);
            connectionSockets.add(socket);
            bindInputs();
        }
    }

    public void removeSocket() {
        if (numOfSockets > minNumOfSockets) {
            numOfSockets--;
            connectionLines.remove(connectionLines.get(connectionLines.size() - 1));
            connectionSockets.remove(connectionSockets.get(connectionSockets.size() - 1));
        }
        for (int i = 1; i < connectionLines.size(); i++) {
            connectionLines.get(i).startXProperty().unbind();
            connectionLines.get(i).startYProperty().unbind();
        }
        bindInputs();
    }

//    @Override
//    public void notifyObservers() {
//        for (int i = 0; i < pointObservers.length; i++) {
//            for (int j = 0; j < pointObservers[0].length; j++) {
//                pointObservers[i][j].update(this);
//            }
//        }
//        for (Socket socket : connectionSockets) {
//            socket.notifyObservers();
//        }
//    }
    private int findLimit(int iteration, int previousValue) {
        int startValue = 10 * (6 - numOfSockets);
        if (iteration == 0) {
            return startValue;
        }
        return previousValue + 20;
    }

    private String addChar(String str, char ch, int position) {
        StringBuilder sb = new StringBuilder(str);
        sb.insert(position, ch);
        return sb.toString();
    }

    private static String removeLastCharRegex(String s) {
        if (s.charAt(s.length() - 1) == '*' || s.charAt(s.length() - 1) == '+' || s.charAt(s.length() - 1) == '(' || s.charAt(s.length() - 1) == ')') {
            return (s == null) ? null : s.replaceAll(".$", "");
        }
        return s;
    }

//    @Override
//    public void prepareToChangePosition(Pane source) {
//
//    }
//
//    @Override
//    public void changedPositionReport() {
//
//    }
    public Circle getInversionDesignation() {
        return inversionDesignation;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    public Text getSymbol() {
        return symbol;
    }

//    public double getCorX() {
//        return corX;
//    }
//
//    public double getCorY() {
//        return corY;
//    }
    public void setInversionDesignation(Circle inversionDesignation) {
        this.inversionDesignation = inversionDesignation;
    }

//    public void setCorX(double corX) {
//        this.corX = corX;
//    }
//
//    public void setCorY(double corY) {
//        this.corY = corY;
//    }
    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public boolean isIncluded2() {
        return included2;
    }

    public void setIncluded2(boolean included2) {
        this.included2 = included2;
    }

    public ObservableList<Socket> getConnectionSockets() {
        return connectionSockets;
    }
}
