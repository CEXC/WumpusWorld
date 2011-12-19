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
	
	public int ZaehleGold(CaveGround[] Felder){
		int anzahl = 0;
		for(CaveGround feld : Felder){
			if(feld.isFilledWithGold())
				anzahl++;
		}
		return anzahl;
	}
	
	// Richtung 0=West, 1=Nord, 2=Ost, 3=Sued
	public CavePosition getZielInRichtung(CavePosition Position, int Richtung) {
		// ungueltige Richtung angegeben? => stehen bleiben
		if( (Richtung<0) || (Richtung>3))
			return Position;
		// Wir brauchen ja ne CavePosition fuer Aktion.Ziel
		CavePosition [] Umgebung = {
				new CavePosition(Position.getX()-1, Position.getY()  ),
				new CavePosition(Position.getX(),   Position.getY()+1),
				new CavePosition(Position.getX()+1, Position.getY()  ),
				new CavePosition(Position.getX(),   Position.getY()-1) };
		return Umgebung[Richtung];
	}
	
	public LinkedList<CavePosition> berechneWumpusGeruchFelder(LinkedList<CavePosition> Positionen){
		LinkedList<CavePosition> GeruchsFelder = new LinkedList<CavePosition>();
		// Umgebung des Agenten damit ich mit Umgebung.get(x) auf die aktuell
		// betrachtete Umgebung zugreifen kann und mit der AgentenPosition vergleichen
		// kann
		LinkedList<CavePosition> Umgebung = new LinkedList<CavePosition>();
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()  ,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()-1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX(),  Positionen.getFirst().getY()-1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()-1));
		return GeruchsFelder;
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
