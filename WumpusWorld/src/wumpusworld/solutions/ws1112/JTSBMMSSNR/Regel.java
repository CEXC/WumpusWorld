package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Regel implements Comparable<Regel> {
	// StatusListe zur Beschreibung der Situation
	LinkedList<SituationsStatus> StatusListe = new LinkedList<SituationsStatus>();
	
	// Aktionen, die bei der Situation ausgefuehrt werden sollen
	// Koennte auch als Liste sehr flexibel gehandhabt werden
	boolean GoldklumpenAufheben = false;
	boolean PfeilAbschiessen = false;
	boolean Bewegen = false;
	boolean Warten = false;
	boolean Fliehen = false;
	boolean Jagen = false;
	// berechnet
	RegelAktion Aktion = new RegelAktion();

	// Prioritaet groesser ist wichtiger Prioritaet >= 1
	int Prioritaet = 1;
		
	// Die aktuelle Situationbeschreibende Variablen
	NeighbourhoodPerception Wahrnehmung;
	//ArrayList<Orientation> BetretbareFelder = new ArrayList<Orientation>();
	LinkedList<CavePosition> Positionen;
	
	public RegelAktion berechneRegelAktion(NeighbourhoodPerception Wahrnehmung, LinkedList<CavePosition> Positionen) {
		this.Wahrnehmung = Wahrnehmung;
		this.Positionen = Positionen;
		
		Aktion = null;
		// Diese Abarbeitungsreihenfolge ist jetzt erstmal fest eingebaut
		// ggf. koennte man diese aber wiederum auch mittels Prioritaeten festlegen,
		// falls jmd. zu viel Zeit hat:
		// Aktionen, die bei Aktion ausfuehrbar sein sollen als Enum codieren
		// eine Liste mit Tripel (fuer Tripel sollte es in core eine Hilfsklasse fuer geben)
		// anlegen, wobei ein Tripel aus "enum der Aktionen", boolean, Prioritaet besteht
		// sortieren nach Prioritaet und dann hier von hoehster bis niedrigster durchlaufen+
		// anwenden. 
		// Oder man implementiert es wie die Situationsbeschreibung. Mal schauen,
		// wie viel Lust ich in der naechsten Woche dazu habe
		// waere vermutlich gut, da man so ganz einfach neue Ideen fuer Aktionen umsetzen kann.
		// Durchreichen zu spaeteren Optionen Beispiel:
		// Fliehen und Jagen = true; fuers Fliehen gibt es keine Sinnvolle Option => ich will 
		// doch Jagen; berechneFlucht() liefert null zurueck
		if((Aktion == null) && Fliehen)
			Aktion = berechneFlucht();
		if((Aktion == null) && Jagen)
			Aktion = berechneJagd();
		if((Aktion == null) && PfeilAbschiessen)
			Aktion = berechneAbschuss();
		if((Aktion == null) && GoldklumpenAufheben)
			Aktion = berechneAufheben();
		if((Aktion == null) && Bewegen)
			Aktion = berechneBewegung();
		if((Aktion == null) && Warten) {
			Aktion = new RegelAktion();
			Aktion.Ziel = Positionen.getFirst();
		}

		// Standard: nichts machen und bei aktueller Position bleiben...
		if(Aktion == null) {
			Aktion = new RegelAktion();
			Aktion.Ziel = Positionen.getFirst();
		}
		return Aktion;
	}
	
	protected RegelAktion berechneFlucht() {
		return null;
	}
	protected RegelAktion berechneJagd() {
		return null;
	}
	protected RegelAktion berechneAbschuss() {
		return null;
	}
	protected RegelAktion berechneAufheben() {
		return null;
	}
	protected RegelAktion berechneBewegung() {
		return null;
	}
	public boolean IstRegelAnwendbar(LinkedList<SituationsStatus> StatusListe) {
		for(SituationsStatus Status : this.StatusListe) {
			// Wenn der Status vom Agenten gar nicht zur Verfuegung gestellt wird,
			// kann er in unserem Sinne nicht gelten
			if(!StatusListe.contains(Status))
				return false;
			if(Status.istAnzutreffen() != StatusListe.get(StatusListe.indexOf(Status)).istAnzutreffen())
				return false;
		}
		return true;
	}
	
	public void addStatus(SituationsStatus Status) {
		if(!StatusListe.contains(Status))
			StatusListe.add(Status);
	}
		
 	protected boolean IstFeldBetretbar(CaveGround Feld) {
		if(Feld == null)
			return false;
		if(Feld.getType() == CaveGroundType.PIT)
			return false;
		return true;
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
			
		if((GoldklumpenAufheben != R.GoldklumpenAufheben) || (PfeilAbschiessen != R.PfeilAbschiessen) ||
				(Bewegen != R.Bewegen) || (Warten != R.Warten) || (Fliehen != R.Fliehen) || 
				(Jagen!= R.Jagen))
			return false;
		
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
	public void setAktion(RegelAktion Aktion) {
		this.Aktion = Aktion;
	}

	public boolean istGoldklumpenAufheben() {
		return GoldklumpenAufheben;
	}
	public void setGoldklumpenAufheben(boolean GoldklumpenAufheben) {
		this.GoldklumpenAufheben = GoldklumpenAufheben;
	}
	public boolean istPfeilAbschiessen() {
		return PfeilAbschiessen;
	}

	public void setPfeilAbschiessen(boolean PfeilAbschiessen) {
		this.PfeilAbschiessen = PfeilAbschiessen;
	}

	public boolean istBewegen() {
		return Bewegen;
	}

	public void setBewegen(boolean Bewegen) {
		this.Bewegen = Bewegen;
	}

	public boolean istWarten() {
		return Warten;
	}

	public void setWarten(boolean Warten) {
		this.Warten = Warten;
	}

	public boolean istFliehen() {
		return Fliehen;
	}

	public void setFliehen(boolean Fliehen) {
		this.Fliehen = Fliehen;
	}

	public boolean istJagen() {
		return Jagen;
	}

	public void setJagen(boolean Jagen) {
		this.Jagen = Jagen;
	}
	
	public Regel(Regel R) {
		StatusListe.addAll(R.StatusListe);
		
		GoldklumpenAufheben = R.GoldklumpenAufheben;
		PfeilAbschiessen = R.PfeilAbschiessen;
		Bewegen = R.Bewegen;
		Warten = R.Warten;
		Fliehen = R.Fliehen;
		Jagen = R.Jagen;
		Prioritaet = R.Prioritaet;
		
		Aktion = R.Aktion;
	}
	
	public Regel() {
	}
}
