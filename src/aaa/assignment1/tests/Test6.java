package aaa.assignment1.tests;

import aaa.Agent;
import aaa.PreySimple;
import aaa.State;
import aaa.StateSimple;
import aaa.assignment1.algorithms.ValueIteration;

/**
 * TEST 6 (Assignment 1.4)
 * This test runs the Value Iteration algorithm for various values of the discount factor and outputs the number of iterations run for each case.
 * It also displays the state-value function for various states afterwards.
 * @author josago
 */
public class Test6
{
	public static final float THETA = 0.000000001f;
	
	public static void main(String[] args)
	{
		State stateInitial = new StateSimple(0, 0, 5, 5);
		Agent prey         = new PreySimple();
		
		ValueIteration vi = null;
		
		for (float GAMMA: new float[] {0.1f, 0.5f, 0.7f, 0.9f})
		{
			vi = new ValueIteration(stateInitial, prey, THETA, GAMMA);
			
			System.out.println("Number of iterations for gamma = " + GAMMA + ": " + vi.getIterations());
		}
		
		System.out.println("");
		
		for (int x = 0; x < State.ENVIRONMENT_SIZE; x++)
		{
			for (int y = 0; y < State.ENVIRONMENT_SIZE; y++)
			{
				System.out.println("Value for Predator(" + x + ", " + y + ") Prey(5, 5): " + vi.getEvaluation(new StateSimple(x, y, 5, 5)));
			}
		}
	}
}
