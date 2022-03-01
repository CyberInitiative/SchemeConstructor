package models;

import java.util.ArrayList;
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

       
    public ArrayList<Socket> getOutputSockets();

    public ArrayList<Socket> getInputSockets();
    //public Socket

    public void setToFront();

    @Override
    public String toString();
}
