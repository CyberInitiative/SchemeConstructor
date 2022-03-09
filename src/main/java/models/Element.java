package models;

import static com.gluonapplication.views.PrimaryPresenter.connectors;
import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.sockets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
public class Element extends SchemeComponent implements ObservableInterface {

    private double ELEMENT_WIDTH = 50;
    private double ELEMENT_HEIGHT = 80;

    private int minimalNumberOfInputs;
    private int numberOfInputs;
    private int maximumNumberOfInputs;
    //private Shape body = new Rectangle(ELEMENT_WIDTH, ELEMENT_HEIGHT, Color.WHITE);

    private ObservableList<Line> inputLines;
    private ObservableList<Socket> connectionInputSockets;
    private Circle inversionDesignation; //обозначение инверсии на элементе;

    //private boolean isSettedOnPane = false;
    private boolean included = false;
    private boolean included2 = false;

    private ConnectionAnchor connectionAnchor = null;

    private ArrayList<Socket> connectionSockets = new ArrayList<>();

    public Element() {
        super(50, 80);

    }

    public Element(ObserverInterface pointObservers[][]) {
        super(50, 80);
        this.pointObservers = pointObservers;
        bindOutput();
        bindSymbol();
        setNodeVisualDetails();
        this.minimalNumberOfInputs = 1;
        this.numberOfInputs = 1;
        this.maximumNumberOfInputs = 4;
        inputLines = FXCollections.observableArrayList();
        connectionInputSockets = FXCollections.observableArrayList();
        createMinimumInputs();
    }

    public Element(ObserverInterface pointObservers[][], int numberOfInputs) {
        super(50, 80);
        this.pointObservers = pointObservers;
        bindOutput();
        bindSymbol();
        setNodeVisualDetails();
        this.minimalNumberOfInputs = 1;
        this.numberOfInputs = numberOfInputs;
        this.maximumNumberOfInputs = 4;
        inputLines = FXCollections.observableArrayList();
        connectionInputSockets = FXCollections.observableArrayList();
        //createInputLines();
        createInputSockets();

    }

    @Override
    protected void setNodeVisualDetails() {
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

    @Override
    protected void bindOutput() {
        DoubleProperty startX = new SimpleDoubleProperty(ELEMENT_WIDTH);
        DoubleProperty startY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);
        DoubleProperty endX = new SimpleDoubleProperty(ELEMENT_WIDTH + 10);
        DoubleProperty endY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);

        outputLine = new ElementLine(ELEMENT_WIDTH, (ELEMENT_HEIGHT / 2), (ELEMENT_WIDTH + 10), (ELEMENT_HEIGHT / 2), this.layoutXProperty().add(ELEMENT_WIDTH), this.layoutYProperty().add(ELEMENT_HEIGHT / 2), this.layoutXProperty().add(ELEMENT_WIDTH + 10), this.layoutYProperty().add(ELEMENT_HEIGHT / 2));

        connectionOutputSocket = new Socket(startX, startY, Role.Output);
        connectionOutputSocket.setPointObservers(pointObservers);

        connectionOutputSocket.centerXProperty().bind(this.layoutXProperty().add(ELEMENT_WIDTH + 10));
        connectionOutputSocket.centerYProperty().bind(this.layoutYProperty().add(ELEMENT_HEIGHT / 2));
        sockets.add(connectionOutputSocket);
    }

    @Override
    protected void bindSymbol() {
        DoubleProperty coordX = new SimpleDoubleProperty((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2.3);
        DoubleProperty coordY = new SimpleDoubleProperty(ELEMENT_HEIGHT / 2);

        symbol.layoutXProperty().bind(coordX);
        symbol.layoutYProperty().bind(coordY);

        coordX.bind(this.layoutXProperty().add((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2.3));
        coordY.bind(this.layoutYProperty().add(ELEMENT_HEIGHT / 2));
    }

    public void createMinimumInputs() {
        for (int i = 0; i < this.minimalNumberOfInputs; i++) {
            Socket socket = new Socket(Role.Input);
            socket.setPointObservers(pointObservers);
            connectionInputSockets.add(socket);
            Line line = new ElementLine();
            inputLines.add(line);
        }
        sockets.addAll(connectionInputSockets);
        bindInputLines();
        bindInputSockets();
    }

    private void createInputLines() {
//        if (inputLines != null && !inputLines.isEmpty()) {
//            inputLines.clear();
//        }
//        for (int i = inputLines.size(); i < numberOfInputs; i++) {
//            Line line = new ElementLine();
//            line.setStrokeWidth(2);
//            inputLines.add(line);
//        }
        if (inputLines.size() < numberOfInputs) {
            for (int i = inputLines.size(); i < numberOfInputs; i++) {
                Line line = new ElementLine();
                line.setStrokeWidth(2);
                inputLines.add(line);
            }
        }
        //bindInputLines();
        //bindInputSockets();
    }

    private void createInputSockets() {
        for (int i = 0; i < numberOfInputs; i++) {
            Socket socket = new Socket(Role.Input);
            socket.setPointObservers(pointObservers);
            connectionInputSockets.add(socket);
        }
        sockets.addAll(connectionInputSockets);
    }

    public void setUpSockets() {
        createInputLines();
        bindInputLines();
        bindInputSockets();
        setOwnerForAllSockets();
    }

    @Override
    public void setVisualComponentsToFront() {
        for (Line line : inputLines) {
            line.toFront();
        }
        for (Socket socket : connectionInputSockets) {
            socket.toFront();
            if (socket.getMainConnectedAnchor() != null) {
                socket.getMainConnectedAnchor().toFront();
            }
        }
        outputLine.toFront();
        connectionOutputSocket.toFront();
        if (connectionOutputSocket.getMainConnectedAnchor() != null) {
            connectionOutputSocket.getMainConnectedAnchor().toFront();
        }
        this.toFront();
        symbol.toFront();
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

    public void bindInputSockets() {
        for (int i = 0; i < connectionInputSockets.size(); i++) {
            DoubleProperty startX = new SimpleDoubleProperty(inputLines.get(i).getEndX());
            DoubleProperty startY = new SimpleDoubleProperty(inputLines.get(i).getEndY());

            connectionInputSockets.get(i).bind(startX, startY);

            connectionInputSockets.get(i).centerXProperty().unbind();
            connectionInputSockets.get(i).centerYProperty().unbind();

            connectionInputSockets.get(i).centerXProperty().bind(inputLines.get(i).endXProperty());
            connectionInputSockets.get(i).centerYProperty().bind(inputLines.get(i).endYProperty());
        }
    }

    public void removeLines() {
        while (inputLines.size() != numberOfInputs) {
            inputLines.remove(inputLines.get(inputLines.size() - 1));
        }
        bindInputLines();
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
            bindInputSockets();
            //System.out.println(inputLines.size());
        }
    }

    public void addInput() {
        if (numberOfInputs < maximumNumberOfInputs) {
            numberOfInputs++;
            Line line = new ElementLine();
            line.setStrokeWidth(2);
            //var lastIndex = inputLines.size() - 1;
            inputLines.add(line);
            for (Line l : inputLines) {
                l.startXProperty().unbind();
                l.startYProperty().unbind();
            }
            bindInputLines();
            Socket socket = new Socket(Role.Input);
            socket.setOwnerComponent(this);
            connectionInputSockets.add(socket);
            bindInputSockets();
            sockets.add(socket);
        }
    }

    private double findLimit(double iteration, double previousValue) {
        double startValue = 10 * (5 - numberOfInputs);
        if (iteration == 0) {
            return startValue;
        }
        return previousValue + 20;
    }

    @Override
    public void registerObserver(ObserverInterface observer) {
        //Не определено;
    }

    @Override
    public void removeObserver(ObserverInterface observer) {
        //Не определено;
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

    public void swapInputSockets() {
        String temp = connectionOutputSocket.getSignal().getVariable();
        //System.out.println("TEMP IS: " + temp);
        if (temp.length() != 2 && connectionInputSockets.size() > 1) {
            int index;
            for (int i = 0; i < this.connectionInputSockets.size(); i++) {
                var curStr = connectionInputSockets.get(i).getSignal().getVariable();
                index = temp.indexOf(curStr);
                temp = addChar(temp, '#', index);
            }
            //String[] words = temp.split("#");
            List<String> items = Arrays.asList(temp.split("#"));
            List<String> finalArr = new ArrayList<>();
            for (String s : items) {
                if (!s.isBlank()) {
                    finalArr.add(s);
                }
            }
            //System.out.println("ITEMS: " + finalArr);          
            //System.out.println("FINAL ARR: " + finalArr);
            if (finalArr.size() > 0) {
                for (int i = 0; i < finalArr.size(); i++) {
//                    if (i != finalArr.size() - 1) {
                    finalArr.set(i, removeLastCharRegex(finalArr.get(i)));
//                    }
                    //System.out.println("WORDS " + finalArr.get(i));
                }
                //System.out.println("BERFORE: " + this.getConnectionInputSockets());
                //System.out.println("FINAL ARR aft: " + finalArr);
            }
            finalArr.removeIf(x -> x.isBlank());
            for (int j = 0; j < this.getConnectionInputSockets().size(); j++) {
                var curSoc = connectionInputSockets.get(j); //тек эл
                for (int i = 0; i < finalArr.size(); i++) {
                    Signal tempr;
                    if (finalArr.get(i).equals(curSoc.getSignal().getVariable()) && i != j) {
                        //System.out.println("FIN ARR: " + finalArr.get(i) + "; index: " + i);
                        //System.out.println("CUR SIG: " + curSoc.getSignal().getVariable() + "; index: " + j);
                        //tempr = connectionInputSockets.get(j).getSignal();
                        tempr = connectionInputSockets.get(i).getSignal();
//                        System.out.println("TEMP " + tempr);
//                        System.out.println("I " + connectionInputSockets.get(i).getSignal().getVariable());
//                        System.out.println("J " + connectionInputSockets.get(j).getSignal().getVariable());
                        connectionInputSockets.get(i).setSignal(curSoc.getSignal());
                        curSoc.setSignal(tempr);
                        j = 0;
                        //System.out.println("TIM " + connectionInputSockets);
                        break;
//                    connectionInputSockets.get(j).setSignal()
                    }
                }
            }
            //System.out.println("AFTER: " + this.getConnectionInputSockets());
        }
    }

    @Override
    public void notifyObservers() {
        for (int i = 0; i < pointObservers.length; i++) {
            for (int j = 0; j < pointObservers[0].length; j++) {
                for (Element elem : elements) {
                    pointObservers[i][j].update(elem);
                    if (connectionInputSockets != null) {
                        for (Socket socket : this.connectionInputSockets) {
                            pointObservers[i][j].update(socket);
                        }
                    }
                    if (connectionOutputSocket != null) {
                        pointObservers[i][j].update(connectionOutputSocket);
                    }
                }
            }
        }
//        if (connectionInputSockets != null) {
//            for (Socket socket : this.connectionInputSockets) {
//                socket.notifyObservers();
//            }
//        }
//        if (this.connectionOutputSocket != null) {
//            this.connectionOutputSocket.notifyObservers();
//        }
    }

    @Override
    public void prepareToChangePosition(Pane source) {
        connectionOutputSocket.requestToSetPathLineToNull(source);
        for (Socket socket : connectionInputSockets) {
            socket.requestToSetPathLineToNull(source);
        }
    }

    @Override
    public void changedPositionReport() {
        connectionOutputSocket.getMainConnectedAnchor().requestConnectionPath();
    }

    @Override
    public String toString() {
        return "Element: " + "OutputSocket=" + connectionOutputSocket + ", InputSockets=" + connectionInputSockets + ", corX: " + corX + "\n";
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

    public ArrayList<Socket> getOutputSockets() {
        ArrayList<Socket> array = new ArrayList<>();
        array.add(connectionOutputSocket);
        return array;
    }

    public ArrayList<Socket> getInputSockets() {
        ArrayList<Socket> array = new ArrayList<>();
        array.addAll(connectionInputSockets);
        return array;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    public Text getSymbol() {
        return symbol;
    }

    public double getCorX() {
        return corX;
    }

    public double getCorY() {
        return corY;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public void setMinimalNumberOfInputs(int minimalNumberOfInputs) {
        this.minimalNumberOfInputs = minimalNumberOfInputs;
    }

    public void setInversionDesignation(Circle inversionDesignation) {
        this.inversionDesignation = inversionDesignation;
    }

    public void setCorX(double corX) {
        this.corX = corX;
    }

    public void setCorY(double corY) {
        this.corY = corY;
    }

    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setConnectionOutputSocket(Socket connectionOutputSocket) {
        if (connectionOutputSocket != null) {
            sockets.remove(connectionOutputSocket);
        }
        this.connectionOutputSocket = connectionOutputSocket;
        setOwnerForOutputSocket();
    }

    public Socket getConnectionOutputSocket() {
        return connectionOutputSocket;
    }

    public void setConnectionInputSockets(ObservableList<Socket> connectionInputSockets) {
        if (this.connectionInputSockets != null && !this.connectionInputSockets.isEmpty()) {
            sockets.removeAll(this.connectionInputSockets);
        }
        this.connectionInputSockets = connectionInputSockets;
        numberOfInputs = connectionInputSockets.size();
        //createInputLines();
        //bindSockets();
        sockets.addAll(connectionInputSockets);
        setOwnerForInputSockets();
    }

    public void setOwnerForOutputSocket() {
        this.getConnectionOutputSocket().setOwnerComponent(this);
    }

    public void setOwnerForInputSockets() {
        for (Socket socket : this.getConnectionInputSockets()) {
            socket.setOwnerComponent(this);
        }
    }

    public void setOwnerForAllSockets() {
        this.getConnectionOutputSocket().setOwnerComponent(this);
        for (Socket socket : this.getConnectionInputSockets()) {
            socket.setOwnerComponent(this);
        }
    }

    public ObservableList<Socket> getConnectionInputSockets() {
        return connectionInputSockets;
    }

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

    public int getNumberOfInputs() {
        return numberOfInputs;
    }

    public void setNumberOfInputs(int numberOfInputs) {
        this.numberOfInputs = numberOfInputs;
    }
}
