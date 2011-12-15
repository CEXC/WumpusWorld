package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.*;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.CompleteCavePerception;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Agent extends NeighbourhoodPerceivingAgent{
    private Orientation Blickrichtung;
    public int ziel_Feld; // Gibt an in welches Feld der Agent will
    public int zustand; // Gibt an in welchem Zustand (siehe Agenten_Automat.jpeg) wir sind
	public int naechster_zustand;
	public AgentZustand automat;
	public NeighbourhoodPerception ansicht;

	public Agent(){
		Blickrichtung = Orientation.NORTH;
		automat = new AgentZustand();
		zustand = 0; // wir starten natuerlich im Zustand 0
		ziel_Feld = 8; // Feld wo der Agent drauf steht
	}
	public int getZustand() {
		return zustand;
	}
	public void setZustand(int zustand) {
		this.zustand = zustand;
	}
	public int getNaechster_zustand() {
		return naechster_zustand;
	}
	public void setNaechster_zustand(int naechsterZustand) {
		naechster_zustand = naechsterZustand;
	}
	public int getZiel_Feld() {
		return ziel_Feld;
	}
	public void setZiel_Feld(int zielFeld) {
		ziel_Feld = zielFeld;
	}
	public NeighbourhoodPerception getAnsicht() {
		return ansicht;
	}
	public void setAnsicht(NeighbourhoodPerception ansicht) {
		this.ansicht = ansicht;
	}
	
	/**
	 * Der Agent berechnet die Anzahl der Goldstuecke die 
	 * im Norden(Norden + Nordost + Nordwest) bzw.
	 * im Osten(Osten + Nordost + Suedost) bzw.
	 * im Sueden(Sueden + Suedost + Suedwest) bzw.
	 * im Westen(Westen + Nordwest + Suedwest) zu finden sind
	 * @param Hoehle
	 * @param dreierFelder bsp. (Norden, Nordwest, Nordost)
	 * @return anzahl der Goldstuecke
	 */
	public int berechne_Anzahl_Goldstuecke(CaveGround[] felder){
		int anzahl = 0;
		for(CaveGround aktfeld : felder){
			if(aktfeld.isFilledWithGold()) 
				anzahl ++;
		}
		return anzahl;
	}
	/**
	 * Der Agent versucht ein "ideales" Fluchtfeld zu finden
	 * Ist noch etwas verbesserungsmöglich, wenn man nämlich versucht
	 * die Anzahl der freien Felder zwischen den Wumpussen zu zaehlen
	 * und sich danach dann richtet wohin man geht
	 * @param wo_Wumpus_gerochen
	 * @return
	 */
	public int ideales_Fluchtfeld(int wo_Wumpus_gerochen){
		int feld,x=0;
		feld = (wo_Wumpus_gerochen + 4) % 8;
		while(getAnsicht().getNeighbourHood()[feld] == null || getAnsicht().getNeighbourHood()[feld].isStench() ||
			  getAnsicht().getNeighbourHood()[feld].getType().equals(CaveGroundType.PIT)){
			feld = (feld + 1) % 8;
			x++;
			if(x==8)
				return 8; //keine Bewegung sondern Wait
		}
		return feld;
	}
	
	/**
	 * Der Agent bewegt sich mit so wenig Zuegen wie moeglich
	 * zu seinem Ziel. Dabei errechnet er sich einfach die Anzahl
	 * der Aktionen wenn er sich links, rechts bzw gehen wuerde
	 * und wenn diese Anzahl niedriger ist als die ausgangsAnzahl der
	 * Aktionen tut er diese Aktion
	 * @param ziel_feld
	 * @return
	 */
	public AgentAction bewege(){
		int alte_anzahl_Aktionen,x;
	
		alte_anzahl_Aktionen = berechne_Aktionen_zum_Feld(getZiel_Feld());
		
		setZustand(20); // Bewegung noch nicht abgeschlossen
		
		Blickrichtung = Blickrichtung.turnLeft();
		if(berechne_Aktionen_zum_Feld(getZiel_Feld()) < alte_anzahl_Aktionen){
			return AgentAction.TURN_LEFT;
		}
		
		Blickrichtung = Blickrichtung.turnRight();
		Blickrichtung = Blickrichtung.turnRight();
		if(berechne_Aktionen_zum_Feld(getZiel_Feld()) < alte_anzahl_Aktionen){
			return AgentAction.TURN_RIGHT;
		}
		Blickrichtung = Blickrichtung.turnLeft();
		
		// vorm Gehen muss das neue Ziel_feld bestimmt werden
		// da es nach dem gehen ja "veraendert" wurde
		if(Blickrichtung == Orientation.NORTH) x=0;
		else if(Blickrichtung == Orientation.EAST) x=2;
		else if(Blickrichtung == Orientation.SOUTH) x=4;
		else x=6; // Orientation.WEST
		
		if(getZiel_Feld() == (2+x)%8) setZiel_Feld(8); // Bewegung abgeschlossen
		if(getZiel_Feld() == (1+x)%8) setZiel_Feld((0+x)%8);
		if(getZiel_Feld() == (3+x)%8) setZiel_Feld((4+x)%8);
		
		return AgentAction.GO;
	}
	/**
	 * Der Agent berechnet wieviele mnimale Aktionen drehen + gehen 
	 * noetig sind um das Zielfeld zu erreichen, ohne in eine
	 * Falle zu tappen.
	 * @param ziel_feld
	 * @return
	 */
	int berechne_Aktionen_zum_Feld(int ziel_Feld){
		int x; // wird fuer die modulo Rechnung benutzt, da das hier ein symetrisches Problem ist
		if(ziel_Feld == 8) // Das Feld wo der Agent selber drauf steht
			return 0;
		// Falls Spielfeldende
		if(getAnsicht().getNeighbourHood()[ziel_Feld] == null)
			return -1;

		// Falls das Feld eine Falle  
		// sollen keine Aktionen gezaehlt werden!
		if(getAnsicht().getNeighbourHood()[ziel_Feld].getType().equals(CaveGroundType.PIT))
			return -2;
		
		if(Blickrichtung == Orientation.NORTH) x=0;
		else if(Blickrichtung == Orientation.EAST) x=2;
		else if(Blickrichtung == Orientation.SOUTH) x=4;
		else x=6; // Orientation.WEST
		
		// Die Felder Westen, Osten und denn Modulo weiter fuer die anderen Faelle
		if(ziel_Feld == (0+x) % 8 || ziel_Feld == (4+x) % 8)
			return 2;
		// Das Feld Sueden, also gegenueber und denn mudulo so weiter
		else if(ziel_Feld == (6+x) % 8)
			return 3;
		
		// Die Eckfelder Nordwest
		else if(ziel_Feld == (1+x) % 8){
			if(getAnsicht().getNeighbourHood()[(2+x) % 8].getType().equals(CaveGroundType.PIT)){
				if(getAnsicht().getNeighbourHood()[(0+x) % 8].getType().equals(CaveGroundType.PIT))
					return -2; // zwei Fallen versperren den Weg
				return 4;
			}
			else if(getAnsicht().getNeighbourHood()[(0+x) % 8].getType().equals(CaveGroundType.PIT)){
				return 4;
			}
			else 
				return 3;
		}
		// Die Eckfelder Nordost
		else if(ziel_Feld == (3+x) % 8){
			if(getAnsicht().getNeighbourHood()[(2+x) % 8].getType().equals(CaveGroundType.PIT)){
				if(getAnsicht().getNeighbourHood()[(4+x) % 8].getType().equals(CaveGroundType.PIT))
					return -2; // zwei Fallen versperren den Weg
				return 4;
			}
			else if(getAnsicht().getNeighbourHood()[(4+x) % 8].getType().equals(CaveGroundType.PIT))
				return 4;
			else 
				return 3;
		}
		// Das Eckfeld Suedwest
		else if(ziel_Feld == (7+x) % 8){
			if(getAnsicht().getNeighbourHood()[(0+x) % 8].getType().equals(CaveGroundType.PIT)){
				if(getAnsicht().getNeighbourHood()[(6+x) % 8].getType().equals(CaveGroundType.PIT))
					return -2; // zwei Fallen versperren den Weg
				return 5;
			}
			else if(getAnsicht().getNeighbourHood()[(6+x) % 8].getType().equals(CaveGroundType.PIT)){
				return 5;
			}
			else
				return 4;
		}
		// Das Eckfeld Suedost
		else if(ziel_Feld == (6+x) % 8){
			if(getAnsicht().getNeighbourHood()[(4+x) % 8].getType().equals(CaveGroundType.PIT)){
				if(getAnsicht().getNeighbourHood()[(6+x) % 8].getType().equals(CaveGroundType.PIT))
					return -2; // zwei Fallen versperren den Weg
				return 5;
			}
			else if(getAnsicht().getNeighbourHood()[(6+x) % 8].getType().equals(CaveGroundType.PIT)){
				return 5;
			}
			else
				return 4;
		}
		else // Das Feld Norden bzw genau das feld vor dem Agenten
			return 1;
	}
	/**
	 * Der Agent sieht den fiesen Wumpus
	 * @param Hoehle
	 * @return falls Wumpus gesehen
	 */
	boolean wumpus_gesehen(){
		if(ansicht.getCurrentCaveGround().isStench())
			return true;
		else
			return false;
	}
	/**
	 * Oh schreck der Agent hat den Wumpus gerochen
	 * @param Hoehle
	 * @return true falls Wumpus gerochen
	 */
	int wumpus_gerochen(){
		int gerochen = -1;
		for(int i=0; i<8; i++){
			if(getAnsicht().getNeighbourHood()[i] != null){
				if(getAnsicht().getNeighbourHood()[i].isStench()){
					gerochen = i;
				}
			}
		}
		return gerochen;
	}
	/**
	 * Zahltag ^^
	 * @return
	 */
	int Gold_gesehen(){
		int gesehen = -1;
		for(int i=0; i<8; i++){
			if(getAnsicht().getNeighbourHood()[i] != null){
				if(getAnsicht().getNeighbourHood()[i].isFilledWithGold()){
					gesehen = i;
				}
			}
		}
		return gesehen;
	}
	
	int leeres_Feld_gesehen(){
		int gesehen = -1;
		for(int i=0; i<8; i++){
			if(getAnsicht().getNeighbourHood()[i] != null){
				if(!getAnsicht().getNeighbourHood()[i].isFilledWithGold() && 
				   !getAnsicht().getNeighbourHood()[i].isStench() &&
				    getAnsicht().getNeighbourHood()[i].getType().equals(CaveGroundType.NORMAL)){
					gesehen = i;
				}
			}
		}
		return gesehen;
	}
	/**
	 * Die Anzahl der gruenen Felder wird vom Agenten gezaehlt
	 * @param Hoehle
	 * @return anzahl gruner Felder
	 */
	int anzahl_gruener_Felder(){
		int anzahl = 0;
		for(CaveGround aktfeld : ansicht.getNeighbourHood()){
			if(aktfeld.isStench())
			  anzahl++;
		}
		if(ansicht.getCurrentCaveGround().isStench())
			anzahl++;
		return anzahl;
	}
	@Override
	protected AgentAction act(NeighbourhoodPerception perception) {
		// Der Zustandsautomat ist in der Datei Agent_Automat.jpeg einsehbar
		this.setAnsicht(perception);
		return automat.aktion(this);
	}
	@Override
	public String getName() {
		return "007";
	}
}
