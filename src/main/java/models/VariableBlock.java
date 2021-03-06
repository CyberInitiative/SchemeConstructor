package models;

import com.gluonapplication.views.PrimaryPresenter;
//import static com.gluonapplication.views.PrimaryPresenter.elements;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Miroslav Levdikov
 */
public class VariableBlock extends SchemeComponent implements ObservableInterface {

    private boolean state = false; //false - выход, true - вход;

    private final double BLOCK_WIDTH = 40;
    private final double BLOCK_HEIGHT = 40;

    //private String variable = String.valueOf(symbol);
    private Socket connectionInputSocket;
    private Line inputLine;

    public VariableBlock(ObserverInterface pointObservers[][]) {
        super(40, 40);
        this.pointObservers = pointObservers;
        setNodeVisualDetails();
        bindSymbol();
        //bindOutputLine();
        bindInputLine();
    }

    public VariableBlock(ObserverInterface pointObservers[][], boolean state, Character smbl) {
        super(40, 40);
        this.pointObservers = pointObservers;
        this.state = state;
        getSymbol().setText(String.valueOf(smbl));
        setNodeVisualDetails();
        bindSymbol();
        if (state) {
            bindInputLine();
        } else {
            bindOutput();
        }

    }

    protected void setNodeVisualDetails() {
        this.setFill(Color.WHITE);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(2.5);

        symbol.setFont(new Font(16));
        symbol.toFront();
        symbol.setMouseTransparent(true);

    }

    public void setOwnerForSocket(Socket socket) {
        socket.setOwnerComponent(this);
    }

    protected void bindSymbol() {
        DoubleProperty coordX = new SimpleDoubleProperty((40 / 2) - symbol.getTabSize() / 2.3);
        DoubleProperty coordY = new SimpleDoubleProperty(40 / 1.6);

        symbol.layoutXProperty().bind(coordX);
        symbol.layoutYProperty().bind(coordY);

        coordX.bind(this.layoutXProperty().add((40 / 2) - symbol.getTabSize() / 2.3));
        coordY.bind(this.layoutYProperty().add(40 / 1.6));
    }

    private void bindInputLine() {
        DoubleProperty startX = new SimpleDoubleProperty(-10);
        DoubleProperty startY = new SimpleDoubleProperty(BLOCK_HEIGHT / 2);
        inputLine = new ElementLine(0, (BLOCK_HEIGHT / 2), -10, (BLOCK_HEIGHT / 2), this.layoutXProperty().add(0), this.layoutYProperty().add(BLOCK_HEIGHT / 2), this.layoutXProperty().subtract(10), this.layoutYProperty().add(BLOCK_HEIGHT / 2));
        connectionInputSocket = new Socket(startX, startY, Socket.Role.Output);
        connectionInputSocket.centerXProperty().bind(this.layoutXProperty().add(- 10));
        connectionInputSocket.centerYProperty().bind(this.layoutYProperty().add(BLOCK_HEIGHT / 2));
        PrimaryPresenter.sockets.add(connectionInputSocket);
    }

    public ArrayList<Socket> getInputSockets() {
        ArrayList<Socket> array = new ArrayList<>();
        if (connectionInputSocket != null) {
            array.add(connectionInputSocket);
        }
        return array;
    }

    public ArrayList<Socket> getOutputSockets() {
        ArrayList<Socket> array = new ArrayList<>();
        if (connectionOutputSocket != null) {
            array.add(connectionOutputSocket);
        }
        return array;
    }

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
                for (VariableBlock block : PrimaryPresenter.blocks) {
                    pointObservers[i][j].update(block);
                    if (connectionInputSocket != null) {
                        pointObservers[i][j].update(connectionInputSocket);
                    }
                    if (connectionOutputSocket != null) {
                        pointObservers[i][j].update(connectionOutputSocket);
                    }
                }
            }
        }
    }

    public ObserverInterface[][] getPointObservers() {
        return pointObservers;
    }

    public void setPointObservers(ObserverInterface[][] pointObservers) {
        this.pointObservers = pointObservers;
    }

    public Socket getConnectionInputSocket() {
        return connectionInputSocket;
    }

    public void setConnectionInputSocket(Socket connectionInputSocket) {
        this.connectionInputSocket = connectionInputSocket;
    }

    public Line getInputLine() {
        return inputLine;
    }

    public void setInputLine(Line inputLine) {
        this.inputLine = inputLine;
    }

    public Socket getConnectionOutputSocket() {
        return connectionOutputSocket;
    }

    public void setConnectionOutputSocket(Socket connectionOutputSocket) {
        this.connectionOutputSocket = connectionOutputSocket;
    }

    public Line getOutputLine() {
        return outputLine;
    }

    public void setOutputLine(Line outputLine) {
        this.outputLine = outputLine;
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

    @Override
    public double getMouseX() {
        return mouseX;
    }

    @Override
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }

    @Override
    public double getMouseY() {
        return mouseY;
    }

    @Override
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    @Override
    public Text getSymbol() {
        return symbol;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setOwnerForOutputSocket() {
        this.getConnectionOutputSocket().setOwnerComponent(this);
    }

    public void setOwnerForInputSockets() {
        this.getConnectionInputSocket().setOwnerComponent(this);
    }

//    public String getVariable() {
//        return variable;
//    }
//
//    public void setVariable(String variable) {
//        this.variable = variable;
//    }
    @Override
    protected void bindOutput() {
        DoubleProperty startX = new SimpleDoubleProperty(BLOCK_WIDTH);
        DoubleProperty startY = new SimpleDoubleProperty(BLOCK_HEIGHT / 2);
        outputLine = new ElementLine(BLOCK_WIDTH, (BLOCK_HEIGHT / 2), (BLOCK_WIDTH + 10), (BLOCK_HEIGHT / 2), this.layoutXProperty().add(BLOCK_WIDTH), this.layoutYProperty().add(BLOCK_HEIGHT / 2), this.layoutXProperty().add(BLOCK_WIDTH + 10), this.layoutYProperty().add(BLOCK_HEIGHT / 2));
        connectionOutputSocket = new Socket(startX, startY, Socket.Role.Output);
        connectionOutputSocket.centerXProperty().bind(this.layoutXProperty().add(BLOCK_WIDTH + 10));
        connectionOutputSocket.centerYProperty().bind(this.layoutYProperty().add(BLOCK_HEIGHT / 2));
        PrimaryPresenter.sockets.add(connectionOutputSocket);
    }

    @Override
    public void setVisualComponentsToFront() {
        this.toFront();
        symbol.toFront();
    }

    @Override
    public void prepareToChangePosition(Pane source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changedPositionReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}