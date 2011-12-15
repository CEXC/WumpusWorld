package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.agents.AgentAction;

public class AgentZustand {
	
	public AgentZustand(){
	}
	
	public AgentAction aktion(Agent agent){
		int zustand = agent.getZustand();
		int x=100; // wenn wir 100 mal uns bewegen wollten und es jedesmal nicht klappte koennen wir
			       // beruhigt davon ausgehen dass es dem Agenten nicht moeglich ist sich zu bewegen!!!
			 	   // In diesem falls kann er nur noch warten
		while(true){
			if(x==0) return AgentAction.WAIT;
			zustand = agent.getZustand();
			switch(zustand){
			  case 0: //Goldzustand bzw. Goldmodus
				  if(agent.wumpus_gerochen() != -1){
					  agent.setZiel_Feld(agent.ideales_Fluchtfeld(agent.wumpus_gerochen()));
					  agent.setZustand(20);
					  break;
				  }
				  else if(agent.getAnsicht().getCurrentCaveGround().isFilledWithGold()){
					  return AgentAction.GRAB_GOLD;
				  }
			      else if(agent.Gold_gesehen() != -1){
			    	  agent.setZiel_Feld(agent.Gold_gesehen());
					  agent.setZustand(20);
					  break;
			      }
			      else{
					  agent.setZiel_Feld(7);
					 // agent.setNaechster_zustand(1);
				      agent.setZustand(20);
				      break;
			      }
			  case 1:
				  agent.setZiel_Feld(7);
				  agent.setNaechster_zustand(0);
				  agent.setZustand(20);
			      break;
			  case 2:
				  agent.setZustand(3);
				  return null;
			  case 3:
				  agent.setZustand(1);
				  return null;
			  case 4:
				  
				  break;
			  case 5:
				  
				  break;
			  case 20: //Bewegen Zustand
				  // falls eine Zielfeld ausgesucht wurde, was eine Falle oder 
				  // Spielfeldrand ist oder wir unser Ziel_Feld erreicht haben
				  if(agent.berechne_Aktionen_zum_Feld(agent.getZiel_Feld()) < 1 || agent.getZiel_Feld() == 8){
						agent.setZustand(agent.getNaechster_zustand());
						x--;
						break;
				  }
				  else if(agent.wumpus_gerochen() != -1){
					  agent.setZiel_Feld(agent.ideales_Fluchtfeld(agent.wumpus_gerochen()));
					  agent.setNaechster_zustand(0);
					  return agent.bewege();
				  }
				  else
					  return agent.bewege();
			  default:
				  System.out.print("Einer unerreichbarer Zustand wurder erreicht");
			}
		}
	}
}
