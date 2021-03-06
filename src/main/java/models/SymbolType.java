package models;

/**
 *
 * @author Miroslav Levdikov
 */
public enum SymbolType {
    LogAddSing(1, true), LogMultSign(2, true), InversionSign(3, true), Operand(false), OpenBracket(false), CloseBracket(false);

    private int priority;
    private boolean comparable;

    SymbolType(boolean comparable) {
        this.comparable = comparable;
    }

    SymbolType(int priority, boolean comparable) {
        this.priority = priority;
        this.comparable = comparable;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isComparable() {
        return comparable;
    }
}
