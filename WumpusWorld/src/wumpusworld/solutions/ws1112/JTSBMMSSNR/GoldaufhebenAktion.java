package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class GoldaufhebenAktion extends RegelAktion {

	public GoldaufhebenAktion(int Prioritaet) {
		super(RegelAktionID.GOLDKLUMPENAUFHEBEN, Prioritaet);
	}

	@Override
	public AgentenAktion berechneAktion(LinkedList<CavePosition> Positionen,
										CaveGround[] Nachbarschaft, 
										NeighbourhoodPerception Wahrnehmung,
										LinkedList<SituationsStatus> StatusListe,
										Orientation Blickrichtung) {
		// Falls wir schon auf einem Goldfeld stehen wird dieses aufgehoben
		// Ansonsten gehen wir zu den meisten Goldstuecken
		// bei himmelsrichtung ist [0] = West, [1] = Nord, [2] = Ost, [3] = Sued
		// fuer bsp himmelsrichtung[0] = [7,0,1] (Suedwest, West, Nordwest)
		AgentenAktion Aktion = new AgentenAktion();
		if(Wahrnehmung.getCurrentCaveGround().isFilledWithGold()){
			Aktion.GoldAufheben = true;
			return Aktion;
		}
		// Es gibt kein Gold in unserer Umgebung, also koennen wir auch keines Aufheben
		if(ZaehleGold(Nachbarschaft) == 0){
			return null;
		}
		int BesteRichtung=-1; // West=0, Nord=1, Ost=2, Sued=3; am Anfang noch keine Richtung berechnet
		int AusweichRichtung=0; // Falls das Goldstueck nicht erreichbar ist
		// Anzahl der jeweiligen Goldstuecke in der jeweiligen Himmelsrichtung
		int MomentaneAnzahl=0,GroessteAnzahl=0; 
		// Zwischenspeicher als Vereinfachung
		for(int i=0; i<4; i++){
			MomentaneAnzahl=0;
			for(int y=0; y<3; y++){
				if((Nachbarschaft[(7+i*2+y)%8] != null) && Nachbarschaft[(7+i*2+y)%8].isFilledWithGold())
					MomentaneAnzahl++;
			}
			if(MomentaneAnzahl >= GroessteAnzahl && IstFeldBetretbar(Nachbarschaft[i*2])){
					BesteRichtung = i;
					GroessteAnzahl = MomentaneAnzahl;
			}
			if(IstFeldBetretbar(Nachbarschaft[i*2]))
				AusweichRichtung = i;
		}
		// Leider versprerrt uns eine Falle den weg
		if(BesteRichtung == -1)
			BesteRichtung = AusweichRichtung;
		Aktion.Ziel = getZielInRichtung(Positionen.getFirst(), BesteRichtung);
		return Aktion;
	}

}
