package aaa.assignment2.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;

public abstract class ModelFreeAlgorithm
{
	public static final int NUM_EPISODES  = 3000;
	public static final int TEST_NUM_RUNS = 1000;
	
	protected final HashMap<StateActionPair, Float> Q = new HashMap<StateActionPair, Float>();
	
	protected float valueInitial;
	protected State env;
	
	private static final HashMap<Integer, List<Integer>> performance = new HashMap<Integer, List<Integer>>();
	
	public Agent buildAgent()
	{
		HashMap<State, List<Integer>> pi = new HashMap<State, List<Integer>>();
		
		Iterator<State> it = env.stateIterator();
		
		while (it.hasNext())
		{
			State s = it.next();
			
			pi.put(s, greedyActions(s));
		}
		
		return AgentUtils.buildPredator(pi);
	}
	
	protected List<Integer> greedyActions(State s)
	{
		
		List<Integer> actions = new ArrayList<Integer>();
		
		float maxQ = Float.NEGATIVE_INFINITY;
		
		for (int action: State.AGENT_ACTIONS)
		{
			StateActionPair sa = new StateActionPair(s, action);
			
			if (!Q.containsKey(sa))
			{
				Q.put(sa, valueInitial);
			}
			
			float thisQ = Q.get(sa);
			
			if (thisQ > maxQ)
			{
				actions.clear();
				actions.add(action);
				
				maxQ = thisQ;
			}
			if (thisQ == maxQ)
			{
				actions.add(action);
			}
		}
		
		Collections.shuffle(actions);
		
		return actions;
	}
	
	protected int epsilonGreedy(State s, float epsilon)
	{
		// Determine greedy action(s) from Q:
		
		List<Integer> actionsGreedy = greedyActions(s);
		
		// Epsilon-greedy policy:
		
		float random = (float) Math.random();
		
		if (random < epsilon)
		{
			float sum = 0;
			
			for (int actionOther: State.AGENT_ACTIONS)
			{
				sum += epsilon / State.AGENT_ACTIONS.length;
					
				if (sum > random)
				{
					return actionOther;
				}
			}
		}
		else
		{
			return actionsGreedy.get(0);
		}
		
		return State.AGENT_MOVE_STAY;
	}
	
	protected int softmax(State s, float tau)
	{
		float total = 0;
		
		HashMap<Integer, Float> probs = new HashMap<Integer, Float>();
		
		for (int a: State.AGENT_ACTIONS)
		{
			float prob = (float) Math.exp(Q.get(new StateActionPair(s, a)) / tau);
			
			probs.put(a, prob);
			
			total += prob;
		}
		
		float random = (float) Math.random() * total;
		
		float sum = 0;
		
		for (int a: State.AGENT_ACTIONS)
		{
			sum += probs.get(a);
			
			if (sum >= random)
			{
				return a;
			}
		}
		
		return State.AGENT_MOVE_STAY;
	}
	
	protected void performanceAdd(int iterations)
	{
		if (!performance.containsKey(iterations))
		{
			performance.put(iterations, new ArrayList<Integer>());
		}
		
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			performance.get(iterations).add(Simulator.runSimulation((State) env.clone(), new PreySimple(), buildAgent(), 0, false));
		}
	}
	
	public static void performanceClear()
	{
		performance.clear();
	}
	
	public static void printPerformance()
	{
		for (int i = 0; i < NUM_EPISODES; i += 10)
		{
			long sum = 0;
			
			List<Integer> terms = performance.get(i);
			
			for (int term: terms)
			{
				sum += term;
			}
			
			System.out.print("(" + i + ", " + (double) sum / terms.size() + ") ");
		}
	}
}
