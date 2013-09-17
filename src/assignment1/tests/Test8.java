package assignment1.tests;

import assignment1.Agent;
import assignment1.PreySimple;
import assignment1.Simulator;
import assignment1.State;
import assignment1.StateReduced;
import assignment1.algorithms.ValueIteration;

/**
 * This class tests that the reduced state space indeed works as expected by first calculating the optimal agent and then displaying its behaviour.
 * @author josago
 */
public class Test8
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.9f;
	
	public static void main(String[] args)
	{
		State stateReduced = new StateReduced(0, 0, 5, 5);
		Agent prey         = new PreySimple();
		
		ValueIteration viReduced = new ValueIteration(stateReduced, prey, THETA, GAMMA);
		
		Agent predator = viReduced.buildAgent();
		
		int turns = Simulator.runSimulation(stateReduced, prey, predator, 250, true);
		
		System.out.println("The predator catched the prey in " + turns + " turns.");
	}
}
