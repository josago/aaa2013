package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.QLearning;

public class Test1
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		State env  = new StateSimple(0, 0, 5, 5);
		
		for (float ALPHA = 0.1f; ALPHA <= 1.0; ALPHA += 0.1)
		{
			for (float GAMMA = 0; GAMMA < 1.0; GAMMA += 0.1)
			{
				QLearning q = new QLearning(env, ALPHA, GAMMA, 0.1f, 15);
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
