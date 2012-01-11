package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

// kapselt Aktionen die zu Regeln hinzugefuegt werden koennen
// und stellt abgeleiteten Klassen einige Hilfsfunktionen zur
// Verfuegung
public abstract class RegelAktion implements Comparable<RegelAktion> {

	final RegelAktionID ID;
	public RegelAktion(RegelAktionID ID, int Prioritaet) {
		this.ID = ID;
		this.Prioritaet = Prioritaet;
	}
	
	public abstract AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen, 
												 CaveGround Nachbarschaft[], 
												 NeighbourhoodPerception Wahrnehmung, 
												 LinkedList<SituationsStatus> StatusListe,
												 Orientation Blickrichtung,
												 RegelAgent Agent);

	protected boolean IstFeldBetretbar(CaveGround Feld) {
		if(Feld == null)
			return false;
		if(Feld.getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	protected boolean IstFeldUeberhauptBetretbar(CaveGround Feld) {
		if(Feld == null)
			return false;
		return true;
	}
	
	// Entferne die Position aus der Liste
	protected LinkedList<CavePosition> removePosition(CavePosition Position, LinkedList<CavePosition> Liste) {
		Position.setOrientation(Orientation.WEST);
		Liste.remove(Position);
		Position.setOrientation(Orientation.NORTH);
		Liste.remove(Position);
		Position.setOrientation(Orientation.EAST);
		Liste.remove(Position);
		Position.setOrientation(Orientation.SOUTH);
		Liste.remove(Position);
		return Liste;
	}
	
	public int ZaehleGold(CaveGround[] Felder){
		int anzahl = 0;
		for(CaveGround feld : Felder){
			if(feld != null)
				if(feld.isFilledWithGold())
					anzahl++;
		}
		return anzahl;
	}
	// Richtung 0=West, 1=Nord, 2=Ost, 3=Sued
	public boolean GucktInZielrichtung(CavePosition AgentPosition, CavePosition Ziel, Orientation Blickrichtung){
		// ungueltiges Ziel oder ungueltige AgentPosition
		if(Ziel == null || AgentPosition == null)
			return false;
		if(Blickrichtung == Orientation.NORTH)
			if(	AgentPosition.getX() == Ziel.getX())
					if(	AgentPosition.getY()+1 == Ziel.getY() )
						return true;
		if(Blickrichtung == Orientation.WEST)
			if	(AgentPosition.getX()-1 == Ziel.getX())
					if(	AgentPosition.getY() == Ziel.getY() )
						return true;
		if(Blickrichtung == Orientation.EAST)
			if(AgentPosition.getX()+1 == Ziel.getX())
					if(	AgentPosition.getY() == Ziel.getY() )
						return true;
		if(Blickrichtung == Orientation.SOUTH)
			if(AgentPosition.getX() == Ziel.getX())
					if(	AgentPosition.getY()-1 == Ziel.getY() )
						return true;
		return false;
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
	
	public LinkedList<CavePosition> BerechneWumpusGeruchFelder(CavePosition Position, CaveGround[] Nachbarschaft){
		LinkedList<CavePosition> GeruchsFelder = new LinkedList<CavePosition>();
		// Umgebung des Agenten damit ich mit Umgebung.get(x) auf die aktuell
		// betrachtete Umgebung zugreifen kann und mit der AgentenPosition vergleichen
		// kann
		LinkedList<CavePosition> Umgebung = new LinkedList<CavePosition>();
		Umgebung.add(new CavePosition(Position.getX()-1,Position.getY()  ));
		Umgebung.add(new CavePosition(Position.getX()-1,Position.getY()+1));
		Umgebung.add(new CavePosition(Position.getX()  ,Position.getY()+1));
		Umgebung.add(new CavePosition(Position.getX()+1,Position.getY()+1));
		Umgebung.add(new CavePosition(Position.getX()+1,Position.getY()  ));
		Umgebung.add(new CavePosition(Position.getX()+1,Position.getY()-1));
		Umgebung.add(new CavePosition(Position.getX(),  Position.getY()-1));
		Umgebung.add(new CavePosition(Position.getX()-1,Position.getY()-1));
		
		for(int i=0; i<Nachbarschaft.length; i++){
			if(Nachbarschaft[i] != null){
				if(Nachbarschaft[i].isStench())
					GeruchsFelder.add(Umgebung.get(i));
			}
		}
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
