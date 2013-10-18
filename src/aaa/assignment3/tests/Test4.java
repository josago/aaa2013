package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.StateMulti;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test4
{
	public static final int NUM_PREDATORS   = 1;
	public static final int NUM_THREADS     = 10;
	
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 0.1f;
	public static final float VALUE_INITIAL = 15;

	public static void main(String[] args)
	{
		// Initialization:
		
		System.out.println("Test4...");
		
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		// Q-Learning:
		
		for (float valueInitial: new float[] {30, 15, 0, -15})
		{
			QLearningMulti.performanceClear();
			
			List<QLearningMultiThread> t = new ArrayList<QLearningMultiThread>();
			
			for (int i = 0; i < NUM_THREADS; i++)
			{
				QLearningMultiThread ql = new QLearningMultiThread(env, prey, predators, valueInitial);
				ql.start();
				
				t.add(ql);
			}
			
			for (QLearningMultiThread ql: t)
			{
				try {
					ql.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("\nvalueInitial = " + valueInitial);
			QLearningMulti.printPerformance();
		}
	}
	
	static class QLearningMultiThread extends Thread
	{
		private final StateMulti env;
		private final Agent prey;
		private final List<Agent> predators;
		
		private final float valueInitial;
		
		public QLearningMultiThread(StateMulti env, Agent prey, List<Agent> predators, float valueInitial)
		{
			this.env = env;
			this.prey = prey;
			this.predators = predators;
			
			this.valueInitial = valueInitial;
		}
		
		@Override
		public void run()
		{
			new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, valueInitial, false, true);
		}
	}
}
