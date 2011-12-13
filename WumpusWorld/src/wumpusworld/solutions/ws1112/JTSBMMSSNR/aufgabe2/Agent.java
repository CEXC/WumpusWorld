package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.*;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.CompleteCavePerception;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Agent extends NeighbourhoodPerceivingAgent{
    private Orientation Blickrichtung = Orientation.NORTH;
    public int zustand; // Gibt an in welchem Zustand (siehe Agenten_Automat.jpeg) wir sind
	public AgentZustand automat;
	public NeighbourhoodPerception ansicht;

	public Agent(){
		automat = new AgentZustand();
		zustand = 0; // wir starten natuerlich im Zustand 0
	}
	public int getZustand() {
		return zustand;
	}
	public void setZustand(int zustand) {
		this.zustand = zustand;
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
	int berechne_Anzahl_Goldstuecke(CaveGround[] felder){
		int anzahl = 0;
		for(CaveGround aktfeld : felder){
			if(aktfeld.isFilledWithGold()) 
				anzahl ++;
		}
		return anzahl;
	}
	/**
	 * Der Agent berechnet wieviele mnimale Aktionen drehen + gehen 
	 * noetig sind um das Zielfeld zu erreichen, ohne in eine
	 * Falle zu tappen.
	 * @param ziel_feld
	 * @return
	 */
	int berechne_Aktionen_zum_Feld(int ziel_Feld){
		int aktionen = 0;
		if(ansicht.getNeighbourHood()[ziel_Feld].equals(CaveGroundType.PIT))
			return aktionen;
		return aktionen;
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
	boolean wumpus_gerochen(){
		if(anzahl_gruener_Felder() > 0)
			return true;
		else 
			return false;
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
