package models;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Miroslav Levdikov
 */
public class SignalRebuilder {

    ArrayList<Signal> rebuildedSignals;

    private boolean isOperand(String operand) {
        return operand.matches("[A-Z]+");
    }

    private boolean isOperandAdvanced(String advOperand) {
        boolean check = false;
        String[] stringArray = advOperand.split("");
        for (String s : stringArray) {
            if (isOperand(s)) {
                check = true;
                break;
            }
        }
        return check;
    }

    private void swap(ArrayList<String> arrayList, int indexF, int indexS) {
        String temp;
        temp = arrayList.get(indexF);
        arrayList.set(indexF, arrayList.get(indexS));
        arrayList.set(indexS, temp);
    }

    private boolean isInvertionOperator(ArrayList<String> decodingList, int index) {
        if (decodingList.get(index).equals("!")) {
            for (int j = index; j-- > 0;) {
                if (isOperandAdvanced(decodingList.get(j))) {
                    if (isOperand(decodingList.get(j))) {
                        rebuildedSignals.add(new Signal(decodingList.get(j), null));
                    }
                    swap(decodingList, index, j);
                    decodingList.set(j, decodingList.get(j) + decodingList.get(index));
                    decodingList.set(index, "#");
                    rebuildedSignals.add(new Signal(decodingList.get(j), "!"));
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLogAddOrMultOperator(ArrayList<String> decodingList, int index) {
        if (decodingList.get(index).equals("*") || decodingList.get(index).equals("+")) {
            for (int j = index; j-- > 0;) {
                if (isOperandAdvanced(decodingList.get(j)) && j != 0) {
                    if (isOperandAdvanced(decodingList.get(j - 1))) {
                        if (isOperand(decodingList.get(j - 1))) {
                            rebuildedSignals.add(new Signal(decodingList.get(j - 1), null));
                        }
                        if (isOperand(decodingList.get(j))) {
                            rebuildedSignals.add(new Signal(decodingList.get(j), null));
                        }
                        decodingList.set(j, decodingList.get(j - 1) + decodingList.get(index) + decodingList.get(j));
                        decodingList.set(j - 1, "#");
                        rebuildedSignals.add(new Signal(decodingList.get(j), decodingList.get(index)));
                        decodingList.set(index, "#");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isBrackets(ArrayList<String> decodingList, int index) {
        if (decodingList.get(index).equals(")") && decodingList.get(index + 1).equals("(") && isOperandAdvanced(decodingList.get(index - 1))) {
            decodingList.set(index - 1, decodingList.get(index) + decodingList.get(index - 1) + decodingList.get(index + 1));
            decodingList.set(index, "#");
            decodingList.set(index + 1, "#");
            rebuildedSignals.add(new Signal(decodingList.get(index - 1), null));
            return true;
        }
        return false;
    }

    public ArrayList<Signal> rebuild(String interpretedExpr) {
        String[] stringArray = interpretedExpr.split("");
        ArrayList<String> decodingList = new ArrayList<>(Arrays.asList(stringArray));
        rebuildedSignals = new ArrayList<>();
        for (int i = 0; i < decodingList.size(); i++) {
            if (isInvertionOperator(decodingList, i)) {
                i = 0;
            }
            if (isLogAddOrMultOperator(decodingList, i)) {
                i = 0;
            }
            if (isBrackets(decodingList, i)) {
                i = 0;
            }
            decodingList.removeIf(x -> x.equals("#"));
            //System.out.println("DEC LIST: " + decodingList);
        }

        removeUnnecessaryVars();
        rebuildedSignals.removeIf(x -> x.getVariable().equals("#"));
        return rebuildedSignals;
    }

    private void removeUnnecessaryVars() {
        for (int f = 1; f < rebuildedSignals.size(); f++) {
            String temp;
            for (int s = 0; s < rebuildedSignals.size(); s++) {
                if (rebuildedSignals.get(f).getVariable().contains(rebuildedSignals.get(s).getVariable()) && rebuildedSignals.get(f).getVariable().length() != 2) {
                    temp = rebuildedSignals.get(f).getVariable().replace(rebuildedSignals.get(s).getVariable(), "");
                    if (temp.equals("!")) {
                        rebuildedSignals.get(s).setVariable("#");
                    }
                }
            }
        }
//        for (int f = 1; f < rebuildedSignals.size(); f++) {
//            String temp;
//            for (int s = 0; s < rebuildedSignals.size(); s++) {
//                if (rebuildedSignals.get(f).getVariable().contains(rebuildedSignals.get(s).getVariable())) {
//                    temp = rebuildedSignals.get(f).getVariable().replace(rebuildedSignals.get(s).getVariable(), "");
//                    if (temp.equals(")(")) {
//                        rebuildedSignals.get(s).setVariable("#");
//                    }
//                }
//            }
//        }
    }
}
