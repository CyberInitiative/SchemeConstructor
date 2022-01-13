package models;

import static com.gluonapplication.views.PrimaryPresenter.connectors;
import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.sockets;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.Socket.Role;

/**
 *
 * @author Miroslav Levdikov
 */
public class Element extends Rectangle implements ObservableInterface, Movable {

    private final double ELEMENT_WIDTH = 50;
    private final double ELEMENT_HEIGHT = 80;

    private int minimalNumberOfInputs;
    private int numberOfInputs;
    private int maximumNumberOfInputs;
    //private Shape body = new Rectangle(ELEMENT_WIDTH, ELEMENT_HEIGHT, Color.WHITE);
    private Line outputLine;
    private Socket connectionOutputSocket;

    private ObservableList<Line> inputLines;
    private ObservableList<Socket> connectionInputSockets;
    private Circle inversionDesignation; //обозначение инверсии на элементе;
    private Text symbol = new Text("?");

    private double corX = 0; //координаты всего элемента
    private double corY = 0;

    private double mouseX = 0; //координаты мыши
    private double mouseY = 0;

    private boolean isDragging = false;
    private boolean isSettedOnPane = false;

    private List<Shape> listOfNodes;

    private ObserverInterface[][] pointObservers;

    private ConnectionAnchor connectionAnchor = null;

    public Element(ObserverInterface pointObservers[][]) {
        super(50, 80);
        this.pointObservers = pointObservers;
        bindOutputLine();
        bindSymbol();
        setNodeVisualDetails();
        this.minimalNumberOfInputs = 1;
        this.numberOfInputs = 4;
        this.maximumNumberOfInputs = 4;
        inputLines = FXCollections.observableArrayList();
        connectionInputSockets = FXCollections.observableArrayList();
        createInputLines();
        createInputSockets();
    }

    @Override
    public void setToFront() {
        for (Line line : inputLines) {
            line.toFront();
        }
        for (Socket socket : connectionInputSockets) {
            socket.toFront();
            if (socket.getConnector() != null) {
                socket.getConnector().toFront();
            }
        }
        outputLine.toFront();
        connectionOutputSocket.toFront();
        if (connectionOutputSocket.getConnector() != null) {
            connectionOutputSocket.getConnector().toFront();
        }
        this.toFront();
        symbol.toFront();
    }

    private void setNodeVisualDetails() {
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(2.5);
        outputLine.setStrokeWidth(2);
        connectionOutputSocket.setFill(Color.WHITE);
        connectionOutputSocket.setStroke(Color.BLACK);
        connectionOutputSocket.getStrokeDashArray().addAll(2d);
        connectionOutputSocket.setStrokeWidth(1);
        if (inversionDesignation != null) {
            inversionDesignation.setStrokeType(StrokeType.INSIDE);
            inversionDesignation.setStrokeWidth(1);
            inversionDesignation.setStroke(Color.BLACK);
            inversionDesignation.toFront();
        }
        if (symbol != null) {
            symbol.setFont(new Font(16));
            symbol.toFront();
            symbol.setMouseTransparent(true);
        }
    }

    public void setOwnerForSocket() {
        this.getConnectionOutputSocket().setOwnerComponent(this);
        for (Socket socket : this.getConnectionInputSockets()) {
            socket.setOwnerComponent(this);
        }
    }

    private void bindOutputLine() {
        DoubleProperty startX = new SimpleDoubleProperty(ELEMENT_WIDTH);
        DoubleProperty startY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);
        DoubleProperty endX = new SimpleDoubleProperty(ELEMENT_WIDTH + 10);
        DoubleProperty endY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);

        outputLine = new ElementLine(ELEMENT_WIDTH, (ELEMENT_HEIGHT / 2), (ELEMENT_WIDTH + 10), (ELEMENT_HEIGHT / 2), this.layoutXProperty().add(ELEMENT_WIDTH), this.layoutYProperty().add(ELEMENT_HEIGHT / 2), this.layoutXProperty().add(ELEMENT_WIDTH + 10), this.layoutYProperty().add(ELEMENT_HEIGHT / 2));

        connectionOutputSocket = new Socket(startX, startY, Role.Output);

        connectionOutputSocket.centerXProperty().bind(this.layoutXProperty().add(ELEMENT_WIDTH + 10));
        connectionOutputSocket.centerYProperty().bind(this.layoutYProperty().add(ELEMENT_HEIGHT / 2));
        sockets.add(connectionOutputSocket);
    }

    private void bindSymbol() {
        DoubleProperty coordX = new SimpleDoubleProperty((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2.3);
        DoubleProperty coordY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);

        symbol.layoutXProperty().bind(coordX);
        symbol.layoutYProperty().bind(coordY);

        coordX.bind(this.layoutXProperty().add((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2.3));
        coordY.bind(this.layoutYProperty().add(ELEMENT_HEIGHT / 2));
    }

    public void createStartInputs() {
        for (int i = 0; i < this.minimalNumberOfInputs; i++) {
            connectionInputSockets.add(new Socket(Role.Input));
        }
    }

    private void createInputLines() {
        if (inputLines.size() < numberOfInputs) {
            for (int i = inputLines.size(); i < numberOfInputs; i++) {
                Line line = new ElementLine();
                line.setStrokeWidth(2);
                inputLines.add(line);
            }
        }
        bindInputLines();
    }

    private void bindInputLines() {
        double previousVar = 0;
        for (int i = 0; i < inputLines.size(); i++) {
            double value = findLimit(i, previousVar);
            previousVar = value;

            DoubleProperty startX = new SimpleDoubleProperty(this.getLayoutX());
            DoubleProperty startY = new SimpleDoubleProperty(value);
            DoubleProperty endX = new SimpleDoubleProperty(this.getLayoutX() + 10);
            DoubleProperty endY = new SimpleDoubleProperty(value);

            inputLines.get(i).startXProperty().bind(startX);
            inputLines.get(i).startYProperty().bind(startY);
            inputLines.get(i).endXProperty().bind(endX);
            inputLines.get(i).endYProperty().bind(endY);

            startX.bind(this.layoutXProperty());
            startY.bind(this.layoutYProperty().add(value));
            endX.bind(this.layoutXProperty().subtract(10));
            endY.bind(this.layoutYProperty().add(value));
        }
    }

    public void deleteInput() {
        if (numberOfInputs > minimalNumberOfInputs) {
            numberOfInputs--;
            inputLines.remove(inputLines.get(inputLines.size() - 1));
            sockets.remove(connectionInputSockets.get(connectionInputSockets.size() - 1));
            connectionInputSockets.remove(connectionInputSockets.get(connectionInputSockets.size() - 1));
            for (Line line : inputLines) {
                line.startXProperty().unbind();
                line.startYProperty().unbind();
            }
            bindInputLines();
            bindSockets();
            System.out.println(inputLines.size());
        }
    }

    public void addInput() {
        if (numberOfInputs < maximumNumberOfInputs) {
            numberOfInputs++;
            Line line = new ElementLine();
            line.setStrokeWidth(2);
            var lastIndex = inputLines.size() - 1;
            inputLines.add(line);
            for (Line l : inputLines) {
                l.startXProperty().unbind();
                l.startYProperty().unbind();
            }
            bindInputLines();
            Socket socket = new Socket(Role.Input);
            socket.setOwnerComponent(this);
            connectionInputSockets.add(socket);
            bindSockets();
            sockets.add(socket);
        }
    }

    private void bindSockets() {
        for (int i = 0; i < inputLines.size(); i++) {
            DoubleProperty startX = new SimpleDoubleProperty(inputLines.get(i).getEndX());
            DoubleProperty startY = new SimpleDoubleProperty(inputLines.get(i).getEndY());

            connectionInputSockets.get(i).bind(startX, startY);

            connectionInputSockets.get(i).centerXProperty().unbind();
            connectionInputSockets.get(i).centerYProperty().unbind();

            connectionInputSockets.get(i).centerXProperty().bind(inputLines.get(i).endXProperty());
            connectionInputSockets.get(i).centerYProperty().bind(inputLines.get(i).endYProperty());
        }
    }

    private void createInputSockets() {
        for (int i = 0; i < inputLines.size(); i++) {
            DoubleProperty startX = new SimpleDoubleProperty(inputLines.get(i).getEndX());
            DoubleProperty startY = new SimpleDoubleProperty(inputLines.get(i).getEndY());

            Socket socket = new Socket(startX, startY, Role.Input);

            socket.centerXProperty().bind(inputLines.get(i).endXProperty());
            socket.centerYProperty().bind(inputLines.get(i).endYProperty());

            connectionInputSockets.add(socket);
        }
        sockets.addAll(connectionInputSockets);
    }

    private double findLimit(double iteration, double previousValue) {
        double startValue = 10 * (5 - numberOfInputs);
        if (iteration == 0) {
            return startValue;
        }
        return previousValue + 20;
    }

//        configureInputPoints();
//        for (int i = 0; i < inputsInElement.size(); i++) {
//            Line line = new Line(inputsInElement.get(i).getLayoutX() - 12.5, inputsInElement.get(i).getLayoutY(), inputsInElement.get(i).getLayoutX(), inputsInElement.get(i).getLayoutY());
//            line.setStrokeWidth(2);
//            inputLines.add(line);
//        }
//        configureInputCircles();
//    }
//
//    private void addNewInput() {
//        Circle newCircle = new Circle(CIRCLE_RADIUS);
//        newCircle.setFill(Color.BLACK);
//        inputsInElement.add(newCircle);
//        configureInputPoints();
//        addGraphicalElement(newCircle);
//    }
//
//    private void configureInputPoints() {
//        this.output.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (output.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - output.getRadius());
//        int distance = (int) ELEMENT_HEIGHT / (inputsInElement.size() + 1); //Растояние между точками входа.
//        for (int i = 0; i < inputsInElement.size(); i++) {
//            inputsInElement.get(i).relocate(this.bodyCorX - (CIRCLE_RADIUS - 1), this.bodyCorY + (distance * (i + 1) - CIRCLE_RADIUS));
//        }
//    }
    private void configureInputCircles() {
        for (int i = 0; i < connectionInputSockets.size(); i++) {
            connectionInputSockets.get(i).relocate(inputLines.get(i).getStartX() - Socket.getRADIUS(), inputLines.get(i).getStartY() - Socket.getRADIUS());
        }
    }

    private void elementMovementEvents() {
        onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();

                corX = getLayoutX();
                corY = getLayoutY();
            }
        });
    }

//    private void testEvent() {
//        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            this.addNewInput();
//        });
//    }
//    private void elementEnteredEvents() {
//        onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getTarget() instanceof Circle) {
//                    System.out.println("Circle!");
//                }
//            }
//        });
//    }
    @Override
    public void registerObserver(ObserverInterface observer) {
        //Не определено;
    }

    @Override
    public void removeObserver(ObserverInterface observer) {
        //Не определено;
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < pointObservers.length; i++) {
            for (int j = 0; j < pointObservers[0].length; j++) {
                for (Element elem : elements) {
                    pointObservers[i][j].update(elem);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Element " + this.hashCode() + "; location X: " + this.getLayoutX() + "; location Y: " + this.getLayoutY();
    }

    public int getMaximumNumberOfInputs() {
        return maximumNumberOfInputs;
    }

    public void setMaximumNumberOfInputs(int maximumNumberOfInputs) {
        this.maximumNumberOfInputs = maximumNumberOfInputs;
    }

    public Circle getInversionDesignation() {
        return inversionDesignation;
    }

    public double getELEMENT_WIDTH() {
        return ELEMENT_WIDTH;
    }

    public double getELEMENT_HEIGHT() {
        return ELEMENT_HEIGHT;
    }

    public int getMinimalNumberOfInputs() {
        return minimalNumberOfInputs;
    }

    public ObservableList<Line> getInputLines() {
        return inputLines;
    }

    public void setInputLines(ObservableList<Line> inputLines) {
        this.inputLines = inputLines;
    }

    public Line getOutputLine() {
        return outputLine;
    }

    @Override
    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    @Override
    public Text getSymbol() {
        return symbol;
    }

    @Override
    public double getCorX() {
        return corX;
    }

    @Override
    public double getCorY() {
        return corY;
    }

    @Override
    public double getMouseX() {
        return mouseX;
    }

    @Override
    public double getMouseY() {
        return mouseY;
    }

    public boolean isIsDragging() {
        return isDragging;
    }

    public void setMinimalNumberOfInputs(int minimalNumberOfInputs) {
        this.minimalNumberOfInputs = minimalNumberOfInputs;
    }

    public void setInversionDesignation(Circle inversionDesignation) {
        this.inversionDesignation = inversionDesignation;
    }

    @Override
    public void setCorX(double corX) {
        this.corX = corX;
    }

    @Override
    public void setCorY(double corY) {
        this.corY = corY;
    }

    @Override
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    @Override
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setIsDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public Socket getConnectionOutputSocket() {
        return connectionOutputSocket;
    }

    public void setConnectionOutputSocket(Socket connectionOutputSocket) {
        this.connectionOutputSocket = connectionOutputSocket;
    }

    public ObservableList<Socket> getConnectionInputSockets() {
        return connectionInputSockets;
    }

    public void setConnectionInputSockets(ObservableList<Socket> connectionInputSockets) {
        this.connectionInputSockets = connectionInputSockets;
    }
}
