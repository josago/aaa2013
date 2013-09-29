package aaa.assignment2;

import java.util.*;

import aaa.*;

public class QLearning
{
	public static final int NUM_EPISODES = 10000;
	
	private final HashMap<StateActionPair, Float> Q;
	
	private final float valueInitial;
	private final State env;
	
	public QLearning(State env, float alpha, float gamma, float epsilon, float valueInitial)
	{
		Agent prey     = new PreySimple();
		Agent predator = new PredatorRandom();
	
		this.valueInitial = valueInitial;
		this.env = env;
		
		Q = new HashMap<StateActionPair, Float>();
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			State s = (State) env.clone();
			
			while (!s.isFinal())
			{
				// Determine greedy action(s) from Q:
				
				List<Integer> actionsGreedy = greedyActions(s);
				
				int actionsOther = State.AGENT_ACTIONS.length - actionsGreedy.size();
				
				// Epsilon-greedy policy:
				
				int action = 0;
				
				float random = (float) Math.random();
				
				if (random < epsilon && actionsOther > 0)
				{
					float sum = 0;
					
					for (int actionOther: State.AGENT_ACTIONS)
					{
						if (!actionsGreedy.contains(actionOther))
						{
							sum += epsilon / actionsOther;
							
							if (sum > random)
							{
								action = actionOther;
							}
						}
					}
				}
				else
				{
					action = actionsGreedy.get(0);
				}
				
				// int action = softmax(s, epsilon);
				
				// Q-value update:
				
				StateActionPair sa = new StateActionPair(s, action);
				
				State sPrime = (State) s.clone();
				
				sPrime.move(predator, action);
				sPrime.move(prey);
				
				float maxQ = Float.MIN_VALUE;
				
				for (int a: State.AGENT_ACTIONS)
				{
					StateActionPair sa2 = new StateActionPair(sPrime, a);
					
					if (!Q.containsKey(sa2))
					{
						Q.put(sa2, valueInitial);
					
					}
					
					maxQ = Math.max(Q.get(sa2), maxQ);
				}
				
				float r = s.isFinal() ? 10 : 0;
				
				float oldQ = Q.get(sa);
				float newQ = oldQ + alpha * (r + gamma * maxQ - oldQ);
				
				Q.put(sa, newQ);
				
				s = sPrime;
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
