package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;

import java.util.logging.Level;

import examples.wumpusworld.exercises.ExerciseUtils;

public class TestGoldsammelAgent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimSystem.getRNGGenerator().setSeed(1233L);
		int AgentenErgebnis = -1;
		int BesuchteFelder = 0;
		GoldsammelAgent Goldi = new GoldsammelAgent();
	    try {
	    	AgentenErgebnis = ExerciseUtils.exerciseOne(Goldi, true, 50L);
	    	BesuchteFelder = Goldi.getBesuchteFelder();
	    } 
	    catch (Throwable t) {
	    	t.printStackTrace();
	    }
	    SimSystem.report(Level.INFO, "Ergebnis des Agenten: " + AgentenErgebnis);
	    SimSystem.report(Level.INFO, "Besuchte Felder: " + BesuchteFelder);
	    System.exit(0);
	  	}
}
