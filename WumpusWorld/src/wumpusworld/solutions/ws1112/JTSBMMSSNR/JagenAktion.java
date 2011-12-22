package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class JagenAktion extends RegelAktion {

	public JagenAktion(int Prioritaet) {
		super(RegelAktionID.WARTEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe, Orientation Blickrichtung) {
		AgentenAktion Aktion = new AgentenAktion();
		CavePosition Ziel = Positionen.getFirst(); // Falls wir keine guenstige Position kriegen lauern wir
		LinkedList<CavePosition> GeruchsFelder = BerechneWumpusGeruchFelder(Positionen.getFirst(), Nachbarschaft);

		if(GeruchsFelder.size() == 0) // Wumpus wurde nirgends gerochen
			return null; // Fehlerfall, eigentlich sollte dieser Zweig nicht auftreten
		
		
		for(int i=0; i<4; i++){
			// Falls der Geruch nur in einer Ecke ist versuchen wir eine bessere
			// Abschussposition zu bekommen
			if(Nachbarschaft[(2*i+1) %8 ] != null){
				if(Nachbarschaft[(2*i+1) %8].isStench() && !Nachbarschaft[(2*i) %8].isStench() && !Nachbarschaft[(2*i+2) %8].isStench()){
					if(IstFeldBetretbar(Nachbarschaft[(2*i+2) %8])){
						Ziel = getZielInRichtung(Positionen.getFirst(), (i+1) %4);
						break;
					}
					else{
						if(IstFeldBetretbar(Nachbarschaft[(2*i) %8])){
							Ziel = getZielInRichtung(Positionen.getFirst(), i %4);
							break;
						}
					}
				}
			}
			// Falls der Geruch direkt ueber, neben oder unter uns ist
			// drehen wir uns in dessen Richtung und schiessen
			if(Nachbarschaft[(2*i)] != null){
				if(Nachbarschaft[(2*i)].isStench()){
					Ziel = getZielInRichtung(Positionen.getFirst(), i);
					Aktion.Ziel = Ziel;
					// Falls wir in die Richtung des Feldes gucken schiessen wir
					if(GucktInZielrichtung(Positionen.getFirst(), Ziel, Blickrichtung)){
						//System.exit(0);
						Aktion.PfeilAbschiessen = true;
						return Aktion;
					}
					break;
				}
			}
		}
		Aktion.Ziel = Ziel;
		return Aktion;
	}

}
