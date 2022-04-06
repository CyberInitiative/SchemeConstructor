package models.shemeGenerator;

import models.shemeGenerator.SchemeGenerator;
import models.shemeGenerator.SignalRebuilder;
import models.shemeGenerator.RPN_Transformator;
import models.schemeComponents.LogicElement;
import models.schemeComponents.VariableBlock;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.FunctionAnalyzer;
import models.partsOfComponents.Signal;

/**
 *
 * @author Miroslav Levdikov
 */
public class Facade {

    private String function = "!A*D*!(C*!B)+B*!(!C+D)+!(!A+!B)*!C"; //A+B+C*(D+C) //!A*D*!(C*!B)+B*!(!C+D)+!(!A+!B)*!C
    private FunctionAnalyzer analyzer = new FunctionAnalyzer(); //!A*D*!(C*!B)+B*!(!C+D)+!(!A+!B)*!C
    private RPN_Transformator rpn_transformator = new RPN_Transformator();
    private SignalRebuilder rebuilder = new SignalRebuilder();
    private SchemeGenerator generator = new SchemeGenerator();
    private ArrayList<LogicElement> arr;
    private ArrayList<VariableBlock> block;

    public ArrayList<String> buildTheScheme(Pane workPane) {
        String editedFunction = analyzer.supplementExpression(function);
        ArrayList<String> messages = analyzer.checkOnEx(function);
        if (messages.isEmpty()) {
            rpn_transformator.convertToRPN(function);
            String rpn_String = rpn_transformator.getResultAsString();
            ArrayList<Signal> rebuildedOperations = rebuilder.rebuild(rpn_String);
            //drawer.formElementsReworked(decodedOperations);
            //System.out.println(rebuildedOperations);
            for (int i = 0; i < rebuildedOperations.size(); i++) {
                if (rebuildedOperations.get(i).getVariable().length() > 2) {
                    //System.out.println(rebuildedOperations.get(i));
                }
            }
            //System.out.println(drawer.formElementsReworked(decodedOperations));
            analyzer.findAllSignals(rebuildedOperations.get(rebuildedOperations.size() - 1).getVariable());
            analyzer.defineFactualTotalPoints(function);
            //generator.createStartVariableBlocks(analyzer.getFactualTotalPoints());
//            arr = generator.formElements(rebuildedOperations);
//            generator.setPositionForVariableBlocks();
            //generator.connectVarBlocks();
            //elements.addAll(arr);
//            for (VariableBlock block : generator.getBlocks()) {
//                workPane.getChildren().add(block);
//            }
//            for (int j = 0; j < arr.size(); j++) 
//                for (int g = 0; g < arr.get(j).getConnectionInputSockets().size(); g++) {
//                    arr.get(j).getConnectionInputSockets().get(g).setOwnerComponent(arr.get(j));
//                }
//            }

//            ArrayList<Element> createdElements = drawer.createElements(decodedOperations);
//            ArrayList<Element> formedElement = drawer.formElements(createdElements);
            //generator.drawScheme(arr, workPane);
            String successRes = "Logic scheme built successfully;";
            messages.add(successRes);
        }
        return messages;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public ArrayList<LogicElement> getArr() {
        return arr;
    }

    public void setArr(ArrayList<LogicElement> arr) {
        this.arr = arr;
    }

    public ArrayList<VariableBlock> getBlock() {
        return block;
    }

    public void setBlock(ArrayList<VariableBlock> block) {
        this.block = block;
    }

    public SchemeGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(SchemeGenerator generator) {
        this.generator = generator;
    }

}
