package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.StateMulti;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test4
{
	public static final int NUM_PREDATORS   = 4;
	public static final int NUM_THREADS     = 10;
	
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 0.1f;
	public static final float VALUE_INITIAL = 15;

	public static void main(String[] args)
	{
		// Initialization:
		
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		// Q-Learning:
		
		QLearningMulti.performanceClear();
		
		for (int i = 0; i < NUM_THREADS; i++)
		{
			new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, VALUE_INITIAL, false, true);
			
			System.out.println(i + " iterations for " + NUM_PREDATORS + " predators...");
		}
		
		QLearningMulti.printPerformance();
	}
}
