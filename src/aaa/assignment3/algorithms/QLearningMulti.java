package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment3.StateMulti;

public class QLearningMulti extends ModelFreeAlgorithm
{
	public static final int NUM_EPISODES = 10000;
	
	private final HashMap<Agent, HashMap<StateActionPair, Float>> Q = new HashMap<Agent, HashMap<StateActionPair, Float>>();
	
	public QLearningMulti(StateMulti env, Agent prey, List<Agent> predators, float alpha, float gamma, float epsilon, float valueInitial, boolean useSoftmax, boolean wantPerformance)
	{
		// Initialization of variables:
		
		super.valueInitial = valueInitial;
		super.env = env;
		
		Q.put(prey, new HashMap<StateActionPair, Float>());
		
		for (Agent predator: predators)
		{
			Q.put(predator, new HashMap<StateActionPair, Float>());
		}
		
		// Q-Learning:
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			if (i % 100 == 0)
			{
				System.out.println(i);
			}
			
			StateMulti s = (StateMulti) env.clone();
			
			while (!s.isFinal())
			{
				StateMulti state = (StateMulti) s.clone(); // state -> State before movements
				HashMap<Agent, Integer> actions = new HashMap<Agent, Integer>();
				
				for (Agent agent: Q.keySet())
				{
					HashMap<StateActionPair, Float> q = Q.get(agent);
					
					// Action selection:
					
					int action;
					
					if (useSoftmax)
					{
						action = softmax(q, state, epsilon); // epsilon used as tau when useSoftmax is true.
					}
					else
					{
						action = epsilonGreedy(q, state, epsilon);
					}
					
					s.move(agent, action); // s -> State after movements
					actions.put(agent, action);
				}
					
				for (Agent agent: Q.keySet())
				{
					HashMap<StateActionPair, Float> q = Q.get(agent);
					
					// Q-value update:
					
					StateActionPair sa = new StateActionPair(state, actions.get(agent));
					
					if (!q.containsKey(sa))
					{
						q.put(sa, valueInitial);
					}
					
					float maxQ = Float.MIN_VALUE;
					
					for (int a: State.AGENT_ACTIONS)
					{
						StateActionPair sa2 = new StateActionPair(s, a);
						
						if (!q.containsKey(sa2))
						{
							q.put(sa2, valueInitial);
						}
						
						maxQ = Math.max(q.get(sa2), maxQ);
					}
					
					float R = s.isFinal() ? s.getReward(agent) : gamma * maxQ;
					
					float oldQ = q.get(sa);
					float newQ = oldQ + alpha * (R - oldQ);
					
					q.put(sa, newQ);
				}
			}

			if (wantPerformance && i % 10 == 0)
			{
				for (Agent agent: Q.keySet())
				{
					if (agent.getType() == Agent.TYPE_PREDATOR)
					{
						performanceAdd(i, env, buildAgent());
						
						break;
					}
				}
			}
		}
	}
	
	public Agent buildAgent(Agent agent)
	{
		return new AgentSparse(agent.getType(), valueInitial, Q.get(agent));
	}
}
