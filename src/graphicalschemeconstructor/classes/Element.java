package graphicalschemeconstructor.classes;

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
public class Element extends Group implements Cloneable {

    private final double ELEMENT_WIDTH = 50;
    private final double ELEMENT_HEIGHT = 70;

    private final double CIRCLE_RADIUS = 1;

    private int minimalNumberOfInputs;

    private Shape body = new Rectangle(ELEMENT_WIDTH, ELEMENT_HEIGHT, Color.WHITE);
    private Circle output = new Circle(CIRCLE_RADIUS);

    private Line outputLine = new Line(ELEMENT_WIDTH, ELEMENT_HEIGHT / 2, ELEMENT_WIDTH + 15, ELEMENT_HEIGHT / 2);

    private ArrayList<Circle> inputs;
    private ArrayList<Line> inputLines;
    private Circle inversionDesignation; //обозначение инверсии на элементе;
    private Text symbol; //символ операции элемента
    private Integer identifier;

    private double bodyCorX; //координаты "тела" элемента
    private double bodyCorY;

    private double corX = 0; //координаты всего элемента
    private double corY = 0;

    private double mouseX = 0; //координаты мыши
    private double mouseY = 0;

    private boolean dragging = false;

    public void setElement(Element element) {;
        if (this.inversionDesignation != null) {
            this.setInversionDesignation(new Circle(element.getInversionDesignation().getRadius(), element.getInversionDesignation().getFill()));
        } else {
            this.setInversionDesignation(null);
        }
        if (this.symbol != null) {
            this.setSymbol(new Text(element.getSymbol().getText()));
        } else {
            this.setSymbol(null);
        }
        this.setMinimalNumberOfInputs(element.getMinimalNumberOfInputs());
        this.createStartInputs();
        this.configureInputPoints();
        this.bindGraphicalEl();
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public ArrayList<Circle> getInputs() {
        return inputs;
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

    public void setBody(Shape body) {
        this.body = body;
    }

    public void setOutput(Circle output) {
        this.output = output;
    }

    public void setOutputLine(Line outputLine) {
        this.outputLine = outputLine;
    }

    public void setInputs(ArrayList<Circle> inputs) {
        this.inputs = inputs;
    }

    public void setInputLines(ArrayList<Line> inputLines) {
        this.inputLines = inputLines;
    }

    public void setInversionDesignation(Circle inversionDesignation) {
        this.inversionDesignation = inversionDesignation;
    }

    public void setSymbol(Text symbol) {
        this.symbol = symbol;
    }

    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    public void setBodyCorX(double bodyCorX) {
        this.bodyCorX = bodyCorX;
    }

    public void setBodyCorY(double bodyCorY) {
        this.bodyCorY = bodyCorY;
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

    public Element() {
        this.body.setStroke(Color.BLACK);
        this.body.setStrokeType(StrokeType.INSIDE);
        this.body.setStrokeWidth(2.5);
        this.output.setFill(Color.BLACK);
        this.output.toFront();
        this.inputs = new ArrayList<>();
        this.inputLines = new ArrayList<>();
        this.identifier = this.hashCode();
        this.outputLine.setStrokeWidth(2);
        this.configureInputPoints();
        this.bindEssentialElements();
        elementMovementEvents();
        elementEnteredEvents();
    }

    public Element(Circle inversionDesignation, Text symbol, int minimalNumberOfInputs) {
        this.minimalNumberOfInputs = minimalNumberOfInputs;
        this.body.setStroke(Color.BLACK);
        this.body.setStrokeType(StrokeType.INSIDE);
        this.body.setStrokeWidth(2.5);
        this.output.setFill(Color.BLACK);
        this.output.toFront();
        this.inversionDesignation = inversionDesignation;
        this.symbol = symbol;
        this.inputs = new ArrayList<>();
        this.inputLines = new ArrayList<>();
        this.identifier = this.hashCode();
        this.outputLine.setStrokeWidth(2);
        this.createStartInputs();
        this.configureInputPoints();
        this.bindAllElements();
        elementMovementEvents();
        elementEnteredEvents();
    }

    private void bindGraphicalEl() {
//        this.getChildren().add(body);
        this.getChildren().add(output);
        this.getChildren().add(outputLine);
        this.getChildren().addAll(inputs);
        this.getChildren().addAll(inputLines);

        if (this.symbol != null) {
            this.getChildren().add(symbol);
            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
            symbol.setFont(new Font("Consolas", 14));
            symbol.toFront();
        }
        if (this.inversionDesignation != null) {
            this.getChildren().add(inversionDesignation);
            this.inversionDesignation.setStrokeType(StrokeType.INSIDE);
            this.inversionDesignation.setStrokeWidth(1);
            this.inversionDesignation.setStroke(Color.BLACK);
            this.inversionDesignation.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (this.inversionDesignation.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - this.inversionDesignation.getRadius());
            inversionDesignation.toFront();
        }
    }

    private void bindEssentialElements() {
        this.getChildren().add(body);
        this.getChildren().add(output);
        this.getChildren().add(outputLine);
    }

    private void bindOptionalElements() {
        this.getChildren().addAll(inputs);
        this.getChildren().addAll(inputLines);

        if (this.symbol != null) {
            this.getChildren().add(symbol);
            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
            symbol.setFont(new Font("Consolas", 14));
            symbol.toFront();
        }
        if (this.inversionDesignation != null) {
            this.getChildren().add(inversionDesignation);
            this.inversionDesignation.setStrokeType(StrokeType.INSIDE);
            this.inversionDesignation.setStrokeWidth(1);
            this.inversionDesignation.setStroke(Color.BLACK);
            this.inversionDesignation.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (this.inversionDesignation.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - this.inversionDesignation.getRadius());
            inversionDesignation.toFront();
        }
    }

    private void bindAllElements() {
        this.getChildren().add(body);
        this.getChildren().add(output);
        this.getChildren().add(outputLine);
        this.getChildren().addAll(inputs);
        this.getChildren().addAll(inputLines);

        if (this.symbol != null) {
            this.getChildren().add(symbol);
            symbol.relocate((ELEMENT_WIDTH / 2) - symbol.getTabSize() / 2, ELEMENT_HEIGHT / 8);
            symbol.setFont(new Font("Consolas", 14));
            symbol.toFront();
        }
        if (this.inversionDesignation != null) {
            this.getChildren().add(inversionDesignation);
            this.inversionDesignation.setStrokeType(StrokeType.INSIDE);
            this.inversionDesignation.setStrokeWidth(1);
            this.inversionDesignation.setStroke(Color.BLACK);
            this.inversionDesignation.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (this.inversionDesignation.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - this.inversionDesignation.getRadius());
            inversionDesignation.toFront();
        }
    }

    private void addGraphicalElement(Shape shape) {
        this.getChildren().add(shape);
    }

    private void createStartInputs() {
        for (int i = 0; i < this.minimalNumberOfInputs; i++) {
            inputs.add(new Circle(CIRCLE_RADIUS));
            inputs.get(i).setFill(Color.BLACK);
            inputs.get(i).toFront();
        }

        configureInputPoints();
        for (int i = 0; i < inputs.size(); i++) {
            Line line = new Line(inputs.get(i).getLayoutX() - 15, inputs.get(i).getLayoutY(), inputs.get(i).getLayoutX(), inputs.get(i).getLayoutY());
            line.setStrokeWidth(2);
            inputLines.add(line);
        }
    }

    private void addNewInput() {
        Circle newCircle = new Circle(CIRCLE_RADIUS);
        newCircle.setFill(Color.BLACK);
        this.inputs.add(newCircle);
        this.configureInputPoints();
        this.addGraphicalElement(newCircle);
    }

    private void configureInputPoints() {
        this.output.relocate((this.bodyCorX + this.ELEMENT_WIDTH) - (output.getRadius() + 1), (this.bodyCorY + this.ELEMENT_HEIGHT / 2) - output.getRadius());
        int distance = (int) ELEMENT_HEIGHT / (inputs.size() + 1); //Растояние между точками входа.
        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).relocate(this.bodyCorX - (CIRCLE_RADIUS - 1), this.bodyCorY + (distance * (i + 1) - CIRCLE_RADIUS));
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

        onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double offsetX = event.getSceneX() - mouseX; //смещение по X
                double offsetY = event.getSceneY() - mouseY;

                corX += offsetX;
                corY += offsetY;

                double scaledX = corX;
                double scaledY = corY;

                setLayoutX(scaledX);
                setLayoutY(scaledY);

                dragging = true;

                mouseX = event.getSceneX();
                mouseY = event.getSceneY();

                event.consume();
            }
        });

        onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragging = false;
            }
        });
    }

    private void testEvent() {
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            this.addNewInput();
        });
    }

    private void elementEnteredEvents() {
        onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getTarget() instanceof Circle) {
                    System.out.println("Circle!");
                }
            }
        });
    }

    public Element copy(int id) {
        Element cloneElement = new Element();
        if (this.inversionDesignation != null) {
            cloneElement.setInversionDesignation(new Circle(this.getInversionDesignation().getRadius(), this.getInversionDesignation().getFill()));
        } else {
            cloneElement.setInversionDesignation(null);
        }
        if (this.symbol != null) {
            cloneElement.setSymbol(new Text(this.getSymbol().getText()));
        } else {
            cloneElement.setSymbol(null);
        }
        cloneElement.setMinimalNumberOfInputs(this.getMinimalNumberOfInputs());
        cloneElement.createStartInputs();
        cloneElement.bindOptionalElements();
        cloneElement.setId(String.valueOf(id));
        return cloneElement;
    }

    @Override
    public String toString() {
        return "Element " + this.hashCode() + ": location = " + this.getLayoutX() + ": output = " + this.minimalNumberOfInputs;
    }
}
