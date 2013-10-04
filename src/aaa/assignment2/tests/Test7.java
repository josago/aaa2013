package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.*;

public class Test7
{
	public static final float ALPHA = 0.1f;
	public static final float GAMMA = 0.1f;
	
	public static void main(String[] args)
	{
		for (float epsilon: new float[] {0f, 0.25f, 0.5f, 0.75f, 1f})
		{
			System.out.println("\nepsilon-greedy " + epsilon + ":");
					
			ModelFreeAlgorithm.performanceClear();
			
			Thread[] t = new Thread[10];
			
			for (int i = 0; i < 10; i++)
			{
				t[i] = new runAlgorithm7(epsilon, false);
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
		
		for (float tau: new float[] {0.1f, 1f, 10f})
		{
			System.out.println("\nsoftmax " + tau + ":");
					
			ModelFreeAlgorithm.performanceClear();
			
			Thread[] t = new Thread[10];
			
			for (int i = 0; i < 10; i++)
			{
				t[i] = new runAlgorithm7(tau, true);
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

class runAlgorithm7 extends Thread
{
	private aaa.State env = new StateReduced(0, 0, 5, 5);
	
	private final float param;
	private final boolean useSoftmax;
	
	public runAlgorithm7(float param, boolean useSoftmax)
	{
		this.param = param;
		this.useSoftmax = useSoftmax;
	}
	
	public void run()
	{
		new QLearning(env, 0.1f, 0.1f, param, 15, useSoftmax, true);
	}
}