package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test3
{
	public static final int NUM_PREDATORS   = 2;
	public static final int NUM_SIMULATIONS = 10000;
	
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 1.0f;
	public static final float VALUE_INITIAL = 0;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti     env = new StateMulti(prey, predators);
		QLearningMulti ql  = new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, VALUE_INITIAL, false, false);
		
		Agent preyNew = ql.buildAgent(prey);
		List<Agent> predatorsNew = new ArrayList<Agent>();
		
		for (Agent predator: predators)
		{
			predatorsNew.add(ql.buildAgent(predator));
		}
		
		// Fast simulations (no waiting between game steps):
		
		int[] length = new int[NUM_SIMULATIONS];
		int[] reward = new int[NUM_SIMULATIONS];
		
		for (int r = 0; r < NUM_SIMULATIONS; r++)
		{
			env = new StateMulti(preyNew, predatorsNew);
			
			length[r] = SimulatorMulti.runSimulation(env, preyNew, predatorsNew, 0, false);
			reward[r] = env.getReward(predatorsNew.get(0));
		}
		
		// Mean game length calculation:
		
		double meanLong = 0;
				
		for (int r = 0; r < NUM_SIMULATIONS; r++)
		{
			meanLong += reward[r] * Math.pow(GAMMA, length[r] - 1);
		}
				
		double mean = (double) meanLong / NUM_SIMULATIONS;
				
		System.out.println("Mean score of " + NUM_SIMULATIONS + " games: " + mean + " points.");
	}
}
