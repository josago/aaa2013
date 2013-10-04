package aaa.assignment2.tests;

import aaa.*;
import aaa.assignment2.algorithms.*;

public class Test1
{
	public static final int TEST_NUM_RUNS = 1000;
	
	public static void main(String[] args)
	{
		for (float ALPHA: new float[] {0.1f, 0.2f, 0.3f, 0.4f, 0.5f})
		{
			for (float GAMMA: new float[] {0.1f, 0.3f, 0.5f, 0.7f, 0.9f})
			{
				System.out.println("\n\\alpha = " + ALPHA + ", \\gamma = " + GAMMA);
				
				ModelFreeAlgorithm.performanceClear();
				
				Thread[] t = new Thread[10];
				
				for (int i = 0; i < 10; i++)
				{
					t[i] = new runAlgorithm(ALPHA, GAMMA);
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

class runAlgorithm extends Thread
{
	private aaa.State env = new StateReduced(0, 0, 5, 5);
	
	private final float ALPHA, GAMMA;
	
	public runAlgorithm(float ALPHA, float GAMMA)
	{
		this.ALPHA = ALPHA;
		this.GAMMA = GAMMA;
	}
	
	public void run()
	{
		new QLearning(env, this.ALPHA, this.GAMMA, 0.1f, 15, false, true);
	}
}
