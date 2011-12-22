package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class RandGesehenAktion extends RegelAktion {

	public RandGesehenAktion(int Prioritaet) {
		super(RegelAktionID.PFEILABSCHIESSEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe, Orientation Blickrichtung,
			RegelAgent Agent) {
		
		for(int i=0; i<4; i++){
			if(Nachbarschaft[i*2] == null){
				Agent.Kante[i] = true;
				Agent.Kante[(i+2) %4] = false;
			}
		}
		
		// @ TODO Das hier ist irgendwie keine schoene Loesung evtl findest du was schickeres
		// Randgesehen soll ja eigentlich nur die Kanten aktualsieren :-/ und keine Aktion zurueckliefen
		// aber, dann wuerde er ja die ganze Zeit am Rand stehen bleiben :-/
		Bewegen2Aktion bew = new Bewegen2Aktion(200);
		return bew.berechneAktion(Positionen, Nachbarschaft, Wahrnehmung, StatusListe, Blickrichtung, Agent);
	}

}
