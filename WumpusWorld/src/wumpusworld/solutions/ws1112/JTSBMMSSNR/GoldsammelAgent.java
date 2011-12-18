package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.CaveGroundType;
import model.wumpusworld.Orientation;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.CompleteCavePerceivingAgent;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CompleteCavePerception;

import james.SimSystem;
import james.core.math.random.generators.IRandom;
import james.core.util.eventset.Entry;
import james.core.util.eventset.SimpleEventQueue;
import james.core.util.misc.Pair;

import java.util.ArrayList;
// Ich musste hier die java.util.LinkedList nehmen,
// weil die James LinkedList nicht mit verschachtelten LinkedLists
// z. B. LinkedList<LinkedList<Wegpunkt>> kann. Dort kommt es zu 
// Verkuerzungen der inneren Liste
import java.util.LinkedList;
import java.util.List;

public class GoldsammelAgent extends CompleteCavePerceivingAgent {
	// Welches Suchverfahren soll eingesetzt werden?
	AgentenVorgehen AV = AgentenVorgehen.ASTERN; 
		
	// Informationen zum aktuellen Status des Agenten
	Integer AktX = -1;
	Integer AktY = -1; 
	Orientation Blickrichtung = Orientation.NORTH;
	int AktGoldklumpenIndex = -1;
	int BesuchteFelder=0;
	int ExpandierteKnoten = 0;
	
	// Muessen die Berechnungen bei bekanntwerden der Hoehle noch gemacht werden?
	// Liste aller Goldklumpen usw. erstellen
	boolean Initialisiert=false;
	IRandom ZufallsZahlenGen = null;
	LinkedList<Pair<Integer, Integer>> RestlicheGoldklumpen = new LinkedList<Pair<Integer, Integer>>();
	LinkedList<LinkedList<Wegpunkt>> WegeZumGold = new LinkedList<LinkedList<Wegpunkt>>();
	//LinkedList<Wegpunkt> WegZumGold = new LinkedList<Wegpunkt>();
	
	// speichert alle in der Hoehle vorhandenen Goldklumpen in der ArrayList AlleGoldklumpen
	protected void erstelleGoldklumpenListe(Cave Hoehle) {
		RestlicheGoldklumpen.clear();
		WegeZumGold.clear();
		for(int i=0; i<Hoehle.getWidth(); i++) {
			for(int j=0; j<Hoehle.getHeight(); j++) {
				if(Hoehle.getGround(i, j).isFilledWithGold()) {
					RestlicheGoldklumpen.add(new Pair<Integer, Integer>(i, j));
				}
			}
		}
	}
	
	// berechne fuer alle Goldklumpen den Weg vom aktuellen Standort aus
	protected void berechneWegeZumGold(Cave Hoehle) {
		WegeZumGold.clear();
		boolean UnerreichbareGoldklumpen=false;
		for(int i=0; i<RestlicheGoldklumpen.size(); i++) {
			LinkedList<Wegpunkt> Weg = new LinkedList<Wegpunkt>();
			Weg = findeWeg(Hoehle, new Pair<Integer, Integer>(AktX, AktY), Blickrichtung, RestlicheGoldklumpen.get(i));
			// Es gibt keinen Weg zu diesem Goldklumpen
			if(null == Weg)
				UnerreichbareGoldklumpen=true;
			WegeZumGold.addLast(Weg);
		}
		// Es gibt vllt. Goldklumpen, zu denen es keinen Weg gibt, entferne diese Goldklumpen aus der Liste
		while(UnerreichbareGoldklumpen && loescheUnereichbareGoldklumpen()) ;
	}
	
	protected boolean loescheUnereichbareGoldklumpen() {
		for(int i=0; i<RestlicheGoldklumpen.size(); i++) {
			if(null == WegeZumGold.get(i)) {
				WegeZumGold.remove(i);
				RestlicheGoldklumpen.remove(i);
				return true;
			}
		}
		return false;
	}
	
	// Heuristik fuer Wegfindung
	protected double RestStreckeLuftLinie(Pair<Integer, Integer> Position, Pair<Integer, Integer> Ziel) {
		return Math.sqrt((Position.getFirstValue()-Ziel.getFirstValue())*(Position.getFirstValue()-Ziel.getFirstValue())+
			   (Position.getSecondValue()-Ziel.getSecondValue())*(Position.getSecondValue()-Ziel.getSecondValue()));
	}
	
	// Testet ob der angegebene Wegpunkt, in der Liste enthalten ist und niedrigere Kosten hat 
	// als der in der Liste eingetragene
	private boolean hatNiedrigereKosten(Wegpunkt WP, List<Wegpunkt> WPL) {
		// falls der WP doch noch nicht in WPL hat er nat. niedrigere Kosten
		if(!WPL.contains(WP))
			return true;
		// durchlaufe Liste der Wegpunkte
		for(int i=0; i< WPL.size(); i++) {
			// Gibt es einen Wegpunkt mit den gleichen Koordinaten und der gleichen Blickrichtung
			// mit niedrigeren Kosten, hat der uebergebene WP nicht niedrigere Kosten
			if( (WPL.get(i).equals(WP)) && (WPL.get(i).getKosten() < WP.getKosten()))
				return false;
			// Falls die Kosten gleich sind, entscheidet bei ASTERNSPEZIAL die Anzahl der Aktionen
			// Bei allen anderen sind die Aktionen = 0 also hier immer false
			if((WPL.get(i).equals(WP)) && (WPL.get(i).getKosten() == WP.getKosten()) &&
					(WPL.get(i).getAktionen() <= WP.getAktionen()))
				return false;
			}
		return true;
	}
	
	// finde den kuerzesten Weg zwischen den Punken benutze je nach Einstellung entweder
	// Breitensuche, uniforme Kostensuche oder A*
	// Verbesserungs moeglichkeit: Suche den Weg mit den wenigsten Aktionen (Drehungen)
	protected LinkedList<Wegpunkt> findeWeg(Cave Hoehle, Pair<Integer, Integer> Start, 
											Orientation StartBlickrichtung, Pair<Integer, Integer> Ziel) {
		// anlegen der Prioritaetenliste
		// Dieses wird von der Aufgabenstellung gefordert, ist aber im Sinne der klassischen Implementierungen des
		// Suchverfahrens Breitensuche nicht korrekt, da die SimpleEventQueue bei Elementen mit gleicher Prioritaet 
		// nicht als FIFO arbeitet, d.h. dass wir zwar den Baum nach Tiefe geordnet, aber innerhalb einer Tiefe 
		// "zufaellig" und nicht von links nach rechts abarbeiten. 
		// Aufgrund der konstanten Kosten von 1, haben wir bei der uniformen Kostensuche das gleiche "Problem".
		SimpleEventQueue<Wegpunkt, WegpunktQualitaet> ZuBesuchen = new SimpleEventQueue<Wegpunkt, WegpunktQualitaet>();
		// Liste zum Speichern aller Besuchten Knoten
		ArrayList<Wegpunkt> BesuchteFelder = new ArrayList<Wegpunkt>();
		// Start/Ziel als Wegpunkt zum Vergleichen einfacher
		Wegpunkt Startpunkt = new Wegpunkt(Start, 0, StartBlickrichtung, 0);
		Wegpunkt Zielpunkt = new Wegpunkt(Ziel, -1);
		
		// Einfachster Fall, wird hier schnell abgehandelt:
		// Start ist Ziel
		if(Startpunkt.istZiel(Zielpunkt))
			{
			LinkedList<Wegpunkt> Weg = new LinkedList<Wegpunkt>();
			return Weg;
			}
		
		// startpunkt als anfang einfuegen
		ZuBesuchen.enqueue(Startpunkt, new WegpunktQualitaet(0));
		// Solange wir noch nicht das Ziel erreicht haben, behandeln wir ggf. den naechsten Knoten
		while(!ZuBesuchen.isEmpty()) {
			// Naechsten Punkt aus der Schlange holen
			Entry<Wegpunkt, WegpunktQualitaet> tmp = ZuBesuchen.dequeue();
			Wegpunkt WP=tmp.getEvent();
			// Ist der Wegpunkt das Ziel?
			if(WP.istZiel(Zielpunkt)) {
				 // Bestimme den gefundenen Weg
				 LinkedList<Wegpunkt> Weg = new LinkedList<Wegpunkt>();
				 Weg.addFirst(WP);
				 Wegpunkt Vorgaenger = WP.getVorgaenger();
				 while(!Vorgaenger.istZiel(Startpunkt)) {
					 Weg.addFirst(Vorgaenger);
					 Vorgaenger = Vorgaenger.getVorgaenger();
				 }
				 return Weg;
			}
			// Falls der Wegpunkt schon in der Liste der besuchten Felder enthalten ist, verwerfen
			// falls dieses Mal keine niedirgeren Kosten
			if(BesuchteFelder.contains(WP) && !hatNiedrigereKosten(WP, BesuchteFelder))
				continue;
			// Den vorhandenen Knoten rausschmeissen, wir brauchen ja keinen unnuetzen Ballast
			else if(BesuchteFelder.contains(WP) && hatNiedrigereKosten(WP, BesuchteFelder))
				BesuchteFelder.remove(WP);
			
			// Wegpunkt in die Liste der Besuchten Felder eintragen
			BesuchteFelder.add(WP);
			
			// JOHN:
			// Wann ist ein Knoten expandiert? IMHO genau dann, wenn wir seine Nachfolger durchgehen
			// Ist dies richtig?
			ExpandierteKnoten++;
			
			// Nachfolger bestimmen
			List<Pair<Integer, Integer>> NachfolgerListe = Hoehle.getVonNeumannNeighbourhoodCoordinates(
															WP.getKoordinaten().getFirstValue(), 
															WP.getKoordinaten().getSecondValue());
			// Alle Nachfolger, die noch nicht besucht wurden, in Schlange haengen
			for(int i=0; i<NachfolgerListe.size(); i++) {
				Wegpunkt Nachfolger = new Wegpunkt(NachfolgerListe.get(i), WP, WP.Kosten+1);
				// wenn Feld nicht betretbar ist, brauchen wir es auch nicht
				if(!IstFeldBetretbar(Hoehle, Nachfolger.getKoordinaten()))
					continue;
				
				// Schon besucht? dieses mal hoehere Kosten? -> Knoten verwerfen
				if(BesuchteFelder.contains(Nachfolger) && !hatNiedrigereKosten(Nachfolger, BesuchteFelder))
					continue;
				
				// Welche Blickrichtung haben wir beim Nachfolger?
				Orientation NeueBlickrichtung = getZielrichtung(WP.getKoordinaten(), Nachfolger.getKoordinaten());
				// Dies ist nur fuer ASTERNSPEZIAL notwendig, ansonsten setzen wir es auf StartBlickrichtung
				Nachfolger.setBlickrichtung( (AV==AgentenVorgehen.ASTERNSPEZIAL) ? NeueBlickrichtung : StartBlickrichtung);
				// Wie viele Aktionen werden benoetigt
				Integer AnzahlAktionen = getAnzahlAktionen(WP, Nachfolger)+WP.getAktionen();
				Nachfolger.setAktionen(AnzahlAktionen);
				// Anhaengen an Queue
				ZuBesuchen.enqueue(Nachfolger, getWegpunktQualitaet(Nachfolger, Ziel, AnzahlAktionen));
			}
		}
		return null;
	}
	
	protected WegpunktQualitaet getWegpunktQualitaet(Wegpunkt WP, Pair<Integer, Integer> Ziel, Integer AnzahlAktionen) {
		switch(AV) {
			case BREITENSUCHE: {
				// hier spiegelt getKosten einfach nur die Tiefe im Suchbaum wieder und hat nichts mit den
				// Kosten fuer den naechsten Schritt zu tun
				return new WegpunktQualitaet(WP.getKosten());
			}
			case UNIFORMEKOSTENSUCHE: {
				// hier spiegelt getKosten die Kosten bis zum Erreichen des Knotens wieder
				return new WegpunktQualitaet(WP.getKosten());	
			}
			case ASTERN: {
				return new WegpunktQualitaet(WP.getKosten(), RestStreckeLuftLinie(WP.getKoordinaten(), Ziel), 0);
			}
			case ASTERNSPEZIAL: {
				return new WegpunktQualitaet(WP.getKosten(), RestStreckeLuftLinie(WP.getKoordinaten(), Ziel), 
												AnzahlAktionen);
			}
			default: {
				return null;
			}
		}
				
	}
	
	protected boolean IstFeldBetretbar(Cave Hoehle, Pair<Integer, Integer> Koordinaten) {
		if(Hoehle.getGround(Koordinaten.getFirstValue(), Koordinaten.getSecondValue()).getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	
	// Nur implementiert fuer ASTERNSPEZIAL
	protected Integer getAnzahlAktionen(Wegpunkt Start, Wegpunkt Ziel) {
		if(AV != AgentenVorgehen.ASTERNSPEZIAL)
			return 0;
		// Ist Start = Ziel?
		if(Start.istZiel(Ziel))
			return 0;
		
		// Gucken wir in Richtung des naechsten Feldes? nur Laufen
		if(Start.getBlickrichtung() == Ziel.getBlickrichtung())
			return 1;
		
		// Wie viele Schritte muessen wir drehen und wie viele Aktionen haben wir dann insgesamt?
		if( (Start.getBlickrichtung() == Orientation.NORTH) ||
				(Start.getBlickrichtung() == Orientation.SOUTH) ) {
			if( (Ziel.getBlickrichtung() == Orientation.EAST) || (Ziel.getBlickrichtung() == Orientation.WEST) )
				return 2;
			else
				return 3;
		}
		if( (Start.getBlickrichtung() == Orientation.EAST) ||
				(Start.getBlickrichtung() == Orientation.WEST) ) {
			if( (Ziel.getBlickrichtung() == Orientation.NORTH) || (Ziel.getBlickrichtung() == Orientation.SOUTH) )
				return 2;
			else
				return 3;
		}
		return 0;
	}
	
	// die methode ist nur fuer benachbarte Felder gedacht
	protected Orientation getZielrichtung(Pair<Integer, Integer> Position, Pair<Integer, Integer> Ziel) {
		Integer PosX = Position.getFirstValue();
		Integer PosY = Position.getSecondValue();
		Integer X = Ziel.getFirstValue();
		Integer Y = Ziel.getSecondValue();
		Orientation Zielrichtung = Orientation.NORTH;
		// In welcher Richtung liegt das naechste Ziel
		if(X > PosX) Zielrichtung = Orientation.EAST;
		else if(X < PosX) Zielrichtung = Orientation.WEST;
		else if(Y > PosY) Zielrichtung = Orientation.NORTH;
		else if(Y < PosY) Zielrichtung = Orientation.SOUTH;
		return Zielrichtung;
	}
	// bewege den Agenten zu Koordinaten. Drehe ihn in die richtige Richtung und veranlasse Schritt
	protected AgentAction bewegeAgentNach(Pair<Integer, Integer> Koordinaten) {
		// Sind wir auf dem Feld?
		if( (Koordinaten.getFirstValue() == AktX) && ((Koordinaten.getSecondValue() == AktY)) )
			return AgentAction.WAIT;
		
		Orientation Zielrichtung=getZielrichtung(new Pair<Integer, Integer>(AktX, AktY), Koordinaten);
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
	
		/*// Update des aktuellen Status
		if(AgentAction.TURN_RIGHT == Aktion)
			Blickrichtung = Blickrichtung.turnRight();
		else if(AgentAction.TURN_LEFT == Aktion)
			Blickrichtung = Blickrichtung.turnLeft();*/
		return Aktion;
	}
	
	private int getNaechstenGoldklumpen() {
		// Fehler keine Goldklumpen oder keine Wege
		if(RestlicheGoldklumpen.isEmpty() || WegeZumGold.isEmpty())
			return -1;
		// alle Wege durchlaufen, Index und Laenge des Kuerzesten merken
		// So lang kann kein Weg sein
		int MinLaenge = 999999999;
		// und einen Index von -1 gibt es auch nicht
		int Index = -1;
		for(int i=0; i<WegeZumGold.size(); i++) {
			if(WegeZumGold.get(i).size() < MinLaenge) {
				MinLaenge= WegeZumGold.get(i).size();
				Index = i;
			}
		}
		return Index;
	}
	
	@Override
	protected AgentAction act(CompleteCavePerception Wahrnehmung) {
		// Update des aktuellen Status
		AktX = Wahrnehmung.getXPosition();
		AktY = Wahrnehmung.getYPosition();
		Blickrichtung = getCavePosition().getOrientation();
		if(!Initialisiert) {
			ZufallsZahlenGen = SimSystem.getRNGGenerator().getNextRNG();
			// Fuege alle Goldklumpenpositionen zur Goldklumpen Liste hinzu
			erstelleGoldklumpenListe(Wahrnehmung.getCave());
			Initialisiert = true;
		}
		// Ist noch Gold vorhanden, falls nicht, einfach warten
		if(RestlicheGoldklumpen.isEmpty()) {
			return AgentAction.WAIT;
		}
		
		// Brauchen wir einen neuen Goldklumpen als Ziel
		if(AktGoldklumpenIndex == -1)
			{
			// berechne Wege zu allen Goldklumpen
			berechneWegeZumGold(Wahrnehmung.getCave());
			// Es koennten unerreichbare Goldklumpen rausgeflogen sein
			if(RestlicheGoldklumpen.isEmpty()) {
				return AgentAction.WAIT;
			}
			// Waehle den nahesten Goldklumpen aus
			AktGoldklumpenIndex = getNaechstenGoldklumpen();
			}
		
		// Wir sind auf dem Weg zum naechsten Goldklumpen.
		// Sind wir auf dem aktuellen Wegpunkt?
		if(AktGoldklumpenIndex != -1 &&
				!WegeZumGold.isEmpty() && !WegeZumGold.get(AktGoldklumpenIndex).isEmpty() &&
				(WegeZumGold.get(AktGoldklumpenIndex).getFirst().getKoordinaten().getFirstValue() == AktX) && 
				(WegeZumGold.get(AktGoldklumpenIndex).getFirst().getKoordinaten().getSecondValue() == AktY)) 
			WegeZumGold.get(AktGoldklumpenIndex).removeFirst();
		
		// Ist die Liste nicht leer, muessen wir uns weiterbewegen,
		// ansonsten brauchen wir hier nichts zu tun, sondern lassen 
		// ggf. das Gold aufheben
		if(!WegeZumGold.get(AktGoldklumpenIndex).isEmpty()) 
			return bewegeAgentNach(WegeZumGold.get(AktGoldklumpenIndex).getFirst().getKoordinaten());
		
		// sind wir an der Position des aktuellen Goldklumpens
		if((RestlicheGoldklumpen.get(AktGoldklumpenIndex).getFirstValue() == AktX) && 
				(RestlicheGoldklumpen.get(AktGoldklumpenIndex).getSecondValue() == AktY)) {
			RestlicheGoldklumpen.remove(AktGoldklumpenIndex);
			AktGoldklumpenIndex = -1;
			return AgentAction.GRAB_GOLD;
		}
		// Hier kommen wir wohl eher nicht hin...visualisieren uns diesen Fall aber
		// trotzdem, indem der Agent sich immer im Kreis dreht
		return AgentAction.TURN_RIGHT;
	}

	public int getBesuchteFelder() {
		return BesuchteFelder;
	}
	
	public int getExpandierteKnoten() {
		return ExpandierteKnoten;
		}
	
	public AgentenVorgehen getAgentenVorgehen() {
		return AV;
		}
	public void setAgentenVorgehen(AgentenVorgehen AV) {
		this.AV = AV;
		}
	@Override
	public String getName() {
		return "Goldsammler";
	}

}
