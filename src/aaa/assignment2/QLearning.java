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
				// Determine greedy action from Q:
				
				int actionGreedy = 0;
				
				float maxQ = Float.MIN_VALUE;
				
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
						actionGreedy = action;
						
						maxQ = thisQ;
					}
				}
				
				// Epsilon-greedy policy:
				
				int action = 0;
				
				float random = (float) Math.random();
				
				if (random < epsilon)
				{
					float sum = 0;
					
					for (int actionOther: State.AGENT_ACTIONS)
					{
						if (actionOther != actionGreedy)
						{
							sum += epsilon / (State.AGENT_ACTIONS.length - 1);
							
							if (sum > random)
							{
								action = actionOther;
							}
						}
					}
				}
				else
				{
					action = actionGreedy;
				}
				
				// Q-value update:
				
				StateActionPair sa = new StateActionPair(s, action);
				
				State sPrime = (State) s.clone();
				
				sPrime.move(predator, action);
				sPrime.move(prey);
				
				maxQ = Float.MIN_VALUE;
				
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
			
			float maxQ = Float.MIN_VALUE;
			
			int actionGreedy = State.AGENT_MOVE_STAY;
			
			for (int action: State.AGENT_ACTIONS)
			{
				StateActionPair sa = new StateActionPair(s, action);
				
				if (!Q.containsKey(sa))
				{
					Q.put(sa, valueInitial);
				}
				
				float thisQ = Q.get(new StateActionPair(s, action));
				
				if (thisQ > maxQ)
				{
					maxQ = thisQ;
					
					actionGreedy = action;
				}
			}
			
			List<Integer> l = new ArrayList<Integer>();
			l.add(actionGreedy);
			
			pi.put(s, l);
		}
		
		return AgentUtils.buildPredator(pi);
	}
}
