package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;
import james.core.math.random.generators.IRandom;
import james.core.util.misc.Pair;

import java.util.ArrayList;

import examples.wumpusworld.exercises.ExerciseUtils;

public class GenetischerAlgo {

	public int testPopulationsFitness(boolean Visualisierung, long PauseZwSchritten) {
		// Keine Population => koennen wir nichts machen
		if(Population == null || Population.isEmpty())
			return -999999999;
		int GesamtFitness = 0;
		for(int i=0; i<Populationsgroesse; i++) {
			int Fitness = 0;
			if(SeedErhalten)
				SimSystem.getRNGGenerator().setSeed(Seed);
		    try {
		    	
		    	/* BUG ? Threading Problem?
		    	 * Zeile 34: ExerciseUtils.exerciseTwo(Population.get(i).getFirstValue(), Visualisierung, PauseZwSchritten);
		    	 * Dies fuehrte zu folgendem Error
		    	 * java.lang.NullPointerException
		    	 * 		at examples.wumpusworld.exercises.ExerciseUtils.exerciseTwo(ExerciseUtils.java:79)
		    	 * 		at wumpusworld.solutions.ws1112.JTSBMMSSNR.GenetischerAlgo.testPopulationsFitness(GenetischerAlgo.java:34)
		    	 * 		at wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent.main(TestRegelAgent.java:148)
		    	 * Breakpoint auf Zeile 34 zeigt, dass weder Population, noch Population.get(i), noch 
		    	 * Population.get(i).getFirstValue() gleich null waren, d.h. der Fehler muss wohl in exerciseTwo liegen
		    	 */
		    	Fitness = ExerciseUtils.exerciseTwo(Population.get(i).getFirstValue(), Visualisierung, PauseZwSchritten);
		    	Population.get(i).setSecondValue(Fitness);
		    	GesamtFitness += Fitness;
		    }
		    catch (Throwable t) {
		    	t.printStackTrace();
		    }    
	    }
		return GesamtFitness;
	}
	public boolean initPopulation() {
		if(0 == Populationsgroesse)
			return false;
		Population = new ArrayList<Pair<RegelAgent, Integer>>();
		for(int i=0; i<Populationsgroesse; i++) {
		    RegelAgent Regeler = new RegelAgent();
		    // SituationsStatus hinzufuegen
		    Regeler.addSituationsStatus(new WumpusVoraus());
		    Regeler.addSituationsStatus(new WumpusGerochen());
		    Regeler.addSituationsStatus(new WumpusGesehen());
		    Regeler.addSituationsStatus(new GoldGesehen());
		    Regeler.addSituationsStatus(new Gefangen());
		    Regeler.addSituationsStatus(new NichtsFestgestellt());
		    Regeler.addSituationsStatus(new RandGesehen());
		    // zufaellige Regelprioritaeten von 0 bis 1000
		    IRandom ZufallsZahlenGen = null;
			ZufallsZahlenGen = SimSystem.getRNGGenerator().getNextRNG();
		    for(Regel NR : Regeln) {
				NR.setPrioritaet(ZufallsZahlenGen.nextInt(1001));
			}
		    Regeler.addRegeln(Regeln);
		    Population.add(new Pair<RegelAgent, Integer>(Regeler, -999999999));
	    }
		return true;
	}
	
	public void setSeed(long NeuesSeed) {
		Seed = NeuesSeed;
		SeedErhalten = true;
	}	
	
	public boolean setMutationswahrscheinlichkeit(int MutW) {
		if((MutW < 0) || (MutW > 100))
			return false;
		Mutationswahrscheinlichkeit = MutW;
		return true;
	}
	
	public boolean setPopulationsgroesse(int Population) {
		if(Population < 1)
			return false;
		Populationsgroesse = Population;
		return true;
	}
	
	public boolean setKreuzungsstelle(int Kreuzungsstelle) {
		if((Kreuzungsstelle < 0) || (Regeln.size() < Kreuzungsstelle))
			return false;
		this.Kreuzungsstelle = Kreuzungsstelle;
		return true;
	}	
	
	public void setRegeln(ArrayList<Regel> Regeln) {
		this.Regeln = Regeln;
	}
	
	private long Seed = 0;
	private boolean SeedErhalten = false;
	private int Mutationswahrscheinlichkeit = 0;
	private int Populationsgroesse = 0;
	private ArrayList<Pair<RegelAgent, Integer>> Population = null;
	private int Kreuzungsstelle = 0;
	private ArrayList<Regel> Regeln = null;
	
}
