package models.shemeGenerator;

import models.shemeGenerator.SymbolType;

/**
 *
 * @author Miroslav Levdikov
 */
public class Symbol {

    private char symbol;
    private SymbolType type;

    public Symbol(char symbol) {
        this.symbol = symbol;
    }

    public void defineType() {
        if (this.symbol == '!') {
            this.type = SymbolType.InversionSign;
        } else if (this.symbol == '*') {
            this.type = SymbolType.LogMultSign;
        } else if (this.symbol == '+') {
            this.type = SymbolType.LogAddSign;
        } else if (this.symbol == '(') {
            this.type = SymbolType.OpenBracket;
        } else if (this.symbol == ')') {
            this.type = SymbolType.CloseBracket;
        } else if (this.symbol >= 'A' && this.symbol <= 'Z') {
            this.type = SymbolType.Operand;
        }
    }

    @Override
    public String toString() {
        return "Symbol{" + "symbol=" + symbol + ", type=" + type + '}';
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public SymbolType getType() {
        return type;
    }
}
