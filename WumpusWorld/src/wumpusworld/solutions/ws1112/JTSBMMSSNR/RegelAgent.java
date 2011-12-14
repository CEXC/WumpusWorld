package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.NeighbourhoodPerception;


public class RegelAgent extends NeighbourhoodPerceivingAgent {
	// Buchfuehrung
	int BesuchteFelder=0;
	// Regelsatz
	LinkedList<Regel> Regeln = new LinkedList<Regel>();
	
	@Override
	protected AgentAction act(NeighbourhoodPerception Wahrnehmung) {
		Wahrnehmung.getNeighbourHood();
		Wahrnehmung.getEast();
		return AgentAction.TURN_RIGHT;
	}
	
	
	
	
	public int getBesuchteFelder() {
		return BesuchteFelder;
	}

	public void addRegel(Regel NeueRegel) {
		if(!Regeln.contains(NeueRegel))
			Regeln.add(NeueRegel);
		Collections.sort(Regeln);
	}
	
	public void addRegeln(List<Regel> NeueRegeln) {
		for(int i=0; i<NeueRegeln.size(); i++) {
			if(!Regeln.contains(NeueRegeln.get(i)))
				Regeln.add(NeueRegeln.get(i));
		}
		Collections.sort(Regeln);
	}
	@Override
	public String getName() {
		return "RegelAgent";
	}	
}