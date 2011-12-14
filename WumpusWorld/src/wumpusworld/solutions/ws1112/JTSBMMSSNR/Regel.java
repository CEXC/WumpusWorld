package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.NeighbourhoodPerception;
import model.wumpusworld.Orientation;

public class Regel implements Comparable<Regel> {
	
	// Aktionenfolge, die bei der Situation ausgefuehrt werden soll
	LinkedList<AgentAction> Aktionen = new LinkedList<AgentAction>();

	// Prioritaet groesser ist wichtiger Prioritaet >= 1
	int Prioritaet = 1;
	
	// Situationsbeschreibende Variablen
	NeighbourhoodPerception Wahrnehmung;
	ArrayList<Orientation> BetretbareFelder = new ArrayList<Orientation>();
	
	public void addAktion(AgentAction Aktion) {
		Aktionen.add(Aktion);
	}
	
	public void addAktionen(List<AgentAction> Aktionen) {
		this.Aktionen.addAll(Aktionen);
	}
	
	public void setPrioritaet(int Prioritaet) {
		if(Prioritaet >= 1) 
			this.Prioritaet = Prioritaet;
	}
	
	public void loescheAktionen() {
		Aktionen.clear();
	}
	
	public boolean IstRegelAnwendbar(NeighbourhoodPerception Wahrnehmung) {
		this.Wahrnehmung = Wahrnehmung;
		berechneBetretbareFelder();
		return false;
	}
	
	private int berechneBetretbareFelder() {
		BetretbareFelder.clear();
		if(IstFeldBetretbar(Wahrnehmung.getNorth()))
			BetretbareFelder.add(Orientation.NORTH);
		if(IstFeldBetretbar(Wahrnehmung.getEast()))
			BetretbareFelder.add(Orientation.EAST);
		if(IstFeldBetretbar(Wahrnehmung.getSouth()))
			BetretbareFelder.add(Orientation.SOUTH);
		if(IstFeldBetretbar(Wahrnehmung.getWest()))
			BetretbareFelder.add(Orientation.WEST);
		return BetretbareFelder.size();
	}
	protected boolean IstFeldBetretbar(CaveGround Feld) {
		if(Feld == null)
			return false;
		if(Feld.getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	
	// Wir sortieren Regeln nach deren Prioritaet wichtiger vor weniger wichtig
	@Override
	public int compareTo(Regel R) {
		if(Prioritaet > R.Prioritaet)
			return -1;
		else if(Prioritaet < R.Prioritaet)
			return 1;
		// Alles gleich...
		return 0;
	}

	public Regel(Regel R) {
		this.Prioritaet = R.Prioritaet;
		this.Aktionen = new LinkedList<AgentAction>();
		this.Aktionen.addAll(R.Aktionen);
	}
	
	public Regel() {
	}
	
	// Vergleich wobei die Prioritaet keine Rolle spielt
	public boolean Vergleich(Regel R) {
		if (this == R) {
			return true;
			}
		
		// TODO hier muss noch die eigentliche situation beschrieben sein
		
		
		if(Aktionen.size() != R.Aktionen.size()) 
			return false;
		
		for(int i=0; i<Aktionen.size(); i++) {
			if(Aktionen.get(i) != R.Aktionen.get(i)) 
				return false;
		}
		return true;
	}
	
	// Prioritaet spielt eine Rolle
	public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null || getClass() != Objekt.getClass()) {
			return false;
			}
		final Regel R = (Regel) Objekt;
		
		if(Prioritaet != R.Prioritaet)
			return false;
		
		return Vergleich(R);
	}
}
