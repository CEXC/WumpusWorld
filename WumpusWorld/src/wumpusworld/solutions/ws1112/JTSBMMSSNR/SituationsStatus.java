package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public abstract class SituationsStatus {
	boolean Anzutreffen = false;
	final SituationsStatusID ID;
	public abstract void updateStatus(LinkedList<CavePosition> Positionen, CaveGround Nachbarschaft[], 
										NeighbourhoodPerception Wahrnehmung);
	
	public SituationsStatus(SituationsStatusID ID) {
		this.ID = ID;
	}
	
	public boolean istAnzutreffen() {
		return Anzutreffen;
	}

	public void setAnzutreffen(boolean anzutreffen) {
		Anzutreffen = anzutreffen;
	}
}
