package wumpusworld.solutions.ws1112.JTSBMMSSNR;

public class WegpunktQualitaet implements Comparable<WegpunktQualitaet> {
	Integer Laenge;
	Integer Aktionen;
	
	public WegpunktQualitaet(Integer Laenge) {
		this.Laenge = Laenge;
		this.Aktionen = 0;
	}
	public WegpunktQualitaet(Integer Laenge, Integer Aktionen) {
		this.Laenge = Laenge;
		this.Aktionen = Aktionen;
	}
	
	@Override
	public int compareTo(WegpunktQualitaet WPQ) {
		final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
		// Laenge geht vor
		if(Laenge < WPQ.Laenge)
			return -1;
		else if(Laenge > WPQ.Laenge)
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
