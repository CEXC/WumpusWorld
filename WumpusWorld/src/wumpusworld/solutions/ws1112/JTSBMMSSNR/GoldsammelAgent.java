package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.CaveGroundType;
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
	boolean Initialisiert=false;
	public GoldsammelAgent() {			
	}
	// Heuristik fuer Wegfindung
	protected Integer RestStreckeLuftLinieZumQuadrat(Pair<Integer, Integer> Position, Pair<Integer, Integer> Ziel) {
		return (Position.getFirstValue()-Ziel.getFirstValue())*(Position.getFirstValue()-Ziel.getFirstValue())+
			   (Position.getSecondValue()-Ziel.getSecondValue())*(Position.getSecondValue()-Ziel.getSecondValue());
	}
	// finde den kuerzesten Weg zwischen den Punken benutze A* dafuer
	// Verbesserungs moeglichkeit: Suche den Weg mit den wenigsten Aktionen (Drehungen)
	protected List<Wegpunkt> findeWeg(Cave Hoehle, Pair<Integer, Integer> Start, Pair<Integer, Integer> Ziel) {
		// anlegen der Prioriaetenliste
		SimpleEventQueue<Wegpunkt, Integer> ZuBesuchen = new SimpleEventQueue<Wegpunkt, Integer>();
		// startpunkt als anfang einfuegen
		ZuBesuchen.enqueue(new Wegpunkt(Start), RestStreckeLuftLinieZumQuadrat(Start, Ziel));
		// Liste zum Speichern aller Besuchten Knoten
		List<Wegpunkt> BesuchteFelder = new ArrayList<Wegpunkt>();
		// Start/Ziel als Wegpunkt zum Vergleichen einfacher
		Wegpunkt Startpunkt = new Wegpunkt(Start);
		Wegpunkt Zielpunkt = new Wegpunkt(Ziel);
		// Solange wir noch nicht das Ziel erreicht haben, behandeln wir ggf. den naechsten Knoten
		while(!ZuBesuchen.isEmpty()) {
			// Naechsten Punkt aus der Schlange holen
			Entry<Wegpunkt, Integer> tmp = ZuBesuchen.dequeue();
			Wegpunkt WP= tmp.getEvent();
			// Ist der Wegpunkt das Ziel?
			if(WP.equals(Zielpunkt)) {
				 SimSystem.report(Level.INFO, "Weg gefunden");
				 // Bestimme den gefundenen Weg
				 LinkedList<Wegpunkt> Weg = new LinkedList<Wegpunkt>();
				 Weg.addFirst(WP);
				 System.out.println(WP.getKoordinaten().getFirstValue()+","+WP.getKoordinaten().getSecondValue());
				 Wegpunkt Vorgaenger = WP.getVorgaenger();
				 while(!Vorgaenger.equals(Startpunkt)) {
					 Weg.addFirst(Vorgaenger);
					 System.out.println(Vorgaenger.getKoordinaten().getFirstValue()+","+
							 			Vorgaenger.getKoordinaten().getSecondValue());
					 Vorgaenger = Vorgaenger.getVorgaenger();
				 }
				 Weg.addFirst(Startpunkt);
				 System.out.println(Startpunkt.getKoordinaten().getFirstValue()+","+
						 Startpunkt.getKoordinaten().getSecondValue());
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
			// Alle Nachfolger, die noch nicht besucht worden, in Schlange haengen
			for(int i=0; i<NachfolgerListe.size(); i++) {
				Wegpunkt Nachfolger  = new Wegpunkt(NachfolgerListe.get(i), WP);
				// Schon besucht? bei Breitensuche brauchen wir den nicht wieder einfuegen
				if(BesuchteFelder.contains(Nachfolger))
					continue;
				// wenn Feld nicht betretbar ist, brauchen wir es auch nicht
				if(!IstFeldBetretbar(Hoehle, Nachfolger.getKoordinaten()))
					continue;
				// Anhaengen an Queue
				ZuBesuchen.enqueue(Nachfolger, RestStreckeLuftLinieZumQuadrat(Nachfolger.getKoordinaten(), Ziel));
			}
		}
		SimSystem.report(Level.INFO, "Weg nicht gefunden");
		return null;
	}
	
	protected boolean IstFeldBetretbar(Cave Hoehle, Pair<Integer, Integer> Koordinaten) {
		if(Hoehle.getGround(Koordinaten.getFirstValue(), Koordinaten.getSecondValue()).getType() == CaveGroundType.PIT)
			return false;
		return true;
	}
	@Override
	public String getName() {
		return "Goldsammel Agent";
	}

	@Override
	protected AgentAction act(CompleteCavePerception Wahrnehmung) {
		if(!Initialisiert) {
			List<Wegpunkt> Weg = findeWeg(Wahrnehmung.getCave(), 
						new Pair<Integer, Integer>(Wahrnehmung.getXPosition(), Wahrnehmung.getYPosition()),
						new Pair<Integer, Integer>(6, 18));
			// TODO implementierung der ganzen geschichte
			// Berechnen der Wege zu allen Goldklumpen von aktuellem standort
			// Suche nach gewuenschtem naechsten Goldklumpen
			// fuege diesen in Goldklumpenschlange ein
			Initialisiert = true;
		}
		// finde aktionenfolge zum aktuellen goldklumpen 
		// arbeite diese ab
		// gold eingesammelt?
		// entferne eingesammelten goldklumpen aus schlange
		// setze aktuellen goldklumpen auf anfang der schlange 
		return AgentAction.WAIT;
	}

}
