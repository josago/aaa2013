package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment2.algorithms.QLearning;

public class Test2
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		for (float valueInitial: new float[] {30f, 15f, 0f, -15f})
		{
			for (float epsilon: new float[] {0.1f, 0.4f, 0.7f, 1f})
			{
				System.out.println("\n\\epsilon = " + epsilon + ", valueInitial = " + valueInitial);
				
				ModelFreeAlgorithm.performanceClear();
				
				Thread[] t = new Thread[10];
				
				for (int i = 0; i < 10; i++)
				{
					t[i] = new runAlgorithm2(valueInitial, epsilon);
					t[i].start();
				}
				
				for (int i = 0; i < 10; i++)
				{
					try {
						t[i].join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				ModelFreeAlgorithm.printPerformance();
			}
		}
	}
}

class runAlgorithm2 extends Thread
{
	private aaa.State env = new StateReduced(0, 0, 5, 5);
	
	private final float valueInitial, epsilon;
	
	public runAlgorithm2(float valueInitial, float epsilon)
	{
		this.valueInitial = valueInitial;
		this.epsilon = epsilon;
	}
	
	public void run()
	{
		new QLearning(env, 0.1f, 0.1f, epsilon, valueInitial, false, true);
	}
}