package models;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Miroslav Levdikov
 */
public class FunctionAnalyzer {

    private final ArrayList<String> allSignals = new ArrayList<>();

    private final ArrayList<Character> factualTotalPoints = new ArrayList<>();

    public ArrayList<Character> getFactualTotalPoints() {
        return factualTotalPoints;
    }

    public ArrayList<String> getAllSignals() {
        return allSignals;
    }

    private boolean isOperand(Character operand) {
        return (operand >= 'A' && operand <= 'Z');
    }

    private void removeDuplicate(ArrayList<Character> chArr) {
        for (int i = 0; i < chArr.size(); i++) {
            for (int j = i + 1; j < chArr.size(); j++) {
                if (Objects.equals(chArr.get(i), chArr.get(j))) {
                    chArr.set(j, '#');
                }
            }
        }
        chArr.removeIf(x -> x.equals('#'));
    }

    private ArrayList<Character> convertToArrayListOfChars(String function) {
        ArrayList<Character> arrayListOfChars = new ArrayList<>();
        for (char character : function.toCharArray()) {
            arrayListOfChars.add(character);
        }
        return arrayListOfChars;
    }

    public ArrayList<String> checkOnEx(String function) {
        String exception;
        ArrayList<String> allExceptions = new ArrayList<>();
        if (function.isEmpty()) {
            exception = "Function is empty;";
            allExceptions.add(exception);
            return allExceptions;
        }
        if (checkLogAddSignEx(function)) {
            exception = "Logical addition sign repetition detected;";
            allExceptions.add(exception);
        }
        if (checkLogMultSignEx(function)) {
            exception = "Logical multiplication sign repetition detected;";
            allExceptions.add(exception);
        }
        if (checkInvSignEx(function)) {
            exception = "Inversion sign repetition detected.\n" + "If you wanted to use double inversion, the correct entry looks like this: '!(!A)';";
            allExceptions.add(exception);
        }
        if (checkBrackets(function)) {
            exception = "Check the number of brackets;";
            allExceptions.add(exception);
        }
        if (validationCheck(function)) {
            exception = "Invalid expression;";
            allExceptions.add(exception);
        }
        return allExceptions;
    }

    private boolean checkLogAddSignEx(String function) {
        ArrayList<Character> arrayListOfChars = convertToArrayListOfChars(function);
        for (int i = 0; i < arrayListOfChars.size(); i++) {
            if (arrayListOfChars.get(i) == '+' && i + 1 != arrayListOfChars.size()) {
                if (arrayListOfChars.get(i + 1) == '+') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkLogMultSignEx(String function) {
        ArrayList<Character> arrayListOfChars = convertToArrayListOfChars(function);
        for (int i = 0; i < arrayListOfChars.size(); i++) {
            if (arrayListOfChars.get(i) == '*' && i + 1 != arrayListOfChars.size()) {
                if (arrayListOfChars.get(i + 1) == '*') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkInvSignEx(String function) {
        ArrayList<Character> arrayListOfChars = convertToArrayListOfChars(function);
        for (int i = 0; i < arrayListOfChars.size(); i++) {
            if (arrayListOfChars.get(i) == '!' && i + 1 != arrayListOfChars.size()) {
                if (arrayListOfChars.get(i + 1) == '!') {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkBrackets(String function) {
        int openingBrackets = 0;
        int closingBrackets = 0;
        for (int i = 0; i < function.length(); i++) {
            if (function.charAt(i) == '(') {
                openingBrackets += 1;
            }
            if (function.charAt(i) == ')') {
                closingBrackets += 1;
            }
        }
        return openingBrackets > closingBrackets || openingBrackets < closingBrackets;
    }

    private boolean validationCheck(String function) {
        if (function.length() != 1) {
            if ((function.charAt(0) == '+' || function.charAt(0) == '*') && isOperand(function.charAt(1))) {
                return true;
            }
            if (function.charAt(function.length() - 1) == '+' || function.charAt(function.length() - 1) == '*' || function.charAt(function.length() - 1) == '!') {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public void findAllSignals(String function) {
        ArrayList<Character> castedToChar = convertToArrayListOfChars(function);
        for (int i = 0; i < function.length(); i++) {
            if (castedToChar.get(i) == '!' && isOperand(castedToChar.get(i + 1))) {
                String invertedOperand = String.valueOf(castedToChar.get(i)) + String.valueOf(castedToChar.get(i + 1));
                castedToChar.set(i, '#');
                castedToChar.set(i + 1, '#');
                allSignals.add(invertedOperand);
            }
            if (isOperand(castedToChar.get(i))) {
                String var = String.valueOf(castedToChar.get(i));
                allSignals.add(var);
            }
        }
    }

    public void defineFactualTotalPoints(String function) {
        ArrayList<Character> array = convertToArrayListOfChars(function);
        for (int i = 0; i < array.size(); i++) {
            if (isOperand(array.get(i))) {
                factualTotalPoints.add(array.get(i));
            }
        }
        removeDuplicate(factualTotalPoints);
    }

    public String supplementExpression(String function) {
        ArrayList<Character> castedToChar = convertToArrayListOfChars(function);
        for (int i = 0; i < castedToChar.size(); i++) {
            if (isOperand(castedToChar.get(i)) && i != castedToChar.size() - 1) {
                if (isOperand(castedToChar.get(i + 1))) {
                    castedToChar.add(i + 1, '*');
                    i = 0;
                } else if (castedToChar.get(i + 1).equals('!') && (isOperand(castedToChar.get(i + 2)) || castedToChar.get(i + 2).equals('(')) && (i + 1) != castedToChar.size() - 1) {
                    castedToChar.add(i + 1, '*');
                    i = 0;
                } else if (castedToChar.get(i + 1).equals('(')) {
                    castedToChar.add(i + 1, '*');
                    i = 0;
                }
            }
        }
        String result = String.valueOf(castedToChar);
        return result;
    }
}
