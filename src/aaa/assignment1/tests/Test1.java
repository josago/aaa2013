package aaa.assignment1.tests;

import aaa.Agent;
import aaa.PredatorRandom;
import aaa.PreySimple;
import aaa.Simulator;
import aaa.StateSimple;

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
