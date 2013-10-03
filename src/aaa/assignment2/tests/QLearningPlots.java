package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.QLearning;

public class QLearningPlots
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		State env  = new StateReduced(0, 0, 5, 5);
		float [] alpha = {0.1f, 0.2f, 0.3f, 0.4f, 0.5f};
		float [] gamma = {0.1f, 0.5f, 0.7f, 0.9f};
		
		
		for (float ALPHA : alpha)
		{
			for (float GAMMA: gamma)
				
			{
				QLearning q = new QLearning(env, ALPHA, GAMMA, 0.1f, 5);
				
			}
			
		
		}
	}
}
