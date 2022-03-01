package models;

/**
 *
 * @author Miroslav Levdikov
 */
public final class Signal {

    private String variable;
    private String operator;
    private boolean isIncluded;
    private boolean variableInBrackets;
    private int layer = 0;

    public Signal(String variable, String operator) {
        this.variable = variable;
        this.operator = operator;
        this.isIncluded = false;
        checkIsolation(variable);
    }
//    public Signal(Signal signal) {
//        copyFrom(signal);
//        this.isIncluded = false;
//    }

//    public void copyFrom(Signal copied) {
//        this.variable = copied.variable;
//        this.operator = copied.operator;
//        this.variableInBrackets = copied.variableInBrackets;
//    }

    private void checkIsolation(String variable) {
        char[] currElemVariable = new char[variable.length()];
        for (int j = 0; j < variable.length(); j++) {
            currElemVariable[j] = variable.charAt(j); //преобразуем выражение в array
        }
        int closeBracketCount = 0, openBracketCount = 0, closePos = 0, openPos = 0;
        for (int j = 0; j < currElemVariable.length; j++) {
            if (currElemVariable[j] == ')') {
                //openBracketCount++;
                openPos = j;
                openBracketCount++;
            }
            if (currElemVariable[j] == '(') {
                //closeBracketCount++;
                closePos = j;
                closeBracketCount++;
                if (openBracketCount == closeBracketCount & closePos != currElemVariable.length - 1) {
                    this.variableInBrackets = false;
                    break;
                }
                if (openBracketCount == closeBracketCount & j == currElemVariable.length - 1 & (openPos == 0 || openPos == 1)) {
                    this.variableInBrackets = true;
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Signal: " + "var: " + variable + ", operator: " + operator + ", isIncluded: " + isIncluded + ", variableInBrackets: " + variableInBrackets
                + ", layer: " + layer + ';';
    }

    public void setIncluded(boolean isIncluded) {
        this.isIncluded = isIncluded;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isVariableInBrackets() {
        return variableInBrackets;
    }

    public void setVariableInBrackets(boolean variableInBrackets) {
        this.variableInBrackets = variableInBrackets;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
