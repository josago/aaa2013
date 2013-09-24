package aaa.assignment1.tests;

import aaa.Agent;
import aaa.PredatorRandom;
import aaa.PreySimple;
import aaa.Simulator;
import aaa.State;
import aaa.StateSimple;

/**
 * TEST 2 (Assignment 1.1)
 * This test runs many simulations in order to calculate both the mean and the standard deviation of game lengths.
 * The simple prey and the random predator are used as agents.
 * @author josago
 */
public class Test2
{
	public static final int TEST_NUM_RUNS = 100000;
	
	public static void main(String[] args)
	{
		int[] length = new int[TEST_NUM_RUNS];
	
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
		
		// Fast simulations (no waiting between game steps):
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			StateSimple env = new StateSimple(0, 0, 5, 5);
			
			length[r] = Simulator.runSimulation(env, prey, predator, 0, false);
		}
		
		// Mean game length calculation:
		
		long mean = 0;
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			mean += length[r];
		}
		
		mean /= TEST_NUM_RUNS;
		
		System.out.println("Mean length of " + TEST_NUM_RUNS + " games: " + mean + " turns.");
		
		// Standard deviation of game lengths calculation:
		
		long std = 0;
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			std += Math.pow(length[r] - mean, 2);
		}
		
		std = (long) Math.pow(std / TEST_NUM_RUNS, 0.5);
		
		System.out.println("Standard deviation: " + std);
	}
}
