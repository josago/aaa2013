package tests;

import assignment1.Agent;
import assignment1.IterativePolicyEvaluation;
import assignment1.PolicyIteration;
import assignment1.PredatorRandom;
import assignment1.PreySimple;
import assignment1.State;
import assignment1.StateReduced;
import assignment1.StateSimple;
import assignment1.ValueIteration;

/**
 * TEST 7 (Assignment 1.5)
 * This test compares the level of performance obtained by using the original space of states vs. a reduced one (11^4 vs. 11^2 states).
 * @author josago
 */
public class Test7
{
	public static final float THETA = 0.000000001f;
	public static final float GAMMA = 0.9f;
	
	public static void main(String[] args)
	{
		State stateSimple  = new StateSimple(0, 0, 5, 5);
		State stateReduced = new StateReduced(0, 0, 5, 5);
		Agent predator     = new PredatorRandom();
		Agent prey         = new PreySimple();
		
		// Iterative Policy Evaluation algorithm:

		long timeStartSimple = System.nanoTime();
		new IterativePolicyEvaluation(stateSimple, predator, prey, THETA, GAMMA);
		long timeEndSimple = System.nanoTime();
		
		System.out.println("Execution time of the Iterative Policy Evaluation algorithm for StateSimple: " + (timeEndSimple - timeStartSimple) / 1000000L + " ms.");
		
		long timeStartReduced = System.nanoTime();
		new IterativePolicyEvaluation(stateReduced, predator, prey, THETA, GAMMA);
		long timeEndReduced = System.nanoTime();
		
		System.out.println("Execution time of the Iterative Policy Evaluation algorithm for StateReduced: " + (timeEndReduced - timeStartReduced) / 1000000L + " ms.");
		
		System.out.println("");
		
		// Policy Iteration algorithm:

		timeStartSimple = System.nanoTime();
		new PolicyIteration(stateSimple, predator, prey, THETA, GAMMA);
		timeEndSimple = System.nanoTime();
		
		System.out.println("Execution time of the Policy Iteration algorithm for StateSimple: " + (timeEndSimple - timeStartSimple) / 1000000L + " ms.");
		
		timeStartReduced = System.nanoTime();
		new PolicyIteration(stateReduced, predator, prey, THETA, GAMMA);
		timeEndReduced = System.nanoTime();
		
		System.out.println("Execution time of the Policy Iteration algorithm for StateReduced: " + (timeEndReduced - timeStartReduced) / 1000000L + " ms.");
		
		System.out.println("");
		
		// Value Iteration algorithm:

		timeStartSimple = System.nanoTime();
		new ValueIteration(stateSimple, prey, THETA, GAMMA);
		timeEndSimple = System.nanoTime();
		
		System.out.println("Execution time of the Value Iteration algorithm for StateSimple: " + (timeEndSimple - timeStartSimple) / 1000000L + " ms.");
		
		timeStartReduced = System.nanoTime();
		new ValueIteration(stateReduced, prey, THETA, GAMMA);
		timeEndReduced = System.nanoTime();
		
		System.out.println("Execution time of the Value Iteration algorithm for StateReduced: " + (timeEndReduced - timeStartReduced) / 1000000L + " ms.");
	}
}
