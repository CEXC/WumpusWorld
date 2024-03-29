package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

// Kapselt verschiedene Situationen (abgeleitet von dieser Klasse)
// diese koennen den Regeln dann hinzugefuegt werden
public abstract class SituationsStatus {
	boolean Anzutreffen = false;
	final SituationsStatusID ID;
	public abstract void updateStatus(LinkedList<CavePosition> Positionen, 
										CaveGround Nachbarschaft[], 
										NeighbourhoodPerception Wahrnehmung, Orientation Blickrichtung);
	
	public SituationsStatus(SituationsStatusID ID, boolean Anzutreffen) {
		this.ID = ID;
		this.Anzutreffen = Anzutreffen;
	}
	
	public boolean istAnzutreffen() {
		return Anzutreffen;
	}

	public void setAnzutreffen(boolean anzutreffen) {
		Anzutreffen = anzutreffen;
	}
	
	final public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null)
			return false;
			
	 	if(getClass() != Objekt.getClass())
	 		return false;
	 	
	 	final SituationsStatus Status = (SituationsStatus) Objekt;	
	 	if(ID != Status.ID)
	 		return false;
	 	
		return true;
	}
}
