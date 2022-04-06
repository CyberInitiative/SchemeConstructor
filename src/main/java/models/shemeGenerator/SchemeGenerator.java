package models.shemeGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Pair;
import models.partsOfComponents.Socket;
import models.WorkspaceManager;

/**
 *
 * @author Miroslav Levdikov
 */
public class SchemeGenerator {

    private WorkspaceManager manager;

    //private HashMap<Socket, Socket> socketMap = new HashMap<>();
//    private Pane pane;
//
//    //Comparator<Element> comparator = (o1, o2) -> o1.getConnectionOutputSocket().getSignal().getLayer().compareTo(o2.getConnectionOutputSocket().getSignal().getLayer());
//    //Comparator<Element> comparator = Comparator.comparing(obj -> obj.getConnectionOutputSocket().getSignal().getLayer());
//
////    public void createStartVariableBlocks(ArrayList<Character> signals) {
////        for (int i = 0; i < signals.size(); i++) {
////            VariableBlock block = new VariableBlock(pointManager.getAllPoints(), false, signals.get(i));
////            block.getConnectionOutputSocket().setSignal(new Signal(signals.get(i).toString(), null));
////            block.setOwnerForOutputSocket();
////            block.setLayoutY(i);
////            //.out.println(block.getConnectionOutputSocket());
////            this.blocks.add(block);
////        }
////    }
//
//    public void setPositionForVariableBlocks() {
//        double indent = calculateIndent(120, this.blocks.size());
////        for (int i = blocks.size(); i-- > 0;) {
////            if (i == blocks.size() - 1) {
////                blocks.get(i).setLayoutX(elems.get(elems.size() - 1).getLayoutX() - 120);
////                blocks.get(i).setLayoutY(500 - (indent - 20));
////                this.blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, 500 - (indent - 20));
////            } else {
////                if (this.blocks.size() > 1) {
////                    blocks.get(i).setLayoutX(elems.get(elems.size() - 1).getLayoutX() - 120);
////                    blocks.get(i).setLayoutY(blocks.get(i + 1).getLayoutY() + 120);
////                    this.blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, this.blocks.get(i + 1).getLayoutY() + 120);
////                }
////            }
////        }
//
//        for (int i = 0; i < this.blocks.size(); i++) {
//            if (i == 0) {
//                //blocks.get(i).setLayoutX(elems.get(elems.size() - 1).getLayoutX() - 120);
//                //blocks.get(i).setLayoutY(500 - (indent - 20));
//                this.blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, 500 - (indent - 20));
//            } else {
//                if (this.blocks.size() > 1) {
//                    //blocks.get(i).setLayoutX(elems.get(elems.size() - 1).getLayoutX() - 120);
//                    //blocks.get(i).setLayoutY(blocks.get(i - 1).getLayoutY() + 120);
//                    this.blocks.get(i).relocate(elems.get(elems.size() - 1).getLayoutX() - 120, this.blocks.get(i - 1).getLayoutY() + 120);
//                }
//            }
//        }
//    }
//
//    public ArrayList<Element> formElements(ArrayList<Signal> rebuildedSignals) {
//        //Создатать элементы по количеству сигналов.
//        for (int i = 0; i < rebuildedSignals.size(); i++) {
//            if (rebuildedSignals.get(i).getVariable().length() != 1) {
//                Element element = new Element(pointManager.getAllPoints(), 2);
//                element.getConnectionOutputSocket().setSignal(rebuildedSignals.get(i));
//                elems.add(element);
//            }
//        }
//        //System.out.println("BEFORE BEFORE: " + elems);
//        for (int i = 0; i < elems.size(); i++) {
//            setConnectionsElements(rebuildedSignals, elems.get(i));
//        }
//        groupByFour(elems);
//        return elems;
//    }
//
//    private void setConnectionsElements(ArrayList<Signal> rebuildedSignals, Element currentElement) {
//        //Установить связи между элементами. В один элемент входит только два сигнала;
//        for (int i = 0; i < rebuildedSignals.size(); i++) {
//            var output = currentElement.getConnectionOutputSocket(); //Сокет выхода элемента;
//            if (rebuildedSignals.get(i).getVariable().equals(output.getSignal().getVariable()) && !rebuildedSignals.get(i).isIncluded()) {
//                if (i > 0) {
//                    for (int j = i; j-- > 0;) {
//                        if (rebuildedSignals.get(i).getVariable().contains(rebuildedSignals.get(j).getVariable()) && !rebuildedSignals.get(j).isIncluded()) {
//                            //j индекс - индекс элемента который входит
//                            rebuildedSignals.get(j).setIncluded(true);
//                            if (rebuildedSignals.get(j).getVariable().length() == 1) {
////                                rebuildedSignals.get(j).setIncluded(true); // don't touch
//                                for (VariableBlock var : blocks) {
//                                    if (var.getConnectionOutputSocket().getSignal().getVariable().equals(rebuildedSignals.get(j).getVariable())) {
////                                        ElementConnectPoint elemConPoint = new ElementConnectPoint(rebuildedSignals.get(j));
//                                        for (Socket socket : currentElement.getConnectionInputSockets()) {
//                                            if (socket.getSignal() == null) {
//                                                socket.setSignal(var.getConnectionOutputSocket().getSignal());
//                                                //System.out.println("VAR: " + var.getConnectionOutputSocket().getSignal());
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                            } else {
//                                for (Socket socket : currentElement.getConnectionInputSockets()) {
//                                    if (socket.getSignal() == null) {
//                                        socket.setSignal(rebuildedSignals.get(j));
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                return;
//            }
//        }
//    }
//
//    private void groupByFourBody(Element elem, int index, ArrayList<Element> arr) {
//        var currOper = elem.getConnectionOutputSocket().getSignal().getOperator();
//        main:
//        for (int i = index; i < arr.size(); i++) {
//            var iEl = arr.get(i);
//            for (int g = 0; g < arr.get(i).getConnectionInputSockets().size(); g++) { //Поиск в массиве точек входа elem
//                var input = iEl.getConnectionInputSockets().get(g).getSignal();
//                if (elem.getConnectionOutputSocket().getSignal() == input) {
//                    if (currOper != null && !currOper.equals("!")) {
//                        if (currOper.equals(arr.get(i).getConnectionOutputSocket().getSignal().getOperator()) && (!arr.get(index).getConnectionOutputSocket().getSignal().isVariableInBrackets() && !arr.get(i).getConnectionOutputSocket().getSignal().isVariableInBrackets()) && elem.getConnectionInputSockets().size() < 4) {
//                            ObservableList<Socket> con = FXCollections.observableArrayList();
//                            con.addAll(elem.getConnectionInputSockets());
//                            for (int e = 0; e < arr.get(i).getConnectionInputSockets().size(); e++) {
//                                if (arr.get(i).getConnectionInputSockets().get(e).getSignal() != elem.getConnectionOutputSocket().getSignal()) {
//                                    Socket socket = new Socket(Socket.Role.Input);
//                                    socket.setSignal(arr.get(i).getConnectionInputSockets().get(e).getSignal());
//                                    //System.out.println("SOCKET: " + socket);
//                                    //con.add(arr.get(i).getConnectionInputSockets().get(e));
//                                    con.add(socket);
//                                }
//                            }
//                            arr.get(i).setConnectionInputSockets(con);
//                            //System.out.println("CON: " + arr.get(i).getConnectionInputSockets().size());
//                            //System.out.println(i + " " + sockets);
//                            elem.setIncluded2(true);
//                            break;
//                        } else {
//                            break main;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void groupByFour(ArrayList<Element> arr) {
//        //System.out.println("BEFORE: " + arr);
//        for (int i = 0; i < arr.size(); i++) {
//            groupByFourBody(arr.get(i), i, arr);
//            checkSameInBrackets(arr.get(i), i, arr);
//        }
//
//        arr.removeIf(x -> x.isIncluded2() == true);
//        arr.removeIf(x -> x.getConnectionOutputSocket().getSignal().getVariable().equals("#"));
//        for (Element elem : arr) {
//            for (Socket soc : elem.getConnectionInputSockets()) {
//                if (soc.getSignal() == null) {
//                    sockets.remove(soc);
//                }
//            }
//            elem.getConnectionInputSockets().removeIf(x -> x.getSignal() == null);
//            elem.setNumberOfInputs(elem.getConnectionInputSockets().size());
//            elem.setUpSockets();
//            //elem.swapInputSockets(); //переработать
//        }
////        for (int i = 0; i < elems.size(); i++) {
////            if (i != elems.size() - 1) {
////                for (int j = 0; j < elems.get(i + 1).getConnectionInputSockets().size(); j++) {
////                    if (elems.get(i).getConnectionOutputSocket().getSignal() == elems.get(i + 1).getConnectionInputSockets().get(j).getSignal()) {
////                        socketMap.put(elems.get(i).getConnectionOutputSocket(), elems.get(i + 1).getConnectionInputSockets().get(j));
////                        break;
////                    }
////                }
////            }
////        }
//        findSignals();
//        findSignalsFromBlocks();
//        setLayers(arr.get(arr.size() - 1), 1, arr);
//        Collections.sort(arr, comparator);
//        setByLayers(arr);
//        elements.addAll(arr);
//        //setConnectionPaths();
//    }
//
//    public double calculateIndent(double indent, int size) {
//        double calcIndent = 0;
//        if (size > 1) {
//            calcIndent = (indent * (size - 1)) / 2;
//        }
//        return calcIndent;
//    }
//
//    public void setByLayers(ArrayList<Element> elems) {
//        ArrayList<ArrayList<Element>> array = new ArrayList<>();
//        ArrayList<Element> currentList = new ArrayList<>();
//        array.add(currentList);
//        for (int i = 0; i < elems.size(); i++) {
//            if (i == 0) {
//                currentList.add(elems.get(i));
//            }
//            if (i > 0) {
//                if (elems.get(i - 1).getConnectionOutputSocket().getSignal().getLayer() != elems.get(i).getConnectionOutputSocket().getSignal().getLayer()) {
//                    ArrayList<Element> list = new ArrayList<>();
//                    array.add(list);
//                    currentList = list;
//                    list.add(elems.get(i));
//                } else {
//                    currentList.add(elems.get(i));
//                }
//            }
//        }
//
//        var firstElem = array.get(0).get(0);
//        firstElem.setLayoutX(700);
//        firstElem.setLayoutY(500);
//
//        for (int j = 1; j < array.size(); j++) {
//            double dist = calculateIndent(120, array.get(j).size());
//            for (int i = 0; i < array.get(j).size(); i++) {
//                if (i == 0) {
//                    array.get(j).get(i).setLayoutY(firstElem.getLayoutY() - dist);
//                    array.get(j).get(i).setLayoutX(firstElem.getLayoutX() - 120 * j);
//                } else {
//                    if (array.get(j).size() > 1) {
//                        array.get(j).get(i).setLayoutY(array.get(j).get(i - 1).getLayoutY() + 120);
//                        array.get(j).get(i).setLayoutX(firstElem.getLayoutX() - 120 * j);
//                    }
//                }
//            }
//        }
//    }
//
//    public void setLayers(Element element, int layer, ArrayList<Element> arr) {
//        element.getConnectionOutputSocket().getSignal().setLayer(layer);
//        var inputs = element.getConnectionInputSockets();
//        if (inputs != null && !inputs.isEmpty()) {
//            for (int i = 0; i < inputs.size(); i++) {
//                for (int j = arr.size(); j-- > 0;) {
//                    if (inputs.get(i).getSignal() == arr.get(j).getConnectionOutputSocket().getSignal()) {
//                        setLayers(arr.get(j), element.getConnectionOutputSocket().getSignal().getLayer() + 1, arr);
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    public void findSignals() {
//        for (int i = elems.size(); i-- > 0;) {
//            for (int j = 0; j < elems.get(i).getConnectionInputSockets().size(); j++) {
//                for (int l = elems.size(); l-- > 0;) {
//                    if (elems.get(l).getConnectionOutputSocket().getSignal() == elems.get(i).getConnectionInputSockets().get(j).getSignal()) {
//                        socketMap.put(elems.get(i).getConnectionInputSockets().get(j), elems.get(l).getConnectionOutputSocket());
//                    }
//                }
//            }
//        }
//    }
//
//    public void findSignalsFromBlocks() {
//        for (VariableBlock block : blocks) {
//            for (Element elem : elems) {
//                for (int i = 0; i < elem.getConnectionInputSockets().size(); i++) {
//                    if (block.getConnectionOutputSocket().getSignal().getVariable().equals(elem.getConnectionInputSockets().get(i).getSignal().getVariable())) {
//                        //System.out.println("BLOCK: " + block.getConnectionOutputSocket().getSignal());
//                        //System.out.println("ELEM: " + elem.getConnectionInputSockets().get(i).getSignal());
//                        socketMap.put(elem.getConnectionInputSockets().get(i), block.getConnectionOutputSocket());
//                    }
//                }
//            }
//        }
//        //System.out.println("MAP! " + socketMap);
//    }
//    //    public void rec(Element elem, int layer, ArrayList<Pair<Integer, ArrayList<Element>>> arrOfPairs) {
//    //        
//    //        ArrayList<Element> layerElem;
//    //        for(int i = 0; i < elem.getConnectionInputSockets().size(); i++){
//    //            //
//    //        }
//    //    }
//    //    public void setCoordinatesForElements(ArrayList<Element> elems) {
//    //        int layer = 0;
//    //        Element lastEl = elems.get(elems.size() - 1);
//    //        ArrayList<Pair<Integer, ArrayList<Element>>> arrayOfPairs = new ArrayList<>();
//    //        for (int i = 0; i < lastEl.getConnectionInputSockets().size(); i++) {
//    //            ArrayList<Element> curEl = new ArrayList<>();
//    //            layer++;
//    //            Pair<Integer, ArrayList<Element>> pair = new Pair<>(layer, curEl);
//    //            arrayOfPairs.add(pair);
//    //        }
//    //    }
//
//    public void setConnectionPaths() {
//        int i = 0;
//        main:
//        for (Element elem : elems) {
//            i++;
//            for (Socket socket : elem.getConnectionInputSockets()) {
//                if (socketMap.containsKey(socket)) {
//                    Socket curSoc = socketMap.get(socket);
//                    Connector connector = new Connector(socket, curSoc);
//                    connector.registerComponents();
//                    var firstEnd = connector.getStartConnectionAnchor();
//                    var secondEnd = connector.getEndConnectionAnchor();
//                    //System.out.println("Conector" + connector);
//                    this.connectors.add(connector);
//                    firstEnd.notifyObservers();
//                    secondEnd.notifyObservers();
//                    firstEnd.requestConnectionLine().setVisible(false);
//
//                    if ((firstEnd.getConnectedSocket() != null && firstEnd.getConnectedSocket().getMainConnectedAnchor() != firstEnd)) {
//                        var clP = firstEnd.getConnectedSocket().getMainConnectedAnchor().getMediator().getConnectionPath().getClosestPoint(secondEnd.getCenterX(), secondEnd.getCenterY());
//                        ConnectionPath path = new ConnectionPath(secondEnd, clP, pane);
//                        firstEnd.getMediator().registerPath(path);
//                    } else if ((secondEnd.getConnectedSocket() != null && secondEnd.getConnectedSocket().getMainConnectedAnchor() != secondEnd)) {
//                        var clP = secondEnd.getConnectedSocket().getMainConnectedAnchor().getMediator().getConnectionPath().getClosestPoint(firstEnd.getCenterX(), firstEnd.getCenterY());
//                        ////System.out.println("CLP: " + clP);
//                        ConnectionPath path = new ConnectionPath(firstEnd, clP, pane);
//                        secondEnd.getMediator().registerPath(path);
//                    } else {
//                        //System.out.println("here");
//                        //System.out.println("PAN: " + pane);
//                        ConnectionPath path = new ConnectionPath(firstEnd, secondEnd, pane);
//                        firstEnd.getMediator().registerPath(path);
//                        //System.out.println("LINE: " + path.getPathPolyline());
//                        //pane.getChildren().add(path.getPathPolyline());
//                    }
//                }
//            }
//        }
//    }
//
//    private void checkSameInBrackets(Element elem, int index, ArrayList<Element> arr) {
//        if (index + 1 != arr.size() + 1) {
//            for (int f = index + 1; f < arr.size(); f++) {
//                String temp;
//                if (arr.get(f).getConnectionOutputSocket().getSignal().getVariable().contains(elem.getConnectionOutputSocket().getSignal().getVariable())) {
//                    temp = arr.get(f).getConnectionOutputSocket().getSignal().getVariable().replace(elem.getConnectionOutputSocket().getSignal().getVariable(), "");
//                    if (temp.equals(")(")) {
//                        //System.out.println("HERE");
//                        //System.out.println("the elem " + elem);
//                        arr.get(f).getConnectionOutputSocket().getSignal().setOperator(elem.getConnectionOutputSocket().getSignal().getOperator());
//                        //System.out.println("ELL: " + elem.getConnectionInputSockets());
//                        arr.get(f).setConnectionInputSockets(elem.getConnectionInputSockets());
//                        //System.out.println("ARR: " + arr.get(f).getConnectionInputSockets());
//                        //arr.get(f).setConnectionOutputSocket(elem.getConnectionOutputSocket());
//                        elem.getConnectionOutputSocket().getSignal().setVariable("#");
//                    }
//                }
//            }
//        }
//    }
//    public Pane getPane() {
//        return pane;
//    }
//
//    public void setPane(Pane pane) {
//        this.pane = pane;
//    }
//
//    /*
//    DO NOT REMOVE;
//     */
////    private void rebuildInComplicatedElement(ArrayList<Element> arr) {
////        for (int i = arr.size(); --i >= 0;) {
////            ArrayList<Signal> con = new ArrayList<>();
////            for (int j = 0; j < arr.get(i).getConnectionInputSockets().size(); j++) { //Проход чоерез точки входа элемента
////                var currEl = arr.get(i).getConnectionOutputSocket().getSignal().getOperator(); //Оператор текущего оператора
////                var indEl = arr.get(i).getConnectionInputSockets().get(j).getSignal().getOperator(); //Оператор текущей входной точки
////                if ((currEl != null && !currEl.equals("!")) && currEl.equals(indEl) && !arr.get(i).isIncluded()) {
////                    //Если операторы равны, то ведем поиск
////                    for (int q = i; --q >= 0;) {
////                        //Найти, входят ли выходные сигналы элементов в входные сигналы элемента i;
////                        //q - элемент до i
////                        if (arr.get(q).getConnectionOutputSocket().getSignal().equals(arr.get(i).getConnectionInputSockets().get(j).getSignal())) {
////                            innerRecursiveMethod(arr, arr.get(q), con, q);
////                            arr.get(q).setIncluded(true);
////                            break;
////                        }
////                    }
////                } else {
////                    con.add(0, arr.get(i).getConnectionInputSockets().get(j).getSignal());
////                }
////            }
////            //
//////            for (Signal signal : con) {
//////                for (Socket socket : arr.get(i).getConnectionInputSockets()) {
//////                    socket.setSignal(signal);
//////                }
//////            }
////
////        }
////        arr.removeIf(x -> x.isIncluded() == true);
////    }
////
////    private void innerRecursiveMethod(ArrayList<Element> arr, Element currentElement, ArrayList<Signal> con, int index) {
////        for (int j = 0; j < currentElement.getConnectionInputSockets().size(); j++) {
////            var currEl = currentElement.getConnectionOutputSocket().getSignal().getOperator();
////            var indEl = currentElement.getConnectionInputSockets().get(j).getSignal().getOperator();
////            if ((currEl != null && !currEl.equals("!")) && currEl.equals(indEl) && !currentElement.isIncluded()) {
////                for (int q = index; --q >= 0;) {
////                    if (arr.get(q).getConnectionOutputSocket().getSignal().equals(currentElement.getConnectionInputSockets().get(j).getSignal())) {
////                        innerRecursiveMethod(arr, arr.get(q), con, q);
////                        arr.get(q).setIncluded(true);
////                        break;
////                    }
////                }
////            } else {
////                con.add(0, currentElement.getConnectionInputSockets().get(j).getSignal());
////            }
////        }
////    }
//    public ArrayList<VariableBlock> getBlocks() {
//        return blocks;
//    }
//
//    public void setBlocks(ArrayList<VariableBlock> blocks) {
//        this.blocks = blocks;
//    }
//
//    public ArrayList<Connector> getConnectors() {
//        return connectors;
//    }
//
//    public void setConnectors(ArrayList<Connector> connectors) {
//        this.connectors = connectors;
//    }
}


/*
for (int b = currentElement.getConnectionInputSockets().size(); b-- > 0;) {
                                            if (currentElement.getConnectionInputSockets().get(b).getSignal() == null) {
                                                currentElement.getConnectionInputSockets().get(b).setSignal(rebuildedSignals.get(j));
                                                break;
                                            }
                                        }
 */
