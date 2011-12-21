package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


import examples.wumpusworld.exercises.ExerciseUtils;

public class TestRegelAgent {

	/**
	 * @param args	
	 * 	Agent und Simulation sind durch CMD Parameter zu beeinflussen
	 * 
		Seed fuer den Random Number Generator: Standard = kein eigenes Seed 
													- RXXXXXXXXL Wobei XXXXXX fuer einen Long steht, der mit L beendet wird
		Visualisierung:							Standard = false
													- V = true
		Zeitdauer zwischen Schritten in ms:		Standard = 0L
													- ZXXXXXXXXL Wobei XXXXXX fuer einen Long steht, der mit L beendet wird
	 	Anzahl zu simulierender Hoehlen:		Standard = 1
	 												- HXXXXXXXXI Wobei XXXXXX fuer einen Int steht, der mit I beendet wird
	 */
	public static void main(String[] args) {		
	    long NeuesSeed = 0L;
	    boolean SeedErhalten = false;
	    boolean Visualisierung = false;
	    long PauseZwSchritten = 0L;
	    int AnzahlSimulationen = 1;
	    for(int i=0; i< args.length; i++) {
	    	if(args[i].equals("V"))
	    		Visualisierung = true;
	    	else if(args[i].startsWith("Z") && args[i].endsWith("L") && args[i].length() > 2) {
	    		PauseZwSchritten = Long.parseLong(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("H") && args[i].endsWith("I") && args[i].length() > 2) {
	    		AnzahlSimulationen = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("R") && args[i].endsWith("L") && args[i].length() > 2) {
	    		NeuesSeed = Long.parseLong(args[i].substring(1,args[i].length()-1));
	    		SimSystem.getRNGGenerator().setSeed(NeuesSeed);
	    		SeedErhalten = true;
	    	}
	    }
	    
	    int GesamtErgebnis = 0;
	    int GesamtBesuchteFelder = 0;
	    
	    ArrayList<Regel> Regeln = new ArrayList<Regel>();
	    
	    Regel R = new Regel();
    
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new GoldGesehen(true));
	    R.addAktion(new GoldaufhebenAktion(200));
	//    R.setPrioritaet(200);
	    Regeln.add(new Regel(R));
	    
	    
	    
	    /*
	    R.addStatus(new WumpusVoraus(true));
	    R.addAktion(new PfeilabschiessenAktion(100));
	    R.setPrioritaet(400);
	    Regeln.add(new Regel(R));
	    
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new RandGesehen(true));
	    R.addAktion(new BewegenAktion(190));
	  //  R.setPrioritaet(300);
	    Regeln.add(new Regel(R));*/
	    
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new NichtsFestgestellt(true));
	    R.addAktion(new BewegenAktion(60));
	   // R.setPrioritaet(10);
	    Regeln.add(new Regel(R));
	    
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGerochen(true));
	    R.addAktion(new FliehenAktion(500));
	 //  R.setPrioritaet(300);
	    Regeln.add(new Regel(R));
	    
	    for(int i=0; i<AnzahlSimulationen; i++) {
			int Ergebnis = 0;
			int BesuchteFelder = 0;
		    RegelAgent Regeler = new RegelAgent();
		    // SituationsStatus hinzufuegen
		    Regeler.addSituationsStatus(new WumpusVoraus());
		    Regeler.addSituationsStatus(new WumpusGerochen());
		    Regeler.addSituationsStatus(new WumpusGesehen());
		    Regeler.addSituationsStatus(new GoldGesehen());
		    Regeler.addSituationsStatus(new Gefangen());
		    Regeler.addSituationsStatus(new NichtsFestgestellt());
		    //Regeler.addSituationsStatus(new RandGesehen());
		    Regeler.addRegeln(Regeln);
		    try {
		    	Ergebnis = ExerciseUtils.exerciseTwo(Regeler, true, 200L);
		    	BesuchteFelder = Regeler.getBesuchteFelder();
		    }
		    catch (Throwable t) {
		    	t.printStackTrace();
		    }	    
		    SimSystem.report(Level.INFO, "Ergebnis des Agenten: " + Ergebnis);
		    SimSystem.report(Level.INFO, "Besuchte Felder: " + BesuchteFelder);
		    GesamtErgebnis += Ergebnis;
		    GesamtBesuchteFelder += BesuchteFelder;
	    }
		    
  	    BufferedWriter BW=null;
  	    try {
  	    	BW = new BufferedWriter(new FileWriter("../A2Ergebnis.csv", true));
  	    	if(SeedErhalten)
  	    		BW.write("S"+NeuesSeed+";");
  			BW.write(AnzahlSimulationen+";"+GesamtErgebnis+";"+GesamtBesuchteFelder);
  			BW.newLine();
  			BW.flush();
  		} catch (IOException ioe) {
  			ioe.printStackTrace();
  	    } finally {                    
  	    	if (BW != null) try {
  	    		BW.close();
  	    	} catch (IOException ioe2) {
  	    	}
  	    }
    System.exit(0);
	}
}