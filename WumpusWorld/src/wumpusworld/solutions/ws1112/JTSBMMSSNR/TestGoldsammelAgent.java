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
	    try {
	    	ExerciseUtils.exerciseOne(new GoldsammelAgent(), true, 100L);
	    } 
	    catch (Throwable t) {
	    	t.printStackTrace();
	    }
	    SimSystem.report(Level.INFO, "Ergebnis des Agenten: " + AgentenErgebnis);
	    System.exit(0);
	  	}
}
