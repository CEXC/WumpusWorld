package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class BewegenAktion extends RegelAktion {

	public BewegenAktion(int Prioritaet) {
		super(RegelAktionID.BEWEGEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		// Hat noch optimierungspotential indem wir ein erreichen des Spielfeldende
		// mit in die Zufallsberechnung mit einbeziehen
		AgentenAktion Aktion = new AgentenAktion();
		// Wir brauchen ja ne CavePosition fuer Aktion.Ziel
		LinkedList<CavePosition> Umgebung = new LinkedList<CavePosition>();
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()  ,Positionen.getFirst().getY()+1));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()  ));
		Umgebung.add(new CavePosition(Positionen.getFirst().getX(),  Positionen.getFirst().getY()-1));
		
		// falls in allen drei moeglichen ZielCavePositionen eine Falle ist gehen wir wieder zurueck
		if(!IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[0]) || Umgebung.get(0).equals(Positionen.get(1)))
			if(!IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[1]) || Umgebung.get(1).equals(Positionen.get(1)))
				if(!IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[2]) || Umgebung.get(2).equals(Positionen.get(1)))
					if(!IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[3]) || Umgebung.get(3).equals(Positionen.get(1))){
						Aktion.Ziel = Positionen.get(1);
						return Aktion;
					}
						
		int Zielrichtung; //0 = West, 1 = Nord, 2 = Ost, 3 = Sued
		// solange des Feld nicht betretbar oder die Zielrichtung gleich der CavePosition
		// aus der wir kommen generieren wir eine Zufallsrichtung
		do{
			Zielrichtung = (int) Math.random()*4;
		}while(!IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[Zielrichtung*2]) || 
				Positionen.get(1).equals(Umgebung.get(Zielrichtung)));
		
		Aktion.Ziel = Umgebung.get(Zielrichtung);
		return Aktion;
	}

}
