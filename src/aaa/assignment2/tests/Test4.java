package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.MonteCarloOnPolicy;

public class Test4
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		State env  = new StateReduced(0, 0, 5, 5);
		
			for (float GAMMA = 0; GAMMA < 1.0; GAMMA += 0.1)
			{
				MonteCarloOnPolicy mc = new MonteCarloOnPolicy(env, GAMMA, 0.1f, 15, false);
				Agent predator = mc.buildAgent();
				
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
