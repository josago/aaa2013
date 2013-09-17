package assignment1.tests;

import assignment1.Agent;
import assignment1.PredatorRandom;
import assignment1.PreySimple;
import assignment1.Simulator;
import assignment1.StateSimple;

/**
 * TEST 1 (Assignment 1.1)
 * Tests the simulator by displaying one game graphically using the simple prey and the random predator as agents.
 * When the game is finished, the number of turns elapsed is displayed.
 * @author josago
 */
public class Test1
{
	public static void main(String[] args)
	{
		StateSimple env = new StateSimple(0, 0, 5, 5);
		
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		int turns = Simulator.runSimulation(env, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
