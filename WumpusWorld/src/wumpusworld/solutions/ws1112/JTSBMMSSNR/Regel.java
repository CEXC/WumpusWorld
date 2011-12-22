package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.Collections;
import java.util.LinkedList;

import model.wumpusworld.Orientation;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Regel implements Comparable<Regel> {
	// StatusListe zur Beschreibung der Situation
	LinkedList<SituationsStatus> StatusListe = new LinkedList<SituationsStatus>();
	// Aktionen, die bei der Situation ausgefuehrt werden sollen
	// sie werden von hoher zu niedriger Prioritaet durchprobiert
	LinkedList<RegelAktion> AktionenListe = new LinkedList<RegelAktion>();
	
	// berechnet
	AgentenAktion Aktion = new AgentenAktion();

	// Prioritaet groesser ist wichtiger Prioritaet >= 1
	int Prioritaet = 1;
		
	// Die aktuelle Situationbeschreibende Variablen
	NeighbourhoodPerception Wahrnehmung;
	//ArrayList<Orientation> BetretbareFelder = new ArrayList<Orientation>();
	LinkedList<CavePosition> Positionen;
	// Die aktuelle Blickrichtung
	Orientation Blickrichtung;
	
	public AgentenAktion berechneRegelAktion(NeighbourhoodPerception Wahrnehmung, 
											LinkedList<CavePosition> Positionen,
											LinkedList<SituationsStatus> AgentenStatusListe,
											Orientation Blickrichtung,
											RegelAgent Agent) {
		this.Wahrnehmung = Wahrnehmung;
		this.Positionen = Positionen;
		this.Blickrichtung = Blickrichtung;
		
		Aktion = null;
		for(RegelAktion RA : AktionenListe) {
			Aktion = RA.berechneAktion(Positionen, Wahrnehmung.getNeighbourHood(), Wahrnehmung, AgentenStatusListe, Blickrichtung, Agent);
			if(Aktion != null)
				break;
		}
		// Standard: nichts machen und bei aktueller Position bleiben...
		if(Aktion == null) {
			Aktion = new AgentenAktion();
			Aktion.Ziel = Positionen.getFirst();
		}
		return Aktion;
	}
	
	public boolean IstRegelAnwendbar(LinkedList<SituationsStatus> AgentenStatusListe) {
		for(SituationsStatus Status : this.StatusListe) {
			// Wenn der Status vom Agenten gar nicht zur Verfuegung gestellt wird,
			// kann er in unserem Sinne nicht gelten
			if(!AgentenStatusListe.contains(Status))
				return false;
			if(Status.istAnzutreffen() != AgentenStatusListe.get(AgentenStatusListe.indexOf(Status)).istAnzutreffen())
				return false;
		}
		return true;
	}
	
	public void addStatus(SituationsStatus Status) {
		if(!StatusListe.contains(Status))
			StatusListe.add(Status);
	}
	
	public void resetStatusListe() {
		StatusListe.clear();
	}
	
	public void addAktion(RegelAktion RA) {
		if(!AktionenListe.contains(RA))
			AktionenListe.add(RA);
		Collections.sort(AktionenListe);
		}
	
	public void resetAktionenListe() {
		AktionenListe.clear();
	}
	
	// Wir sortieren Regeln nach deren Prioritaet wichtiger vor weniger wichtig
	@Override
	public int compareTo(Regel R) {
		if(Prioritaet > R.Prioritaet)
			return -1;
		else if(Prioritaet < R.Prioritaet)
			return 1;
		// Alles gleich...
		return 0;
	}
	
	// Vergleich wobei die Prioritaet keine Rolle spielt
	public boolean VergleichOhneP(Regel R) {
		if(this == R) {
			return true;
			}
		
		//StatusListe
		if(StatusListe.size() != R.StatusListe.size())
			return false;
		for(SituationsStatus Status : StatusListe) {
			if(!R.StatusListe.contains(Status))
				return false;
		}
			
		// AktionenListe
		if(AktionenListe.size() != R.AktionenListe.size())
			return false;
		for(RegelAktion RA : AktionenListe) {
			if(!R.AktionenListe.contains(RA))
				return false;
		}
		
		return true;
	}
	
	// Prioritaet spielt eine Rolle
	public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null || getClass() != Objekt.getClass()) {
			return false;
			}
		final Regel R = (Regel) Objekt;
		
		if(Prioritaet != R.Prioritaet)
			return false;
		
		return VergleichOhneP(R);
	}
	
	public void setPrioritaet(int Prioritaet) {
		if(Prioritaet >= 1) 
			this.Prioritaet = Prioritaet;
	}
	
	public int getPrioritaet() {
		return Prioritaet;
	}
	
	public Regel(Regel R) {
		StatusListe.addAll(R.StatusListe);
		AktionenListe.addAll(R.AktionenListe);
		Prioritaet = R.Prioritaet;
		
		Aktion = R.Aktion;
	}
	
	public Regel() {
	}
}
