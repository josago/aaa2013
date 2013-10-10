package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment3.StateMulti;

public class QLearningMulti extends ModelFreeAlgorithm
{
	public static final int NUM_EPISODES = 10000;
	
	@SuppressWarnings("unchecked")
	private final HashMap<StateActionPair, Float>[] Q = new HashMap[2];
	
	public QLearningMulti(StateMulti env, Agent prey, List<Agent> predators, float alpha, float gamma, float epsilon, float valueInitial, boolean useSoftmax, boolean wantPerformance)
	{
		// Initialization of variables:
		
		Q[Agent.TYPE_PREDATOR] = new HashMap<StateActionPair, Float>();
		Q[Agent.TYPE_PREY]     = new HashMap<StateActionPair, Float>();
		
		super.valueInitial = valueInitial;
		super.env = env;
		
		List<Agent> allAgents = new ArrayList<Agent>(predators);
		allAgents.add(prey);
		
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
				
				for (Agent agent: allAgents)
				{
					state.changeViewPoint(agent);
					
					// Action selection:
					
					int action;
					
					if (useSoftmax)
					{
						action = softmax(Q[agent.getType()], state, epsilon); // epsilon used as tau when useSoftmax is true.
					}
					else
					{
						action = epsilonGreedy(Q[agent.getType()], state, epsilon);
					}
					
					s.move(agent, action); // s -> State after movements
					actions.put(agent, action);
				}
					
				for (Agent agent: allAgents)
				{
					s.changeViewPoint(agent);
					state.changeViewPoint(agent);
					
					// Q-value update:
					
					StateActionPair sa = new StateActionPair((StateMulti) state.clone(), actions.get(agent));
					
					if (!Q[agent.getType()].containsKey(sa))
					{
						Q[agent.getType()].put(sa, valueInitial);
					}
					
					float maxQ = Float.MIN_VALUE;
					
					for (int a: State.AGENT_ACTIONS)
					{
						StateActionPair sa2 = new StateActionPair((StateMulti) s.clone(), a);
						
						if (!Q[agent.getType()].containsKey(sa2))
						{
							Q[agent.getType()].put(sa2, valueInitial);
						}
						
						maxQ = Math.max(Q[agent.getType()].get(sa2), maxQ);
					}
					
					float R = s.isFinal() ? s.getReward(agent) : gamma * maxQ;
					
					float oldQ = Q[agent.getType()].get(sa);
					float newQ = oldQ + alpha * (R - oldQ);
					if (agent.getType() == agent.TYPE_PREDATOR) System.out.println(newQ);
					Q[agent.getType()].put(sa, newQ);
				}
			}

			if (wantPerformance && i % 10 == 0)
			{
				performanceAdd(i, env, buildAgent(predators.get(0)));
			}
		}
	}
	
	public Agent buildAgent(Agent agent)
	{
		return new AgentSparse(agent.getType(), valueInitial, Q[agent.getType()]);
	}
}
