package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class FliehenAktion extends RegelAktion {

	public FliehenAktion(int Prioritaet) {
		super(RegelAktionID.FLIEHEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		// @TODO Verbesserung indem das ideale Fluchtfeld, was aber zwei Drehungen
		// benoetigen wuerde, eine geringere Priorisierung bekommt
		
		// Da wir auf die aktuelle Umgebung nur mit CaveGround zugreifen koennen
		// muessen wir auch bei aktuellen CavePosition die Nachbarfelder relativ
		// halten sprich West=0, Nordwest=1, Norden =2 ...
		AgentenAktion Aktion = new AgentenAktion();
		
		LinkedList<CavePosition> GeruchsFelder = BerechneWumpusGeruchFelder(Positionen.getFirst(), Nachbarschaft);
		
		if(GeruchsFelder.size() == 0) // Wumpus wurde nirgends gerochen
			return null; // Fehlerfall, eigentlich sollte dieser Zweig nicht auftreten
		if(GeruchsFelder.size() >= 8){ // Wumpus ueberall gerochen
			Aktion.Ziel = Positionen.getFirst();
			return Aktion; // RegelAktion WAIT
		}
		LinkedList<CavePosition> FreieFeldreihe = new LinkedList<CavePosition>();
		LinkedList<CavePosition> GroessteFreieFeldreihe = new LinkedList<CavePosition>();
		CavePosition perfektes_Fluchtfeld;
		
		// Umgebung des Agenten damit ich mit Umgebung.get(x) auf die aktuell
		// betrachtete Umgebung zugreifen kann und mit der AgentenPosition vergleichen
		// kann
		LinkedList<CavePosition> Umgebung = new LinkedList<CavePosition>();
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()  ,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()-1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX(),  Positionen.getFirst().getY()-1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()-1));
		
		// muessen zweimal die Umgebung untersuchen wegen dem Ueberlauf
		// von Feld 7 zu Feld 0
		for(int i = 0; i < 17; i++){
			FreieFeldreihe.add(Umgebung.get(i%8));
			for(CavePosition feld : GeruchsFelder){
				if(feld.equals(Umgebung.get(i%8)) || !IstFeldBetretbar(Nachbarschaft[i%8])){
					FreieFeldreihe.removeLast();
					if(FreieFeldreihe.size() > GroessteFreieFeldreihe.size()){
						GroessteFreieFeldreihe.clear();
						GroessteFreieFeldreihe.addAll(FreieFeldreihe);
					}
					FreieFeldreihe.clear();
					break;
				}
			}
		}
		
		// keine Moeglichkeit zur Flucht gefunden
		if(GroessteFreieFeldreihe.size()==0){
			Aktion.Ziel = Positionen.getFirst();
			return Aktion; // RegelAktion WAIT			
		}
		// falls nur eines der Eckfelder oder mehrere Eckfelder frei sind 
		// soll gewartet werden, da dieses klueger ist!
		if(	   (GroessteFreieFeldreihe.get(0).equals(Umgebung.get(1)) || 
				GroessteFreieFeldreihe.get(0).equals(Umgebung.get(3)) ||
				GroessteFreieFeldreihe.get(0).equals(Umgebung.get(5)) ||
				GroessteFreieFeldreihe.get(0).equals(Umgebung.get(7))) &&
				GroessteFreieFeldreihe.size() < 2){
			Aktion.Ziel = Positionen.getFirst();
			return Aktion; // RegelAktion WAIT
		}
		perfektes_Fluchtfeld = GroessteFreieFeldreihe.get(GroessteFreieFeldreihe.size()-1 >> 1); // wie /2 aber mit abrunden ;)

		// falls Eckfeld muessen wir eines daneben nehmen
		if(		perfektes_Fluchtfeld.equals(Umgebung.get(1)) || 
				perfektes_Fluchtfeld.equals(Umgebung.get(3)) ||
				perfektes_Fluchtfeld.equals(Umgebung.get(5)) ||
				perfektes_Fluchtfeld.equals(Umgebung.get(7))){
			
			// falls nach dem Eckfeld kein Feld mehr kommt muessen wir das davor nehmen
			if((GroessteFreieFeldreihe.size() >> 1) + 1 < GroessteFreieFeldreihe.size())
				perfektes_Fluchtfeld = GroessteFreieFeldreihe.get((GroessteFreieFeldreihe.size() >> 1) + 1);
			else
				perfektes_Fluchtfeld = GroessteFreieFeldreihe.get((GroessteFreieFeldreihe.size() >> 1) - 1);
		}
		Aktion.Ziel = perfektes_Fluchtfeld;
		return Aktion;
	}

}
