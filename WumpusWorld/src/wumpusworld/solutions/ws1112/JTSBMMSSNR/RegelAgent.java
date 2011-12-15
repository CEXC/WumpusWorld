package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
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
	boolean WumpusGerochen = false;
	boolean WumpusGesehen = false;
	boolean WumpusVoraus = false;
	boolean GoldGesehen = false;
	boolean NichtsFestgestellt = false;
	boolean Gefangen = false;
	NeighbourhoodPerception Wahrnehmung;
	int AktX = -1;
	int AktY = -1;
	Orientation Blickrichtung = Orientation.NORTH;
	CaveGround Nachbarschaft[];
	// speichere die letzten drei Positionen + die aktuelle um nicht immer hin und her zu laufen
	LinkedList<CavePosition> AltePositionen = new LinkedList<CavePosition>();
	
	@Override
	protected AgentAction act(NeighbourhoodPerception Wahrnehmung) {
		// zur Vereinfachung
		this.Wahrnehmung = Wahrnehmung;
		AktX = getCavePosition().getX();
		AktY = getCavePosition().getY();
		Blickrichtung = getCavePosition().getOrientation();
		Nachbarschaft = Wahrnehmung.getNeighbourHood();
		AltePositionen.addFirst(getCavePosition());
		if(AltePositionen.size() > 4)
			AltePositionen.removeLast();
		updateWumpusGerochen();
		updateWumpusGesehen();
		updateWumpusVoraus();
		updateGoldGesehen();
		updateGefangen();
		updateNichtsFestgestellt();
		
		// alle Regeln durchlaufen und von den zutreffenden die mit der hoechsten Prioritaet
		// ausfuehren
		for(Regel NR : Regeln) {
			if(NR.IstRegelAnwendbar(WumpusGerochen, WumpusGesehen, WumpusVoraus, 
									GoldGesehen, NichtsFestgestellt, Gefangen)) {
				// Regel anwenden
				AgentAction Aktion = AktionAusfuehren(NR.berechneRegelAktion(Wahrnehmung, AltePositionen));
				if(Aktion == AgentAction.GO)
					BesuchteFelder++;
				return Aktion;
			}
		}
		return AgentAction.WAIT;
	}
	
	protected AgentAction AktionAusfuehren(RegelAktion Aktion) {
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
	
	protected void updateWumpusGesehen() {
		WumpusGesehen = false;
		if(Wahrnehmung.getCurrentCaveGround().isStench())
			WumpusGesehen = true;
	}
	
	protected void updateWumpusGerochen() {
		WumpusGerochen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if((Feld != null) && Feld.isStench())
				WumpusGerochen = true;
		}
	}
	
	protected void updateWumpusVoraus() {
		WumpusVoraus = false;
		int ErstesFeld = 7; // Westen
		if(Blickrichtung == Orientation.NORTH)
			ErstesFeld = 1;
		if(Blickrichtung == Orientation.EAST)
			ErstesFeld = 3;
		if(Blickrichtung == Orientation.SOUTH)
			ErstesFeld = 5;
		WumpusVoraus = true;
		for(int i=0; i<3; i++) {
			if(!Nachbarschaft[ErstesFeld+i].isStench())
				WumpusVoraus = false;
		}
	}
	
	protected void updateGoldGesehen() {
		GoldGesehen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if((Feld != null) && Feld.isFilledWithGold())
				GoldGesehen = true;
		}
	}
	
	protected void updateGefangen() {
		Gefangen = false;
		if( ((Wahrnehmung.getNorth() == null) || (Wahrnehmung.getNorth().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getEast() == null) || (Wahrnehmung.getEast().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getSouth() == null) || (Wahrnehmung.getSouth().getType() == CaveGroundType.PIT)) &&
			((Wahrnehmung.getWest() == null) || (Wahrnehmung.getWest().getType() == CaveGroundType.PIT)) )
			Gefangen = true;
	}
	
	// Diese Funktion ist voll ausprogrammiert, da wir uns nicht auf die Reihenfolge des Aufrufs
	// der updateFunktionen verlassen wollen.
	protected void updateNichtsFestgestellt() {
		NichtsFestgestellt = true;
		for(CaveGround Feld : Nachbarschaft) {
			// Wir sind an einer Seite, es gibt Gold, der Wumpus ist in der Naehe
			if((null == Feld) || (Feld.isFilledWithGold()) || (Feld.isStench())) { 
				NichtsFestgestellt = false;
				return ;
			}
		}
		// es kann auch noch was an der aktuellen Position sein
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold() || 
			Wahrnehmung.getCurrentCaveGround().isStench()) {
			NichtsFestgestellt = false;
		}
			
	}
	
	public int getBesuchteFelder() {
		return BesuchteFelder;
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