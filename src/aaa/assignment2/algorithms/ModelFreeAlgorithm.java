package aaa.assignment2.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;

public abstract class ModelFreeAlgorithm
{
	public static final int NUM_EPISODES = 10000;
	
	protected final HashMap<StateActionPair, Float> Q = new HashMap<StateActionPair, Float>();
	
	protected float valueInitial;
	protected State env;
	
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
}
