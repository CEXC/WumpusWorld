package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class PfeilabschiessenAktion extends RegelAktion {

	public PfeilabschiessenAktion(int Prioritaet) {
		super(RegelAktionID.PFEILABSCHIESSEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		return null;
	}

}
