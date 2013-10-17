package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment2.StateActionPair;
import aaa.assignment2.algorithms.ModelFreeAlgorithm;
import aaa.assignment3.*;

public class QLearningMulti extends ModelFreeAlgorithm
{
	public static final int NUM_EPISODES  = 40000;
	public static final int TEST_NUM_RUNS = 100;
	
	@SuppressWarnings("unchecked")
	private final HashMap<StateActionPair, Float>[] Q = new HashMap[2];
	
	private static final HashMap<Integer, List<Float>> performance = new HashMap<Integer, List<Float>>();
	
	protected QLearningMulti()
	{
		
	}
	
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
					
					Q[agent.getType()].put(sa, newQ);
				}
			}

			if (wantPerformance && i % 400 == 0)
			{
				performanceAdd(i, prey, predators);
			}
		}
	}
	
	public Agent buildAgent(Agent agent)
	{
		return new AgentSparse(agent.getType(), valueInitial, Q[agent.getType()]);
	}
	
	protected void performanceAdd(int iterations, Agent prey, List<Agent> predators)
	{
		Agent preyNew = buildAgent(prey);
		List<Agent> predatorsNew = new ArrayList<Agent>();
		
		for (Agent predator: predators)
		{
			predatorsNew.add(buildAgent(predator));
		}
		
		StateMulti env = new StateMulti(preyNew, preyNew, predatorsNew);
		
		synchronized (performance)
		{
			if (!performance.containsKey(iterations))
			{
				performance.put(iterations, new ArrayList<Float>());
			}
		}
			
		for (int r = 0; r < TEST_NUM_RUNS; r++)
		{
			StateMulti state = (StateMulti) env.clone();
			
			int turns  = SimulatorMulti.runSimulation(state, preyNew, predatorsNew, 0, false);
			int reward = state.getReward(predatorsNew.get(0));
				
			synchronized (performance)
			{
				performance.get(iterations).add((float) (reward * Math.pow(0.9, turns - 1)));
			}
		}
		
		synchronized (performance)
		{
			List<Float> terms = performance.get(iterations);

			float sum = 0;
				
			for (float term: terms)
			{
				sum += term;
			}
				
			System.out.print("(" + iterations + ", " + sum / terms.size() + ") ");
		}
	}
	
	public static void performanceClear()
	{
		performance.clear();
	}
	
	public static void printPerformance()
	{
		for (int i = 0; i < NUM_EPISODES; i += 400)
		{
			float sum = 0;
			
			List<Float> terms = performance.get(i);
			
			for (float term: terms)
			{
				sum += term;
			}
			
			System.out.print("(" + i + ", " + sum / terms.size() + ") ");
		}
	}
}
