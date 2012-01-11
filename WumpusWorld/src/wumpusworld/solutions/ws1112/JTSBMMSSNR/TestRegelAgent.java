package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;


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
	 	Anzahl zu simulierender Generationen:	Standard = 20
	 												- GXXXXXXXXI Wobei XXXXXX fuer einen Int steht, der mit I beendet wird
	 	Groesse der Population:					Standard = 50
	 												- PXXXXXXXXI Wobei XXXXXX fuer einen Int steht, der mit I beendet wird
	 	Anteil an Fortpflanzung in Prozent:		Standard = 50
	 												- FXXXXXXXXI Wobei XXXXXX fuer einen Int steht, der mit I beendet wird
	 	Wahrscheinlichkeit einer Mutation in %: Standard = 3
	 												- MXXXXXXXXI Wobei XXXXXX fuer einen Int steht, der mit I beendet wird
	 	Kreuzung an definierter Genom Stelle:	Standard = random
	 												- KXXXXXXXXI Wobei XXXXXX fuer einen Int >0 && < Laenge des Genoms steht, der mit I beendet wird
	 */
	public static void main(String[] args) {		
	    long NeuesSeed = 0L;
	    boolean SeedErhalten = false;
	    boolean Visualisierung = false;
	    long PauseZwSchritten = 0L;
	    int AnzahlGenerationen = 20;
	    int Mutationswahrscheinlichkeit = 3;
	    int Kreuzungsstelle = -1;
	    int Populationsgroesse = 50;
	    int Fortpflanzung = 50;
	    for(int i=0; i< args.length; i++) {
	    	if(args[i].equals("V"))
	    		Visualisierung = true;
	    	else if(args[i].startsWith("Z") && args[i].endsWith("L") && args[i].length() > 2) {
	    		PauseZwSchritten = Long.parseLong(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("G") && args[i].endsWith("I") && args[i].length() > 2) {
	    		AnzahlGenerationen = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("R") && args[i].endsWith("L") && args[i].length() > 2) {
	    		NeuesSeed = Long.parseLong(args[i].substring(1,args[i].length()-1));
	    		SimSystem.getRNGGenerator().setSeed(NeuesSeed);
	    		SeedErhalten = true;
	    	}
	    	else if(args[i].startsWith("P") && args[i].endsWith("I") && args[i].length() > 2) {
	    		Populationsgroesse = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("F") && args[i].endsWith("I") && args[i].length() > 2) {
	    		Fortpflanzung = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("M") && args[i].endsWith("I") && args[i].length() > 2) {
	    		Mutationswahrscheinlichkeit = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    	else if(args[i].startsWith("K") && args[i].endsWith("I") && args[i].length() > 2) {
	    		Kreuzungsstelle = Integer.parseInt(args[i].substring(1,args[i].length()-1));
	    	}
	    }
	    
	    // Plausibilitaetstest
	    if(AnzahlGenerationen < 1) {
	    	SimSystem.report(Level.SEVERE, "Weniger als eine Generation funktioniert nicht");
	    	System.exit(0);	
	    }
	    
	    // Initialisierung des genetischen Algos
	    GenetischerAlgo GenAlgo = new GenetischerAlgo();
	    // falls seed gesetzt, benutzen wir die gleiche Hoehle fuer alle Individuen	    
	    if(SeedErhalten)
	    	GenAlgo.setSeed(NeuesSeed);
	    if(!GenAlgo.setPopulationsgroesse(Populationsgroesse)) {
	    	SimSystem.report(Level.SEVERE, "Versuch des Setzens einer ungueltigen Populationsgroesse: " + 
	    									Mutationswahrscheinlichkeit);
	    	System.exit(0);	    	
	    }
	    if(!GenAlgo.setMutationswahrscheinlichkeit(Mutationswahrscheinlichkeit)) {
	    	SimSystem.report(Level.SEVERE, "Versuch des Setzens einer ungueltigen Mutationswahrscheinlichkeit: " + 
	    									Mutationswahrscheinlichkeit);
	    	System.exit(0);	    	
	    }
	    if(!GenAlgo.setProzentAnteilDieFortpflanzen(Fortpflanzung)) {
	    	SimSystem.report(Level.SEVERE, "Versuch des Setzens einer ungueltigen Mutationswahrscheinlichkeit: " + 
	    									Mutationswahrscheinlichkeit);
	    	System.exit(0);	    	
	    }
	    // REGELN - diese werden an GenAlgo weitergegeben
	    ArrayList<Regel> Regeln = new ArrayList<Regel>();
	    
	    Regel R = new Regel();
    
	    // Wenn nichts gesehen wurde, kein Rand kein gar nichts bewegen wir uns einfach herum
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new NichtsFestgestellt(true));
	    R.addAktion(new Bewegen2Aktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn Gold gesehen wurde, hebe dieses auf
	    R.addStatus(new GoldGesehen(true));
	    R.addAktion(new GoldaufhebenAktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn der Rand gesehen wurde bewegen wir uns trotzdem weiter
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new RandGesehen(true));
	    R.addAktion(new Bewegen2Aktion(200));
	    Regeln.add(new Regel(R));
	    	    
	    // Wenn der Wumpus gerochen wurde, fliehen wir
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGerochen(true));
	    R.addAktion(new FliehenAktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn der Wumpus gerochen wurde, jagen wir
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGerochen(true));
	    R.addAktion(new JagenAktion(200));
	    Regeln.add(new Regel(R));
	    	    
	    // Wenn der Wumpus gesehen wurde, fliehen wir
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGesehen(true));
	    R.addAktion(new FliehenAktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn der Wumpus gesehen wurde und wir gefangen sind, jagen wir
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGesehen(true));
	    R.addStatus(new Gefangen(true));
	    R.addAktion(new JagenAktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn der Wumpus gesehen wurde, schiessen wir einen pfeil ab
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new WumpusGesehen(true));
	    R.addAktion(new PfeilabschiessenAktion(200));
	    Regeln.add(new Regel(R));
	    
	    // Wenn der Wumpus gefangen ist, gehen wir auch ueber gruben
	    R.resetStatusListe();
	    R.resetAktionenListe();
	    R.addStatus(new Gefangen(true));
	    R.addAktion(new GeheUeberGruben(200));
	    Regeln.add(new Regel(R));
	    
	    GenAlgo.setRegeln(Regeln);
	    // REGELN - Ende	    
	    
	    if(Kreuzungsstelle > -1 && !GenAlgo.setKreuzungsstelle(Kreuzungsstelle)) {
	    	SimSystem.report(Level.SEVERE, "Versuch des Setzens einer ungueltigen Kreuzungsstelle: " + 
	    			Kreuzungsstelle);
	    	System.exit(0);	    	
	    }
	    
	    if(!GenAlgo.initPopulation()) {
	    	SimSystem.report(Level.SEVERE, "Fehler beim Initialisieren der Population");
	    	System.exit(0);	    	
	    }
	    
	    float Fitness[] = new float[AnzahlGenerationen];
	    for(int i=0; i<AnzahlGenerationen; i++) {
	    	Fitness[i] = GenAlgo.testPopulationsFitness(Visualisierung, PauseZwSchritten);
	    	if(Fitness[i] == -999999999) {
	    		SimSystem.report(Level.SEVERE, "Schwerer fehler bei der Berechnung der Populationsfitness"+
	    										" ist die Population initialisiert worden?");
		    	System.exit(0);	 
	    	}
	    	SimSystem.report(Level.INFO, "Fitness der "+(i+1)+". Generation: " + Fitness[i]);
	    	 if(!GenAlgo.nextGeneration()) {
	 	    	SimSystem.report(Level.SEVERE, "Fehler beim erstellen der naechsten Population");
	 	    	System.exit(0);	    	
	 	    }
	    } 
	    // damit man es gebuendelt nochmal am ende hat
	    for(int i=0; i<AnzahlGenerationen; i++) {
	    	SimSystem.report(Level.INFO, "Fitness der "+(i+1)+". Generation: " + Fitness[i]);
	    }
	    
	    BufferedWriter BW=null;
  	    try {
  	    	BW = new BufferedWriter(new FileWriter("../A2Ergebnis.txt", true));
  	    	BW.newLine();
  	    	BW.write("////////////// Neuer Durchgang ///////////////////////////////////////////////////");
  	    	BW.newLine();
  	    	if(SeedErhalten) {
  	    		BW.write("Verwendetes Seed: " + NeuesSeed);
  	    		BW.newLine();
  	    	}
  			BW.write("Ausgangs Populationsgroesse: " + Populationsgroesse);
  			BW.newLine();
  			BW.write("Mutationswahrscheinlichkeit: "+Mutationswahrscheinlichkeit+"%");
  			BW.newLine();
  			BW.write("Rekombinationsstelle (-1==Zufall): " + Kreuzungsstelle); 
  			BW.newLine();
  			BW.write("Fortpflanzungsteilnehmer (die besten x%): " + Fortpflanzung + "%");
  			BW.newLine();
  			for(int i=0; i<AnzahlGenerationen; i++) {
  		    	BW.write("Fitness der "+(i+1)+". Generation: " + Fitness[i]);
  		    	BW.newLine();
  		    }
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