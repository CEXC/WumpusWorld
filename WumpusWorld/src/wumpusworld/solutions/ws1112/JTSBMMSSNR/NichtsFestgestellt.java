package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class NichtsFestgestellt extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung, Orientation Blickrichtung) {
		Anzutreffen = true;
		for(CaveGround Feld : Nachbarschaft) {
			// Wir sind an einer Seite, es gibt Gold, der Wumpus ist in der Naehe
			if((null == Feld) || (Feld.isFilledWithGold()) || (Feld.isStench())) { 
				Anzutreffen = false;
				return ;
			}
		}
		// es kann auch noch was an der aktuellen Position sein
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold() || 
			Wahrnehmung.getCurrentCaveGround().isStench()) {
			Anzutreffen = false;
		}
	}
	
	public NichtsFestgestellt(boolean Anzutreffen) {
		super(SituationsStatusID.NICHTSFESTGESTELLT, Anzutreffen);
	}
	public NichtsFestgestellt() {
		super(SituationsStatusID.NICHTSFESTGESTELLT, false);
	}
}
