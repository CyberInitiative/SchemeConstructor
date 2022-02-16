package models;

import javafx.scene.text.Text;

/**
 *
 * @author Miroslav Levdikov
 */
public interface Movable {
    public void setCorX(double corX);
    public void setCorY(double corY);
    public void setMouseX(double mouseX);
    public void setMouseY(double mouseY);
    
    public double getCorX();
    public double getCorY();
    public double getMouseX();
    public double getMouseY();
    
    public void setSymbol(Text symbol);
    public Text getSymbol();
    
    public void setToFront();

    @Override
    public String toString();   
}
