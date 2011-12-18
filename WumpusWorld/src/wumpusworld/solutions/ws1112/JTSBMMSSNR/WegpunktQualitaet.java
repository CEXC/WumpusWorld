package wumpusworld.solutions.ws1112.JTSBMMSSNR;

public class WegpunktQualitaet implements Comparable<WegpunktQualitaet> {
	Integer Laenge;
	Double Heuristik;
	Integer Aktionen;
	
	public WegpunktQualitaet(Integer Laenge) {
		this.Laenge = Laenge;
		this.Heuristik = 0.0;
		this.Aktionen = 0;
	}
	public WegpunktQualitaet(Integer Laenge, Double Heuristik, Integer Aktionen) {
		this.Laenge = Laenge;
		this.Heuristik = Heuristik;
		this.Aktionen = Aktionen;
	}
	
	@Override
	public int compareTo(WegpunktQualitaet WPQ) {
		// Laenge geht vor, Falls der Algorithmus keine Heuristik
		// benutzt, ist diese == 0.0 und spielt daher keine Rolle
		if(Laenge+Heuristik < WPQ.Laenge+WPQ.Heuristik)
			return -1;
		else if(Laenge+Heuristik > WPQ.Laenge+WPQ.Heuristik)
			return 1;
		// Danach geht es um die Unterschiede zwischen den Aktionen
		if(Aktionen < WPQ.Aktionen)
			return -1;
		else if(Aktionen > WPQ.Aktionen)
			return 1;
		// Alles gleich...
		return 0;
	}
	
	public boolean equals(Object Objekt) {
		if (this == Objekt) {
			return true;
			}
		if (Objekt == null || getClass() != Objekt.getClass()) {
			return false;
			}
		final WegpunktQualitaet WPQ = (WegpunktQualitaet) Objekt;
		
		if( (Laenge==WPQ.Laenge) && (Aktionen == WPQ.Aktionen))
			return true;
		return false;
	}
}
