package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class WumpusGerochen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung, Orientation Blickrichtung) {
		Anzutreffen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if((Feld != null) && Feld.isStench())
				Anzutreffen = true;
		}
	}
	
	public WumpusGerochen(boolean Anzutreffen) {
		super(SituationsStatusID.WUMPUSGEROCHEN, Anzutreffen);
	}
	public WumpusGerochen() {
		super(SituationsStatusID.WUMPUSGEROCHEN, false);
	}
}
