package models;

import java.util.ArrayList;
import models.connector.Connector;

/**
 *
 * @author Miroslav Levdikov
 */
public class SignalEmulator {
        public void sendSignals(ArrayList<Connector> connectors) {
        for (Connector con : connectors) {
            if (con.getStartSocket().getSignal().getLogicalStatus() == null && con.getEndSocket().getSignal().getLogicalStatus() == null) {
                
            }
        }
    }
}
