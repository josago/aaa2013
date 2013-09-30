package aaa.assignment2;

import java.util.*;

import aaa.*;

public class Sarsa
{
	public static final int NUM_EPISODES = 10000;
	
	private final HashMap<StateActionPair, Float> Q;
	
	private final float valueInitial;
	private final State env;
	
	public Sarsa(State env, float alpha, float gamma, float epsilon, float valueInitial)
	{
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
	
		this.valueInitial = valueInitial;
		this.env = env;
		
		Q = new HashMap<StateActionPair, Float>();
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			int action = epsilonGreedy(s, epsilon);
			
			while (!s.isFinal())
			{
				State sPrime = (State) s.clone();
				
				sPrime.move(predator, action);
				sPrime.move(prey);
				
				int actionPrime = epsilonGreedy(sPrime, epsilon);
				
				// Q-value update:
				
				StateActionPair sa      = new StateActionPair(s,      action);
				StateActionPair saPrime = new StateActionPair(sPrime, actionPrime);
				
				float r = s.isFinal() ? 10 : 0;
				
				float oldQ = Q.get(sa);
				float newQ = oldQ + alpha * (r + gamma * Q.get(saPrime) - oldQ);
				
				Q.put(sa, newQ);
				
				s      = sPrime;
				action = actionPrime;
			}
		}
	}
	
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
	
	private List<Integer> greedyActions(State s)
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
	
	private int epsilonGreedy(State s, float epsilon)
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
	
	private int softmax(State s, float tau)
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
