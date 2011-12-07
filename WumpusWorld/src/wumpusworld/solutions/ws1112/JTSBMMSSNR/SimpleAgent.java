package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.CompleteCavePerceivingAgent;
import model.wumpusworld.environment.CompleteCavePerception;

public class SimpleAgent extends CompleteCavePerceivingAgent{
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "exampleClass";
	}

	@Override
	protected AgentAction act(CompleteCavePerception perception) {
		// TODO Auto-generated method stub
		return AgentAction.WAIT;
	}
}
