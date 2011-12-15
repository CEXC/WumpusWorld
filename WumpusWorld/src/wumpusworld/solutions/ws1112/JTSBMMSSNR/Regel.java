package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import java.util.LinkedList;

import model.wumpusworld.CaveGround;
import model.wumpusworld.CaveGroundType;
import model.wumpusworld.environment.CavePosition;
import model.wumpusworld.environment.NeighbourhoodPerception;

public class Regel implements Comparable<Regel> {
	// Beschreibung der Situation
	Bedingung WumpusGerochen = Bedingung.EGAL;
	Bedingung WumpusGesehen = Bedingung.EGAL;
	Bedingung WumpusVoraus = Bedingung.EGAL;
	Bedingung GoldGesehen = Bedingung.EGAL;
	Bedingung NichtsFestgestellt = Bedingung.EGAL;
	Bedingung Gefangen = Bedingung.EGAL;
	
	// Aktionen, die bei der Situation ausgefuehrt werden soll
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
	//NeighbourhoodPerception Wahrnehmung;
	//ArrayList<Orientation> BetretbareFelder = new ArrayList<Orientation>();
	
	public RegelAktion berechneRegelAktion(NeighbourhoodPerception Wahrnehmung, LinkedList<CavePosition> Positionen) {
		if(PfeilAbschiessen)
			Aktion.PfeilAbschiessen = true;
		else if(GoldklumpenAufheben)
			Aktion.GoldAufheben = true;
		else if(Warten) 
			Aktion.Ziel = Positionen.getFirst();
		return Aktion;
	}
	
	public boolean IstRegelAnwendbar(boolean WumpusGerochen, boolean WumpusGesehen, boolean WumpusVoraus,
										boolean GoldGesehen, boolean NichtsFestgestellt, boolean Gefangen) {
		if(!IstBedingungZutreffend(this.WumpusGerochen, WumpusGerochen))
			return false;
		if(!IstBedingungZutreffend(this.WumpusGesehen, WumpusGesehen))
			return false;
		if(!IstBedingungZutreffend(this.WumpusVoraus, WumpusVoraus))
			return false;
		if(!IstBedingungZutreffend(this.GoldGesehen, GoldGesehen))
			return false;
		if(!IstBedingungZutreffend(this.NichtsFestgestellt, NichtsFestgestellt))
			return false;
		if(!IstBedingungZutreffend(this.Gefangen, Gefangen))
			return false;
		return true;
	}
	
	protected boolean IstBedingungZutreffend(Bedingung B, boolean Wert) {
		if(B == Bedingung.EGAL)
			return true;
		if((B == Bedingung.ZUTREFFEND) && Wert)
			return true;
		if((B == Bedingung.NICHTZUTREFFEND) && !Wert)
			return true;
		return false;
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
		
		if((WumpusGerochen != R.WumpusGerochen) || (WumpusGesehen != R.WumpusGesehen) ||
				(GoldGesehen != R.GoldGesehen) || (NichtsFestgestellt != R.NichtsFestgestellt) ||
				(Gefangen != R.Gefangen))
			return false;

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
	public Bedingung getGefangen() {
		return Gefangen;
	}
	public void setGefangen(Bedingung Gefangen) {
		this.Gefangen = Gefangen;
	}
	public Bedingung getWumpusGerochen() {
		return WumpusGerochen;
	}
	public void setWumpusGerochen(Bedingung WumpusGerochen) {
		this.WumpusGerochen = WumpusGerochen;
	}
	public Bedingung getWumpusGesehen() {
		return WumpusGesehen;
	}
	public void setWumpusGesehen(Bedingung WumpusGesehen) {
		this.WumpusGesehen = WumpusGesehen;
	}
	public Bedingung getWumpusVoraus() {
		return WumpusVoraus;
	}
	public void setWumpusVoraus(Bedingung WumpusVoraus) {
		this.WumpusVoraus = WumpusVoraus;
	}
	public Bedingung getGoldGesehen() {
		return GoldGesehen;
	}
	public void setGoldGesehen(Bedingung GoldGesehen) {
		this.GoldGesehen = GoldGesehen;
	}
	public Bedingung getNichtsFestgestellt() {
		return NichtsFestgestellt;
	}
	public void setNichtsFestgestellt(Bedingung NichtsFestgestellt) {
		this.NichtsFestgestellt = NichtsFestgestellt;
	}
	public boolean isGoldklumpenAufheben() {
		return GoldklumpenAufheben;
	}
	public void setGoldklumpenAufheben(boolean GoldklumpenAufheben) {
		this.GoldklumpenAufheben = GoldklumpenAufheben;
	}
	public boolean isPfeilAbschiessen() {
		return PfeilAbschiessen;
	}

	public void setPfeilAbschiessen(boolean PfeilAbschiessen) {
		this.PfeilAbschiessen = PfeilAbschiessen;
	}

	public boolean isBewegen() {
		return Bewegen;
	}

	public void setBewegen(boolean Bewegen) {
		this.Bewegen = Bewegen;
	}

	public boolean isWarten() {
		return Warten;
	}

	public void setWarten(boolean Warten) {
		this.Warten = Warten;
	}

	public boolean isFliehen() {
		return Fliehen;
	}

	public void setFliehen(boolean Fliehen) {
		this.Fliehen = Fliehen;
	}

	public boolean isJagen() {
		return Jagen;
	}

	public void setJagen(boolean Jagen) {
		this.Jagen = Jagen;
	}
	
	public Regel(Regel R) {
		WumpusGerochen = R.WumpusGerochen;
		WumpusGesehen = R.WumpusGesehen;
		WumpusVoraus = R.WumpusVoraus;
		GoldGesehen = R.GoldGesehen;
		NichtsFestgestellt = R.NichtsFestgestellt;
		Gefangen = R.Gefangen;
		
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
