package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * @author Miroslav Levdikov
 */
public class RPN_Transformator {

    /*Данный класс отвечает за перевод логического выражения в обратную польскую нотацию (reverse polish notation)*/
    
    private final List<Character> resultOfConvertion = new ArrayList<Character>();
    private final Stack<Symbol> operationStack = new Stack<>();

    public List<Character> getResultOfConvertion() {
        return resultOfConvertion;
    }

    public String getResultAsString() {
        return resultOfConvertion.stream().map(String::valueOf).collect(Collectors.joining());
    }

    private void checkTopOfStack(Symbol currentSymbol) {
        if (!operationStack.empty() && operationStack.peek().getType().getPriority() >= currentSymbol.getType().getPriority() && operationStack.peek().getType().isComparable() && currentSymbol.getType().isComparable()) {
            resultOfConvertion.add(operationStack.pop().getSymbol());
            checkTopOfStack(currentSymbol);
        } else if (!operationStack.empty() && operationStack.peek().getType() != SymbolType.OpenBracket && currentSymbol.getType() == SymbolType.CloseBracket) {
            resultOfConvertion.add(operationStack.pop().getSymbol());
            if (!operationStack.empty() && operationStack.peek().getType() == SymbolType.OpenBracket) {
                resultOfConvertion.add(operationStack.pop().getSymbol());
            } else {
                checkTopOfStack(currentSymbol);
            }
        } else {
            if (currentSymbol.getType() != SymbolType.CloseBracket) {
                operationStack.push(currentSymbol);
            }
        }
    }

    public void convertToRPN(String function) {
        List<Symbol> symbols = new ArrayList<>();
        for (int i = 0; i < function.length(); i++) {
            Symbol symbol = new Symbol(function.charAt(i));
            symbol.defineType();
            symbols.add(symbol);
        }
        for (int i = 0; i < symbols.size(); i++) {
            if (symbols.get(i).getType() == SymbolType.Operand) {
                resultOfConvertion.add(symbols.get(i).getSymbol());
            } else if (symbols.get(i).getType().isComparable()) {
                checkTopOfStack(symbols.get(i));
            } else if (symbols.get(i).getType() == SymbolType.CloseBracket) {
                operationStack.push(symbols.get(i));
                checkTopOfStack(symbols.get(i));
            } else if (symbols.get(i).getType() == SymbolType.OpenBracket) {
                operationStack.push(symbols.get(i));
            }
        }
        for (int i = operationStack.size(); --i >= 0;) {
            resultOfConvertion.add(operationStack.get(i).getSymbol());
        }
        //System.out.println("Result: " + resultOfConvertion);
    }
}
