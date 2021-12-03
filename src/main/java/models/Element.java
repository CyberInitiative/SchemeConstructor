package models;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Miroslav Levdikov
 */
public class Element extends Group {

    private final double ELEMENT_WIDTH = 50;
    private final double ELEMENT_HEIGHT = 75;

    private final double CIRCLE_RADIUS = 1;
    private final double CONNECT_ZONE_RADIUS = 6.25;

    private int minimalNumberOfInputs;

    private Shape body = new Rectangle(ELEMENT_WIDTH, ELEMENT_HEIGHT, Color.WHITE);
    private final Circle output = new Circle(CIRCLE_RADIUS);

    private Line outputLine = new Line(ELEMENT_WIDTH, ELEMENT_HEIGHT / 2, ELEMENT_WIDTH + 12.5, ELEMENT_HEIGHT / 2);

    private int identifier = body.hashCode();
    
    private ArrayList<Circle> inputsInElement;
    private ArrayList<Line> inputLines;
    private ArrayList<Circle> connectionInputCircle;
    private Circle inversionDesignation; //обозначение инверсии на элементе;
    private Text symbol = new Text("?");

    private Rectangle mouseBorder;

    private double bodyCorX; //координаты "тела" элемента
    private double bodyCorY;

    private double corX = 0; //координаты всего элемента
    private double corY = 0;

    private double mouseX = 0; //координаты мыши
    private double mouseY = 0;

    private boolean dragging = false;
    private boolean settedOnPane = false;
    
    public void draw(){
        this.setTranslateX(corX);
        this.setTranslateY(corY);
    }
    
    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public ArrayList<Circle> getInputsInElement() {
        return inputsInElement;
    }

    public Rectangle getMouseBorder() {
        return mouseBorder;
    }

    public void setMouseBorder(Rectangle mouseBorder) {
        this.mouseBorder = mouseBorder;
    }

    public Circle getOutput() {
        return output;
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

    public double getCIRCLE_RADIUS() {
        return CIRCLE_RADIUS;
    }

    public int getMinimalNumberOfInputs() {
        return minimalNumberOfInputs;
    }

    public Shape getBody() {
        return body;
    }

    public Line getOutputLine() {
        return outputLine;
    }

    public ArrayList<Line> getInputLines() {
        return inputLines;
    }

    public Text getSymbol() {
        return symbol;
    }

    public double getBodyCorX() {
        return bodyCorX;
    }

    public double getBodyCorY() {
        return bodyCorY;
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

    public boolean isDragging() {
        return dragging;
    }

    public void setMinimalNumberOfInputs(int minimalNumberOfInputs) {
        this.minimalNumberOfInputs = minimalNumberOfInputs;
    }

    public void setInversionDesignation(Circle inversionDesignation) {
        this.inversionDesignation = inversionDesignation;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
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

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public ArrayList<Circle> getConnectionInputCircle() {
        return connectionInputCircle;
    }
    
    public Element() {
        minimalNumberOfInputs = 2;
        inputsInElement = new ArrayList<>();
        inputLines = new ArrayList<>();
        connectionInputCircle = new ArrayList<>();
        createStartInputs();
        configureInputPoints();
        bindEssentialElements();
        bindOptionalElements();
        useNodeVisualDetails();
        configureInputPoints();
        elementMovementEvents();
//        elementEnteredEvents();
    }

    public Element(int minimalNumberOfInputs, Text symbol, Circle inversionDesignation) {
        this.minimalNumberOfInputs = minimalNumberOfInputs;
        this.symbol = symbol;
        this.inversionDesignation = inversionDesignation;
        inputsInElement = new ArrayList<>();
        inputLines = new ArrayList<>();
        connectionInputCircle = new ArrayList<>();
        createStartInputs();
        configureInputPoints();
        bindAllElements();
        useNodeVisualDetails();
        elementMovementEvents();
//        elementEnteredEvents();
    }

    private void useNodeVisualDetails() {
        body.setStroke(Color.BLACK);
        body.setStrokeType(StrokeType.INSIDE);
        body.setStrokeWidth(2.5);
        output.setFill(Color.BLACK);
        output.toFront();
        outputLine.setStrokeWidth(2);
        if (inversionDesignation != null) {
            inversionDesignation.setStrokeType(StrokeType.INSIDE);
            inversionDesignation.setStrokeWidth(1);
            inversionDesignation.setStroke(Color.BLACK);
            inversionDesignation.toFront();
        }
    }
  
    private void bindEssentialElements() {
        getChildren().add(getBody());
        getChildren().add(getOutput());
        getChildren().add(getOutputLine());
        if (symbol != null) {
            getChildren().add(symbol);
            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
            symbol.setFont(new Font("Consolas", 14));
            symbol.toFront();
        }
    }

    public void bindOptionalElements() {
        getChildren().addAll(inputsInElement);
        getChildren().addAll(inputLines);
        getChildren().addAll(connectionInputCircle);

//        if (symbol != null) {
//            getChildren().add(symbol);
//            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
//            symbol.setFont(new Font("Consolas", 14));
//            symbol.toFront();
//        }
        if (inversionDesignation != null) {
            getChildren().add(inversionDesignation);
            inversionDesignation.setStrokeType(StrokeType.INSIDE);
            inversionDesignation.setStrokeWidth(1);
            inversionDesignation.setStroke(Color.BLACK);
            inversionDesignation.relocate((bodyCorX + ELEMENT_WIDTH) - (inversionDesignation.getRadius() + 1), (bodyCorY + ELEMENT_HEIGHT / 2) - inversionDesignation.getRadius());
            inversionDesignation.toFront();
        }
    }

    private void bindAllElements() {
        getChildren().add(body);
        getChildren().add(output);
        getChildren().add(outputLine);
        getChildren().addAll(inputsInElement);
        getChildren().addAll(inputLines);
        getChildren().addAll(connectionInputCircle);

        if (symbol != null) {
            getChildren().add(symbol);
            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
            symbol.setFont(new Font("Consolas", 14));
            symbol.toFront();
        }
        if (inversionDesignation != null) {
            getChildren().add(inversionDesignation);
            inversionDesignation.relocate((bodyCorX + ELEMENT_WIDTH) - (inversionDesignation.getRadius() + 1), (bodyCorY + ELEMENT_HEIGHT / 2) - inversionDesignation.getRadius());
        }
    }

    private void addGraphicalElement(Shape shape) {
        this.getChildren().add(shape);
    }

    public void createStartInputs() {
        for (int i = 0; i < this.minimalNumberOfInputs; i++) {
            inputsInElement.add(new Circle(CIRCLE_RADIUS));
            inputsInElement.get(i).setFill(Color.BLACK);
            inputsInElement.get(i).toFront();
            connectionInputCircle.add(new Circle(CONNECT_ZONE_RADIUS));
            connectionInputCircle.get(i).setFill(Color.rgb(52, 201, 36));
           // connectionInputCircle.get(i).setStrokeType(StrokeType.INSIDE);
            //connectionInputCircle.get(i).setStrokeWidth(0.5);
            //connectionInputCircle.get(i).setStroke(Color.BLACK);
            connectionInputCircle.get(i).setOpacity(0.95);
            connectionInputCircle.get(i).toFront();
        }
        configureInputPoints();
        for (int i = 0; i < inputsInElement.size(); i++) {
            Line line = new Line(inputsInElement.get(i).getLayoutX() - 12.5, inputsInElement.get(i).getLayoutY(), inputsInElement.get(i).getLayoutX(), inputsInElement.get(i).getLayoutY());
            line.setStrokeWidth(2);
            inputLines.add(line);
        }
        configureInputCircles();
    }

    private void addNewInput() {
        Circle newCircle = new Circle(CIRCLE_RADIUS);
        newCircle.setFill(Color.BLACK);
        inputsInElement.add(newCircle);
        configureInputPoints();
        addGraphicalElement(newCircle);
    }

    private void configureInputPoints() {
        this.output.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (output.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - output.getRadius());
        int distance = (int) ELEMENT_HEIGHT / (inputsInElement.size() + 1); //Растояние между точками входа.
        for (int i = 0; i < inputsInElement.size(); i++) {
            inputsInElement.get(i).relocate(this.bodyCorX - (CIRCLE_RADIUS - 1), this.bodyCorY + (distance * (i + 1) - CIRCLE_RADIUS));
        }
    }

    private void configureInputCircles() {
        for (int i = 0; i < connectionInputCircle.size(); i++) {
            connectionInputCircle.get(i).relocate(inputLines.get(i).getStartX() - CONNECT_ZONE_RADIUS, inputLines.get(i).getStartY() - CONNECT_ZONE_RADIUS);
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

    private void testEvent() {
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            this.addNewInput();
        });
    }

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
    public String toString() {
        return "Element " + this.hashCode() + ": location = " + this.getLayoutX() + ": output = " + this.minimalNumberOfInputs;
    }
}
