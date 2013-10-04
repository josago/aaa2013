package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment2.algorithms.QLearning;

public class Test2
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		State env  = new StateReduced(0, 0, 5, 5);
		
		float ALPHA = 0.8f;
		float GAMMA = 0.9f;
		
		for (float epsilon: new float[] {0f, 0.25f, 0.5f, 0.75f, 1f})
		{
			for (float valueInitial: new float[] {30f, 15f, 0f, -15f})
			{
				System.out.println("\n\\epsilon = " + epsilon + ", valueInitial = " + valueInitial);
				
				ModelFreeAlgorithm.performanceClear();
				
				for (int i = 0; i < 10; i++)
				{
					System.out.print(i + "...");
					new QLearning(env, ALPHA, GAMMA, epsilon, valueInitial, false, true);
				}
				
				System.out.println("");
				ModelFreeAlgorithm.printPerformance();
			}
		}
	}
}
