package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CavePosition;

public class Feld{
	
	public Feld(){
	}
	/**
	 * Liefert alle Felder um den Agenten zurück
	 * @param aktPosition
	 * @return
	 */
	public CavePosition[] getUmgebung(CavePosition aktPosition) {
		CavePosition[] umgebung = null;
		umgebung[0] = getWest(aktPosition);
		umgebung[1] = getNordwest(aktPosition);
		umgebung[2] = getNord(aktPosition);
		umgebung[3] = getNordost(aktPosition);
		umgebung[4] = getOst(aktPosition);
		umgebung[5] = getSuedost(aktPosition);
		umgebung[6] = getSued(aktPosition);
		umgebung[7] = getSuedwest(aktPosition);
		umgebung[8] = aktPosition;
		return umgebung;
	}
	/**
	 * Gibt alle Felder zurueck die im Norden liegen
	 * @param aktPosition
	 * @return
	 */
	public CavePosition[] getNordFelder(CavePosition aktPosition){
		CavePosition[] felder = null;
		felder[0] = getNordwest(aktPosition);
		felder[1] = getNord(aktPosition);
		felder[2] = getNordost(aktPosition);
		return felder;
	}
	/**
	 * Gibt alle Felder zurueck die im Osten liegen
	 * @param aktPosition
	 * @return
	 */
	public CavePosition[] getOstFelder(CavePosition aktPosition){
		CavePosition[] felder = null;
		felder[0] = getNordost(aktPosition);
		felder[1] = getOst(aktPosition);
		felder[2] = getSuedost(aktPosition);
		return felder;
	}
	/**
	 * Gibt alle Felder zurueck die im Sueden liegen
	 * @param aktPosition
	 * @return
	 */
	public CavePosition[] getSuedFelder(CavePosition aktPosition){
		CavePosition[] felder = null;
		felder[0] = getSuedwest(aktPosition);
		felder[1] = getSued(aktPosition);
		felder[2] = getSuedost(aktPosition);
		return felder;
	}
	/**
	 * Gibt alle Felder zurueck die im Westen liegen
	 * @param Hoehle
	 * @param aktPosition
	 * @return
	 */
	public CavePosition[] getWestFelder(CavePosition aktPosition){
		CavePosition[] felder = null;
		felder[0] = getNordwest(aktPosition);
		felder[1] = getWest(aktPosition);
		felder[2] = getSuedwest(aktPosition);
		return felder;
	}
	boolean isSpielfeldende(Cave Hoehle, CavePosition ziel_Position){
		if(ziel_Position.getX() < 0 || ziel_Position.getX() > Hoehle.getWidth()) return true;
		if(ziel_Position.getY() < 0 || ziel_Position.getY() > Hoehle.getHeight()) return true;
		return false;
	}
	/**
	 * Ermittelt ob die ziel_Position eine Falle ist
	 * @param Hoehle
	 * @param ziel_Position
	 * @return
	 */
	boolean isFalle(Cave Hoehle, CavePosition ziel_Position){
		if(Hoehle.getGround(ziel_Position).getType().equals(CaveGroundType.PIT))
			return true;
		return false;
	}
	/**
	 * Ueberprueft ob ziel_Position das westliche Feld ist
	 * @param aktPosition
	 * @param ziel_Position
	 * @return
	 */
	boolean isWest(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getWest(aktPosition))) return true;
		return false;
	}
	boolean isNordwest(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getNordwest(aktPosition))) return true;
		return false;
	}
	boolean isNord(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getNord(aktPosition))) return true;
		return false;
	}
	boolean isNordost(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getNordost(aktPosition))) return true;
		return false;
	}
	boolean isOst(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getOst(aktPosition))) return true;
		return false;
	}
	boolean isSuedost(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getSuedost(aktPosition))) return true;
		return false;
	}
	boolean isSued(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getSued(aktPosition))) return true;
		return false;
	}
	boolean isSuedwest(CavePosition aktPosition, CavePosition ziel_Position){
		if(ziel_Position.equals(this.getSuedwest(aktPosition))) return true;
		return false;
	}
	
	/**
	 * berechnet die relativen Himmelsrichtungen
	 * @param aktPosition
	 * @return
	 */
	public CavePosition getWest(CavePosition aktPosition) {
	    return new CavePosition(aktPosition.getX()-1,aktPosition.getY());
	}
	public CavePosition getNordwest(CavePosition aktPosition) {
		return new CavePosition(aktPosition.getX()-1,aktPosition.getY()+1);
	}
	public CavePosition getNord(CavePosition aktPosition) {
	    return new CavePosition(aktPosition.getX(),aktPosition.getY()+1);
	}
	public CavePosition getNordost(CavePosition aktPosition) {
		return new CavePosition(aktPosition.getX()+1,aktPosition.getY()+1);
	}
	public CavePosition getOst(CavePosition aktPosition) {
	    return new CavePosition(aktPosition.getX()+1,aktPosition.getY());
	}
	public CavePosition getSuedost(CavePosition aktPosition) {
		return new CavePosition(aktPosition.getX()+1,aktPosition.getY()-1);
	}
	public CavePosition getSued(CavePosition aktPosition) {
		return new CavePosition(aktPosition.getX(),aktPosition.getY()-1);
	}
	public CavePosition getSuedwest(CavePosition aktPosition) {
		return new CavePosition(aktPosition.getX()-1,aktPosition.getY()-1);
	}
}
