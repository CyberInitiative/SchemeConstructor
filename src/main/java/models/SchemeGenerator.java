package models;

import static com.gluonapplication.views.PrimaryPresenter.elements;
import static com.gluonapplication.views.PrimaryPresenter.pointManager;
import static com.gluonapplication.views.PrimaryPresenter.sockets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;
import javafx.util.Pair;

/**
 *
 * @author Miroslav Levdikov
 */
public class SchemeGenerator {
    
    private ArrayList<VariableBlock> blocks = new ArrayList<>();
    private ArrayList<Element> elems = new ArrayList<>();
    
    private HashMap<Socket, Socket> socketMap = new HashMap<>();

    //Comparator<Element> comparator = (o1, o2) -> o1.getConnectionOutputSocket().getSignal().getLayer().compareTo(o2.getConnectionOutputSocket().getSignal().getLayer());
    Comparator<Element> comparator = Comparator.comparing(obj -> obj.getConnectionOutputSocket().getSignal().getLayer());
    
    public void createStartVariableBlocks(ArrayList<Character> signals) {
        for (int i = 0; i < signals.size(); i++) {
            VariableBlock block = new VariableBlock(pointManager.getAllPoints(), false, signals.get(i));
            blocks.add(block);
        }
    }
    
    public void buildStartVariableBlocks() {
        /*
        Создание блоков переменных для каждого обнаруженного сигнала в формуле;
         */
        double indent = calculateIndent(120, blocks.size());
        for (int i = 0; i < blocks.size(); i++) {
            if (i == 0) {
                blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, 500 - (indent - 20));
            } else {
                if (blocks.size() > 1) {
                    blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, blocks.get(i - 1).getLayoutY() + 120);
                }
            }
        }
    }
    
    public ArrayList<Element> formElements(ArrayList<Signal> rebuildedSignals, ArrayList<Character> signals) {
        //Создатать элементы по количеству сигналов.
        for (int i = 0; i < rebuildedSignals.size(); i++) {
            if (rebuildedSignals.get(i).getVariable().length() != 1) {
                Element element = new Element(pointManager.getAllPoints(), 2);
                element.getConnectionOutputSocket().setSignal(rebuildedSignals.get(i));
                elems.add(element);
            }
        }
        //System.out.println("BEFORE BEFORE: " + elems);
        for (int i = 0; i < elems.size(); i++) {
            setConnectionsElements(rebuildedSignals, elems.get(i), signals);
        }
        groupByFour(elems);
        return elems;
    }
    
    private void setConnectionsElements(ArrayList<Signal> rebuildedSignals, Element currentElement, ArrayList<Character> signals) {
        //Установить связи между элементами. В один элемент входит только два сигнала;
        for (int i = 0; i < rebuildedSignals.size(); i++) {
            var output = currentElement.getConnectionOutputSocket(); //Сокет выхода элемента;
            if (rebuildedSignals.get(i).getVariable().equals(output.getSignal().getVariable()) && !rebuildedSignals.get(i).isIncluded()) {
                if (i > 0) {
                    for (int j = i; j-- > 0;) {
                        if (rebuildedSignals.get(i).getVariable().contains(rebuildedSignals.get(j).getVariable()) && !rebuildedSignals.get(j).isIncluded()) {
                            //j индекс - индекс элемента который входит
                            rebuildedSignals.get(j).setIncluded(true);
                            if (rebuildedSignals.get(j).getVariable().length() == 1) {
//                                rebuildedSignals.get(j).setIncluded(true); // don't touch
                                for (Character signal : signals) {
                                    if (signal.toString().equals(rebuildedSignals.get(j).getVariable())) {
//                                        ElementConnectPoint elemConPoint = new ElementConnectPoint(rebuildedSignals.get(j));
                                        for (Socket socket : currentElement.getConnectionInputSockets()) {
                                            if (socket.getSignal() == null) {
                                                socket.setSignal(rebuildedSignals.get(j));
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                for (Socket socket : currentElement.getConnectionInputSockets()) {
                                    if (socket.getSignal() == null) {
                                        socket.setSignal(rebuildedSignals.get(j));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                return;
            }
        }
    }

//    private void buildComplicatedElement(ArrayList<Element> elements) {
//        for (int i = elements.size(); --i >= 0;) {
//            ArrayList<Signal> signals = new ArrayList<>();
//            for (int j = 0; j < elements.get(i).getConnectionInputSockets().size(); j++) {
//                var currElOp = elements.get(i).getConnectionOutputSocket().getSignal().getOperator();
//                var inpCurElOP = elements.get(i).getConnectionInputSockets().get(j).getSignal().getOperator();//Оператор элемента i
//                if (currElOp != null && !currElOp.equals("!") && currElOp.equals(inpCurElOP) && !elements.get(i).isIncluded()) {
//                    
//                }
//            }
//        }
//    }
    private void groupByFourBody(Element elem, int index, ArrayList<Element> arr) {
        var currOper = elem.getConnectionOutputSocket().getSignal().getOperator();
        main:
        for (int i = index; i < arr.size(); i++) {
            var iEl = arr.get(i);
            for (int g = 0; g < arr.get(i).getConnectionInputSockets().size(); g++) { //Поиск в массиве точек входа elem
                var input = iEl.getConnectionInputSockets().get(g).getSignal();
                if (elem.getConnectionOutputSocket().getSignal() == input) {
                    if (currOper != null && !currOper.equals("!")) {
                        if (currOper.equals(arr.get(i).getConnectionOutputSocket().getSignal().getOperator()) && (!arr.get(index).getConnectionOutputSocket().getSignal().isVariableInBrackets() && !arr.get(i).getConnectionOutputSocket().getSignal().isVariableInBrackets()) && elem.getConnectionInputSockets().size() < 4) {
                            ObservableList<Socket> con = FXCollections.observableArrayList();
                            con.addAll(elem.getConnectionInputSockets());
                            for (int e = 0; e < arr.get(i).getConnectionInputSockets().size(); e++) {
                                if (arr.get(i).getConnectionInputSockets().get(e).getSignal() != elem.getConnectionOutputSocket().getSignal()) {
                                    Socket socket = new Socket(Socket.Role.Input);
                                    socket.setSignal(arr.get(i).getConnectionInputSockets().get(e).getSignal());
                                    //System.out.println("SOCKET: " + socket);
                                    //con.add(arr.get(i).getConnectionInputSockets().get(e));
                                    con.add(socket);
                                }
                            }
                            arr.get(i).setConnectionInputSockets(con);
                            //System.out.println("CON: " + arr.get(i).getConnectionInputSockets().size());
                            //System.out.println(i + " " + sockets);
                            elem.setIncluded2(true);
                            break;
                        } else {
                            break main;
                        }
                    }
                }
            }
        }
    }
    
    private void groupByFour(ArrayList<Element> arr) {
        //System.out.println("BEFORE: " + arr);
        for (int i = 0; i < arr.size(); i++) {
            groupByFourBody(arr.get(i), i, arr);
            checkSameInBrackets(arr.get(i), i, arr);
        }
        
        arr.removeIf(x -> x.isIncluded2() == true);
        arr.removeIf(x -> x.getConnectionOutputSocket().getSignal().getVariable().equals("#"));
        for (Element elem : arr) {
            for (Socket soc : elem.getConnectionInputSockets()) {
                if (soc.getSignal() == null) {
                    sockets.remove(soc);
                }
            }
            elem.getConnectionInputSockets().removeIf(x -> x.getSignal() == null);
            elem.setNumberOfInputs(elem.getConnectionInputSockets().size());
            elem.removeLines();
        }
        
        for (int j = 0; j < arr.size(); j++) {
            arr.get(j).setOwnerForAllSockets();
        }
        for (int i = 0; i < elems.size(); i++) {
            if (i != elems.size() - 1) {
                for (int j = 0; j < elems.get(i + 1).getConnectionInputSockets().size(); j++) {
                    if (elems.get(i).getConnectionOutputSocket().getSignal() == elems.get(i + 1).getConnectionInputSockets().get(j).getSignal()) {
                        socketMap.put(elems.get(i).getConnectionOutputSocket(), elems.get(i + 1).getConnectionInputSockets().get(j));
                        break;
                    }
                }
            }
        }
        connectVarBlocks();
        setLayers(arr.get(arr.size() - 1), 1, arr);
        Collections.sort(arr, comparator);
        setByLayers(arr);
    }
    
    public double calculateIndent(double indent, int size) {
        double calcIndent = 0;
        if (size > 1) {
            calcIndent = (indent * (size - 1)) / 2;
        }
        return calcIndent;
    }
    
    public void setByLayers(ArrayList<Element> elems) {
        ArrayList<ArrayList<Element>> array = new ArrayList<>();
        ArrayList<Element> currentList = new ArrayList<>();
        array.add(currentList);
        for (int i = 0; i < elems.size(); i++) {
            if (i == 0) {
                currentList.add(elems.get(i));
            }
            if (i > 0) {
                if (elems.get(i - 1).getConnectionOutputSocket().getSignal().getLayer() != elems.get(i).getConnectionOutputSocket().getSignal().getLayer()) {
                    ArrayList<Element> list = new ArrayList<>();
                    array.add(list);
                    currentList = list;
                    list.add(elems.get(i));
                } else {
                    currentList.add(elems.get(i));
                }
            }
        }
        
        var firstElem = array.get(0).get(0);
        firstElem.setLayoutX(700);
        firstElem.setLayoutY(500);
        
        for (int j = 1; j < array.size(); j++) {
            double dist = calculateIndent(120, array.get(j).size());
            for (int i = 0; i < array.get(j).size(); i++) {
                if (i == 0) {
                    array.get(j).get(i).setLayoutY(firstElem.getLayoutY() - dist);
                    array.get(j).get(i).setLayoutX(firstElem.getLayoutX() - 120 * j);
                } else {
                    if (array.get(j).size() > 1) {
                        array.get(j).get(i).setLayoutY(array.get(j).get(i - 1).getLayoutY() + 120);
                        array.get(j).get(i).setLayoutX(firstElem.getLayoutX() - 120 * j);
                    }
                }
            }
        }
    }
    
    private void connectElements() {
        for (int i = 0; i < elems.size(); i++) {
            for (int j = 0; j < elems.get(i).getConnectionInputSockets().size(); j++) {
                
            }
        }
    }
    
    public void setLayers(Element element, int layer, ArrayList<Element> arr) {
        element.getConnectionOutputSocket().getSignal().setLayer(layer);
        var inputs = element.getConnectionInputSockets();
        if (inputs != null && !inputs.isEmpty()) {
            for (int i = 0; i < inputs.size(); i++) {
                for (int j = arr.size(); j-- > 0;) {
                    if (inputs.get(i).getSignal() == arr.get(j).getConnectionOutputSocket().getSignal()) {
                        setLayers(arr.get(j), element.getConnectionOutputSocket().getSignal().getLayer() + 1, arr);
                        break;
                    }
                }
            }
        }
    }
    
    public void connectVarBlocks() {
        System.out.println("BLOCKS " + blocks.size());
        for (VariableBlock block : blocks) {
            for (Element elem : elems) {
                for (int i = 0; i < elem.getConnectionInputSockets().size(); i++) {
                    //System.out.println(elem.getConnectionInputSockets().get(i));
                    //System.out.println("BLOCK: " + block.getSymbol().getText());
                    //System.out.println("ELEM: " + elem.getConnectionInputSockets().get(i).getSignal().getVariable());
                    if (block.getSymbol().getText() != null && elem.getConnectionInputSockets().get(i).getSignal() != null) {
//                        if (block.getSymbol().getText().equals(elem.getConnectionInputSockets().get(i).getSignal().getVariable())) {
//                            socketMap.put(elem.getConnectionInputSockets().get(i), block.getConnectionOutputSocket());
//                        }
                    }
                }
            }
        }
        System.out.println("MAP! " + socketMap);
    }
    //    public void rec(Element elem, int layer, ArrayList<Pair<Integer, ArrayList<Element>>> arrOfPairs) {
    //        
    //        ArrayList<Element> layerElem;
    //        for(int i = 0; i < elem.getConnectionInputSockets().size(); i++){
    //            //
    //        }
    //    }
    //    public void setCoordinatesForElements(ArrayList<Element> elems) {
    //        int layer = 0;
    //        Element lastEl = elems.get(elems.size() - 1);
    //        ArrayList<Pair<Integer, ArrayList<Element>>> arrayOfPairs = new ArrayList<>();
    //        for (int i = 0; i < lastEl.getConnectionInputSockets().size(); i++) {
    //            ArrayList<Element> curEl = new ArrayList<>();
    //            layer++;
    //            Pair<Integer, ArrayList<Element>> pair = new Pair<>(layer, curEl);
    //            arrayOfPairs.add(pair);
    //        }
    //    }

    private void checkSameInBrackets(Element elem, int index, ArrayList<Element> arr) {
        if (index + 1 != arr.size() + 1) {
            for (int f = index + 1; f < arr.size(); f++) {
                String temp;
                if (arr.get(f).getConnectionOutputSocket().getSignal().getVariable().contains(elem.getConnectionOutputSocket().getSignal().getVariable())) {
                    temp = arr.get(f).getConnectionOutputSocket().getSignal().getVariable().replace(elem.getConnectionOutputSocket().getSignal().getVariable(), "");
                    if (temp.equals(")(")) {
                        //System.out.println("HERE");
                        //System.out.println("the elem " + elem);
                        arr.get(f).getConnectionOutputSocket().getSignal().setOperator(elem.getConnectionOutputSocket().getSignal().getOperator());
                        //System.out.println("ELL: " + elem.getConnectionInputSockets());
                        arr.get(f).setConnectionInputSockets(elem.getConnectionInputSockets());
                        //System.out.println("ARR: " + arr.get(f).getConnectionInputSockets());
                        //arr.get(f).setConnectionOutputSocket(elem.getConnectionOutputSocket());
                        elem.getConnectionOutputSocket().getSignal().setVariable("#");
                    }
                }
            }
        }
    }

    /*
    DO NOT REMOVE;
     */
//    private void rebuildInComplicatedElement(ArrayList<Element> arr) {
//        for (int i = arr.size(); --i >= 0;) {
//            ArrayList<Signal> con = new ArrayList<>();
//            for (int j = 0; j < arr.get(i).getConnectionInputSockets().size(); j++) { //Проход чоерез точки входа элемента
//                var currEl = arr.get(i).getConnectionOutputSocket().getSignal().getOperator(); //Оператор текущего оператора
//                var indEl = arr.get(i).getConnectionInputSockets().get(j).getSignal().getOperator(); //Оператор текущей входной точки
//                if ((currEl != null && !currEl.equals("!")) && currEl.equals(indEl) && !arr.get(i).isIncluded()) {
//                    //Если операторы равны, то ведем поиск
//                    for (int q = i; --q >= 0;) {
//                        //Найти, входят ли выходные сигналы элементов в входные сигналы элемента i;
//                        //q - элемент до i
//                        if (arr.get(q).getConnectionOutputSocket().getSignal().equals(arr.get(i).getConnectionInputSockets().get(j).getSignal())) {
//                            innerRecursiveMethod(arr, arr.get(q), con, q);
//                            arr.get(q).setIncluded(true);
//                            break;
//                        }
//                    }
//                } else {
//                    con.add(0, arr.get(i).getConnectionInputSockets().get(j).getSignal());
//                }
//            }
//            //
////            for (Signal signal : con) {
////                for (Socket socket : arr.get(i).getConnectionInputSockets()) {
////                    socket.setSignal(signal);
////                }
////            }
//
//        }
//        arr.removeIf(x -> x.isIncluded() == true);
//    }
//
//    private void innerRecursiveMethod(ArrayList<Element> arr, Element currentElement, ArrayList<Signal> con, int index) {
//        for (int j = 0; j < currentElement.getConnectionInputSockets().size(); j++) {
//            var currEl = currentElement.getConnectionOutputSocket().getSignal().getOperator();
//            var indEl = currentElement.getConnectionInputSockets().get(j).getSignal().getOperator();
//            if ((currEl != null && !currEl.equals("!")) && currEl.equals(indEl) && !currentElement.isIncluded()) {
//                for (int q = index; --q >= 0;) {
//                    if (arr.get(q).getConnectionOutputSocket().getSignal().equals(currentElement.getConnectionInputSockets().get(j).getSignal())) {
//                        innerRecursiveMethod(arr, arr.get(q), con, q);
//                        arr.get(q).setIncluded(true);
//                        break;
//                    }
//                }
//            } else {
//                con.add(0, currentElement.getConnectionInputSockets().get(j).getSignal());
//            }
//        }
//    }
    public ArrayList<VariableBlock> getBlocks() {
        return blocks;
    }
    
    public void setBlocks(ArrayList<VariableBlock> blocks) {
        this.blocks = blocks;
    }
}

/*
for (int b = currentElement.getConnectionInputSockets().size(); b-- > 0;) {
                                            if (currentElement.getConnectionInputSockets().get(b).getSignal() == null) {
                                                currentElement.getConnectionInputSockets().get(b).setSignal(rebuildedSignals.get(j));
                                                break;
                                            }
                                        }
 */
