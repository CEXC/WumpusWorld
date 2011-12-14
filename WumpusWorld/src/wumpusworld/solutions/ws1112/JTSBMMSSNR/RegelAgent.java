package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.NeighbourhoodPerception;



public class RegelAgent extends NeighbourhoodPerceivingAgent {
	int BesuchteFelder=0;
	@Override
	protected AgentAction act(NeighbourhoodPerception Wahrnehmung) {
		
		return AgentAction.TURN_RIGHT;
	}
	
	public int getBesuchteFelder() {
		return BesuchteFelder;
	}

	@Override
	public String getName() {
		return "RegelAgent";
	}	
}