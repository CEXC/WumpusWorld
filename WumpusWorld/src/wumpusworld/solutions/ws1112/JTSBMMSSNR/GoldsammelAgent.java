package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.CaveGroundType;
import model.wumpusworld.Orientation;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.CompleteCavePerceivingAgent;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CompleteCavePerception;
import james.SimSystem;
import james.core.util.collection.list.LinkedList;
import james.core.util.eventset.Entry;
import james.core.util.eventset.SimpleEventQueue;
import james.core.util.misc.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GoldsammelAgent extends CompleteCavePerceivingAgent {
	// Welches Suchverfahren soll eingesetzt werden?
	AgentenVorgehen AV = AgentenVorgehen.UNIFORMEKOSTENSUCHE; 
		
	// Informationen zum aktuellen Status des Agenten
	Integer AktX = -1;
	Integer AktY = -1; 
	Orientation Blickrichtung = Orientation.NORTH;
	Integer BesuchteFelder=0;
	
	// Muessen die Berechnungen bei bekanntwerden der Hoehle noch gemacht werden?
	// Liste aller Goldklumpen usw. erstellen, Reihenfolge planen
	boolean Initialisiert=false;
	LinkedList<Pair<Integer, Integer>> AlleGoldklumpen = new LinkedList<Pair<Integer, Integer>>();
	LinkedList<Pair<Integer, Integer>> ReihenfolgeGoldklumpen = new LinkedList<Pair<Integer,Integer>>();
	LinkedList<Wegpunkt> WegZumGold = new LinkedList<Wegpunkt>();
	
	// speichert alle in der Hoehle vorhandenen Goldklumpen in der ArrayList AlleGoldklumpen
	protected void erstelleGoldklumpenListe(Cave Hoehle) {
		AlleGoldklumpen.clear();
		for(int i=0; i<Hoehle.getWidth(); i++) {
			for(int j=0; j<Hoehle.getHeight(); j++) {
				if(Hoehle.getGround(i, j).isFilledWithGold()) {
					AlleGoldklumpen.add(new Pair<Integer, Integer>(i, j));
				}
			}
		}
	}
	
	// Heuristik fuer Wegfindung
	protected Integer RestStreckeLuftLinie(Pair<Integer, Integer> Position, Pair<Integer, Integer> Ziel) {
		return (int) Math.sqrt((Position.getFirstValue()-Ziel.getFirstValue())*(Position.getFirstValue()-Ziel.getFirstValue())+
			   (Position.getSecondValue()-Ziel.getSecondValue())*(Position.getSecondValue()-Ziel.getSecondValue()));
	}
	
	// finde den kuerzesten Weg zwischen den Punken benutze A* dafuer
	// Verbesserungs moeglichkeit: Suche den Weg mit den wenigsten Aktionen (Drehungen)
	protected LinkedList<Wegpunkt> findeWeg(Cave Hoehle, Pair<Integer, Integer> Start, Pair<Integer, Integer> Ziel) {
		// anlegen der Prioritaetenliste
		SimpleEventQueue<Wegpunkt, WegpunktQualitaet> ZuBesuchen = new SimpleEventQueue<Wegpunkt, WegpunktQualitaet>();
		// startpunkt als anfang einfuegen
		ZuBesuchen.enqueue(new Wegpunkt(Start), new WegpunktQualitaet(RestStreckeLuftLinie(Start, Ziel)));
		// Liste zum Speichern aller Besuchten Knoten
		List<Wegpunkt> BesuchteFelder = new ArrayList<Wegpunkt>();
		// Start/Ziel als Wegpunkt zum Vergleichen einfacher
		Wegpunkt Startpunkt = new Wegpunkt(Start);
		Wegpunkt Zielpunkt = new Wegpunkt(Ziel);
		// Solange wir noch nicht das Ziel erreicht haben, behandeln wir ggf. den naechsten Knoten
		while(!ZuBesuchen.isEmpty()) {
			// Naechsten Punkt aus der Schlange holen
			Entry<Wegpunkt, WegpunktQualitaet> tmp = ZuBesuchen.dequeue();
			Wegpunkt WP= tmp.getEvent();
			// Ist der Wegpunkt das Ziel?
			if(WP.equals(Zielpunkt)) {
				 // Bestimme den gefundenen Weg
				 LinkedList<Wegpunkt> Weg = new LinkedList<Wegpunkt>();
				 Weg.addFirst(WP);
				 Wegpunkt Vorgaenger = WP.getVorgaenger();
				 while(!Vorgaenger.equals(Startpunkt)) {
					 Weg.addFirst(Vorgaenger);
					 Vorgaenger = Vorgaenger.getVorgaenger();
				 }
				 return Weg;
			}
			// Falls der Wegpunkt schon in der Liste der besuchten Felder enthalten ist, verwerfen
			if(BesuchteFelder.contains(WP))
				continue;
			// Wegpunkt in die Liste der Besuchten Felder eintragen
			BesuchteFelder.add(WP);
			// Nachfolger bestimmen
			List<Pair<Integer, Integer>> NachfolgerListe = Hoehle.getVonNeumannNeighbourhoodCoordinates(
															WP.getKoordinaten().getFirstValue(), 
															WP.getKoordinaten().getSecondValue());
			// Alle Nachfolger, die noch nicht besucht wurden, in Schlange haengen
			for(int i=0; i<NachfolgerListe.size(); i++) {
				Wegpunkt Nachfolger  = new Wegpunkt(NachfolgerListe.get(i), WP);
				// Schon besucht? bei A* brauchen wir den nicht wieder einfuegen
				if(BesuchteFelder.contains(Nachfolger))
					continue;
				// wenn Feld nicht betretbar ist, brauchen wir es auch nicht
				if(!IstFeldBetretbar(Hoehle, Nachfolger.getKoordinaten()))
					continue;
				// Anhaengen an Queue
				ZuBesuchen.enqueue(Nachfolger, 
						new WegpunktQualitaet(RestStreckeLuftLinie(Nachfolger.getKoordinaten(), Ziel)));
			}
		}
		return null;
	}
	
	protected boolean IstFeldBetretbar(Cave Hoehle, Pair<Integer, Integer> Koordinaten) {
		if(Hoehle.getGround(Koordinaten.getFirstValue(), Koordinaten.getSecondValue()).getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	
	// Diese Methode plant die Reihenfolge in der die Goldstuecke aufgesammelt werden sollen.
	// Sie sucht also nach dem kuerzesten Weg innerhalb der Hoehle, welcher alle Goldstuecke
	// enthaelt
	protected void planeGoldsammelReihenfolge() {
		ReihenfolgeGoldklumpen = AlleGoldklumpen;
	}
	
	// bewege den Agenten zu Koordinaten. Drehe ihn in die richtige Richtung und veranlasse Schritt
	protected AgentAction bewegeAgentNach(Pair<Integer, Integer> Koordinaten) {
		Orientation Zielrichtung=Orientation.NORTH;
		Integer X = Koordinaten.getFirstValue();
		Integer Y = Koordinaten.getSecondValue();
		// In welcher Richtung liegt das naechste Ziel
		if(X > AktX) Zielrichtung = Orientation.EAST;
		else if(X < AktX) Zielrichtung = Orientation.WEST;
		else if(Y > AktY) Zielrichtung = Orientation.NORTH;
		else if(Y < AktY) Zielrichtung = Orientation.SOUTH;
		// Wir muessen auf dem Feld sein, mache nichts
		else
			return AgentAction.WAIT;
		
		// Gucken wir in Richtung des naechsten Feldes? -> Laufen
		if(Blickrichtung == Zielrichtung) {
			BesuchteFelder++;
			return AgentAction.GO;
		}
		
		// Sonst muessen wir uns drehen. Grundrichtung nach rechts, es gibt nur ein paar Ausnahmen
		AgentAction Aktion = AgentAction.TURN_RIGHT;
		if( ((Orientation.NORTH == Blickrichtung) && (Orientation.WEST == Zielrichtung)) || 
			((Orientation.WEST == Blickrichtung) && (Orientation.SOUTH == Zielrichtung)) ||
			((Orientation.SOUTH == Blickrichtung) && (Orientation.EAST == Zielrichtung)) ||
			((Orientation.EAST == Blickrichtung) && (Orientation.NORTH == Zielrichtung)) )
			Aktion = AgentAction.TURN_LEFT;
	
		// Update des aktuellen Status
		if(AgentAction.TURN_RIGHT == Aktion)
			Blickrichtung = Blickrichtung.turnRight();
		else if(AgentAction.TURN_LEFT == Aktion)
			Blickrichtung = Blickrichtung.turnLeft();
		return Aktion;
	}
	
	@Override
	protected AgentAction act(CompleteCavePerception Wahrnehmung) {
		// Update des aktuellen Status
		AktX = Wahrnehmung.getXPosition();
		AktY = Wahrnehmung.getYPosition();
		if(!Initialisiert) {
			// Fuege alle Goldklumpenpositionen zur Goldklumpen Liste hinzu
			erstelleGoldklumpenListe(Wahrnehmung.getCave());
			
			// Gibt es Gold? sonst brauchen wir gar nichts machen
			if(!AlleGoldklumpen.isEmpty()) {
				// Hier wird die eigentliche Suche nach dem Weg ausgefuehrt
				planeGoldsammelReihenfolge();
				// das erste Goldstueck anpeilen
				WegZumGold = findeWeg(Wahrnehmung.getCave(),
						new Pair<Integer, Integer>(AktX, AktY),
						ReihenfolgeGoldklumpen.getFirst());
			}
			Initialisiert = true;
		}
		// Ist noch Gold vorhanden, falls nicht, einfach warten
		if(ReihenfolgeGoldklumpen.isEmpty()) {
			return AgentAction.WAIT;
		}
		
		// Wir sind auf dem Weg zum naechsten Goldklumpen.
		// Sind wir auf dem aktuellen Wegpunkt?
		if(!WegZumGold.isEmpty() && (WegZumGold.getFirst().getKoordinaten().getFirstValue() == AktX) && 
				(WegZumGold.getFirst().getKoordinaten().getSecondValue() == AktY)) 
			WegZumGold.removeFirst();
		
		// Ist die Liste nicht leer, muessen wir uns weiterbewegen,
		// ansonsten brauchen wir hier nichts zu tun, sondern lassen 
		// ggf. das Gold aufheben
		if(!WegZumGold.isEmpty()) 
			return bewegeAgentNach(WegZumGold.getFirst().getKoordinaten());
		
		// sind wir an der Position des aktuellen Goldklumpens
		if(!ReihenfolgeGoldklumpen.isEmpty() &&
				(ReihenfolgeGoldklumpen.getFirst().getFirstValue() == AktX) && 
				(ReihenfolgeGoldklumpen.getFirst().getSecondValue() == AktY)) {
			ReihenfolgeGoldklumpen.removeFirst();
			// gibt es noch mehr Gold?
			if(!ReihenfolgeGoldklumpen.isEmpty()) {
				WegZumGold = findeWeg(Wahrnehmung.getCave(), 
						new Pair<Integer, Integer>(Wahrnehmung.getXPosition(), Wahrnehmung.getYPosition()),
						ReihenfolgeGoldklumpen.getFirst());
			}
			return AgentAction.GRAB_GOLD;
		}
		return AgentAction.WAIT;
	}

	public Integer getBesuchteFelder() {
		return BesuchteFelder;
	}
	@Override
	public String getName() {
		return "Goldsammel Agent";
	}

}
