package models;

/**
 *
 * @author Miroslav Levdikov
 */
public class Signal {

    private String variable;
    private final String operator;
    private boolean isIncluded;

    public Signal(String variable, String operator) {
        this.variable = variable;
        this.operator = operator;
        this.isIncluded = false;
    }

    @Override
    public String toString() {
        return "Signal varibale: " + variable + ", Operator: " + operator + ", Is Included: " + isIncluded + " ";
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
}
