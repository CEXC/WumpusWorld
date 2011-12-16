package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public abstract class RegelAktion {

	final RegelAktionID ID;
	
	public RegelAktion(RegelAktionID ID) {
		this.ID = ID;
	}
	
	public abstract AgentAction berechneAktion(LinkedList<CavePosition> Positionen, CaveGround Nachbarschaft[], 
			NeighbourhoodPerception Wahrnehmung, LinkedList<SituationsStatus> StatusListe);

	final public boolean equals(Object Objekt) {
	if (this == Objekt) {
		return true;
		}
	if (Objekt == null)
		return false;
	
	if(getClass() != Objekt.getClass())
		return false;
	
	final RegelAktion RA = (RegelAktion) Objekt;	
	if(ID != RA.ID)
		return false;
	
	return true;
	}
}
