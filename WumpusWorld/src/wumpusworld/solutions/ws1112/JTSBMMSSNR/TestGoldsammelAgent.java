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
		//SimSystem.getRNGGenerator().setSeed(1233L);
		int AgentenErgebnis = 0;
		int BesuchteFelder = 0;
		int ExpandierteKnoten = 0;
	    GoldsammelAgent Goldi = new GoldsammelAgent();
		Goldi.setAgentenVorgehen(AgentenVorgehen.BREITENSUCHE);
	    try {
	    	AgentenErgebnis += ExerciseUtils.exerciseOne(Goldi, false, 0L);
	    	BesuchteFelder += Goldi.getBesuchteFelder();
	    	ExpandierteKnoten += Goldi.getExpandierteKnoten();
	    } 
	    catch (Throwable t) {
	    	t.printStackTrace();
	    }
	    
	    SimSystem.report(Level.INFO, "Ergebnis des Agenten: " + AgentenErgebnis);
	    SimSystem.report(Level.INFO, "Besuchte Felder: " + BesuchteFelder);
	    SimSystem.report(Level.INFO, "Expandierte Knoten: " + ExpandierteKnoten);
	    BufferedWriter BW=null;
	    try {
	    	BW = new BufferedWriter(new FileWriter("../../../../OB_BS_Ergebnis.csv", true));
			BW.write(AgentenErgebnis+";"+BesuchteFelder+";"+ExpandierteKnoten);
			BW.newLine();
			BW.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
	    } finally {                       // always close the file
	    	if (BW != null) try {
	    		BW.close();
	    	} catch (IOException ioe2) {
	    	}
	    }
	    System.exit(0);
    }
}
