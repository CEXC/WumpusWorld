package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

// ACHTUNG:
// Es gibt Situationen in denen wir dies nicht sicher bestimmen koennen
// Wir liefern dann FALSE zurueck
// Bsp:  
/*
 * |----------------|
 * |   |   | S | W? |
 * |----------------|
 * |   | A | S | W? |
 * |----------------|
 * Spielfeldende
 */
public class WumpusVoraus extends SituationsStatus {
	@Override
	public void updateStatus(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung) {
		Orientation Blickrichtung = Positionen.getFirst().getOrientation();	
		Anzutreffen = false;
		// Geruch quer durch uns => Wumpus ist da 
		if( (null != Wahrnehmung.getNorth()) && Wahrnehmung.getNorth().isStench() && 
			(null != Wahrnehmung.getSouth()) && Wahrnehmung.getSouth().isStench()) {
			if((null != Wahrnehmung.getWest()) && Wahrnehmung.getWest().isStench() 
					&& (Blickrichtung==Orientation.WEST))
				Anzutreffen = true;
			else if((null != Wahrnehmung.getEast()) && Wahrnehmung.getEast().isStench() 
					&& (Blickrichtung==Orientation.EAST))
				Anzutreffen = true;
		}
		// Geruch quer durch uns => Wumpus ist da 
		if( (null != Wahrnehmung.getWest()) && Wahrnehmung.getWest().isStench() && 
			(null != Wahrnehmung.getEast()) && Wahrnehmung.getEast().isStench()) {
			if((null != Wahrnehmung.getNorth()) && Wahrnehmung.getNorth().isStench() 
					&& (Blickrichtung==Orientation.NORTH))
				Anzutreffen = true;
			else if((null != Wahrnehmung.getSouth()) && Wahrnehmung.getSouth().isStench() && 
					(Blickrichtung==Orientation.SOUTH))
				Anzutreffen = true;
		}
	}
	public WumpusVoraus(boolean Anzutreffen) {
		super(SituationsStatusID.WUMPUSVORAUS, Anzutreffen);
	}
	public WumpusVoraus() {
		super(SituationsStatusID.WUMPUSVORAUS, false);
	}
}
