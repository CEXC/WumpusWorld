package wumpusworld.solutions.ws1112.JTSBMMSSNR;

import james.SimSystem;

import java.util.logging.Level;

import experiments.wumpus.WumpusExperimentUtils;

/**
 * Executes a simulation with the simple agent.
 * 
 * @author Roland Ewald
 */
public class TestMyAgent {

  public static void main(String[] args) {
    
    int agentScore = -1;
    try {
      agentScore =
          WumpusExperimentUtils.testAgent(
              "examples.wumpusworld.simple.SimpleWumpusWorld",
              new SimpleAgent(), true, 1000, 20, 1000, 1);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    SimSystem.report(Level.INFO, "My score: " + agentScore);
    System.exit(0);
  }
}
