package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class GoldGesehen extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
<<<<<<< HEAD
<<<<<<< HEAD
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung) {
=======
								CaveGround[] Nachbarschaft, 
								NeighbourhoodPerception Wahrnehmung) {
>>>>>>> master
=======
								CaveGround[] Nachbarschaft, 
								NeighbourhoodPerception Wahrnehmung) {
>>>>>>> master
		Anzutreffen = false;
		for(CaveGround Feld : Nachbarschaft) {
			if((Feld != null) && Feld.isFilledWithGold())
				Anzutreffen = true;
		}
<<<<<<< HEAD
<<<<<<< HEAD
=======
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold())
			Anzutreffen = true;
>>>>>>> master
=======
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold())
			Anzutreffen = true;
>>>>>>> master
	}
	
	public GoldGesehen(boolean Anzutreffen) {
		super(SituationsStatusID.GOLDGESEHEN, Anzutreffen);
	}
	public GoldGesehen() {
		super(SituationsStatusID.GOLDGESEHEN, false);
	}
}
