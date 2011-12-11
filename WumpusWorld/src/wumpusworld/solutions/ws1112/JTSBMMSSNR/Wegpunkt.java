package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.core.util.misc.Pair;

public class Wegpunkt {
	Pair<Integer, Integer> Koordinaten;
	Wegpunkt Vorgaenger;
	
	public Wegpunkt() {
		Koordinaten = new Pair<Integer, Integer>(-1,-1);
		Vorgaenger = null;
	}
	public Wegpunkt(Wegpunkt WP) {
		setKoordinaten(WP.getKoordinaten());
		setVorgaenger(WP.getVorgaenger());
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten) {
		setKoordinaten(Koordinaten);
		setVorgaenger(null);
	}
	public Wegpunkt(Pair<Integer, Integer> Koordinaten, Wegpunkt Vorgaenger) {
		setKoordinaten(Koordinaten);
		setVorgaenger(Vorgaenger);
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
		return true;
	}
}
