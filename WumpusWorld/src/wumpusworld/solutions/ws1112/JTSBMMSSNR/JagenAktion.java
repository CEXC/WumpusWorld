package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class JagenAktion extends RegelAktion {

	public JagenAktion(int Prioritaet) {
		super(RegelAktionID.WARTEN, Prioritaet);
	}

	@Override
	public AgentAction berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		return null;
	}

}
