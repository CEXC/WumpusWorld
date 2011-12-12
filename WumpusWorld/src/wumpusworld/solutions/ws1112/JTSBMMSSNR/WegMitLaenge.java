package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.core.util.misc.Pair;

public class WegMitLaenge {
	Pair<Integer, Integer> Start, Ziel;
	Integer Laenge;
	public WegMitLaenge(Pair<Integer, Integer> Start, Pair<Integer, Integer> Ziel, Integer Laenge) {
		this.Start=Start;
		this.Ziel=Ziel;
		this.Laenge=Laenge;
	}
	public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null || getClass() != Objekt.getClass()) {
			return false;
			}
		final WegMitLaenge Weg = (WegMitLaenge) Objekt;
		
		if(Start == Weg.Start && Ziel==Weg.Ziel)
			return true;
		return false;
	}
}
