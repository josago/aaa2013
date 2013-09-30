package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.QLearning;

public class Test2
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		State env  = new StateSimple(0, 0, 5, 5);
		
		float ALPHA = 0.8f;
		float GAMMA = 0.9f;
		
		for (float epsilon = 0; epsilon <= 1.0; epsilon += 0.1)
		{
			for (float valueInitial = 30; valueInitial >= -15; valueInitial -= 5)
			{
				QLearning q = new QLearning(env, ALPHA, GAMMA, epsilon, valueInitial);
				Agent predator = q.buildAgent();
				
				int[] length = new int[TEST_NUM_RUNS];

				// Fast simulations (no waiting between game steps):
				
				for (int r = 0; r < TEST_NUM_RUNS; r++)
				{
					length[r] = Simulator.runSimulation((State) env.clone(), prey, predator, 0, false);
				}
				
				// Mean game length calculation:
				
				float mean = 0;
				
				for (int r = 0; r < TEST_NUM_RUNS; r++)
				{
					mean += length[r];
				}
				
				mean /= TEST_NUM_RUNS;
				
				System.out.print(mean + " & ");
			}
			
			System.out.println("\\\\");
		}
	}
}
