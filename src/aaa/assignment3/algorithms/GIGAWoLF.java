package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;

public class GIGAWoLF
{
	public static final int NUM_EPISODES = 40000;
	
	private final HashMap<State, HashMap<Integer, Float>> preyX, preyZ;
	private final HashMap<State, HashMap<Integer, Float>> predatorsX, predatorsZ;
	
	public GIGAWoLF(Agent prey, List<Agent> predators, float gamma, float eta, float decay)
	{
		// Initialization of variables:
		
		preyX = new HashMap<State, HashMap<Integer, Float>>();
		preyZ = new HashMap<State, HashMap<Integer, Float>>();
		
		predatorsX = new HashMap<State, HashMap<Integer, Float>>();
		predatorsZ = new HashMap<State, HashMap<Integer, Float>>();
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		List<Agent> agents = new ArrayList<Agent>(predators);
		agents.add(prey);
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			StateMulti s = (StateMulti) env.clone();
			
			List<StateMulti> states = new ArrayList<StateMulti>();
			
			HashMap<Agent, List<Integer>> actions = new HashMap<Agent, List<Integer>>();
			
			for (Agent agent: agents)
			{
				actions.put(agent, new ArrayList<Integer>());
			}
			
			// Episode generation:
			
			while (!s.isFinal())
			{
				states.add((StateMulti) s.clone());
				
				StateMulti sPrime = (StateMulti) s.clone();
				
				for (Agent agent: agents)
				{
					HashMap<State, HashMap<Integer, Float>> table = agent.getType() == Agent.TYPE_PREDATOR ? predatorsX : preyX;
					
					int action = getAction(getPi(table, s));
					actions.get(agent).add(action);
					
					sPrime.move(agent, action);
				}
				
				s = sPrime;
			}
			
			int rewardPrey = s.getReward(prey);
			
			// GIGA-WoLF:
			
			for (StateMulti state: states)
			{
				for (Agent agent: agents)
				{
					int   action = actions.get(agent).remove(0);
					float reward = (float) (rewardPrey * Math.pow(gamma, states.size() - 1));
					
					if (agent.getType() == Agent.TYPE_PREDATOR)
					{
						reward *= -1;
					}
					
					// Step (1):
					
					HashMap<State, HashMap<Integer, Float>> table = agent.getType() == Agent.TYPE_PREDATOR ? predatorsX : preyX;
					
					HashMap<Integer, Float> xOld = getPi(table, state);
					
					HashMap<Integer, Float> xTemp = clonePi(xOld);
					xTemp.put(action, xTemp.get(action) + eta * reward);
					normalizePi(xTemp);
					
					// Step (2):
					
					table = agent.getType() == Agent.TYPE_PREDATOR ? predatorsZ : preyZ;
					
					HashMap<Integer, Float> zOld = getPi(table, state);
					
					HashMap<Integer, Float> zNew = clonePi(xOld);
					zNew.put(action, zNew.get(action) + (eta * reward) / 3);
					normalizePi(zNew);
					
					float delta = Math.min(1, normPi(diffPi(zNew, zOld)) / normPi(diffPi(zNew, xTemp)));
					
					// Step (3):
					
					HashMap<Integer, Float> xNew = sumPi(clonePi(xTemp), prodPi(diffPi(zNew, xTemp), delta));
					
					table = agent.getType() == Agent.TYPE_PREDATOR ? predatorsX : preyX;
					
					table.put(state, xNew);
				}
			}
			
			eta *= decay;
		}
	}
	
	public Agent buildAgent(Agent agent)
	{
		class GIGAWoLFAgent implements Agent
		{
			private final Agent agent;
			private final HashMap<State, HashMap<Integer, Float>> table;
			
			public GIGAWoLFAgent(Agent agent)
			{
				this.agent = agent;
				
				table = agent.getType() == Agent.TYPE_PREDATOR ? predatorsX : preyX;
			}
			
			@Override
			public int getType()
			{
				return agent.getType();
			}

			@Override
			public String getSymbol()
			{
				return agent.getSymbol();
			}

			@Override
			public float pi(State env, int action)
			{
				if (!table.containsKey(env))
				{
					return 1.0f / State.AGENT_ACTIONS.length;
				}
				else
				{
					return table.get(env).get(action);
				}
			}
			
		}
		
		return new GIGAWoLFAgent(agent);
	}
	
	private int getAction(HashMap<Integer, Float> pi)
	{
		double random = Math.random();
		
		float sum = 0;
		
		for (int action: State.AGENT_ACTIONS)
		{
			sum += pi.get(action);
			
			if (sum > random)
			{
				return action;
			}
		}
		
		return Agent.ACTION_STAY;
	}
	
	private HashMap<Integer, Float> getPi(HashMap<State, HashMap<Integer, Float>> table, State s)
	{
		if (!table.containsKey(s))
		{
			HashMap<Integer, Float> pi = new HashMap<Integer, Float>();
			
			for (int action: State.AGENT_ACTIONS)
			{
				pi.put(action, 1.0f / State.AGENT_ACTIONS.length);
			}
			
			table.put(s, pi);
		}
		
		return table.get(s);
	}
	
	private HashMap<Integer, Float> clonePi(HashMap<Integer, Float> pi)
	{
		HashMap<Integer, Float> clone = new HashMap<Integer, Float>();
		
		for (int action: State.AGENT_ACTIONS)
		{
			clone.put(action, pi.get(action));
		}
		
		return clone;
	}
	
	private void normalizePi(HashMap<Integer, Float> pi)
	{
		float sum = 0;
		
		for (float prob: pi.values())
		{
			sum += prob;
		}
		
		for (int action: State.AGENT_ACTIONS)
		{
			pi.put(action, pi.get(action) / sum);
		}
	}
	
	private HashMap<Integer, Float> sumPi(HashMap<Integer, Float> a, HashMap<Integer, Float> b)
	{
		HashMap<Integer, Float> sum = new HashMap<Integer, Float>();
		
		for (int action: State.AGENT_ACTIONS)
		{
			sum.put(action, a.get(action) + b.get(action));
		}
		
		return sum;
	}
	
	private HashMap<Integer, Float> diffPi(HashMap<Integer, Float> a, HashMap<Integer, Float> b)
	{
		HashMap<Integer, Float> diff = new HashMap<Integer, Float>();
		
		for (int action: State.AGENT_ACTIONS)
		{
			diff.put(action, a.get(action) - b.get(action));
		}
		
		return diff;
	}
	
	private HashMap<Integer, Float> prodPi(HashMap<Integer, Float> pi, float c)
	{
		HashMap<Integer, Float> prod = new HashMap<Integer, Float>();
		
		for (int action: State.AGENT_ACTIONS)
		{
			prod.put(action, pi.get(action) * c);
		}
		
		return prod;
	}
	
	private float normPi(HashMap<Integer, Float> pi)
	{
		float sum = 0;
		
		for (int action: State.AGENT_ACTIONS)
		{
			sum += Math.pow(pi.get(action), 2);
		}
		
		return (float) Math.sqrt(sum);
	}
}
