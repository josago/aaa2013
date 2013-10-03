package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment2.algorithms.QLearning;

public class Test6
{
	public static void main(String[] args)
	{
		State env  = new StateReduced(0, 0, 5, 5);
		
		for (float ALPHA: new float[] {0.1f, 0.2f, 0.3f, 0.4f, 0.5f})
		{
			for (float GAMMA: new float[] {0.1f, 0.3f, 0.5f, 0.7f, 0.9f})
			{
				System.out.println("\n\\alpha = " + ALPHA + ", \\gamma = " + GAMMA);
				
				ModelFreeAlgorithm.performanceClear();
				
				for (int i = 0; i < 10; i++)
				{
					System.out.print(i + "...");
					new QLearning(env, ALPHA, GAMMA, 0.1f, 15, true);
				}
				
				System.out.println("");
				ModelFreeAlgorithm.printPerformance();
			}
		}
	}
}
