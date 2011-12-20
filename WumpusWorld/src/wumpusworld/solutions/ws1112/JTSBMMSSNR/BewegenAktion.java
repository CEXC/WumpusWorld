package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;
import james.core.math.random.generators.IRandom;

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
		
		// bisher hatten wir noch nie ein Ziel gesetzt
		if(null == Ziel)
			Ziel = Positionen.getFirst();
		
		// Es gibt ein Ziel, jetzt muessen wir herausfinden, 
		// ob es ein aktuelles Ziel ist, oder ob wir ein neues berechnen muessen
		if(!Ziel.coordinatesEqual(Positionen.getFirst())) {
			// ist das Ziel ein Nachbarfeld?
			for(int i=0; i<4; i++) {
				if(Ziel.coordinatesEqual(getZielInRichtung(Positionen.getFirst(), i))) {
					// ist das Feld betretbar? => Ziel weiterverwenden
					if(IstFeldBetretbar(Nachbarschaft[i*2])) {
						Aktion.Ziel = Ziel;
						return Aktion;
					}
				}
			}
		}
		// Wir muessen ein neues Ziel berechnen
		
		// Falls bisher nur auf einem Feld gewesen
		CavePosition LetztePosition = Positionen.getFirst();
		if(Positionen.size() > 1)
			LetztePosition = Positionen.get(1);
		// Moegliche Ziele
		LinkedList<CavePosition> Ziele = new LinkedList<CavePosition>();
		for(int i=0; i<4; i++) {
			if(IstFeldBetretbar(Nachbarschaft[i*2]))
					Ziele.add(getZielInRichtung(Positionen.getFirst(), i));
		}
		
		// kein Zielfeld?
		if(Ziele.size() == 0)
			return null;
		
		// Nur ein Feld betretbar, also muessen wir dorthin
		if(Ziele.size() == 1) {
			Aktion.Ziel = Ziele.getFirst();
			return Aktion;
		}
		
		// loeschen der LetztePosition aus der Zielliste, es gibt ja neue Ziele
		// TODO sicherstellen, dass die position auch geloescht wird, falls andere Blickrichtung!
		Ziele.remove(LetztePosition);
		// zufaellig eines der uebrigen Ziele auswaehlen
		IRandom ZufallsZahlenGen = null;
		ZufallsZahlenGen = SimSystem.getRNGGenerator().getNextRNG();
		Aktion.Ziel = Ziele.get(ZufallsZahlenGen.nextInt(Ziele.size()));
		// Ziel in Speicher packen, damit wir uns auch beim naechsten Aufruf dorthin bewegen
		// und nicht einfach wild rumdrehen
		Ziel = Aktion.Ziel;
		return Aktion;
	}
	CavePosition Ziel=null;
}
