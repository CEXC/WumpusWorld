package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.agents.AgentAction;

public class AgentZustand {
	
	public AgentZustand(){
	}
	
	public AgentAction aktion(Agent agent){
		int zustand = agent.getZustand();
		while(true){
			switch(zustand){
			  case 0:
				  agent.setZustand(1);
				  return AgentAction.TURN_LEFT;
			  case 1:
				  agent.setZustand(0);
				  return AgentAction.TURN_RIGHT;
			  case 2:
				  
				  break;
			  case 3:
				  
				  break;
			  case 4:
				  
				  break;
			  case 5:
				  
				  break;
			  default:
				  System.out.print("Einer unerreichbarer Zustand wurder erreicht");
			}
		}
	}
}
