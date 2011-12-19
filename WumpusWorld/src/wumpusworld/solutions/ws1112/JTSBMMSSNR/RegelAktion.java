package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public abstract class RegelAktion implements Comparable<RegelAktion> {

	final RegelAktionID ID;
	public RegelAktion(RegelAktionID ID, int Prioritaet) {
		this.ID = ID;
		this.Prioritaet = Prioritaet;
	}
	
	public abstract AgentAction berechneAktion(LinkedList<CavePosition> Positionen, CaveGround Nachbarschaft[], 
			NeighbourhoodPerception Wahrnehmung, LinkedList<SituationsStatus> StatusListe);

	// Prioritaet groesser ist wichtiger Prioritaet >= 1
	int Prioritaet = 1;
		
	// Wir sortieren Regeln nach deren Prioritaet wichtiger vor weniger wichtig
	@Override
	public int compareTo(RegelAktion RA) {
		if(Prioritaet > RA.Prioritaet)
			return -1;
		else if(Prioritaet < RA.Prioritaet)
			return 1;
		// Alles gleich...
		return 0;
	}
		
	// ACHTUNG: Hier spielt die Prioritaet keine Rolle
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
