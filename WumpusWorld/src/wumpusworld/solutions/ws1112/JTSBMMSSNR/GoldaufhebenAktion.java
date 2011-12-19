package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class GoldaufhebenAktion extends RegelAktion {

	public GoldaufhebenAktion(int Prioritaet) {
		super(RegelAktionID.GOLDKLUMPENAUFHEBEN, Prioritaet);
	}

	@Override
	public AgentAction berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		return null;
	}

}
