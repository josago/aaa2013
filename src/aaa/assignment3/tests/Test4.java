package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.StateMulti;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test4
{
	public static final int NUM_PREDATORS   = 4;
	public static final int NUM_THREADS     = 10;

	public static void main(String[] args)
	{
		// Initialization:
		
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		// Q-Learning:
		
		QLearningMulti.performanceClear();
		
		Thread[] t = new Thread[NUM_THREADS];
		
		for (int i = 0; i < NUM_THREADS; i++)
		{
			t[i] = new QLearningMultiThread(env, prey, predators);
			t[i].start();
		}
		
		for (int i = 0; i < NUM_THREADS; i++)
		{
			try {
				t[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		QLearningMulti.printPerformance();
	}
}

class QLearningMultiThread extends Thread
{
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 0.1f;
	public static final float VALUE_INITIAL = 15;
	
	private final StateMulti env;
	
	private final Agent prey;
	private final List<Agent> predators;
	
	public QLearningMultiThread(StateMulti env, Agent prey, List<Agent> predators)
	{
		this.env       = env;
		this.prey      = prey;
		this.predators = predators;
	}
	
	public void run()
	{
		new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, VALUE_INITIAL, false, true);
	}
}
