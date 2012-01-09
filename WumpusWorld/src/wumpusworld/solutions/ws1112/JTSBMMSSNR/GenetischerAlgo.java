package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;
import james.core.math.random.generators.IRandom;
import james.core.model.variables.IntVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.wumpusworld.WumpusWorldState;
import model.wumpusworld.agents.IWumpusWorldAgent;
import model.wumpusworld.agents.NeighbourhoodPerceivingAgent;
import model.wumpusworld.agents.TerriblyDangerousWumpus;

import experiments.wumpus.WumpusExperimentUtils;

public class GenetischerAlgo {

	public float testPopulationsFitness(boolean Visualisierung, long PauseZwSchritten) {
		// Keine Population => koennen wir nichts machen
		if(Population == null || Population.isEmpty())
			return -999999999;
		int GesamtFitness = 0;
		int AnzahlSimulationen = 0;
		for(RegelAgent Individuum : Population) {
			Integer Fitness = null;
			if(SeedErhalten)
				SimSystem.getRNGGenerator().setSeed(Seed);
		    try {
		    	// BUG (siehe Mail)? daher ein bisl Trickserei mit eigener exerciseTwo
		    	Fitness = GenetischerAlgo.exerciseTwo(Individuum, Visualisierung, PauseZwSchritten);

		    	Individuum.setFitness(Fitness);
		    	if(Fitness == null)
		    		continue;
		    	AnzahlSimulationen++;
		    	GesamtFitness += Fitness;
		    }
		    catch (Throwable t) {
		    	t.printStackTrace();
		    }    
	    }
		return GesamtFitness/AnzahlSimulationen;
	}
	
	public boolean nextGeneration() {
		if((0 == Populationsgroesse) || (ProzentAnteilDieFortpflanzen == 0))
			return false;
		
		ArrayList<RegelAgent> NeuePopulation = new ArrayList<RegelAgent>();

		// Sortiere Population von fit zu nicht fit
		//Collections.sort(Population);
		// Schmeisse alle ohne Berechtigung zur Fortpflanzung raus
		int MaxEltern = Populationsgroesse*ProzentAnteilDieFortpflanzen/100;
		ArrayList<RegelAgent> Eltern = new ArrayList<RegelAgent>(Population.subList(0, MaxEltern-1));
		
				
		for(int i=0; i<Populationsgroesse; i++) {
			// Waehle zufaellig zwei Elternteile
		    IRandom ZufallsZahlenGen = null;
			ZufallsZahlenGen = SimSystem.getRNGGenerator().getNextRNG();
			// Damit wir auch sicher zwei verschiedene Elternteile haben
			int iVater, iMutter;
			iMutter = ZufallsZahlenGen.nextInt(Eltern.size());
			iVater = ZufallsZahlenGen.nextInt(Eltern.size());
			while(iMutter == iVater) 
				iVater = ZufallsZahlenGen.nextInt(Eltern.size());
		    RegelAgent Mutter = Eltern.get(iMutter);
		    RegelAgent Vater = Eltern.get(iVater);

			
		    RegelAgent Regeler = new RegelAgent();
		    // SituationsStatus hinzufuegen
		    Regeler.addSituationsStatus(new WumpusVoraus());
		    Regeler.addSituationsStatus(new WumpusGerochen());
		    Regeler.addSituationsStatus(new WumpusGesehen());
		    Regeler.addSituationsStatus(new GoldGesehen());
		    Regeler.addSituationsStatus(new Gefangen());
		    Regeler.addSituationsStatus(new NichtsFestgestellt());
		    Regeler.addSituationsStatus(new RandGesehen());
		 	
		    // Hole Regeln der Eltern
		    LinkedList<Regel> VaterRegeln = Mutter.getRegeln();
		    LinkedList<Regel> MutterRegeln = Vater.getRegeln();
		    
		    // Durchlaufe alle vorhandenen Regeln und 
		    // Kreuze Prioritaeten
		    // ggf an zufaelliger Stelle
		    int KS = Kreuzungsstelle;
		    if(KS == -1)
		    	KS = ZufallsZahlenGen.nextInt(Regeln.size());
		    for(int j=0; j<Regeln.size(); j++) {
		    	// Muttergenom
		    	LinkedList<Regel> BenutzeRegeln = null;
		    	if(j <= KS) {
		    		BenutzeRegeln = MutterRegeln;
		    	}
		    	// Vatergenom
		    	else {
		    		BenutzeRegeln = VaterRegeln;
		    		}
		    	for(Regel R : BenutzeRegeln) {
		    		if(R.VergleichOhneP(Regeln.get(j)))
		    			Regeln.get(j).setPrioritaet(R.getPrioritaet());
		    	}
		    	// Ggf Mutieren wir noch eine bisl rum
		    	if(Mutationswahrscheinlichkeit > ZufallsZahlenGen.nextInt(100))
		    		Regeln.get(j).setPrioritaet(ZufallsZahlenGen.nextInt(1001));
			}		
		    Regeler.addRegeln(Regeln);
		    NeuePopulation.add(Regeler);
	    }
		Population = NeuePopulation;
		return true;
	}
	
	
	
	public boolean initPopulation() {
		if(0 == Populationsgroesse)
			return false;
		Population = new ArrayList<RegelAgent>();
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
		    Population.add(Regeler);
	    }
		return true;
	}
	
	public static Integer exerciseTwo(
		      NeighbourhoodPerceivingAgent agentToBeTested, boolean visualisation,
		      long pauseBetweenSteps) throws Throwable {
		    List<IWumpusWorldAgent> agentList = new ArrayList<IWumpusWorldAgent>();
		    agentList.add(agentToBeTested);
		    agentList.add(new TerriblyDangerousWumpus());
		    agentList.add(new TerriblyDangerousWumpus());
		    agentList.add(new TerriblyDangerousWumpus());

		    Map<String, Object> additionalParameters = new HashMap<String, Object>();
		    additionalParameters.put(WumpusWorldState.WUMPUS_PIT_DENSITY, .2);

		    Map<String, Object> results =
		    		WumpusExperimentUtils.testAgents(
		            "examples.wumpusworld.simple.SimpleWumpusWorld", agentList,
		            visualisation, pauseBetweenSteps, 20, 500, 0, additionalParameters);
		    // Fange den null Pointer ab
		    if(results == null) {
		    	System.out.println("results == null");
		    	return null;
		    }
		    return ((IntVariable) results.get(agentToBeTested.getClass().getName()))
		        .getValue();
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
	public int getProzentAnteilDieFortpflanzen() {
		return ProzentAnteilDieFortpflanzen;
	}

	public boolean setProzentAnteilDieFortpflanzen(int Fortpflanzung) {
		if((Fortpflanzung <= 0) || (Fortpflanzung > 100))
			return false;
		ProzentAnteilDieFortpflanzen = Fortpflanzung;
		return true;

	}
	
	private long Seed = 0;
	private boolean SeedErhalten = false;
	private int Mutationswahrscheinlichkeit = 0;
	private int Populationsgroesse = 0;
	private ArrayList<RegelAgent> Population = null;
	private int Kreuzungsstelle = -1;
	private int ProzentAnteilDieFortpflanzen = 0;
	private ArrayList<Regel> Regeln = null;
}
