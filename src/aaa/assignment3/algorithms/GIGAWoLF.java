package aaa.assignment3.algorithms;

import java.util.*;

import aaa.*;
import aaa.assignment3.*;

public class GIGAWoLF extends QLearningMulti
{
	public static final int NUM_EPISODES = 100000;
	
	@SuppressWarnings("unchecked")
	private final HashMap<State, HashMap<Integer, Float>> X[] = new HashMap[2];
	@SuppressWarnings("unchecked")
	private final HashMap<State, HashMap<Integer, Float>> Z[] = new HashMap[2];
	
	public GIGAWoLF(Agent prey, List<Agent> predators, float gamma, float eta, float decay, boolean wantPerformance)
	{
		// Initialization of variables:
		
		X[Agent.TYPE_PREDATOR] = new HashMap<State, HashMap<Integer, Float>>();
		Z[Agent.TYPE_PREDATOR] = new HashMap<State, HashMap<Integer, Float>>();
		
		X[Agent.TYPE_PREY] = new HashMap<State, HashMap<Integer, Float>>();
		Z[Agent.TYPE_PREY] = new HashMap<State, HashMap<Integer, Float>>();
		
		StateMulti env = new StateMulti(prey, prey, predators);
		
		List<Agent> agents = new ArrayList<Agent>(predators);
		agents.add(prey);
		
		for (int i = 0; i < NUM_EPISODES; i++)
		{
			// System.out.println(X[Agent.TYPE_PREY].size() + " vs " + X[Agent.TYPE_PREDATOR].size());
			
			StateMulti s = (StateMulti) env.clone();
			
			List<StateMulti> states = new ArrayList<StateMulti>();
			
			HashMap<Agent, List<Integer>> actions = new HashMap<Agent, List<Integer>>();
			
			for (Agent agent: agents)
			{
				actions.put(agent, new ArrayList<Integer>());
			}
			
			// Episode generation:
			
			int steps = 0;
			
			while (!s.isFinal() && steps < SimulatorMulti.TURNS_LIMIT)
			{
				states.add((StateMulti) s.clone());
				
				StateMulti sPrime = (StateMulti) s.clone();
				
				for (Agent agent: agents)
				{
					s.changeViewPoint(agent);
					
					int action = getAction(getPi(X[agent.getType()], s));
					actions.get(agent).add(action);
					
					sPrime.move(agent, action);
				}
				
				s = sPrime;
				steps++;
			}
			
			int rewardPrey = s.getReward(prey);
			
			// GIGA-WoLF:
			
			int t = 0;
			
			for (StateMulti state: states)
			{
				for (Agent agent: agents)
				{
					int   action = actions.get(agent).get(t);
					float reward = (float) (rewardPrey * Math.pow(gamma, states.size() - 1 - t));
					
					if (reward != 0)
					{
						if (agent.getType() == Agent.TYPE_PREDATOR)
						{
							reward *= -1;
						}
						
						state.changeViewPoint(agent);
						
						// Step (1):
						
						HashMap<Integer, Float> xOld = getPi(X[agent.getType()], state);
						
						HashMap<Integer, Float> xTemp = clonePi(xOld);
						xTemp.put(action, xOld.get(action) + eta * reward);
						normalizePi(xTemp);
						
						// Step (2):
						
						HashMap<Integer, Float> zOld = getPi(Z[agent.getType()], state);
						
						HashMap<Integer, Float> zNew = clonePi(zOld);
						zNew.put(action, zOld.get(action) + (eta * reward) / 3);
						normalizePi(zNew);
						
						Z[agent.getType()].put((State) state.clone(), zNew);
						
						float delta = Math.min(1, normPi(diffPi(zNew, zOld)) / normPi(diffPi(zNew, xTemp)));
						
						if (Float.isNaN(delta))
						{
							delta = 1;
						}
						
						// Step (3):
						
						HashMap<Integer, Float> xNew = sumPi(clonePi(xTemp), prodPi(diffPi(zNew, xTemp), delta));
						
						X[agent.getType()].put((State) state.clone(), xNew);
					}
				}
				
				t++;
			}
			
			eta *= decay;
			
			// Performance graph plot:
			
			if (wantPerformance && i % 400 == 0)
			{
				performanceAdd(i, prey, predators);
			}
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
				
				table = X[agent.getType()];
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
			
			table.put((State) s.clone(), pi);
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
		float min = Float.POSITIVE_INFINITY;
		
		for (float prob: pi.values())
		{
			min = Math.min(min, prob);
		}
		
		for (float prob: pi.values())
		{
			if (min < 0)
			{
				sum += prob - min;
			}
			else
			{
				sum += prob;
			}
		}
		
		for (int action: State.AGENT_ACTIONS)
		{
			if (sum == 0)
			{
				pi.put(action, 1.0f / State.AGENT_ACTIONS.length);
			}
			else if (min < 0)
			{
				pi.put(action, (pi.get(action) - min) / sum);
			}
			else
			{
				pi.put(action, pi.get(action) / sum);
			}
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
