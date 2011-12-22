package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;


public class RegelAgent extends NeighbourhoodPerceivingAgent {
	// Buchfuehrung
	int BesuchteFelder=1;
	// Regelsatz
	LinkedList<Regel> Regeln = new LinkedList<Regel>();
	
	// Aktuelle Situation
	LinkedList<SituationsStatus> StatusListe = new LinkedList<SituationsStatus>();
	NeighbourhoodPerception Wahrnehmung;
	int AktX = -1;
	int AktY = -1;
	Orientation Blickrichtung = Orientation.NORTH;
	CaveGround Nachbarschaft[];
	// speichere die letzten drei Positionen + die aktuelle um nicht immer hin und her zu laufen
	LinkedList<CavePosition> AltePositionen = new LinkedList<CavePosition>();
	// speicher erreichen des Randes West = 0, Nord = 1, Ost = 2, Sued = 3
	boolean Kante[] = {false, false, false, false};
	
	@Override
	protected AgentAction act(NeighbourhoodPerception Wahrnehmung) {
		// zur Vereinfachung
		this.Wahrnehmung = Wahrnehmung;
		AktX = getCavePosition().getX();
		AktY = getCavePosition().getY();
		Blickrichtung = getCavePosition().getOrientation();
		Nachbarschaft = Wahrnehmung.getNeighbourHood();
		for(int i=0; i<4; i++){
			if(Nachbarschaft[i*2]==null)
				Kante[i] = true;
		}
		if((AltePositionen.size() == 0) || !AltePositionen.getFirst().coordinatesEqual(getCavePosition()))
			AltePositionen.addFirst(getCavePosition().makeCopy());
		if(AltePositionen.size() > 3)
			AltePositionen.removeLast();
		
		for(SituationsStatus Status : StatusListe) {
			Status.updateStatus(AltePositionen, Nachbarschaft, Wahrnehmung, Blickrichtung);
		}
		
		// alle Regeln durchlaufen und von den zutreffenden die mit der hoechsten Prioritaet
		// ausfuehren
		for(Regel NR : Regeln) {
			if(NR.IstRegelAnwendbar(StatusListe)) {
				// Regel anwenden
				AgentAction Aktion = AktionAusfuehren(NR.berechneRegelAktion(Wahrnehmung, AltePositionen,
														StatusListe, Blickrichtung, this));
				if(Aktion == AgentAction.GO)
					BesuchteFelder++;
				return Aktion;
			}
		}
		return AgentAction.WAIT;
	}
	
	protected AgentAction AktionAusfuehren(AgentenAktion Aktion) {
		if(Aktion.PfeilAbschiessen)
			return AgentAction.SHOOT_ARROW;
		if(Aktion.GoldAufheben)
			return AgentAction.GRAB_GOLD;
		return bewegeAgenten(Aktion.Ziel);
	}
	
	protected AgentAction bewegeAgenten(CavePosition Ziel) {
		// Blickrichtung stimmt nicht? => drehen!
		if(Blickrichtung != getZielrichtung(Ziel))
			return dreheAgenten(getZielrichtung(Ziel));
		if((Ziel.getX() == AktX) && ((Ziel.getY() == AktY)))
			return AgentAction.WAIT;
		return AgentAction.GO;
	}
	
	// die methode ist nur fuer direkt benachbarte felder gedacht 
	protected Orientation getZielrichtung(CavePosition Ziel) {
		Integer X = Ziel.getX();
		Integer Y = Ziel.getY();
		Orientation Zielrichtung = Orientation.NORTH;
		// In welcher Richtung liegt das naechste Ziel
		if(X > AktX) Zielrichtung = Orientation.EAST;
		else if(X < AktX) Zielrichtung = Orientation.WEST;
		else if(Y > AktY) Zielrichtung = Orientation.NORTH;
		else if(Y < AktY) Zielrichtung = Orientation.SOUTH;
		return Zielrichtung;
	}
	
	protected AgentAction dreheAgenten(Orientation Zielrichtung) {
		if(Blickrichtung == Zielrichtung)
			return AgentAction.WAIT;
		// Standard Drehrichtung rechts
		AgentAction Aktion = AgentAction.TURN_RIGHT;
		if( ((Orientation.NORTH == Blickrichtung) && (Orientation.WEST == Zielrichtung)) ||
			((Orientation.WEST == Blickrichtung) && (Orientation.SOUTH == Zielrichtung)) ||
			((Orientation.SOUTH == Blickrichtung) && (Orientation.EAST == Zielrichtung)) ||
			((Orientation.EAST == Blickrichtung) && (Orientation.NORTH == Zielrichtung)) )
			Aktion = AgentAction.TURN_LEFT;
		return Aktion;
	}
	
	public int getBesuchteFelder() {
		return BesuchteFelder;
	}
	
	public void addSituationsStatus(SituationsStatus Status) {
		StatusListe.add(Status);
	}
	
	public void addRegel(Regel NeueRegel) {
		if(!Regeln.contains(NeueRegel))
			Regeln.add(NeueRegel);
		Collections.sort(Regeln);
	}
	
	public void addRegeln(List<Regel> NeueRegeln) {
		for(Regel NR : NeueRegeln) {
			if(!Regeln.contains(NR))
				Regeln.add(NR);
		}
			
		Collections.sort(Regeln);
	}
	@Override
	public String getName() {
		return "RegelAgent";
	}	
}