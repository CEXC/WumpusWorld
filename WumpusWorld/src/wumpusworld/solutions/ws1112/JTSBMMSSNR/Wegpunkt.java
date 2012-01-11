package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import model.wumpusworld.Orientation;
import james.core.util.misc.Pair;

// Hilfsklasse fuer die verschiedenen Suchverfahren
public class Wegpunkt {
	Pair<Integer, Integer> Koordinaten;
	Wegpunkt Vorgaenger;
	Orientation Blickrichtung;
	Integer Kosten;
	Integer Aktionen;
	
	public Wegpunkt() {
		Koordinaten = new Pair<Integer, Integer>(-1,-1);
		Blickrichtung = Orientation.NORTH;
		Vorgaenger = null;
		Kosten = 0;
		Aktionen = 0;
	}
	public Wegpunkt(Wegpunkt WP) {
		this.Koordinaten = WP.Koordinaten;
		this.Vorgaenger = WP.Vorgaenger;
		this.Blickrichtung = WP.Blickrichtung;
		this.Kosten = WP.Kosten;
		this.Aktionen = WP.Aktionen;
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten, Integer Kosten) {
		this.Koordinaten = Koordinaten;
		this.Kosten = Kosten;
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten, Integer Kosten, Orientation Blickrichtung, Integer Aktionen) {
		this.Koordinaten = Koordinaten;
		this.Kosten = Kosten;
		this.Blickrichtung = Blickrichtung;
		this.Aktionen = Aktionen;
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten, Wegpunkt Vorgaenger, Integer Kosten) {
		this.Koordinaten = Koordinaten;
		this.Kosten = Kosten;
		this.Vorgaenger = Vorgaenger;
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten, Wegpunkt Vorgaenger, Integer Kosten, Orientation Blickrichtung,
			Integer Aktionen) {
		this.Koordinaten = Koordinaten;
		this.Kosten = Kosten;
		this.Vorgaenger = Vorgaenger;
		this.Aktionen = Aktionen;
		this.Blickrichtung = Blickrichtung;
	}
	
	public Pair<Integer, Integer> getKoordinaten() {
		return Koordinaten;
	}
	public void setKoordinaten(Pair<Integer, Integer> Koordinaten) {
		this.Koordinaten = Koordinaten;
	}
	public Wegpunkt getVorgaenger() {
		return Vorgaenger;
	}
	public void setVorgaenger(Wegpunkt Vorgaenger) {
		this.Vorgaenger = Vorgaenger;
	}
	public Orientation getBlickrichtung() {
		return Blickrichtung;
	}
	public void setBlickrichtung(Orientation Blickrichtung) {
		this.Blickrichtung = Blickrichtung;
	}
	public Integer getKosten() {
		return Kosten;
	}
	public void setKosten(Integer Kosten) {
		this.Kosten = Kosten;
	}
	public Integer getAktionen() {
		return Aktionen;
	}
	public void setAktionen(Integer Aktionen) {
		this.Aktionen = Aktionen;
	}
	// test ob zielpunkt, dabei spielen nur Koordinaten eine Rolle
	public boolean istZiel(Wegpunkt Ziel) {
		if(Koordinaten.getFirstValue()!=Ziel.getKoordinaten().getFirstValue()) 
			return false;
		if(Koordinaten.getSecondValue()!=Ziel.getKoordinaten().getSecondValue())
			return false;
		return true;
	}
	// es kommt nur auf die Koordinaten und Blickrichtung an
	public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null || getClass() != Objekt.getClass()) {
			return false;
			}
		final Wegpunkt WP = (Wegpunkt) Objekt;
		
		if(Koordinaten.getFirstValue()!=WP.getKoordinaten().getFirstValue()) 
			return false;
		if(Koordinaten.getSecondValue()!=WP.getKoordinaten().getSecondValue())
			return false;
		if(Blickrichtung != WP.Blickrichtung)
			return false;
		return true;
	}
}
