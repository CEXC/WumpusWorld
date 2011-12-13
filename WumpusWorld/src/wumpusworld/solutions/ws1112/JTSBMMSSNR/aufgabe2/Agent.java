package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import model.wumpusworld.*;
import model.wumpusworld.agents.AgentAction;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.environment.Cave;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Agent extends NeighbourhoodPerceivingAgent{
    private Orientation Blickrichtung = Orientation.NORTH;
    public int zustand; // Gibt an in welchem Zustand (siehe Agenten_Automat.jpeg) wir sind
	private Feld felder;
	public AgentZustand automat;
    
	public Agent(){
		felder = new Feld();
		automat = new AgentZustand();
		zustand = 0; // wir starten natuerlich im Zustand 0
	}
	public int getZustand(){
		return zustand;
	}
	public void setZustand(int zustand){
		this.zustand = zustand;
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
	int berechne_Anzahl_Goldstuecke(Cave Hoehle, CavePosition[] dreierFelder){
		int anzahl = 0;
		for(CavePosition aktfeld : dreierFelder){
			if(Hoehle.getGround(aktfeld).isFilledWithGold()) 
				anzahl ++;
		}
		return anzahl;
	}
	/**
	 * Der Agent berechnet wieviele mnimale Aktionen drehen + gehen 
	 * noetig sind um das Zielfeld zu erreichen, ohne in eine
	 * Falle zu tappen.
	 * @param Hoehle
	 * @param ziel_feld
	 * @return
	 */
	int berechne_Aktionen_zum_Feld(Cave Hoehle, CavePosition ziel_feld){
		int aktionen = 0;
		
		// Falls wir schon auf dem Ziel stehen sollten brauchen wir nur 0 Aktionen
		if(ziel_feld.equals(getCavePosition())) return aktionen = 0;
		// Falls das Ziel eine Falle oder das Spielfeldende ist 
		// gibt es keine Aktionen zu zaehlen (-1)
		if(felder.isFalle(Hoehle, ziel_feld) || felder.isSpielfeldende(Hoehle, ziel_feld)) return aktionen = -1;
		// Falls sich unser Zielfeld in einer der Ecken befindet, kann der Weg dadurch versperrt werden wenn 
		// zwei Fallen unguenstig liegen
		if(felder.isNordwest(getCavePosition(), ziel_feld) && felder.isFalle(Hoehle, felder.getWest(getCavePosition())) && felder.isFalle(Hoehle, felder.getNord(getCavePosition())))
			return aktionen = -3;
		if(felder.isNordost(getCavePosition(), ziel_feld) && felder.isFalle(Hoehle, felder.getNord(getCavePosition())) && felder.isFalle(Hoehle, felder.getOst(getCavePosition())))
			return aktionen = -3;
		if(felder.isSuedost(getCavePosition(), ziel_feld) && felder.isFalle(Hoehle, felder.getOst(getCavePosition())) && felder.isFalle(Hoehle, felder.getSued(getCavePosition())))
			return aktionen = -3;
		if(felder.isSuedwest(getCavePosition(), ziel_feld) && felder.isFalle(Hoehle, felder.getSued(getCavePosition())) && felder.isFalle(Hoehle, felder.getWest(getCavePosition())))
			return aktionen = -3;
		
		if(Blickrichtung == Orientation.NORTH){
			// die trivialen faelle wenn unser Zielfeld keiner der Eckfelder ist
			if(felder.isOst(getCavePosition(), ziel_feld) || felder.isWest(getCavePosition(), ziel_feld))
				return aktionen = 2;
			if(felder.isSued(getCavePosition(), ziel_feld))
				return aktionen = 3; // wir muessen uns zwei mal drehen
			// die nicht trivialen faelle wenn unser Zielfeld einer der Eckfelder ist, dann gehen
			if(felder.isNordwest(getCavePosition(), ziel_feld) || felder.isNordost(getCavePosition(), ziel_feld)){
				// im Norden versperrt uns eine Falle den Weg also muessen wir ueber Westen/Osten gehen
				if(felder.isFalle(Hoehle,felder.getNord(getCavePosition())))
					return aktionen = 4;
				else 
					return aktionen = 3;
			}
			if(felder.isSuedwest(getCavePosition(), ziel_feld)){
				// im Westen versperrt uns eine Falle den Weg also muessen wir ueber Sueden gehen
				if(felder.isFalle(Hoehle,felder.getWest(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			if(felder.isSuedost(getCavePosition(), ziel_feld)){
				// im Osten versperrt uns eine Falle den Weg also muessen wir ueber Sueden gehen
				if(felder.isFalle(Hoehle,felder.getOst(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			else return aktionen = 1; // Zielfeld liegt genau vor uns
		}
		else if(Blickrichtung == Orientation.EAST){
			// die trivialen faelle wenn unser Zielfeld keiner der Eckfelder ist
			if(felder.isNord(getCavePosition(), ziel_feld) || felder.isSued(getCavePosition(), ziel_feld))
				return aktionen = 2;
			if(felder.isWest(getCavePosition(), ziel_feld))
				return aktionen = 3; // wir muessen uns zwei mal drehen
			// die nicht trivialen faelle wenn unser Zielfeld einer der Eckfelder ist, dann gehen
			if(felder.isSuedost(getCavePosition(), ziel_feld) || felder.isNordost(getCavePosition(), ziel_feld)){
				// im Osten versperrt uns eine Falle den Weg also muessen wir ueber Sueden/Norden gehen
				if(felder.isFalle(Hoehle,felder.getOst(getCavePosition())))
					return aktionen = 4;
				else 
					return aktionen = 3;
			}
			if(felder.isSuedwest(getCavePosition(), ziel_feld)){
				// im Sueden versperrt uns eine Falle den Weg also muessen wir ueber Westen gehen
				if(felder.isFalle(Hoehle,felder.getSued(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			if(felder.isNordwest(getCavePosition(), ziel_feld)){
				// im Norden versperrt uns eine Falle den Weg also muessen wir ueber Westen gehen
				if(felder.isFalle(Hoehle,felder.getNord(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			else return aktionen = 1; // Zielfeld liegt genau vor uns
			
		}
		else if(Blickrichtung == Orientation.SOUTH){
			// die trivialen faelle wenn unser Zielfeld keiner der Eckfelder ist
			if(felder.isWest(getCavePosition(), ziel_feld) || felder.isOst(getCavePosition(), ziel_feld))
				return aktionen = 2;
			if(felder.isNord(getCavePosition(), ziel_feld))
				return aktionen = 3; // wir muessen uns zwei mal drehen
			// die nicht trivialen faelle wenn unser Zielfeld einer der Eckfelder ist, dann gehen
			if(felder.isSuedost(getCavePosition(), ziel_feld) || felder.isSuedwest(getCavePosition(), ziel_feld)){
				// im Sueden versperrt uns eine Falle den Weg also muessen wir ueber Westen/Osten gehen
				if(felder.isFalle(Hoehle,felder.getSued(getCavePosition())))
					return aktionen = 4;
				else 
					return aktionen = 3;
			}
			if(felder.isNordwest(getCavePosition(), ziel_feld)){
				// im Westen versperrt uns eine Falle den Weg also muessen wir ueber Norden gehen
				if(felder.isFalle(Hoehle,felder.getWest(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			if(felder.isNordost(getCavePosition(), ziel_feld)){
				// im Osten versperrt uns eine Falle den Weg also muessen wir ueber Norden gehen
				if(felder.isFalle(Hoehle,felder.getOst(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			else return aktionen = 1; // Zielfeld liegt genau vor uns
			
		}
		else if(Blickrichtung == Orientation.WEST){
			// die trivialen faelle wenn unser Zielfeld keiner der Eckfelder ist
			if(felder.isNord(getCavePosition(), ziel_feld) || felder.isSued(getCavePosition(), ziel_feld))
				return aktionen = 2;
			if(felder.isOst(getCavePosition(), ziel_feld))
				return aktionen = 3; // wir muessen uns zwei mal drehen
			// die nicht trivialen faelle wenn unser Zielfeld einer der Eckfelder ist, dann gehen
			if(felder.isNordwest(getCavePosition(), ziel_feld) || felder.isSuedwest(getCavePosition(), ziel_feld)){
				// im Westen versperrt uns eine Falle den Weg also muessen wir ueber Sueden/Norden gehen
				if(felder.isFalle(Hoehle,felder.getWest(getCavePosition())))
					return aktionen = 4;
				else 
					return aktionen = 3;
			}
			if(felder.isNordost(getCavePosition(), ziel_feld)){
				// im Norden versperrt uns eine Falle den Weg also muessen wir ueber Osten gehen
				if(felder.isFalle(Hoehle,felder.getNord(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			if(felder.isSuedost(getCavePosition(), ziel_feld)){
				// im Sueden versperrt uns eine Falle den Weg also muessen wir ueber Osten gehen
				if(felder.isFalle(Hoehle,felder.getSued(getCavePosition())))
					return aktionen = 5;
				else 
					return aktionen = 4;
			}
			else return aktionen = 1; // Zielfeld liegt genau vor uns
		}
		else System.out.print("Fehlerhafte Blickrichtung");
		return aktionen;
	}
	/**
	 * Der Agent sieht den fiesen Wumpus
	 * @param Hoehle
	 * @return falls Wumpus gesehen
	 */
	boolean wumpus_gesehen(Cave Hoehle){
		if(Hoehle.getGround(getCavePosition()).isStench())
			return true;
		else
			return false;
	}
	/**
	 * Oh schreck der Agent hat den Wumpus gerochen
	 * @param Hoehle
	 * @return true falls Wumpus gerochen
	 */
	boolean wumpus_gerochen(Cave Hoehle){
		if(anzahl_gruener_Felder(Hoehle) > 0)
			return true;
		else 
			return false;
	}
	/**
	 * Die Anzahl der gruenen Felder wird vom Agenten gezaehlt
	 * @param Hoehle
	 * @return anzahl gruner Felder
	 */
	int anzahl_gruener_Felder(Cave Hoehle){
		int anzahl = 0;
		for(CavePosition aktfeld : felder.getUmgebung(getCavePosition())){
			if(Hoehle.getGround(aktfeld).isStench())
			  anzahl++;
		}
		return anzahl;
	}
	@Override
	protected AgentAction act(NeighbourhoodPerception perception) {
		// Der Zustandsautomat ist in der Datei Agent_Automat.jpeg einsehbar
		return automat.aktion(this);
	}
	@Override
	public String getName() {
		return "007";
	}
}
