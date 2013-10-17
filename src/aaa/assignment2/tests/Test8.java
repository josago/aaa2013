package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.*;

public class Test8
{
	public static void main(String[] args)
	{
		State env  = new StateReduced(0, 0, 5, 5);
		
		
			for (float GAMMA: new float[] {0.1f, 0.3f, 0.5f, 0.7f, 0.9f})
			{
				System.out.println("gamma = " + GAMMA);
				
				ModelFreeAlgorithm.performanceClear();
				
				for (int i = 0; i < 10; i++)
				{
					System.out.print(i + "...");
					new MonteCarloOffPolicy(env, GAMMA, 0, true);
				}
				
				System.out.println("");
				ModelFreeAlgorithm.printPerformance();
			}
		
	}
}
