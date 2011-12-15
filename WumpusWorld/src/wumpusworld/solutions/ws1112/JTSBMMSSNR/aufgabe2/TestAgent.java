package wumpusworld.solutions.ws1112.JTSBMMSSNR.aufgabe2;

import james.SimSystem;
import wumpusworld.solutions.ws1112.JTSBMMSSNR.AgentenVorgehen;
import wumpusworld.solutions.ws1112.JTSBMMSSNR.GoldsammelAgent;
import examples.wumpusworld.exercises.ExerciseUtils;

public class TestAgent {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//SimSystem.getRNGGenerator().setSeed(1233L);
		SimSystem.consoleOut = false;
		    Agent James = new Agent();
		    try {
		    	ExerciseUtils.exerciseTwo(James, true, 1000L);
		    }
		    catch (Throwable t) {
		    	t.printStackTrace();
		    }
	}

}
