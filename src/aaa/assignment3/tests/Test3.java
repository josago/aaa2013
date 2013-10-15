package aaa.assignment3.tests;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;
import aaa.assignment3.algorithms.QLearningMulti;

public class Test3
{
	public static final int NUM_PREDATORS   = 2;
	public static final int NUM_THREADS     = 8;
	public static final int NUM_SIMULATIONS = 125; // (per thread)
	
	public static final float ALPHA         = 0.9f;
	public static final float GAMMA         = 0.9f;
	public static final float EPSILON       = 0.1f;
	public static final float VALUE_INITIAL = 15;
	
	public static void main(String[] args)
	{
		Agent prey = new PreySimple();
		List<Agent> predators = new ArrayList<Agent>();
		
		for (int i = 0; i < NUM_PREDATORS; i++)
		{
			predators.add(new PredatorRandom());
		}
		
		StateMulti     env = new StateMulti(prey, prey, predators);
		QLearningMulti ql  = new QLearningMulti(env, prey, predators, ALPHA, GAMMA, EPSILON, VALUE_INITIAL, false, false);
		
		Agent preyNew = ql.buildAgent(prey);
		List<Agent> predatorsNew = new ArrayList<Agent>();
		
		for (Agent predator: predators)
		{
			predatorsNew.add(ql.buildAgent(predator));
		}
		
		// Fast simulations (no waiting between game steps):
		
		double score[] = new double[] {0.0};
		
		List<SimulatorThread> threads = new ArrayList<SimulatorThread>();
		
		for (int r = 0; r < NUM_THREADS; r++)
		{
			env = new StateMulti(preyNew, preyNew, predatorsNew);
			
			SimulatorThread thread = new SimulatorThread(env, preyNew, predatorsNew, score);
			
			threads.add(thread);
			thread.start();
		}
		
		for (Thread thread: threads)
		{
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Mean game score calculation:
				
		score[0] /= (NUM_SIMULATIONS * NUM_THREADS);
				
		System.out.println("Average score of " + (NUM_SIMULATIONS * NUM_THREADS) + " games: " + score[0] + " points.");
	}
	
	static class SimulatorThread extends Thread
	{
		private final StateMulti env;
		private final Agent prey;
		private final List<Agent> predators;
		
		private final double[] score;
		
		public SimulatorThread(StateMulti env, Agent prey, List<Agent> predators, double[] score)
		{
			this.env       = env;
			this.prey      = prey;
			this.predators = predators;
			
			this.score = score;
		}
		
		@Override
		public void run()
		{
			for (int r = 0; r < NUM_SIMULATIONS; r++)
			{
				StateMulti s = (StateMulti) env.clone();
				
				int   length = SimulatorMulti.runSimulation(s, prey, predators, 0, false);
				float reward = s.getReward(predators.get(0));
				
				synchronized (score)
				{
					score[0] += reward * Math.pow(GAMMA, length - 1);
				}
			}
		}
	}
}
