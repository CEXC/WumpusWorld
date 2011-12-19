package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public abstract class RegelAktion implements Comparable<RegelAktion> {

	final RegelAktionID ID;
	public RegelAktion(RegelAktionID ID, int Prioritaet) {
		this.ID = ID;
		this.Prioritaet = Prioritaet;
	}
	
	public abstract AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen, CaveGround Nachbarschaft[], 
			NeighbourhoodPerception Wahrnehmung, LinkedList<SituationsStatus> StatusListe);

	protected boolean IstFeldBetretbar(CaveGround Feld) {
		if(Feld == null)
			return false;
		if(Feld.getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	
	
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
		
	// ACHTUNG: Hier spielt die Prioritaet eine Rolle
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
	
		if(Prioritaet != RA.Prioritaet)
			return false;
		
		return true;
	}
}
