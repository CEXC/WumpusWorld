package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class GoldaufhebenAktion extends RegelAktion {

	public GoldaufhebenAktion(int Prioritaet) {
		super(RegelAktionID.GOLDKLUMPENAUFHEBEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
			CaveGround[] Nachbarschaft, NeighbourhoodPerception Wahrnehmung,
			LinkedList<SituationsStatus> StatusListe) {
		// Falls wir schon auf einem Goldfeld stehen wird dieses aufgehoben
		// Ansonsten gehen wir zu den meisten Goldstuecken
		// bei himmelsrichtung ist [0] = West, [1] = Nord, [2] = Ost, [3] = Sued
		// fuer bsp himmelsrichtung[0] = [7,0,1] (Suedwest, West, Nordwest)
			AgentenAktion Aktion = new AgentenAktion();
			if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold()){
				Aktion.GoldAufheben = true;
				return Aktion;
			}
			if(ZaehleGold(Wahrnehmung.getNeighbourHood()) == 0){
				// Fehlerfall eigentlich sollten wir schon mindestens ein Gold 
				// gesehen haben bevor wir berechneAufheben machen
				return null;
			}
			int x=0; // kleines offset fuer die genau Bestimmung der Richtung
			int besteRichtung=0; // West=0, Nord=1, Ost=2, Sued=3
			int momentane_Anzahl=0,gr_Anzahl=0; // Anzahl der jeweiligen Goldstuecke in der jeweiligen Himmelsrichtung
			CaveGround[][] himmelsrichtung = new CaveGround[4][3];
			for(int i=0; i<4; i++){
				for(int y=0; y<3; y++){
					himmelsrichtung[i][y] = Wahrnehmung.getNeighbourHood()[y+7+x % 8];
				}
				x += 2;
				momentane_Anzahl = ZaehleGold(himmelsrichtung[i]);
				if(momentane_Anzahl > gr_Anzahl){
					if(IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[i*2])){
						besteRichtung = i;
					}
					gr_Anzahl = momentane_Anzahl;
				}
			}
			// Wir brauchen ja ne CavePosition fuer Aktion.Ziel
			LinkedList<CavePosition> Umgebung = new LinkedList<CavePosition>();
			Umgebung.add(new CavePosition(Positionen.getFirst().getX()-1,Positionen.getFirst().getY()  ));
			Umgebung.add(new CavePosition(Positionen.getFirst().getX()  ,Positionen.getFirst().getY()+1));
			Umgebung.add(new CavePosition(Positionen.getFirst().getX()+1,Positionen.getFirst().getY()  ));
			Umgebung.add(new CavePosition(Positionen.getFirst().getX(),  Positionen.getFirst().getY()-1));
			
			Aktion.Ziel = Umgebung.get(besteRichtung);
		return Aktion;
	}

}
