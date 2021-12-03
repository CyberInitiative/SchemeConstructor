package models;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;

/**
 *
 * @author Miroslav Levdikov
 */
public class MyGroup {
    private List<Node> nodesList = new ArrayList<>();
    
    public void addToGroup(Node node){
        nodesList.add(node);
    }
    
    public void getLayout(){
        for(Node curr : nodesList){
//            curr.getLa
        }
    }
}
