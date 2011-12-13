package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import examples.wumpusworld.exercises.ExerciseUtils;

public class TestGoldsammelAgent {

	/**
	 * @param args	
	 * 	Agent und Simulation sind durch CMD Parameter zu beeinflussen
	 * 
		Suchverfahren: 							Standard = BS
							    					- BS  = Breitensuche
							    					- UK  = Uniforme Kostensuche 
							     					- AS  = A*
							    					- ASS = A* Spezial mit Versuch der Minimierung der Aktionfolge
							    	 				
							    					Bsp. Argumente BS AS UK => uniforme Kostensuche
		Seed fuer den Random Number Generator: Standard = kein eigenes Seed 
													- RXXXXXXXXL Wobei XXXXXX fuer einen Long steht, der mit L beendet wird
		Visualisierung:							Standard = false
													- V = true
		Zeitdauer zwischen Schritten in ms:		Standard = 0L
													- ZXXXXXXXXL Wobei XXXXXX fuer einen Long steht, der mit L beendet wird
	 */
	public static void main(String[] args) {		
	    AgentenVorgehen AV = AgentenVorgehen.BREITENSUCHE;
	    long NeuesSeed = 0L;
	    boolean SeedErhalten = false;
	    boolean Visualisierung = false;
	    long PauseZwSchritten = 0L;
	    for(int i=0; i< args.length; i++) {
	    	if(args[i].equals("BS"))
	    		AV = AgentenVorgehen.BREITENSUCHE;
	    	else if(args[i].equals("UK"))
	    		AV = AgentenVorgehen.UNIFORMEKOSTENSUCHE;
	    	else if(args[i].equals("AS"))
	    		AV = AgentenVorgehen.ASTERN;
	    	else if(args[i].equals("ASS"))
	    		AV = AgentenVorgehen.ASTERNSPEZIAL;
	    	else if(args[i].startsWith("R") && args[i].endsWith("L") && args[i].length() > 2) {
	    		NeuesSeed = Long.parseLong(args[i].substring(1,args[i].length()-2));
	    		SimSystem.getRNGGenerator().setSeed(NeuesSeed);
	    		SeedErhalten = true;
	    	}
	    	else if(args[i].equals("V"))
	    		Visualisierung = true;
	    	else if(args[i].startsWith("Z") && args[i].endsWith("L") && args[i].length() > 2) {
	    		PauseZwSchritten = Long.parseLong(args[i].substring(1,args[i].length()-2));
	    	}
	    }
	    
		int AgentenErgebnis = 0;
		int BesuchteFelder = 0;
		int ExpandierteKnoten = 0;
	    GoldsammelAgent Goldi = new GoldsammelAgent();
	    Goldi.setAgentenVorgehen(AV);
	    try {
	    	AgentenErgebnis = ExerciseUtils.exerciseOne(Goldi, Visualisierung, PauseZwSchritten);
	    	BesuchteFelder = Goldi.getBesuchteFelder();
	    	ExpandierteKnoten = Goldi.getExpandierteKnoten();
	    }
	    catch (Throwable t) {
	    	t.printStackTrace();
	    }
	    
	    SimSystem.report(Level.INFO, "Ergebnis des Agenten: " + AgentenErgebnis);
	    SimSystem.report(Level.INFO, "Besuchte Felder: " + BesuchteFelder);
	    SimSystem.report(Level.INFO, "Expandierte Knoten: " + ExpandierteKnoten);
	    System.exit(0);
	    
		
  	    BufferedWriter BW=null;
  	    try {
  	    	BW = new BufferedWriter(new FileWriter("../"+AV.toString()+"_Ergebnis.csv"));
  	    	if(SeedErhalten)
  	    		BW.write(NeuesSeed+";");
  			BW.write(AgentenErgebnis+";"+BesuchteFelder+";"+ExpandierteKnoten);
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
