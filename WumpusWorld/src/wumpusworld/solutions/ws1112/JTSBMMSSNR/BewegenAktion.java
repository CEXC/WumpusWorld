package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class BewegenAktion extends RegelAktion {

	public BewegenAktion(int Prioritaet) {
		super(RegelAktionID.BEWEGEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		return null;
	}

}
