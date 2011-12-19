package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class GoldGesehen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
								CaveGround[] Nachbarschaft, 
								NeighbourhoodPerception Wahrnehmung) {
		Anzutreffen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if((Feld != null) && Feld.isFilledWithGold())
				Anzutreffen = true;
		}
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold())
			Anzutreffen = true;
	}
	
	public GoldGesehen(boolean Anzutreffen) {
		super(SituationsStatusID.GOLDGESEHEN, Anzutreffen);
	}
	public GoldGesehen() {
		super(SituationsStatusID.GOLDGESEHEN, false);
	}
}
