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
	 */
	public static void main(String[] args) {
		SimSystem.getRNGGenerator().setSeed(1233L);
		
		SimSystem.consoleOut = true;
		int AgentenErgebnis = 0;
		int BesuchteFelder = 0;
		int ExpandierteKnoten = 0;
	    GoldsammelAgent Goldi = new GoldsammelAgent();
		Goldi.setAgentenVorgehen(AgentenVorgehen.ASTERNSPEZIAL);
	    try {
	    	AgentenErgebnis = ExerciseUtils.exerciseOne(Goldi, false, 0L);
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
    }
}
