package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.*;

public class Test7
{
	public static final float ALPHA = 0.1f;
	public static final float GAMMA = 0.1f;
	
	public static void main(String[] args)
	{
		State env = new StateReduced(0, 0, 5, 5);

		/*for (float epsilon: new float[] {0f, 0.25f, 0.5f, 0.75f, 1f})
		{
			System.out.println("\nepsilon-greedy " + epsilon + ":");
					
			ModelFreeAlgorithm.performanceClear();
					
			for (int i = 0; i < 10; i++)
			{
				System.out.print(i + "...");
				new QLearning(env, ALPHA, GAMMA, epsilon, 15, false, true);
			}
					
			System.out.println("");
			ModelFreeAlgorithm.printPerformance();
		}*/
		
		for (float tau: new float[] {0.1f, 0.2f, 0.3f, 0.4f, 1f})
		{
			System.out.println("\nsoftmax " + tau + ":");
					
			ModelFreeAlgorithm.performanceClear();
					
			for (int i = 0; i < 10; i++)
			{
				System.out.print(i + "...");
				new QLearning(env, ALPHA, GAMMA, tau, 15, true, true);
			}
					
			System.out.println("");
			ModelFreeAlgorithm.printPerformance();
		}
	}
}