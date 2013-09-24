package aaa.assignment1.tests;

import aaa.Agent;
import aaa.PreySimple;
import aaa.Simulator;
import aaa.State;
import aaa.StateSimple;
import aaa.assignment1.algorithms.ValueIteration;

/**
 * TEST 5 (Assignment 1.4)
 * This test calculates the optimal policy using the Value Iteration algorithm and then runs and displays a simulation to check the behaviour of the optimal agent.
 * When the game is finished, the number of turns elapsed are displayed.
 * @author josago
 */
public class Test5
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.9f;
	
	public static void main(String[] args)
	{
		State stateInitial = new StateSimple(0, 0, 5, 5);
		Agent prey         = new PreySimple();

		ValueIteration vi = new ValueIteration(stateInitial, prey, THETA, GAMMA);

		Agent predator = vi.buildAgent();
		
		int turns = Simulator.runSimulation(stateInitial, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
