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
		// Da wir auf die aktuelle Umgebung nur mit CaveGround zugreifen koennen
		// muessen wir auch bei aktuellen CavePosition die Nachbarfelder relativ
		// halten sprich West=0, Nordwest=1, Norden =2 ...
		Aktion = new RegelAktion();
		
		LinkedList<CavePosition> wo_Wumpus_gerochen = berechneWumpusGeruchFelder();
		if(wo_Wumpus_gerochen.size() == 0) // Wumpus wurde nirgends gerochen
			return null; // Fehlerfall, eigentlich sollte dieser Zweig nicht auftreten
		if(wo_Wumpus_gerochen.size() >= 8){ // Wumpus ueberall gerochen
			Aktion.Ziel = Positionen.getFirst();
			return Aktion; // RegalAktion WAIT
		}
		LinkedList<CavePosition> freie_Feldreihe = new LinkedList<CavePosition>();
		LinkedList<CavePosition> gr_freie_Feldreihe = new LinkedList<CavePosition>();
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
		for(int i = 0; i < 16; i++){
			freie_Feldreihe.add(Umgebung.get(i%8));
			for(CavePosition feld : wo_Wumpus_gerochen){
				if(feld.equals(Umgebung.get(i%8)) || !IstFeldBetretbar(Wahrnehmung.getNeighbourHood()[i%8])){
					freie_Feldreihe.removeLast();
					if(freie_Feldreihe.size() > gr_freie_Feldreihe.size())
						gr_freie_Feldreihe = freie_Feldreihe;
					freie_Feldreihe.clear();
					break;
				}
			}
		}
		// falls nur eines der Eckfelder oder mehrere Eckfelder frei sind 
		// soll gewartet werden, da dieses klueger ist!
		if(	   (gr_freie_Feldreihe.get(0).equals(Umgebung.get(1)) || 
				gr_freie_Feldreihe.get(0).equals(Umgebung.get(3)) ||
				gr_freie_Feldreihe.get(0).equals(Umgebung.get(5)) ||
				gr_freie_Feldreihe.get(0).equals(Umgebung.get(7))) &&
				gr_freie_Feldreihe.size() < 2){
			Aktion.Ziel = Positionen.getFirst();
			return Aktion; // RegalAktion WAIT
		}
		perfektes_Fluchtfeld = gr_freie_Feldreihe.get(gr_freie_Feldreihe.size() >> 1); // wie /2 aber mit abrunden ;)

		// falls Eckfeld muessen wir eines daneben nehmen
		if(		perfektes_Fluchtfeld.equals(Umgebung.get(1)) || 
				perfektes_Fluchtfeld.equals(Umgebung.get(3)) ||
				perfektes_Fluchtfeld.equals(Umgebung.get(5)) ||
				perfektes_Fluchtfeld.equals(Umgebung.get(7))){
			
			// falls nach dem Eckfeld kein Feld mehr kommt muessen wir das davor nehmen
			if((perfektes_Fluchtfeld = gr_freie_Feldreihe.get((gr_freie_Feldreihe.size() >> 1) + 1)) == null)
				perfektes_Fluchtfeld = gr_freie_Feldreihe.get((gr_freie_Feldreihe.size() >> 1) - 1);
		}
		Aktion.Ziel = perfektes_Fluchtfeld;
		return Aktion;
	}
	protected RegelAktion berechneJagd() {
		return null;
	}
	protected RegelAktion berechneAbschuss() {
		return null;
	}
	
	// Falls wir schon auf einem Goldfeld stehen wird dieses aufgehoben
	// Ansonsten gehen wir zu den meisten Goldstuecken
	// bei himmelsrichtung ist [0] = West, [1] = Nord, [2] = Ost, [3] = Sued
	// fuer bsp himmelsrichtung[0] = [7,0,1] (Suedwest, West, Nordwest)
	protected RegelAktion berechneAufheben() {
		RegelAktion Aktion = new RegelAktion();
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
	protected RegelAktion berechneBewegung() {
		// Hat noch optimierungspotential indem wir ein erreichen des Spielfeldende
		// mit in die Zufallsberechnung mit einbeziehen
		RegelAktion Aktion = new RegelAktion();
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
	
	public int ZaehleGold(CaveGround[] Felder){
		int anzahl = 0;
		for(CaveGround feld : Felder){
			if(feld.isFilledWithGold())
				anzahl++;
		}
		return anzahl;
	}
	
	public LinkedList<CavePosition> berechneWumpusGeruchFelder(){
		LinkedList<CavePosition> GeruchsFelder = new LinkedList<CavePosition>();
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
		
		for(int i=0; i<8; i++){
			if(Wahrnehmung.getNeighbourHood()[i].isStench()){
				GeruchsFelder.add(Umgebung.get(i));
			}
		}
		
		return GeruchsFelder;
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
